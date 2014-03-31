package com.stonekeep.congo.coconut;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.Action;
import com.stonekeep.congo.dao.HistoryDAO;
import com.stonekeep.congo.dao.PropertyDAO;
import com.stonekeep.congo.dao.SettingDAO;
import com.stonekeep.congo.dao.StateDAO;
import com.stonekeep.congo.data.Badge;
import com.stonekeep.congo.data.Convention;
import com.stonekeep.congo.data.History;
import com.stonekeep.congo.data.Property;
import com.stonekeep.congo.data.Registrant;
import com.stonekeep.congo.data.Setting;
import com.stonekeep.congo.data.State;
import com.stonekeep.congo.util.SystemBadge;

public class PrintBadge implements Action, SessionAware, ServletRequestAware {
	private Logger logger = Logger.getLogger(PrintBadge.class);
	public String printbutton= null;
	public boolean printbadge = false;
	public boolean checkin = true;
	public Map<String, Object> sessionData;
	public List<Property> promptProperties;
	private Map<String,Setting>settings;
	public String message;
	private HttpServletRequest request;

	// Injected values we'll need to know about
	public String tmpdir = "/tmp/" ;
	public String imagedir = "/tmp/images/";
	public String printcommand = "lpr -Ppool";
	public String badgeIdentifier;
	public int cid;
	
	// Injected via constructor
	public final StateDAO stateDAO;
	public final HistoryDAO historyDAO;
	public final SettingDAO settingDAO;
	public final PropertyDAO propertyDAO;

	public Registrant r;
	public Convention c;

	// private final RegistrantDAO registrantDAO;

	public PrintBadge(StateDAO stateDAO,HistoryDAO historyDAO, SettingDAO settingDAO, PropertyDAO propertyDAO) {
		this.stateDAO = stateDAO;
		this.historyDAO = historyDAO;
		this.settingDAO = settingDAO;
		this.propertyDAO = propertyDAO;
		logger.debug("PrintBadge initiated");
	}
	
	public String setupPrintBadge() throws Exception {
		r = (Registrant) sessionData.get("workingregistrant");
		Convention c = (Convention)sessionData.get("conference");
		cid = c.getConCID();
		logger.debug("Here we load the properties to prompt...");
		logger.debug("Getting list of properties tht will be prompted...");
		List<Property> props = propertyDAO.listAllProperties(cid, r.rid);
		promptProperties = new ArrayList<Property>();
		for (Property p : props) {
			if (p.regprompt) {
				logger.debug("Adding '" + p.name + "' for prompting.");
				promptProperties.add(p);
			}
		}
		logger.debug("promptProperties has " + promptProperties.size() + " elements.");
		sessionData.put("promptProperties", promptProperties);	// Save off the list of stuff we prompted for.
		return SUCCESS;
	}

	@Override
	public String execute() throws Exception {
		
		settings = settingDAO.listSettings();
		if (settings.containsKey("event_printcommand")) {
			printcommand = settings.get("event_printcommand").getSettingValue();
		} else {
			message="The event printcommand is not set. Please go to Maintenance->Settings to define.";
			return "message";
		}
		
		logger.debug("PrintBadge called ------------------------------------------------------------------");
		logger.debug("Settings from the page I was just on :");
		logger.debug("printbutton is :" + printbutton);
		logger.debug("printbadge is -:" + printbadge);
		logger.debug("checkin is ----:" + checkin);
		logger.debug("Settings injected from the or from settings... : ");
		logger.debug("tmpdir --------:" + tmpdir);
		logger.debug("imagedir ------:" + imagedir);
		logger.debug("printcommand --:" + printcommand);
		r = (Registrant) sessionData.get("workingregistrant");
		Registrant operator = (Registrant) sessionData.get("coconutuser");
		Convention c = (Convention)sessionData.get("conference");
		cid = c.getConCID();
		if (printbutton.equalsIgnoreCase("preview")) {
			logger.debug("Preview button selected.  Returning 'preview' to struts...");
			return "preview";
		}
		if (printbutton.equalsIgnoreCase("exit")) {
			logger.debug("Exiting printbadge back to registrant.");
			return "exit";
		}
		if (printbutton.equalsIgnoreCase("print")) {

			if (printbadge) {
				// We are go to print.
				SystemBadge sb = new SystemBadge();
				sb.registrant = r;
				sb.convention = c;
				sb.tmpdir = tmpdir;
				sb.imagedir = imagedir;
				sb.printCommand = printcommand;
				
				Badge b = sb.generateBadge();

				sb.badge = b;
				
				logger.info("Calling SystemBadge.printIt() for " + r.firstName + " " + r.lastName + "(" + r.badgeName +")");
				try {
					sb.printIt();
				}
				catch (Exception e) {
					logger.error(e.getMessage());
					message = "Error while printing badge : " + e.getMessage() ;
					return "message";
				}

				message="Badge has been printed.";

				History h = historyDAO.create(r.rid, cid, "BADGED", operator.rid);
				historyDAO.save(h);

				// change status to badged.

				State s = stateDAO.getState(cid,r.rid);
				s.badged=true;
				stateDAO.save(s);
			}

			if (checkin) {
				logger.debug("Marking as checked in...");
				// change status to checked in.
				State s = stateDAO.getState(cid,r.rid);
				s.checkedin=true;
				stateDAO.save(s);
				History h = historyDAO.create(r.rid, cid, "CHECKEDIN", operator.rid);
				historyDAO.save(h);
				
				logger.debug("Processing regPrompt items, if there are any..");
				ArrayList<Property> promptProperties = (ArrayList<Property>)sessionData.get("promptProperties");
				for (Property p : promptProperties) {
					logger.debug("Need to check / update property " + p.name + " to value " + request.getParameter(p.name) );
					String webValue = request.getParameter(p.name);
					Property regProperty = propertyDAO.getProperty(r.rid,cid,p.name);
				
					if (regProperty.type.equals("boolean")) {
						propertyDAO.setProperty(r.rid, cid, p.name, (webValue==null) ? "0" : "1");
					} else if (! webValue.equals(regProperty.value)) {
						logger.debug("Setting value for " + p.name + " for registrant " + r.rid + " to new value " + webValue);
						propertyDAO.setProperty(r.rid, cid, p.name, webValue);
					}
				}

			}

		}
		logger.debug("Done.");
		return SUCCESS;
	}
	
	@Override
	public void setSession(Map<String, Object> session) {
		logger.debug("Receiving session data...");
		this.sessionData = session;
	}
	
	public List<Property> getPromptProperties() {
		logger.debug("Returning property list with " + promptProperties.size() + " elements.");
		return this.promptProperties;
	}
	
	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		request=arg0;	
	}
}
