package net.sf.xplanner.dao.impl;

import java.io.Serializable;

import net.sf.xplanner.domain.Task;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.technoetic.xplanner.domain.Identifiable;

public class CommonDao<E extends Identifiable> {
	private SessionFactory sessionFactory;

	public <T> T getById(Class<T> clazz, Serializable id) {
		return (T) getSession().get(clazz, id);
	}
	
	protected final Session getSession(){
		return SessionFactoryUtils.getSession(sessionFactory, Boolean.FALSE);
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Criteria createCriteria() {
		return null;
	}

	public int save(E object) {
		getSession().save(object);
		return object.getId();
	}

	public void delete(Object object) {
		getSession().delete(object);
	}

	public void flush() {
		getSession().flush();
	}

	public void rollback() {
		if(getSession().isOpen() && getSession().isDirty()){
			getSession().getTransaction().rollback();
		}
	}
}
