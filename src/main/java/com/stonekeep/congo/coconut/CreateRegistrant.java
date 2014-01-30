package com.stonekeep.congo.coconut;

import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.Action;
import com.stonekeep.congo.dao.AddressDAO;
import com.stonekeep.congo.dao.EmailDAO;
import com.stonekeep.congo.dao.HistoryDAO;
import com.stonekeep.congo.dao.PhoneDAO;
import com.stonekeep.congo.dao.RegistrantDAO;
import com.stonekeep.congo.data.Address;
import com.stonekeep.congo.data.Email;
import com.stonekeep.congo.data.History;
import com.stonekeep.congo.data.Phone;
import com.stonekeep.congo.data.Registrant;

public class CreateRegistrant implements Action, SessionAware {

	private Map<String, Object> sessionData;
	private Logger logger = Logger.getLogger(CreateRegistrant.class);
	public String lname;
	public String fname;
	public String company;
	public String badgename;
	public String address;
	public String address1;
	public String city;
	public String state;
	public String zipcode;
	public String phonenumber;
	public String email;
	public String createbutton;
	public String message;
	public int rid;
	private final RegistrantDAO registrantDAO;
	private final AddressDAO addressDAO;
	private final EmailDAO emailDAO;
	private final PhoneDAO phoneDAO;
	private final HistoryDAO historyDAO;

	public CreateRegistrant(RegistrantDAO registrantDAO, AddressDAO addressDAO,
			EmailDAO emailDAO, PhoneDAO phoneDAO, HistoryDAO historyDAO) {
		this.registrantDAO = registrantDAO;
		this.addressDAO = addressDAO;
		this.emailDAO = emailDAO;
		this.phoneDAO = phoneDAO;
		this.historyDAO = historyDAO;
	}

	@Override
	public String execute() throws Exception {
		logger.debug("CreateRegistrant starting...");

		if (createbutton != null) {
			Registrant r = registrantDAO.create();
			r.firstName = fname;
			r.lastName = lname;
			r.company = company;
			r.badgeName = badgename;
			r.comment = "";
			r.enabled=true;
			r.mergedTo=0;
			if (badgename.length() < 1) {
				r.badgeName = fname + " " + lname;
			}
			registrantDAO.save(r); // This doesn't create a password, it leaves
			// it null.

			// Save address, phone, and Email info
			Address a = new Address();
			a.line1 = address;
			a.line2 = address1;
			a.city = city;
			a.state = state;
			a.zipcode = zipcode;
			a.rid = r.rid;
			a.primary = true;
			a.location = "Home";
			addressDAO.create(a);

			Phone p = new Phone();
			p.location = "Home";
			p.phone = phonenumber;
			p.rid = r.rid;
			p.primary = true;
			phoneDAO.create(p);

			Email e = new Email();
			e.address = email;
			e.rid = r.rid;
			e.primary = true;
			e.location = "Home";
			emailDAO.create(e);

			sessionData.put("workingregistrant", r);
			logger.debug("Creation done, new rid is " + r.rid);

			// Log the creation.
			Registrant operator = (Registrant) sessionData.get("coconutuser");
			History h = historyDAO.create(r.rid, 0, "CREATE", operator.rid);
			historyDAO.save(h);
			return SUCCESS;
		} else {
			logger.debug("Cancelling create.");
			return "exit";
		}
	}

	@Override
	public void setSession(Map<String, Object> session) {
		logger.debug("Receiving session data...");
		this.sessionData = session;
	}
}
