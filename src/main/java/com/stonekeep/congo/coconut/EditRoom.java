package com.stonekeep.congo.coconut;

import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Action;
import com.stonekeep.congo.dao.RoomDAO;
import com.stonekeep.congo.data.Room;

public class EditRoom extends Editor {

	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(EditRoom.class);
	public int id;
	public List<Room> rooms;
	public Room workingroom;
	private int whatcid;
	private final RoomDAO roomDAO;
	public String message;
	
	public EditRoom(RoomDAO roomDAO) {
		this.roomDAO = roomDAO;
	}

	public void prepare() throws Exception {
		logger.debug("prepare: Prepare starting");
		if (rooms == null) {
			logger.debug("prepare: Initializing roomDAO and getting rooms");
			rooms = roomDAO.listAll();
			logger.debug("prepare: rooms list is " + rooms.size() + " elements long.");
			logger.debug(rooms);
			for (Room v:rooms) {
				logger.debug("room is " + v);
			}
		}
		if (id > 0) {
			workingroom = roomDAO.getById(id);
		}
		logger.debug("prepare: workingroom is : " + workingroom);
		logger.debug("prepare: id is " + id);
		logger.debug("Exiting prepare - workingroom is " + workingroom);
	}

	public String view() throws Exception {
		logger.debug("view...");
		if (typebutton != null) {
			return Action.SUCCESS;
		}
		if (exitbutton != null) {
			return "exit";
		}
		// load up the data for the edit form...
		logger.debug("view: Loading room " + id);
		workingroom = roomDAO.getById(id);
		logger.debug("workingroom is " + workingroom);
		return Action.SUCCESS;
	}

	public String delete() throws Exception {
		logger.debug("delete...");
		logger.debug("I'll be deleting " + id);
		roomDAO.remove(id);
		return Action.SUCCESS;
	}
	
	public String create() throws Exception {
		logger.debug("Creating new room...");
		Room v = new Room();
		v.setRoomName("New room");
		roomDAO.add(v);
		return Action.SUCCESS;
	}

	public String update() throws Exception {
		logger.debug("updating...");
		logger.debug("update: id is " + id);
		logger.debug("update: Button is " + typebutton);
		logger.debug("update: workingroom is " + workingroom);
		if (typebutton != null) {
			// Here we need to validate some stuff.
			if (workingroom.getRoomName().length() < 1) {
				logger.debug("No room name specified .  Returning INPUT");
				message="Please specify a name for this room";
				return INPUT;
			}
			logger.debug("update: saving, this is an update...");
			logger.debug("Room id is " + workingroom.getId());
			logger.debug("room name is " + workingroom.getRoomName());
			roomDAO.update(workingroom);
		}
		logger.debug("Exiting update()");
		return Action.SUCCESS;
	}
	

}
