package com.stonekeep.congo.interceptors;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.stonekeep.congo.dao.LinkDAO;
import com.stonekeep.congo.dao.RegistrantDAO;
import com.stonekeep.congo.dao.StateDAO;
import com.stonekeep.congo.data.Convention;
import com.stonekeep.congo.data.Link;
import com.stonekeep.congo.data.Registrant;
import com.stonekeep.congo.data.Setting;
import com.stonekeep.congo.data.State;

public class PublicLoginChecker extends AbstractInterceptor {
	
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(PublicLoginChecker.class);

	private Map<String,Object> sessionData;
	
	private int cid;
	private final RegistrantDAO registrantDAO;	//Injected!
	private final StateDAO stateDAO;			//Injected!
	private final LinkDAO linkDAO;			//Injected!

	
	public List<Link> friendsList = new ArrayList<Link>();
	
	public void setCid(int cid) { this.cid = cid; }

	public PublicLoginChecker(RegistrantDAO registrantDAO,StateDAO stateDAO, LinkDAO linkDAO) {
		this.registrantDAO = registrantDAO;
		this.stateDAO = stateDAO;
		this.linkDAO = linkDAO;
		logger.debug("PublicLoginChecker instantiated.");
	}
	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		logger.debug("Intercept intercepting...");
		sessionData = (Map)invocation.getInvocationContext().getContextMap().get("session");
		logger.debug("Session map contains " + sessionData.size() + " elements.");
		Map<String,Setting>settings = (Map<String, Setting>) sessionData.get("settings");
		if (settings != null) {
			logger.debug("Within session, settings has " + settings.size() + " elements.");
			for (Setting s : settings.values()) {
				logger.debug("--- Setting " + s.getSettingName() + " : " + s.getSettingValue());
			}
		} else {
			logger.debug("No settings available in session.");
		}
		

		String aClass = invocation.getInvocationContext().getActionInvocation().getAction().getClass().toString();

		if ((sessionData.get("webuser") != null) || (aClass.indexOf("Logon") > -1)) {
			
			cid = ((Convention) sessionData.get("currentConvention")).conCID;

			Registrant r = (Registrant)sessionData.get("webuser");
			logger.info(r.rid + ":" + r.lastName + ", "+ r.firstName);
			r.currentState = stateDAO.getState(cid, r.rid);					// Make sure we have current info.
			
			List<Link> fl = linkDAO.listOk(r.rid);
			logger.debug("Loading active links with " + fl.size() + " members.");
			populateLinks(fl,r.rid);
			sessionData.put("linksList",fl);
			
			// This loads up the pending list
			List<Link> pending = linkDAO.listPending(r.rid);
			logger.debug("Loading pending links list with " + pending.size() + " members.");
			populateLinks(pending,r.rid);
			sessionData.put("linksPending",pending);
			
			return invocation.invoke();
		} else {
			logger.info("No webuser in session, returning 'notloggedin' to the interceptor.");
			return "notloggedin";
		}
	}
	
	public void populateLinks(List<Link> flist,int myrid) throws SQLException {
		Registrant r=null;
		for (Link f : flist) {
			f.setLinkMyRid(myrid);
			if (f.getLinkRid1() != myrid) {
				r = registrantDAO.getByID(f.getLinkRid1());
			} else {
				r = registrantDAO.getByID(f.getLinkRid2());
			}
			f.setLinkOtherRid(r.rid);
			f.setLinkName(r.getLastName() + ", " + r.getFirstName() + " (" + r.getBadgeName() + ")");
			
			Map<String,Setting> settings = (Map<String, Setting>) sessionData.get("settings");
			cid = Integer.parseInt(settings.get("event_default").getSettingValue());
			logger.debug("Via event_default in settings, current cid is " + cid);
			State s = stateDAO.getState(cid,r.rid);
			if (s != null) {
				f.setLinkRegistered(s.registered);
			}
		}
	}

	@Override
	public void init() {
		logger.trace("Initted.");
	}
	
	@Override
	public void destroy() {
		logger.trace("Destroyed.");
	}
}
