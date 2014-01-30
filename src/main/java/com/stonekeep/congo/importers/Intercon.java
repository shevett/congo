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
import com.stonekeep.congo.dao.EmailDAO;
import com.stonekeep.congo.dao.NoteDAO;
import com.stonekeep.congo.dao.PhoneDAO;
import com.stonekeep.congo.dao.PropertyDAO;
import com.stonekeep.congo.dao.RegistrantDAO;
import com.stonekeep.congo.dao.RegistrationTypeDAO;
import com.stonekeep.congo.dao.StateDAO;
import com.stonekeep.congo.data.Convention;
import com.stonekeep.congo.data.Email;
import com.stonekeep.congo.data.Note;
import com.stonekeep.congo.data.Phone;
import com.stonekeep.congo.data.Property;
import com.stonekeep.congo.data.PropertyConfiguration;
import com.stonekeep.congo.data.Registrant;
import com.stonekeep.congo.data.RegistrationType;
import com.stonekeep.congo.data.State;

public class Intercon implements SessionAware {
	private Logger logger = Logger.getLogger(Intercon.class);
	public String importbutton = null;
	public String importFormat = null;
	public StringBuffer message;
	public Map<String, Object> sessionData;
	public int totalrows;
	public int cid;
	private int counter;
	private Scanner scanner;
	public String data = null;
	
	List<PropertyConfiguration> propertyList = null;
	String[] plist = {"ShirtOrder","InHotel","RoomNumber","Breakfast"};
	ArrayList<String> regTypesToCreate = new ArrayList<String>();
	LinkedHashMap<String, RegistrationType> regTypes = null;

	
	public RegistrantDAO registrantDAO;
	public PropertyDAO propertyDAO;
	public RegistrationTypeDAO registrationTypeDAO;
	public StateDAO stateDAO;
	public EmailDAO emailDAO;
	public PhoneDAO phoneDAO;
	public NoteDAO noteDAO;
	
	// LastName		FirstName	Nickname	EMail	Status	LoginNote
	//		LastName?, FirstName?, Email are exactly what they sound like.
	//		Nickname is badge name, and might be an empty field.
	//		LoginNote? should show up as a login note on the attendee's record, and might be an empty field.
	
	// Status is the complicated one:
	//Alumni: NOT sub'd, NOT reg'd
	//Unpaid: sub'd but NOT reg'd
	//Anything else: sub'd AND reg'd, with regtype = Status
	
	/* This method checks to make sure the properties to be
	 * set on each Registrant have their master property types available.
	 */
	
	private void createRegistrationType(String rtname,int cid) throws SQLException {
		logger.debug("Creating registration type " + rtname + " for event " + cid);
		RegistrationType rt = new RegistrationType();
		rt.setRegActive(true);
		rt.setRegCID(cid);
		rt.setRegDesc(rtname);
		rt.setRegName(rtname);
		rt.setRegPrint(rtname);
		rt.setRegSequence(1);
		rt.setRegComp(false);
		rt.setRegCost(new BigDecimal(0));
		registrationTypeDAO.create(rt);
		logger.debug("Created regtype " + rtname);
	}
	
	public String validate(String data) {
		message = new StringBuffer();
		logger.debug("Doing validation scan...");
		int counter=0;
		Scanner scanner = new Scanner(data);
		while (scanner.hasNextLine()){
			counter++;
			String line = scanner.nextLine();
			logger.debug("parsing line: " + line);
			String[] a=line.split("\t",-1);
			if (a == null) {
				logger.debug("line " + counter + " is null.");
				message.append("Line " + counter + " contains no tabs, cannot split.");
			} else {
				if (a.length < 5) {
					message.append("Line " + counter + " failed parsing.  At least 5 elements are needed, found " + a.length + ".<br>");
				} else {
					if (! a[4].equals("Alumni") && !a[4].equals("Unpaid")) { // We need to create this...
						String rt = a[4];
						logger.debug("Checking for regtype " + rt);
						if (! regTypesToCreate.contains(rt)) {
							regTypesToCreate.add(rt);
							logger.debug("Added new regtype to create " + rt);
						}
					}
				}	
			}
		}
		return message.toString();
	}
	
	public Intercon() throws Exception {
	}

	public String createRegistrationTypes() throws Exception {
		logger.debug("Creating registration types...");
		for (String s : regTypesToCreate) {
			if (regTypes.containsKey(s)) logger.debug("Skipped create of " + s + " - exists.");
			else createRegistrationType(s,cid);
		}
		return Action.SUCCESS;
	}
	
	public String processData(String data) throws Exception {
		String so = null;
		String ih = null;
		String rn = null;
		String bf = null;
		logger.debug("Creating users...");
		message.append("<br>");
		scanner = new Scanner(data); // Reset...
		counter=0;
		while (scanner.hasNextLine()){
			counter++;
			String line = scanner.nextLine();
			logger.debug("parsing line: " + line);
			String[] a=line.split("\t",-1);
			// Strip the quotes...
			for (int i=0; i<a.length; i++)
				a[i] = a[i].replace("\"","");
			// Populate the strings
			String ln = a[0];
			String fn = a[1];
			String bn = a[2];
			String em = a[3];
			String rt = a[4];
			String nt = a[5];


			logger.debug("Adding " + fn + " " + ln + " as regtype " + rt);
			
			// Create the registrant
			Registrant r = registrantDAO.create();
			r.enabled = true;
			r.badgeName = bn;
			r.firstName = fn;
			r.lastName = ln;
			registrantDAO.save(r);
			
			// Aaaand register them (if they should be registered)
			State s = new State();
			s.cid =cid;
			s.rid = r.rid;
			if (rt.equals("Alumni")) {
				s.registered=false;
				s.subscribed=false;
				s.regtype=null;
			} else if (rt.equals("Unpaid")) {
				s.registered=false;
				s.subscribed=true;
				s.regtype=null;
			} else {
				s.registered=true;
				s.subscribed=true;
				s.regtype=rt;
			}
			stateDAO.create(s);
			
			if (nt.length() > 0) {
				//Note...
				Note n = new Note();
				n.cid = cid;
				n.rid = r.rid;
				n.message = nt;
				n.type = "NOTICE";
				n.postRid = 0;
				noteDAO.create(n);
			}
			
		}
		message = new StringBuffer(counter + " registrants inserted.");
		return Action.SUCCESS;
	}

	@Override
	public void setSession(Map<String, Object> sessionData) {
		this.sessionData = sessionData;
	}
}
