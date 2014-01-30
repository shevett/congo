package com.stonekeep.congo.coconut;

import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Action;
import com.stonekeep.congo.dao.DepartmentDAO;
import com.stonekeep.congo.data.Department;

public class EditDepartment extends Editor {

	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(EditDepartment.class);
	public int id;
	public List<Department> departments;
	public Department workingdepartment;
	private int whatcid;
	private final DepartmentDAO departmentDAO;
	public String message;
	
	public EditDepartment(DepartmentDAO departmentDAO) {
		this.departmentDAO = departmentDAO;
	}

	public void prepare() throws Exception {
		logger.debug("prepare: Prepare starting");
		if (departments == null) {
			logger.debug("prepare: Initializing departmentDAO and getting departments");
			departments = departmentDAO.listAll();
			logger.debug("prepare: departments list is " + departments.size() + " elements long.");
			logger.debug(departments);
			for (Department v:departments) {
				logger.debug("room is " + v);
			}
		}
		if (id > 0) {
			workingdepartment = departmentDAO.getById(id);
		}
		logger.debug("prepare: workingdepartment is : " + workingdepartment);
		logger.debug("prepare: id is " + id);
		logger.debug("Exiting prepare - workingdepartment is " + workingdepartment);
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
		logger.debug("view: Loading room " + id);
		workingdepartment = departmentDAO.getById(id);
		logger.debug("workingdepartment is " + workingdepartment);
		return Action.SUCCESS;
	}

	public String delete() throws Exception {
		logger.debug("delete...");
		logger.debug("I'll be deleting " + id);
		departmentDAO.remove(id);
		return Action.SUCCESS;
	}
	
	public String create() throws Exception {
		logger.debug("Creating new room...");
		Department v = new Department();
		v.setDepartmentName("New department");
		departmentDAO.add(v);
		return Action.SUCCESS;
	}

	public String update() throws Exception {
		logger.debug("updating...");
		logger.debug("update: id is " + id);
		logger.debug("update: Button is " + typebutton);
		logger.debug("update: workingdepartment is " + workingdepartment);
		if (typebutton != null) {
			// Here we need to validate some stuff.
			if (workingdepartment.getDepartmentName().length() < 1) {
				logger.debug("No department name specified .  Returning INPUT");
				message="Please specify a name for this department";
				return INPUT;
			}
			logger.debug("update: saving, this is an update...");
			logger.debug("Department id is " + workingdepartment.getId());
			logger.debug("room name is " + workingdepartment.getDepartmentName());
			departmentDAO.update(workingdepartment);
		}
		logger.debug("Exiting update()");
		return Action.SUCCESS;
	}
	

}
