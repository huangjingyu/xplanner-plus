package com.technoetic.xplanner.actions;

import java.util.ArrayList;
import java.util.List;

import net.sf.xplanner.domain.History;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import com.technoetic.xplanner.domain.TaskDisposition;
import com.technoetic.xplanner.security.SecurityHelper;

public class TestTaskContinuer extends ContinuerBaseTestCase {
   private UserStory story1;
   private UserStory story2;
   private Task taskToMoveOrContinue;
   private static final String TASK_NAME = "Test Task";

   protected void setUp() throws Exception {
      super.setUp();
      setUpContinuers();
      taskToMoveOrContinue =
            createTask(((Integer) (support.hibernateSession.saveIds[0])).intValue(),
                       1010,
                       5.0,
                       2.0,
                       createTimeEntries());
      taskToMoveOrContinue.setName(TASK_NAME);
      support.hibernateSession.save(taskToMoveOrContinue);
      List<Task> tasks = new ArrayList<Task>();
      tasks.add(taskToMoveOrContinue);

//       setUpThreadSession(); // AbstractUnitTestCase.mockSession
      /**
       * use hibernate support.hibernate session instead of AbstractUnitTestCase.mockSession
       */
//       ThreadSession.set(support.hibernateSession); // hibernate.session

      story1 = new UserStory();
      story1.setTasks(tasks);
      story1.setName("name");
      story1.setCustomer(new Person());
      story1.setDescription("A description.");
      support.hibernateSession.save(story1);

      story2 = new UserStory();
      story2.setName("story2 name");
      support.hibernateSession.save(story2);
      support.hibernateSession.loadAddReturnByClassById(story2.getId(), story2);
      assertTrue(story2.getId() != 0);
      Iteration iteration = new Iteration();
      List<UserStory> stories = new ArrayList<UserStory>();
      stories.add(story1);
      stories.add(story2);
      iteration.setUserStories(stories);
      support.hibernateSession.save(iteration);
      story1.setIteration(iteration);
      story2.setIteration(iteration);
      support.hibernateSession.loadAddReturnByClassById(iteration.getId(), iteration);
   }

   public void testTaskContinuation() throws Exception {
      taskToMoveOrContinue.setCompleted(false);
      taskToMoveOrContinue.setUserStory(story1);

      createNotesListFor(taskToMoveOrContinue);

      putContentOnMockQuery();

      Iteration iteration = (Iteration) metaDataRepository.getParent(story2);
      assertNotNull("Test not set up correctly", iteration);

      taskContinuer.init(support.hibernateSession, support.resources, SecurityHelper.getRemoteUserId(support.request));
      Task continuedTask = (Task) taskContinuer.continueObject(taskToMoveOrContinue, story1, story2);

      assertHistoryInObject(continuedTask,
                                    "Continued from task:" +
                                    taskToMoveOrContinue.getId() +
                                    " in story:" +
                                    story1.getId(),
                                    History.CONTINUED);
      assertHistoryInObject(taskToMoveOrContinue,
                                    "Continued as task:" + continuedTask.getId() + " in story:" + story2.getId(),
                                    History.CONTINUED);
      assertHistoryInObject(story2,
                                    "Continued task:" +
                                    continuedTask.getId() +
                                    " from task:" +
                                    taskToMoveOrContinue.getId() +
                                    " in story:" +
                                    story1.getId(),
                                    History.CONTINUED);
      assertHistoryInObject(story1,
                                    "Continued task:" +
                                    taskToMoveOrContinue.getId() +
                                    " as task:" +
                                    continuedTask.getId() +
                                    " in story:" +
                                    story2.getId(),
                                    History.CONTINUED);

      List tasks = getSavedInstancesOf(Task.class);
      assertEquals("# of tasks", 2, tasks.size());

      verifyNotesBeingContinued(1);
      assertTaskProperties(taskToMoveOrContinue, continuedTask);
      assertEquals("original task disposition", TaskDisposition.PLANNED.getCode(), taskToMoveOrContinue.getDisposition());
      assertEquals("continued task disposition", TaskDisposition.CARRIED_OVER.getCode(), continuedTask.getDisposition());
   }

}
