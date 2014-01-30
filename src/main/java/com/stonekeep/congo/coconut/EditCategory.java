package com.stonekeep.congo.coconut;

import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Action;
import com.stonekeep.congo.dao.CategoryDAO;
import com.stonekeep.congo.data.Category;

public class EditCategory extends Editor {

	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(EditCategory.class);
	public int id;
	public List<Category> categories;
	public Category workingcategory;
	private int whatcid;
	private final CategoryDAO categoryDAO;
	public String message;
	
	public EditCategory(CategoryDAO categoryDAO) {
		this.categoryDAO = categoryDAO;
	}

	public void prepare() throws Exception {
		logger.debug("prepare: Prepare starting");
		if (categories == null) {
			logger.debug("prepare: Initializing categoryDAO and getting categories");
			categories = categoryDAO.listAll();
			logger.debug("prepare: categories list is " + categories.size() + " elements long.");
			logger.debug(categories);
			for (Category v:categories) {
				logger.debug("category is " + v);
			}
		}
		if (id > 0) {
			workingcategory = categoryDAO.getById(id);
		}
		logger.debug("prepare: workingcategory is : " + workingcategory);
		logger.debug("prepare: id is " + id);
		logger.debug("Exiting prepare - workingcategory is " + workingcategory);
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
		logger.debug("view: Loading category " + id);
		workingcategory = categoryDAO.getById(id);
		logger.debug("workingcategory is " + workingcategory);
		return Action.SUCCESS;
	}

	public String delete() throws Exception {
		logger.debug("delete...");
		logger.debug("I'll be deleting " + id);
		categoryDAO.remove(id);
		return Action.SUCCESS;
	}
	
	public String create() throws Exception {
		logger.debug("Creating new category...");
		Category v = new Category();
		v.setCategoryName("New category");
		categoryDAO.add(v);
		return Action.SUCCESS;
	}

	public String update() throws Exception {
		logger.debug("updating...");
		logger.debug("update: id is " + id);
		logger.debug("update: Button is " + typebutton);
		logger.debug("update: workingcategory is " + workingcategory);
		if (typebutton != null) {
			// Here we need to validate some stuff.
			if (workingcategory.getCategoryName().length() < 1) {
				logger.debug("No category name specified .  Returning INPUT");
				message="Please specify a name for this category";
				return INPUT;
			}
			logger.debug("update: saving, this is an update...");
			logger.debug("Category id is " + workingcategory.getId());
			logger.debug("category name is " + workingcategory.getCategoryName());
			categoryDAO.update(workingcategory);
		}
		logger.debug("Exiting update()");
		return Action.SUCCESS;
	}
	

}
