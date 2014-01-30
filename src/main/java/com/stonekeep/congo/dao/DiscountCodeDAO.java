package com.stonekeep.congo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.stonekeep.congo.data.DiscountCode;

/**
 * DiscountCodeDAO is the data acccess object for the DiscountCode objects.
 */
public class DiscountCodeDAO extends DAO<DiscountCode> {
	
	private static Logger logger = Logger.getLogger(DiscountCodeDAO.class);

	public DiscountCodeDAO(DataSource dataSource) {
		super(dataSource);
	}
	
	public DiscountCode get(int whatcid, String name) throws SQLException {
		logger.debug("Retrieving discountcode '" + name + "' for event " + whatcid);
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		ResultSet rset = null;
		DiscountCode t = null;
		try {
			String sql = "SELECT * FROM config_discountcodes WHERE dc_cid=? AND dc_name=?";

			p = c.prepareStatement(sql);
			p.setInt(1, whatcid);
			p.setString(2, name);
			rset = p.executeQuery();
			if (rset.next()) {
				// Got one. Load it.
				t = new DiscountCode();
				loadData(t, rset);
			}
		} finally {
			Closer.close(rset,p,c);
		}
		return t;
	}

	public void save(DiscountCode t) throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		try {
			String sql = "UPDATE config_discountcodes SET " +
					"dc_desc=?," +
					"dc_note=?, " +
					"dc_type=?, " +
					"dc_factor=?, " + 
					"dc_active=? " +
					"WHERE " +
					"dc_cid=? AND dc_name=?";
			p = c.prepareStatement(sql);
			p.setString(1, t.getDesc());
			p.setString(2, t.getNote());
			p.setString(3, t.getType());
			p.setBigDecimal(4,t.getFactor());
			p.setBoolean(5,t.getActive());
			p.setInt(6,t.getCID());
			p.setString(7,t.getName());
			logger.debug(p.executeUpdate() + " rows updated.");
		} finally {
			Closer.close(null,p,c);
		}
	}

	public void create(DiscountCode t) throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		try {
			String sql = "INSERT INTO config_discountcodes " +
					"(dc_cid,dc_name,dc_desc,dc_note,dc_type,dc_factor,dc_active) " +
					"VALUES " +
					"(?,?,?,?,?,?,?)";
			p = c.prepareStatement(sql);
			p.setInt(1,t.getCID());
			p.setString(2,t.getName());
			p.setString(3,t.getDesc());
			p.setString(4,t.getNote());
			p.setString(5,t.getType());
			p.setBigDecimal(6,t.getFactor());
			p.setBoolean(7,t.getActive());
			logger.debug(p.executeUpdate() + " rows updated.");
		} finally {
			Closer.close(null,p,c);
		}
	}
	
	public void delete(String whatDiscountCode) throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		try {
			String sql = "DELETE FROM config_discountcodes WHERE dc_name=?";
			p = c.prepareStatement(sql);
			p.setString(1,whatDiscountCode);
			logger.debug(p.executeUpdate() + " rows updated.");
		} finally {
			Closer.close(null,p,c);
		}
	}

	public Map<String, DiscountCode> list(int whatcid) throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		ResultSet rset = null;
		HashMap<String, DiscountCode> alldiscountcodes = new HashMap<String, DiscountCode>();
		try {
			String sql = "SELECT * from config_discountcodes WHERE dc_cid=0 OR dc_cid=?";
			p = c.prepareStatement(sql);
			p.setInt(1, whatcid);
			rset = p.executeQuery();
			while (rset.next()) {
				DiscountCode row = new DiscountCode();
				loadData(row, rset);
				alldiscountcodes.put(row.getName(), row);
			}
		} finally {
			Closer.close(rset,p,c);
		}
		return alldiscountcodes;
	}

	protected void loadData(DiscountCode where, ResultSet fromwhat)
			throws SQLException {
		where.setCID(fromwhat.getInt("dc_cid"));
		where.setName(fromwhat.getString("dc_name"));
		where.setDesc(fromwhat.getString("dc_desc"));
		where.setNote(fromwhat.getString("dc_note"));
		where.setType(fromwhat.getString("dc_type"));
		where.setFactor(fromwhat.getBigDecimal("dc_factor"));
		where.setActive(fromwhat.getBoolean("dc_active"));
		where.setLastmodified(fromwhat.getTimestamp("dc_lastmodified"));
	}
}
