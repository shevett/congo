package com.stonekeep.congo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.stonekeep.congo.data.History;
import com.stonekeep.congo.data.Phone;

/**
 * DAO for Phone objects
 * 
 * @author dbs
 * @author ojacobson
 */
public class PhoneDAO implements ContactDAO<Phone> {
    private static Logger logger = Logger.getLogger(PhoneDAO.class);
    private final DataSource dataSource;
    private final HistoryDAO historyDAO;

    public PhoneDAO(DataSource dataSource, HistoryDAO historyDAO) {
        this.dataSource = dataSource;
        this.historyDAO = historyDAO;
    }

    public Phone getPhone(int whatrid, String whatlocation) throws SQLException {
        Connection c = dataSource.getConnection();
        PreparedStatement p = null;
        ResultSet rset = null;
        Phone phone = null;
        try {
            String sql = "SELECT * FROM reg_phone WHERE phone_rid=? and phone_location=?";

            p = c.prepareStatement(sql);
            p.setInt(1, whatrid);
            p.setString(2, whatlocation);
            rset = p.executeQuery();
            if (rset.next()) {
                // Got one. Load it.
                phone = new Phone();
                loadData(phone, rset);
            }
        } finally {
        	Closer.close(rset,p,c);
        }
        return phone;
    }
    
    /*
     * Get an phone  for a registrant, use the 'primary' flag to pick which one. 
     */
    public Phone getPhone(int whatrid) throws SQLException {
    	Connection c = dataSource.getConnection();
    	PreparedStatement p = null;
    	ResultSet rset = null;
    	Phone phone = null;
        try {
            String sql = "SELECT * FROM reg_phone WHERE phone_rid=? order by phone_primary DESC";
            p = c.prepareStatement(sql);
            p.setInt(1, whatrid);
            rset = p.executeQuery();
            if (rset.next()) {
                // Got one. Load it.
                phone = new Phone();
                loadData(phone, rset);
            }
        } finally {
        	Closer.close(rset,p,c);
        }    	
        return phone;
    }

    public void save(Phone phone,int operator) throws SQLException {
        Connection c = dataSource.getConnection();
        PreparedStatement p = null;
        try {
            String sql = "UPDATE reg_phone SET " + "phone_phone=?, "
                    + "phone_primary=? "
                    + "WHERE phone_rid=? AND phone_location=?";
            p = c.prepareStatement(sql);
            p.setString(1, phone.phone);
            p.setBoolean(2, phone.primary);
            p.setInt(3, phone.rid);
            p.setString(4, phone.location);
            logger.debug(p.executeUpdate() + " rows updated.");
            p.close();
            History h = historyDAO.create();
            h.rid = phone.rid;
            h.cid = 0;
            h.operator = operator;
            h.actcode = "CHANGE";
            h.comment = "Phone edited";
            h.arg1 = phone.location;
            h.arg2 = phone.phone;
            historyDAO.save(h);
        } finally {
        	Closer.close(null,p,c);
        }
    }

    public void create(Phone phone) throws SQLException {
        logger.debug("Creating new Phone");
        Connection c = dataSource.getConnection();
        PreparedStatement p = null;
        try {
            String sql = "INSERT INTO reg_phone "
                    + "(phone_rid,phone_location,phone_phone,phone_primary) "
                    + "VALUES (?,?,?,?)";
            p = c.prepareStatement(sql);
            p.setInt(1, phone.rid);
            p.setString(2, phone.location);
            p.setString(3, phone.phone);
            p.setBoolean(4, phone.primary);
            p.execute();
        } finally {
            Closer.close(null,p,c);
        }
    }

    public Map<String, Phone> list(int whatrid) throws SQLException {
        Connection c = dataSource.getConnection();
        PreparedStatement p = null;
        ResultSet rset = null;
        HashMap<String, Phone> allPhones = new HashMap<String, Phone>();
        try {
            String sql = "SELECT * from reg_phone where phone_rid=?";
            p = c.prepareStatement(sql);
            p.setInt(1, whatrid);
            rset = p.executeQuery();
            while (rset.next()) {
                Phone row = new Phone();
                loadData(row, rset);
                allPhones.put(row.location, row);
                logger.debug("Listing - adding " + row.location + " which has value of " + row.phone);
            }
        } finally {
        	Closer.close(rset,p,c);
        }
        return allPhones;
    }

    public void delete(int whatrid, String location) throws SQLException {
        Connection c = dataSource.getConnection();
        PreparedStatement p = null;
        try {
            String sql = "DELETE from reg_phone where phone_rid=? and phone_location=?";
            p = c.prepareStatement(sql);
            p.setInt(1, whatrid);
            p.setString(2, location);
            p.execute();
        } finally {
            Closer.close(null,p,c);
        }
    }

    private static void loadData(Phone where, ResultSet fromwhat)
            throws SQLException {
        where.rid = fromwhat.getInt("phone_rid");
        where.location = fromwhat.getString("phone_location");
        where.phone = fromwhat.getString("phone_phone");
        where.primary = fromwhat.getBoolean("phone_primary");
    }

    @Override
    public Phone createBlankContact() {
        return new Phone();
    }

    @Override
    public Phone lookup(int rid, String location) throws SQLException {
        return getPhone(rid, location);
    }

    @Override
    public void delete(Phone contact) throws SQLException {
        delete(contact.getRid(), contact.getLocation());
    }

}
