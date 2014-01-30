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
@Table(name="config_settings")
public class Setting extends Data {
	
	private int settingId;
	private String settingName;
	private String settingType;
	private String settingValue;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="setting_id")
	public int getSettingId() { return this.settingId; }     
	
	@Column(name="setting_name")
	public String getSettingName() { return this.settingName; }
	
	@Column(name="setting_type")
	public String getSettingType() { return this.settingType; }
	
	@Column(name="setting_value")
	public String getSettingValue() { return this.settingValue; }
	
	public void setSettingId(int newid) { this.settingId = newid; }
	public void setSettingName(String newname) { this.settingName = newname; }
	public void setSettingType(String newtype) { this.settingType = newtype; }
	public void setSettingValue(String newvalue) { this.settingValue = newvalue; }
}
