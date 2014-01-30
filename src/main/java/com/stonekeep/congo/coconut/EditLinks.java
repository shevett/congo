package com.stonekeep.congo.coconut;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.BeanMap;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
import com.stonekeep.congo.dao.LinkDAO;
import com.stonekeep.congo.dao.RegistrantDAO;
import com.stonekeep.congo.dao.StateDAO;
import com.stonekeep.congo.data.Convention;
import com.stonekeep.congo.data.Link;
import com.stonekeep.congo.data.Registrant;
import com.stonekeep.congo.data.State;

public class EditLinks extends ActionSupport implements SessionAware {

	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(EditLinks.class);
	public String linkbutton = null;
	public String exitbutton = null;
	public String linksearch;
	public String resultlimit ;
	public String message;
	private Map<String, Object> sessionData;
	public List<Link> linkList ;
	public List<Registrant> searchResults;
 	public int noteid;
 	public int id;

	private final LinkDAO linkDAO;
	private final RegistrantDAO registrantDAO;
	private final StateDAO stateDAO;

	public EditLinks(LinkDAO linkDAO, RegistrantDAO registrantDAO, StateDAO stateDAO) {
		this.linkDAO = linkDAO;
		this.registrantDAO = registrantDAO;
		this.stateDAO = stateDAO;
	}
	
	public String execute() throws Exception {
		logger.debug("Loading the list of links for this registrant.");
		int rid = ((Registrant)(sessionData.get("workingregistrant"))).rid;
		// friendList = linkDAO.findAllById(rid);
		linkList = linkDAO.listBrowse(rid);
		populateLinks(linkList,rid);
		logger.debug("Size of linkList : " +linkList.size());
		if (linkbutton != null) {
			logger.debug("linkbutton is " + linkbutton + " searching for " + linksearch);
			// guessing it's search right now
			int cid = ((Convention) sessionData.get("conference")).conCID;
			Object[] results = registrantDAO.searchByAny("%" + linksearch.trim() + "%", 1000, Integer.parseInt(resultlimit));
			searchResults = (List<Registrant>) results[1];
			if (searchResults != null ) {
				logger.debug("Returning " + searchResults.size() + " rows...");
			}
		} 
		return SUCCESS;
	}
	
	public void populateLinks(List<Link> flist,int whatrid) throws SQLException {
		logger.debug("populating flist, checking against " + whatrid);
		Registrant r=null;
		for (Link f : flist) {
			if (f.getLinkRid1() == whatrid) 
				r = registrantDAO.getByID(f.getLinkRid2());
			else 
				r = registrantDAO.getByID(f.getLinkRid1());
			f.setLinkName(r.getLastName() + ", " + r.getFirstName() + " (" + r.getBadgeName() + ")");
			logger.debug("Rid1 is " + f.getLinkRid1() + ", rid2 is " + f.getLinkRid2() + ", linkname is " + f.getLinkName());
			int cid = ((Convention) sessionData.get("conference")).conCID;
			State s = stateDAO.getState(cid,r.rid);
			if (s != null) {
				f.setLinkRegistered(s.registered);
			}
		}
	}
	
	public String addLink() throws Exception {
		Registrant r = ((Registrant)sessionData.get("workingregistrant"));
		logger.debug("Adding link " + id + " to registrant " + r.rid);
		Link f = new Link();
		f.setLinkRid1(r.rid);
		f.setLinkRid2(id);
	    java.util.Date today = new java.util.Date();
	    f.setLinkDate(new Timestamp(today.getTime()));
	    f.setLinkStatus("Ok");
		linkDAO.add(f);
		return SUCCESS;
	}
	
	public String unLink() throws Exception {
		logger.debug("Removing friend " + id );
		Link f = new Link();
		f.setId(id);
		logger.debug("Fetching friend id " + id);
		f = linkDAO.getById(id);
		logger.debug("Calling remove...");
		linkDAO.remove(f);
		return SUCCESS;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		logger.debug("Receiving session data...");
		this.sessionData = session;
	}
}
