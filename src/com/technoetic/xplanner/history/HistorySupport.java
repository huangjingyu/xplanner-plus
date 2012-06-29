package com.technoetic.xplanner.history;

import java.util.Date;
import java.util.List;

import net.sf.xplanner.domain.History;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.technoetic.xplanner.domain.DomainMetaDataRepository;
import com.technoetic.xplanner.domain.Identifiable;

public class HistorySupport {
	private static Logger log = Logger.getLogger(HistorySupport.class);
	private static long FIFTEEN_MINUTES = 3600000 * 15;
	private SessionFactory sessionFactory;

	// DEBT should be spring loaded (metadatarepository should be passed in and
	// we should not have private methods
	private void saveEvent(Class objectClass, int containerOid, int oid, String action,
			String description, int personId, Date when) {
		try {
			History History = new History(when, containerOid, oid, DomainMetaDataRepository.getInstance()
					.classToTypeName(objectClass), action, description, personId);
			if (!isEventThrottled(getSession(), History)) {
				getSession().save(History);
			}
			if (action.equals(History.DELETED)) {
				// Set name in event descriptions for deleted objects
				List events = getEvents(oid);
				for (int i = 0; i < events.size(); i++) {
					History event = (History) events.get(i);
					if (StringUtils.isEmpty(event.getDescription())) {
						event.setDescription(description);
					}
				}
			}
		} catch (HibernateException e) {
			log.error("history error", e);
		}
	}

	private boolean isEventThrottled(Session session, History event) throws HibernateException {
		if (event.getAction().equals(History.UPDATED)) {
			History previousEvent = (History) session
					.createQuery(
							"from event in "
									+ History.class
									+ " where event.targetId = :oid and event.action = :action order by event.whenHappened desc")
					.setInteger("oid", event.getTargetId()).setString("action", History.UPDATED).setMaxResults(1)
					.uniqueResult();
			return previousEvent != null
					&& (event.getWhenHappened().getTime() - previousEvent.getWhenHappened().getTime()) < FIFTEEN_MINUTES;
		} else {
			return false;
		}
	}

	public void saveEvent(Identifiable object, String action, String description, int personId,
			Date when) {
		try {
			Integer id = (Integer) PropertyUtils.getProperty(object, "id");
			saveEvent(object.getClass(), DomainMetaDataRepository.getInstance().getParentId(object),
					id.intValue(), action, description, personId, when);
		} catch (Exception e) {
			log.error("history error", e);
		}
	}

	public List getEvents(int oid) throws HibernateException {
		// TODO externalize these queries into mapping file
		Query query = getSession().createQuery("from event in " + History.class + " where event.targetId = ? order by event.whenHappened desc");
		query.setInteger(0, oid);
		return query.list();
	}

	public List getContainerEvents(int oid) throws Exception {
		Query query = getSession().createQuery("from event in " + History.class + " where event.containerId = ? " + " and event.action in ('"
				+ History.CREATED + "','" + History.DELETED + "') order by event.whenHappened desc");
		query.setInteger(0, oid);
		return query.list();
	}

	public Object getHistoricalObject(History event) throws HibernateException {
		if (event.getAction().equals(History.DELETED)) {
			return null;
		}
		return DomainMetaDataRepository.getInstance().getObject(event.getObjectType(), event.getTargetId());
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected final Session getSession(){
		return SessionFactoryUtils.getSession(sessionFactory, Boolean.FALSE);
	}

}
