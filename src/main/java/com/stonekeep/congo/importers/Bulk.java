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

public class Bulk implements SessionAware {
	private Logger logger = Logger.getLogger(Bulk.class);
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
	public HistoryDAO historyDAO;
	
	// 54512: foo@bar.com:Bob Smith:EarnedComp

	// badge id
	// email address
	// full name
	// regtype
	
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
	
	public String validate(String data) throws NumberFormatException, SQLException {
		logger.debug("Doing valiation scan...");
		int counter=0;
		Scanner scanner = new Scanner(data);
		message = new StringBuffer();
		while (scanner.hasNextLine()){
			counter++;
			String line = scanner.nextLine();
			logger.debug("parsing line: " + line);
			String[] a=line.split(":");
			boolean flagOk = true;
			if (a.length < 4) {
				message.append("Line " + counter + " failed parsing.  Only found " + a.length + " elements (should be 4)<br>");
			} else {
				// Try to figure out who this person is...
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
							message.append(line + " -- Already registered ( " + r.currentState.regtype + " )<br>");
							flagOk=false;
						}
					}
				} else {
					// Registrant ID is 0.  Look up by email and/or fname/lname
					if (a[1].length() > 0) {
						Registrant r = registrantDAO.searchbyEmail(a[1]);
						if (r != null ) {
							if (r.currentState.registered) {
								message.append(line + " -- Already registered ( " + r.currentState.regtype + " )<br>");
								flagOk=false;
							} else {
								message.append(line + " -- Cannot process, email address not found.<br>");
								flagOk=false;
							}
						} else {
							message.append(line + " -- Can't find this registrant. <br>");
							flagOk=false;
						}
					} else {
						// If we're here, we have no email address, and a reg ID of 0.  Look up by last name,
						// scan through the results looking for a first name match (or just one row returned), and
						// match that.
						logger.debug("Doing an extended search for '" + a[2] + "' ...");
						String[] fnln = a[2].split(" ");
						Object results[] = registrantDAO.searchByAny(fnln[1], cid, 0);
						Integer totalCount = (Integer)results[0];
						ArrayList<Registrant> rlist = (ArrayList)results[1];
						// Did we get nada from that?  Solly chahlie, no one found.
						if (totalCount == 0) { 
							flagOk = false;
							message.append(line + " -- No one with the last name of " + fnln[1] + " was found.<br>");
						} else {
							// If that returned only one row, that's our man.
							if (totalCount == 1) {
								// nothing to do, let it fall through.
							} else {
								// Trickier now.  we got more than one of that last nae.  Scan through checking first names.
								for (Registrant r : rlist)
									if (r.firstName.equalsIgnoreCase(fnln[0])) {
										// We have a winnah!
										// Don't re-register them if they're already registered.
										registrantDAO.updateCurrentState(r, cid);
										if (r.currentState.registered) {
											flagOk = false;
											message.append(line + " -- Found firstname + lastname, but already registered.<br>");
										}
										
									}
							}

						}
					}
				}
				
				if (flagOk) {
					message.append(line + " -- READY<br>");
				}
				
				String rt = a[3];
				if (! regTypesToCreate.contains(rt)) {
					regTypesToCreate.add(rt);
					logger.debug("Added new regtype to create " + rt);
				}
			}
		}
		return message.toString();
	}
	
	public Bulk() throws Exception {
	}

	public String execute(String data) throws Exception {
		logger.debug("Import.process() ----------------------------------------------------------");
		logger.debug("Button value is " + importbutton);
		logger.debug("Selected importer is " + importFormat);
		// int cid = ((Convention) sessionData.get("conference")).getConCID();
		
		// this scans and populates the regTypesToCreate list...
		validate(data);
		
		LinkedHashMap<String, RegistrationType> regTypes = registrationTypeDAO.list(cid);
		logger.debug("There are " + regTypes.size() + " registration types defined for event " + cid);
		for (String rt : regTypes.keySet()) {
			logger.debug("---->" + rt);
		}
		
		logger.debug("Need to create " + regTypesToCreate.size() + " reg types and their comps...");
		logger.debug("Proceeding...");
		
		// Go ahead with import...
		// Create any regtypes
		logger.debug("Creating registration types...");
		for (String s : regTypesToCreate) {
			if (regTypes.containsKey(s)) logger.debug("Skipped create of " + s + " - exists.");
			else createRegistrationType(s,cid);
		}
		
		
		// 54512: foo@bar.com:Bob Smith:EarnedComp

		logger.debug("Creating users...");
		message.append("<br>");
		scanner = new Scanner(data); // Reset...
		counter=0;
		int numfields = 4;
		while (scanner.hasNextLine()){
			String line = scanner.nextLine();
			logger.debug("parsing line: " + line);
			String[] a=line.split(":");
			boolean flagOk = true;
			Registrant r = null;
			if (a.length < 4) {
				message.append("Line " + counter + " failed parsing.  Only found " + a.length + " elements (should be 4)<br>");
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
							message.append(line + " -- Already registered ( " + r.currentState.regtype + " )<br>");
							flagOk=false;
						}
					}
				} else {
					// Registrant ID is 0.  Look up by email and/or fname/lname
					if (a[1].length() > 0) {
						r = registrantDAO.searchbyEmail(a[1]);
						if (r != null ) {
							if (r.currentState.registered) {
								message.append(line + " -- Already registered ( " + r.currentState.regtype + " )<br>");
								flagOk=false;
							} else {
								message.append(line + " -- Cannot process, email address not found.<br>");
								flagOk=false;
							}
						}  else {
							message.append(line + " -- Can't find this registrant. <br>");
							flagOk=false;
						}
					} else {
						// If we're here, we have no email address, and a reg ID of 0.  Look up by last name,
						// scan through the results looking for a first name match (or just one row returned), and
						// match that.
						logger.debug("Doing a search for '" + a[2] + "' ...");
						String[] fnln = a[2].split(" ");
						Object results[] = registrantDAO.searchByAny(fnln[1], cid, 0);
						
						// Something odd happened here.  The [0]th element is suposed to have the rowcount
						// but it keeps returning '4' even when there's no results.  Need to research that.
						ArrayList<Registrant> rlist = (ArrayList)results[1];
						Integer totalCount = rlist.size();
						if (totalCount == 0) { // Did we get nada from that?  Solly chahlie, no one found.
							flagOk = false;
							message.append(line + " -- No one with the last name of " + fnln[1] + " was found.<br>");
						} else {
							if (totalCount == 1) {		// Good.  Thats one row, return it.
								r = (Registrant)rlist.get(0);
							} else {
								for (Registrant i : rlist) {
									if (i.firstName.equalsIgnoreCase(fnln[0])) {	// We have a winnah!
										r = i;
										registrantDAO.updateCurrentState(r, cid);
										if (r.currentState.registered) {	// Don't re-register them if they're already registered.
											flagOk = false;
											message.append(line + " -- Found firstname + lastname, but already registered.");
										}
										
									}
								}
							}

						}
					}
				}
				
				if (flagOk) {
					message.append(line + " -- READY<br>");
					// 54512: foo@bar.com:Bob Smith:EarnedComp

					counter++;

					String fn = a[2];
					String em = a[1];
					String rt = a[3];
					
					// Create the registrant
					
					logger.debug("Processing.  fn: " + fn + ", em: " + em + ", rt: " +rt);

					r.enabled = true;
					r.badgeName = fn;
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
					try {
						emailDAO.create(e);
					}
					catch (Exception ex) {
						// There's probably already a Home email. 
						logger.warn("Email 'home' Location for rid " + r.rid + " already exists.  Skipping (didn't add " + em + ")");
					}
					
					registrantDAO.save(r);
										
					// Aaaand register them.
					State s = new State();
					s.cid =cid;
					s.rid = r.rid;
					s.registered = true;
					s.subscribed = true;
					s.regtype = rt;
					if (r.getCurrentstate() == null) {
						stateDAO.create(s);
					} else {
						stateDAO.save(s);
					}
					
					// create a history record showing we registered them.
					History h = historyDAO.create(r.rid, cid, "REGISTERED", 0);
					h.comment="Bulk import";
					historyDAO.save(h);
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
