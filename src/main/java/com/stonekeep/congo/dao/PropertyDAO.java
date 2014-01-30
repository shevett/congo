package com.stonekeep.congo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.stonekeep.congo.data.Property;
import com.stonekeep.congo.data.PropertyConfiguration;

public class PropertyDAO  {

	private final DataSource dataSource;
	private static Logger logger = Logger.getLogger(PropertyDAO.class);

	// PropertyConfiguration SQL queries...

	private final String SQL_LIST_PROPERTYCONFIGURATION = "select * from config_properties where prop_cid=? or prop_scope='GLOBAL'";
	private final String SQL_INSERT_PROPERTYCONFIGURATION = "INSERT INTO config_properties " +
		"(prop_cid,prop_name,prop_default,prop_type,prop_regprompt,prop_cost,prop_description,prop_sequence,prop_scope,prop_format) " +
		"VALUES " +  
		"(?,?,?,?,?,?,?,?,?,?) ";
	private final String SQL_DELETE_PROPERTYCONFIGURATION = "DELETE FROM config_properties " + 
		"WHERE prop_cid=? and prop_name=? LIMIT 1";
	private final String SQL_SELECT_PROPERTYCONFIGURATION = "SELECT * FROM config_properties " + 
		"WHERE (prop_cid=? or prop_scope='Global') AND prop_name=? LIMIT 1";
	private final String SQL_UPDATE_PROPERTYCONFIGURATION = "UPDATE config_properties SET " + 
		"prop_default=?,prop_type=?,prop_regprompt=?,prop_cost=?,prop_description=?,prop_sequence=?,prop_scope=?,prop_format=? "+
		"WHERE prop_cid=? and prop_name=?";
		
	// Property SQL queries...
	
	private final String SQL_LIST_PROPERTIES = "SELECT * FROM reg_properties,config_properties " +
		"WHERE (((prop_cid = config_cid) AND (prop_name = config_name)) OR " +
		"((prop_cid = 0) AND (prop_name = config_name) AND (config_scope='Global'))" ;
	
	
	private final String SQL_LIST_UPDATED_PROPERTIES = 
		"select * from config_properties,reg_properties " +
		"where " +
		"config_properties.prop_name = reg_properties.prop_name AND " +
		"(config_properties.prop_cid = ? OR config_properties.prop_scope='Global') AND " +
		"(reg_properties.prop_rid=? AND (reg_properties.prop_cid=0 OR reg_properties.prop_cid=?))";

//		"reg_properties.prop_rid=? group by reg_properties.prop_name";
	
	private final String SQL_LIST_ALL_PROPERTIES =
		"select * from config_properties " +
		"left outer join reg_properties " +
		"on " +
		"((config_properties.prop_name=reg_properties.prop_name and reg_properties.prop_cid=?) OR " +
		"(config_properties.prop_name=reg_properties.prop_name and config_properties.prop_scope='Global')) AND " +
		"reg_properties.prop_rid=? WHERE " +
		"(config_properties.prop_scope='Global' OR config_properties.prop_cid = ?)" ;

	private final String SQL_LIST_REG_PROPERTIES =
		"SELECT *, get_prop(?, ?, prop_name) AS prop_value from config_properties " + 
		"WHERE prop_cid = ?";

	private final String SQL_DELETE_PROPERTY = 
		"DELETE FROM reg_properties WHERE prop_rid=? and prop_name=? AND (prop_cid=0 OR prop_cid=?)";

	private final String SQL_GET_PROPERTY = 
		"select * from config_properties " +
		"left outer join reg_properties " +
		"on " +
		"((config_properties.prop_name=reg_properties.prop_name and reg_properties.prop_cid=?) OR " +
		"(config_properties.prop_name=reg_properties.prop_name and config_properties.prop_scope='Global')) AND " +
		"reg_properties.prop_rid=? WHERE " +
		"config_properties.prop_name=?" ;
	
	private final String SQL_ADD_PROPERTY =
		"insert into reg_properties (prop_rid,prop_cid,prop_name,prop_value) values (?,?,?,?)";
	
	public PropertyDAO(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public List<PropertyConfiguration> listPropertyConfigurations(int cid) throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		ResultSet rset = null;
		ArrayList<PropertyConfiguration> props = new ArrayList<PropertyConfiguration>();
		logger.info("Retrieving PropertyConfiguration records for convention " + cid);
		try {
			p = c.prepareStatement(SQL_LIST_PROPERTYCONFIGURATION);
			p.setInt(1,cid);
			rset = p.executeQuery();
			while (rset.next()) {
				PropertyConfiguration row = new PropertyConfiguration();
				loadPropertyConfiguration(row, rset);
				props.add(row);
			}
			logger.debug("props list is " + props.size() + " elements long.");
		} finally {
			Closer.close(rset,p,c);
		}
		return props;
	}
	
	public List<Property> listProperties(int cid, int rid) throws SQLException {
		logger.debug("Listing properties for convention " + cid + " rid " + rid);
		List<Property> pl = new ArrayList<Property>();
		Connection c = dataSource.getConnection();
		ResultSet rs = null;
		PreparedStatement p = null;
		try {
			logger.debug("listProperties...");
			p = c.prepareStatement(SQL_LIST_PROPERTIES);
			p.setInt(1,cid);
			p.setInt(2,rid);
			rs =  p.executeQuery();
			while (rs.next()) {
				Property pr = new Property();
				logger.debug("Loading property " + rs.getString("prop_name"));
				loadProperty(pr,rs);
				pl.add(pr);
			}
		} finally {
			Closer.close(rs,p,c);
			c.close();
		}
		return pl;
	}
	
	public List<Property> listAllProperties(int cid, int rid) throws SQLException {
		logger.debug("Listing properties for convention " + cid + " rid " + rid);
		List<Property> pl = new ArrayList<Property>();
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			logger.debug("listAllProperties using : " + SQL_LIST_ALL_PROPERTIES);
			logger.debug("1 is " + cid);
			logger.debug("2 is " + rid);
			p = c.prepareStatement(SQL_LIST_ALL_PROPERTIES);
			p.setInt(1,cid);
			p.setInt(2,rid);
			p.setInt(3,cid);
			rs =  p.executeQuery();
			while (rs.next()) {
				Property pr = new Property();
				loadProperty(pr,rs);
				pr.rid = rid;
				pr.isdefault = (rs.getString("prop_rid") == null);
				String scope = rs.getString("config_properties.prop_scope");
				pr.isglobal = (scope.equals("Global") ? true : false);
				pr.description = rs.getString("prop_description");
				pr.cost = rs.getFloat("prop_cost");
				pr.type = rs.getString("prop_type");
				pr.format = rs.getString("prop_format");
				pr.regprompt = rs.getBoolean("prop_regprompt");
				logger.debug(pr.name + " : " + pr.description + " " + pr.cost + " " + pr.isglobal + " " + pr.isdefault + " (value is " + pr.value + ")");
				pl.add(pr);
			}
		} finally {
			Closer.close(rs,p,c);
		}
		return pl;
	}

	public Property getProperty(int rid, int cid,  String name) throws SQLException {
		logger.debug("Fetching property '" + name + "' from convention " + cid + " rid " + rid);
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		ResultSet rs = null;
		Property pr = null;
		try {
			p = c.prepareStatement(SQL_GET_PROPERTY);
			p.setInt(1,cid);
			p.setInt(2,rid);
			p.setString(3,name);
			rs =  p.executeQuery();
			while (rs.next()) {	// found one!
				pr = new Property();
				loadProperty(pr,rs);
				pr.rid = rid;
				pr.isdefault = (rs.getString("prop_rid") == null);
				String scope = rs.getString("config_properties.prop_scope");
				pr.isglobal = (scope.equals("Global") ? true : false);
				pr.description = rs.getString("prop_description");
				pr.cost = rs.getFloat("prop_cost");
				pr.type = rs.getString("prop_type");
				pr.cid = rs.getInt("prop_cid");
				logger.debug(pr.cid + " : " + pr.name + " : " + pr.description + " " + pr.cost + " " + pr.isglobal + " " + pr.isdefault + " (value is " + pr.value + ")");
			}
		} finally {
			Closer.close(rs,p,c);
		}
		return pr;
	}

	public void setProperty(int rid, int cid, String name, String value) throws SQLException {
		logger.debug("Setting property rid: " + rid + ", cid: " + cid + ", name: " + name + " to value " + value);
		deleteProperty(rid,cid,name);  // this won't do anything if there's no value already.
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		try {
			p = c.prepareStatement(SQL_ADD_PROPERTY);
			p.setInt(1,rid);
			p.setInt(2,cid);
			p.setString(3,name);
			if (value.length() > 99) {
				value = value.substring(1,99);
			}
			p.setString(4,value);
			p.executeUpdate();
		} finally {
			Closer.close(null,p,c);
		}
	}
	
	public List<Property> listUpdatedProperties(int cid, int rid) throws SQLException {
		logger.debug("Generating list of " + cid + " rid " + rid);
		List<Property> pl = new ArrayList<Property>();
		Connection c = dataSource.getConnection();
		ResultSet rs = null;
		PreparedStatement p = null;
		try {
			p = c.prepareStatement(SQL_LIST_UPDATED_PROPERTIES);
			p.setInt(1,cid);
			p.setInt(2,rid);
			p.setInt(3,cid);
			rs =  p.executeQuery();
			while (rs.next()) {
				Property pr = new Property();
				pr.name = rs.getString("reg_properties.prop_name");
				pr.type = rs.getString("config_properties.prop_type");
				pr.value = rs.getString("reg_properties.prop_value");
				logger.debug("Loading property " + pr.name + " with value " + pr.value);
				pl.add(pr);
			}
		} finally {
			Closer.close(rs,p,c);
		}
		return pl;
		
	}

	public void savePropertyConfiguration(PropertyConfiguration pc) throws SQLException {
		Connection c = dataSource.getConnection();
		logger.debug("Saving propertyconfiguration...");
		logger.debug("cid is " + pc.cid);
		logger.debug("name is " + pc.name);
		logger.debug("defaultvalue is " + pc.defaultvalue);
		logger.debug("type is " + pc.type);
		logger.debug("regprompt is " + pc.regprompt);
		logger.debug("cost is " + pc.cost);
		logger.debug("description is " + pc.description);
		logger.debug("sequence is " + pc.sequence);
		logger.debug("scope is " + pc.scope);
		logger.debug("format is " + pc.format);
		PreparedStatement p = null;
		try {
			p = c.prepareStatement(SQL_INSERT_PROPERTYCONFIGURATION);
			p.setInt(1,pc.cid);
			p.setString(2,pc.name);
			p.setString(3,pc.defaultvalue);
			p.setString(4,pc.type);
			p.setBoolean(5,pc.regprompt);
			p.setFloat(6,pc.cost);
			p.setString(7,pc.description);
			p.setInt(8,pc.sequence);
			p.setString(9,pc.scope);
			p.setString(10,pc.format);
			int rows = p.executeUpdate();
			if (rows < 1) {
				logger.warn("Insert failed to add new row.");
			}
		} finally {
			Closer.close(null,p,c);
		}
	}

	public void updatePropertyConfiguration(PropertyConfiguration pc) throws SQLException {
	    PreparedStatement p = null;
	    logger.debug("Update: Using " + SQL_UPDATE_PROPERTYCONFIGURATION);
	    logger.info("note - format is " + pc.format);
		Connection c = dataSource.getConnection();
		try {
			p = c.prepareStatement(SQL_UPDATE_PROPERTYCONFIGURATION);
			p.setString(1,pc.defaultvalue);
			p.setString(2,pc.type);
			p.setBoolean(3,pc.regprompt);
			p.setFloat(4,pc.cost);
			p.setString(5,pc.description);
			p.setInt(6,pc.sequence);
			p.setString(7,pc.scope);
			p.setString(8,pc.format);
			p.setInt(9,pc.cid);
			p.setString(10,pc.name);
			int rows = p.executeUpdate();
			if (rows < 1) {
				logger.warn("Update of property config " + pc.name + " did not update any rows.");
			}
		} finally {
			Closer.close(null,p,c);
		}
	}
	
	public void deletePropertyConfiguration(int cid, String propname) throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		try {
			p = c.prepareStatement(SQL_DELETE_PROPERTYCONFIGURATION);
			p.setInt(1,cid);
			p.setString(2,propname);
			p.execute();
		} finally {
			Closer.close(null,p,c);
		}
	}

	public PropertyConfiguration getPropertyConfiguration(int cid, String name) throws SQLException {
		logger.debug("Loading PropertyConfiguration '" + name + "' for con " + cid);
		PropertyConfiguration pc = new PropertyConfiguration();
		PreparedStatement p = null;
		ResultSet rs = null;
		Connection c = dataSource.getConnection();
		try {
			p = c.prepareStatement(SQL_SELECT_PROPERTYCONFIGURATION);
			p.setInt(1,cid);
			p.setString(2,name);
			rs =  p.executeQuery();
			if (rs.next()) {
				loadPropertyConfiguration(pc,rs);
			}
		} finally {
			Closer.close(rs,p,c);
		}
		logger.debug("Property configuration has value " + pc);
		if (pc != null) {
			logger.debug("cid: " + pc.cid);
			logger.debug("description: " + pc.description);
			logger.debug("name: " + pc.name);
		}
		return pc;
	}
	
	public void save() {}
	public void delete() {} 
	public void list() {}
	
	public void deleteProperty(int rid, int cid, String propname) throws SQLException {
		PreparedStatement p = null;
		logger.debug("Deleting property using " + SQL_DELETE_PROPERTY);
		logger.debug("Deleting rid:" + rid + ", cid: " + cid + ", property name " + propname);
		Connection c = dataSource.getConnection();
		try {
			p = c.prepareStatement(SQL_DELETE_PROPERTY);
			p.setInt(1,rid);
			p.setString(2,propname);
			p.setInt(3,cid);
			p.executeUpdate();
		} finally {
			Closer.close(null,p,c);
		}
	}		

	private void loadPropertyConfiguration(PropertyConfiguration p, ResultSet rset) throws SQLException {
		p.cid = rset.getInt("prop_cid");
		p.cost = rset.getFloat("prop_cost");
		p.defaultvalue = rset.getString("prop_default");
		p.description = rset.getString("prop_description");
		p.name = rset.getString("prop_name");
		p.regprompt = rset.getBoolean("prop_regprompt");
		p.sequence = rset.getInt("prop_sequence");
		p.type = rset.getString("prop_type");
		p.scope = rset.getString("prop_scope");
		p.format = rset.getString("prop_format");
		logger.debug("Loaded " + p.name + " - " + p.description + " from resultset " + rset);
	}
	
	private void loadProperty(Property p, ResultSet rset) throws SQLException {
		p.cid = rset.getInt("prop_cid");
		// p.rid = rset.getInt("prop_rid");
		p.name = rset.getString("prop_name");
		p.value = rset.getString("prop_value");
	}
}
