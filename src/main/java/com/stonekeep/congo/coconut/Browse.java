package com.stonekeep.congo.coconut;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.Action;
import com.stonekeep.congo.dao.RegistrantDAO;
import com.stonekeep.congo.dao.RegistrationTypeDAO;
import com.stonekeep.congo.data.Convention;
import com.stonekeep.congo.data.Registrant;
import com.stonekeep.congo.data.RegistrationType;

public class Browse implements SessionAware {
	private Logger logger = Logger.getLogger(Browse.class);
	public String browsebutton = null;
	public String message;
	private Map<String, Object> sessionData;
	public List<Registrant> registrantList = new ArrayList<Registrant>();
	public int noteid;
	public Map<String, RegistrationType> regtypeList;
	public String regtype = "Any";
	public int status = -1;
	public int badged = -1;
	public int checkedin = -1;
	public int skip = 0;
	private final RegistrantDAO registrantDAO;
	private final RegistrationTypeDAO registrationTypeDAO;
	public int totalrows;
	
	public void setRegtype(String newvalue) { this.regtype = newvalue; }
	public void setStatus(int newvalue) { this.status = newvalue; }
	public void setBadged(int newvalue) { this.badged = newvalue; }
	public void setCheckedin(int newvalue) { this.checkedin = newvalue; }
	public void setSkip(int newvalue) { this.skip = newvalue; }
	
	public Browse(RegistrantDAO registrantDAO,
			RegistrationTypeDAO registrationTypeDAO) {
		this.registrantDAO = registrantDAO;
		this.registrationTypeDAO = registrationTypeDAO;
	}

	public String setup() throws Exception {
		logger.debug("Browse.setup() ----------------------------------------------------------");
		int cid = ((Convention) sessionData.get("conference")).getConCID();
		Object[] results = registrantDAO.searchExtended("%", cid, null, status, badged, checkedin, skip);
		totalrows = ((Integer)results[0]).intValue();
		registrantList = (List<Registrant>) results[1];
		regtypeList = registrationTypeDAO.list(cid);

		return Action.SUCCESS;
	}

	public String process() throws Exception {
		logger.debug("Browse.process() ----------------------------------------------------------");
		logger.debug("regtype from this browse is " + regtype);
		logger.debug("Regtype to display -: " + regtype);
		logger.debug("Status to display --: " + status);
		logger.debug("Skip value is ------: " + skip);
		logger.debug("badged value is ----: " + badged);
		logger.debug("checkedin value is -: " + checkedin);
		if ((browsebutton != null) && (browsebutton.equalsIgnoreCase("Cancel"))) {
			return "exit";
		} else {
			int cid = ((Convention) sessionData.get("conference")).getConCID();
			Object[] results = registrantDAO.searchExtended("%", cid, !regtype.equals("Any") ? regtype : null, status, badged, checkedin, skip);
			totalrows = ((Integer)results[0]).intValue();
			registrantList = (List<Registrant>) results[1];
			regtypeList = registrationTypeDAO.list(cid);

			return Action.SUCCESS;
		}
	}

	@Override
	public void setSession(Map<String, Object> sessionData) {
		this.sessionData = sessionData;
	}

	public List<Registrant> getRegistrantList() {
		return this.registrantList;
	}

	public Map<String, RegistrationType> getRegtypeList() {
		return this.regtypeList;
	}
}
