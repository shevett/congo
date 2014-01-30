package com.stonekeep.congo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

import com.stonekeep.congo.data.Venue;

public class VenueDAO extends GenericDAO<Venue,Integer> {
	static Venue dataType = new Venue();
	static Integer fieldType = new Integer(0);
	private int cid = 0;		// injected
	
	public void setCid(int cid) { this.cid = cid; }

    @Override
    protected Class<Venue> getDomainClass() {
        return Venue.class;
    }
    
	public VenueDAO() {
		super(dataType,fieldType);
		setTableName("config_venues");
		setPrimaryKey("id");
		setJoinSQL("left outer join reg_master " +
				"on Venue_rid1=master_rid " +
				"left outer join reg_state " +
				"on Venue_rid1=state_rid and state_cid=" + cid);
	}
	
	// Note this should use :named params

	@Transactional
	public List<Venue> listAll() {
		Query q = sessionFactory.getCurrentSession().createQuery("from Venue");
		return q.list();
	}
	
	@Transactional
	public void remove(Integer id) throws SQLException {
		Venue f = new Venue();
		f.setId(id);
		remove(f);
	}

	@Override
	public Venue loadData(ResultSet r) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}