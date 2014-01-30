package com.stonekeep.congo.coconut;

import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Action;
import com.stonekeep.congo.dao.VenueDAO;
import com.stonekeep.congo.data.Venue;

public class EditVenue extends Editor {

	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(EditVenue.class);
	public int id;
	public List<Venue> venues;
	public Venue workingvenue;
	private int whatcid;
	private final VenueDAO venueDAO;
	public String message;
	
	public EditVenue(VenueDAO venueDAO) {
		this.venueDAO = venueDAO;
	}

	public void prepare() throws Exception {
		logger.debug("prepare: Prepare starting");
		if (venues == null) {
			logger.debug("prepare: Initializing venueDAO and getting venues");
			venues = venueDAO.listAll();
			logger.debug("prepare: venues list is " + venues.size() + " elements long.");
			logger.debug(venues);
			for (Venue v:venues) {
				logger.debug("Venue is " + v);
			}
		}
		if (id > 0) {
			workingvenue = venueDAO.getById(id);
		}
		logger.debug("prepare: workingvenue is : " + workingvenue);
		logger.debug("prepare: id is " + id);
		logger.debug("Exiting prepare - workingvenue is " + workingvenue);
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
		logger.debug("view: Loading venue " + id);
		workingvenue = venueDAO.getById(id);
		logger.debug("workingvenue is " + workingvenue);
		return Action.SUCCESS;
	}

	public String delete() throws Exception {
		logger.debug("delete...");
		logger.debug("I'll be deleting " + id);
		venueDAO.remove(id);
		return Action.SUCCESS;
	}
	
	public String create() throws Exception {
		logger.debug("Creating new venue...");
		Venue v = new Venue();
		v.setVenueName("New Venue");
		venueDAO.add(v);
		return Action.SUCCESS;
	}

	public String update() throws Exception {
		logger.debug("updating...");
		logger.debug("update: id is " + id);
		logger.debug("update: Button is " + typebutton);
		logger.debug("update: workingvenue is " + workingvenue);
		if (typebutton != null) {
			// Here we need to validate some stuff.
			if (workingvenue.getVenueName().length() < 1) {
				logger.debug("No venue name specified .  Returning INPUT");
				message="Please specify a name for this venue";
				return INPUT;
			}
			logger.debug("updateagain: Button is " + typebutton);
			logger.debug("update: saving, this is an update...");
			venueDAO.update(workingvenue);
		}
		logger.debug("Exiting update()");
		return Action.SUCCESS;
	}
	

}
