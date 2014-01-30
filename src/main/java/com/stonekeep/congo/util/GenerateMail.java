package com.stonekeep.congo.util;

import java.util.Random;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.stonekeep.congo.dao.RegistrantDAO;
import com.stonekeep.congo.data.Registrant;

public class GenerateMail extends ActionSupport {
	private Logger logger = Logger.getLogger(GenerateMail.class);
	
	public String email;
	public String emailbutton;
	public String cancelbutton;
	public String message;

	private final SMTP smtp;
	private final RegistrantDAO registrantDAO;
	
	public GenerateMail(RegistrantDAO registrantDAO, SMTP smtp) { 
		this.smtp = smtp;
		this.registrantDAO = registrantDAO;
		logger.debug("email target is " + email);
		logger.debug("email button is " + emailbutton);
		logger.debug("cancelbutton is " + cancelbutton);
		logger.debug("message is " + message);
	}
	
	private String generatePassword() {
		char ch[] = new char[8];
		Random rn = new Random();
		for (int i=0; i<8; i++)
			ch[i] = (char) (rn.nextInt(26) + 97);
        return new String(ch);
	}

	public String forgot() throws Exception {
		logger.debug("Forgot action - email has value '" + email + "'");
		logger.debug("emailbutton is " + emailbutton);
		logger.debug("cancelbutton is " + cancelbutton);
		
		if (emailbutton != null) {
			if (email.length() < 6) {
				message="Please enter a valid email address.";
				return INPUT;
			}
			
			// There's a possibility there's more than one registrant for a specific email address.
			// This checks to see if that's the case.
			Object[] l = registrantDAO.searchByAny(email, 0, 0);
			Integer rowCount = (Integer)l[0];
			if (rowCount > 1) {
				message="There are more than one Registrant in the database with that email address.  " +
					"A password reset can only be done by registration staff.  Please contact them for assistance.";
				return INPUT;
			}
			Registrant r = registrantDAO.searchbyEmail(email);
			if (r != null) {
				String newPassword = generatePassword();
				registrantDAO.setPassword(r, newPassword);
				smtp.sendReminderMail(r,email,newPassword);
				logger.info("Sent password reset email for registrant " + r.rid + " to " + email);
				return SUCCESS;
			} else {
				message = "Sorry, no registrant was found with that email address.";
				logger.warn("Registrant lookup for email " + email + " failed.  No such registrant found");
				return INPUT;
			}
		} else {
			logger.debug("returning exit");
			return "exit";
		}
	}
	
	public String execute() {
		logger.debug("Generating reminder mail...");
		return SUCCESS;
	}
}