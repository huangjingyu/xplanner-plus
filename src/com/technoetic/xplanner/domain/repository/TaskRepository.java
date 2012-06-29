package com.technoetic.xplanner.domain.repository;

import java.util.Collection;
import java.util.Date;

/*
 * A repository that can be used to access collections of tasks
 * based on certain criteria.
 * 
 * @author James Beard
 */
public interface TaskRepository extends ObjectRepository {
   
   /*
    * Returns a collection of tasks in current iterations where personId 
    * is the acceptor.
    * 
    * @param  personId    the id of the acceptor
    * @return             the collection of tasks
    */
   public Collection getCurrentTasksForPerson(int personId);
   
   /*
    * Returns a collection of tasks in current iterations where personId 
    * is the acceptor, and the task has been started.
    * 
    * @param  personId    the id of the acceptor
    * @return             the collection of tasks
    */
   public Collection getCurrentActiveTasksForPerson(int personId);
   
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
   public Collection getCurrentActiveTasks(Collection tasks);
   
   /*
    * Returns a collection of tasks in current iterations where personId 
    * is the acceptor, and the task hasn't been started yet.
    * 
    * @param  personId    the id of the acceptor
    * @return             the collection of tasks
    */
   public Collection getCurrentPendingTasksForPerson(int personId);

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
   public Collection getCurrentPendingTasks(Collection tasks);
   
   /*
    * Returns a collection of tasks in current iterations where personId 
    * is the acceptor, and the task has already been completed.
    * 
    * @param  personId    the id of the acceptor
    * @return             the collection of tasks
    */
   public Collection getCurrentCompletedTasksForPerson(int personId);

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
   public Collection getCurrentCompletedTasks(Collection tasks);
   
   /*
    * Returns a collection of tasks in future iterations where personId 
    * is the acceptor, and the task has not been completed.
    * 
    * @param  personId    the id of the acceptor
    * @return             the collection of tasks
    */
   public Collection getFutureTasksForPerson(int personId);
   
   public Collection getProjectLeadsEmailNotification(Date date);
   
}
