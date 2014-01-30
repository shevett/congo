package com.stonekeep.congo.ajax;

import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
import com.stonekeep.congo.dao.RegistrantDAO;
import com.stonekeep.congo.data.Registrant;

public class RegistrantLookup extends ActionSupport implements SessionAware {

	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(RegistrantLookup.class);
	public String message;
	private Map<String, Object> sessionData;

	private final RegistrantDAO registrantDAO;
	
	// Fields to return to caller...
	private int tacoID=0;
	private Registrant r = null;
	public int getTacoID() { return this.tacoID; }
	public Registrant getRegistrant() { return this.r; }

	public RegistrantLookup(RegistrantDAO registrantDAO) {
		this.registrantDAO = registrantDAO;
	}
	
	public String execute() throws Exception {
		logger.debug("Executing...");
		r = registrantDAO.getByID(6623);
		r.firstName = "Test";
		r.lastName = "Person";
		return SUCCESS;
	}
	
	@Override
	public void setSession(Map<String, Object> session) {
		logger.debug("Receiving session data...");
		this.sessionData = session;
	}

}
