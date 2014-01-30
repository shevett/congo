package com.stonekeep.congo.coconut;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.Action;

public class Logout implements Action, ServletRequestAware {
	public String logoutbutton = null;
	private Logger logger = Logger.getLogger(Logout.class);
	private HttpServletRequest request;

	@Override
	public String execute() throws Exception {
		logger.debug("Logout called");
		if (logoutbutton.equalsIgnoreCase("Logout")) {
			logout();
			return "login";
		} else {
			return "exit";
		}
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	private void logout() {
		HttpSession session = request.getSession(false);
		if (session != null)
			session.invalidate();
	}

}
