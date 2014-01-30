package com.stonekeep.congo.data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="config_sessions")
public class Session extends Data {
	
	private int id;
	private Integer cid;
	private String sessionTitle;
	private String sessionDescription;
	private String sessionDetail;
	private String sessionNotes;
	private String sessionTags;
	private Integer sessionDepartment;
	private Integer sessionCategory;
	private Integer sessionRoom;
	private Date sessionStart;
	private Date sessionEnd;
	private int sessionDuration;
	private int sessionMaxAttendees;
	private BigDecimal sessionCost;
	private Integer sessionOwner;
	private String sessionStatus;
	private boolean sessionPublic;
	private Date sessionLastUpdated;
	private Integer sessionLastEditedBy;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="session_id")
	public int getId() { return this.id; }   
	
	@Column(name="session_cid")
	public Integer getCid() { return this.cid; }
	
	@Column(name="session_title")
	public String getSessionTitle() { return this.sessionTitle; }
	
	@Column(name="session_description")
	public String getSessionDescription() { return this.sessionDescription; }
	
	@Column(name="session_detail")
	public String getSessionDetail() { return this.sessionDetail; }
	
	@Column(name="session_notes")
	public String getSessionNotes() { return this.sessionNotes; }
	
	@Column(name="session_tags")
	public String getSessionTags() { return this.sessionTags; }
	
	@Column(name="session_department")
	public Integer getSessionDepartment() { return this.sessionDepartment; }
	
	@Column(name="session_category")
	public Integer getSessionCategory() { return this.sessionCategory; }
	
	@Column(name="session_room")
	public Integer getSessionRoom() { return this.sessionRoom; }
	
	@Column(name="session_start")
	public Date getSessionStart() { return this.sessionStart; }
	
	@Column(name="session_end")
	public Date getSessionEnd() { return this.sessionEnd; }
	
	@Column(name="session_Duration")
	public int getSessionDuration() { return this.sessionDuration; }
	
	@Column(name="session_maxattendees")
	public int getSessionMaxAttendees() { return this.sessionMaxAttendees; }
	
	@Column(name="session_cost")
	public BigDecimal getSessionCost() { return this.sessionCost; }
	
	@Column(name="session_owner")
	public Integer getSessionOwner() { return this.sessionOwner; }
	
	@Column(name="session_status")
	public String getSessionStatus() { return this.sessionStatus; }
	
	@Column(name="session_public")
	public boolean getSessionPublic() { return this.sessionPublic; }
	
	@Column(name="session_lastupdated")
	public Date getSessionLastUpdated() { return this.sessionLastUpdated; }
	
	@Column(name="session_lasteditedby")
	public Integer getSessionLastEditedBy() { return this.sessionLastEditedBy; }
	
	public void setId(int newid) { this.id = newid; }
	public void setCid(Integer n) { this.cid = n; }
	public void setSessionDescription(String n) { this.sessionDescription = n; }
	public void setSessionDetail(String n) { this.sessionDetail = n; }
	public void setSessionNotes(String n) { this.sessionNotes = n; }
	public void setSessionTags(String n) { this.sessionTags = n; }
	public void setSessionTitle(String n) { this.sessionTitle = n; }
	public void setSessionDepartment(Integer n) { this.sessionDepartment = n; }
	public void setSessionCategory(Integer n) { this.sessionCategory = n; }
	public void setSessionRoom(Integer n) { this.sessionRoom = n; }
	public void setSessionStart(Date n) { this.sessionStart = n; }
	public void setSessionEnd(Date n) { this.sessionEnd = n; }
	public void setSessionDuration(int n) { this.sessionDuration = n; }
	public void setSessionMaxAttendees(int n) { this.sessionMaxAttendees = n; }
	public void setSessionCost(BigDecimal n) { this.sessionCost = n; }
	public void setSessionOwner(Integer n) { this.sessionOwner = n; }
	public void setSessionStatus(String n) { this.sessionStatus = n; }
	public void setSessionPublic(boolean n) { this.sessionPublic = n; }
	public void setSessionLastUpdated(Date n) { this.sessionLastUpdated = n; }
	public void setSessionLastEditedBy(Integer n) { this.sessionLastEditedBy = n; }
}
