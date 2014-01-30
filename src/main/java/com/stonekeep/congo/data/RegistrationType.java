package com.stonekeep.congo.data;

import java.math.BigDecimal;

/**
 * A class representing a single registration type. 
 * @author dbs
 */
public class RegistrationType {
	private Integer regCID ;
	private String regName;
	private String regDesc;
	private String regPrint;
	private BigDecimal regCost;
	private String regExpire ;
	private Integer regCount;
	private Integer regSequence;
	private Boolean regActive;
	private String regDiscountCode;
	private String regBanner;
	private Boolean regComp;
	private Integer regDayMap;
	
	// this is calculated...
	public boolean[] regDayArray = new boolean[7];
	
	public Integer getRegCID() { return this.regCID; }
	public String getRegName() { return this.regName; }
	public String getRegDesc() { return this.regDesc; }
	public String getRegPrint() { return this.regPrint; }
	public BigDecimal getRegCost() { return this.regCost; }
	public String getCostText() { return "$" + this.regCost; }	// used as a textual format for displaying in lists.
	public String getRegExpire() { return this.regExpire; }
	public Integer getRegCount() { return this.regCount; }
	public Integer getRegSequence() { return this.regSequence; }
	public Boolean getRegActive() { return this.regActive; }
	public String getRegDiscountCode() { return this.regDiscountCode; }
	public String getRegBanner() { return this.regBanner; }
	public Boolean getRegComp() { return this.regComp; }
	public boolean[] getRegDayArray() { return this.regDayArray; }
	public Integer getRegDayMap() { return this.regDayMap; }

	public void setRegCID(Integer newcid) { this.regCID = newcid; }
	public void setRegName(String newname) { this.regName = newname; }
	public void setRegDesc(String newdesc) { this.regDesc = newdesc; }
	public void setRegPrint(String newprint) { this.regPrint = newprint; }
	public void setRegCost(BigDecimal newcost) { this.regCost = newcost; }
	public void setRegExpire(String newexpire) { this.regExpire = newexpire; }
	public void setRegCount(Integer newcount) { this.regCount = newcount; }
	public void setRegSequence(Integer newsequence ) { this.regSequence = newsequence; }
	public void setRegActive(Boolean newactive) { this.regActive = newactive; }
	public void setRegDiscountCode(String newcode) { this.regDiscountCode = newcode; }
	public void setRegBanner(String newbanner) { this.regBanner = newbanner; }
	public void setRegComp(Boolean comp) { this.regComp = comp; }
	public void setRegDayArray(boolean[] newdays) { this.regDayArray = newdays; }
	public void setRegDayMap(Integer newMap) { this.regDayMap = newMap; }
}
