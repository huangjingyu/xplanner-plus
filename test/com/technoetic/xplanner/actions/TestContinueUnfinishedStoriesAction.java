package com.technoetic.xplanner.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import com.technoetic.xplanner.domain.IterationStatus;

public class TestContinueUnfinishedStoriesAction extends ContinuerBaseTestCase {
   private ContinueUnfinishedStoriesAction action;

   protected void setUp() throws Exception {
      super.setUp();
      setUpContinuers();

      action = new ContinueUnfinishedStoriesAction();
      action.setStoryContinuer(storyContinuer);

      setUpTasksStoryAndIteration();
      addACompleteStoryToIteration();
      setUpTargetIteration();
      putContentOnMockQuery();
      support.hibernateSession.save(targetIteration);
   }

   private void addACompleteStoryToIteration() {
      UserStory completedStory = new UserStory();

      List completedTasks = new ArrayList();

      Task newCompletedTask = new Task();
      newCompletedTask.setCompleted(true);
      completedTasks.add(newCompletedTask);
      completedStory.setTasks(completedTasks);
      completedStory.setIteration(iteration);
      assertTrue(completedStory.isCompleted());
      iteration.getUserStories().add(completedStory);
   }

   public void testContinueAllUnfinishedStories() throws Exception {

      verifyOriginalIteration();
      assertEquals("stories in target iteration", 0, targetIteration.getUserStories().size());

      action.continueUnfinishedStoriesInIteration(support.request,
                                                          support.hibernateSession,
                                                          iteration, targetIteration);

      verifyOriginalIteration();
      verifyTargetIterationAfterContinuation();
   }

   private void verifyOriginalIteration() {
      Collection originalIterationStories = iteration.getUserStories();
      assertEquals("number of stories in original iteration", 2, originalIterationStories.size());
      UserStory originalStory = (UserStory) originalIterationStories.iterator().next();
      assertEquals("number of tasks in original story", 3, originalStory.getTasks().size());
      assertFalse(originalStory.isCompleted());
   }

   private void verifyTargetIterationAfterContinuation() {
      assertEquals("original iteration status", IterationStatus.INACTIVE_KEY, iteration.getStatusKey());
      Collection targetIterationStories = targetIteration.getUserStories();
      assertEquals("stories in target iteration " + targetIterationStories, 1, targetIterationStories.size());
      UserStory continuedStory = (UserStory) targetIterationStories.iterator().next();
      verifyContinuedStory(continuedStory);
   }

}
