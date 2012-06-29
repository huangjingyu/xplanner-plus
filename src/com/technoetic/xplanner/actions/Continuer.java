/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: May 28, 2005
 * Time: 8:52:29 PM
 */
package com.technoetic.xplanner.actions;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Iterator;

import javassist.util.proxy.ProxyObject;
import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.History;
import net.sf.xplanner.domain.Note;

import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.domain.Describable;
import com.technoetic.xplanner.domain.DomainMetaDataRepository;
import com.technoetic.xplanner.domain.DomainObjectWikiLinkFormatter;
import com.technoetic.xplanner.domain.NoteAttachable;
import com.technoetic.xplanner.history.HistorySupport;
import com.technoetic.xplanner.util.LogUtil;

public abstract class Continuer {
	protected static final Logger LOG = LogUtil.getLogger();

	public static final String CONTINUE_ACTION = "Continue";
	public static final String MOVE_ACTION = "Move";
	protected MessageFormat toText;
	protected MessageFormat fromText;
	protected MessageFormat fromParentText;
	protected MessageFormat toParentText;

	protected DomainObjectWikiLinkFormatter linkFormatter;
	protected int currentUserId;
	private Session hibernateSession;
	protected Date when = new Date();
	public static final String CONTINUED_FROM_DESCRIPTION_KEY = "continue.description.from";
	public static final String CONTINUED_AS_DESCRIPTION_KEY = "continue.description.to";
	public static final String CONTINUE_DESCRIPTION_TO_PARENT_KEY = "continue.description.from_parent";
	public static final String CONTINUE_DESCRIPTION_FROM_PARENT_KEY = "continue.description.to_parent";
	protected DomainMetaDataRepository metaDataRepository;

	private HistorySupport historySupport;

	protected Continuer() {
	}

	public final void init(Session session, MessageResources messageResources, int currentUserId) {
		setHibernateSession(session);
		setLinkFormatter(new DomainObjectWikiLinkFormatter());
		setMessageResources(messageResources);
		setCurrentUserId(currentUserId);
	}

	public void setMessageResources(MessageResources messageResources) {
		toText = new MessageFormat(messageResources.getMessage(CONTINUED_FROM_DESCRIPTION_KEY));
		fromText = new MessageFormat(messageResources.getMessage(CONTINUED_AS_DESCRIPTION_KEY));
		fromParentText = new MessageFormat(messageResources.getMessage(CONTINUE_DESCRIPTION_FROM_PARENT_KEY));
		toParentText = new MessageFormat(messageResources.getMessage(CONTINUE_DESCRIPTION_TO_PARENT_KEY));
	}

	public DomainObject continueObject(DomainObject fromObject, DomainObject fromParent, DomainObject toParent)
			throws HibernateException {

		LOG.debug("hibernateSession used=" + getHibernateSession());
		DomainObject toObject = createContinuingObject(fromObject, toParent);

		doContinueObject(fromObject, toParent, toObject);

		// FIXME The original iteration should not have container events of
		// created action when continue a story in target targetIteration

		updateDescriptionAndHistory(fromObject, fromParent, toObject, toParent);

		continueNotes((NoteAttachable) fromObject, (NoteAttachable) toObject);

		getHibernateSession().update(fromObject);
		getHibernateSession().update(toParent);
		getHibernateSession().update(fromParent);

		return toObject;

	}

	public void setMetaDataRepository(DomainMetaDataRepository metaDataRepository) {
		this.metaDataRepository = metaDataRepository;
	}

	private DomainObject createContinuingObject(DomainObject fromObject, DomainObject toParent)
			throws HibernateException {
		DomainObject toObject = cloneObject(fromObject);
		getHibernateSession().save(toObject);
		metaDataRepository.setParent(toObject, toParent);
		return toObject;
	}

	private void updateDescriptionAndHistory(DomainObject fromObject, DomainObject fromParent, DomainObject toObject,
			DomainObject toParent) {
		Object[] parentDescriptionParams = new Object[] { linkFormatter.format(fromObject),
				linkFormatter.format(fromParent), linkFormatter.format(toObject), linkFormatter.format(toParent) };
		updateDescription((Describable) toObject, fromText, parentDescriptionParams);
		updateDescription((Describable) fromObject, toText, parentDescriptionParams);

		addHistoryEvent(History.CONTINUED, fromObject, toText.format(parentDescriptionParams));
		addHistoryEvent(History.CONTINUED, toObject, fromText.format(parentDescriptionParams));
		addHistoryEvent(History.CONTINUED, fromParent, toParentText.format(parentDescriptionParams));
		addHistoryEvent(History.CONTINUED, toParent, fromParentText.format(parentDescriptionParams));
	}

	private DomainObject cloneObject(DomainObject fromObject) {
		try {
			DomainObject toObject = createInstance(fromObject);
			BeanUtils.copyProperties(fromObject, toObject, new String[] { "tasks" });
			toObject.setId(0);
			return toObject;

		} catch (Exception e) {
			LOG.error(e);
		}
		return null;
	}

	protected DomainObject createInstance(DomainObject fromObject) throws InstantiationException,
			IllegalAccessException {

		if (ProxyObject.class.isAssignableFrom(fromObject.getClass())) {
			return (DomainObject) fromObject.getClass().getSuperclass().newInstance();
		}
		return (DomainObject) fromObject.getClass().newInstance();
	}

	protected abstract void doContinueObject(DomainObject fromObject, DomainObject toParent, DomainObject toObject)
			throws HibernateException;

	protected void addHistoryEvent(String type, DomainObject target, String description) {
		historySupport.saveEvent(target, type, description, currentUserId, when);
	}

	private void updateDescription(Describable describable, MessageFormat direction, Object[] params) {
		describable.setDescription(direction.format(params) + "\n\n" + describable.getDescription());
	}

	public void continueNotes(NoteAttachable fromObject, NoteAttachable toObject) throws HibernateException {
		Query query = getHibernateSession().getNamedQuery(Note.class.getName() + Note.ATTACHED_NOTES_QUERY);
		query.setString("attachedToId", String.valueOf(fromObject.getId()));
		Iterator iterator = query.iterate();
		while (iterator.hasNext()) {
			Note note = (Note) iterator.next();
			Note newNote = (Note) cloneObject(note);
			newNote.setAttachedToId(toObject.getId());
			newNote.setId(0);
			getHibernateSession().save(newNote);
		}
	}

	public Session getHibernateSession() {
		if (hibernateSession != ThreadSession.get()) {
			LOG.error("hibernateSession mismatch " + hibernateSession + " and " + ThreadSession.get());
		}
		return hibernateSession;
	}

	public void setHibernateSession(Session hibernateSession) {
		this.hibernateSession = hibernateSession;
	}

	public void setCurrentUserId(int currentUserId) {
		this.currentUserId = currentUserId;
	}

	public void setLinkFormatter(DomainObjectWikiLinkFormatter linkFormatter) {
		this.linkFormatter = linkFormatter;
	}


	@Required
	@Autowired
	public void setHistorySupport(HistorySupport historySupport) {
		this.historySupport = historySupport;
	}
	
}