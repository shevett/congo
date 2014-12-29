package com.stonekeep.congo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.stonekeep.congo.data.History;
import com.stonekeep.congo.data.Registrant;

public class RegistrantDAO {
	private static Logger logger = Logger.getLogger(RegistrantDAO.class);
	private final DataSource dataSource;
	private final StateDAO stateDAO;
	public final AddressDAO addressDAO;
	public final EmailDAO emailDAO;
	private final NoteDAO noteDAO;
	public final PhoneDAO phoneDAO;
	public final HistoryDAO historyDAO;
	public boolean includeDisabled = false;
	public boolean registeredOnly = false;

	private String trimToLength(String original, int l) {
		if (original == null) {
			return new String("");
		}
		if (original.length() < l) {
			return original;
		}
		logger.warn("Field too long.  Trimming " + original + " to length " + l);
		StringBuffer sb = new StringBuffer(l);
		sb.append(original);
		return sb.substring(0, l).trim().toString();
	}

	public RegistrantDAO(DataSource dataSource, StateDAO stateDAO,
			AddressDAO addressDAO, EmailDAO emailDAO, NoteDAO noteDAO,
			PhoneDAO phoneDAO, HistoryDAO historyDAO) {
		this.dataSource = dataSource;
		this.stateDAO = stateDAO;
		this.addressDAO = addressDAO;
		this.emailDAO = emailDAO;
		this.noteDAO = noteDAO;
		this.phoneDAO = phoneDAO;
		this.historyDAO = historyDAO;
	}

	public Registrant getByID(int registrantID) throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		ResultSet rset = null;
		Registrant r = null;
		try {
			logger.debug("Loading up registrant " + registrantID);
			String sql = "select * from reg_master where master_rid=?";
			p = c.prepareStatement(sql);
			p.setInt(1, registrantID);
			rset = p.executeQuery();
			if (rset.next()) {
				// Got one. Load it.
				r = new Registrant();
				load(r, rset);
			}
		} finally {
			Closer.close(rset, p, c);
		}
		return r;
	}

	private void load(Registrant r, ResultSet rset) throws SQLException {
		logger.debug("Loading full registrant details for " + r.rid);
		r.firstName = rset.getString("master_firstname");
		r.lastName = rset.getString("master_lastname");
		r.company = rset.getString("master_company");
		r.badgeName = rset.getString("master_badgename");
		r.password = rset.getString("master_password");
		r.rid = rset.getInt("master_rid");
		r.enabled = rset.getBoolean("master_enabled");
		r.mergedTo = rset.getInt("master_mergedto");
		r.comment = rset.getString("master_comment");

		// This registrant should also have their state information. Load it too
		TreeMap t = new TreeMap(stateDAO.list(r.rid));
		r.conList = t.descendingMap();
		// r.conList = stateDAO.list(r.rid);

		// Load up any NOTICE-type notes for this person.
		r.noteList = noteDAO.listNotices(r.rid);

		// Count how many notes there are total
		r.totalNotes = noteDAO.list(r.rid).size();

		r.phoneList = phoneDAO.list(r.rid);
		r.addressList = addressDAO.list(r.rid);
		r.emailList = emailDAO.list(r.rid);
		logger.debug("phoneList has " + r.phoneList.size() + " entries");
	}

	/**
	 * A simplified load() method that does not load ancillary data such as
	 * phone, address, or email data so it can be used in browse or list
	 * results.
	 * 
	 * @param r
	 *            The registrans object to load into
	 * @param rset
	 *            The SQL Resultset containing the query result
	 * @throws SQLException
	 */
	private void loadSimple(Registrant r, ResultSet rset) throws SQLException {
		r.firstName = rset.getString("master_firstname");
		r.lastName = rset.getString("master_lastname");
		r.company = rset.getString("master_company");
		r.badgeName = rset.getString("master_badgename");
		r.password = rset.getString("master_password");
		r.rid = rset.getInt("master_rid");
		r.enabled = rset.getBoolean("master_enabled");
		r.mergedTo = rset.getInt("master_mergedto");
		r.comment = rset.getString("master_comment");
	}

	public Registrant create() throws SQLException {
		Connection c = dataSource.getConnection();
		Registrant rtmp = new Registrant();
		ResultSet r;
		try {
			String sql = "INSERT into reg_master values ()";
			Statement s = c.createStatement();
			s.execute(sql, Statement.RETURN_GENERATED_KEYS);
			r = s.getGeneratedKeys();
			while (r.next()) {
				Integer i = r.getInt(1);
				logger.debug("Generated keys: " + i);
				rtmp.rid = i;
			}
		} finally {
			Closer.close(null, null, c);
		}
		return rtmp;
	}

	public void delete(int rid) throws SQLException {
		logger.debug("Deleting registrant " + rid);
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		try {
			String sql = "DELETE from reg_master WHERE master_rid=?";
			p = c.prepareStatement(sql);
			p.setInt(1, rid);
			logger.debug(p.executeUpdate() + " rows updated.");
		} finally {
			Closer.close(null, p, c);
		}
	}

	/*
	 * Logs any changes to a registrant into the history table by retrieving the
	 * old registrant, comparing values with the new one, and making history
	 * entries for everything that's different
	 * 
	 * @param newRegistrant Updated record to be scanned
	 * 
	 * @param loggingUser A registrant ID of the person making the change
	 * 
	 * @param cid The convention ID.
	 */
	public void logChanges(Registrant newRegistrant, int loggingUser, int cid)
			throws SQLException {
		// Retrieve what's in the database for this registrant
		Registrant oldRegistrant = getByID(newRegistrant.rid);
		if (!oldRegistrant.firstName.equals(newRegistrant.firstName)) {
			History h = historyDAO.create(newRegistrant.rid, cid, "CHANGE",
					loggingUser);
			h.comment = "First Name changed";
			h.arg1 = oldRegistrant.firstName;
			h.arg2 = newRegistrant.firstName;
			historyDAO.save(h);
		}
		if (!oldRegistrant.lastName.equals(newRegistrant.lastName)) {
			History h = historyDAO.create(newRegistrant.rid, cid, "CHANGE",
					loggingUser);
			h.comment = "Last Name changed";
			h.arg1 = oldRegistrant.lastName;
			h.arg2 = newRegistrant.lastName;
			historyDAO.save(h);
		}
		if (!oldRegistrant.badgeName.equals(newRegistrant.badgeName)) {
			History h = historyDAO.create(newRegistrant.rid, cid, "CHANGE",
					loggingUser);
			h.comment = "Badge Name changed";
			h.arg1 = oldRegistrant.badgeName;
			h.arg2 = newRegistrant.badgeName;
			historyDAO.save(h);
		}
		if (!oldRegistrant.company.equals(newRegistrant.company)) {
			History h = historyDAO.create(newRegistrant.rid, cid, "CHANGE",
					loggingUser);
			h.comment = "Company changed";
			h.arg1 = oldRegistrant.company;
			h.arg2 = newRegistrant.company;
			historyDAO.save(h);
		}
	}

	public void save(Registrant r) throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		try {
			String sql = "UPDATE reg_master SET " + "master_rid=?,"
					+ "master_firstname=?," + "master_lastname=?,"
					+ "master_badgename=?, " + "master_company=?,"
					+ "master_enabled=?, " + "master_comment=?,"
					+ "master_mergedto=? " + "WHERE master_rid=?";
			p = c.prepareStatement(sql);
			p.setInt(1, r.rid);
			p.setString(2, trimToLength(r.firstName, 30));
			p.setString(3, trimToLength(r.lastName, 40));
			p.setString(4, trimToLength(r.badgeName, 40));
			p.setString(5, trimToLength(r.company, 30));
			p.setBoolean(6, r.enabled);
			p.setString(7, trimToLength(r.comment, 254));
			p.setInt(8, r.mergedTo);
			p.setInt(9, r.rid);
			logger.debug(p.executeUpdate() + " rows updated.");
		} finally {
			Closer.close(null, p, c);
		}
	}

	public void setPassword(Registrant r, String newPassword)
			throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		try {
			logger.trace("Setting password for " + r.rid + " to " + newPassword);
			String sql = "UPDATE reg_master SET "
					+ "master_password=old_password(?) " + "WHERE master_rid=?";
			p = c.prepareStatement(sql);
			p.setString(1, newPassword);
			p.setInt(2, r.rid);
			logger.debug("Setting password : " + p.executeUpdate()
					+ " rows updated.");
			p.close();

			logger.debug("Saving history record...");
			History h = historyDAO.create(r.rid, 0, "CHANGE", r.rid);
			h.comment = "Password changed";
			historyDAO.save(h);
		} finally {
			Closer.close(null, p, c);
		}
	}

	public Registrant searchByBadgeName(String bn) throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		Registrant r = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * from reg_master WHERE master_badgename LIKE ?";
			p = c.prepareStatement(sql);
			p.setString(1, "%" + bn + "%");
			rs = p.executeQuery();
			if (rs.next()) {
				r = new Registrant();
				loadSimple(r, rs);
			}
		} finally {
			Closer.close(rs, p, c);
		}
		return r;
	}

	public Registrant searchByExactBadgeName(String bn) throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		Registrant r = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * from reg_master WHERE master_badgename = ?";
			p = c.prepareStatement(sql);
			p.setString(1, bn);
			rs = p.executeQuery();
			if (rs.next()) {
				r = new Registrant();
				loadSimple(r, rs);
			}
		} finally {
			Closer.close(rs, p, c);
		}
		return r;
	}

	public Registrant searchbyEmail(String em) throws SQLException {
		logger.debug("Searching for email address " + em);
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		ResultSet rs = null;
		Registrant r = null;
		try {
			String sql = "SELECT * from reg_email WHERE email_address=?";
			int rid = 0;
			p = c.prepareStatement(sql);
			p.setString(1, em);
			rs = p.executeQuery();
			if (rs.next()) {
				rid = rs.getInt("email_rid");
				logger.debug("Found rid " + rid);
				r = this.getByID(rid);
			}
		} finally {
			Closer.close(rs, p, c);
		}
		logger.debug("Returning registrant with value " + r);
		return r;
	}

	/**
	 * Generate a list of Registrant objects based on a generic search query.
	 * 
	 * @param whatString
	 * @param cid
	 * @return
	 * @throws SQLException
	 */
	public Object[] searchExtended(String whatString, int cid, String regtype,
			int registered, int badged, int checkedin, int skip)
			throws SQLException {
		logger.debug("Searching for any match against " + whatString);
		Connection c = dataSource.getConnection();
		ArrayList<Registrant> tmpArray = new ArrayList<Registrant>();
		Registrant r = null;
		PreparedStatement p = null;
		ResultSet rs = null;
		int fullCount = 0;
		try {
			StringBuffer sql = new StringBuffer(
					"SELECT SQL_CALC_FOUND_ROWS * from reg_master "
							+ "LEFT OUTER JOIN reg_state ON (state_rid=master_rid) AND "
							+ "(state_cid = ?) " + "WHERE "
							+ "((master_firstname LIKE ?) OR "
							+ "(master_lastname LIKE ?) OR "
							+ "(master_badgename LIKE ?) OR "
							+ "(master_company LIKE ?)) AND "
							+ "state_registered != ? AND "
							+ "state_badged != ? AND "
							+ "state_checkedin != ? ");
			if (regtype != null) {
				sql.append("AND state_regtype LIKE ? ");
			}
			sql.append("ORDER BY master_lastname, master_firstname LIMIT ?,200");

			logger.debug("Before preparing, sql is " + sql);
			p = c.prepareStatement(sql.toString());
			p.setInt(1, cid);
			p.setString(2, whatString);
			p.setString(3, whatString);
			p.setString(4, whatString);
			p.setString(5, whatString);

			// This is sort of whacked - pass -1 for 'registered' to return all.
			logger.debug("registered value is " + registered);
			if (registered == -1)
				p.setInt(6, -1);
			if (registered == 0)
				p.setInt(6, 1);
			if (registered == 1)
				p.setInt(6, 0);

			// Continued whackedness - pass -1 for 'badged' to return all.
			logger.debug("badged value is " + badged);
			if (badged == -1)
				p.setInt(7, -1);
			if (badged == 0)
				p.setInt(7, 1);
			if (badged == 1)
				p.setInt(7, 0);

			// Continued whackedness - pass -1 for 'checkedin' to return all.
			logger.debug("checkedin value is " + checkedin);
			if (checkedin == -1)
				p.setInt(8, -1);
			if (checkedin == 0)
				p.setInt(8, 1);
			if (checkedin == 1)
				p.setInt(8, 0);

			if (regtype != null) {
				p.setString(9, (regtype == null) ? "%" : regtype);
				p.setInt(10, skip);
			} else {
				p.setInt(9, skip);
			}
			rs = p.executeQuery();

			while (rs.next()) {
				r = new Registrant();
				loadSimple(r, rs);
				updateCurrentState(r, cid); // make sure we have a regtype.
				tmpArray.add(r);
			}

			// Now get how many rows were returned by that call...
			Statement s = c.createStatement();
			rs = s.executeQuery("select FOUND_ROWS()");
			rs.next();
			fullCount = rs.getInt(1);
			logger.debug("Total of " + fullCount + " rows.");
			s.close();
			logger.debug("list() complete - returning " + tmpArray.size()
					+ " elements.");
		} finally {
			Closer.close(rs, p, c);
		}
		return new Object[] { new Integer(fullCount), tmpArray };
	}

	public Object[] searchByAny(String whatString, int cid, int skipvalue)
			throws SQLException {
		logger.debug("Searching for any match against " + whatString
				+ " - includeDisabled is " + includeDisabled);
		Connection c = dataSource.getConnection();
		ArrayList<Registrant> tmpArray = new ArrayList<Registrant>();
		Registrant r = null;
		PreparedStatement p = null;
		ResultSet rs = null;
		int fullCount = 0;
		try {
			StringBuffer sql = new StringBuffer(
					"SELECT SQL_CALC_FOUND_ROWS * from reg_master "
							+ "LEFT OUTER JOIN reg_state ON (state_rid=master_rid) AND (state_cid=?) "
							+ "LEFT OUTER JOIN reg_email ON (email_rid=master_rid) "
							+ "WHERE " + "((master_firstname LIKE ?) OR "
							+ "(master_lastname LIKE ?) OR "
							+ "(master_badgename LIKE ?) OR "
							+ "(email_address LIKE ?) OR "
							+ "(master_company LIKE ?))");

			// This block can only be triggered if the RegistrantDAOBuilder was
			// used and this dao is an instance.
			if (includeDisabled) {
				sql.append("AND (master_enabled=FALSE OR master_enabled=TRUE) ");
			} else {
				sql.append("AND (master_enabled=TRUE) ");
			}

			if (registeredOnly) {
				sql.append("AND (state_registered=TRUE) ");
			}

			sql.append("GROUP BY master_rid ORDER BY master_lastname, master_firstname LIMIT ?");

			logger.debug("Before preparing, sql is " + sql);
			p = c.prepareStatement(sql.toString());
			p.setInt(1, cid);
			p.setString(2, whatString);
			p.setString(3, whatString);
			p.setString(4, whatString);
			p.setString(5, whatString);
			p.setString(6, whatString);
			p.setInt(7, skipvalue);

			rs = p.executeQuery();

			while (rs.next()) {
				r = new Registrant();
				loadSimple(r, rs);
				updateCurrentState(r, cid); // make sure we have a regtype.
				tmpArray.add(r);
				logger.debug("Array now " + tmpArray.size()
						+ " elements, just added: " + r.firstName + " "
						+ r.lastName);
			}
			p.close();

			// Now get how many rows were returned by that call...
			Statement s = c.createStatement();
			rs = s.executeQuery("select FOUND_ROWS()");
			rs.next();
			fullCount = rs.getInt(1);
			logger.debug("Total of " + fullCount + " rows.");
			s.close();
			logger.debug("list() complete - returning " + tmpArray.size()
					+ " elements.");
		} finally {
			Closer.close(rs, null, c);
		}
		return new Object[] { new Integer(fullCount), tmpArray };
	}

	/*
	 * A method for updating the currentstate State record on a registrant.
	 */
	public void updateCurrentState(Registrant r, int cid) throws SQLException {
		r.currentState = stateDAO.getState(cid, r.rid);
		if (r.currentState != null) {
			logger.debug("Setting current state regtype to "
					+ r.currentState.getRegtype());
		}
	}

	// Check to see if the password supplied with this registrant matches. We
	// have
	// to actually ask the database this question, because passwords are
	// encrypted
	// by MySQL.
	public boolean checkPassword(Registrant r, String password)
			throws SQLException {
		final String sql = "select master_rid from reg_master where master_rid=? and master_password=old_password(?)";

		// Do some sanity checks and don't let goofy password situations stand:
		if (password.length() < 1) {
			logger.warn("Won't even try to validate a blank password.");
			return false;
		}

		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		ResultSet rs = null;

		try {
			logger.debug("Trying to check password ---" + password + "---");
			p = c.prepareStatement(sql.toString());
			p.setInt(1, r.rid);
			p.setString(2, password);
			rs = p.executeQuery();
			if (rs.next()) {
				logger.debug("Password validation for registrant " + r.rid
						+ " successful.");
				return true;
			} else {
				logger.warn("Password validation for registrant " + r.rid
						+ " FAILED.");
				return false;
			}
		} finally {
			Closer.close(rs, p, c);
		}
	}

	public void repair(int whatrid) throws SQLException {
		final String sqlphone = "update reg_phone set phone_location='Home' where phone_rid=? and phone_location=''";
		final String sqlemail = "update reg_email set email_location='Home' where email_rid=? and email_location=''";

		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		try {

			p = c.prepareStatement(sqlphone);
			p.setInt(1, whatrid);
			p.execute();
			p.close();

			p = c.prepareStatement(sqlemail);
			p.setInt(1, whatrid);
			p.execute();
			p.close();

		} finally {
			Closer.close(null, p, c);
		}
	}

}
