package com.stonekeep.congo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.stonekeep.congo.data.State;

/**
 * Stores and provides the options associated to individual registrants with
 * regards to conventions.
 */
public class StateDAO {
	private static Logger logger = Logger.getLogger(StateDAO.class);
	private final DataSource dataSource;

	public StateDAO(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public State getState(int conventionID, int registrantID)
			throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		ResultSet rset = null;
		State state = null;
		try {
			logger.debug("Fetching state for cid " + conventionID + " and rid "
					+ registrantID);
			String sql = "SELECT *,con_detail.con_name as cn "
					+ "FROM reg_state, con_detail "
					+ "WHERE state_cid=con_cid "
					+ "AND state_cid=? AND state_rid=?";
			p = c.prepareStatement(sql);
			p.setInt(1, conventionID);
			p.setInt(2, registrantID);
			rset = p.executeQuery();
			if (rset.next()) {
				state = new State();
				loadData(state, rset);
			}
		} finally {
		    Closer.close(rset, p, c); 
		}
		return state;
	}

	public void save(State s) throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		try {
			String sql = "UPDATE reg_state SET " 
					+ "state_subscribed=?, "
					+ "state_registered=?, " 
					+ "state_badged=?, "
					+ "state_checkedin=?, " 
					+ "state_regtype=? "
					+ "WHERE state_cid=? AND state_rid=?";
			 p = c.prepareStatement(sql);
			p.setBoolean(1, s.getSubscribed());
			p.setBoolean(2, s.getRegistered());
			p.setBoolean(3, s.getBadged());
			p.setBoolean(4, s.getCheckedin());
			p.setString(5, s.getRegtype());
			p.setInt(6, s.getCid());
			p.setInt(7, s.getRid());
			logger.debug(p.executeUpdate() + " rows updated.");
		} finally {
			Closer.close(null,p,c);
		}
	}

	public void create(State s) throws Exception {
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		try {
			String sql = "INSERT INTO reg_state "
					+ "(state_cid,state_rid,state_registered,state_badged,state_checkedin,state_subscribed,state_regtype) "
					+ "VALUES (?,?,?,?,?,?,?)";
			p = c.prepareStatement(sql);
			p.setInt(1, s.getCid());
			p.setInt(2, s.getRid());
			p.setBoolean(3, s.getRegistered());
			p.setBoolean(4, s.getBadged());
			p.setBoolean(5, s.getCheckedin());
			p.setBoolean(6, s.getSubscribed());
			p.setString(7, s.getRegtype());
			logger.debug(p.executeUpdate() + " rows updated.");
		} finally {
			Closer.close(null,p,c);
		}
	}

	public Map<Integer, State> list(int whatrid) throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		ResultSet rset = null;
		HashMap<Integer, State> allStates = null;
		try {
			String sql = "SELECT *,con_detail.con_name AS cn "
					+ "FROM reg_state,con_detail "
					+ "WHERE con_detail.con_cid = reg_state.state_cid "
					+ "AND state_rid=? " + "ORDER BY state_cid DESC";
			p = c.prepareStatement(sql);
			p.setInt(1, whatrid);
			rset = p.executeQuery();
			allStates = new HashMap<Integer, State>();
			while (rset.next()) {
				State row = new State();
				loadData(row, rset);
				allStates.put(new Integer(row.cid), row);
			}
			logger.debug("allStates is " + allStates);
		} finally {
		    Closer.close(rset, p, c); 
		}
		return allStates;
	}

	private void loadData(State where, ResultSet fromwhat) throws SQLException {
		where.rid = fromwhat.getInt("state_rid");
		where.cid = fromwhat.getInt("state_cid");
		where.registered = fromwhat.getBoolean("state_registered");
		where.badged = fromwhat.getBoolean("state_badged");
		where.checkedin = fromwhat.getBoolean("state_checkedin");
		where.regtype = fromwhat.getString("state_regtype");
		where.subscribed = fromwhat.getBoolean("state_subscribed");
		where.eventname = fromwhat.getString("cn");
	}

	public void updateWithEvent(int cid, int rid) throws SQLException {
		logger.debug("Running updateWithEvent(" + cid + "," + rid
				+ ") to make sure we have a state row for this person.");
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		try {
			String sql = "INSERT INTO " + "reg_state (state_rid,state_cid) "
					+ "VALUES (?,?) ";
			p = c.prepareStatement(sql);
			p.setInt(1, rid);
			p.setInt(2, cid);
			// There's a problem here.  In one version of the MySQL driver, there's a SQLIntegrityConstraintViolationException.
			// In another, it's a MySQLIntegrityConstraintViolationException.  Irritating.  I'm going to just catch 
			// an 'exception' and Deal.
			try {
				p.execute();
			} catch (Exception e) {
				 //This is okay, it means there was a state record already for this.
				logger.debug("Exception is " + e.getMessage());
				logger.warn("Constraint violation updating #" + rid + " - already have a state record here, this is okay.");
			}
		} finally {
			Closer.close(null, p, c);
		}
	}
	
	public void dump(State s) {
		logger.debug("State: cid " + s.cid + ", rid " + s.rid);
		logger.debug("subscribed " + s.subscribed);
		logger.debug("registered " + s.registered);
		logger.debug("checkedin " + s.checkedin);
		logger.debug("badged " + s.badged);
		logger.debug("regtype " + s.regtype);
	}
}
