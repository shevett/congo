package com.stonekeep.congo.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.apache.commons.net.smtp.AuthenticatingSMTPClient;
import org.apache.commons.net.smtp.SMTPClient;
import org.apache.commons.net.smtp.SMTPReply;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.stonekeep.congo.dao.SettingDAO;
import com.stonekeep.congo.dao.TemplateDAO;
import com.stonekeep.congo.data.Convention;
import com.stonekeep.congo.data.Registrant;
import com.stonekeep.congo.data.Setting;
import com.stonekeep.congo.data.Template;

public class SMTP extends AuthenticatingSMTPClient {
	private Logger logger = Logger.getLogger(SMTP.class);

	// Injected values from spring...
	private int smtpport;	
	private String smtpserver;
	private String smtpfromaddress;
	private String smtpfromname;
	private String smtpbcc;
	private String smtpusername;
	private String smtppassword;
	private String smtpauth;
	private String smtptls;
	private int cid;
	
	public void setcid(int whatcid) { cid=whatcid; }
	
	public String message = null;

	private final TemplateDAO templateDAO;	// injected
	private final SettingDAO settingDAO; // injected
	
	private Map<String,Setting>settings;
	
	
	public SMTP(TemplateDAO templateDAO,SettingDAO settingDAO) throws Exception {
		logger.debug("SMTP initialized...");
		this.templateDAO = templateDAO;
		this.settingDAO = settingDAO;
	}
	
	public String sendAnyMail(String target,String messagebody) throws Exception {
		loadSettings();
		message = null;
		if (smtpserver.length() < 1) {
			message = "No SMTP server defined.  Email to " + target + " was not sent.";
			logger.warn(message);
			return message;
		}
		try {
			setDefaultPort(smtpport);	// Still need to enable authentication here.
			int reply;
			logger.debug("Connecting to " + smtpserver + " on port " + smtpport + " via methodology " + smtpauth + ".  Use TLS is " + smtptls + " ...");
			connect(smtpserver);
			logger.debug(getReplyString());
			if (smtptls.equalsIgnoreCase("Yes")) {
				logger.debug("Attempting to do a STARTTLS...");
				execTLS();
				logger.debug(getReplyString());
			} else {
				logger.debug("Skipping STARTTLS attempt due to mail settings configuration.");
			}

			logger.debug("Saying EHLO....");
			ehlo("arisia.org");
			logger.debug(getReplyString());
			if (! smtpauth.equals("NONE")) {
				logger.debug("Authenticating using methodology " + smtpauth);
				AuthenticatingSMTPClient.AUTH_METHOD m = AuthenticatingSMTPClient.AUTH_METHOD.valueOf(smtpauth);
				if (auth(m, smtpusername, smtppassword)) {
					logger.debug("Authentication successful!");
				} else {
					message = "SMTP Authentication to " + smtpserver + " using " + smtpauth + " failed.  Cannot proceed.  Message is " + getReplyString();
					logger.error(message);
					throw new IOException(message);
				}
			}
			reply = getReplyCode();
			if(!SMTPReply.isPositiveCompletion(reply)) {
				message = "SMTP server " + smtpserver + " refused connection : " + reply ;
				logger.error(message);
			} else {
				logger.debug("SMTP server ready: " + reply);
				logger.debug("Sending main message...");
				if (sendSimpleMessage(smtpfromaddress,target,messagebody)) {
					logger.info("Email sent to " + target + " via " + smtpserver + " successfully.");
				} else {
					reply = getReplyCode();
					message = "Message send to " + target + " failed.  Server responded: " + reply + " (" + getReplyString() + ")";
					logger.error(message);
				}
				if (smtpbcc.length() > 1) {
					logger.info("Sending BCC copy...");
					if (sendSimpleMessage(smtpfromaddress,smtpbcc,messagebody)) {
						logger.info("Email sent to " + smtpbcc + " via " + smtpserver + " successfully.");
					} else {
						reply = getReplyCode();
						message = "Message send to " + smtpbcc + " via " + smtpserver + " failed.  Server responded: " + reply + " (" + getReplyString() + ")" ;
						logger.error(message);
					}
				}	
			}
			disconnect();
		} catch(IOException e) {
			if(isConnected()) {
				try {
					logger.debug("Forcing disconnect due to IOException");
					disconnect();
				} catch(IOException f) {
					// do nothing
				}
			}
			message = "Could not connect to server '" + smtpserver + "' : " + e.getMessage();
			logger.error(message);
		}
		return message;
	}

	public void sendReminderMail(Registrant r, String target,String newpassword) throws Exception {
		loadSettings();
		String templateBody = new String();
		
		// Gather up the template for a reset...
		Template t = templateDAO.get(cid,"Reminder-1");
		if (t == null) {
			throw new Exception("No such template 'Reminder-1' found for event " + cid);
		}
		String templateSource = t.getBody();
		
		// Ready to go!
		Velocity.init();
		VelocityContext context = new VelocityContext();
		context.put("RegistrantEmail",target);
		context.put("RegistrantFirstname",r.getFirstName());
		context.put("RegistrantLastName",r.getLastName());
		context.put("RegistrantBadgeName",r.getBadgeName());
				
		context.put("RegistrantID",r.getRid());
		context.put("RegistrantPassword",newpassword);
		
		/* Generate the output */
		StringWriter out =  new StringWriter();

		Velocity.evaluate(context,out,"onthefly",templateSource);
		
		// Dump the resulting template render into what's being returned.
		templateBody = out.toString();
		
		try {
			setDefaultPort(smtpport);
			int reply;
			logger.debug("Connecting to " + smtpserver + " on port " + smtpport + "...");
			connect(smtpserver);
			logger.info(getReplyString());
			if (! smtpauth.equals("NONE")) {
				logger.debug("Authenticating using methodology " + smtpauth);
				AuthenticatingSMTPClient.AUTH_METHOD m = AuthenticatingSMTPClient.AUTH_METHOD.valueOf(smtpauth);
				if (auth(m, smtpusername, smtppassword)) {
					logger.debug("Authentication successful!");
				} else {
					message = "SMTP Authentication to " + smtpserver + " using " + smtpauth + " failed.  Cannot proceed.";
					logger.error(message);
					throw new IOException(message);
				}
			}
			reply = getReplyCode();
			if(!SMTPReply.isPositiveCompletion(reply)) {
				logger.error("SMTP server refused connection : " + reply);
			} else {
				logger.info("SMTP server ready: " + reply);
				if (sendSimpleMessage(smtpfromaddress,target,templateBody)) {
					logger.info("Message sent to " + target + " successfully.");
				} else {
					reply = getReplyCode();
					logger.error("Message send to " + target + " failed.  Server responded: " + reply);
				}
			}
			disconnect();
		} catch(IOException e) {
			if(isConnected()) {
				try {
					logger.debug("Forcing disconnect due to IOException");
					disconnect();
				} catch(IOException f) {
					// do nothing
				}
			}
			message = "Could not connect to server : " + e.getMessage();
			logger.error(message);
		}
	}
	
	public void sendFriendRequest(Convention c, Registrant r, String target,String fromWhom) throws Exception {
		loadSettings();
		Template t = templateDAO.get(cid,"Link-1");
		String templateBody = new String();
		if (t == null) {
			throw new Exception("No such template 'Link-1' found for event " + cid);
		}
		String templateSource = t.getBody();
		
		// Ready to go!
		Velocity.init();
		VelocityContext context = new VelocityContext();

		// Registrant fields...
		context.put("RegistrantEmail",target);
		context.put("RegistrantFirstName",r.getFirstName());
		context.put("RegistrantLastName",r.getLastName());
		context.put("RegistrantBadgeName",r.getBadgeName());
		context.put("RegistrantID",r.getRid());
		
		// Event fields..
		context.put("EventName",c.getConName());
		context.put("EventEmail",c.getConEmail());
		context.put("EventWebsite",c.getConWebsite());
		
		/* Generate the output */
		StringWriter out =  new StringWriter();

		Velocity.evaluate(context,out,"onthefly",templateSource);
		
		// Dump the resulting template render into what's being returned.
		templateBody = out.toString();
		logger.debug("Message, post-processing, resolves to:");
		logger.debug("---------");
		logger.debug(templateBody);
		logger.debug("---------");
		try {
			setDefaultPort(smtpport);
			int reply;
			logger.debug("Connecting to " + smtpserver + " on port " + smtpport + "...");
			connect(smtpserver);
			logger.info(getReplyString());
			reply = getReplyCode();
			if(!SMTPReply.isPositiveCompletion(reply)) {
				logger.error("SMTP server refused connection : " + reply);
			} else {
				logger.info("SMTP server ready: " + reply);
				if (sendSimpleMessage(smtpfromaddress,target,templateBody)) {
					logger.info("Message sent to " + target + " successfully.");
				} else {
					reply = getReplyCode();
					logger.error("Message send to " + target + " failed.  Server responded: " + reply);
				}
			}
			disconnect();
		} catch(IOException e) {
			if(isConnected()) {
				try {
					logger.debug("Forcing disconnect due to IOException");
					disconnect();
				} catch(IOException f) {
					// do nothing
				}
			}
			message = "Could not connect to server : '" + smtpserver + "' : " + e.getMessage();
			logger.error(message);
		}
	}
	
	private void loadSettings() {
		settings = settingDAO.listSettings();
	
		// This allows for the possibility that the current cid may be set by the caller.
		if (cid == 0) {
			cid = Integer.parseInt(settings.get("event_default").getSettingValue());
		}
		
		smtpport = Integer.parseInt(settings.get("smtp_port").getSettingValue());
		smtpserver = settings.get("smtp_server").getSettingValue();
		smtpfromaddress = settings.get("smtp_fromaddress").getSettingValue();
		smtpfromname = settings.get("smtp_fromname").getSettingValue();
		smtpbcc = settings.get("smtp_bcc").getSettingValue();
		smtpauth = settings.get("smtp_method").getSettingValue();
		smtpusername = settings.get("smtp_username").getSettingValue();
		smtppassword = settings.get("smtp_password").getSettingValue();
		smtptls = settings.get("smtp_tls").getSettingValue();
		
	}

}
