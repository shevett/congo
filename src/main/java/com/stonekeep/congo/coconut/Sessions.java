package com.stonekeep.congo.coconut;

import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Action;
import com.stonekeep.congo.dao.SessionDAO;
import com.stonekeep.congo.data.Convention;
import com.stonekeep.congo.data.Session;

public class Sessions extends Editor {

	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(Sessions.class);
	public int id;
	public List<Session> sessions;
	public Session workingsession;
	private int whatcid;
	private final SessionDAO sessionDAO;
	public String message;
	public String[] statuses = {"PROPOSED","VETTED","SCHEDULED","DROPPED","CANCELLED","NEEDS EDIT","ASSIGNED","DUPLICATE"};

	
	public Sessions(SessionDAO sessionDAO) {
		this.sessionDAO = sessionDAO;
	}

	public void prepare() throws Exception {
		logger.debug("prepare: Prepare starting");
		whatcid = ((Convention) sessionData.get("conference")).conCID;
		if (sessions == null) {
			logger.debug("prepare: Initializing sessionDAO and getting sessions for event " + whatcid);
			sessions = sessionDAO.listAll(whatcid);
			logger.debug("prepare: sessions list is " + sessions.size() + " elements long.");
		}
		if (id > 0) {
			workingsession = sessionDAO.getById(id);
		}
		logger.debug("prepare: workingsession is : " + workingsession);
		logger.debug("prepare: id is " + id);
		logger.debug("Exiting prepare - workingsession is " + workingsession);
	}

	public String view() throws Exception {
		logger.debug("view...");
		if (typebutton != null) {
			return Action.SUCCESS;
		}
		if (exitbutton != null) {
			return "exit";
		}
		// load up the data for the edit form...
		logger.debug("view: Loading room " + id);
		workingsession = sessionDAO.getById(id);
		logger.debug("workingsession is " + workingsession);
		return Action.SUCCESS;
	}

	public String delete() throws Exception {
		logger.debug("delete...");
		logger.debug("I'll be deleting " + id);
		sessionDAO.remove(id);
		return Action.SUCCESS;
	}
	
	public String create() throws Exception {
		logger.debug("Creating new session for event " + whatcid);
		Session v = new Session();
		v.setCid(whatcid);
		v.setSessionTitle("New session");
		sessionDAO.add(v);
		return Action.SUCCESS;
	}

	public String update() throws Exception {
		logger.debug("updating...");
		logger.debug("update: id is " + id);
		logger.debug("update: Button is " + typebutton);
		logger.debug("update: workingsession is " + workingsession);
		if (typebutton != null) {
			// Here we need to validate some stuff.
			if (workingsession.getSessionTitle().length() < 1) {
				logger.debug("No room name specified .  Returning INPUT");
				message="Please specify a name for this room";
				return INPUT;
			}
			logger.debug("update: saving, this is an update...");
			logger.debug("Session id is " + workingsession.getId());
			logger.debug("session name is " + workingsession.getSessionTitle());
			sessionDAO.update(workingsession);
		}
		logger.debug("Exiting update()");
		return Action.SUCCESS;
	}
	

}
