package com.stonekeep.congo.data;

/**
 * An object representing the state of a registrant for a specific event.
 * @author dbelfer-shevett
 */
public class State {
	public int rid;
	public int cid;
	public String eventname;	// joined from con_detail
	public boolean subscribed;
	public boolean registered;
	public boolean checkedin;
	public boolean badged;
	public String regtype;
	public String printAs;
	public String banner;
	
	public int getRid() { return this.rid; }
	public int getCid() { return this.cid; }
	public String getEventname() { return this.eventname; }
	public boolean getSubscribed() { return this.subscribed; }
	public boolean getRegistered() { return this.registered; }
	public boolean getCheckedin() { return this.checkedin; }
	public boolean getBadged() { return this.badged; }

	public String getSubscribedText() { return (this.subscribed ? "Yes" : "No"); }
	public String getRegisteredText() { return (this.registered ? "Yes" : "No"); }
	public String getCheckedinText() { return (this.checkedin ? "Yes" : "No"); }
	public String getBadgedText() { return (this.badged ? "Yes" : "No"); }
	
	public String getRegtype() { return this.regtype; }

}
