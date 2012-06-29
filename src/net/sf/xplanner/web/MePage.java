package net.sf.xplanner.web;

import java.util.List;

import net.sf.xplanner.dao.TaskDao;
import net.sf.xplanner.dao.UserStoryDao;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.technoetic.xplanner.domain.repository.TaskRepository;

@Controller
public class MePage extends BasePage<Person> {
	@Autowired
	private TaskDao taskDao;
	@Autowired
	private UserStoryDao userStoryDao;
	@Autowired
	private TaskRepository taskRepository;

	
	
	@RequestMapping("/me/status/{id}")
	public ModelAndView list(@PathVariable Integer id) {
		ModelAndView modelAndView = getModelAndView("view/meStatus", id);
		// grab a list of all active tasks
		List<Task> tasks = taskDao.getCurrentTasksForPerson(id);

		modelAndView.addObject("currentActiveTasksForPerson", taskRepository.getCurrentActiveTasks(tasks));
		modelAndView.addObject("currentPendingTasksForPerson", taskRepository.getCurrentPendingTasks(tasks));
		modelAndView.addObject("currentCompletedTasksForPerson", taskRepository.getCurrentCompletedTasks(tasks));
		modelAndView.addObject("futureTasksForPerson", taskRepository.getFutureTasksForPerson(id));
		modelAndView.addObject("storiesForCustomer", userStoryDao.getStoriesForPersonWhereCustomer(id));
		modelAndView.addObject("storiesForTracker", userStoryDao.getStoriesForPersonWhereTracker(id));
		return modelAndView;
	}

	public void setTaskRepository(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}
	public void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
	}

	public void setUserStoryDao(UserStoryDao userStoryDao) {
		this.userStoryDao = userStoryDao;
	}
//	@RequestMapping("/{objectType}/edit/{objectId}")
//	public String edit(@PathVariable String objectType, @PathVariable Integer objectId){
//		System.out.println(objectType);
//		return objectType;
//	}
}
