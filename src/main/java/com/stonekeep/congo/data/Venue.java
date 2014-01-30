package com.stonekeep.congo.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="config_venues")
public class Venue extends Data {
	
	private int id = 0;
	private String venueName;
	private String venueWebsite;
	private String venueLocation;
	private String venueNotes;
	private String venuePhone;
	private String venueContact;
	private int venueRooms;
	private int venueFootage;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="venue_id")
	public int getId() { return this.id; }     
	
	@Column(name="venue_name")
	public String getVenueName() { return this.venueName; }
	
	@Column(name="venue_website")
	public String getVenueWebsite() { return this.venueWebsite; }
	
	@Column(name="venue_location")
	public String getVenueLocation() { return this.venueLocation; }
	
	@Column(name="venue_notes")
	public String getVenueNotes() { return this.venueNotes; }
	
	@Column(name="venue_phone")
	public String getVenuePhone() { return this.venuePhone; }
	
	@Column(name="venue_contact")
	public String getVenueContact() { return this.venueContact; }
	
	@Column(name="venue_rooms")
	public int getVenueRooms() { return this.venueRooms; }
	
	@Column(name="venue_footage")
	public int getVenueFootage() { return this.venueFootage; }
	
	public void setId(int newid) { this.id = newid; }
	public void setVenueName(String newName) { this.venueName = newName; }
	public void setVenueWebsite(String n) { this.venueWebsite = n; }
	public void setVenueLocation(String n) { this.venueLocation = n; }
	public void setVenueNotes(String n) { this.venueNotes = n; }
	public void setVenuePhone(String n) { this.venuePhone = n; }
	public void setVenueContact(String n) { this.venueContact = n; }
	public void setVenueRooms(int n) { this.venueRooms = n; }
	public void setVenueFootage(int n) { this.venueFootage = n; }
	
}
