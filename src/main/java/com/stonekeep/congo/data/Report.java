package com.stonekeep.congo.data;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Report {
	public int author;
	public Timestamp runDate;
	public String title;
	public String type;
	public String actionClass;
	public ArrayList<String> titles;
	public ArrayList<ArrayList<String>> rows;
	public ArrayList<ArrayList<String>> summary;
	public ArrayList<String> charts;
	
	public int getAuthor() { return this.author; }
	public Timestamp getRunDate() { return this.runDate; }
	public String getTitle() { return this.title; }
	public String getType() { return this.type; }
	public String getActionClass() { return this.actionClass; }
	public ArrayList<String> getTitles() { return this.titles; }
	public ArrayList<ArrayList<String>> getRows() { return this.rows; }
	public ArrayList<ArrayList<String>> getSummary() { return this.summary; }
	public ArrayList<String> getCharts() { return this.charts; }
	
	public Report() {
		titles = new ArrayList<String>();
		rows = new ArrayList<ArrayList<String>>();
		summary = new ArrayList<ArrayList<String>>();
		charts = new ArrayList<String>();
	}

}
