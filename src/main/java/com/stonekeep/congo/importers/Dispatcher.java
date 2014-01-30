package com.stonekeep.congo.importers;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.Action;
import com.stonekeep.congo.coconut.SendMail;
import com.stonekeep.congo.dao.EmailDAO;
import com.stonekeep.congo.dao.HistoryDAO;
import com.stonekeep.congo.dao.NoteDAO;
import com.stonekeep.congo.dao.PhoneDAO;
import com.stonekeep.congo.dao.PropertyDAO;
import com.stonekeep.congo.dao.RegistrantDAO;
import com.stonekeep.congo.dao.RegistrationTypeDAO;
import com.stonekeep.congo.dao.StateDAO;
import com.stonekeep.congo.data.Convention;
import com.stonekeep.congo.data.RegistrationType;

public class Dispatcher implements SessionAware {
	private Logger logger = Logger.getLogger(Dispatcher.class);
	public String importbutton = null;
	public String importFormat = null;
	public StringBuffer message;
	public int totalrows;
	public String data = null;
	public LinkedHashMap<String,RegistrationType> regTypes;
	public String regtype = null;
	
	private final RegistrantDAO registrantDAO;
	private final PropertyDAO propertyDAO;
	private final RegistrationTypeDAO registrationTypeDAO;
	private final StateDAO stateDAO;
	private final EmailDAO emailDAO;
	private final PhoneDAO phoneDAO;
	private final NoteDAO noteDAO;
	private final HistoryDAO historyDAO;
	private final SendMail sendMail;
	private Map<String, Object> sessionData;
	
	public Dispatcher(RegistrantDAO registrantDAO,
			PropertyDAO propertyDAO,
			RegistrationTypeDAO registrationTypeDAO,
			StateDAO stateDAO,
			EmailDAO emailDAO,
			PhoneDAO phoneDAO,
			NoteDAO noteDAO,
			HistoryDAO historyDAO,
			SendMail sendMail) {
		this.registrantDAO = registrantDAO;
		this.propertyDAO = propertyDAO;
		this.registrationTypeDAO = registrationTypeDAO;
		this.stateDAO = stateDAO;
		this.emailDAO = emailDAO;
		this.phoneDAO = phoneDAO;
		this.noteDAO = noteDAO;
		this.historyDAO = historyDAO;
		this.sendMail = sendMail;
	}
	
	public String execute() throws Exception {
		logger.debug("Dispatcher begins...");
		logger.debug("Button value is " + importbutton);
		logger.debug("Selected importer is " + importFormat);
		
		if (regTypes == null) {
			logger.debug("execute: Initializing rtdao and getting regtypes");
			int whatcid = ((Convention) sessionData.get("conference")).conCID;
			regTypes = registrationTypeDAO.list(whatcid);
			logger.debug("execute: regtypes is " + regTypes);
		}
		
		if (importFormat == null) {
			return Action.INPUT;
		}
		
		if (importFormat.equals("bulk")) {
			Bulk bulk = new Bulk();
			message = new StringBuffer();
			bulk.cid = ((Convention) sessionData.get("conference")).getConCID();
			bulk.registrantDAO = registrantDAO;
			bulk.propertyDAO = propertyDAO;
			bulk.registrationTypeDAO = registrationTypeDAO;
			bulk.stateDAO = stateDAO;
			bulk.emailDAO = emailDAO;
			bulk.phoneDAO = phoneDAO;
			bulk.noteDAO = noteDAO;
			bulk.historyDAO = historyDAO;

			if (importbutton.equalsIgnoreCase("Validate only")) {
				message = new StringBuffer("Beginning validation only scan...<br>");
				message.append(bulk.validate(data));
				logger.debug("Need to create " + bulk.regTypesToCreate.size() + " reg types and their comps...");
				logger.debug("Need to create " + bulk.plist.length + " property configurations (" + bulk.plist + ")");
				return Action.INPUT;
			} else {		
				message = new StringBuffer("Importing records... (Validating first, then importing.)<br>");
				bulk.propertyList = propertyDAO.listPropertyConfigurations(bulk.cid);
				message.append(bulk.execute(data));
			}
			return Action.INPUT;
		}
		
		if (importFormat.equals("byregtype")) {
			ByRegType brt = new ByRegType();
			message = new StringBuffer();
			brt.cid = ((Convention) sessionData.get("conference")).getConCID();
			brt.registrantDAO = registrantDAO;
			brt.propertyDAO = propertyDAO;
			brt.registrationTypeDAO = registrationTypeDAO;
			brt.stateDAO = stateDAO;
			brt.emailDAO = emailDAO;
			brt.phoneDAO = phoneDAO;
			brt.noteDAO = noteDAO;
			brt.historyDAO = historyDAO;
			brt.regtype = regtype;
			brt.c = (Convention) sessionData.get("conference");
			brt.sm = sendMail;
			
			if (importbutton.equalsIgnoreCase("Validate only")) {
				message = new StringBuffer("Beginning validation only scan...<br>");
				message.append(brt.validate(data));
				return Action.INPUT;
			} else {		
				message = new StringBuffer("Importing records... (Validating first, then importing.)<br>");
				message.append(brt.execute(data));
			}
			return Action.INPUT;
		}

		if (importFormat.equals("bib")) {
			BiB bib = new BiB();
			message = new StringBuffer();
			bib.cid = ((Convention) sessionData.get("conference")).getConCID();
			bib.registrantDAO = registrantDAO;
			bib.propertyDAO = propertyDAO;
			bib.registrationTypeDAO = registrationTypeDAO;
			bib.stateDAO = stateDAO;
			bib.emailDAO = emailDAO;
			bib.phoneDAO = phoneDAO;
			bib.noteDAO = noteDAO;
			message.append(bib.validate(data));
			logger.debug("Need to create " + bib.regTypesToCreate.size() + " reg types and their comps...");
			logger.debug("Need to create " + bib.plist.length + " property configurations (" + bib.plist + ")");

			if (message.length() > 0) {
				message.append("Import generated message / error output.  Import aborted.");
				return Action.INPUT;
			} else {
				message.append("Import scan successful without any messages.  Ready to load");
			}
			
			if (importbutton.equalsIgnoreCase("Validate only")) {
				return Action.INPUT;
			}
			
			bib.propertyList = propertyDAO.listPropertyConfigurations(bib.cid);

			message.append(bib.execute(data));
					
		} else if (importFormat.equals("intercon")) {
			Intercon intercon = new Intercon();
			message = new StringBuffer();
			intercon.cid = ((Convention) sessionData.get("conference")).getConCID();
			intercon.regTypes = registrationTypeDAO.list(intercon.cid);
			intercon.registrantDAO = registrantDAO;
			intercon.propertyDAO = propertyDAO;
			intercon.registrationTypeDAO = registrationTypeDAO;
			intercon.stateDAO = stateDAO;
			intercon.emailDAO = emailDAO;
			intercon.phoneDAO = phoneDAO;
			intercon.noteDAO = noteDAO;
			intercon.sessionData = sessionData;
			message.append(intercon.validate(data));
			logger.debug("Need to create " + intercon.regTypesToCreate.size() + " reg types and their comps...");
			logger.debug("Need to create " + intercon.plist.length + " property configurations (" + intercon.plist + ")");

			if (message.length() > 0) {
				message.append("Import generated message / error output.  Import aborted.");
				return Action.INPUT;
			} else {
				message.append("Import scan successful without any messages.  Ready to load");
			}
			
			if (importbutton.equalsIgnoreCase("Validate only")) {
				return Action.INPUT;
			}
		
			// Create any regtypes needed:
			intercon.createRegistrationTypes();
			
			// intercon.propertyList = propertyDAO.listPropertyConfigurations(intercon.cid);

			
			// Create properties...
			// intercon.createProperties();
			
			// And go ahead and perform the import...
			intercon.processData(data);		
			
		} else {
			message = new StringBuffer("No importer selected.");
			return Action.INPUT;
		}
		return Action.INPUT;
	}

	@Override
	public void setSession(Map<String, Object> sessionData) {
		this.sessionData = sessionData;
	}
}
