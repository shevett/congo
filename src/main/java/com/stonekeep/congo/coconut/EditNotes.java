package com.stonekeep.congo.coconut;

import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.stonekeep.congo.dao.NoteDAO;
import com.stonekeep.congo.dao.RegistrantDAO;
import com.stonekeep.congo.data.Convention;
import com.stonekeep.congo.data.Note;
import com.stonekeep.congo.data.Registrant;

public class EditNotes implements SessionAware {
	private Logger logger = Logger.getLogger(EditNotes.class);
	public String notebutton = null;
	public String exitbutton = null;
	public boolean loginnote;
	public String note;
	public String message;
	private Map<String, Object> sessionData;
	public Map<Integer,Note> noteList ;
 	public int noteid;

	private final NoteDAO noteDAO;
	private final RegistrantDAO registrantDAO;

	public EditNotes(NoteDAO noteDAO,RegistrantDAO registrantDAO) {
		this.noteDAO = noteDAO;
		this.registrantDAO = registrantDAO;
	}

	public String addNote() throws Exception {
		logger.debug("AddNote called");

		if (notebutton != null) {
			Note n = new Note();
			n.rid = ((Registrant) sessionData.get("workingregistrant")).rid;
			n.cid = ((Convention) sessionData.get("conference")).conCID;
			n.message = note;
			n.postRid = ((Registrant) sessionData.get("coconutuser")).rid;
			// The note type will depend on if the checkbox was checked.
			if (loginnote) {
				n.type = "NOTICE";
			} else {
				n.type = "NORMAL";
			}
			noteDAO.create(n);
			
			// Refreshing the active registrant so display counters are okay
			
			Registrant r = (Registrant) sessionData.get("workingregistrant");
			r = registrantDAO.getByID(r.rid);
			sessionData.put("workingregistrant", r);
			return "success";
			
		} else {
			logger.debug("probably just hit exit");
			return "success";
		}
	}

	public String ackNote() throws Exception {
		logger.debug("Acknowledging note " + noteid);
		Note n = noteDAO.getNote(((Registrant) sessionData
				.get("workingregistrant")).rid, noteid);
		n.acknowledgeRid = ((Registrant) sessionData.get("coconutuser")).rid;
		n.acknowledgeDate = new java.sql.Timestamp(System.currentTimeMillis());
		noteDAO.save(n);
		return "success";
	}
	
	/* 
	 * This method needs to reload the registrant into state because the counter
	 * is needed on the sidebar and gets refreshed immediately.  So, after deleting the
	 * note, refresh the reg.
	 */
	public String deleteNote() throws Exception {
		logger.debug("Deleting note " + noteid);
		Registrant r = (Registrant) sessionData.get("workingregistrant");
		noteDAO.delete(r.rid, noteid);
		r = registrantDAO.getByID(r.rid);
		sessionData.put("workingregistrant", r);

		return "success";
	}
	
	public String showNotes() throws Exception {
		logger.debug("Loading the list of notes for this registrant.");
		int rid = ((Registrant)(sessionData.get("workingregistrant"))).rid;
		noteList = noteDAO.list(rid);
		logger.debug("Size of noteList : " +noteList.size());
		return "success";
	}

	@Override
	public void setSession(Map<String, Object> session) {
		logger.debug("Receiving session data...");
		this.sessionData = session;
	}
}
