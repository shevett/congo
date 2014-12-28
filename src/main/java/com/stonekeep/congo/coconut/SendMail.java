package com.stonekeep.congo.coconut;

import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.opensymphony.xwork2.Action;
import com.stonekeep.congo.dao.ConventionDAO;
import com.stonekeep.congo.dao.EmailDAO;
import com.stonekeep.congo.dao.HistoryDAO;
import com.stonekeep.congo.dao.RegistrantDAO;
import com.stonekeep.congo.dao.TemplateDAO;
import com.stonekeep.congo.data.Convention;
import com.stonekeep.congo.data.Email;
import com.stonekeep.congo.data.History;
import com.stonekeep.congo.data.Registrant;
import com.stonekeep.congo.data.Template;
import com.stonekeep.congo.util.SMTP;

public class SendMail implements Action,SessionAware {
	private Logger logger = Logger.getLogger(SendMail.class);

	private final SMTP smtp;
	public final RegistrantDAO registrantDAO;
	private final HistoryDAO historyDAO;
	public final EmailDAO emailDAO;
	private final TemplateDAO templateDAO;
	private final ConventionDAO conventionDAO;
	
	private Map<String, Object> sessionData;
	
	public String templateBody;
	public String message = null;
	public String sendButton = null;
	public String exitButton = null;
	public String previewButton = null;
	public Map<String,Template> templateList;
	public String selectedTemplate = null;
	
	public int cid;		//injected!
	
	public Convention c;
	public Registrant r;
	public Email e;
	public String target;
	public String primaryEmail;
	
	public String getMessage() { return this.message; }
	public String getPrimaryEmail() { return this.primaryEmail; }
	
	public int getCid() {	return cid; }
	public void setCid(int cid) {this.cid = cid;}

	public SendMail(SMTP smtp,
			RegistrantDAO registrantDAO, 
			HistoryDAO historyDAO, 
			EmailDAO emailDAO, 
			TemplateDAO templateDAO,
			ConventionDAO conventionDAO) {
		logger.debug("SendMail initialized...");
		this.smtp=smtp;
		this.registrantDAO = registrantDAO;
		this.historyDAO = historyDAO;
		this.emailDAO = emailDAO;
		this.templateDAO = templateDAO;
		this.conventionDAO = conventionDAO;
	}
	
	// populate local instances of Registrant and Conference
	private VelocityContext setup() throws SQLException {
		VelocityContext context = new VelocityContext();
		c = (c==null ? (Convention)conventionDAO.getByID(cid) : c);
		
		logger.debug("VelocityContext Setup: r is " + r + ", and c is " + c);
		// It's important to make sure that the registrant has the correct state info
		registrantDAO.updateCurrentState(r, c.getConCID());
		
		e = (Email)emailDAO.getEmail(r.rid);
		if (e != null) {
			primaryEmail = e.getAddress();
		} else {
			message="No email address available!";
		}
	
		/* Merge data model with template */
		context.put("EventName",c.conName);
		context.put("EventEmail",c.getConEmail());
		context.put("EventWebsite",c.getConWebsite());
		context.put("EventStart",c.getConStart());
		
		context.put("RegistrantID",r.getRid());
		context.put("RegistrantLastName",r.getLastName());
		context.put("RegistrantFirstName",r.getFirstName());
		context.put("RegistrantBadgeName",r.getBadgeName());
		
		context.put("RegistrantEmail",primaryEmail);

		context.put("RegistrantType",r.currentState.regtype);
		
		return context;
	}
	

	public String getTemplateBody() {
		return this.templateBody;
	}

	public String preview() throws Exception {
		// Load up a preview for the nice folks.
		logger.debug("Setting up preview... (cid is " + cid + " c is " + c + ", template to render is " + selectedTemplate);
		
		r = (Registrant)sessionData.get("workingregistrant");
		c = (Convention)sessionData.get("conference");
		
		cid = c.getConCID();
		logger.debug("Workingregistrant shows: # " + r.getRid() + " : " + r.getFirstName() + " " + r.getLastName());
		
		// Get list of templates and let them see them...
		
		templateList = templateDAO.list(cid);
		
		if (selectedTemplate == null) {
			selectedTemplate = "Email-1";
		}
		
		Template t = templateDAO.get(cid,selectedTemplate);
		if (t == null) {
			message = "No such template '" + selectedTemplate + "' found for event " + cid;
			return "message";
		}
		String templateSource = t.getBody();
		
		// Ready to go!
		Velocity.init();
		VelocityContext context = setup();
		
		/* Generate the output */
		StringWriter out =  new StringWriter();

		Velocity.evaluate(context,out,"onthefly",templateSource);
		
		// Dump the resulting template render into what's being returned.
		templateBody = out.toString();
		
		// The preview form has some other fields it'll need...
		
		logger.debug("Fetching email address for registrant " + r.rid);
		e = (Email)emailDAO.getEmail(r.rid);
		if (e != null) {
			primaryEmail = e.getAddress();
		} else {
			message="No email address available!";
		}
		
		return SUCCESS;
	}
	
	public String sendMail() throws Exception {
		// Here we can either come from Coconut or from the public interface.  Find the 
		// convention...
		if (c == null) {
			if (sessionData.containsKey("conference")) {
				c = (c==null ? (Convention)sessionData.get("conference") : c);
			} else {
				c = (c==null ? (Convention)sessionData.get("currentConvention") : c);
			}
		}
		cid = c.getConCID();
		logger.debug("exitButton is " + exitButton);
		logger.debug("sendButton is " + sendButton);
		logger.debug("previewButton is " + previewButton);
		logger.debug("cid is " + cid);
		logger.debug("c is " + c);
		if (exitButton != null) {
			return SUCCESS;
		}
		if (previewButton != null) {
			return INPUT;
		}
		
		r = (r==null ? (Registrant)sessionData.get("workingregistrant") : r);

		// Make sure a default template is available ( bug #316 )
		if (selectedTemplate == null) {
			selectedTemplate = "Email-1";
		}
		
		logger.debug("Fetching " + selectedTemplate + " template for event " + cid);
		Template t = templateDAO.get(cid,selectedTemplate);
		if (t == null) {
			logger.warn("Template '" + selectedTemplate + "' for event " + cid + " not found.  Returning warning message.");
			message = "No such template '" + selectedTemplate + "' found for event " + cid;
			return "message";
		}
		String templateSource = t.getBody();

		Velocity.init();
		VelocityContext context = setup();
		StringWriter out = new StringWriter();
		Velocity.evaluate(context,out,"onthefly",templateSource);

		// Mock setup
		if (target == null) {
			r = (r==null ? (Registrant)sessionData.get("workingregistrant") : r);
			Email e = emailDAO.getEmail(r.rid);
			target = e.address;
		}
		
		logger.debug("'target' for email is " + target);
		
		message = smtp.sendAnyMail(target, out.toString());
		if (message != null) {
			return "message";
		}
		
		// Record the history...
		History h = historyDAO.create(r.rid,c.getConCID(),"EMAIL",r.rid);
		h.arg1 = target;
		h.comment = "Sent email";
		historyDAO.save(h);
		
		logger.debug("Mail sent to " + target);
		return SUCCESS;
	}
	
	@Override
	public void setSession(Map<String, Object> session) {
		logger.debug("Session being set via setSession.");
		this.sessionData = session;
	}

	@Override
	public String execute() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
}