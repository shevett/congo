package com.stonekeep.congo.reports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
import com.stonekeep.congo.dao.PropertyDAO;
import com.stonekeep.congo.data.Convention;
import com.stonekeep.congo.data.PropertyConfiguration;
import com.stonekeep.congo.data.Report;

public class Properties extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(Properties.class);
	private Map<String,Object> sessionData;
	public int cid;					
	private final DataSource dataSource;	// injected
	private final PropertyDAO propertyDAO ;

	public String message = "";
	public Report myReport = new Report();
	public String format = "";
	public String runbutton = null;
	public String cancelbutton = null;
	public String selectedProperty=null;
	public String checkValue=null;
	
	public List<PropertyConfiguration> configList ;
	
	public void setCid(int cid) { this.cid = cid; }
	public Report getMyReport() {return this.myReport; }
	public void setFormat(String fmt) { this.format = fmt; }

	public Properties(DataSource c, PropertyDAO propertyDAO) {
		this.propertyDAO = propertyDAO;
		this.dataSource = c;
	}
	
	public String setup() throws Exception {
		logger.debug("setup invoked...");
		Convention con = (Convention)sessionData.get("conference");
		cid = con.getConCID();
		configList = propertyDAO.listPropertyConfigurations(cid);
		logger.debug("Returning success...");
		return SUCCESS;
	}
	
	public String execute() throws Exception {
		logger.debug("execute()");
		logger.debug("runbutton is " + runbutton);
		logger.debug("cancelbutton is " + cancelbutton);
		logger.debug("selectedProperty is " + selectedProperty);
		logger.debug("checkValue is " + checkValue);
		Convention con = (Convention)sessionData.get("conference");
		cid = con.getConCID();
		logger.debug("cid is " + cid);
		
		if (cancelbutton != null) {
			return "exit";
		}

		myReport.title = "Properties Report";
		myReport.titles.add("Registrant ID");
		myReport.titles.add("Name");
		myReport.titles.add("Badgename");
		myReport.titles.add("Company name");
		myReport.titles.add("Email");
		myReport.titles.add("Property");
		myReport.titles.add("Value");
		myReport.titles.add("Source");
		
		Connection c = dataSource.getConnection();
		
		// Create a temporary table that we can build up with matches...
		
		String createSql = "CREATE TEMPORARY TABLE propfolks (" +
		  "`master_rid` int(8) NOT NULL AUTO_INCREMENT, " + 
		  "`master_name` varchar(40) DEFAULT NULL, " + 
		  "`master_company` varchar(30) DEFAULT NULL, " +
		  "`prop_name` varchar(30), " +
		  "`prop_value` varchar(100), " +
		  "`prop_source` varchar(100), " +
		  "KEY `id` (`master_rid`) " +
		") ENGINE=InnoDB";
		
		logger.debug("Creating temporary table.");
		PreparedStatement p = c.prepareStatement(createSql);
		p.execute();
		logger.debug("Done...");
		
		// Load in all the properties from reg_properties that match the query
		
		String loadSql = "insert into propfolks (master_rid, prop_name, prop_value, prop_source) " +
			"(select prop_rid,prop_name,prop_value,prop_cid " +
			"from reg_properties " +
			"where prop_name=? " +
			"and prop_value LIKE ? and (prop_cid=? OR prop_cid=0))";
		
		logger.debug("Doing initial load of all registrants with appropriate data...");
		
		p = c.prepareStatement(loadSql);
		p.setString(1,selectedProperty);
		p.setString(2,checkValue);
		p.setInt(3,cid);
		
		int numrows = p.executeUpdate();
		
		logger.debug("Temporary table built.  Generated " + numrows + " worth of data.");
		
		String selectSql = "select * from propfolks " +
				"left outer join reg_master on reg_master.master_rid=propfolks.master_rid " +
				"left outer join reg_email on reg_email.email_rid=propfolks.master_rid and reg_email.email_primary " +
				"order by master_lastname";
		p = c.prepareStatement(selectSql);
		
		ResultSet r = p.executeQuery(selectSql);
		
		int totalcounter=0;
		
		while (r.next()) {
			ArrayList<String>rowdata = new ArrayList<String>();
			rowdata.add(r.getInt("reg_master.master_rid")+"");
			rowdata.add(r.getString("reg_master.master_lastname") + ", " + r.getString("reg_master.master_firstname"));
			rowdata.add(r.getString("reg_master.master_badgename"));
			rowdata.add(r.getString("reg_master.master_company"));
			rowdata.add(r.getString("reg_email.email_address"));
			rowdata.add(r.getString("prop_name"));
			rowdata.add(r.getString("prop_value"));
			rowdata.add(r.getInt("prop_source") + "");
			myReport.rows.add(rowdata);
			totalcounter++;
		}
		
		ArrayList<String>sumdata = new ArrayList<String>();
		sumdata.add("Total Registrants");
		sumdata.add(String.valueOf(totalcounter));
		myReport.summary.add(sumdata);

		logger.debug("Done.");
		return SUCCESS;
	}

	@Override
	public void setSession(Map arg0) {
		this.sessionData = arg0;
	}
	

}
