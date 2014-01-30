package com.stonekeep.congo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import org.springframework.transaction.annotation.Transactional;

import com.stonekeep.congo.data.Link;
import com.stonekeep.congo.data.Setting;

public class SettingDAO extends GenericDAO<Setting,Integer> {
	static Setting dataType = new Setting();
	static Integer fieldType = new Integer(0);

	@Override
    protected Class<Setting> getDomainClass() {
        return Setting.class;
    }
    
	public SettingDAO() {
		super(dataType,fieldType);
	}
	
	
	@Transactional
	public void remove(Integer id) throws SQLException {
		Setting f = new Setting();
		f.setSettingId(id);
		remove(f);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Setting> list() {
		Query q = sessionFactory.getCurrentSession().createQuery("from Setting ");
		return q.list();
	}
	
	@Transactional
	public Map<String,Setting> listSettings() {
		// Query q = sessionFactory.getCurrentSession().createQuery("from Setting ");
		List<Setting> l = list();
		Map<String,Setting> m = new HashMap();
		for (Setting s : l) {
			m.put(s.getSettingName(),s);
		}
		return m;
	}
	
//	@Transactional
//	public Setting getById(Integer id) {
//		Setting f = super.getById(id);
//		// Need to also get the registration status for this...
//		return f;
//	}

	@Override
	public Setting loadData(ResultSet r) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	

}