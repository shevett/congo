
package com.stonekeep.congo.coconut;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.Action;
import com.stonekeep.congo.dao.ConventionDAO;
import com.stonekeep.congo.dao.PropertyDAO;
import com.stonekeep.congo.dao.RegistrantDAO;
import com.stonekeep.congo.dao.SettingDAO;
import com.stonekeep.congo.data.Convention;
import com.stonekeep.congo.data.Property;
import com.stonekeep.congo.data.Registrant;
import com.stonekeep.congo.data.Setting;

public class Login implements Action, SessionAware {

	private Logger logger = Logger.getLogger(Login.class);
	public String username;
	public String password = null;
	private int cid = 0;		// injected
	public String keyname;		// injected
	public String keytext;		// injected
	
	public boolean registered;	
	
	public void setKeyname(String keyname) { this.keyname = keyname; }
	public void setKeytext(String keytext) { this.keytext = keytext; }

	public int getCid() {	return cid; }
	public void setCid(int cid) {this.cid = cid;}
	
	public boolean getRegistered() { return registered;}

	public String message;
	private Map<String, Object> sessionData;
	public Registrant r;
	public Map<Integer, Convention> conList;
	public Map<String,Setting> settings;
	private final ConventionDAO conventionDAO;
	private final RegistrantDAO registrantDAO;
	private final PropertyDAO propertyDAO;
	private final SettingDAO settingDAO;

	public Login(ConventionDAO conferenceDAO, RegistrantDAO registrantDAO, PropertyDAO propertyDAO, SettingDAO settingDAO) {
		this.conventionDAO = conferenceDAO;
		this.registrantDAO = registrantDAO;
		this.propertyDAO = propertyDAO;
		this.settingDAO = settingDAO;
	}

	@Override
	public String execute() throws Exception {
		// This code was re-pulling the default event setting after a login attempt - 
		// which doesn't make much sense as the default is set in the JSP
		//		settings = settingDAO.listSettings();
		//		if (settings.containsKey("event_default")) {
		//			cid = Integer.parseInt(settings.get("event_default").getSettingValue());
		//		}
		logger.debug("Someone's trying to log in, validate them...");
		logger.debug("Username is " + username);
		logger.debug("Convention is " + cid);
		// People can log in via registrant ID, Email address, or badgename.
		// First, if it's
		// numeric, use that,
		int possibleRID = 0;
		Registrant r = null;
		try {
			possibleRID = Integer.parseInt(username);
			r = registrantDAO.getByID(possibleRID);
		} catch (NumberFormatException e) {
			// The username field must be textual, find it...
			logger.debug("Searchbyexactbadgename - looking for " + username);
			r = registrantDAO.searchByExactBadgeName(username);
			logger.debug("results of search are " + r);
			
			if (r == null) {
				logger.debug("Searchbyemail - looking for " + username);
				r = registrantDAO.searchbyEmail(username);
			}
		}

		if ((r != null) && registrantDAO.checkPassword(r, password)) {
			logger.debug("Password validation successful.  Checking to see if they're allowed to use coconut...");

			Property p = propertyDAO.getProperty(r.rid,cid,"Administrator");
			boolean loginOk = true;
			String userType = "User";
			if (p == null || p.value == null || p.value.equals("0")) {
				// Okay, admin won't work.  How about an Operator?
				p = propertyDAO.getProperty(r.rid,cid,"Operator");
				if (p == null || p.value == null || p.value.equals("0")) {
					loginOk=false;
				} else {
					userType="Operator";
				}
			} else {
				userType = "Administrator";
			}
			
			if (!loginOk) {				
				logger.warn("Attempt to access Coconut, and no Operator or Administrator privs.");
				message="Operator or Administrator privileges required to access Coconut.";
				return INPUT;				
			}
			
			logger.debug("Property check OK - Matched on " + p.name + " as true.");
			logger.debug("Storing away registrant to session.  r is " + r);
			sessionData.put("coconutuser", r);
			sessionData.put("userType",userType);
			message = "Success";
			Convention c = conventionDAO.getByID(cid);
			conventionDAO.recalculate(c);
			sessionData.put("conference", c);
			
			// Store away whether we're a registered installation into the session.
			
			sessionData.put("registered",new Boolean(com.stonekeep.congo.util.LicenseUtilities.validate(keyname,keytext))) ;
			sessionData.put("keytext",keytext);
			sessionData.put("keyname",keyname);
			
			logger.info("Session opened for #" + r.rid + " (" + r.lastName + ", " + r.firstName + ") for event "
					+ cid + " (" + c.conName + ")");
			return SUCCESS;
		} else {
			message = "Invalid userid or password specified.";
			logger.debug("Failed validation. r is " + r
					+ " - or the checkpassword failed.");
			return INPUT;
		}
	}

	/*
	 * Sets up the environment for the Login.jsp home page for logins.
	 */
	public String setup() throws Exception {
		logger.debug("Setting up for login page...");
		conList = conventionDAO.list();
		logger.debug("conList contains " + conList.size() + " elements.");
		logger.debug("cid being set to " + cid);
		// Settings are normally stored in the session, but since we're at a login screen, session is not active yet.
		settings = settingDAO.listSettings();
		if (settings.containsKey("event_default")) {
			cid = Integer.parseInt(settings.get("event_default").getSettingValue());
		}
		if (com.stonekeep.congo.util.LicenseUtilities.validate(keyname,keytext)) {
			logger.debug("License is Valid!");
			registered=true;
		} else {
			logger.debug("License is Invalid!");
			registered=false;
		}
		return SUCCESS;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.sessionData = session;
	}
}
