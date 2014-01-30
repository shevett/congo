package com.stonekeep.congo.importers;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
import com.stonekeep.congo.data.Email;
import com.stonekeep.congo.data.History;
import com.stonekeep.congo.data.Note;
import com.stonekeep.congo.data.Phone;
import com.stonekeep.congo.data.Property;
import com.stonekeep.congo.data.PropertyConfiguration;
import com.stonekeep.congo.data.Registrant;
import com.stonekeep.congo.data.RegistrationType;
import com.stonekeep.congo.data.State;

public class ByRegType implements SessionAware {
	private Logger logger = Logger.getLogger(ByRegType.class);
	public String importbutton = null;
	public String importFormat = null;
	public StringBuffer message;
	private Map<String, Object> sessionData;
	public int totalrows;
	public String data = null;
	private Scanner scanner;
	private int counter;
	public int cid;
	public String regtype;	// set by the calling form
	public Convention c;
	
	List<PropertyConfiguration> propertyList;
	String[] plist = {"TicketType"};
	ArrayList<String> regTypesToCreate = new ArrayList<String>();
	
	public RegistrantDAO registrantDAO;
	public PropertyDAO propertyDAO;
	public RegistrationTypeDAO registrationTypeDAO;
	public StateDAO stateDAO;
	public EmailDAO emailDAO;
	public PhoneDAO phoneDAO;
	public NoteDAO noteDAO;
	public HistoryDAO historyDAO;
	public SendMail sm;

	
	// Only skimming the first column off the input.
	
	public String validate(String data) throws NumberFormatException, SQLException {
		logger.info("Doing validation scan using regtype of " + regtype);
		int counter=0;
		Scanner scanner = new Scanner(data);
		message = new StringBuffer();
		while (scanner.hasNextLine()){
			counter++;
			String line = scanner.nextLine();
			logger.debug("parsing line: " + line);
			String[] a=line.split(",");
			boolean flagOk = true;
			if (a.length < 1) {
				message.append("Line " + counter + " failed parsing.  Only found " + a.length + " elements (should be more than 1)<br>");
			} else {
				// Try to figure out who this person is...
				try {
					int rid = Integer.parseInt(a[0]);
					logger.debug("Searching for information on rid " + rid);
					if (Integer.parseInt(a[0]) > 0) {
						Registrant r = registrantDAO.getByID(Integer.parseInt(a[0]));
						if (r == null) {
							message.append(line + " -- Registrant ID does not exist <br>");
							flagOk=false;
						} else {
							registrantDAO.updateCurrentState(r, cid);
							if (r.currentState!=null && r.currentState.registered) {
								message.append(line + " -- " + r.lastName + ", " + r.firstName + " already registered ( " + r.currentState.regtype + " )<br>");
								flagOk=false;
							}
						}
					} 
				}
				catch (Exception e) {
					message.append(line + " -- Parse error on line " + counter + ": " + e.getMessage());
				}
				
				if (flagOk) {
					message.append(line + " -- READY (Will reg as " + regtype + "<br>");
				}
				
			}
		}
		logger.info(counter + " lines scanned.");
		return message.toString();
	}
	
	public ByRegType() throws Exception {
	}

	public String execute(String data) throws Exception {
		logger.debug("Import.process() ----------------------------------------------------------");
		logger.debug("Button value is " + importbutton);
		logger.debug("Selected importer is " + importFormat);
		// int cid = ((Convention) sessionData.get("conference")).getConCID();
		
		// this scans and populates the regTypesToCreate list...
		validate(data);
		
		logger.debug("Proceeding...");
		
		// Go ahead with import...
		
		// 54512,blahblahblah

		logger.debug("Creating users...");
		message.append("<hr>");
		message.append("Beginning import to regtype " + regtype + "<br>");
		scanner = new Scanner(data); // Reset...
		counter=0;
		int numfields = 4;
		while (scanner.hasNextLine()){
			String line = scanner.nextLine();
			logger.debug("parsing line: " + line);
			String[] a=line.split(",");
			boolean flagOk = true;
			Registrant r = null;
			if (a.length < 1) {
				message.append("Line " + counter + " failed parsing.  Only found " + a.length + " elements (should more than 1)<br>");
			} else {
				// Try to figure out who this person is...
				int rid = Integer.parseInt(a[0]);
				logger.debug("Searching for information on rid " + rid);
				if (Integer.parseInt(a[0]) > 0) {
					r = registrantDAO.getByID(Integer.parseInt(a[0]));
					if (r == null) {
						message.append(line + " -- Registrant ID does not exist <br>");
						flagOk=false;
					} else {
						registrantDAO.updateCurrentState(r, cid);
						if (r.currentState!=null && r.currentState.registered) {
							message.append(line + " -- " + r.lastName + ", " + r.firstName + " already registered ( " + r.currentState.regtype + " )<br>");
							flagOk=false;
						}
					}
				} 
				
				if (flagOk) {
					
					counter++;
										
					// Aaaand register them.
					State s = new State();
					s.cid =cid;
					s.rid = r.rid;
					s.registered = true;
					s.subscribed = true;
					s.regtype = regtype;
					if (r.getCurrentstate() == null) {
						stateDAO.create(s);
					} else {
						stateDAO.save(s);
					}
					
					// create a history record showing we registered them.
					History h = historyDAO.create(r.rid, cid, "REGISTERED", 0);
					h.comment="Bulk import";
					h.arg1 = regtype;
					historyDAO.save(h);
					
					// Send email to them.
					// Set up the mail call with the invoice item for the current line item
					// and their currente mail, then fire the mail off. 
					sm.c = c;
					sm.r = r;
					String result = "No error message.";
					if (sm.emailDAO.getEmail(r.rid) != null) {		// Fix for bug #311 - crash if no email
						sm.target = sm.emailDAO.getEmail(r.rid).address;
						String m = sm.sendMail();
						if (m.equals("message")) {
							result = sm.message;
						}
						
					}
					
					message.append(line + " -- Registered " + r.lastName + ", " + r.firstName + " as " + regtype + ".  Email result: " + result + "<br>");
				}
			}
			
		}
		message.append(counter + " registrations updated.");
		return message.toString();
	}

	@Override
	public void setSession(Map<String, Object> sessionData) {
		this.sessionData = sessionData;
	}
}
