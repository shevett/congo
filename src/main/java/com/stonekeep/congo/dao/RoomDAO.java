package com.stonekeep.congo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

import com.stonekeep.congo.data.Room;

public class RoomDAO extends GenericDAO<Room,Integer> {
	static Room dataType = new Room();
	static Integer fieldType = new Integer(0);
	private int cid = 0;		// injected
	
	public void setCid(int cid) { this.cid = cid; }

    @Override
    protected Class<Room> getDomainClass() {
        return Room.class;
    }
    
	public RoomDAO() {
		super(dataType,fieldType);
		setTableName("config_rooms");
		setPrimaryKey("id");
		setJoinSQL("left outer join reg_master " +
				"on Room_rid1=master_rid " +
				"left outer join reg_state " +
				"on Room_rid1=state_rid and state_cid=" + cid);
	}
	
	// Note this should use :named params

	@Transactional
	public List<Room> listAll() {
		Query q = sessionFactory.getCurrentSession().createQuery("from Room");
		return q.list();
	}
	
	@Transactional
	public void remove(Integer id) throws SQLException {
		Room f = new Room();
		f.setId(id);
		remove(f);
	}

	@Override
	public Room loadData(ResultSet r) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}