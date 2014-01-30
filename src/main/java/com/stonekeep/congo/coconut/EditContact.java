package com.stonekeep.congo.coconut;

import java.sql.SQLException;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.Preparable;
import com.stonekeep.congo.dao.ContactDAO;
import com.stonekeep.congo.data.Contact;
import com.stonekeep.congo.data.Registrant;

public class EditContact<T extends Contact> implements Preparable,SessionAware {
	public String message ;

    public EditContact(ContactDAO<T> contactDAO) {
        this.contactDAO = contactDAO;
    }

    public String add() {
        contact.setRid(rid);
        return Action.SUCCESS;
    }

    public String create() throws SQLException {
        if (navButton != null)
            return navButton;

        return doCreate();
    }

    private String doCreate() throws SQLException {
    	try {
    		contactDAO.create(contact);
    	}
    	catch (Exception e) {
    		message = e.getMessage();
    		return Action.INPUT;
    	}
        return Action.SUCCESS;
    }

    public String edit() throws SQLException {
        contact = contactDAO.lookup(rid, location);
        return Action.SUCCESS;
    }

    public T getContact() {
        return contact;
    }

    @Override
    public void prepare() {
        contact = contactDAO.createBlankContact();
    }

    public String save() throws SQLException {
        if (navButton != null)
            return navButton;

        if (deleteButton != null)
            return doDelete();

        return doSave();
    }

    public void setDeleteButton(String deleteButton) {
        this.deleteButton = deleteButton;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setNavButton(String navButton) {
        this.navButton = navButton;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    private String doDelete() throws SQLException {
        contactDAO.delete(contact);
        return Action.SUCCESS;
    }

    private String doSave() throws SQLException {
    	Registrant r = (Registrant)sessionData.get("coconutuser");
    	try {
    		contactDAO.save(contact,r.rid);
    	}
    	catch (Exception e) {
    		message = e.getMessage();
    		return Action.INPUT;
    	}
        return Action.SUCCESS;
    }

    private T contact;
    private final ContactDAO<T> contactDAO;
    private String deleteButton;
    private String location;
    private String navButton;
    private int rid;
    private Map<String, Object> sessionData;

    
    @Override
	public void setSession(Map<String, Object> sessionData) {
		this.sessionData = sessionData;
	}
}
