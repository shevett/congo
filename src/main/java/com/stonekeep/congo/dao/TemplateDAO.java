package com.stonekeep.congo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.stonekeep.congo.data.Template;

/**
 * TemplateDAO is the data acccess object for the Template objects.
 */
public class TemplateDAO extends DAO<Template> {
	
	private static Logger logger = Logger.getLogger(TemplateDAO.class);

	public TemplateDAO(DataSource dataSource) {
		super(dataSource);
	}
	
	public Template get(int whatcid, String name) throws SQLException {
		logger.debug("Retrieving template '" + name + "' for event " + whatcid);
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		ResultSet rset = null;
		Template t = null;
		try {
			String sql = "SELECT * FROM templates WHERE template_cid=? AND template_name=?";
			p = c.prepareStatement(sql);
			p.setInt(1, whatcid);
			p.setString(2, name);
			rset = p.executeQuery();
			if (rset.next()) {
				// Got one. Load it.
				t = new Template();
				loadData(t, rset);
			}
		} finally {
			Closer.close(rset, p, c);
		}
		return t;
	}

	public void save(Template t) throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		try {
			String sql = "UPDATE templates SET " +
					"template_desc=?," +
					"template_body=? " +
					"WHERE " +
					"template_cid=? AND template_name=?";
			p = c.prepareStatement(sql);
			p.setString(1, t.getDesc());
			p.setString(2, t.getBody());
			p.setInt(3,t.getCID());
			p.setString(4,t.getName());
			logger.debug(p.executeUpdate() + " rows updated.");
		} finally {
			Closer.close(null,p,c);
		}
	}

	public void create(Template t) throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		try {
			String sql = "INSERT INTO templates " +
					"(template_cid,template_name,template_desc,template_body) " +
					"VALUES " +
					"(?,?,?,?)";
			p = c.prepareStatement(sql);
			p.setInt(1,t.getCID());
			p.setString(2,t.getName());
			p.setString(3,t.getDesc());
			p.setString(4,t.getBody());
			logger.debug(p.executeUpdate() + " rows updated.");
		} finally {
			Closer.close(null,p,c);
		}
	}

	public Map<String, Template> list(int whatcid) throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		ResultSet rset = null;
		HashMap<String, Template> allTemplates = new HashMap<String, Template>();
		try {
			String sql = "SELECT * from templates WHERE template_cid=0 OR template_cid=?";
			p = c.prepareStatement(sql);
			p.setInt(1, whatcid);
			rset = p.executeQuery();
			while (rset.next()) {
				Template row = new Template();
				loadData(row, rset);
				allTemplates.put(row.getName(), row);
			}
		} finally {
			Closer.close(rset,p,c);
		}
		return allTemplates;
	}
	
	public void delete(String templateName) throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement p=null;
		try {
			String sql = "DELETE FROM templates where template_name=?";
			p = c.prepareStatement(sql);
			p.setString(1, templateName);
			p.execute();
		} finally {
			Closer.close(null,p,c);
		}
	}

	protected void loadData(Template where, ResultSet fromwhat)
			throws SQLException {
		where.setCID(fromwhat.getInt("template_cid"));
		where.setName(fromwhat.getString("template_name"));
		where.setDesc(fromwhat.getString("template_desc"));
		where.setBody(fromwhat.getString("template_body"));
		where.setLastmodified(fromwhat.getTimestamp("template_lastmod"));
	}
}
