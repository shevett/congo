package com.stonekeep.congo.reports;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.stonekeep.congo.dao.HistoryDAO;
import com.stonekeep.congo.data.History;
import com.stonekeep.congo.data.Report;

public class ActivityReport extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(ActivityReport.class);
	private final HistoryDAO historyDAO ;

	public String message = "";
	public Report myReport = new Report();
	public String format = "";
	
	public Report getMyReport() {return this.myReport; }
	public void setFormat(String fmt) { this.format = fmt; }

	public ActivityReport(HistoryDAO historyDAO) {
		this.historyDAO = historyDAO;
	}
	
	public String execute() throws Exception {
		logger.debug("execute()");
		myReport.title = "Activity Report (Most Recent 200 Entries)";
		myReport.titles.add("Date/Time");
		myReport.titles.add("Registrant");
		myReport.titles.add("Activity");
		myReport.titles.add("Comment");
		myReport.titles.add("Details");
		
		List<History> historyActivity = historyDAO.listActivity();

		for (History h : historyActivity) {
			ArrayList<String>rowdata = new ArrayList<String>();
			rowdata.add(h.activity.toString());
			rowdata.add(h.rid + " (" + h.lastname + ", " + h.firstname +")");
			rowdata.add(h.actcode);
			rowdata.add(h.comment);
			rowdata.add(h.arg1);
			myReport.rows.add(rowdata);
		}
		
		logger.debug("Done.");
		return SUCCESS;
	}

}
