package com.stonekeep.congo.coconut;

import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Action;
import com.stonekeep.congo.dao.TemplateDAO;
import com.stonekeep.congo.data.Convention;
import com.stonekeep.congo.data.Template;

public class EditTemplate extends Editor {

	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(EditTemplate.class);
	public String id;
	public Map<String, Template> templates;
	public String whichTemplate;
	public Template workingTemplate;
	private int whatcid;
	private final TemplateDAO templateDAO;
	public String message;
	
	public EditTemplate(TemplateDAO TemplateDAO) {
		this.templateDAO = TemplateDAO;
	}

	public void prepare() throws Exception {
		logger.debug("prepare: Prepare starting");
		if (templates == null) {
			logger.debug("prepare: Initializing templateDAO and getting templates");
			whatcid = ((Convention) sessionData.get("conference")).conCID;
			templates = new TreeMap(templateDAO.list(whatcid));
			logger.debug("prepare: templates is " + templates);
		}
		logger.debug("prepare: workingTemplate is : " + workingTemplate);
		logger.debug("prepare: id is " + id);
		if (id != null) {
			if (id.length() > 1) {
				// asked to load up the working template...
				logger.debug("prepare: Fetching Template with id "	+ id);
				workingTemplate = templateDAO.get(whatcid, id);
				if (workingTemplate == null) { 
					workingTemplate = templateDAO.get(0,id);
				}
			} else {
				logger.debug("prepare: Must be a create, instantiating new.");
				workingTemplate = new Template();
				workingTemplate.setCID(whatcid);
				workingTemplate.setName(whichTemplate);
			}
		} else {
			logger.debug("prepare: yep, id is null");
		}
		logger.debug("Exiting prepare - workingTemplate is " + workingTemplate);
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
		logger.debug("view: Loading template " + whichTemplate);
		workingTemplate = templateDAO.get(whatcid, whichTemplate);
		if (workingTemplate == null) { 
			workingTemplate = templateDAO.get(0,whichTemplate);
		}
		id = workingTemplate.getName();
		return Action.SUCCESS;
	}
	
	public String deletePrompt() throws Exception {
		logger.debug("deletePrompt: Loading template " + whichTemplate);
		workingTemplate = templateDAO.get(whatcid, whichTemplate);
		if (workingTemplate == null) { 
			workingTemplate = templateDAO.get(0,whichTemplate);
		}
		return Action.SUCCESS;
	}
	public String delete() throws Exception {
		logger.debug("delete...");
		logger.debug("I'll be deleting " + whichTemplate);
		templateDAO.delete(whichTemplate);
		return Action.SUCCESS;
	}

	public String update() throws Exception {
		logger.debug("updating...");
		logger.debug("update: id is " + id);
		logger.debug("update: Button is " + typebutton);
		logger.debug("update: workingTemplate is " + workingTemplate);
		if (typebutton != null) {
			// Here we need to validate some stuff.
			if (workingTemplate == null) {
				logger.debug("Null workingTemplate.  Returning INPUT");
				message="An error occurred creating or saving this Template.";
				return INPUT;
			}
			if (workingTemplate.getName().length() < 1) {
				logger.debug("No regname specified .  Returning INPUT");
				message="Please specify a name for this registration type";
				return INPUT;
			}
			logger.debug("updateagain: Button is " + typebutton);
			if (id.length() < 1) {
				logger.debug("update: create, creating...");
				templateDAO.create(workingTemplate);
			} else {
				logger.debug("update: saving, this is an update...");
				templateDAO.save(workingTemplate);
			}
		}
		logger.debug("Exiting update()");
		return Action.SUCCESS;
	}
	

}
