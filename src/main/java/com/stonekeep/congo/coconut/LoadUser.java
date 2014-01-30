package com.stonekeep.congo.coconut;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.Action;
import com.stonekeep.congo.dao.PropertyDAO;
import com.stonekeep.congo.dao.RegistrantDAO;
import com.stonekeep.congo.dao.RegistrationTypeDAO;
import com.stonekeep.congo.dao.StateDAO;
import com.stonekeep.congo.data.Convention;
import com.stonekeep.congo.data.Property;
import com.stonekeep.congo.data.Registrant;
import com.stonekeep.congo.data.RegistrationType;
import com.stonekeep.congo.data.State;

/**
 * Load up a user into the stack for displaying on the zoomed user page. It's
 * assumed that 'rid' is the registrant ID we're looking at.
 * 
 * @author dbs
 * 
 */
public class LoadUser implements Action, SessionAware {

	private Logger logger = Logger.getLogger(LoadUser.class);
	public int rid = 0;
	public int cid = 0;
	public Registrant r;
	private Map<String, Object> sessionData;
	public String subscribedText = "<a href=\"subscribeRegistrant.action\">Subscribe</a>";
	public String unsubscribeText = "<a href=\"unsubscribeRegistrant.action\">Unsubscribe</a>";
	public String registeredText = "<a href=\"setupRegistration.action\" accesskey=\"G\">Re[<u>g</u>]ister...</a>";
	public String voidRegistrationText = "<a href=\"VoidRegistrantForm\" accesskey=\"V\">[<u>V</u>]oid...</a>";
	public String badgedText = "<a href=\"setupPrintBadge\" accesskey=\"P\">[<u>P</u>]rint badge...</a>";
	public String checkedinText = "<a href=\"checkinRegistrant.action\" accesskey=\"C\">[<u>C</u>]heckin...</a>";
	public List<Property> propertyList = null;
	
	public int getCid() { return this.cid; } 

	private final RegistrantDAO registrantDAO;
	private final StateDAO stateDAO;
	private final PropertyDAO propertyDAO;
	private final RegistrationTypeDAO registrationTypeDAO;

	public LoadUser(RegistrantDAO registrantDAO, StateDAO stateDAO,
			PropertyDAO propertyDAO,
			RegistrationTypeDAO registrationTypeDAO) {
		this.registrantDAO = registrantDAO;
		this.stateDAO = stateDAO;
		this.propertyDAO = propertyDAO;
		this.registrationTypeDAO = registrationTypeDAO;
	}

	@Override
	public String execute() throws Exception {
		if (rid == 0) {
			Registrant r=(Registrant) sessionData.get("workingregistrant");
			if (r==null) {
				return "exit";
			}
			rid = r.rid;
			// check here, return 'exit' if not found.
		}
		cid = ((Convention) sessionData.get("conference")).conCID;
		logger.debug("LoadUser called - loading #" + rid + " for conference " + cid);
		stateDAO.updateWithEvent(cid, rid);
		r = registrantDAO.getByID(rid);
		registrantDAO.updateCurrentState(r, cid);
		State s = r.currentState;
		logger.debug("Regtype is " + r.currentState.regtype);
		if (r.currentState.registered) {
			RegistrationType rt = registrationTypeDAO.getRegistrantType(cid, r.getCurrentstate().regtype);
			r.currentState.printAs = rt.getRegPrint();
			r.currentState.banner = rt.getRegBanner();
			logger.debug("Current state record has printAs as " + r.currentState.printAs);
		}
		sessionData.put("workingregistrant", r);

		// This section populates some local variables required by the
		// ShowRegistrant
		// screen.
		State currentState = stateDAO.getState(cid, rid);
		if (currentState.subscribed) {
			subscribedText = "Yes (<a href=\"unsubscribeRegistrant.action\">Unsub</a>)";
		}
		if (currentState.registered) {
			registeredText = "Yes (<a href=\"VoidRegistrantForm\" accesskey=\"v\">[<u>V</u>]oid</a> | <a href=\"TransferRegistrantForm\" accesskey=\"F\">Trans[<u>f</u>]er</a>)";
		} else {
			checkedinText = "";
			badgedText = "";
		}
		if (currentState.badged) {
			badgedText = "Yes (<a href=\"PrintBadgeForm\">Again</a>)";
		}
		if (currentState.checkedin) {
			checkedinText = "Yes (<a href=\"uncheckinRegistrant.action\">Uncheckin</a>)";
		}

		// We're going to need a list of the current properties. Load 'em.
		propertyList = propertyDAO.listUpdatedProperties(cid, rid);

		return SUCCESS;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.sessionData = session;
	}
}
