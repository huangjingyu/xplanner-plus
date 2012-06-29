package com.technoetic.xplanner.tags;

import java.util.List;

import javax.servlet.ServletRequest;

import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Note;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.TimeEntry;
import net.sf.xplanner.domain.UserStory;

import org.apache.struts.action.ActionMapping;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.db.IdSearchHelper;
import com.technoetic.xplanner.db.OLDIdSearchHelper;
import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.domain.Feature;

// todo - Get rid of this once we have a true object model with relationships

public class DomainContext {
    public static final String REQUEST_KEY = "domainContext";

    private String projectName;
    private int projectId;
    private String iterationName;
    private int iterationId;
    private String storyName;
    private int storyId;
    private String taskName;
    private int taskId;
    private String featureName;
    private int featureId;
    private Object targetObject;
    private ActionMapping actionMapping;
    private static OLDIdSearchHelper idSearchHelper = new OLDIdSearchHelper();

   public ActionMapping getActionMapping() {
       return actionMapping;
   }

    public void setActionMapping(ActionMapping actionMapping) {
        this.actionMapping = actionMapping;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getIterationName() {
        return iterationName;
    }

    public void setIterationName(String iterationName) {
        this.iterationName = iterationName;
    }

    public int getIterationId() {
        return iterationId;
    }

    public void setIterationId(int iterationId) {
        this.iterationId = iterationId;
    }

    public String getStoryName() {
        return storyName;
    }

    public void setStoryName(String storyName) {
        this.storyName = storyName;
    }

    public int getStoryId() {
        return storyId;
    }

    public void setStoryId(int storyId) {
        this.storyId = storyId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getTaskId() {
        return taskId;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public int getFeatureId() {
        return featureId;
    }

    public void setFeatureId(int featureId) {
        this.featureId = featureId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }


    public Object getTargetObject() {
        return targetObject;
    }

    public void populate(Object object) throws Exception {
        Session session = ThreadSession.get();
        targetObject = object;
        if (object instanceof Project) {
            populate((Project)object);
        } else if (object instanceof Iteration) {
            populate(session, (Iteration)object);
        } else if (object instanceof UserStory) {
            populate(session, (UserStory)object);
        } else if (object instanceof Task) {
            populate(session, (Task)object);
        } else if (object instanceof Feature) {
            populate(session, (Feature)object);                
        } else if (object instanceof Note) {
            populate((Note)object);
        } else if (object instanceof TimeEntry) {
            populate(session, (TimeEntry)object);
        }
    }

    private void populate(Project project) throws Exception {
        setProjectName(project.getName());
        setProjectId(project.getId());
    }

    private void populate(Session session, Iteration iteration) throws Exception {
        Project project = (Project)session.load(Project.class, new Integer(iteration.getProject().getId()));
        populate(project);
        setIterationName(iteration.getName());
        setIterationId(iteration.getId());
    }

    private void populate(Session session, UserStory story) throws Exception {
        List results = session.find(
                "select p.name, p.id, i.name, i.id " +
                "from net.sf.xplanner.domain.Project as p, " +
                "net.sf.xplanner.domain.Iteration as i, " +
                "net.sf.xplanner.domain.UserStory as s  " +
                "where s.id = ? and  s.iteration = i and i.project = p",
                new Integer(story.getId()), Hibernate.INTEGER);
        if (results.size() > 0) {
            Object[] result = (Object[])results.iterator().next();
            setProjectName((String)result[0]);
            setProjectId(((Integer)result[1]).intValue());
            setIterationName((String)result[2]);
            setIterationId(((Integer)result[3]).intValue());
        }
        setStoryName(story.getName());
        setStoryId(story.getId());
    }

    private void populate(Session session, Task task) throws Exception {
        List results = session.find(
                "select p.name, p.id, i.name, i.id, s.name, s.id " +
                "from net.sf.xplanner.domain.Project as p, " +
                "net.sf.xplanner.domain.Iteration as i, " +
                "net.sf.xplanner.domain.UserStory as s, " +
                "net.sf.xplanner.domain.Task as t " +
                "where t.id = ? and t.userStory.id = s.id and s.iteration = i and i.project = p",
                new Integer(task.getId()), Hibernate.INTEGER);
        if (results.size() > 0) {
            Object[] result = (Object[])results.iterator().next();
            setProjectName((String)result[0]);
            setProjectId(((Integer)result[1]).intValue());
            setIterationName((String)result[2]);
            setIterationId(((Integer)result[3]).intValue());
            setStoryName((String)result[4]);
            setStoryId(((Integer)result[5]).intValue());
        }
        setTaskName(task.getName());
        setTaskId(task.getId());
    }

    private void populate(Session session, TimeEntry timeEntry) throws Exception {
        List results = session.find(
                "select p.name, p.id, i.name, i.id, s.name, s.id, t.name " +
                "from net.sf.xplanner.domain.Project as p, " +
                "net.sf.xplanner.domain.Iteration as i, " +
                "net.sf.xplanner.domain.UserStory as s, " +
                "net.sf.xplanner.domain.Task as t " +
                "where t.id = ? and t.userStory.id = s.id and s.iteration = i and i.project = p",
                new Integer(timeEntry.getTask().getId()), Hibernate.INTEGER);
        if (results.size() > 0) {
            Object[] result = (Object[])results.iterator().next();
            setProjectName((String)result[0]);
            setProjectId(((Integer)result[1]).intValue());
            setIterationName((String)result[2]);
            setIterationId(((Integer)result[3]).intValue());
            setStoryName((String)result[4]);
            setStoryId(((Integer)result[5]).intValue());
            setTaskName(((String)result[6]));
        }
        setTaskId(timeEntry.getId());
    }

    private void populate(Session session, Feature feature) throws Exception {
        List results = session.find(
                "select p.name, p.id, i.name, i.id, s.name, s.id " +
                "from net.sf.xplanner.domain.Project as p, " +
                "net.sf.xplanner.domain.Iteration as i, " +
                "net.sf.xplanner.domain.UserStory as s, " +
                "net.sf.xplanner.domain.Feature as f " +
                "where f.id = ? and f.userStory.id = s.id and s.iteration = i and i.project = p",
                new Integer(feature.getId()), Hibernate.INTEGER);
        if (results.size() > 0) {
            Object[] result = (Object[])results.iterator().next();
            setProjectName((String)result[0]);
            setProjectId(((Integer)result[1]).intValue());
            setIterationName((String)result[2]);
            setIterationId(((Integer)result[3]).intValue());
            setStoryName((String)result[4]);
            setStoryId(((Integer)result[5]).intValue());
        }
        setFeatureName(feature.getName());
        setFeatureId(feature.getId());
    }

    private void populate(Note note) throws Exception {
        populate(getNoteTarget(note.getAttachedToId()));
    }

    public static DomainObject getNoteTarget(int attachedToId) {
       try {
          return idSearchHelper.search(attachedToId);
       } catch (HibernateException e) {
          return null;
       }
    }

    public void save(ServletRequest request) {
        request.setAttribute(REQUEST_KEY, this);
    }

    public static DomainContext get(ServletRequest request) {
        return (DomainContext)request.getAttribute(REQUEST_KEY);
    }
}
