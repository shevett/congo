package com.stonekeep.congo.reports;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
import com.stonekeep.congo.builders.RegistrantDAOBuilder;
import com.stonekeep.congo.dao.RegistrantDAO;
import com.stonekeep.congo.data.Address;
import com.stonekeep.congo.data.Email;
import com.stonekeep.congo.data.Phone;
import com.stonekeep.congo.data.Registrant;
import com.stonekeep.congo.data.Report;

public class AfterEventExport extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(AfterEventExport.class);
	private Map<String,Object> sessionData;
	private int cid = 0;					// this is injected by spring as the preferredcid.
	private RegistrantDAO registrantDAO;

	public String message = "";
	public String cancelbutton;
	public String runbutton;
	public int eventnumber = 0;
	public Report myReport = new Report();
	public String format = "CSV";
	public boolean onlyregistered=false;
	
	public void setCid(int cid) { this.cid = cid; }
	public Report getMyReport() {return this.myReport; }

	public AfterEventExport(RegistrantDAOBuilder registrantDAObuilder) {
		this.registrantDAO = registrantDAObuilder.getRegistrantDAO();
	}
	
	public String execute() throws Exception {
		logger.debug("execute()");
		if (cancelbutton != null) {
			return "exit";
		}
		if (eventnumber == 0) {
			message="Please specify a valid event number.";
			return INPUT;
		} else {
			cid = eventnumber;
		}
		myReport.title = "Export After Event Report";
		myReport.actionClass = "ReportAfterEventExport";
		myReport.titles.add("ID #");
		myReport.titles.add("Last Name");
		myReport.titles.add("First Name");
		myReport.titles.add("Badge name");
		myReport.titles.add("Company");
		myReport.titles.add("RegType");
		myReport.titles.add("Add1");
		myReport.titles.add("Add2");
		myReport.titles.add("City");
		myReport.titles.add("State");
		myReport.titles.add("Zipcode");
		myReport.titles.add("Country");
		myReport.titles.add("Phone");
		myReport.titles.add("Email");
		myReport.titles.add("Badged");
		myReport.titles.add("Checkedin");
		
		logger.debug("Fetching list of registrants for " + cid);
		registrantDAO.registeredOnly = onlyregistered;
		Object[] results = registrantDAO.searchByAny("%", cid,65535);
		int totalrows = ((Integer)results[0]).intValue();
		List<Registrant> l = (List<Registrant>) results[1];
		
		StringBuffer sb = new StringBuffer();
		Address a;
		Phone p;
		Email e;
		Registrant r;
		for (Registrant rl : l) {
			r = registrantDAO.getByID(rl.rid);
			registrantDAO.updateCurrentState(r, cid);

			ArrayList<String>rowdata = new ArrayList<String>();
			rowdata.add(r.rid + "");  //ugly, sorry.
			rowdata.add(r.lastName );
			rowdata.add(r.firstName);
			rowdata.add(r.badgeName);
			rowdata.add(r.company);
			
			rowdata.add((rl.currentState == null) ? "" : rl.currentState.regtype);

			a = r.addressList.get("Home");
			logger.debug("HOME address for " + r.rid + " is " + a);
			rowdata.add((a==null) ? "" : a.getLine1());
			rowdata.add((a==null) ? "" : a.getLine2());
			rowdata.add((a==null) ? "" : a.getCity());
			rowdata.add((a==null) ? "" : a.getState());
			rowdata.add((a==null) ? "" : a.getCountry());
			rowdata.add((a==null) ? "" : a.getZipcode() + "");
			
			p = r.phoneList.get("Home");
			logger.debug("HOME phone number for " + r.rid + " is " + p);
			rowdata.add(p != null ? p.phone : "");
			
			e = r.emailList.get("Home");
			logger.debug("HOME email for " + r.rid + " is " + e);
			rowdata.add(e != null ? e.getAddress() : "") ;
			
			rowdata.add((r.currentState == null) ? "" : r.currentState.badged + "");
			rowdata.add((r.currentState == null) ? "" : r.currentState.checkedin + "");

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
