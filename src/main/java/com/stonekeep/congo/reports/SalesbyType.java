package com.stonekeep.congo.reports;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
import com.stonekeep.congo.dao.InvoiceDAO;
import com.stonekeep.congo.data.Convention;
import com.stonekeep.congo.data.Report;

public class SalesbyType extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(SalesbyType.class);
	private Map<String,Object> sessionData;
	private int cid = 0;
	private final InvoiceDAO invoiceDAO ;

	public String message = "";
	public Report myReport = new Report();
	public String format = "";
	
	public void setCid(int cid) { this.cid = cid; }
	public Report getMyReport() {return this.myReport; }
	public void setFormat(String fmt) { this.format = fmt; }

	public SalesbyType(InvoiceDAO invoiceDAO) {
		this.invoiceDAO = invoiceDAO;
		logger.debug("Format is " + format);
	}
	
	public String execute() throws Exception {
		logger.debug("execute()");
		cid = ((Convention) sessionData.get("conference")).getConCID();
		logger.debug("execute() : reporting sales by payment for event '" + cid + "'");
		myReport.title = "Sales by Payment Type Report";
		myReport.titles.add("Payment type");
		myReport.titles.add("Count");
		myReport.titles.add("Amount");
		
		Map<String,BigDecimal[]> salesData = invoiceDAO.salesByType(cid);
		
		DecimalFormat money = new DecimalFormat("$#,###,##0.00");
		
		int revenueTotal = 0;
		
		for ( String key : salesData.keySet()) {
        	BigDecimal[] bd = salesData.get(key);
			ArrayList<String>rowdata = new ArrayList<String>();
			rowdata.add(key);
			rowdata.add(bd[0] + "");
			revenueTotal = revenueTotal + bd[1].intValue();
			rowdata.add(money.format(bd[1]));
			myReport.rows.add(rowdata);
		}
        
		logger.debug("Generating summary");
		ArrayList<String>sumdata = new ArrayList<String>();
		sumdata.add("Total Categories");
		sumdata.add(String.valueOf(salesData.size()));
		myReport.summary.add(sumdata);
		
		sumdata = new ArrayList<String>();
		sumdata.add("Total revenues");
		sumdata.add(String.valueOf(money.format(revenueTotal)));
		myReport.summary.add(sumdata);

		
		logger.debug("Done.");
		return SUCCESS;
	}

	@Override
	public void setSession(Map arg0) {
		this.sessionData = arg0;
	}
	

}
