package com.stonekeep.congo.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="config_categories")
public class Category extends Data {
	
	private int id;
	private int cid;
	private String categoryName;
	private boolean categoryActive;
	private String categoryDescription;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="cat_id")
	public int getId() { return this.id; }   
	
	@Column(name="cat_cid")
	public Integer getCid() { return this.cid; }
	
	@Column(name="cat_name")
	public String getCategoryName() { return this.categoryName; }
	
	@Column(name="cat_active")
	public boolean getCategoryActive() { return this.categoryActive; }
	
	@Column(name="cat_desc")
	public String getCategoryDescription() { return this.categoryDescription; }
		
	public void setId(int newid) { this.id = newid; }
	public void setCid(Integer n) { this.cid = n; }
	public void setCategoryName(String n) { this.categoryName = n; }
	public void setCategoryActive(boolean n) { this.categoryActive = n; }
	public void setCategoryDescription(String n) { this.categoryDescription = n; }
}
