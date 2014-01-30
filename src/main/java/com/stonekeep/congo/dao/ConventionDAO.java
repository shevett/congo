package com.stonekeep.congo.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.stonekeep.congo.data.Convention;

/**
 * Provides access to Convention data via JDBC.
 */
public class ConventionDAO {
	private static Logger logger = Logger.getLogger(ConventionDAO.class);

	private final DataSource dataSource;

	public ConventionDAO(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Convention getByID(int cid) throws SQLException {
		Connection c = dataSource.getConnection();
		ResultSet rset = null;
		Convention con = null;
		PreparedStatement p = null;
		PreparedStatement p1 = null;
		ResultSet rset1 = null;
		try {
			logger.debug("Fetching convention ID " + cid);
			String sql = "SELECT * FROM con_detail WHERE con_cid=?";
			p = c.prepareStatement(sql);
			p.setInt(1, cid);
			rset = p.executeQuery();
			if (rset.next()) {
				// Got one. Load it.
				con = new Convention();
				loadData(con, rset);
				String sql1 = "SELECT "
						+ "sum(state_registered) as regged, "
						+ "sum(state_badged) as badged, "
						+ "sum(state_checkedin) as checkedin, "
						+ "sum(state_subscribed) as subbed FROM reg_state WHERE state_cid=?";
				p1 = c.prepareStatement(sql1);
				p1.setInt(1, con.conCID);
				rset1 = p1.executeQuery();
				if (rset1.next()) {
					// load 'er up.
					logger.debug("Updated calculated totals for convention " + con.conCID);
					con.numBadged = rset1.getInt("badged");
					con.numCheckedin = rset1.getInt("checkedin");
					con.numRegistered = rset1.getInt("regged");
					con.numSubscribed = rset1.getInt("subbed");
				}
			}
		} finally {
			Closer.close(rset,p,c);
			Closer.close(rset1,p1,c);
		}
		return con;
	}

	public void save(Convention con) throws Exception {
	    logger.debug("save...");
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		try {
			String sql = "UPDATE con_detail SET " + "con_name=?, "
					+ "con_location=?, " + "con_start=?, " + "con_end=?, "
					+ "con_comment=?, " + "con_website=?, " + "con_email=?, "
					+ "con_stylesheet=?, " + "con_badgelayout=?, "
					+ "con_description=?, " + "con_cap=? "
					+ "WHERE con_cid=?";
			p = c.prepareStatement(sql);
			p.setString(1, con.conName);
			p.setString(2, con.conLocation);
			p.setDate(3, (Date) con.conStart);
			p.setDate(4, (Date) con.conEnd);
			p.setString(5, con.conComment);
			p.setString(6, con.conWebsite);
			p.setString(7, con.conEmail);
			p.setString(8, con.conStylesheet);
			p.setString(9, con.conBadgelayout);
			p.setString(10, con.conDescription);
			p.setInt(11,con.conCap);
			p.setInt(12, con.conCID);
			logger.debug(p.executeUpdate() + " rows updated.");
		} finally {
			Closer.close(null,p,c);
		}
	}

	public void create(Convention con) throws Exception {
		logger.debug("Creating new con");
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			String sql = "INSERT INTO con_detail " + "(con_name) "
					+ "VALUES (?)";
			p = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			p.setString(1, con.conName);
			p.execute();
			r = p.getGeneratedKeys();
			while (r.next()) {
				Integer i = r.getInt(1);
				logger.debug("Generated keys: " + i);
				con.conCID = i;
				con.conCap = 0;
				save(con);
			}
			p.close();
		} finally {
			Closer.close(r,p,c);
		}
	}

	public Map<Integer, Convention> list() throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		PreparedStatement p1 = null;
		ResultSet rset = null;
		ResultSet rset1 = null;
		HashMap<Integer, Convention> allTypes = new HashMap<Integer, Convention>();
				
		try {
			String sql = "SELECT * from con_detail where con_cid > 0 order by con_cid ";
			p = c.prepareStatement(sql);
			rset = p.executeQuery();
			logger.debug(p.toString());
			while (rset.next()) {
				Convention row = new Convention();
				loadData(row, rset);
				String sql1 = "SELECT "
						+ "sum(state_registered) as regged, "
						+ "sum(state_badged) as badged, "
						+ "sum(state_checkedin) as checkedin, "
						+ "sum(state_subscribed) as subbed FROM reg_state WHERE state_cid=?";
				p1 = c.prepareStatement(sql1);
				p1.setInt(1, row.conCID);
				rset1 = p1.executeQuery();
				if (rset1.next()) {
					// load 'er up.
					row.numBadged = rset1.getInt("badged");
					row.numCheckedin = rset1.getInt("checkedin");
					row.numRegistered = rset1.getInt("regged");
					row.numSubscribed = rset1.getInt("subbed");
				}
				allTypes.put(row.conCID, row);
			}
			p.close();
		} finally {
			Closer.close(rset,p,c);
			Closer.close(rset1,p1,c);
		}
		return allTypes;
	}

	public void recalculate(Convention con) throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		ResultSet rset = null;
		try {
			String sql = "SELECT "
					+ "sum(state_registered) as regged, "
					+ "sum(state_badged) as badged, "
					+ "sum(state_checkedin) as checkedin, "
					+ "sum(state_subscribed) as subbed FROM reg_state WHERE state_cid=?";
			p = c.prepareStatement(sql);
			p.setInt(1, con.conCID);
			rset = p.executeQuery();
			if (rset.next()) {
				// load 'er up.
				logger.debug("Registered = " + rset.getInt("regged") + ", subbed = " + rset.getInt("subbed"));
				con.numBadged = rset.getInt("badged");
				con.numCheckedin = rset.getInt("checkedin");
				con.numRegistered = rset.getInt("regged");
				con.numSubscribed = rset.getInt("subbed");
			}
		} finally {
			Closer.close(rset, p, c);
		}
	}
	
	public void delete(Integer id) throws SQLException {
		logger.info("Deleting event " + id);
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		try {
			String sql = "DELETE from con_detail WHERE con_cid=?";
			p = c.prepareStatement(sql);
			p.setInt(1, id);
			logger.debug(p.executeUpdate() + " rows updated.");
			p.close();
		} finally {
			Closer.close(null,p,c);
		}
	}

	private static void loadData(Convention where, ResultSet fromwhat)
			throws SQLException {
		where.conCID = fromwhat.getInt("con_cid");
		where.conName = fromwhat.getString("con_name");
		where.conLocation = fromwhat.getString("con_location");
		where.conStart = fromwhat.getDate("con_start");
		where.conEnd = fromwhat.getDate("con_end");
		where.conComment = fromwhat.getString("con_comment");
		where.conWebsite = fromwhat.getString("con_website");
		where.conEmail = fromwhat.getString("con_email");
		where.conStylesheet = fromwhat.getString("con_stylesheet");
		where.conBadgelayout = fromwhat.getString("con_badgelayout");
		where.conDescription = fromwhat.getString("con_description");
		where.conCap = fromwhat.getInt("con_cap");
	}

}
