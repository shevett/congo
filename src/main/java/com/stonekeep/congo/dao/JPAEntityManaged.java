package com.stonekeep.congo.dao;

import javax.persistence.EntityManager;

public abstract class JPAEntityManaged {

	private EntityManager m_entityManager;

	public EntityManager getEntityManager() {
		return m_entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		m_entityManager = entityManager;
	}
	
}
