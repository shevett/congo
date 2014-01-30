package com.stonekeep.congo.coconut;

import java.io.InputStream;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Logger;
import org.apache.struts2.util.ServletContextAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.Action;
import com.stonekeep.congo.dao.ConventionDAO;
import com.stonekeep.congo.dao.SettingDAO;
import com.stonekeep.congo.dao.TemplateDAO;
import com.stonekeep.congo.data.Convention;
import com.stonekeep.congo.data.Setting;

public class EditEvent extends Editor implements ServletContextAware {

	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(EditEvent.class);
	public Integer id;
	public Map<Integer, Convention> events;
	public String whichEvent;
	public Convention workingEvent;
	public String message;
	public String saveButton;
	public String cancelButton;
	
	public String eventDefault ;
	
	private ServletContext sc;

	
	// Why was i unable to autowire this?
	//	@Autowired
	private final ConventionDAO eventDAO;
	private final SettingDAO settingDAO;
	
	public EditEvent(ConventionDAO eventDAO,SettingDAO settingDAO) {
		this.eventDAO = eventDAO;
		this.settingDAO = settingDAO;
	}
	
	public void prepare() throws Exception {
		logger.debug("prepare: Prepare starting");
		if (events == null) {
			logger.debug("prepare: Initializing conventionDAO and getting events");
			logger.debug("ConventionDAO is " + eventDAO);
			events = new TreeMap(eventDAO.list());
			logger.debug("prepare: conventions is " + events);
		}
		logger.debug("prepare: workingConvention is : " + workingEvent);
		logger.debug("prepare: id is " + id);
		if (id != null) {
			if (id > 1) {
				// asked to load up the working convention...
				logger.debug("prepare: Fetching convention with id " + id);
				workingEvent = eventDAO.getByID(id);
			} else {
				logger.debug("prepare: Must be a create, instantiating new.");
				workingEvent = new Convention();
			}
		} else {
			logger.debug("prepare: yep, id is null");
		}
		Map<String,Setting> settings = settingDAO.listSettings();
		Setting s = settings.get("event_default");
		eventDefault = s.getSettingValue();
		logger.debug("prepare: event default value is " + eventDefault);
		logger.debug("Exiting prepare - workingConvention is " + workingEvent);
	}

	public String view() throws Exception {
		logger.debug("view...");
		if (typebutton != null) {
			return Action.SUCCESS;
		}
		if (exitbutton != null) {
			return "exit";
		}
		// load up the data for the edit form...
		logger.debug("view: Loading convention " + id);
		workingEvent = eventDAO.getByID(id);
		return Action.SUCCESS;
	}
	
	public String deletePrompt() throws Exception {
		return Action.SUCCESS;
	}
	
	public String create() throws Exception {
		Convention e = new Convention();
		eventDAO.create(e);
		return SUCCESS;
	}
	
	public String delete() throws Exception {
		logger.debug("delete...");
		if (cancelButton != null) {
			return "cancel";
		}
		logger.debug("I'll be deleting " + id);
		eventDAO.delete(id);
		return Action.SUCCESS;
	}

	public String update() throws Exception {
		logger.debug("updating...");
		logger.debug("update: id is " + id);
		logger.debug("update: saveButton is " + saveButton);
		logger.debug("update: cancelButton is " + cancelButton);
		logger.debug("update: workingEvent is " + workingEvent);
		// Check if they hit cancel...
		if (cancelButton != null) { 
			return "cancel";
		}
		// Here we need to validate some stuff.
		if (workingEvent == null) {
			logger.debug("Null workingEvent.  Returning INPUT");
			message="An error occurred creating or saving this event.  No Event was found.";
			return INPUT;
		}
		if (workingEvent.getConName().length() < 1) {
			logger.debug("No event name specified .  Returning INPUT");
			message="Please specify a name for this event";
			return INPUT;
		}
		
		try {

			// Validate the dates...
			//		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			//		workingEvent.conStart = new java.sql.Date(df.parse(e).getTime());
			//		workingEvent.conEnd = new java.sql.Date(df.parse(conEnd).getTime());

			// Validate the XML in the badge layout...
			String schemaLang = "http://www.w3.org/2001/XMLSchema";
			SchemaFactory factory = SchemaFactory.newInstance(schemaLang);

			InputStream xsdSource = sc.getResourceAsStream("/WEB-INF/badgelayout.xsd");
			Schema schema = factory.newSchema(new StreamSource(xsdSource));
			Validator validator = schema.newValidator();

			// at last perform validation:
			if (workingEvent.conBadgelayout.length() > 0) {
				logger.debug("Validating the conBadgeLayout using the XSD...");
				Source src = new StreamSource(new StringReader(workingEvent.conBadgelayout));
				validator.validate(src);
				logger.debug("Validation passed.");
			}
		} catch (Exception e) {
			logger.error("Error parsing convention dates : " + e.getMessage()) ;
			message = "Error while performing Badge XML Validation:<br> " + e.getMessage();
			return INPUT;
		}

	logger.debug("update: saving, this is an update...");
	eventDAO.save(workingEvent);
	logger.debug("Exiting update()");
	return Action.SUCCESS;
}

	@Override
	public void setServletContext(ServletContext arg0) {
		this.sc = arg0;
	}
	

}
