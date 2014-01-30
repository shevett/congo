package com.stonekeep.congo.web;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
import com.stonekeep.congo.dao.LinkDAO;
import com.stonekeep.congo.dao.RegistrantDAO;
import com.stonekeep.congo.data.Convention;
import com.stonekeep.congo.data.Link;
import com.stonekeep.congo.data.Registrant;
import com.stonekeep.congo.util.SMTP;

public class Links extends ActionSupport implements SessionAware {

	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(Links.class);
	private Map<String,Object> sessionData;
	public ArrayList<Link> fl;
	
	public int cid = 0;					// this is injected by spring as the preferredcid.
	public boolean paypalenabled ; 			// injected!
	public boolean authorizenetenabled ; 	// injected!'
	
	private final LinkDAO linkDAO ;
	private final RegistrantDAO registrantDAO;
	private final SMTP smtp;
	
	/* form buttons and fields */
	public String emailbutton=null;
	public String email=null;
	public String exitbutton=null;
	public String donebutton=null;
	public int id=0;
	public String message;
	public boolean dontsendmail;
	
	public void setCid(int cid) { this.cid = cid; }
	public void setPaypalenabled(boolean newvalue) { this.paypalenabled = newvalue; }
	public void setAuthorizenetenabled(boolean newvalue) { this.authorizenetenabled = newvalue; }
	
	public Links(LinkDAO linkDAO,RegistrantDAO registrantDAO, SMTP smtp) {
		this.linkDAO = linkDAO; 
		this.registrantDAO = registrantDAO;
		this.smtp = smtp;
	}

	public String execute() throws Exception {
		logger.debug("Links execute()...");
		return SUCCESS;
	}
	
	/* Handle form submission stuff from the Links.jsp page... */
	public String form() throws Exception {
		if (donebutton != null) return "done";
		if (exitbutton != null) return "exit";
		if (emailbutton != null) {
			return(generateInviteMail());
		}
		if (emailbutton != null) {
			// handle sending an invite for a friend here...
		}
		return INPUT;
	}
	
	/* A remove request */
	public String remove() throws Exception {
		logger.debug("I would be removing item " + id);
		linkDAO.remove(id);
		return SUCCESS;
	}
	
	public String accept() throws Exception {
		Link f = linkDAO.getById(id);
		f.setLinkStatus("Ok");
		linkDAO.update(f);
		return SUCCESS;
	}
	
	public String generateInviteMail() throws Exception {
		logger.debug("Generate an invite mail to " + email);
			
		if (email != null) {
			if (email.length() < 6) {
				logger.warn("Email address '" + email + "' too short...");
				message="Please enter a valid email address.";
				return INPUT;
			}
			
			Registrant friendregistrant = registrantDAO.searchbyEmail(email);
			if (friendregistrant != null) {
				
				// When a request for a friend is generated, a new Friend entry needs to be made, set to
				// Pending, and a new requestkey made.  This key will be sent to the friended user.
				Link f = new Link();
				Registrant myself = (Registrant)sessionData.get("webuser");
				Convention currentConvention = (Convention)sessionData.get("currentConvention");

				
				MessageDigest md = MessageDigest.getInstance("MD5");

				String requestSource=myself.rid + " " + email;
				
				byte[] bytesOfMessage = requestSource.getBytes("UTF-8");
				
			    String requestKey = new String(Hex.encodeHex(md.digest(bytesOfMessage)));
			    
				f.setLinkRid1(myself.rid);
				f.setLinkRequestKey(requestKey);
				f.setLinkRid2(friendregistrant.rid);
				f.setLinkStatus("Pending");
				linkDAO.add(f);
				
				// Check the form checkbox - if they didnt' ask to generate the email, skip this step...

				if (dontsendmail) {
					logger.info("Email send skipped per request.");
				} else {
					smtp.sendFriendRequest(currentConvention, myself, email,myself.firstName + " " + myself.lastName + " (" + myself.badgeName + ")");
					logger.info("Sent friend request to " + email);
				}
				return SUCCESS;
			} else {
				message = "Sorry, no registrant was found with address '" + email +"'";
				logger.warn("Registrant lookup for email " + email + " failed.  No such registrant found");
				return INPUT;
			}
		} else {
			logger.debug("returning exit");
			return "exit";
		}
	}
	
	public String register() throws Exception {
		// This needs to set up stuff for the Registration form for a LINKED uid...
		logger.debug("Link registration request for " + id);
		Link f = linkDAO.getById(id);
		Registrant myself = (Registrant)sessionData.get("webuser");
		// need to get the rid that's not me
		int otherRid = (f.getLinkRid1() == myself.rid) ? f.getLinkRid1() : f.getLinkRid2();
		Registrant r = registrantDAO.getByID(otherRid);
		logger.debug("Link reg request for registrant (" + r.rid + ") " + r.badgeName);
		sessionData.put("linkRegistrant",r);
		return SUCCESS;
	}
	
	@Override
	public void setSession(Map<String, Object> arg0) {
		this.sessionData = arg0;
	}
	


}
