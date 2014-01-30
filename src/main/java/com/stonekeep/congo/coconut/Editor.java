package com.stonekeep.congo.coconut;

import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class Editor extends ActionSupport implements SessionAware,Preparable {

	private static final long serialVersionUID = 2L;

	private Logger logger ;
	public Map<String, Object> sessionData;
	private int whatcid;
	public String message;
	public String exitbutton;
	public String typebutton;

	public String list() throws Exception {
		return SUCCESS;
	}
	
	public void prepare() throws Exception {
		
	}

//	public abstract String view() throws Exception ;
//
//	public abstract String update() throws Exception ;
//	
//	public String delete() throws Exception {
//		logger.warn("Class has not implemented delete.  No action performed.");
//		return SUCCESS;
//	}
	
	public String execute() { 
		return "success";
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.sessionData = session;
	}

}
