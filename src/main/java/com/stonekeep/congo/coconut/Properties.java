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
import com.stonekeep.congo.dao.PropertyDAO;
import com.stonekeep.congo.data.Convention;
import com.stonekeep.congo.data.Property;
import com.stonekeep.congo.data.PropertyConfiguration;
import com.stonekeep.congo.data.Registrant;

public class Properties implements Preparable, SessionAware, ServletRequestAware {
	private final PropertyDAO propertyDAO;
	public String createbutton = null;
	public String exitbutton = null;
	public String function = null;
	public String editbutton = null;
	public String propertyaction = null;  // This is used on the properties editor screen
	private Map<String, Object> sessionData;
	private Map<String, Object> requestData;
	private HttpServletRequest request;
	private Logger logger = Logger.getLogger(Properties.class);
	public String name;
	public String defaultvalue;
	public String type;
	public boolean regprompt;
	public Float cost = 0.00f;
	public String description = "";
	public String scope;
	public String format;
	public int sequence = 0;
	public PropertyConfiguration workingpc;
	public List<PropertyConfiguration> propertyConfigurationList = new ArrayList<PropertyConfiguration>();
	public List<Property> propertyList = new ArrayList<Property>();
	public String message ;
	
	public Properties(PropertyDAO propertyDAO) {
		this.propertyDAO = propertyDAO;
	}

	public String listPropertyConfigurations() throws SQLException {
		logger.debug("listPropertyConfigurations called");
		int cid = ((Convention) sessionData.get("conference")).getConCID();
		propertyConfigurationList = propertyDAO.listPropertyConfigurations(cid);
		return Action.SUCCESS;
	}

	public String listProperties() throws SQLException {
		logger.debug("listProperties called");
		int cid = ((Convention) sessionData.get("conference")).getConCID();
		int rid = ((Registrant) sessionData.get("workingregistrant")).getRid();
		propertyList = propertyDAO.listAllProperties(cid, rid);
		// Editability needs to be determined based on session usertype... 
		for (Property p : propertyList) {
			p.editable=true;
			if (p.name.equals("Administrator") || p.name.equals("Operator")) {
				// the only people who can change admin or operator props are admins...
				p.editable = sessionData.get("userType").equals("Administrator");
			} 
		}
		return Action.SUCCESS;
	}

	public String updateProperties() throws SQLException {
		if (exitbutton != null) {
			logger.debug("Exiting properties editor.  Seeyabye.");
			return Action.SUCCESS;
		} else {
			int cid = ((Convention) sessionData.get("conference")).getConCID();
			int rid = ((Registrant) sessionData.get("workingregistrant")).getRid();

			logger.debug("Some action happened, going back to the editor.");
			logger.debug("propertyaction is " + propertyaction);	
			logger.debug("CID to be used : " + cid);
			logger.debug("RID to be used : " + rid);
			if (propertyaction != null) {
				String[] cmd = propertyaction.split(":");
				logger.debug("Command received: " + cmd[0] + " and argument " + cmd[1]);
				
				if (cmd[0].equals("remove")) {
					// Removal passes 3 params: action:propname:isglobal
					if (cmd[2].equals("true")) {	// this is a global property
						propertyDAO.deleteProperty(rid,0,cmd[1]);
					} else {
						propertyDAO.deleteProperty(rid,cid,cmd[1]);
					}
				}
				
				if (cmd[0].equals("toggle")) {
					// Retrieve whatever the current state of this property is
					logger.debug("Retrieving rid " + rid + ", cid " + cid + ", propname is " + cmd[1]);
					Property p = propertyDAO.getProperty(rid,cid,cmd[1]);
					logger.debug("I'd need to toggle property " + p.name + " which is type " + p.type + " and currently has the value " + p.value);
					int workingcid = (p.isglobal ? 0 : cid);
					logger.debug("Calling setProperty with rid " + rid + ", cid " + workingcid + " ... ");
					if ((p.value == null) || (p.value.equals("0"))) {
						propertyDAO.setProperty(rid, workingcid, p.name,"1");
					} else {
						propertyDAO.setProperty(rid, workingcid, p.name,"0");
					}
				}
				
				if (cmd[0].equals("set")) {
					Property p = propertyDAO.getProperty(rid,cid,cmd[1]);
					String newValue = request.getParameterValues(p.name)[0];
					logger.debug("Changing value of " + p.name + " from " + p.value + " to " + newValue);
					int workingcid = (p.isglobal ? 0 : p.cid);
					logger.debug("Changing value of " + p.name + " from " + p.value + " to " + newValue + " for cid " + workingcid);
					propertyDAO.setProperty(rid,workingcid,p.name,newValue);
				}
			}
			return Action.INPUT;
		}
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
				propertyDAO.deletePropertyConfiguration(cid, name);
				return "deleted";
			}
		}

		// Can't imagine how we got here, unless some unscrupulous vermin added
		// a third
		// button.
		return "exit";
	}

	// Create a new propertyconfiguration.
	public String createPropertyConfiguration() throws SQLException {
		if (createbutton != null) {
			// Do some sanity checking...
			if (type.equalsIgnoreCase("-1")) {
				message = "Please select a property type.";
				return "input";
			}
			logger.debug("Creating new PropertyConfiguration...");
			int cid = ((Convention) sessionData.get("conference")).getConCID();
			PropertyConfiguration pc = new PropertyConfiguration();
			logger.debug("pc has the value " + pc);
			logger.debug("Setting name to " + name + " and cost to " + cost);
			pc.cid = cid;
			pc.name = name;
			pc.scope = "Event";
			pc.cost = cost;
			pc.defaultvalue = defaultvalue;
			pc.regprompt = regprompt;
			pc.description = description;
			pc.sequence = sequence;
			pc.type = type;
			pc.scope = scope;
			pc.format = format;
			propertyDAO.savePropertyConfiguration(pc);
		}
		return "success";
	}

	public String update() {
		return null;
	}

	// Load up a PC for editing.
	public String editPropertyConfiguration() throws SQLException {
		logger.info("i'm going to load name " + name);
		int cid = ((Convention) sessionData.get("conference")).getConCID();
		PropertyConfiguration workingpc = propertyDAO.getPropertyConfiguration(
				cid, name);
		logger.debug("Fetching working pc (description is "
				+ workingpc.description + ")");
		sessionData.put("workingpc", workingpc);
		return Action.SUCCESS;
	}

	// Save away one that was just edited.
	public String updatePropertyConfiguration() throws SQLException {
		logger.info("Updating property...");
		if (editbutton != null) {
			logger.info("Saving...");
			PropertyConfiguration wpc = (PropertyConfiguration) sessionData
					.get("workingpc");
			sessionData.remove("workingpc"); // clean up our session a bit.
			wpc.defaultvalue = defaultvalue;
			wpc.description = description;
			wpc.scope = scope;
			wpc.format = format;
			wpc.regprompt = regprompt;
			wpc.sequence = sequence;
			wpc.type = type;
			propertyDAO.updatePropertyConfiguration(wpc);

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

	public List<PropertyConfiguration> getPropertyConfigurationList() {
		return this.propertyConfigurationList;
	}

	public String getName;
	public String getDefaultvalue;
	public String getType;
	public boolean getRegprompt;
	public Float getCost;
	public String getDescription;
	public String getScope;
	public String getFormat;
	public int getSequence;

	public PropertyConfiguration getWorkingpc() {
		return this.workingpc;
	}

	@Override
	public void setServletRequest(HttpServletRequest request){
		this.request = request;
	}

	public HttpServletRequest getServletRequest(){
		return request;
	}
}
