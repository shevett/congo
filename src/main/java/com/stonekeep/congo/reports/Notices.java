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
import com.stonekeep.congo.dao.NoteDAO;
import com.stonekeep.congo.dao.PropertyDAO;
import com.stonekeep.congo.data.PropertyConfiguration;
import com.stonekeep.congo.data.Report;

public class Notices extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(Notices.class);
	private Map<String,Object> sessionData;
	public int cid = 0;						// injected
	public DataSource dataSource = null;	// injected
	private final NoteDAO noteDAO ;

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
	public void setDataSource(DataSource d) { this.dataSource = d; }

	public Notices(NoteDAO noteDAO) {
		this.noteDAO = noteDAO;
		logger.setLevel(Level.INFO);
	}
	
	public String setup() throws Exception {
		logger.debug("setup invoked...");
//		configList = DAO.listPropertyConfigurations(cid);
		logger.debug("Returning success...");
		return SUCCESS;
	}
	
	public String execute() throws Exception {
		logger.debug("execute()");
		logger.debug("runbutton is " + runbutton);
		logger.debug("cancelbutton is " + cancelbutton);
		logger.debug("selectedProperty is " + selectedProperty);
		logger.debug("checkValue is " + checkValue);
		
		if (cancelbutton != null) {
			return "exit";
		}

		myReport.title = "Notices Report";
		myReport.titles.add("Registrant ID");
		myReport.titles.add("Name");
		myReport.titles.add("Badgename");
		myReport.titles.add("Type");
		myReport.titles.add("Posted by");
		myReport.titles.add("Post date");
		myReport.titles.add("Message");
		myReport.titles.add("Acked by");
		myReport.titles.add("Ack date");
		
		Connection c = dataSource.getConnection();
		
//		// Create a temporary table that we can build up with matches...
//		
//		String createSql = "CREATE TEMPORARY TABLE propfolks (" +
//		  "`master_rid` int(8) NOT NULL AUTO_INCREMENT, " + 
//		  "`master_name` varchar(40) DEFAULT NULL, " + 
//		  "`master_company` varchar(30) DEFAULT NULL, " +
//		  "`prop_name` varchar(30), " +
//		  "`prop_value` varchar(100), " +
//		  "`prop_source` varchar(100), " +
//		  "KEY `id` (`master_rid`) " +
//		") ENGINE=InnoDB";
//		
//		logger.debug("Creating temporary table.");
//		PreparedStatement p = c.prepareStatement(createSql);
//		p.execute();
//		logger.debug("Done...");
//		
//		// Load in all the notices from reg_notices that match the query
//		
//		String loadSql = "insert into propfolks (master_rid, prop_name, prop_value, prop_source) " +
//			"(select prop_rid,prop_name,prop_value,prop_cid " +
//			"from reg_notices " +
//			"where prop_name=? " +
//			"and prop_value LIKE ? and (prop_cid=? OR prop_cid=0))";
//		
//		logger.debug("Doing initial load of all registrants with appropriate data...");
//		
//		p = c.prepareStatement(loadSql);
//		p.setString(1,selectedProperty);
//		p.setString(2,checkValue);
//		p.setInt(3,cid);
//		
//		int numrows = p.executeUpdate();
//		
//		logger.debug("Temporary table built.  Generated " + numrows + " worth of data.");
		
		String selectSql = "select * from reg_notes " +
				"left outer join reg_master on reg_master.master_rid=reg_notes.note_rid " +
				"order by master_lastname";
		
		PreparedStatement p = c.prepareStatement(selectSql);
		p = c.prepareStatement(selectSql);
		
		ResultSet r = p.executeQuery(selectSql);
		
		int totalcounter=0;
		
		while (r.next()) {
			ArrayList<String>rowdata = new ArrayList<String>();
			rowdata.add(r.getInt("reg_master.master_rid")+"");
			rowdata.add(r.getInt("reg_notes.note_cid")+"");
			rowdata.add(r.getString("reg_master.master_lastname") + ", " + r.getString("reg_master.master_firstname"));
			rowdata.add(r.getString("reg_master.master_badgename"));
			rowdata.add(r.getString("reg_notes.note_type"));
			rowdata.add(r.getString("reg_notes.note_postrid"));
			rowdata.add(r.getString("reg_notes.note_postdate"));
			rowdata.add(r.getString("reg_notes.note_message"));
			rowdata.add(r.getString("reg_notes.note_ackrid"));
			rowdata.add(r.getString("reg_notes.note_ackdate"));
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
