package com.stonekeep.congo.payments;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;

import org.apache.log4j.Logger;

import com.stonekeep.congo.coconut.SendMail;
import com.stonekeep.congo.dao.EmailDAO;
import com.stonekeep.congo.dao.HistoryDAO;
import com.stonekeep.congo.dao.InvoiceDAO;
import com.stonekeep.congo.dao.PropertyDAO;
import com.stonekeep.congo.dao.RegistrantDAO;
import com.stonekeep.congo.dao.StateDAO;
import com.stonekeep.congo.data.Convention;
import com.stonekeep.congo.data.History;
import com.stonekeep.congo.data.Invoice;
import com.stonekeep.congo.data.InvoiceDetail;
import com.stonekeep.congo.data.PropertyConfiguration;
import com.stonekeep.congo.data.State;

public class InvoiceUtilities {
	private Logger logger = Logger.getLogger(InvoiceUtilities.class);
	private final StateDAO stateDAO;
	private final InvoiceDAO invoiceDAO;
	private final HistoryDAO historyDAO;
	private final PropertyDAO propertyDAO;
	private final EmailDAO emailDAO;
	private final RegistrantDAO registrantDAO;
	private final SendMail sm;
	private Map<String,Object> sessionData;
	public boolean sendMail = false;

	public InvoiceUtilities(StateDAO stateDAO,
			InvoiceDAO invoiceDAO,
			HistoryDAO historyDAO,
			PropertyDAO propertyDAO,
			EmailDAO emailDAO,
			RegistrantDAO registrantDAO,
			SendMail sm) {
		this.invoiceDAO = invoiceDAO;
		this.stateDAO = stateDAO;
		this.historyDAO = historyDAO;
		this.propertyDAO = propertyDAO;
		this.emailDAO = emailDAO;
		this.registrantDAO = registrantDAO;
		this.sm = sm;
	}
	
	/*
	 * take a specific invoice, do the payment processing, and if the payment
	 * went through okay, then do whatever is on the invoice.
	 */
	public void processInvoice(Invoice i,String paymenttype) throws SQLException,Exception {
		logger.debug("Payment type is " + paymenttype);
		logger.debug("Invoice ID is " + i.id);
		boolean shouldSendMail = false;

		// Here we need to process the payment.  Assuming the paying bits go through okay, then
		// complete all the tasks / purchases in the invoice...
		logger.debug("There are " + i.detailList.size() + " invoice detail items to process.");
		for (InvoiceDetail id : i.detailList) {
			logger.debug("Processing seq " + id.sequence + " type code " + id.type);
			if (id.type.equals("REGISTRATION")) {			// a registration
				// Change the state of the registrant...
				State s = stateDAO.getState(id.cid,id.rid);
				if (s == null) {							// no state record exists for this combo.
					logger.debug("This is a new state record.  Creating...");
					s = new State();
					s.cid = id.cid;
					s.rid = id.rid;
					stateDAO.create(s);
				}
				s.subscribed=true;
				s.registered = true;
				s.regtype = id.type2;
				stateDAO.dump(s);
				stateDAO.save(s);
				
				// Record the history...
				
				History h = historyDAO.create(id.rid,id.cid,"REGISTERED",id.rid);
				h.arg1 = id.type2;
				h.comment = "Invoice #" + i.id;
				historyDAO.save(h);
				
				// Generate mail to this person
				
				logger.debug("sendMail is " + sendMail);
				if (sendMail) {
					// Set up the mail call with the invoice item for the current line item
					// and their currente mail, then fire the mail off. 
					if (sessionData.containsKey("conference")) {
						sm.c = (Convention)sessionData.get("conference");
					} else {
						sm.c = (Convention)sessionData.get("currentConvention");
					}
					sm.r = sm.registrantDAO.getByID(id.rid);
					if (sm.emailDAO.getEmail(id.rid) != null) {		// Fix for bug #311 - crash if no email
						sm.target = sm.emailDAO.getEmail(id.rid).address;
						sm.sendMail();
					}
				}
			}
			if (id.type.equals("PROPERTY")) {				// make a property change
				logger.debug("Setting property " + id.type2 + " using value " + id.description);
				PropertyConfiguration p = propertyDAO.getPropertyConfiguration(id.cid, id.type2);
				if (p.type.equals("boolean") && id.description.equals("on")) {
					propertyDAO.setProperty(id.rid,id.cid,id.type2,"1");
				} else {
					propertyDAO.setProperty(id.rid,id.cid,id.type2,id.description);
				}
			}
		}
		// If we're here, we didn't get a crash during invoice update, 
		logger.debug("Processing complete, marking invoice as PAID...");
		i.status="PAID";
		i.paydate = new Timestamp(System.currentTimeMillis());
		i.paytype = paymenttype;
		invoiceDAO.saveInvoice(i);

	}
	
	public void setSession(Map<String, Object> arg0) {
		this.sessionData = arg0;
	}
	
}