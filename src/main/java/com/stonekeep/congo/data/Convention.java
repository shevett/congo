package com.stonekeep.congo.data;

import java.util.Date;

public class Convention  {
	public Integer conCID ;
	public String conName;
	public String conLocation;
	public Date conStart ;
	public Date conEnd;
	public String conComment;
	public String conWebsite;
	public String conEmail;
	public String conStylesheet;
	public String conBadgelayout;
	public String conDescription;
	public Integer conCap;
	
	// Calculated fields
	public int numSubscribed;
	public int numRegistered;
	public int numBadged;
	public int numCheckedin;
	
	public int getConCID() { return this.conCID; }
	public String getConName() { return this.conName; }
	public String getConLocation() { return this.conLocation; }
	public Date getConStart() { return this.conStart; }
	public Date  getConEnd() { return this.conEnd; }
	public String getConComment() { return this.conComment; }
	public String getConWebsite() { return this.conWebsite; }
	public String getConEmail() { return this.conEmail; }
	public String getConStylesheet() { return this.conStylesheet; }
	public String getConBadgelayout() { return this.conBadgelayout; }
	public String getConDescription() { return this.conDescription; }
	public int getNumSubscribed() { return this.numSubscribed; }
	public int getNumRegistered() { return this.numRegistered; }
	public int getNumBadged() { return this.numBadged; }
	public int getNumCheckedin() { return this.numCheckedin; }
	public int getConCap() { return this.conCap; }
}
