package com.stonekeep.congo.interceptors;

import java.util.Map;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.stonekeep.congo.dao.ConventionDAO;
import com.stonekeep.congo.dao.SettingDAO;
import com.stonekeep.congo.data.Convention;
import com.stonekeep.congo.data.Setting;

public class PublicSessionSetup extends AbstractInterceptor {
	
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(PublicSessionSetup.class);

	private Map<String,Object> sessionData = null;
	
	private final ConventionDAO conventionDAO;
	private final SettingDAO settingDAO;

	public PublicSessionSetup(ConventionDAO conventionDAO,SettingDAO settingDAO) {
		this.conventionDAO = conventionDAO;
		this.settingDAO = settingDAO;
		logger.debug("PublicSessionSetup instantiated.");
	}
	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		logger.debug("executing...");
		sessionData = ActionContext.getContext().getSession();
		
		// load the current settings into the session.
		Map<String,Setting> settings = settingDAO.listSettings();
		logger.debug("Loading settings : " + settings.size() + " elements loaded into session.");
		sessionData.put("settings",settings);
		
		if (settings.containsKey("event_default")) {
			int cid = Integer.parseInt(settings.get("event_default").getSettingValue());
			logger.debug("Retrieving convention details for convention " + cid);
			Convention currentConvention = conventionDAO.getByID(cid);
			logger.debug("Storing convention object " + currentConvention + " into session.");
			sessionData.put("currentConvention",currentConvention);
			return invocation.invoke();
		} else {
			logger.warn("No event_default in settings.  Returning not configured.");
			return "notconfigured";
		}
	}
}
