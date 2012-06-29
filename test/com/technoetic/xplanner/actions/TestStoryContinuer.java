/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.technoetic.xplanner.actions;

import java.util.ArrayList;
import java.util.List;

import net.sf.xplanner.domain.History;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

public class TestStoryContinuer extends ContinuerBaseTestCase {

   protected void setUp() throws Exception {
      super.setUp();
      setUpTasksStoryAndIteration();
      setUpTargetIteration();
      setUpContinuers();
      support.hibernateSession.save(targetIteration);

   }

   public void testStoryContinuation() throws Exception {

      putContentOnMockQuery();

      UserStory continuedStory = (UserStory) storyContinuer.continueObject(story, iteration, targetIteration);
      List tasks = new ArrayList(continuedStory.getTasks());
      assertEquals("continued tasks in continued story", 2, tasks.size());
      Task continuedTask1 = (Task) tasks.get(0);
      Task continuedTask2 = (Task) tasks.get(1);

      assertHistoryInObject(incompleteTask1, "Continued as task:" + continuedTask1.getId() + " in story:" +  continuedStory.getId(),History.CONTINUED);
      assertHistoryInObject(incompleteTask2, "Continued as task:" + continuedTask2.getId() + " in story:" +  continuedStory.getId(),History.CONTINUED);
      assertHistoryInObject(story, "Continued task:" + incompleteTask1.getId() + " as task:" + continuedTask1.getId() + " in story:" + continuedStory.getId(), History.CONTINUED);
      assertHistoryInObject(story, "Continued task:" + incompleteTask2.getId() + " as task:" + continuedTask2.getId() + " in story:" + continuedStory.getId(), History.CONTINUED);
      assertHistoryInObject(iteration, "Continued story:" + story.getId() + " as story:" + continuedStory.getId() + " in iteration:" + targetIteration.getId(), History.CONTINUED);

      assertHistoryInObject(targetIteration, "Continued story:" + continuedStory.getId() + " from story:" + story.getId() + " in iteration:" + iteration.getId(), History.CONTINUED);
      assertHistoryInObject(continuedStory,  "Continued task:" + continuedTask1.getId() + " from task:" + incompleteTask1.getId() + " in story:" +  story.getId(),History.CONTINUED);
      assertHistoryInObject(continuedStory,  "Continued task:" + continuedTask2.getId() + " from task:" + incompleteTask2.getId() + " in story:" +  story.getId(),History.CONTINUED);
      assertHistoryInObject(continuedTask1, "Continued from task:" + incompleteTask1.getId() + " in story:" +  story.getId(),History.CONTINUED);
      assertHistoryInObject(continuedTask2, "Continued from task:" + incompleteTask2.getId() + " in story:" +  story.getId(),History.CONTINUED);

      assertFalse("Story should not been completed", story.isCompleted());
      verifyNotesBeingContinued(2);
      verifyContinuedStory(continuedStory);
   }

}