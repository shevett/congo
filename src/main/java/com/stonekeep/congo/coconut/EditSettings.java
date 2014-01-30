package com.stonekeep.congo.coconut;

import java.sql.SQLException;
import java.util.Enumeration;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.BeanMap;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
import com.stonekeep.congo.dao.SettingDAO;
import com.stonekeep.congo.data.Setting;

public class EditSettings extends ActionSupport implements SessionAware,ServletRequestAware {

	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(EditSettings.class);
	public String message;
	private Map<String, Object> sessionData;
	private HttpServletRequest request;

	private final SettingDAO settingDAO;

	public EditSettings(SettingDAO settingDAO) {
		this.settingDAO = settingDAO;
	}
	
	public String execute() throws Exception {
		logger.debug("Executing...");
		// Get a list of settings in the db...
		Map<String,Setting> oldSettings = settingDAO.listSettings();
		
		// First, what's the list of submitted values in the request?
		Map<String,String[]> requestMap = request.getParameterMap();
		for (String s : requestMap.keySet()) {
			if (s.contains("_")) {
				if (oldSettings.containsKey(s)) {	// This key already exists, update it
					logger.debug("Updating existing key " + s + " to new value " + requestMap.get(s)[0]);
					Setting setting = oldSettings.get(s);
					if (validateValue(setting,requestMap.get(s)[0])) {
						setting.setSettingValue(requestMap.get(s)[0]);
						settingDAO.update(setting);
					} else {
						return "Setting " + setting.getSettingName() + " must have a type of " + setting.getSettingType();
					}
				} else {							// no key for this value, create it.
					logger.debug("Creating new key " + s + " with new value " + requestMap.get(s)[0]);
					Setting setting = new Setting();
					setting.setSettingName(s);
					setting.setSettingValue(requestMap.get(s)[0]);
					setting.setSettingType("string");
					settingDAO.add(setting);
				}
			} else {
				logger.debug("Key " + s + " skipped (no '_', therefore not a setting)");
			}
			
		}
		
		message="Something important here.";
		return SUCCESS;
	}
	
	private boolean validateValue(Setting setting, String string) {
		String settingType = setting.getSettingType();
		// this needs tighter value control here.
		return true;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		logger.debug("Receiving session data...");
		this.sessionData = session;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		request=arg0;	
	}
}
