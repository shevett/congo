package com.stonekeep.congo.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="config_rooms")
public class Room extends Data {
	
	private int id;
	private String roomName;
	private Integer roomVenue;
	private String roomLocation;
	private String roomPhone;
	private int roomWidth;
	private int roomLength;
	private int roomHeight;
	private int roomCapacity;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="room_id")
	public int getId() { return this.id; }   
	
	@Column(name="room_venue")
	public Integer getRoomVenue() { return this.roomVenue; }
	
	@Column(name="room_name")
	public String getRoomName() { return this.roomName; }
	
	@Column(name="room_locationinvenue")
	public String getRoomLocation() { return this.roomLocation; }
	
	@Column(name="room_phone")
	public String getRoomPhone() { return this.roomPhone; }
	
	@Column(name="room_width")
	public int getRoomWidth() { return this.roomWidth; }
	
	@Column(name="room_length")
	public int getRoomLength() { return this.roomLength; }
	
	@Column(name="room_height")
	public int getRoomHeight() { return this.roomHeight; }
	
	@Column(name="room_capacity")
	public int getRoomCapacity() { return this.roomCapacity; }
	
	public void setId(int newid) { this.id = newid; }
	public void setRoomVenue(Integer n) { this.roomVenue = n; }
	public void setRoomName(String newName) { this.roomName = newName; }
	public void setRoomPhone(String n) { this.roomPhone = n; }
	public void setRoomLocation(String n) { this.roomLocation = n; }
	public void setRoomWidth(int n) { this.roomWidth = n; }
	public void setRoomHeight(int n) { this.roomHeight = n; }
	public void setRoomLength(int n) { this.roomLength = n; }
	public void setRoomCapacity(int n) { this.roomCapacity = n; }
}
