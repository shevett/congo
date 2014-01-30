package com.stonekeep.congo.interceptors;

import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;


public class AdministratorChecker extends AbstractInterceptor {
	
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(AdministratorChecker.class);

	private Exception setupException = null;
	private Map sessionData = null;
	
	public String message;
	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		logger.debug("executing...");
		sessionData = (Map)invocation.getInvocationContext().getContextMap().get("session");
		String userType = ((String) sessionData.get("userType"));
		if (! userType.equals("Administrator")) {
			message="You do not have permission to view that resource.";
			return "message";
		} else {
			return invocation.invoke();
		}
	}

	@Override
	public void init() {
		logger.debug("Initted.");
	}
	
	@Override
	public void destroy() {
		logger.debug("Destroyed.");
	}
}
