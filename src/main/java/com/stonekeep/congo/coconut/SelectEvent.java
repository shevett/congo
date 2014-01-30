package com.stonekeep.congo.coconut;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.Action;
import com.stonekeep.congo.dao.ConventionDAO;
import com.stonekeep.congo.dao.SettingDAO;
import com.stonekeep.congo.data.Convention;
import com.stonekeep.congo.data.Setting;

public class SelectEvent implements SessionAware {
	private Logger logger = Logger.getLogger(SelectEvent.class);
	public Integer id ;
	public String selectbutton;
	public SortedMap<Integer, Convention> eventlist;

	private Map<String, Object> sessionData;
	private final ConventionDAO conventionDAO;
	private final SettingDAO settingDAO;

	public SelectEvent(ConventionDAO conventionDAO,SettingDAO settingDAO) {
		this.conventionDAO = conventionDAO;
		this.settingDAO = settingDAO;
	}

	public String activate() throws Exception {
		logger.debug("Activating event " + id);
		Map<String,Setting> oldSettings = settingDAO.listSettings();
		Setting setting = oldSettings.get("event_default");
		if (setting == null) {
			logger.debug("Creating new 'event_default' key...");
			setting = new Setting();
			setting.setSettingName("event_default");
			setting.setSettingValue(id.toString());
			setting.setSettingType("string");
			settingDAO.add(setting);
		} else {
			setting.setSettingValue(id.toString());
			settingDAO.update(setting);
		}
		return Action.SUCCESS;
	}
	
	public String select() throws Exception {
		logger.debug("Selecting event " + id);
		Convention tmp = conventionDAO.getByID(id);
		sessionData.put("conference", tmp);
		return Action.SUCCESS;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.sessionData = session;
	}
}
