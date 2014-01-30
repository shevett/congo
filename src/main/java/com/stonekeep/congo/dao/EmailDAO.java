package com.stonekeep.congo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.stonekeep.congo.data.Email;
import com.stonekeep.congo.data.History;

/**
 * DAO for Email objects
 * 
 * @author dbs
 * @author ojacobson
 */
public class EmailDAO implements ContactDAO<Email> {
    private static Logger logger = Logger.getLogger(EmailDAO.class);
    private final DataSource dataSource;
    private final HistoryDAO historyDAO;

    public EmailDAO(DataSource dataSource, HistoryDAO historyDAO) {
        this.dataSource = dataSource;
        this.historyDAO = historyDAO;
    }

    public Email getEmail(int whatrid, String whatlocation) throws SQLException {
        Connection c = dataSource.getConnection();
        PreparedStatement p = null;
        ResultSet rset = null;
        Email e = null;
        try {
            String sql = "SELECT * FROM reg_email WHERE email_rid=? and email_location=? ";
            p = c.prepareStatement(sql);
            p.setInt(1, whatrid);
            p.setString(2, whatlocation);
            rset = p.executeQuery();
            if (rset.next()) {
                // Got one. Load it.
                e = new Email();
                loadData(e, rset);
            }
        } finally {
        	Closer.close(rset,p,c);
        }
        return e;
    }
    
    /*
     * Get an email address for a registrant, use the 'primary' flag to pick which one. 
     */
    public Email getEmail(int whatrid) throws SQLException {
    	Connection c = dataSource.getConnection();
    	PreparedStatement p = null;
    	ResultSet rset = null;
    	Email e = null;
        try {
            String sql = "SELECT * FROM reg_email WHERE email_rid=? order by email_primary DESC";
            p = c.prepareStatement(sql);
            p.setInt(1, whatrid);
            rset = p.executeQuery();
            if (rset.next()) {
                // Got one. Load it.
                e = new Email();
                loadData(e, rset);
            }
        } finally {
        	Closer.close(rset,p,c);
        }    	
        return e;
    }

    public void save(Email e,int operator) throws SQLException {
        Connection c = dataSource.getConnection();
        PreparedStatement p = null;
        try {
            String sql = "UPDATE reg_email SET " + "email_address=?, "
                    + "email_primary=? "
                    + "WHERE email_rid=? AND email_location=?";
            p = c.prepareStatement(sql);
            p.setString(1, e.address);
            p.setBoolean(2, e.primary);
            p.setInt(3, e.rid);
            p.setString(4, e.location);
            logger.debug(p.executeUpdate() + " rows updated.");
            p.close();
            History h = historyDAO.create();
            h.rid = e.rid;
            h.cid = 0;
            h.operator = operator;
            h.actcode = "CHANGE";
            h.comment = "Email edited";
            h.arg1 = e.location;
            h.arg2 = e.address;
            historyDAO.save(h);
        } finally {
        	Closer.close(null,p,c);
        }
    }

    public void create(Email e) throws SQLException {
        logger.debug("Creating new email");
        Connection c = dataSource.getConnection();
        PreparedStatement p = null;
        try {
            String sql = "INSERT INTO reg_email "
                    + "(email_rid,email_location,email_address,email_primary) "
                    + "VALUES (?,?,?,?)";
            p = c.prepareStatement(sql);
            p.setInt(1, e.rid);
            p.setString(2, e.location);
            p.setString(3, e.address);
            p.setBoolean(4, e.primary);
            p.execute();
        } finally {
        	Closer.close(null,p,c);
        }
    }

    public Map<String, Email> list(int whatrid) throws SQLException {
        Connection c = dataSource.getConnection();
        PreparedStatement p = null;
        ResultSet rset = null;
        HashMap<String, Email> allEmails = new HashMap<String, Email>();
        Email row = null;
        try {
            String sql = "SELECT * from reg_email where email_rid=?";
            p = c.prepareStatement(sql);
            p.setInt(1, whatrid);
            rset = p.executeQuery();
            while (rset.next()) {
                row = new Email();
                loadData(row, rset);
                allEmails.put(row.location, row);
            }
        } finally {
        	Closer.close(rset,p,c);
        }
        return allEmails;
    }

    public void delete(int whatrid, String location) throws SQLException {
        Connection c = dataSource.getConnection();
        PreparedStatement p = null;
        try {
            String sql = "DELETE from reg_email where email_rid=? and email_location=?";
            p = c.prepareStatement(sql);
            p.setInt(1, whatrid);
            p.setString(2, location);
            p.execute();
        } finally {
            Closer.close(null,p,c);
        }
    }

    private static void loadData(Email where, ResultSet fromwhat)
            throws SQLException {
        where.rid = fromwhat.getInt("email_rid");
        where.location = fromwhat.getString("email_location");
        where.address = fromwhat.getString("email_address");
        where.primary = fromwhat.getBoolean("email_primary");
    }

    @Override
    public Email createBlankContact() {
        return new Email();
    }

    @Override
    public Email lookup(int rid, String location) throws SQLException {
        return getEmail(rid, location);
    }

    @Override
    public void delete(Email contact) throws SQLException {
        delete(contact.getRid(), contact.getLocation());
    }
}