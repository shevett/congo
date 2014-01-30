package com.stonekeep.congo.coconut;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.Action;
import com.stonekeep.congo.dao.HistoryDAO;
import com.stonekeep.congo.data.History;
import com.stonekeep.congo.data.Registrant;

/**
 * Load up the history of a registrant for display.
 * 
 * @author dbs
 */
public class ShowHistory implements Action, SessionAware {

	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(ShowHistory.class);
	public Registrant r;
	public List<History> history;
	public int rowcount;
	private Map<String, Object> sessionData;
	private final HistoryDAO historyDAO;

	public ShowHistory(HistoryDAO historyDAO) {
		this.historyDAO = historyDAO;
	}

	@Override
	public String execute() throws Exception {
		logger.debug("ShowHistory called");
		r = (Registrant) sessionData.get("workingregistrant");
		history = historyDAO.list(r.rid);
		rowcount = history.size();
		return SUCCESS;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.sessionData = session;
	}
}
