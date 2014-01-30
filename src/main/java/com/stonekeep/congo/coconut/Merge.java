package com.stonekeep.congo.coconut;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.stonekeep.congo.dao.HistoryDAO;
import com.stonekeep.congo.dao.RegistrantDAO;
import com.stonekeep.congo.data.History;
import com.stonekeep.congo.data.Registrant;

public class Merge implements SessionAware {
	private final DataSource dataSource;
	private Logger logger = Logger.getLogger(Merge.class);
	public String mergebutton = null;
	public String message = null;
	public String mergetarget = null;
	public StringBuffer mergeLog = new StringBuffer();

	private Map<String, Object> sessionData;
	
	private final RegistrantDAO registrantDAO;
	private final HistoryDAO historyDAO;

	public Merge(DataSource datasource,RegistrantDAO registrantDAO, HistoryDAO historyDAO) {
		this.dataSource = datasource;
		this.registrantDAO = registrantDAO;
		this.historyDAO = historyDAO;
	}
	
	public String mergeExit() throws Exception {
		return "success";
	}

	public String mergeRegistrantPreview() throws Exception {
		logger.debug("mergeRegistrantPreview called");
		logger.debug("mergebutton is " + mergebutton);
		Connection c= null;

		if (mergebutton.equalsIgnoreCase("exit")) {
			return "exit";
		}
		// Validate - are we specifying a regid thats valid?
		int mt = 0;
		try {
			mt = Integer.parseInt(mergetarget);
		} catch (NumberFormatException e) {
			message = "Please enter a valid registrant ID";
			return "input";
		}
		
		// Check for the condition where the source and the target may have a state record
		// for the same event.
		
		// Get a list of state_cids for which we have records for the source...
		Registrant ms = (Registrant)sessionData.get("workingregistrant");
		c = dataSource.getConnection();
		c.setAutoCommit(false);
		logger.debug("Checking for duplicate state records...");
		PreparedStatement p = c.prepareStatement("select state_cid from reg_state where state_rid=? and state_subscribed order by state_cid");
		p.setInt(1,ms.rid);
		ResultSet rset = p.executeQuery();
		HashMap sourceCids = new HashMap();
		while (rset.next()) {
			sourceCids.put(rset.getInt("state_cid"),true);
			logger.debug("Adding " + rset.getInt("state_cid") + " to source CID list.");
		}
		logger.debug("Source rid " + ms.rid + " has " + sourceCids.size() + " state rows.  Comparing against target state list.");

		p=c.prepareStatement("select state_cid from reg_state where state_rid=? and state_subscribed order by state_cid");
		p.setInt(1,mt);
		rset=p.executeQuery();
		while (rset.next()) {
			int targetCid = rset.getInt("state_cid");
			if (sourceCids.containsKey(targetCid)) {
				// this is bad.  we have conflicting state keys.
				message="Both source (" + ms.rid + ") and target (" + mt + ") have 'state' information for Event " + targetCid + " showing they're subscribed.  Please unsubscribe one of the conflicting records.";
				logger.warn(message);
				return "input";
			} else {
				logger.debug("Search for targetCid '" + targetCid + "' in list failed");
			}
		}
		
		Registrant t = (Registrant) registrantDAO.getByID(mt);
		if (t == null) {
			message = "No such registrant " + mt + " found.";
			return "input";
		} else {
			sessionData.put("mergetarget", t);
			return "success";
		} 
	}
	
	public String mergeRegistrantProcess() throws Exception {
		Connection c=null;
		logger.debug("mergeRegistrantProcess beginning.");
		if (mergebutton.equalsIgnoreCase("exit")) {
			logger.debug("Exit hit, cancelling.");
			return "exit";
		}
		Registrant ms = (Registrant)sessionData.get("workingregistrant");
		Registrant mt = (Registrant)sessionData.get("mergetarget");
		logger.debug("Merging from " + ms.rid + " to " + mt.rid + "\n");
		
		mergeLog = new StringBuffer();	// Zero out the merge log.
		
		// This is the merge process - we wrap the entire thing in a transaction in
		// case something blows up.
		try {
			c = dataSource.getConnection();
			c.setAutoCommit(false);
			
			// Do some housecleaning.  Remove any state records from either registrant that 
			// show not subscribed.  These are just going to get in the way of the merge.
			mergeLog.append("Removing un-subscribed history from source and target...\n");
			logger.debug("Removing un-subscribed history from source and target");
			// Note, the complexity of the state_subscribed is to work around a problem in bug #309.  When a state record is auto-created,
			// it fills the fields with NULLS.  and those are not hit when you do ! state_subscribed.
			PreparedStatement p = c.prepareStatement("delete from reg_state where (state_rid=? OR state_rid=?) AND (state_subscribed=0 or state_subscribed IS NULL)");
			p.setInt(1,mt.rid);
			p.setInt(2,ms.rid);
			mergeLog.append(p.executeUpdate() + " rows updated.\n");
			p.close();

			// reg_history (hist_rid, hist_operator)
			logger.debug("merging reg_history");
			message = "merging reg_history : step 1\n";
			p = c.prepareStatement("update reg_history set hist_rid=? where hist_rid=?");
			p.setInt(1,mt.rid);
			p.setInt(2,ms.rid);
			mergeLog.append(p.executeUpdate() + " rows updated.\n");
			p.close();
			
			p = c.prepareStatement("update reg_history set hist_rid=? where hist_rid=?");
			logger.debug("merging reg_history : step 2");
			mergeLog.append("merging reg_history : step 2\n");
			p.setInt(1,mt.rid);
			p.setInt(2,ms.rid);
			mergeLog.append(p.executeUpdate() + " rows updated.\n");
			p.close();
			
			p = c.prepareStatement("update reg_history set hist_operator=? where hist_operator=?");
			logger.debug("merging reg_history : step 3");
			mergeLog.append("merging reg_history : step 3\n");
			p.setString(1,mt.rid + "");
			p.setString(2,ms.rid + "");
			mergeLog.append(p.executeUpdate() + " rows updated.\n");
			p.close();
			
			// This needs to take into account collisions
			logger.debug("merging reg_state : step 1");
			mergeLog.append("setting all subscribed registrants to " + mt.rid + " from " + ms.rid + "\n");
			p = c.prepareStatement("update reg_state set state_rid=? where state_rid=? and state_subscribed");
			p.setInt(1,mt.rid);
			p.setInt(2,ms.rid);
			mergeLog.append(p.executeUpdate() + " rows updated.\n");
			p.close();
			
			// The previous update may have left rows remaining in the reg_state table under
			// the old ID due to collisions.  Purge those.
			logger.debug("Cleaning up orphaned reg_state records...");
			mergeLog.append("Cleaning up orphaned reg_state records with rid " + ms.rid + "\n");
			p = c.prepareStatement("delete from reg_state where state_rid=?");
			p.setInt(1,ms.rid);
			mergeLog.append(p.executeUpdate() + " rows updated.\n");
			p.close();
			
			// reg_phone (phone_rid)
			logger.debug("merging reg_phone");
			mergeLog.append("Changing all phone entries from " + ms.rid + " to " + mt.rid + "\n");
			p = c.prepareStatement("update ignore reg_phone set phone_rid=? where phone_rid=?");
			p.setInt(1,mt.rid);
			p.setInt(2,ms.rid);
			mergeLog.append(p.executeUpdate() + " rows updated.\n");
			p.close();

			logger.debug("cleaning up possible orphaned phone entries");
			mergeLog.append("Cleaning up orphaned phone entries with a rid of " + ms.rid + "\n");
			p = c.prepareStatement("delete from reg_phone where phone_rid=?");
			p.setInt(1,ms.rid);
			mergeLog.append(p.executeUpdate() + " rows updated.\n");
			p.close();

			// reg_address (add_rid)
			logger.debug("merging reg_address");
			mergeLog.append("Changing all address entries from " + ms.rid + " to " + mt.rid + "\n");
			p = c.prepareStatement("update ignore reg_address set add_rid=? where add_rid=?");
			p.setInt(1,mt.rid);
			p.setInt(2,ms.rid);
			mergeLog.append(p.executeUpdate() + " rows updated.\n");
			p.close();
			
			logger.debug("cleaning up possible orphaned address entries");
			mergeLog.append("Cleaning up orphaned phone entries with a rid of " + ms.rid + "\n");
			p = c.prepareStatement("delete from reg_address where add_rid=?");
			p.setInt(1,ms.rid);
			mergeLog.append(p.executeUpdate() + " rows updated.\n");
			p.close();

			// reg_notes (note_rid,note_postrid)
			logger.debug("merging reg_notes");
			p = c.prepareStatement("update reg_notes set note_rid=? where note_rid=?");
			mergeLog.append("Notes merge - Step 1 of 2\n");
			p.setInt(1,mt.rid);
			p.setInt(2,ms.rid);
			mergeLog.append(p.executeUpdate() + " rows updated.\n");
			p.close();

			p = c.prepareStatement("update reg_notes set note_postrid=? where note_postrid=?");
			mergeLog.append("Notes merge - Step 2 of 2\n");
			p.setInt(1,mt.rid);
			p.setInt(2,ms.rid);
			mergeLog.append(p.executeUpdate() + " rows updated.\n");
			p.close();

			// invoices (invoice_payer,invoice_operator,invoice_creator)
			logger.debug("merging invoices");
			p = c.prepareStatement("update invoices set invoice_payer=? where invoice_payer=?");
			mergeLog.append("Updating invoice payer pointers\n");
			p.setInt(1,mt.rid);
			p.setInt(2,ms.rid);
			mergeLog.append(p.executeUpdate() + " rows updated.\n");
			p.close();

			p = c.prepareStatement("update invoices set invoice_operator=? where invoice_operator=?");
			mergeLog.append("Updating invoice operator pointers\n");
			p.setInt(1,mt.rid);
			p.setInt(2,ms.rid);
			mergeLog.append(p.executeUpdate() + " rows updated.\n");
			p.close();
			
			p = c.prepareStatement("update invoices set invoice_creator=? where invoice_creator=?");
			mergeLog.append("Updating invoice creator pointers\n");
			p.setInt(1,mt.rid);
			p.setInt(2,ms.rid);
			mergeLog.append(p.executeUpdate() + " rows updated.\n");
			p.close();
			
			// reg_properaties (prop_rid)
			logger.debug("merging reg_properties");
			mergeLog.append("Processing registrant properties\n");
			p = c.prepareStatement("update reg_properties set prop_rid=? where prop_rid=?");
			p.setInt(1,mt.rid);
			p.setInt(2,ms.rid);
			mergeLog.append(p.executeUpdate() + " rows updated.\n");
			p.close();
			
			// Lets not forget the links.  Anyone linked to us or from us needs to change to the
			// new one...
			
			logger.debug("Merging reg_links...");
			p = c.prepareStatement("update reg_links set link_rid1=? where link_rid1=?");
			mergeLog.append("Merging links, step 1 of 2\n");
			p.setInt(1,mt.rid);
			p.setInt(2,ms.rid);
			mergeLog.append(p.executeUpdate() + " rows updated.\n");
			p.close();

			p = c.prepareStatement("update reg_links set link_rid2=? where link_rid2=?");
			mergeLog.append("Merging links, step 2 of 2\n");
			p.setInt(1,mt.rid);
			p.setInt(2,ms.rid);
			mergeLog.append(p.executeUpdate() + " rows updated.\n");
			p.close();
			
			// reg_master (master_rid)
			logger.debug("merging reg_master");
			mergeLog.append("Disabling old (" + ms.rid + ") master record.\n");
			p = c.prepareStatement("update reg_master " +
					"set master_enabled=false, " +
					"master_mergedto=?, " +
					"master_comment='merged' " +
					"where master_rid=?");
			p.setInt(1,mt.rid);
			p.setInt(2,ms.rid);
			mergeLog.append(p.executeUpdate() + " row(s) updated.\n\n");
			p.close();
			
			// If we're here, we didnt' have any failures.  Commit it!
			logger.debug("Committing merge transaction.");
			mergeLog.append("All data updates successful, committing transaction and exiting.  Done.\n");
			c.commit();
			c.setAutoCommit(true);	// this shouldn't go here, should be after the catch.
		}
		catch (SQLException ex) {
			mergeLog.append("*** Error condition encountered during merge:\n");
			mergeLog.append(ex.getMessage() + "\n");
			mergeLog.append("Attempting rollback...\n");
			if (c != null) {
				try {
					logger.error("SQLException forcing rollback of merge process.");
					logger.error(ex.getMessage());
					logger.error(ex.getCause());
					logger.error(ex.getSQLState());
					logger.error(ex.getStackTrace().toString());
					c.rollback();
					mergeLog.append("Rollback successful - no data was changed.\n");
				} catch(SQLException excep) {
					logger.error("SQLException during rollback of merge: ");
					logger.error(excep.getMessage());
					mergeLog.append("Rollback FAILED - Data may be in an unstable state.\n");
					mergeLog.append(excep.getMessage() + "\n");
				}	
			}
			mergeLog.append("Merge processing exiting.\n");
			return "exit";
		} finally {
			c.close();
		}
		
		// If we're here, we didn't hit an error anywhere.  Now log
		// to the target that a merge has happened.
		History h = historyDAO.create(mt.rid,0,"MERGE",((Registrant)sessionData.get("coconutuser")).rid);
		h.arg1 = ms.rid+"";
		historyDAO.save(h);

		return "exit";
	}


	@Override
	public void setSession(Map<String, Object> session) {
		logger.debug("Receiving session data...");
		this.sessionData = session;
	}
}
