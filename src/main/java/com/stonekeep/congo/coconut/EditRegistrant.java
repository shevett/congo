package com.stonekeep.congo.coconut;

import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.Action;
import com.stonekeep.congo.dao.RegistrantDAO;
import com.stonekeep.congo.data.Convention;
import com.stonekeep.congo.data.Registrant;

public class EditRegistrant implements Action, SessionAware {
	private Logger logger = Logger.getLogger(EditRegistrant.class);
	public String savebutton = null;
	public String exitbutton = null;
	public String firstName;
	public String lastName;
	public String company;
	public String badgeName;
	public String password;
	public boolean enabled;
	public String comment;
	private Map<String, Object> sessionData;

	private final RegistrantDAO registrantDAO;

	public EditRegistrant(RegistrantDAO registrantDAO) {
		this.registrantDAO = registrantDAO;

	}

	@Override
	public String execute() throws Exception {
		logger.debug("EditRegistrant called");
		if (savebutton != null) {
			Registrant r = (Registrant) sessionData.get("workingregistrant");
			r.firstName = firstName;
			r.lastName = lastName;
			r.badgeName = badgeName;
			r.company = company;
			r.enabled = enabled;
			r.comment = comment;

			// If they typed a new password, it needs to get saved crypted...
			logger.info("r is " + r);
			logger.info("form password is " + password);
			if (r.password != null) {
				if (!r.password.equalsIgnoreCase(password)) {
					registrantDAO.setPassword(r, password);
				}
			} else {
				registrantDAO.setPassword(r, password);
			}
			registrantDAO.logChanges(r, 
					((Registrant)sessionData.get("coconutuser")).rid, 
					((Convention) sessionData.get("conference")).conCID);
			registrantDAO.save(r);

			return "success";
		} else {
			logger.debug("probably just hit exit");
			return "success";
		}
	}

	@Override
	public void setSession(Map<String, Object> session) {
		logger.debug("Receiving session data...");
		this.sessionData = session;
	}
}
