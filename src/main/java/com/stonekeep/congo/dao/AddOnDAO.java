package com.stonekeep.congo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.stonekeep.congo.data.AddOn;
import com.stonekeep.congo.data.AddOnConfiguration;
import com.stonekeep.congo.data.Data;

/**
 * AddOnDAO is the data access object for the AddOn and AddOnConfiguration objects.
 */
public class AddOnDAO extends DAO<AddOn> {
	private static Logger logger = Logger.getLogger(AddOnDAO.class);
//	private final DataSource dataSource;

	public AddOnDAO(DataSource dataSource) {
		super(dataSource);
	}
	
	public AddOn getAddOn(int whatcid, int whatrid, String name)
			throws SQLException {
		logger.debug("Retrieving AddOn '" + name + "' for event " + whatcid + " for registrant " + whatrid);
		Connection c = dataSource.getConnection();
		try {
			String sql = "SELECT * FROM reg_addons WHERE addon_cid=? AND addon_name=? AND addon_rid=?";
			PreparedStatement p = null;
			ResultSet rset = null;
			AddOn t = null;
			p = c.prepareStatement(sql);
			p.setInt(1, whatcid);
			p.setInt(2, whatrid);
			p.setString(3, name);
			rset = p.executeQuery();
			if (rset.next()) {
				// Got one. Load it.
				t = new AddOn();
				loadAddOn(t, rset);
			}
			p.close();
			return t;
		} finally {
			c.close();
		}
	}

	public AddOnConfiguration getAddOnConfiguration(int whatcid, String name)
		throws SQLException {
		logger.debug("Retrieving AddOn '" + name + "' for event " + whatcid);
		Connection c = dataSource.getConnection();
		try {
			String sql = "SELECT * FROM config_addons WHERE addon_cid=? AND addon_name=?";
			PreparedStatement p = null;
			ResultSet rset = null;
			AddOnConfiguration t = null;
			p = c.prepareStatement(sql);
			p.setInt(1, whatcid);
			p.setString(2, name);
			rset = p.executeQuery();
			if (rset.next()) {
				// Got one. Load it.
				t = new AddOnConfiguration();
				loadAddOnConfiguration(t, rset);
			}
			p.close();
			return t;
		} finally {
			c.close();
		}
	}

	public void saveAddOn(AddOn t) throws SQLException {
		Connection c = dataSource.getConnection();
		try {
			String sql = "UPDATE addons SET " +
				"addon_cid=?," +
				"addon_rid=?," +
				"addon_name=?," +
				"addon_quantity=?," +
				"addon_invoiceid=? " +
				"WHERE " +
				"addon_cid=? AND addon_rid=? AND addon_name=?";
			PreparedStatement p;
			p = c.prepareStatement(sql);
			p.setInt(1,t.getCid());
			p.setInt(2,t.getRid());
			p.setString(3,t.getName());
			p.setInt(4,t.getQuantity());
			p.setInt(5,t.getInvoiceid());
			logger.debug(p.executeUpdate() + " rows updated.");
			p.close();
		} finally {
			c.close();
		}
	}

	public void createAddOn(AddOn t) throws SQLException {
		Connection c = dataSource.getConnection();
		try {
			String sql = "INSERT INTO addons " +
					"(addon_cid,addon_rid,addon_name,addon_quantity,addon_invoiceid) " +
					"VALUES " +
					"(?,?,?,?)";
			PreparedStatement p;
			p = c.prepareStatement(sql);
			p.setInt(1,t.getCid());
			p.setInt(2,t.getRid());
			p.setString(3,t.getName());
			p.setInt(4,t.getQuantity());
			p.setInt(5,t.getInvoiceid());
			logger.debug(p.executeUpdate() + " rows updated.");
			p.close();
		} finally {
			c.close();
		}
	}

	public List<AddOn> listAddOn(int whatcid,int whatrid) throws SQLException {
		Connection c = dataSource.getConnection();
		try {
			String sql = "SELECT * from addons WHERE addon_rid=? AND addon_cid=?";
			PreparedStatement p;
			p = c.prepareStatement(sql);
			p.setInt(1, whatcid);
			ResultSet rset = p.executeQuery();
			List<AddOn> allAddOns = new ArrayList<AddOn>();
			while (rset.next()) {
				AddOn row = new AddOn();
				loadAddOn(row, rset);
				allAddOns.add(row);
			}
			p.close();
			return allAddOns;
		} finally {
			c.close();
		}
	}
	
	public ArrayList<AddOnConfiguration> listAddOnConfigurations(int whatcid) throws SQLException {
		Connection c = dataSource.getConnection();
		try {
			String sql = "SELECT * from addons WHERE addon_rid=? AND addon_cid=?";
			PreparedStatement p;
			p = c.prepareStatement(sql);
			p.setInt(1, whatcid);
			ResultSet rset = p.executeQuery();
			ArrayList<AddOnConfiguration> allAddOns = new ArrayList<AddOnConfiguration>();
			while (rset.next()) {
				AddOnConfiguration row = new AddOnConfiguration();
				loadAddOnConfiguration(row, rset);
				allAddOns.add(row);
			}
			p.close();
			return allAddOns;
		} finally {
			c.close();
		}
	}

	private void loadAddOn(AddOn where, ResultSet fromwhat)
			throws SQLException {
		where.setCid(fromwhat.getInt("addon_cid"));
		where.setRid(fromwhat.getInt("addon_rid"));
		where.setName(fromwhat.getString("addon_name"));
		where.setQuantity(fromwhat.getInt("addon_quantity"));
		where.setInvoiceid(fromwhat.getInt("addon_invoiceid"));
		where.setLastmodified(fromwhat.getTimestamp("addon_lastmod"));
	}
	
	private void loadAddOnConfiguration(AddOnConfiguration where, ResultSet fromwhat) 
		throws SQLException {
		where.setCid(fromwhat.getInt("addon_cid"));
		where.setName(fromwhat.getString("addon_name"));
		where.setDisplayName(fromwhat.getString("addon_displayname"));
		where.setDescription(fromwhat.getString("addon_description"));
		where.setPrice(fromwhat.getBigDecimal("addon_price"));
		where.setActive(fromwhat.getBoolean("addon_active"));
		where.setClosed(fromwhat.getBoolean("addon_closed"));
		where.setCap(fromwhat.getInt("addon_cap"));
		where.setDetail(fromwhat.getString("addon_detail"));
		where.setLastmodified(fromwhat.getTimestamp("addon_lastmod")) ;
	}

	@Override
	public void create(AddOn d) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Data get(int whatcid, String name) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, AddOn> list(int whatcid) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void loadData(AddOn where, ResultSet fromwhat)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(AddOn d) throws SQLException {
		// TODO Auto-generated method stub
		
	}
}
