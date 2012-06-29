package com.technoetic.xplanner.db;

import java.util.Collection;
import java.util.Date;

/**
 * TODO move to task repository.
 * 
 * @see com.technoetic.xplanner.domain.repository.TaskRepositoryHibernate
 */
public class TaskQueryHelper {
   public static final String EMAIL_TO_LEADS_QUERY = "com.technoetic.xplanner.domain.TimeEntryEmailNotificationToProjectSpecificLeads";
   public static final String EMAIL_TO_ACCEPTORS_QUERY = "com.technoetic.xplanner.domain.TimeEntryEmailNotificationToAcceptors";

   private Collection cache = null;
   private int personId;

   private TaskQuery taskQuery;

   public TaskQueryHelper() {
   }

   public TaskQueryHelper(TaskQuery taskQuery) {
      this.taskQuery = taskQuery;
   }

   public void setPersonId(int personId) {
      this.personId = personId;
   }

   public Collection getCurrentActiveTasksForPerson() {
      return taskQuery.query(cache, personId, Boolean.FALSE, Boolean.TRUE);
   }

   public Collection getCurrentPendingTasksForPerson() {
      return taskQuery.query(cache, personId, Boolean.FALSE, Boolean.FALSE);
   }

   public Collection getCurrentCompletedTasksForPerson() {
      return taskQuery.query(cache, personId, Boolean.TRUE, null);
   }

   public Collection getFutureTasksForPerson() {
      return taskQuery.queryTasks("tasks.planned.future", personId);
   }

   public Collection getTaskAcceptorsEmailNotification(Date date) {
      return taskQuery.queryTasks(EMAIL_TO_ACCEPTORS_QUERY, date);
   }

   public Collection getProjectLeedsEmailNotification(Date date) {
      return taskQuery.queryTasks(EMAIL_TO_LEADS_QUERY, date);
   }

   public void setTaskQuery(TaskQuery taskQuery) {
      this.taskQuery = taskQuery;
   }
}
