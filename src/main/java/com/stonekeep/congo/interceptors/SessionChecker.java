package com.stonekeep.congo.interceptors;

import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.stonekeep.congo.dao.ConventionDAO;
import com.stonekeep.congo.dao.SettingDAO;
import com.stonekeep.congo.data.Convention;


/**
 * @author shevett
 * The primary interceptor for all CONGO operations, this class will
 * make sure configuration information is in place, check to see if an
 * active session is available, and, hopefully, set customer information.
 */
public class SessionChecker extends AbstractInterceptor {
	
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(SessionChecker.class);
	private final ConventionDAO conventionDAO;
	private final SettingDAO settingDAO;

	private Exception setupException = null;
	private Map sessionData = null;
	
	private String operators;	// Injected
	private String keyname;		// injected
	private String keytext;		// injected
	
	public void setOperators(String oplist) {this.operators = oplist;}
	public void setKeyname(String keyname) { this.keyname = keyname; }
	public void setKeytext(String keytext) { this.keytext = keytext; }
	
	public SessionChecker(ConventionDAO conventionDAO, SettingDAO settingDAO) {
		this.conventionDAO = conventionDAO;
		this.settingDAO = settingDAO;
		logger.debug("SessionChecker instantiated.");
	}
	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		logger.debug("executing...");
		sessionData = (Map)invocation.getInvocationContext().getContextMap().get("session");
		String aClass = invocation.getInvocationContext().getActionInvocation().getAction().getClass().toString();
		logger.debug(aClass);
		logger.debug("Session map is " + sessionData.size() + " elements long.");
		
		sessionData.put("settings",settingDAO.listSettings());
		
		dumpMap(sessionData);
		
		if (setupException != null) {
			throw setupException;
		}
		if (aClass.indexOf("Logon") > -1) {
			logger.debug("This is a logon session, let 'er rip.");
			return invocation.invoke();
		}
		if (sessionData.get("coconutuser") != null) {

			Convention c = (Convention)sessionData.get("conference") ;
			conventionDAO.recalculate(c);
			sessionData.put("conference", c);
			return invocation.invoke();
		} else {
			logger.info("No active session, redirecting to login page.");
			return "login";
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
	
	public void dumpMap(Map mp) {
	    Iterator it = mp.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        logger.debug("---> " + pairs.getKey() + " = " + pairs.getValue());
	    }
	}
}
