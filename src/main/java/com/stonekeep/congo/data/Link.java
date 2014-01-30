package com.stonekeep.congo.data;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

@Entity
@Table(name="reg_links")
public class Link extends Data {
	
	private int id;
	private int linkRid1;
	private int linkRid2;
	private String linkRequestKey;
	private Timestamp linkRequestDate;
	private Timestamp linkDate;
	private String linkStatus;
	
	// This is calculated...
	private String linkName;
	private boolean linkRegistered;
	private int linkMyRid ;
	private int linkOtherRid;
	

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="link_id")
	public int getId() { return this.id; }     
	
	@Column(name="link_rid1")
	public int getLinkRid1() { return this.linkRid1; }
	
	@Column(name="link_rid2")
	public int getLinkRid2() { return this.linkRid2; }
	
	@Column(name="link_requestdate")
	public Timestamp getLinkRequestDate() { return this.linkRequestDate; }
	
	@Column(name="link_requestkey")
	public String getLinkRequestKey() { return this.linkRequestKey; }
	
	@Column(name="link_date")
	public Timestamp getLinkDate() { return this.linkDate; }
	
	@Column(name="link_status")
	public String getLinkStatus() { return this.linkStatus; }
	
	@Transient
	public boolean getLinkRegistered() { return this.linkRegistered; }
	
	@Transient
	public String getLinkName() { return this.linkName; }
	
	@Transient
	public int getLinkMyRid() { return this.linkMyRid; }
	
	@Transient
	public int getLinkOtherRid() { return this.linkOtherRid; }
	
	public void setId(int newid) { this.id = newid; }
	public void setLinkRid1(int newrid) { this.linkRid1 = newrid; }
	public void setLinkRid2(int newrid) { this.linkRid2 = newrid; }
	public void setLinkRequestKey(String newkey) { this.linkRequestKey = newkey; }
	public void setLinkRequestDate(Timestamp ts) { this.linkRequestDate = ts; }
	public void setLinkName(String newname) { this.linkName = newname; }
	public void setLinkDate(Timestamp ts) { this.linkDate = ts; }
	//public void setLinkId(int id) {this.id = id;}   
	public void setLinkRegistered(Boolean b) { this.linkRegistered = b; }
	public void setLinkStatus(String s) { this.linkStatus = s; }
	public void setLinkMyRid(int i) { this.linkMyRid = i; }
	public void setLinkOtherRid(int i) { this.linkOtherRid = i; }
}
