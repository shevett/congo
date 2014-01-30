package com.stonekeep.congo.coconut;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.Preparable;
import com.stonekeep.congo.dao.HistoryDAO;
import com.stonekeep.congo.dao.InvoiceDAO;
import com.stonekeep.congo.dao.RegistrantDAO;
import com.stonekeep.congo.dao.RegistrationTypeDAO;
import com.stonekeep.congo.dao.StateDAO;
import com.stonekeep.congo.data.Convention;
import com.stonekeep.congo.data.History;
import com.stonekeep.congo.data.Invoice;
import com.stonekeep.congo.data.InvoiceDetail;
import com.stonekeep.congo.data.Registrant;
import com.stonekeep.congo.data.RegistrationType;
import com.stonekeep.congo.data.State;

public class Operations implements Action, Preparable, SessionAware {
	private Logger logger = Logger.getLogger(Operations.class);
	public String eventname;
	public String createbutton;
	public String registerbutton;
	public String voidbutton;
	public String voidreason;
	public String proceedbutton;
	public String reprocessbutton;
	public String transferbutton;
	public String transfertargetinfo = null;
	public int transfertarget;
	public String exitbutton;
	public String regtype;
	public String regprice;
	public String regcomment;
	public String sendto;
	public String message;
	public boolean checkin = false;
	public boolean printbadge = false;
	public Map<String, Object> sessionData;
	private int rid;
	private State s;
	public Map<String, RegistrationType> rtList;
	private final StateDAO stateDAO;
	private final RegistrationTypeDAO registrationTypeDAO;
	private final HistoryDAO historyDAO;
	private final InvoiceDAO invoiceDAO;
	private final RegistrantDAO registrantDAO ;
	private Registrant r;
	private Convention c;
	private Invoice i;
	
	// Injected values we'll need to know about
	public String tmpdir ;
	public String imagedir;
	public String printcommand;
	public String badgeIdentifier;
	public int cid;
	
	public void setTmpdir(String newdir) { this.tmpdir = newdir; }
	public void setImagedir(String imagedir) { this.imagedir = imagedir; }
	public void setPrintcommand(String pc) { this.printcommand = pc;  }
	public void setBadgeIdentifier(String bi) { this.badgeIdentifier = bi; }
	public void setCid(int cid) { this.cid = cid; }

	public Operations(StateDAO stateDAO,
			RegistrationTypeDAO registrationTypeDAO, 
			HistoryDAO historyDAO,
			InvoiceDAO invoiceDAO,
			RegistrantDAO registrantDAO) {
		this.stateDAO = stateDAO;
		this.registrationTypeDAO = registrationTypeDAO;
		this.historyDAO = historyDAO;
		this.invoiceDAO = invoiceDAO;
		this.registrantDAO = registrantDAO;
	}

	@Override
	public String execute() {
		logger.info("Operations...");
		if (sessionData.containsKey("checkin")) {
			checkin = (Boolean)sessionData.get("checkin");
		}
		if (sessionData.containsKey("printbadge")) {
			printbadge = (Boolean)sessionData.get("printbadge");
		}
		return SUCCESS;
	}

	public String voidregistration() throws Exception {
		setupState();
		// This method may now be called after a form prompting for information.  Check to see if
		// the submit button was pressed, or it was cancelled.
		if (voidbutton != null) {
			s.registered = false;
			s.regtype = null;
			s.badged=false;
			s.checkedin=false;
			stateDAO.save(s);
			History h = historyDAO.create(rid, cid, "VOID", r.rid);
			h.operator = r.getRid();
			h.comment = voidreason ;
			historyDAO.save(h);
			logger.debug("Registration for rid " + r.rid + " voided.");
		} else {
			logger.debug("Void cancelled.");
		}
		return SUCCESS;
	}
	
	public String transferregistration() throws Exception {
		setupState();
		logger.debug("Transfer button is " + transferbutton);
		if (transferbutton.equalsIgnoreCase("validate")) {
			logger.debug("Validating new target registrant of " + transfertarget);
			Registrant t = registrantDAO.getByID(transfertarget);
			if (t != null) {
				// Check some basic info...
				State st = stateDAO.getState(cid,transfertarget);
				if (st != null && st.registered) {
					message = "The registrant '" + transfertarget +"' is already registered for this event.";
				} else {
					transfertargetinfo = t.rid + " : " + t.firstName + " " + t.lastName  + "(" + t.badgeName + ") of " + t.company ;
				}
			} else {
				transfertargetinfo = null;
				message = "No such registrant ID found.";
			}
			return INPUT;
		}
		
		// The state on the source registrant may be getting hosed - refresh it just in case.
		s = stateDAO.getState(cid, rid);

		if (transferbutton.equalsIgnoreCase("transfer")) {
			logger.debug("At this point I'd be transferring " + rid + "(regtype is " + s.regtype + ") to " + transfertarget);
			
			// Add the history record to the source showing it was transferred out.
			History h = historyDAO.create(rid, cid, "TRANSFER", r.rid);
			h.comment = "Transfer to " + transfertarget;
			h.arg1 = transfertarget + "";
			historyDAO.save(h);
			
			// Register the new fellow
			State target = stateDAO.getState(cid,transfertarget);
			if (target == null) {
				target = new State();
				target.cid = cid;
				target.rid = transfertarget;
				stateDAO.create(target);
			}
			target.registered = true;
			target.subscribed = true;
			target.regtype = s.regtype;
			stateDAO.save(target);
			
			// Log on the target what happened
			h = historyDAO.create(transfertarget, cid, "REGISTERED", r.rid);
			h.comment = "Transfer in from " + rid;
			historyDAO.save(h);
			
			// Unsubscribe the old registrant.  Bye bye!
			s.registered = false;
			s.regtype = "";
			stateDAO.save(s);
			
		}
		return SUCCESS;
	}

	public String subscribe() throws Exception {
		setupState();
		s.subscribed = true;
		stateDAO.save(s);
		History h = historyDAO.create(rid, cid, "SUBSCRIBE", r.rid);
		historyDAO.save(h);
		return SUCCESS;
	}

	public String unsubscribe() throws Exception {
		setupState();
		s.subscribed = false;
		stateDAO.save(s);
		History h = historyDAO.create(rid, cid, "UNSUB", r.rid);
		historyDAO.save(h);
		return SUCCESS;
	}

	public String checkin() throws Exception {
		setupState();
		s.checkedin = true;
		stateDAO.save(s);
		History h = historyDAO.create(rid, cid, "CHECKEDIN", r.rid);
		historyDAO.save(h);
		return SUCCESS;
	}

	public String uncheckin() throws Exception {
		setupState();
		s.checkedin = false;
		stateDAO.save(s);
		History h = historyDAO.create(rid, cid, "UNCHECKED", r.rid);
		historyDAO.save(h);
		return SUCCESS;
	}
	
	public String setbadged() throws Exception {
		setupState();
		s.badged = true;
		stateDAO.save(s);
		History h = historyDAO.create(rid,cid,"BADGED",r.rid);
		historyDAO.save(h);
		return SUCCESS;
	}

	/*
	 * Process the invoice add screen (RegisterRegistrantForm) 
	 */
	public String invoiceAdd() throws Exception {
		setupState();
		if (registerbutton != null) {
			logger.debug("register button pressed...");
			// Create a new invoice if there isn't one already
			if (i == null) {
				i = invoiceDAO.createInvoice();
				i.status = "READY";
				i.detailList = new ArrayList<InvoiceDetail>();
				logger.debug("Invoice " + i.id + " setting creator to " + rid + " and operator to " + r.rid);
				i.creator = rid ;
				i.operator = r.rid ; 
				invoiceDAO.saveInvoice(i);
			} else {
				// Make sure the invoice that's there is in READY state.
				if (! i.status.equals("READY")) {
					message="Cannot add lines to an invoice that is not READY.  The current invoice is marked as " + i.status;
					return "message";
				}
			}
			InvoiceDetail id = invoiceDAO.createInvoiceDetail();
			id.invoice = i.id;
			id.rid = rid;
			id.cid = cid;
			id.operator = r.rid;
			id.type = "REGISTRATION";
			id.type2 = regtype;
			id.description = "#" + cid + " : " + c.getConName(); 
			logger.debug("Converting regprice value " + regprice + " to bigdecimal...");
			id.amount = new BigDecimal(regprice.trim());
			id.discount = new BigDecimal(0);
			id.finalamount = id.amount;
			id.comment = regcomment ;
			id.sequence = invoiceDAO.getNextSequence(i.id); // calculate the
															// next sequence
			logger.debug("Invoice " + i.id+ " has the following detail record: ");
			logger.debug("-- operator : " + id.operator);
			logger.debug("-- type : " + id.type);
			logger.debug("-- type2 : " + id.type2);
			logger.debug("-- amount : " + id.amount);
			logger.debug("-- finalamount : " + id.finalamount);
			logger.debug("-- sequence : " + id.sequence);
			invoiceDAO.saveInvoiceDetail(id);
			invoiceDAO.recalculate(i.id);
			i = invoiceDAO.getInvoice(i.id);
//			i.detailList.add(id);
			i.detailList = invoiceDAO.listInvoiceDetails(i.id);
			sessionData.put("workinginvoice", i);
		} else if (exitbutton != null) {
			return "exit";
		}

		return SUCCESS;
	}
	
	/**
	 * This method repairs an existing Registrant record by doing internal consistency 
	 * checks and updates.
	 * @return
	 * @throws Exception
	 */
	public String repair() throws Exception {
		setupState();
		logger.debug("Repairing registrant " + rid);
		registrantDAO.repair(rid);
		return SUCCESS;
	}

	private void setupState() throws Exception {
		rid = ((Registrant) sessionData.get("workingregistrant")).rid;
		cid = ((Convention) sessionData.get("conference")).conCID;
		c = (Convention)sessionData.get("conference");
		r = (Registrant) sessionData.get("coconutuser");
		s = stateDAO.getState(cid, rid);
		i = (Invoice) sessionData.get("workinginvoice");

		// For operations, we'll need some other goodies in the context. Load
		// them up too.
		rtList = registrationTypeDAO.list(cid);

	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.sessionData = session;
	}

	@Override
	public void prepare() throws Exception {
		logger.debug("Prepare invoked... This time.");
		setupState();

	}
}
