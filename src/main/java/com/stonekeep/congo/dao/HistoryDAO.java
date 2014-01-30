package com.stonekeep.congo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.stonekeep.congo.data.History;

/**
 * Provides access to History data objects.
 * 
 * @author dbs
 */
public class HistoryDAO {
	private final String SQL_LIST_HISTORY = "SELECT * FROM reg_history " +
			"LEFT OUTER JOIN reg_master ON hist_rid=master_rid " +
			"WHERE hist_rid=? ORDER BY hist_id DESC";
	private final String SQL_LIST_ACTIVITY = "SELECT * FROM reg_history " +
			"LEFT OUTER JOIN reg_master ON hist_rid=master_rid " +
			"ORDER BY hist_activity DESC limit 200";
	private static Logger logger = Logger.getLogger(HistoryDAO.class);

	private final DataSource dataSource;

	public HistoryDAO(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void save(History h) throws SQLException {
		logger.debug("Saving history record - rid is " + h.rid + " and cid is " + h.cid);
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		try {
			String sql = "UPDATE reg_history "
					+ "SET hist_rid=?, hist_cid=?, hist_tid=?, "
					+ "hist_activity=?, hist_operator=?, hist_actcode=?,"
					+ "hist_arg1=?, hist_arg2=?, hist_comment=? " + "WHERE hist_id=?";
			p = c.prepareStatement(sql);
			p.setInt(1, h.rid);
			p.setInt(2, h.cid);
			p.setInt(3, h.tid);
			p.setTimestamp(4, h.activity);
			p.setInt(5, h.operator);
			p.setString(6, h.actcode);
			p.setString(7, String.format("%40.40s",h.arg1));
			p.setString(8, String.format("%40.40s",h.arg2));
			p.setString(9,h.comment);
			p.setInt(10, h.id);
			logger.debug(p.executeUpdate() + " rows updated.");
		} finally {
			Closer.close(null, p, c);
		}
	}
	
	public ArrayList<History> list(int rid) throws SQLException {
		logger.debug("Listing history records for registrant " + rid);
		ArrayList<History> tmpList = new ArrayList<History>();
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			p = c.prepareStatement(SQL_LIST_HISTORY) ;
			p.setInt(1,rid);
			r = p.executeQuery();
			History h = new History();
			while (r.next()) {
				h = load(r);
				tmpList.add(h);
			}
		} finally {
			Closer.close(r,p,c);
			c.close();
		}
		logger.debug("Returning " + tmpList.size() + " history records.");
		return tmpList;
	}
	
	public ArrayList<History> listActivity() throws SQLException {
		logger.debug("Listing recent activity in CONGO");
		ArrayList<History> tmpList = new ArrayList<History>();
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			p = c.prepareStatement(SQL_LIST_ACTIVITY) ;
			r = p.executeQuery();
			History h = new History();
			while (r.next()) {
				h = load(r);
				tmpList.add(h);
			}
		} finally {
			Closer.close(r,p,c);
		}
		logger.debug("Returning " + tmpList.size() + " history activity records.");
		p.close();
		return tmpList;
	}

	public History create() throws SQLException {
		Connection c = dataSource.getConnection();
		ResultSet r = null;
		History htmp = new History();
		try {
			String sql = "INSERT into reg_history values ()";
			Statement s = c.createStatement();
			s.execute(sql, Statement.RETURN_GENERATED_KEYS);
			r = s.getGeneratedKeys();
			while (r.next()) {
				Integer i = r.getInt(1);
				logger.debug("Generated key: " + i);
				htmp.id = i;
			}
		} finally {
			Closer.close(r,null,c);
		}
		return htmp;
	}
	
	public History load(ResultSet r) throws SQLException {
		History h = new History();
		h.actcode = r.getString("hist_actcode");
		h.activity = r.getTimestamp("hist_activity");
		h.arg1 = r.getString("hist_arg1");
		h.arg2 = r.getString("hist_arg2");
		String workingString = r.getString("hist_operator");
		// In some of the original data, the operator column had a name, rather than 
		// an registrant id.  For those, discard it and just use 0.
		try {
			h.operator = Integer.parseInt(workingString);
		}
		catch (Exception e) {
			h.operator=0;
		}
		h.rid = r.getInt("hist_rid");
		h.tid = r.getInt("hist_tid");
		h.id = r.getInt("hist_id");
		h.cid = r.getInt("hist_cid");
		h.firstname = r.getString("master_firstname");
		h.lastname = r.getString("master_lastname");
		h.comment = r.getString("hist_comment");
		return h;
	}
	
	public History create(int rid, int cid, String actcode, int operator) throws SQLException {
		History h = create();
		h.rid = rid;
		h.cid = cid;
		h.actcode = actcode;
		h.operator = operator;
		return h;
	}
	
	public HashMap<String,Integer> registrationsByDate(int cid) throws SQLException {
		// This is called by Reports.Bydate to generate numbers based on history records.
		String sql = "SELECT date_format(hist_activity,'%Y-%m-%d'),count('hist_actcode') " +
			"FROM reg_history " +
			"WHERE hist_cid=? AND hist_actcode=? " +
			"GROUP BY date_format(hist_activity,'%Y-%m-%d') " +
			"ORDER BY  hist_activity ";
		HashMap<String,Integer> workingMap = new HashMap<String,Integer>();
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		ResultSet r = null;
		int counter = 0;
		try {
			p = c.prepareStatement(sql);
			p.setInt(1, cid);
			p.setString(2,"REGISTERED");
			r = p.executeQuery();
			while (r.next()) {
				String dateStamp = r.getString(1);
				Integer i = r.getInt(2);
				workingMap.put(dateStamp, i);
				counter++;
			}
			logger.info("registrationsByDate generated " + counter + " entries.");
			
			// This is a hack-fix.  When we went to v2, entries started being put in history 
			// with the actcode 'REGISTER' as oppsosed to 'REGISTERED'.  This should fix that.

			p.setInt(1, cid);
			p.setString(2,"REGISTER");
			r = p.executeQuery();
			counter=0;
			while (r.next()) {
				String dateStamp = r.getString(1);
				Integer i = r.getInt(2);
				workingMap.put(dateStamp, i);
				counter++;
			}
			logger.info("registrationsByDate generated " + counter + " entries.");
			
			// Now count hte voids, and subtract / update them...
			p = c.prepareStatement(sql);
			p.setInt(1, cid);
			p.setString(2,"VOID");
			r = p.executeQuery();
			counter=0;
			while (r.next()) {
				String dateStamp = r.getString(1);
				if (workingMap.containsKey(dateStamp)) {
					workingMap.put(dateStamp, new Integer(workingMap.get(dateStamp).intValue() - r.getInt(2)));
				} else {
					workingMap.put(dateStamp, new Integer(-1 * r.getInt(2)));
					counter++;
				}
			}
		} finally {
			Closer.close(r,p,c);
		}
		return workingMap;
	}
	
}