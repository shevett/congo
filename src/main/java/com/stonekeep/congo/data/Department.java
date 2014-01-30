package com.stonekeep.congo.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="config_departments")
public class Department extends Data {
	
	private int id;
	private Integer cid;
	private String departmentName;
	private Integer departmentOwner;
	private String departmentDescription;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="dep_id")
	public int getId() { return this.id; }   
	
	@Column(name="dep_cid")
	public Integer getCid() { return this.cid; }
	
	@Column(name="dep_name")
	public String getDepartmentName() { return this.departmentName; }
	
	@Column(name="dep_owner")
	public Integer getDepartmentOwner() { return this.departmentOwner; }
	
	@Column(name="dep_desc")
	public String getDepartmentDescription() { return this.departmentDescription; }
		
	public void setId(int newid) { this.id = newid; }
	public void setCid(Integer n) { this.cid = n; }
	public void setDepartmentName(String n) { this.departmentName = n; }
	public void setDepartmentOwner(Integer n) { this.departmentOwner = n; }
	public void setDepartmentDescription(String n) { this.departmentDescription = n; }
}
