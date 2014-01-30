package com.stonekeep.congo.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="config_roomlayout")
public class Layout extends Data {
	
	private int id;
	private String layoutName;
	private Integer layoutTables;
	private String layoutNotes;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="layout_id")
	public int getId() { return this.id; }   
	
	@Column(name="layout_name")
	public String getLayoutName() { return this.layoutName; }
	
	@Column(name="layout_tables")
	public Integer getLayoutTables() { return this.layoutTables; }
	
	@Column(name="layout_notes")
	public String getLayoutNotes() { return this.layoutNotes; }
		
	public void setId(int newid) { this.id = newid; }
	public void setLayoutName(String n) { this.layoutName = n; }
	public void setLayoutTables(Integer n) { this.layoutTables = n; }
	public void setLayoutNotes(String n) { this.layoutNotes= n; }
}
