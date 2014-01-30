package com.stonekeep.congo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

import com.stonekeep.congo.data.Session;

public class SessionDAO extends GenericDAO<Session,Integer> {
	static Session dataType = new Session();
	static Integer fieldType = new Integer(0);
	private int cid = 0;		// injected
	
	public void setCid(int cid) { this.cid = cid; }

    @Override
    protected Class<Session> getDomainClass() {
        return Session.class;
    }
    
	public SessionDAO() {
		super(dataType,fieldType);
		setTableName("config_sessions");
		setPrimaryKey("id");
	}
	
	// Note this should use :named params

	@Transactional
	public List<Session> listAll() {
		Query q = sessionFactory.getCurrentSession().createQuery("from Session");
		return q.list();
	}
	
	@Transactional
	public List<Session> listAll(int whatcid) {
		Query q = sessionFactory.getCurrentSession().createQuery("from Session as s where s.cid=" + whatcid);
		return q.list();
	}
	
	@Transactional
	public void remove(Integer id) throws SQLException {
		Session f = new Session();
		f.setId(id);
		remove(f);
	}

	@Override
	public Session loadData(ResultSet r) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}