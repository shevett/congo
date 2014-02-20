package com.stonekeep.congo.coconut;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.Action;
import com.stonekeep.congo.builders.RegistrantDAOBuilder;
import com.stonekeep.congo.dao.RegistrantDAO;
import com.stonekeep.congo.dao.StateDAO;
import com.stonekeep.congo.data.Convention;
import com.stonekeep.congo.data.Registrant;

public class LookupRegistrant implements Action, SessionAware {
	private Logger logger = Logger.getLogger(LookupRegistrant.class);
	public Map<String, Object> conList = null;
	public String exitbutton = null;
	public String searchbutton = null;
	public String search = null;
	public boolean includedisabled=false;
	public boolean registeredonly=false;
	public String message = "Enter a search string";
	public List<Registrant> searchResults = null;
	private RegistrantDAO registrantDAO = null;
	private StateDAO stateDAO = null;
	private Map<String, Object> sessionData;
	public int totalrows;

	public LookupRegistrant(RegistrantDAOBuilder rdao, StateDAO stateDAO) {
		this.registrantDAO = rdao.getRegistrantDAO();
		this.stateDAO = stateDAO;
	}

	@Override
	public String execute() throws Exception {
		int cid = ((Convention) sessionData.get("conference")).getConCID();
		// Sanitize the input of spaces before trying to parse it
		search = search.replaceAll("\\s","");
		logger.debug("Searchbutton is " + searchbutton);
		if (searchbutton != null) {
			try {
				int rid = Integer.parseInt(search);
				logger.debug("Parsed that this is an int.  We'll search for the reg ID.");
				Registrant r = registrantDAO.getByID(rid);
				if (r != null) { 
					stateDAO.getState(cid, rid);
					r.currentState = stateDAO.getState(cid,rid);
				}
				searchResults = new ArrayList<Registrant>();
				searchResults.add(r);
				return SUCCESS;
			}
			catch (NumberFormatException e) {
				logger.debug("Searching for " + search + " for event " + cid+ ".  Includedisabled is " + includedisabled);
				registrantDAO.includeDisabled = includedisabled;
				registrantDAO.registeredOnly = registeredonly;
				Object[] results = registrantDAO.searchByAny("%" + search.trim() + "%", cid, 200);
				totalrows = ((Integer)results[0]).intValue();
				searchResults = (List<Registrant>) results[1];
				return SUCCESS;
			}
		} else {
			return "exit";
		}
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.sessionData = session;
	}

}
