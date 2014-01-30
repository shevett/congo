package com.stonekeep.congo.coconut;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.Action;
import com.stonekeep.congo.dao.HistoryDAO;
import com.stonekeep.congo.dao.InvoiceDAO;
import com.stonekeep.congo.dao.RegistrantDAO;
import com.stonekeep.congo.dao.RegistrationTypeDAO;
import com.stonekeep.congo.dao.SettingDAO;
import com.stonekeep.congo.dao.TemplateDAO;
import com.stonekeep.congo.data.Convention;
import com.stonekeep.congo.data.Email;
import com.stonekeep.congo.data.Invoice;
import com.stonekeep.congo.data.InvoiceDetail;
import com.stonekeep.congo.data.Registrant;
import com.stonekeep.congo.data.State;
import com.stonekeep.congo.payments.InvoiceUtilities;
import com.stonekeep.congo.util.SystemBadge;

public class Invoices implements Action, SessionAware {

	private Logger logger = Logger.getLogger(Invoices.class);
	private final InvoiceDAO invoiceDAO;
	private final InvoiceUtilities invoiceUtilities;
	private final RegistrantDAO registrantDAO;
	private final HistoryDAO historyDAO;
	private final Operations operations;
	private final RegistrationTypeDAO registrationTypeDAO;
	private final TemplateDAO templateDAO;
	private final SettingDAO settingDAO;
	private Map<String, Object> sessionData;
	private Invoice i = null;
	public List<Invoice> invoiceList = new ArrayList<Invoice>();
	public String exitbutton = null;
	public String registrantbutton = null;
	public String invoicebutton = null;
	public String proceedbutton = null;
	public String voidbutton = null;
	public String reprocessbutton = null;
	public String skipbutton = null;
	public String message = null;
	public String sendto;
	public String search = "";
	public int skip;
	public boolean checkin;
	public boolean printbadge;
	public int id = 0;
	public String paymenttype = null;
	public int rid = 0;
	private final SendMail sm;
	
	public boolean includevoid = true;
	public boolean includepaid = true;
	public boolean includeready = true;
	
	public void setRid(int newvalue) { this.rid = newvalue; }

	public Invoices(InvoiceDAO invoiceDAO, 
			InvoiceUtilities invoiceUtilities,
			RegistrantDAO registrantDAO,
			HistoryDAO historyDAO,
			Operations operations,
			RegistrationTypeDAO registrationTypeDAO,
			TemplateDAO templateDAO,
			SettingDAO settingDAO,
			SendMail sm) throws SQLException {
		this.invoiceDAO = invoiceDAO;
		this.invoiceUtilities = invoiceUtilities;
		this.registrantDAO = registrantDAO;
		this.historyDAO = historyDAO;
		this.operations = operations;
		this.registrationTypeDAO = registrationTypeDAO ;
		this.templateDAO = templateDAO;
		this.settingDAO = settingDAO;
		this.sm = sm;
	}

	@Override
	public String execute() throws Exception {
		logger.info("execute...");
		return select();
	}

	/*
	 * Load up a list of invoices to select and work with.
	 */
	public String browse() throws Exception {
		logger.debug("Executing Invoices.browse()...");
		int cid = ((Convention) sessionData.get("conference")).conCID;
		if (rid != 0) {
			logger.info("Browsing invoices just for " + rid);
			invoiceList = invoiceDAO.listInvoices(cid,rid);
		} else {
			EnumSet<InvoiceDAO.IncludeTypes> toSelect = EnumSet.noneOf(InvoiceDAO.IncludeTypes.class);
			if (includepaid) toSelect.add(InvoiceDAO.IncludeTypes.PAID);
			if (includeready) toSelect.add(InvoiceDAO.IncludeTypes.READY);
			if (includevoid) toSelect.add(InvoiceDAO.IncludeTypes.VOID);
			invoiceList = invoiceDAO.listInvoices(cid,skip,50,toSelect,search);
		}
		return SUCCESS;
	}

	/*
	 * Process requests from the InvoiceBrowse screen - exit == exit, otherwise
	 * jump to displaying the current invoice.  Return to registrant if requested.
	 */
	public String select() throws Exception {
		logger.debug("select... (exitbutton is " + exitbutton + ", skip is " + skip + ", id is " + id);
		if (exitbutton != null) {
			if (exitbutton.equals("exit")) {
				logger.debug("Exiting browse.");
				return "exit";
			} 
			if (exitbutton.equals("return")) {
				logger.debug("Returning to user");
				return "registrant";
			}
		}
		if (skip > 0) {
			logger.debug("Skip button pressed: " + skip);
			return INPUT;
		}
		if (id > 0) {
			logger.debug("Selected invoice " + id + " - loading and redirecting to viewer.");
			i = invoiceDAO.getInvoice(id);
			if (i != null) {
				i.detailList = invoiceDAO.listInvoiceDetails(id);
				sessionData.put("workinginvoice", i);
				return SUCCESS;
			} else {
				message="Invoice not found.";
				return INPUT;
			}
		} else {
			return INPUT;
		}
	}

	/*
	 * Process the form request.
	 */
	public String process() throws SQLException,Exception {
		logger.debug("sendto is " + sendto);
		logger.debug("process...");
		if (exitbutton != null) {
			logger.debug("Exiting");
		} else if (registrantbutton != null) {
			logger.debug("Jumping to registrant for this invoice");
			i = (Invoice) sessionData.get("workinginvoice");
			// Load up the registrant, and head over there.
			Registrant r = registrantDAO.getByID(i.creator);
			sessionData.put("workingregistrant", r);
			return "registrant";
		} else if (invoicebutton != null) {
			logger.debug("Jumping to Invoice Browse...");
			return "browse";
		} else if (proceedbutton != null) {
			logger.debug("---------------------------------");
			i = (Invoice) sessionData.get("workinginvoice");
			logger.debug("Processing invoice " + i.id + " which has " + i.detailList.size() + " registrants to process.");
			if (i.status.equals("READY")) {
				invoiceUtilities.setSession(sessionData);
				invoiceUtilities.sendMail = false;
				invoiceUtilities.processInvoice(i, paymenttype);
				logger.debug("Checking for email notifications...");
				if (sendto != null) {
					// Do email notifications as requested...
					// First, store away whatever they specified into the session.
					logger.debug("Storing away 'sendto' value as " + sendto);
					sessionData.put("sendto",sendto);
					if (sendto.equals("none")) {
						logger.info("Mail set to 'none'.  Not sending any notifications.");
					}
					// Now send off the mail...
					// This is a bit hacky - it's using the SendMail class I created for
					// mailing in the public interface - but it does process the templates
					// properly.  This will likely need to be refactored.
					if (sendto.equals("registrant")) {
						Convention c = (Convention)sessionData.get("conference");
						sm.c = c;
						sm.r = sm.registrantDAO.getByID(i.creator);;
						sm.target = sm.emailDAO.getEmail(i.creator).address;
						sm.sendMail();
					}
					
					if (sendto.equals("everyone")) {
						// Need to iterate over the invoice detail line items, get the email address, and
						// fire off the mail
						Convention c = (Convention)sessionData.get("conference");
						for (InvoiceDetail id : i.detailList) {
							Email e = sm.emailDAO.getEmail(id.rid);
							// If there's no email for a user, 'e' can come back null.  Allow for that.
							if (e != null) {
								logger.info("Sending mail to " + e.address);
								sm.c = c;
								sm.r = sm.registrantDAO.getByID(id.rid);
								sm.target=e.address;
								sm.sendMail();
							} else {
								logger.warn("No email address for registrant " + id.rid + ".  Skipping.");
							}
						}
					}
				}
				// Okay, if checkin or badged is checked, we need to iterate back through
				// the invoice members and check in / badge each one.  Tally ho!
				logger.debug("Checking to see if a checkin or badging is needed...");
				if (checkin || printbadge) {
					logger.debug("checkin is " + checkin);
					logger.debug("printbadge is " + printbadge);
					logger.debug("Checkin or Badge requested.  Checking invoice contents...");
					SystemBadge sb = new SystemBadge();
					Registrant r ;
					int cid = ((Convention) sessionData.get("conference")).conCID;
					for (InvoiceDetail id : i.detailList) {
						logger.debug("Checking detail row " + id.id + " for registrant " + id.rid);
						r = registrantDAO.getByID(id.rid);
						sessionData.put("workingregistrant",r);
						if (printbadge) {
							logger.debug("Badging " + id.rid);
							registrantDAO.updateCurrentState(r, cid);
							State s = r.currentState ;
							r.currentState.printAs = registrationTypeDAO.getRegistrantType(cid, s.regtype).getRegPrint();
							r.currentState.banner = registrationTypeDAO.getRegistrantType(cid, s.regtype).getRegBanner();
							sb.setRegistrant(r);
							sb.convention = ((Convention) sessionData.get("conference")) ;
							sb.printCommand = operations.printcommand;
							sb.badge = sb.generateBadge();
							sb.printIt();
							operations.sessionData = sessionData;
							operations.setbadged();
						}
						if (checkin) {
							logger.debug("Checking in " + id.rid);
							operations.sessionData = sessionData ;
							operations.checkin();
						}
					}
				}
				logger.debug("---------------------------------");
				// Assuming processing is done, remove the current invoice from the session and
				// story away any required data for future reference.
				sessionData.remove("workinginvoice");
				sessionData.put("checkin",checkin);
				sessionData.put("printbadge",printbadge);
			} else {
				message = "Cannot process an invoice unless it is READY.";
				return INPUT;
			}
		} else if (reprocessbutton != null) {
			logger.debug("Forcing reprocess of invoice...");
			i = (Invoice) sessionData.get("workinginvoice");
			invoiceUtilities.processInvoice(i, paymenttype);
		} else if (voidbutton != null) {
			i = (Invoice) sessionData.get("workinginvoice");
			i.status = "VOID";
			invoiceDAO.saveInvoice(i);
			sessionData.remove("workinginvoice");
		}
		return SUCCESS;
	}
	
	/*
	 * Discard the current working invoice from the session.
	 */
	public String discard() {
		logger
				.debug("Discarding current working invoice from session at users request.");
		sessionData.remove("workinginvoice");
		return SUCCESS;
	}
	
	public String deleteItem() throws SQLException {
		logger.debug("Deleting item number " + id + " from invoice...");
		i = (Invoice) sessionData.get("workinginvoice");
		invoiceDAO.deleteDetailItem(i.id,id);
		
		// Invoice has been udpated, now we need to reload it and recalculate it.
		invoiceDAO.recalculate(i.id);
		i = (Invoice)invoiceDAO.getInvoice(i.id);
		i.detailList = invoiceDAO.listInvoiceDetails(i.id);
		sessionData.put("workinginvoice",i);
		return SUCCESS;
	}
	
	public String markWritein() throws SQLException {
		i = (Invoice) sessionData.get("workinginvoice");
		logger.debug("Marking item number " + id + " from invoice " + i.id);
		InvoiceDetail invoiceDetail = (InvoiceDetail)invoiceDAO.getInvoiceDetail(id);
		invoiceDetail.postprocess = false;
		invoiceDAO.saveInvoiceDetail(invoiceDetail);
		
		// Invoice has been udpated, now we need to reload it and recalculate it.
		invoiceDAO.recalculate(i.id);
		i = (Invoice)invoiceDAO.getInvoice(i.id);
		i.detailList = invoiceDAO.listInvoiceDetails(i.id);
		sessionData.put("workinginvoice",i);
		return SUCCESS;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		sessionData = session;
	}

}
