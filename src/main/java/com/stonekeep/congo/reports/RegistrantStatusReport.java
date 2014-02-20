package com.stonekeep.congo.reports;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
import com.stonekeep.congo.dao.InvoiceDAO;
import com.stonekeep.congo.dao.RegistrationTypeDAO;
import com.stonekeep.congo.data.Convention;
import com.stonekeep.congo.data.RegistrationType;
import com.stonekeep.congo.data.Report;

public class RegistrantStatusReport extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(RegistrantStatusReport.class);
	private Map<String,Object> sessionData;
	private int cid = 0;
	private final RegistrationTypeDAO registrationTypeDAO ;
	private final InvoiceDAO invoiceDAO ;

	public String message = "";
	public Report myReport = new Report();
	public String format = "";
	
	public void setCid(int cid) { this.cid = cid; }
	public Report getMyReport() {return this.myReport; }
	public void setFormat(String fmt) { this.format = fmt; }

	public RegistrantStatusReport(RegistrationTypeDAO registrationTypeDAO, InvoiceDAO invoiceDAO) {
		this.registrationTypeDAO = registrationTypeDAO;
		this.invoiceDAO = invoiceDAO;
		logger.debug("Format is " + format);
	}
	
	public String execute() throws Exception {
		logger.debug("execute()");
		cid = ((Convention) sessionData.get("conference")).getConCID();
		myReport.title = "Registrant Status Report";
		myReport.titles.add("RegType");
		myReport.titles.add("Description");
		myReport.titles.add("Comp?");
		myReport.titles.add("Print As");
		myReport.titles.add("Amount");
		myReport.titles.add("Registered");
		myReport.titles.add("Badged");
		myReport.titles.add("Estimated Revenue");
		myReport.titles.add("Actual Revenue");
		
		DecimalFormat money = new DecimalFormat("$#,###,##0.00");
		
		logger.info("Generating report of registration types for " + cid);
		Map<String,RegistrationType> regTypes = new TreeMap(registrationTypeDAO.list(cid));
		int regtotal = 0;
		int badgedtotal = 0;
		int paidTotal = 0;
		int unpaidTotal = 0;
		int compedTotal = 0;
		int estimatedRevenueTotal = 0;
		BigDecimal calculatedRevenueTotal = new BigDecimal(0);
		for (RegistrationType rt : regTypes.values()) {
			logger.debug("Adding row " + rt.getRegName());
			ArrayList<String>rowdata = new ArrayList<String>();
			rowdata.add(rt.getRegName());
			rowdata.add(rt.getRegDesc());
			rowdata.add(rt.getRegComp() ? "Yes" : "No");
			rowdata.add(rt.getRegPrint());
			rowdata.add(money.format(rt.getRegCost()));
			int regcount = rt.getRegCount();
			int regbadged = registrationTypeDAO.getBadgedCount(cid,rt.getRegName());
			rowdata.add(String.valueOf(regcount));
			rowdata.add(String.valueOf(regbadged));
			int estimatedRevenue = rt.getRegCost().intValue() * regcount ;
			rowdata.add(money.format(estimatedRevenue));
			
			BigDecimal totalRevenue = invoiceDAO.getRevenue(cid,rt.getRegName());

			rowdata.add(money.format(totalRevenue.doubleValue()));
			
			// Counters...
			regtotal += regcount;
			badgedtotal += regbadged;
			if (rt.getRegComp()) {
				compedTotal += regcount;
			} else {
				paidTotal += regcount;
			}
			if (rt.getRegCost().intValue() == 0) {
				unpaidTotal += regcount;
			}
			estimatedRevenueTotal += estimatedRevenue;
			calculatedRevenueTotal = calculatedRevenueTotal.add(totalRevenue);
			myReport.rows.add(rowdata);
		}
		
		logger.debug("Generating summary");
		ArrayList<String>sumdata = new ArrayList<String>();
		sumdata.add("Total Badged");
		sumdata.add(String.valueOf(badgedtotal));
		myReport.summary.add(sumdata);

		sumdata = new ArrayList<String>();
		sumdata.add("Total Registered");
		sumdata.add(String.valueOf(regtotal));
		myReport.summary.add(sumdata);
		
		sumdata = new ArrayList<String>();
		sumdata.add("Total standard (paid) registrations");
		sumdata.add(String.valueOf(paidTotal));
		myReport.summary.add(sumdata);

		sumdata = new ArrayList<String>();
		sumdata.add("Total unpaid (cost=0) registrations");
		sumdata.add(String.valueOf(unpaidTotal));
		myReport.summary.add(sumdata);
		
		sumdata = new ArrayList<String>();
		sumdata.add("Total registrations marked as 'Comps'");
		sumdata.add(String.valueOf(compedTotal));
		myReport.summary.add(sumdata);

		sumdata = new ArrayList<String>();
		sumdata.add("Estimated Revenue from all paid registrations");
		sumdata.add(money.format(estimatedRevenueTotal));
		myReport.summary.add(sumdata);
		
		sumdata = new ArrayList<String>();
		sumdata.add("Calculated Revenue total from Invoices");
		sumdata.add(money.format(calculatedRevenueTotal.doubleValue()));
		myReport.summary.add(sumdata);
		
		myReport.charts.add("regtypeStatisticsByCount.action");
				
		logger.debug("Done.");
		return SUCCESS;
	}

	@Override
	public void setSession(Map arg0) {
		this.sessionData = arg0;
	}
	

}
