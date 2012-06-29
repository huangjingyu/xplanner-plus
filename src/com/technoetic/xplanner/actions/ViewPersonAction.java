package com.technoetic.xplanner.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.dao.TaskDao;
import net.sf.xplanner.dao.UserStoryDao;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Task;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.domain.repository.TaskRepository;

/**
 * User: Mateusz Prokopowicz
 * Date: Jul 5, 2005
 * Time: 2:45:56 PM
 */
public class ViewPersonAction extends ViewObjectAction<Person>{
   private TaskDao taskDao;
   private UserStoryDao userStoryDao;
   
   TaskRepository taskRepository;

   public void setTaskRepository(TaskRepository taskRepository) {
      this.taskRepository = taskRepository;
   }


   protected ActionForward doExecute(ActionMapping actionMapping,
                                     ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse reply) throws Exception {
       
      int personId = Integer.parseInt(request.getParameter("oid"));
      
      // grab a list of all active tasks
      List<Task> tasks = taskDao.getCurrentTasksForPerson(personId);
      Person person = getCommonDao().getById(Person.class, personId);
      request.setAttribute("person", person);
      // use the utility methods on the task repository to filter the results
      request.setAttribute("currentActiveTasksForPerson", taskRepository.getCurrentActiveTasks(tasks));
      request.setAttribute("currentPendingTasksForPerson", taskRepository.getCurrentPendingTasks(tasks));
      request.setAttribute("currentCompletedTasksForPerson", taskRepository.getCurrentCompletedTasks(tasks));
      request.setAttribute("futureTasksForPerson", taskRepository.getFutureTasksForPerson(personId));
      request.setAttribute("storiesForCustomer", userStoryDao.getStoriesForPersonWhereCustomer(personId));
      request.setAttribute("storiesForTracker", userStoryDao.getStoriesForPersonWhereTracker(personId));
      
      return super.doExecute(actionMapping, form, request, reply);
   }

	public void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
	}

	public void setUserStoryDao(UserStoryDao userStoryDao) {
		this.userStoryDao = userStoryDao;
	}
   
}
