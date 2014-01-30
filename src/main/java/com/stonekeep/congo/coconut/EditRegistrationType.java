package com.stonekeep.congo.coconut;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Action;
import com.stonekeep.congo.dao.DiscountCodeDAO;
import com.stonekeep.congo.dao.RegistrationTypeDAO;
import com.stonekeep.congo.data.Convention;
import com.stonekeep.congo.data.RegistrationType;

public class EditRegistrationType extends Editor {

	private Logger logger = Logger.getLogger(EditRegistrationType.class);
	public String id;
//	public Map<String, RegistrationType> regTypes;
	public LinkedHashMap<String,RegistrationType> regTypes;
	public String whichRegtype;
	public String order;
	public RegistrationType workingRegistrationType;
	private int whatcid;
	private final RegistrationTypeDAO registrationTypeDAO;
	private final DiscountCodeDAO discountCodeDAO;
	
	public EditRegistrationType(RegistrationTypeDAO registrationTypeDAO,
			DiscountCodeDAO discountCodeDAO) {
		this.registrationTypeDAO = registrationTypeDAO;
		this.discountCodeDAO = discountCodeDAO;
	}

	public void prepare() throws Exception {
		logger.debug("prepare: Prepare starting...");
		if (regTypes == null) {
			logger.debug("prepare: Initializing rtdao and getting regtypes, order is " + order);
			whatcid = ((Convention) sessionData.get("conference")).conCID;
//			regTypes = new TreeMap(registrationTypeDAO.list(whatcid));
			regTypes = registrationTypeDAO.list(whatcid);
			logger.debug("prepare: regtypes is " + regTypes);
		}
		logger.debug("prepare: wrt is : " + workingRegistrationType);
		logger.debug("prepare: id is " + id);
		if (id != null) {
			if (id.length() > 1) {
				// asked to load up the working reg...
				logger
						.debug("prepare: Fetching registrationtype with id "
								+ id);
				workingRegistrationType = registrationTypeDAO
						.getRegistrantType(whatcid, id);
			} else {
				logger.debug("prepare: Must be a create, instantiating new.");
				workingRegistrationType = new RegistrationType();
				workingRegistrationType.setRegCID(whatcid);
				workingRegistrationType.setRegName(whichRegtype);
				logger.debug("Setting registration sequence to 1");
				workingRegistrationType.setRegSequence(1);
				workingRegistrationType.setRegActive(true);
				logger.debug("prepare: wrt is " + workingRegistrationType);
				id = workingRegistrationType.getRegName();
			}
		} else {
			logger.debug("prepare: yep, id is null");
		}
	}

	public String view() throws Exception {
		logger.debug("View: typebutton is -->" + typebutton + "<--");
		logger.debug("View: exitbutton is " + exitbutton);
		if (typebutton != null) {
			if (typebutton.equals("Import from Another Event")) {
				logger.debug("Returning 'import'");
				return "import";
			} else {
				return Action.SUCCESS;
			}
		}  
		if (exitbutton != null) {
			return "exit";
		}
		// load up the data for the edit form...
		logger.debug("view: Loading registrant " + whichRegtype);
		workingRegistrationType = registrationTypeDAO.getRegistrantType(
				whatcid, whichRegtype);
		id = workingRegistrationType.getRegName();
		logger.debug("view; after fetch - id is " + id + ", description is " + workingRegistrationType.getRegDesc());
		return Action.SUCCESS;
	}
	
	public String delete() throws Exception {
		logger.debug("Deleting registration type " + whichRegtype + " from event " + whatcid);
		registrationTypeDAO.delete(whatcid,whichRegtype);
		return SUCCESS;
	}
	
	public String setOrder() throws Exception {
		logger.debug("Setting order to " + order);
		return SUCCESS;
	}

	public String update() throws Exception {
		logger.debug("updating...");
		logger.debug("update: id is " + id);
		logger.debug("update: Button is " + typebutton);
		logger.debug("update: registrationtype is " + workingRegistrationType);
		if (typebutton != null) {
			// Here we need to validate some stuff.
			if (workingRegistrationType == null) {
				logger.debug("Null WRT.  Returning INPUT");
				message="An error occurred creating or saving this registrationtype.";
				return INPUT;
			}
			if (workingRegistrationType.getRegSequence() == null || workingRegistrationType.getRegSequence() < 1) {
				logger.debug("Sequence < 0 .  Returning INPUT");
				message="Sequence number determines what order registration types appear.  It should be greater than 0";
				return INPUT;
			}
			if (workingRegistrationType.getRegName().length() < 1) {
				logger.debug("No regname specified .  Returning INPUT");
				message="Please specify a name for this registration type";
				return INPUT;
			}
			if (workingRegistrationType.getRegDiscountCode().length() > 1) {
				String rdc = workingRegistrationType.getRegDiscountCode();
				logger.debug("Validating discount code " + rdc);
				if (discountCodeDAO.get(whatcid, rdc) == null) {
					message="No such discount code '" + rdc + "'.  You must specify an existing discount code.";
					return INPUT;
				}
			}
			if (workingRegistrationType.getRegCost() == null) {
				logger.debug("No cost entered...");
				message="Registration cost cannot be empty.  If there's no cost, enter '0'";
				return INPUT;
			}
			logger.debug("updateagain: Button is " + typebutton);
			if (id.length() < 1) {
				logger.debug("update: create, creating...");
				registrationTypeDAO.create(workingRegistrationType);
			} else {
				logger.debug("update: saving, this is an update...");
				registrationTypeDAO.save(workingRegistrationType);
			}
		}
		logger.debug("Exiting update()");
		return Action.SUCCESS;
	}

}
