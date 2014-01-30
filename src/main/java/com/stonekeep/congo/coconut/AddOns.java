package com.stonekeep.congo.coconut;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.Preparable;
import com.stonekeep.congo.dao.AddOnDAO;
import com.stonekeep.congo.data.AddOn;
import com.stonekeep.congo.data.AddOnConfiguration;
import com.stonekeep.congo.data.Convention;
import com.stonekeep.congo.data.Registrant;

public class AddOns implements Preparable, SessionAware, ServletRequestAware {
	private final AddOnDAO addonDAO;
	public String createbutton = null;
	public String exitbutton = null;
	public String function = null;
	public String editbutton = null;
	public String AddOnaction = null;  // This is used on the properties editor screen
	private Map<String, Object> sessionData;
	private Map<String, Object> requestData;
	private HttpServletRequest request;
	private Logger logger = Logger.getLogger(AddOns.class);
	public String name;
	public String defaultvalue;
	public String type;
	public boolean regprompt;
	public Float cost = 0.00f;
	public String description = "";
	public String scope;
	public int sequence = 0;
	public AddOnConfiguration workingao;
	public List<AddOnConfiguration> addonConfigurationList = new ArrayList<AddOnConfiguration>();
	public List<AddOn> addonList = new ArrayList<AddOn>();
	public String message ;
	
	public AddOns(AddOnDAO AddOnDAO) {
		this.addonDAO = AddOnDAO;
	}

	public String listAddOnConfigurations() throws SQLException {
		logger.debug("listAddOnConfigurations called");
		int cid = ((Convention) sessionData.get("conference")).getConCID();
		addonConfigurationList = addonDAO.listAddOnConfigurations(cid);
		return Action.SUCCESS;
	}

	public String listAddOns() throws SQLException {
		logger.debug("listAddOns called");
		int cid = ((Convention) sessionData.get("conference")).getConCID();
		int rid = ((Registrant) sessionData.get("workingregistrant")).getRid();
		addonList = addonDAO.listAddOn(cid, rid);
		return Action.SUCCESS;
	}


	// The 'create' or 'exit' button on the properties maintenance screen.
	public String formButton() throws SQLException {
		if (createbutton != null) {
			return Action.SUCCESS;
		}
		if (exitbutton != null) {
			return "exit";
		}

		// Did they pass in a param like 'function'?
		if (function != null) {
			if (function.equals("delete")) {
				logger.debug("Deleting " + name);
				int cid = ((Convention) sessionData.get("conference"))
						.getConCID();
				//AddOnDAO.deleteAddOnConfiguration(cid, name);
				return "deleted";
			}
		}

		// Can't imagine how we got here, unless some unscrupulous vermin added
		// a third
		// button.
		return "exit";
	}

	// Create a new AddOnconfiguration.
	public String createAddOnConfiguration() throws SQLException {
		if (createbutton != null) {
			// Do some sanity checking...
			if (type.equalsIgnoreCase("-1")) {
				message = "Please select a AddOn type.";
				return "input";
			}
			logger.debug("Creating new AddOnConfiguration...");
			int cid = ((Convention) sessionData.get("conference")).getConCID();
			AddOnConfiguration pc = new AddOnConfiguration();
			logger.debug("pc has the value " + pc);
			logger.debug("Setting name to " + name + " and cost to " + cost);
//			pc.cid = cid;
//			pc.name = name;
//			pc.scope = "Event";
//			pc.cost = cost;
//			pc.defaultvalue = defaultvalue;
//			pc.regprompt = regprompt;
//			pc.description = description;
//			pc.sequence = sequence;
//			pc.type = type;
//			pc.scope = scope;
//			AddOnDAO.saveAddOnConfiguration(pc);
		}
		return "success";
	}

	public String update() {
		return null;
	}

	// Load up a PC for editing.
	public String editAddOnConfiguration() throws SQLException {
		logger.info("i'm going to load name " + name);
		int cid = ((Convention) sessionData.get("conference")).getConCID();
//		AddOnConfiguration workingao = AddOnDAO.getAddOnConfiguration(
//				cid, name);
//		logger.debug("Fetching working pc (description is "
//				+ workingao.description + ")");
		sessionData.put("workingao", workingao);
		return Action.SUCCESS;
	}

	// Save away one that was just edited.
	public String updateAddOnConfiguration() throws SQLException {
		logger.info("Updating AddOn...");
		if (editbutton != null) {
			logger.info("Saving...");
			AddOnConfiguration wpc = (AddOnConfiguration) sessionData
					.get("workingao");
			sessionData.remove("workingao"); // clean up our session a bit.
//			wpc.defaultvalue = defaultvalue;
//			wpc.description = description;
//			wpc.scope = scope;
//			wpc.regprompt = regprompt;
//			wpc.sequence = sequence;
//			wpc.type = type;
//			AddOnDAO.updateAddOnConfiguration(wpc);

		}
		return Action.SUCCESS;
	}

	@Override
	public void prepare() throws Exception {
		logger.info("Properties preparing...");
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.sessionData = session;
	}

//	public List<AddOnConfiguration> getAddOnConfigurationList() {
//		return this.AddOnConfigurationList;
//	}

	public String getName;
	public String getDefaultvalue;
	public String getType;
	public boolean getRegprompt;
	public Float getCost;
	public String getDescription;
	public String getScope;
	public int getSequence;

	public AddOnConfiguration getworkingao() {
		return this.workingao;
	}

	@Override
	public void setServletRequest(HttpServletRequest request){
		this.request = request;
	}

	public HttpServletRequest getServletRequest(){
		return request;
	}
}
