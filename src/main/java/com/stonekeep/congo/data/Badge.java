package com.stonekeep.congo.data;

import java.util.ArrayList;
import java.util.Date;

public class Badge {
    public void setCid(int cid) {
        this.cid = cid;
    }
    
    public void setBadgeIdentifier(String newid) {
    	this.badgeIdentifier = newid;
    }
    
    public void setLastUpdated(Date newdate) {
    	this.lastUpdated = newdate;
    }
    
    public void setBadgeRows(ArrayList<BadgeRow> rows) {
    	this.badgeRows = rows;
    }

    public int cid;
    private String badgeIdentifier;
	private Date lastUpdated;
	private ArrayList<BadgeRow> badgeRows;

	public int getCid() { 
		return this.cid;
	}
	
	public String getBadgeIdentifier() {
		return this.badgeIdentifier;
	}
	
	public Date getLastUpdated() {
		return this.lastUpdated;
	}
	
	public ArrayList<BadgeRow> getBadgeRows() {
		return this.badgeRows;
	}
}
