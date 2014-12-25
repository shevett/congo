package com.stonekeep.congo.coconut;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.Action;
import com.stonekeep.congo.dao.Closer;
import com.stonekeep.congo.data.Convention;

public class MaintenanceChecker implements Action,SessionAware {

	private Logger logger = Logger.getLogger(MaintenanceChecker.class);
	private Map<String, Object> sessionData;
	private final DataSource dataSource;
	public String message;
	
	public ArrayList<String> systemNotices = new ArrayList<String>();
	
	public MaintenanceChecker(DataSource d) {
		this.dataSource = d;
	}
	
	
	/* 
	 * This action does some 'sanity' checks across all of CONGO.  Any messages or problems
	 * it finds that needs to be told to the operator should be added to the 'systemNotices' list.
	 * 
	 * @see com.opensymphony.xwork2.Action#execute()
	 */
	@Override
	public String execute() throws Exception {
		int cid = ((Convention) sessionData.get("conference")).getConCID();
		logger.debug("CID " + cid + " - Checking status of system for maintenance...");
		logger.debug("systemNotices starts out with " + systemNotices.size() + " elements.");
		
		// Here, check to make sure the user is allowed to be in the maintenance screens, kick 'em out
		// if not...
		// ${session.userType} needs to be 'Administrator'
		String userType = ((String) sessionData.get("userType"));
		if (! userType.equals("Administrator")) {
			message="You do not have permission to view that resource.";
			return "message";
		}

		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		ResultSet rset = null;
		try {

			// Check to see if the templates needed are there...
			p = c.prepareStatement("select * from templates where template_name='Email-1' and template_cid=?");
			p.setInt(1,cid);
			rset = p.executeQuery();
			if (! rset.next()) { systemNotices.add("The default email template 'Email-1' is not defined for this event."); }

			// Check to see if the templates needed are there...
			p = c.prepareStatement("select * from templates where template_name='Link-1' and template_cid=?");
			p.setInt(1,cid);
			rset = p.executeQuery();
			if (! rset.next()) { systemNotices.add("The default Link template 'Link-1' is not defined for this event."); }

			// Do we have a 'Reminder-1' template?
			p = c.prepareStatement("select * from templates where template_name='Reminder-1' and template_cid=?");
			p.setInt(1,cid);
			rset = p.executeQuery();
			if (! rset.next()) { systemNotices.add("The default password reminder template 'Reminder-1' is not defined for this event."); }

			// Are there any registration types at all for this event?
			p = c.prepareStatement("select * from con_regtypes where reg_cid=?");
			p.setInt(1,cid);
			rset = p.executeQuery();
			if (! rset.next()) { 
				systemNotices.add("There are no registration types defined for this event."); 
			} else {
				// Are any of the registration types active for this event?
				p = c.prepareStatement("select * from con_regtypes where reg_cid=? and reg_active=1");
				p.setInt(1,cid);
				rset = p.executeQuery();
				if (! rset.next()) { systemNotices.add("There are no registration types marked Active."); }
			}

			// Has the admin set a default event?
			p = c.prepareStatement("select * from config_settings where setting_name='event_default'"); 
			rset = p.executeQuery();
			if (! rset.next()) { systemNotices.add("No default event specified.  Use 'Work with Events' to set the active event."); }

			// Is the default event '0'?
			p = c.prepareStatement("select * from config_settings where setting_name='event_default' and setting_value='0'"); 
			rset = p.executeQuery();
			if (rset.next()) { systemNotices.add("The default event is set to '0'.  This is probably not what you want.  Use System Settings to change."); }

			// Is there an 'Operator' property for this event?
			p = c.prepareStatement("select * from config_properties where prop_name='Operator' and prop_cid=?");
			p.setInt(1,cid);
			rset = p.executeQuery();
			if (! rset.next()) { systemNotices.add("There is no 'Operator' property defined for this event."); }

			// Has the paypal configuration stuff been set up yet?
			p = c.prepareStatement("select * from config_settings where setting_name='paypal_enabled'");
			rset = p.executeQuery();
			if (! rset.next()) { 
				systemNotices.add("The paypal configuration settings are not available.  Please select Payment processing"); 
			} 

			p.close();
		}
		finally {
			Closer.close(rset,p,c);
		}
		
		logger.debug("systemNotices ends up with " + systemNotices.size() + " elements.");

		if (systemNotices.size() == 0) {
			systemNotices.add("No issues, everything looks good!");
		}
		
		return SUCCESS;
	}
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.sessionData = session;
	}

}
