package com.technoetic.xplanner.soap;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.xplanner.context.ContextLoaderListener;
import net.sf.xplanner.dao.impl.CommonDao;
import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.History;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Note;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.TimeEntry;
import net.sf.xplanner.domain.UserStory;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.ByteArrayConverter;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;
import org.hibernate.type.Type;
import org.springframework.remoting.jaxrpc.ServletEndpointSupport;

import com.technoetic.xplanner.db.QueryException;
import com.technoetic.xplanner.db.TaskQueryHelper;
import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.domain.RelationshipMappingRegistry;
import com.technoetic.xplanner.domain.repository.AttributeRepository;
import com.technoetic.xplanner.domain.repository.AttributeRepositoryImpl;
import com.technoetic.xplanner.filters.ThreadServletRequest;
import com.technoetic.xplanner.history.HistorySupport;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;
import com.technoetic.xplanner.soap.domain.DomainData;
import com.technoetic.xplanner.soap.domain.IterationData;
import com.technoetic.xplanner.soap.domain.NoteData;
import com.technoetic.xplanner.soap.domain.PersonData;
import com.technoetic.xplanner.soap.domain.ProjectData;
import com.technoetic.xplanner.soap.domain.TaskData;
import com.technoetic.xplanner.soap.domain.TimeEntryData;
import com.technoetic.xplanner.soap.domain.UserStoryData;
import com.technoetic.xplanner.tags.DomainContext;
import com.technoetic.xplanner.util.MainBeanFactory;

// TODO: SOAP input validation.
// Ideally, extract the validation out of the struts forms into reusable
// validation to be used by soap

public class XPlanner {
    private final Logger log = Logger.getLogger(getClass());
    private final AttributeRepository attributes = new AttributeRepositoryImpl();

    public XPlanner()
    {
        //  The SOAP interface is required to use Calendars for dates. This
        //  converter is intended to be an adapter for the Date usage in the
        //  XPlanner domain objects. However, I'm not comfortable with this since
        //  the converters are global objects.
        ConvertUtils.register(new Converter()
        {
            public Object convert(Class type, Object value)
            {
                if (value == null) return null;
                if (value instanceof Calendar) return ((Calendar) value).getTime();
                return value;
            }
        }, Date.class);
        ConvertUtils.register(new Converter()
        {
            public Object convert(Class type, Object value)
            {
                if (value == null) return null;
                if (value instanceof Date)
                {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime((Date) value);
                    return calendar;
                }
                return value;
            }
        }, Calendar.class);
        ConvertUtils.register(new ByteArrayConverter(null), byte[].class); // by default a null value is not converted in a null array.
    }

    //
    // Projects
    //

    public ProjectData[] getProjects() throws Exception
    {
        return (ProjectData[]) getObjects(ProjectData.class, null, null, null, null);
    }

    public ProjectData getProject(int id) throws Exception
    {
        return (ProjectData) getObject(ProjectData.class, id);
    }

    public ProjectData addProject(ProjectData project) throws Exception
    {
        return (ProjectData) addObject(0, project);
    }

    public void removeProject(int id) throws Exception
    {
        removeObject(ProjectData.class, id);
    }

    public void update(ProjectData object) throws Exception
    {
        updateObject(object);
    }

    public IterationData getCurrentIteration(int projectId) throws Exception
    {
        IterationData[] iterations = (IterationData[]) getObjects(IterationData.class,
                                                                  "object.startDate <= ? and object.endDate >= ? and object.projectId = ?",
                                                                  new Object[]{new Date(),
                                                                               new Date(),
                                                                               new Integer(projectId)},
                                                                  new Type[]{Hibernate.DATE,
                                                                             Hibernate.DATE,
                                                                             Hibernate.INTEGER},
                                                                  null);
        return iterations.length > 0 ? iterations[0] : null;
    }

    public IterationData[] getIterations(int projectId) throws Exception
    {
        return (IterationData[]) getObjects(ProjectData.class, projectId, "iterations", IterationData.class);
    }

    //
    // Iterations
    //

    public IterationData getIteration(int id) throws Exception
    {
        return (IterationData) getObject(IterationData.class, id);
    }

    public IterationData addIteration(IterationData iteration) throws Exception
    {
        return (IterationData) addObject(getProjectId(Project.class, iteration.getProjectId()), iteration);
    }

    public void removeIteration(int id) throws Exception
    {
        removeObject(IterationData.class, id);
    }

    public void update(IterationData object) throws Exception
    {
        updateObject(object);
    }

    public UserStoryData[] getUserStories(int containerId) throws Exception
    {
        return (UserStoryData[]) getObjects(IterationData.class, containerId, "userStories", UserStoryData.class);
    }

    //
    // User Stories
    //

    public UserStoryData getUserStory(int id) throws Exception
    {
        return (UserStoryData) getObject(UserStoryData.class, id);
    }

    public UserStoryData addUserStory(UserStoryData story) throws Exception
    {
        return (UserStoryData) addObject(getProjectId(Iteration.class, story.getIterationId()), story);
    }

    public void removeUserStory(int id) throws Exception
    {
        removeObject(UserStoryData.class, id);
    }

    public void update(UserStoryData object) throws Exception
    {
        updateObject(object);
    }

    public TaskData[] getTasks(int containerId) throws Exception
    {
        return (TaskData[]) getObjects(UserStoryData.class, containerId, "tasks", TaskData.class);
    }
//FEATURE:
//    public FeatureData[] getFeatures(int containerId) throws Exception {
//        return (FeatureData[])getObjects(UserStoryData.class, containerId, "features", FeatureData.class);
//    }


    //
    // Features
    //

//   public FeatureData getFeature(int id) throws Exception {
//       return (FeatureData)getObject(FeatureData.class, id);
//   }
//
//   public FeatureData addFeature(FeatureData feature) throws Exception {
//       return (FeatureData)addObject(getProjectId(UserStory.class, feature.getStoryId()), feature);
//   }
//
//   public void removeFeature(int id) throws Exception {
//       removeObject(FeatureData.class, id);
//   }
//
//   public void update(FeatureData object) throws Exception {
//           updateObject(object);
//   }


    //
    // Tasks
    //

    public TaskData getTask(int id) throws Exception
    {
        return (TaskData) getObject(TaskData.class, id);
    }

    public TaskData[] getCurrentTasksForPerson(int personId) throws QueryException
    {
       final TaskQueryHelper taskQueryHelper = (TaskQueryHelper) getSpringBean("taskQueryHelper");
       taskQueryHelper.setPersonId(personId);
       return (TaskData[]) toArray(TaskData.class,
                                    taskQueryHelper.getCurrentActiveTasksForPerson());
    }

    public TaskData[] getPlannedTasksForPerson(int personId) throws QueryException
    {
       final TaskQueryHelper taskQueryHelper = (TaskQueryHelper) getSpringBean("taskQueryHelper");
       taskQueryHelper.setPersonId(personId);
       taskQueryHelper.setPersonId(personId);
       return (TaskData[]) toArray(TaskData.class,
                                    taskQueryHelper.getCurrentPendingTasksForPerson());
    }

    private Object getSpringBean(String string) {
		return ContextLoaderListener.getContext().getBean(string);
	}

	public TaskData addTask(TaskData task) throws Exception
    {
        return (TaskData) addObject(getProjectId(UserStory.class, task.getStoryId()), task);
    }

    public void removeTask(int id) throws Exception
    {
        removeObject(TaskData.class, id);
    }

    public void update(TaskData object) throws Exception
    {
        updateObject(object);
    }


    //
    // Time Entries
    //

    public TimeEntryData[] getTimeEntries(int containerId) throws Exception
    {
        return (TimeEntryData[]) getObjects(TaskData.class, containerId, "timeEntries", TimeEntryData.class);
    }

    public TimeEntryData getTimeEntry(int id) throws Exception
    {
        return (TimeEntryData) getObject(TimeEntryData.class, id);
    }

    public TimeEntryData addTimeEntry(TimeEntryData timeEntry) throws Exception
    {
        return (TimeEntryData) addObject(getProjectId(Task.class, timeEntry.getTaskId()), timeEntry);
    }

    public void removeTimeEntry(int id) throws Exception
    {
        removeObject(TimeEntryData.class, id);
    }

    public void update(TimeEntryData object) throws Exception
    {
        updateObject(object);
    }

    //
    // Notes
    //

    public NoteData getNote(int id) throws Exception
    {
        return (NoteData) getObject(NoteData.class, id);
    }

    public NoteData addNote(NoteData note) throws Exception
    {
        return (NoteData) addObject(getProjectId(DomainContext.getNoteTarget(note.getAttachedToId())), note);
    }

    public void removeNote(int id) throws Exception
    {
        removeObject(NoteData.class, id);
    }

    public void update(NoteData note) throws Exception
    {
        updateObject(note);
    }

    public NoteData[] getNotesForObject(int attachedToId) throws Exception
    {
        return (NoteData[]) getObjects(NoteData.class, "attachedTo_Id = " + attachedToId, null, null, null);
    }

    //
    // People
    //

    public PersonData getPerson(int id) throws Exception
    {
        return (PersonData) getObject(PersonData.class, id);
    }

    public PersonData addPerson(PersonData object) throws Exception
    {
        return (PersonData) addObject(0, object);
    }

    public void removePerson(int id) throws Exception
    {
        removeObject(PersonData.class, id);
    }

    public void update(PersonData object) throws Exception
    {
        updateObject(object);
    }

    public PersonData[] getPeople() throws Exception
    {
        return (PersonData[]) getObjects(PersonData.class, null, null, null, null);
    }

    //
    // Attributes
    //

    public void setAttribute(int objectId, String key, String value) throws Exception
    {
        attributes.setAttribute(objectId, key, value);
        commit(ThreadSession.get());
    }

    public String getAttribute(int objectId, String key) throws Exception
    {
        return attributes.getAttribute(objectId, key);
    }

    public void deleteAttribute(int objectId, String key) throws Exception
    {
        attributes.delete(objectId, key);
        commit(ThreadSession.get());
    }

    public Map getAttributes(int objectId) throws Exception
    {
        return attributes.getAttributes(objectId, null);
    }

    public Map getAttributesWithPrefix(int objectId, String prefix) throws Exception
    {
        return attributes.getAttributes(objectId, prefix);
    }

    //
    // Support Functions
    //

    private int getProjectId(Class containerClass, int containerId) throws Exception
    {
        Object object = ThreadSession.get().load(containerClass, new Integer(containerId));
        DomainContext context = new DomainContext();
        context.populate(object);
        return context.getProjectId();
    }

    private int getProjectId(Object object) throws Exception
    {
        DomainContext context = new DomainContext();
        context.populate(object);
        return context.getProjectId();
    }

    private Object[] getObjects(Class dataClass, String where, Object[] values, Type[] types, String orderBy) throws Exception
    {
        try
        {
            Session session = ThreadSession.get();
            Class objectClass = getInternalClass(dataClass);
            String query = "from " + objectClass.getName();
            if (where != null)
            {
                query += " where " + where;
            }
            if (orderBy != null)
            {
                query += " order by " + orderBy;
            }
            List objects = values != null
                ? session.find(query, values, types)
                : session.find(query);
            return toArray(dataClass, objects);
        }
        catch (Exception ex)
        {
            log.error("error loading objects", ex);
            throw ex;
        }
    }

    private Object[] getObjects(Class fromDataClass, int id, String propertyName, Class toDataClass) throws Exception
    {
        try
        {
            Session session = ThreadSession.get();
            Class objectClass = getInternalClass(fromDataClass);
            log.debug("getting object: " + id);
            Object object = session.load(objectClass, new Integer(id));
            log.debug("loaded object: " + object);
            Collection objects = (Collection) PropertyUtils.getProperty(object, propertyName);
            Object[] dataArray = toArray(toDataClass, objects);
            return dataArray;
        }
        catch (Exception ex)
        {
            log.error("error loading objects", ex);
            throw ex;
        }
    }

    private Object getObject(Class dataClass, int id) throws Exception
    {
       Session session = ThreadSession.get();
       try {
          Class objectClass = getInternalClass(dataClass);
          log.debug("getting object: " + id);
          DomainObject object = (DomainObject) session.load(objectClass, new Integer(id));
          log.debug("loaded object: " + object);
          DomainData data = (DomainData) dataClass.newInstance();
          if (hasPermission(getProjectId(object), object, "read"))
          {
             populateDomainData(data, object);
             return data;
          }
          else
          {
             throw new AuthenticationException("no permission to read object");
          }
       }
       catch (ObjectNotFoundException ex) {
          return null;
       }
       catch (Exception ex) {
          log.error("error loading objects", ex);
          throw ex;
       }
    }

    static Integer NULL = new Integer(-1);

    private void updateObject(DomainData data) throws Exception
    {
        Session session = null;
        try
        {
            Integer id = NULL;
            session = ThreadSession.get();
            Class objectClass = getInternalClass(data.getClass());
            id = getObjectId(data);
            DomainObject object = (DomainObject) session.load(objectClass, id);
            if (hasPermission(getProjectId(object), object, "edit"))
            {
                // JM: no need to write lock the object
                // There is a lot more chance to get the client out-of-sync during a get/update than just in that method
                // See to-do at the top of the file for better implementation
                if (object != null)
                {
                    populateDomainObject(object, data);
                }
                saveHistory(session, object, History.UPDATED);
                commit(session);
            }
            else
            {
                throw new AuthenticationException("no permission to update object");
            }
        }
        catch (Exception ex)
        {
            rollback(session);
            throw ex;
        }
    }

    private void saveHistory(Session session, DomainObject object, String eventType) throws AuthenticationException
    {
        String description = null;
        if (eventType.equals(History.DELETED))
        {
            try
            {
                description = BeanUtils.getProperty(object, "name");
            }
            catch (Exception e)
            {
                description = "unknown name";
            }
        }
		((HistorySupport) getSpringBean("historySupport")).saveEvent(object, eventType, description,
				SecurityHelper.getRemoteUserId(ThreadServletRequest.get()), new Date());
    }

    private Integer getObjectId(Object data) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        return (Integer) PropertyUtils.getProperty(data, "id");
    }

    protected void removeObject(Class dataClass, int id) throws Exception
    {
       //DEBT Should use the metarepository
        Session session = null;
        try
        {
            session = ThreadSession.get();
            log.debug("removing object: " + id);
            Class objectClass = getInternalClass(dataClass);
            DomainObject object = (DomainObject) session.load(objectClass, new Integer(id));
            if (hasPermission(getProjectId(object), object, "delete"))
            {
                session.delete(object);
                saveHistory(session, object, History.DELETED);
                commit(session);
            }
            else
            {
                throw new AuthenticationException("no permission to delete object");
            }
        }
        catch (ObjectNotFoundException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            rollback(session);
            throw ex;
        }
    }


    protected Object addObject(int projectId, DomainData data) throws Exception
    {
        Session session = null;
        try
        {
            session = ThreadSession.get();
            Class objectClass = getInternalClass(data.getClass());
            DomainObject object = (DomainObject) objectClass.newInstance();
            if (hasPermission(projectId, object, "create"))
            {
                populateDomainObject(object, data);
                log.debug("adding object: " + object);
                Serializable id = session.save(object);
                saveHistory(session, object, History.CREATED);
                commit(session);
//                return getObject(data.getClass(), ((Integer) id).intValue());
                populateDomainData(data, object);
                return data;
            }
            else
            {
                throw new AuthenticationException("no permission to create object");
            }
        }
        catch (Exception ex)
        {
            rollback(session);
            throw ex;
        }
    }

    private void commit(Session session)
    {
        if (session == null) return;
        try
        {
            session.flush();
            session.connection().commit();
        }
        catch (Exception ex)
        {
            log.error("error", ex);
            throw new RuntimeException(ex);
        }
    }

    private void rollback(Session session)
    {
        if (session == null) return;
        try
        {
            session.connection().rollback();
        }
        catch (Exception e)
        {
            log.error("error", e);
        }
    }

    private void closeSession(Session session)
    {
        if (session == null) return;
        try
        {
            session.close();
        }
        catch (Exception ex)
        {
            log.error("error", ex);
        }
    }

    private Object[] toArray(Class dataClass, Collection objects)
    {
        try
        {
            ArrayList accessibleObjects = selectAccessibleObjects(objects);
            Object[] dataObjects = createArray(dataClass, accessibleObjects);
            Iterator iter = accessibleObjects.iterator();
            for (int i = 0; iter.hasNext(); i++)
            {
                populateDomainData((DomainData) dataObjects[i], (DomainObject) iter.next());
            }
            return dataObjects;
        }
        catch (Exception ex)
        {
            log.error("error in toArray", ex);
            return null;
        }
    }

    private ArrayList selectAccessibleObjects(Collection objects) throws Exception
    {
        ArrayList accessibleObjects = new ArrayList();
        for (Iterator objectIterator = objects.iterator(); objectIterator.hasNext();)
        {
            DomainObject object = (DomainObject) objectIterator.next();
            if (hasPermission(getProjectId(object), object, "read"))
            {
                accessibleObjects.add(object);
            }

        }
        return accessibleObjects;
    }

    private boolean hasPermission(int projectId, DomainObject sourceObject, String permission) throws Exception
    {
        int remoteUserId = SecurityHelper.getRemoteUserId(ThreadServletRequest.get());
        log.info("Checking permission for userid " + remoteUserId);
        return SystemAuthorizer.get().hasPermission(projectId, remoteUserId,
                                                    sourceObject, permission);
    }

    private void populateDomainData(DomainData data, DomainObject sourceObject) throws IllegalAccessException,
                                                                                       InvocationTargetException,
                                                                                       NoSuchMethodException
    {
        BeanUtils.copyProperties(data, sourceObject);
        Map description = PropertyUtils.describe(data);
        Iterator keyItr = description.keySet().iterator();
        while (keyItr.hasNext())
        {
            String key = (String) keyItr.next();
            if ("class".equals(key) || "relationshipMapping".equals(key)) continue;
            if (isRelationship(sourceObject, key))
            {
                RelationshipMappingRegistry.getInstance().getRelationshipMapping(sourceObject, key).populateAdapter(
                    data, sourceObject);
            }
        }
    }

    private void populateDomainObject(DomainObject targetObject, DomainData data)
        throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, HibernateException
    {
        BeanUtils.copyProperties(targetObject, data);
        Map description = PropertyUtils.describe(data);
        Iterator keyItr = description.keySet().iterator();
        while (keyItr.hasNext())
        {
            String key = (String) keyItr.next();
            if ("class".equals(key) || "relationshipMapping".equals(key)) continue;
            if (isRelationship(targetObject, key))
            {
            	//FIXME: soap service
                RelationshipMappingRegistry.getInstance().getRelationshipMapping(targetObject, key).populateDomainObject(
                    targetObject, data, (CommonDao<?>)getSpringBean("commonDao"));
            }
        }
    }

    private boolean isRelationship(DomainObject domainObject, String key)
    {
        return RelationshipMappingRegistry.getInstance().getRelationshipMapping(domainObject, key) != null;
    }

    private Object[] createArray(Class dataClass, Collection objects) throws InstantiationException,
                                                                             IllegalAccessException
    {
        Object[] dataObjects = (Object[]) Array.newInstance(dataClass, objects.size());
        for (int i = 0; i < dataObjects.length; i++)
        {
            dataObjects[i] = dataClass.newInstance();
        }
        return dataObjects;
    }

    private Class getInternalClass(Class dataClass)
    {
        try
        {
            Method method = dataClass.getMethod("getInternalClass", null);
            return (Class) method.invoke(dataClass, null);
        }
        catch (Exception e)
        {
            return getDomainClassForDataClass(dataClass);
        }
    }

    private Class getDomainClassForDataClass(Class dataClass)
    {
        return (Class) dataToDomainClassMap.get(dataClass);
    }

    static Map dataToDomainClassMap = createDataToDomainClassMap();

    private static Map createDataToDomainClassMap()
    {
        HashMap map = new HashMap();
        map.put(ProjectData.class, Project.class);
        map.put(IterationData.class, Iteration.class);
        map.put(UserStoryData.class, UserStory.class);
        map.put(TaskData.class, Task.class);
        map.put(TimeEntryData.class, TimeEntry.class);
        map.put(PersonData.class, Person.class);
        map.put(NoteData.class, Note.class);
        return map;
    }

}
