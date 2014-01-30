package com.stonekeep.congo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

import com.stonekeep.congo.data.Department;

public class DepartmentDAO extends GenericDAO<Department,Integer> {
	static Department dataType = new Department();
	static Integer fieldType = new Integer(0);
	private int cid = 0;		// injected
	
	public void setCid(int cid) { this.cid = cid; }

    @Override
    protected Class<Department> getDomainClass() {
        return Department.class;
    }
    
	public DepartmentDAO() {
		super(dataType,fieldType);
		setTableName("config_departments");
		setPrimaryKey("id");
		setJoinSQL("left outer join reg_master " +
				"on Department_rid1=master_rid " +
				"left outer join reg_state " +
				"on Department_rid1=state_rid and state_cid=" + cid);
	}
	
	// Note this should use :named params

	@Transactional
	public List<Department> listAll() {
		Query q = sessionFactory.getCurrentSession().createQuery("from Department");
		return q.list();
	}
	
	@Transactional
	public void remove(Integer id) throws SQLException {
		Department f = new Department();
		f.setId(id);
		remove(f);
	}

	@Override
	public Department loadData(ResultSet r) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}