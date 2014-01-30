package com.stonekeep.congo.coconut;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Action;
import com.stonekeep.congo.dao.RegistrantDAO;

public class DeleteRegistrant implements Action {
	public String deletebutton = null;
	public int whatRID = 0;
	private Logger logger = Logger.getLogger(DeleteRegistrant.class);
	private RegistrantDAO registrantDAO;
	
	public DeleteRegistrant(RegistrantDAO registrantDAO) {
		this.registrantDAO = registrantDAO;
	}

	@Override
	public String execute() throws Exception {
		logger.debug("Delete Registrant called.");
		if (deletebutton!= null) {
			logger.warn("User has requested a registrant deletetion of RID " + whatRID);
			registrantDAO.delete(whatRID);
			return "success";
		} else {
			return "exit";
		}
	}


}
