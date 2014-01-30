package com.stonekeep.congo.coconut;

import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Action;
import com.stonekeep.congo.dao.DiscountCodeDAO;
import com.stonekeep.congo.data.Convention;
import com.stonekeep.congo.data.DiscountCode;

public class EditDiscountCodes extends Editor {

	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(EditDiscountCodes.class);
	public String id;
	public Map<String, DiscountCode> discountcodes;
	public String whichDiscountCode;
	public DiscountCode workingDiscountCode;
	private int whatcid;
	private final DiscountCodeDAO discountcodeDAO;
	public String message;
	
	public EditDiscountCodes(DiscountCodeDAO DiscountCodeDAO) {
		this.discountcodeDAO = DiscountCodeDAO;
	}

	public void prepare() throws Exception {
		logger.debug("prepare: Prepare starting");
		if (discountcodes == null) {
			logger.debug("prepare: Initializing discountcodedao and getting discountcodes");
			whatcid = ((Convention) sessionData.get("conference")).conCID;
			discountcodes = new TreeMap(discountcodeDAO.list(whatcid));
			logger.debug("prepare: discountcodes is " + discountcodes);
		}
		logger.debug("prepare: workingDiscountCode is : " + workingDiscountCode);
		logger.debug("prepare: id is " + id);
		if (id != null) {
			if (id.length() > 1) {
				// asked to load up the working discountcode...
				logger.debug("prepare: Fetching DiscountCode with id "	+ id);
				workingDiscountCode = discountcodeDAO.get(whatcid, id);
				if (workingDiscountCode == null) { 
					workingDiscountCode = discountcodeDAO.get(0,id);
				}
			} else {
				logger.debug("prepare: Must be a create, instantiating new.");
				workingDiscountCode = new DiscountCode();
				workingDiscountCode.setCID(whatcid);
				workingDiscountCode.setName(whichDiscountCode);
			}
		} else {
			logger.debug("prepare: yep, id is null");
		}
		logger.debug("Exiting prepare - workingDiscountCode is " + workingDiscountCode);
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
		logger.debug("view: Loading discountcode " + whichDiscountCode);
		workingDiscountCode = discountcodeDAO.get(whatcid, whichDiscountCode);
		if (workingDiscountCode == null) { 
			workingDiscountCode = discountcodeDAO.get(0,whichDiscountCode);
		}
		id = workingDiscountCode.getName();
		return Action.SUCCESS;
	}
	
	public String delete() throws Exception {
		logger.debug("deleteing " + whichDiscountCode + "...");
		try {
			discountcodeDAO.delete(whichDiscountCode);
		}
		catch (SQLException e) {
			message="Cannot delete - a registration type is using this discount code.";
			return INPUT;
		}
		return SUCCESS;
	}

	public String update() throws Exception {
		logger.debug("updating...");
		logger.debug("update: id is " + id);
		logger.debug("update: Button is " + typebutton);
		logger.debug("update: workingDiscountCode is " + workingDiscountCode);
		if (typebutton != null) {
			// Here we need to validate some stuff.
			if (workingDiscountCode == null) {
				logger.debug("Null workingDiscountCode.  Returning INPUT");
				message="An error occurred creating or saving this DiscountCode.";
				return INPUT;
			}
			if (workingDiscountCode.getName().length() < 1) {
				logger.debug("No regname specified .  Returning INPUT");
				message="Please specify a name for this registration type";
				return INPUT;
			}
			logger.debug("updateagain: Button is " + typebutton);
			if (id.length() < 1) {
				logger.debug("update: create, creating...");
				discountcodeDAO.create(workingDiscountCode);
			} else {
				logger.debug("update: saving, this is an update...");
				discountcodeDAO.save(workingDiscountCode);
			}
		}
		logger.debug("Exiting update()");
		return Action.SUCCESS;
	}
	

}
