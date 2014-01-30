package com.stonekeep.congo.coconut;

import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Action;
import com.stonekeep.congo.dao.LayoutDAO;
import com.stonekeep.congo.data.Layout;

public class EditLayout extends Editor {

	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(EditLayout.class);
	public int id;
	public List<Layout> layouts;
	public Layout workinglayout;
	private int whatcid;
	private final LayoutDAO layoutDAO;
	public String message;
	
	public EditLayout(LayoutDAO layoutDAO) {
		this.layoutDAO = layoutDAO;
	}

	public void prepare() throws Exception {
		logger.debug("prepare: Prepare starting");
		if (layouts == null) {
			logger.debug("prepare: Initializing layoutDAO and getting layouts");
			layouts = layoutDAO.listAll();
			logger.debug("prepare: layouts list is " + layouts.size() + " elements long.");
			logger.debug(layouts);
			for (Layout v:layouts) {
				logger.debug("layout is " + v);
			}
		}
		if (id > 0) {
			workinglayout = layoutDAO.getById(id);
		}
		logger.debug("prepare: workinglayout is : " + workinglayout);
		logger.debug("prepare: id is " + id);
		logger.debug("Exiting prepare - workinglayout is " + workinglayout);
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
		logger.debug("view: Loading layout " + id);
		workinglayout = layoutDAO.getById(id);
		logger.debug("workinglayout is " + workinglayout);
		return Action.SUCCESS;
	}

	public String delete() throws Exception {
		logger.debug("delete...");
		logger.debug("I'll be deleting " + id);
		layoutDAO.remove(id);
		return Action.SUCCESS;
	}
	
	public String create() throws Exception {
		logger.debug("Creating new layout...");
		Layout v = new Layout();
		v.setLayoutName("New layout");
		layoutDAO.add(v);
		return Action.SUCCESS;
	}

	public String update() throws Exception {
		logger.debug("updating...");
		logger.debug("update: id is " + id);
		logger.debug("update: Button is " + typebutton);
		logger.debug("update: workinglayout is " + workinglayout);
		if (typebutton != null) {
			// Here we need to validate some stuff.
			if (workinglayout. getLayoutName().length() < 1) {
				logger.debug("No layout name specified .  Returning INPUT");
				message="Please specify a name for this layout";
				return INPUT;
			}
			logger.debug("update: saving, this is an update...");
			logger.debug("Layout id is " + workinglayout.getId());
			logger.debug("layout name is " + workinglayout.getLayoutName());
			layoutDAO.update(workinglayout);
		}
		logger.debug("Exiting update()");
		return Action.SUCCESS;
	}
	

}
