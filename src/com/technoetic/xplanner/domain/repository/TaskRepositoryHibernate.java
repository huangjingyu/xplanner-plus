package com.technoetic.xplanner.domain.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.sf.xplanner.domain.Task;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.HibernateException;

/*
 * A Hibernate implementation of the TaskRepository interface.
 * 
 * Implementation is based pretty heavily on (and should 
 * eventually supercede) TaskQueryHelper.
 * 
 * @author James Beard
 * @see    com.technoetic.xplanner.db.TaskQueryHelper
 */
public class TaskRepositoryHibernate extends HibernateObjectRepository implements TaskRepository {
   public static final String EMAIL_TO_LEADS_QUERY = "net.sf.xplanner.domain.TimeEntryEmailNotificationToProjectSpecificLeads";
   public static final String EMAIL_TO_ACCEPTORS_QUERY = "net.sf.xplanner.domain.TimeEntryEmailNotificationToAcceptors";

   /*
    * A implementation of the Predicate interface to filter
    * collections of tasks based on whether they are completed
    * and/or active.
    * 
    * @author James Beard
    */
   private class TaskStatusFilter implements Predicate {
      Boolean isCompleted;
      Boolean isActive;

      public TaskStatusFilter(Boolean isCompleted, Boolean isActive) {
         this.isCompleted = isCompleted;
         this.isActive = isActive;
      }

      public boolean evaluate(Object o) {
         final Task task = (Task) o;
         return (isCompleted == null || isCompleted.booleanValue() == task.isCompleted()) &&
                (isActive == null || isActive.booleanValue() == task.getTimeEntries().size() > 0);
      }
   }

   public TaskRepositoryHibernate() throws HibernateException {
       super(Task.class);
   }
   
   /*
    * Returns a collection of tasks in current iterations where personId 
    * is the acceptor, and the task has been started.
    * 
    * @param  personId    the id of the acceptor
    * @return             the collection of tasks
    */
   public Collection getCurrentActiveTasksForPerson(int personId) {
      return getCurrentActiveTasks(getCurrentTasksForPerson(personId));
   }
   
   /*
    * Filters a collection of tasks for those in current iterations
    * that have been started.
    * 
    * Can be used to further filter a cached Collection of all the tasks for
    * a particular person.
    * 
    * @param tasks the collection of tasks
    * @return      the filtered collection of tasks
    */
   public Collection getCurrentActiveTasks(Collection tasks) {
       return CollectionUtils.select(tasks,
               new TaskStatusFilter(Boolean.FALSE, Boolean.TRUE));
   }

   /*
    * Returns a collection of tasks in current iterations where personId 
    * is the acceptor, and the task hasn't been started yet.
    * 
    * @param  personId    the id of the acceptor
    * @return             the collection of tasks
    */
   public Collection getCurrentPendingTasksForPerson(int personId) {
      return getCurrentPendingTasks(getCurrentTasksForPerson(personId));
   }

   /*
    * Filters a collection of tasks for those in current iterations
    * that haven't been started yet.
    * 
    * Can be used to further filter a cached Collection of all the tasks for
    * a particular person. 
    * 
    * @param tasks the collection of tasks
    * @return      the filtered collection of tasks
    */
   public Collection getCurrentPendingTasks(Collection tasks) {
       return CollectionUtils.select(tasks,
               new TaskStatusFilter(Boolean.FALSE, Boolean.FALSE));
   }

   /*
    * Returns a collection of tasks in current iterations where personId 
    * is the acceptor, and the task has already been completed.
    * 
    * @param  personId    the id of the acceptor
    * @return             the collection of tasks
    */
   public Collection getCurrentCompletedTasksForPerson(int personId) {
      return getCurrentCompletedTasks(getCurrentTasksForPerson(personId));
   }

   /*
    * Filters a collection of tasks for those in current iterations
    * that have already been completed.
    * 
    * Can be used to further filter a cached Collection of all the tasks for
    * a particular person. 
    * 
    * @param tasks the collection of tasks
    * @return      the filtered collection of tasks
    */
   public Collection getCurrentCompletedTasks(Collection tasks) {
       return CollectionUtils.select(tasks,
               new TaskStatusFilter(Boolean.TRUE, null));
   }

   /*
    * Returns a collection of tasks in future iterations where personId 
    * is the acceptor, and the task has not been completed.
    * 
    * @param  personId    the id of the acceptor
    * @return             the collection of tasks
    */
   public Collection getFutureTasksForPerson(int personId) {
      return queryTasks("tasks.planned.future", personId);
   }

   public Collection getProjectLeadsEmailNotification(Date date) {
      return getHibernateTemplate().findByNamedQuery(EMAIL_TO_LEADS_QUERY, date);
   }

   /*
    * Returns a collection of tasks in current iterations where personId 
    * is the acceptor.
    * 
    * @param  personId    the id of the acceptor
    * @return             the collection of tasks
    */
   public Collection getCurrentTasksForPerson(int personId) {
      Collection currentTasks = queryTasks("tasks.current.accepted", personId);
      currentTasks.addAll(queryTasks("tasks.current.worked", personId));
      
      return currentTasks;
   }

   private List queryTasks(String queryName, int personId) {
      return getHibernateTemplate().findByNamedQueryAndNamedParam(
            queryName,
            new String[]{"now", "personId"},
            new Object[]{new Date(), new Integer(personId)});
   }

}
