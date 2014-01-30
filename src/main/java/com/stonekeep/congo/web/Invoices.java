package com.stonekeep.congo.web;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.Action;
import com.stonekeep.congo.dao.InvoiceDAO;
import com.stonekeep.congo.data.Convention;
import com.stonekeep.congo.data.Invoice;
import com.stonekeep.congo.data.Registrant;

public class Invoices implements Action, SessionAware {
	public String exitbutton = null;
	private Logger logger = Logger.getLogger(Invoices.class);
	private final InvoiceDAO invoiceDAO;
	public List<Invoice> invoices;
	private Map<String,Object> sessionData;
	
	public Invoices(InvoiceDAO invoiceDAO) {
		this.invoiceDAO = invoiceDAO;
	}

	@Override
	public String execute() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String retrieveInvoices() throws Exception {
		// Retrieve a list of invoices for the current webuser.
		Registrant r = (Registrant)sessionData.get("webuser");
		Convention c = (Convention)sessionData.get("currentConvention");

		invoices = invoiceDAO.listInvoices(c.getConCID(), r.getRid());
		return SUCCESS;
	}

	@Override
	public void setSession(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		this.sessionData = arg0;
	}

}
