package com.technoetic.xplanner.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

import net.sf.xplanner.domain.Task;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.classic.Session;
import org.hibernate.type.Type;

import com.technoetic.xplanner.db.hibernate.ThreadSession;

public class CurrentTaskQuery {
    private final Logger log = Logger.getLogger(getClass());
    private static String query;
    private Collection tasksInProgress;
    private Collection completedTasks;
    private Collection tasks;
    private int personId;

    private java.util.Collection getTasks() throws QueryException {
        if (personId == 0) {
            throw new QueryException("no person specified for query");
        }
        Session session = ThreadSession.get();
        if (tasks == null) {
            try {
                if (session == null) {
                    log.error("no Hibernate session provided, ignoring "+this);
                    return Collections.EMPTY_LIST;
                }
                try {
                    if (query == null) {
                        query =
                                "select distinct task "
                                + " from task in class net.sf.xplanner.domain.Task, "
                                + " time_entry in class net.sf.xplanner.domain.TimeEntry, "
                                + " iteration in class net.sf.xplanner.domain.Iteration, "
                                + " story in class net.sf.xplanner.domain.UserStory,"
                                + " person in class net.sf.xplanner.domain.Person "
                                + " where task.id = time_entry.task.id and"
                                + " task.userStory.id = story.id and story.iteration.id = iteration.id and"
                                + " (iteration.startDate <= ? and iteration.endDate >= ?) and"
                                + " (time_entry.person1Id = person.id or time_entry.person2Id = person.id)"
                                + " and person.id = ?";
                    }
                    Date now = new Date();
                    tasks =
                            session.find(
                                    query,
                                    new Object[]{now, now, new Integer(personId)},
                                    new Type[]{Hibernate.DATE, Hibernate.DATE, Hibernate.INTEGER});
                } catch (Exception ex) {
                    log.error("query error", ex);
                } finally {
                    session.connection().rollback();
                }
            } catch (Exception ex) {
                log.error("error in CurrentTaskQuery", ex);
            }
        }
        return tasks;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getPersonId() {
        return personId;
    }

    public Collection getCompletedTasks() throws Exception {
        if (completedTasks == null) {
            completedTasks = new ArrayList();
            Iterator taskItr = getTasks().iterator();
            while (taskItr.hasNext()) {
                Task task = (Task)taskItr.next();
                if (task.isCompleted()) {
                    completedTasks.add(task);
                }
            }
        }
        return completedTasks;
    }

    public Collection getTasksInProgress() throws QueryException {
        if (tasksInProgress == null) {
            tasksInProgress = new ArrayList();
            Iterator taskItr = getTasks().iterator();
            while (taskItr.hasNext()) {
                Task task = (Task)taskItr.next();
                if (!task.isCompleted()) {
                    tasksInProgress.add(task);
                }
            }
        }
        return tasksInProgress;
    }
}
