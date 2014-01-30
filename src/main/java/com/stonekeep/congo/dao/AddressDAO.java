package com.stonekeep.congo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.stonekeep.congo.data.Address;
import com.stonekeep.congo.data.History;

/**
 * DAO for Address objects
 * 
 * @author dbs
 * @author ojacobson
 */
public class AddressDAO implements ContactDAO<Address> {
    private static Logger logger = Logger.getLogger(AddressDAO.class);
    private final DataSource dataSource;
    private final HistoryDAO historyDAO;

    public AddressDAO(DataSource dataSource,HistoryDAO historyDAO) {
        this.dataSource = dataSource;
        this.historyDAO = historyDAO;
    }

    public Address getAddress(int whatrid, String whatlocation)
            throws SQLException {
    	logger.debug("getAddress...");
        Connection c = dataSource.getConnection();
        String sql = "SELECT * FROM reg_address WHERE add_rid=? and add_location=?";
        PreparedStatement p = null;
        ResultSet rset = null;
        Address a = null;
        try {
            p = c.prepareStatement(sql);
            p.setInt(1, whatrid);
            p.setString(2, whatlocation);
            rset = p.executeQuery();
            if (rset.next()) {
                // Got one. Load it.
                a = new Address();
                loadData(a, rset);
            }
        } finally {
            Closer.close(rset,p,c);
        }
        return a;
    }
    
    /*
     * Get an address for a registrant, use the 'primary' flag to pick which one. 
     */
    public Address getAddress(int whatrid) throws SQLException {
    	Connection c = dataSource.getConnection();
    	PreparedStatement p = null;
    	ResultSet rset = null;
    	Address address = null;
        try {
            String sql = "SELECT * FROM reg_address WHERE add_rid=? order by add_primary DESC";
            p = c.prepareStatement(sql);
            p.setInt(1, whatrid);
            rset = p.executeQuery();
            if (rset.next()) {
                // Got one. Load it.
                address = new Address();
                loadData(address, rset);
            }
        } finally {
            Closer.close(rset,p,c);
        }    	
        return address;
    }

    public void save(Address a,int operator) throws SQLException {
    	logger.debug("Save...");
        Connection c = dataSource.getConnection();
        PreparedStatement p = null;
        try {
            String sql = "UPDATE reg_address SET " 
            		+ "add_line1=?, "
                    + "add_line2=?, " 
                    + "add_city=?, " 
                    + "add_state=?, "
                    + "add_zipcode=?, "
                    + "add_country=?, "
                    + "add_primary=? "
                    + "WHERE add_rid=? AND add_location=?";
            p = c.prepareStatement(sql);
            p.setString(1, a.line1);
            p.setString(2, a.line2);
            p.setString(3, a.city);
            p.setString(4, a.state);
            p.setString(5, String.format("%1.10s",a.zipcode));	// Don't write more than 10 characters.
            p.setString(6, a.country);
            p.setBoolean(7, a.primary);
            p.setInt(8, a.rid);
            p.setString(9, a.location);
            logger.debug(p.executeUpdate() + " rows updated.");
            History h = historyDAO.create();
            h.rid = a.rid;
            h.cid = 0;
            h.operator = operator;
            h.actcode = "CHANGE";
            h.comment = "Address edited";
            h.arg1 = a.location;
            historyDAO.save(h);
        } finally {
            Closer.close(null,p,c);
        }
    }

    public void create(Address a) throws SQLException {
        logger.debug("Creating new address - rid is " + a.rid + ", location is " + a.location);
        Connection c = dataSource.getConnection();
        PreparedStatement p = null;
        try {
            String sql = "INSERT INTO reg_address " + "(add_rid,add_location) "
                    + "VALUES (?,?)";
            p = c.prepareStatement(sql);
            p.setInt(1, a.rid);
            p.setString(2, a.location);
            p.execute();
            save(a,0);
        } finally {
            Closer.close(null,p,c);
        }
    }

    public Map<String, Address> list(int whatrid) throws SQLException {
    	logger.debug("Listing addresses for " + whatrid);
        Connection c = dataSource.getConnection();
        HashMap<String, Address> allAddresss = new HashMap<String, Address>();
        PreparedStatement p = null;
        ResultSet rset = null;
        try {
            String sql = "SELECT * from reg_address where add_rid=?";
            p = c.prepareStatement(sql);
            p.setInt(1, whatrid);
            rset = p.executeQuery();
            while (rset.next()) {
                Address row = new Address();
                loadData(row, rset);
                allAddresss.put(row.location, row);
                logger.debug("Adding " + row.location + " which has country of " + row.country);
            }
        } finally {
            Closer.close(rset,p,c);
        }
        return allAddresss;
    }

    public void delete(int whatrid, String location) throws SQLException {
        Connection c = dataSource.getConnection();
        PreparedStatement p = null;
        try {
            String sql = "DELETE from reg_address where add_rid=? and add_location=?";
            p = c.prepareStatement(sql);
            p.setInt(1, whatrid);
            p.setString(2, location);
            p.execute();
        } finally {
        	Closer.close(null,p,c);
        }
    }

    private static void loadData(Address where, ResultSet fromwhat)
            throws SQLException {
    	logger.debug("loadData()....");
        where.rid = fromwhat.getInt("add_rid");
        where.location = fromwhat.getString("add_location");
        where.line1 = fromwhat.getString("add_line1");
        where.line2 = fromwhat.getString("add_line2");
        where.city = fromwhat.getString("add_city");
        where.state = fromwhat.getString("add_state");
        where.country = fromwhat.getString("add_country");
        where.zipcode = fromwhat.getString("add_zipcode");
        where.primary = fromwhat.getBoolean("add_primary");
    }

    @Override
    public Address createBlankContact() {
        return new Address();
    }

    @Override
    public Address lookup(int rid, String location) throws SQLException {
    	logger.debug("lookup...");
        return getAddress(rid, location);
    }

    @Override
    public void delete(Address contact) throws SQLException {
        delete(contact.getRid(), contact.getLocation());
    }
}