package com.stonekeep.congo.data;

import java.util.Map;

public class Registrant {
	public int rid;
	public String firstName;
	public String lastName;
	public String company ;
	public String badgeName ;
	public String password;
	public boolean enabled;
	public int mergedTo;
	public String comment = "";
	public Map<Integer,State> conList;
	public Map<String,Phone> phoneList;
	public Map<String,Address> addressList;
	public Map<String,Email> emailList;
	public Map<Integer,Note> noteList;
	public State currentState ;
	public int totalNotes ;

	public int getRid() { return this.rid; }
	public String getFirstName() { return this.firstName; }
	public String getLastName() { return this.lastName; }
	public String getCompany() { return this.company; }
	public String getBadgeName() { return this.badgeName; }
	public String getcomment() { return this.comment; }
	public boolean getEnabled() { return this.enabled; }
	public int getMergedTo() { return this.mergedTo; }
	public Map<Integer,State> getConList() { return this.conList; }
	public Map<String,Phone> getPhoneList() { return this.phoneList; }
	public Map<String,Address> getAddressList() { return this.addressList; }
	public Map<String,Email> getEmailList() { return this.emailList; }
	public Map<Integer,Note> getNoteList() { return this.noteList; }
	public State getCurrentstate() { return this.currentState; }
	public int getTotalNotes() { return this.totalNotes; }
	
	public String getPrimaryEmail() { 
		Email fallback = null;
		for (Email e : this.emailList.values()) {
			if (e.primary) {
				return e.address;
			} else { fallback = e; }
		}
		return (fallback != null) ? fallback.address : null;
	}
	
	public String getPrimaryPhone() {
		Phone fallback = null;
		for (Phone p : this.phoneList.values()) {
			if (p.primary) {
				return p.phone;
			} else { fallback = p; }
		}
		return (fallback != null) ? fallback.phone : null;
	}
	
	public Address getPrimaryAddress() {
		Address fallback = null;
		for (Address a : this.addressList.values()) {
			if (a.primary) {
				return a;
			} else { fallback = a; }
		}
		return fallback;
	}
}

