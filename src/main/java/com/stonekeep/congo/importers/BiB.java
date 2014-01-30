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

public class BiB implements SessionAware {
	private Logger logger = Logger.getLogger(BiB.class);
	public String importbutton = null;
	public String importFormat = null;
	public StringBuffer message;
	private Map<String, Object> sessionData;
	public int totalrows;
	public String data = null;
	private Scanner scanner;
	private int counter;
	public int cid;
	
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
	
	//"SysID","RegID","Badgename","Fullname","Email","TicketDate","TicketType","PhoneLastFour","IsPresenter","IsComped","Notes"
	//"7267","bartleby","bartleby bartleby","bartleby50@yahoo.com","SATSUN","WILLCALL","0092","N","N",""

	//TicketDate: Indicates what they paid for. Will be "SAT," "SUN," or "SATSUN"
	//TicketType: Will be "PHYSICAL" if they will have a physical ticket, or
	//"WILLCALL" if they don't have a physical ticket
	
	/*
	1) I'm not clear as to whether or not you still want the "SysID" column. That's my unique number, but I can leave it off if you need me to.
	2) I get the last four digits of the credit card number from BPT. I can pass that to you if you think it'll help people identify themselves at reg.
	3) The "IsPresenter" flag is meaningless and will always be "N", but the "IsComped" flag is for reals.
	*/ 
	
	/* This method checks to make sure the properties to be
	 * set on each Registrant have their master property types available.
	 */
	public boolean validateProperties(String pname,List<PropertyConfiguration> propertyList) throws SQLException {		
		// Create an arraylist of the propertynames
		for (PropertyConfiguration pc : propertyList) {
			if (pc.name.equals(pname)) return true;
		}
		return false;
	}
	
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
		logger.debug("Doing validation scan...");
		int counter=0;
		Scanner scanner = new Scanner(data);
		message = new StringBuffer();
		while (scanner.hasNextLine()){
			counter++;
			String line = scanner.nextLine();
			logger.debug("parsing line: " + line);
			String[] a=line.split(",");
			if (a.length < 10) {
				message.append("Line " + counter + " failed parsing.  Only found " + a.length + " elements (should be 10)<br>");
			} else {
				String rt = a[4].replace("\"","");	// slice off the quotes
				if (! regTypesToCreate.contains(rt)) {
					regTypesToCreate.add(rt);
					logger.debug("Added new regtype to create " + rt);
				}
			}
		}
		return message.toString();
	}
	
	public BiB() throws Exception {
	}

	public String execute(String data) throws Exception {
		logger.debug("Import.process() ----------------------------------------------------------");
		logger.debug("Button value is " + importbutton);
		logger.debug("Selected importer is " + importFormat);
		// int cid = ((Convention) sessionData.get("conference")).getConCID();
		message = new StringBuffer();

		
		LinkedHashMap<String, RegistrationType> regTypes = registrationTypeDAO.list(cid);
		logger.debug("There are " + regTypes.size() + " registration types defined for event " + cid);
		for (String rt : regTypes.keySet()) {
			logger.debug("---->" + rt);
		}


		
		logger.debug("Need to create " + regTypesToCreate.size() + " reg types and their comps...");
		logger.debug("Need to create " + plist.length + " property configurations (" + plist + ")");
		
		if (message.length() > 0) {
			message.append("Import generated message / error output.  Import aborted.");
			return Action.INPUT;
		} else {
			message.append("Import scan successful without any messages.  Ready to load rows");
		}
		
//		if (importbutton.equalsIgnoreCase("Validate only")) {
//			return Action.INPUT;
//		}
		
		logger.debug("Proceeding...");
		
		// Go ahead with import...
		// Create any regtypes
		logger.debug("Creating registration types...");
		for (String s : regTypesToCreate) {
			if (regTypes.containsKey(s)) logger.debug("Skipped create of " + s + " - exists.");
			else createRegistrationType(s,cid);
			String t = "COMP" + s;
			if (regTypes.containsKey(t)) logger.debug("Skipped create of " + t + " - exists.");
			else createRegistrationType(t,cid);
		}
		
		logger.debug("Creating properties...");
		for (String s: plist) {
			logger.debug("Checking " + s);
			if (! validateProperties(s,propertyList)) {
				// This property (s) is not in the propertyconfiguration.  Create it.
				logger.debug("Creating " + s);
				PropertyConfiguration pc = new PropertyConfiguration();
				pc.cid = cid;
				pc.description = s;
				pc.format = "String";
				pc.scope = "Event";
				pc.name = s;
				propertyDAO.savePropertyConfiguration(pc);
			} else {
				logger.debug("Skipping " + s + " - already a PropertyConfiguration for this.");
			}
		}
		
		//"SysID","RegID","Badgename","Fullname","Email","TicketDate","TicketType","PhoneLastFour","IsPresenter","IsComped","Notes"
		//"7267","bartleby","bartleby bartleby","bartleby50@yahoo.com","SATSUN","WILLCALL","0092","N","N",""
		logger.debug("Creating users...");
		message.append("<br>");
		scanner = new Scanner(data); // Reset...
		counter=0;
		while (scanner.hasNextLine()){
			counter++;
			String line = scanner.nextLine();
			logger.debug("parsing line: " + line);
			String[] a=line.split(",");
			// Strip the quotes...
			for (int i=0; i<=9; i++)
				a[i] = a[i].replace("\"","");
			// Populate the strings
			String bn = a[1];
			String fn = a[2];
			String em = a[3];
			String rt = a[4];
			String pt = a[5];
			String pn = a[6];
			String ip = a[7];
			String ic = a[8];
			String nn = a[9];
			if (ic.equalsIgnoreCase("Y")) {
				rt = "COMP" + rt;
			}
			logger.debug("'" + bn + "' ( " + em + ") for registrant " + fn + " for regtype " + rt);
			
			// Create the registrant
			Registrant r = registrantDAO.create();
			r.enabled = true;
			r.badgeName = bn;
			String[] fnln = fn.split(" ");
			r.firstName = fnln[0];
			if (fnln.length > 1) {
				r.lastName = fnln[1];
			}
			Email e = new Email();
			e.rid = r.rid;
			e.setAddress(em);
			e.setLocation("Home");
			e.setPrimary(true);
			emailDAO.create(e);
			
			registrantDAO.save(r);
			
			// Aaaand register them.
			State s = new State();
			s.cid =cid;
			s.rid = r.rid;
			s.registered = true;
			s.subscribed = true;
			s.regtype = rt;
			stateDAO.create(s);
			
			// Set the properties
			propertyDAO.setProperty(r.rid,cid,"TicketType",pt);
			
			// Phone number
			Phone p = new Phone();
			p.setRid(r.rid);
			p.setLocation("Home");
			p.setPhone(pn);
			p.setPrimary(true);
			phoneDAO.create(p);
			
			//Note...
			Note n = new Note();
			n.cid = cid;
			n.rid = r.rid;
			n.message = nn;
			n.type = "NORMAL";
			n.postRid = 0;
			noteDAO.create(n);
			
		}
		message.append(counter + " registrants inserted.");
		return Action.SUCCESS;
	}

	@Override
	public void setSession(Map<String, Object> sessionData) {
		this.sessionData = sessionData;
	}
}
