package com.stonekeep.congo.reports;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
import com.stonekeep.congo.dao.HistoryDAO;
import com.stonekeep.congo.data.Convention;
import com.stonekeep.congo.data.Report;

public class ByDate extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(ByDate.class);
	private Map<String,Object> sessionData;
	private int cid = 0;
	private final HistoryDAO historyDAO ;

	public String message = "";
	public Report myReport = new Report();
	public String format = "";
	
	public void setCid(int cid) { this.cid = cid; }
	public Report getMyReport() {return this.myReport; }
	public void setFormat(String fmt) { this.format = fmt; }

	public ByDate(HistoryDAO historyDAO) {
		this.historyDAO = historyDAO;
		logger.debug("Format is " + format);
	}
	
	public String execute() throws Exception {
		logger.debug("execute()");
		cid = ((Convention) sessionData.get("conference")).getConCID();
		myReport.title = "Registrations By Date Report";
		myReport.titles.add("Date");
		myReport.titles.add("Registrations / Voids");
		myReport.titles.add("Running Total");
		
		TreeMap<String,Integer> countData = new TreeMap(historyDAO.registrationsByDate(cid));

		int runningTotal = 0;
        for ( String key : countData.keySet()) {
        	Integer i = countData.get( key );
			ArrayList<String>rowdata = new ArrayList<String>();
			rowdata.add(key);
			rowdata.add(i + "");
			runningTotal = runningTotal + i.intValue();
			rowdata.add(runningTotal + "");
			myReport.rows.add(rowdata);
		}
		
		logger.debug("Done.");
		return SUCCESS;
	}

	@Override
	public void setSession(Map arg0) {
		this.sessionData = arg0;
	}
	

}
