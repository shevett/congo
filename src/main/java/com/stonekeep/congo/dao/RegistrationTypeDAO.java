package com.stonekeep.congo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.stonekeep.congo.data.RegistrationType;

/**
 * RegistrationTypeDAO is the data acccess object for the Registration Type.
 */
public class RegistrationTypeDAO  {
	private static Logger logger = Logger.getLogger(RegistrationTypeDAO.class);
	private final DataSource dataSource;

	public RegistrationTypeDAO(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public RegistrationType getRegistrantType(int whatcid, String name)
			throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		ResultSet rset = null;
		RegistrationType regType = null;
		try {
			String sql = "SELECT * FROM con_regtypes WHERE reg_cid=? AND reg_name=?";

			p = c.prepareStatement(sql);
			p.setInt(1, whatcid);
			p.setString(2, name);
			rset = p.executeQuery();
			if (rset.next()) {
				// Got one. Load it.
				regType = new RegistrationType();
				loadData(regType, rset);
			}
		} finally {
			Closer.close(rset,p,c);
		}
		return regType;

	}

	public void save(RegistrationType r) throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		logger.debug("day array for sunday is " + (r.getRegDayArray())[0]);
		updateDayMap(r);
		try {
			String sql = "UPDATE con_regtypes SET " + "reg_cost=?,"
					+ "reg_desc=?, reg_print=?, "
					+ "reg_sequence=?, reg_active=? ,"
					+ "reg_discountcode=?, "
					+ "reg_banner=?, "
					+ "reg_comp=?, "
					+ "reg_daymap=? " 
					+ "WHERE reg_cid=? AND reg_name=?";
			p = c.prepareStatement(sql);
			p.setBigDecimal(1, r.getRegCost());
			p.setString(2, r.getRegDesc());
			p.setString(3, r.getRegPrint());
			p.setInt(4,r.getRegSequence());
			p.setBoolean(5, r.getRegActive());
			if (r.getRegDiscountCode() != null && r.getRegDiscountCode().length() < 1) {
				p.setString(6,null);
			} else {
				p.setString(6,r.getRegDiscountCode());
			}
			if (r.getRegBanner() != null && r.getRegBanner().length() < 1) {
				p.setString(7,null);
			} else {
				p.setString(7,r.getRegBanner());
			}
			p.setBoolean(8,r.getRegComp());
			p.setInt(9,r.getRegDayMap());
			p.setInt(10, r.getRegCID());
			p.setString(11, r.getRegName());
			logger.debug(p.executeUpdate() + " rows updated.");
		} finally {
			Closer.close(null,p,c);
		}
	}

	public void create(RegistrationType r) throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		updateDayMap(r);
		try {
			String sql = "INSERT INTO con_regtypes "
					+ "(reg_name,reg_desc,reg_cost,reg_print,reg_cid,reg_sequence,reg_active,reg_discountcode,reg_banner,reg_comp, reg_daymap) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
			p = c.prepareStatement(sql);
			p.setString(1, r.getRegName());
			p.setString(2, r.getRegDesc());
			p.setBigDecimal(3, r.getRegCost());
			p.setString(4, r.getRegPrint());
			p.setInt(5, r.getRegCID());
			p.setInt(6, r.getRegSequence());
			p.setBoolean(7,r.getRegActive());
			if (r.getRegDiscountCode() != null && r.getRegDiscountCode().length() < 1) {
				p.setString(8,null);
			} else {
				p.setString(8,r.getRegDiscountCode());
			}
			if (r.getRegBanner() != null && r.getRegBanner().length() < 1) {
				p.setString(9,null);
			} else {
				p.setString(9,r.getRegBanner());
			}
			p.setBoolean(10,r.getRegComp());
			p.setInt(11,r.getRegDayMap());

			logger.debug(p.executeUpdate() + " rows updated.");
		} finally {
			Closer.close(null,p,c);
		}
	}
	
	public void delete(int cid, String regtype) throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		try {
			p = c.prepareStatement("delete from con_regtypes where reg_cid=? and reg_name=?");
			p.setInt(1,cid);
			p.setString(2,regtype);
			p.execute();
		} finally {
			Closer.close(null,p,c);
		}
	}

	public LinkedHashMap<String, RegistrationType> list(int whatcid) throws SQLException {
		logger.debug(".list(" + whatcid + ") : Generating regtype list");
		Connection c = dataSource.getConnection();
		PreparedStatement p= null;
		LinkedHashMap<String, RegistrationType> allTypes = new LinkedHashMap<String, RegistrationType>();
		ResultSet rset = null;
		try {
			// String sql = "SELECT * from con_regtypes where reg_cid=? ORDER BY reg_sequence";
			String sql = "SELECT reg_cid,reg_name,reg_desc,reg_print,reg_cost,reg_expire,reg_sequence," +
					"reg_active,reg_discountcode, reg_banner, reg_daymap, reg_comp, count(reg_state.state_regtype) as reg_count " +
					"from con_regtypes " +
					"left outer join reg_state " +
					"on reg_state.state_registered=1 and reg_state.state_regtype=reg_name and state_cid=? " +
					"where reg_cid=? " +
					"group by con_regtypes.reg_name " +
					"ORDER BY reg_sequence,reg_name";
			p = c.prepareStatement(sql);
			p.setInt(1, whatcid);
			p.setInt(2, whatcid);
			rset = p.executeQuery();
			while (rset.next()) {
				RegistrationType row = new RegistrationType();
				loadData(row, rset);
				row.setRegCount(rset.getInt("reg_count"));	// INELEGANT!  I am ashamed. -dbs
				// logger.debug("regtype = " + row.getRegName() + ", sequence = " + row.getRegSequence() + ", active = " + row.getRegActive() + ", count = " + rset.getInt("reg_count"));
				allTypes.put(row.getRegName(), row);
			}
		} finally {
			Closer.close(rset,p,c);
		}
		return allTypes;

	}
	
	public int getBadgedCount(int whatcid,String whatregtype) throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		ResultSet rset = null;
		int retval = 0;
		logger.debug("Getting badge count for event " + whatcid + ", regtype " + whatregtype);
		try {
			String sql = "select count(*) from reg_state where state_cid=? and state_regtype=? and state_badged=1";
			p = c.prepareStatement(sql);
			p.setInt(1, whatcid);
			p.setString(2, whatregtype);
			rset = p.executeQuery();
			rset.next();
			retval = rset.getInt(1);
		} finally {
			Closer.close(rset,p,c);
		}
		return retval;
	}
	
	public void updateDayArray(RegistrationType target) {
		boolean[] dayArray = new boolean[7];
		for (int x=0; x<=6; x++) {
			dayArray[x] = ((target.getRegDayMap() & 2^x) == 1);
			// logger.debug("updateDayArray: Value in " + target.getRegName() + " from daymap " + target.getRegDayMap() + " for day " + x + " is " + dayArray[x]);
		}
		target.setRegDayArray(dayArray);
	}
	
	public void updateDayMap(RegistrationType target) {
		int newValue = 0;
		for (int x=0; x<=6; x++) {
			//logger.debug("updateDayMap: Value in " + target.getRegName() + " for day " + x + " is " + target.regDayArray[x]);
			newValue = newValue & (target.regDayArray[x] ? 2^x : 0);
		}
		target.setRegDayMap(newValue);
		logger.debug("updateDayMap: recalculated dayMap for regType " + target.getRegName() + " to " + newValue);

	}

	private void loadData(RegistrationType where, ResultSet fromwhat)
			throws SQLException {
		where.setRegCID(fromwhat.getInt("reg_cid"));
		where.setRegCost(fromwhat.getBigDecimal("reg_cost"));
		where.setRegCount(new Integer(0)); // TODO: this needs to be loaded
		where.setRegDesc(fromwhat.getString("reg_desc"));
		// where.setRegExpire("")// TODO: This needs to be loaded
		where.setRegName(fromwhat.getString("reg_name"));
		where.setRegPrint(fromwhat.getString("reg_print"));
		where.setRegSequence(fromwhat.getInt("reg_sequence"));
		where.setRegActive(fromwhat.getBoolean("reg_active"));
		where.setRegDiscountCode(fromwhat.getString("reg_discountcode"));
		where.setRegBanner(fromwhat.getString("reg_banner"));
		where.setRegComp(fromwhat.getBoolean("reg_comp"));
		where.setRegDayMap(fromwhat.getInt("reg_daymap"));
		
		/* Need to recalculate the regDayArray with bits from regDayMap... */
		updateDayArray(where);

	}
}
