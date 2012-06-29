package com.technoetic.xplanner.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.TimeEntry;
import net.sf.xplanner.domain.UserStory;

public class TestIteration extends TestCase {
    Iteration iteration = new Iteration();

    public void testGetActualHoursWithNoStories() throws Exception {
        assertEquals(0.0d, iteration.getCachedActualHours(),0.0);
    }

    public void testGetEstimatedHoursWithNoStories() throws Exception {
        assertEquals(0.0d, iteration.getEstimatedHours(),0.0);
    }

    public void testGetAdjustedEstimatedHoursWithNoStories() throws Exception {
        assertEquals(0.0d, iteration.getAdjustedEstimatedHours(),0.0);
    }

    public void testGetRemainingHours() throws Exception {
        assertEquals(0.0d, iteration.getTaskRemainingHours(),0.0);
    }

    public void testGetEstimatedOriginalHours() throws Exception {
      assertEquals(0.0d, iteration.getTaskEstimatedOriginalHours(),0.0);
        List<UserStory> stories = new ArrayList<UserStory>();
        UserStory story = new UserStory();
        //start iteration
        story.setEstimatedOriginalHours(new Double(10.0));
        stories.add(story);
        List<Task> tasks = new ArrayList();
        Task task = new Task();
        task.setUserStory(story);
        task.setEstimatedOriginalHours(3);
        tasks.add(task);
        story.setTasks(tasks);
        iteration.setUserStories(stories);
      assertEquals(3.0d, iteration.getTaskEstimatedOriginalHours(),0.0);
        task.setDisposition(TaskDisposition.ADDED);
      assertEquals(0.0d, iteration.getTaskEstimatedOriginalHours(),0.0);
    }

    public void testGetOverestimatedHours() throws Exception {
        Iteration iteration = setUpTestIteration();
        assertEquals(7.0d, iteration.getTaskOverestimatedHours(),0.0);
    }

    public void testGetOverestimatedOriginalHours() throws Exception {
        Iteration iteration = setUpTestIteration();
        assertEquals(4.0d, iteration.getOverestimatedOriginalHours(),0.0);
    }

    public void testGetUnderestimatedHours() throws Exception {
        Iteration iteration = setUpTestIteration();
        assertEquals(1.0d, iteration.getTaskUnderestimatedOriginalHours(),0.0);
    }

   public void testGetUnderestimatedOriginalHours() throws Exception {
        Iteration iteration = setUpTestIteration();
        assertEquals(1.0d, iteration.getUnderestimatedOriginalHours(),0.0);
    }

   public void testGetAddedHours() throws Exception {
        Iteration iteration = setUpTestIteration();
        assertEquals(5.0d, iteration.getEstimatedHoursOfAddedTasks(),0.0);
    }

   public void testGetAddedOrDiscoveredOriginalHours() throws Exception {
        Iteration iteration = setUpTestIteration();
        assertEquals(5.0d, iteration.getAddedOriginalHours(),0.0);
    }

   public void testGetPostponedHours() throws Exception {
        Iteration iteration = setUpTestIteration();
        assertEquals(2.0d, iteration.getPostponedHours(),0.0);
    }

   public void testIsFuture() throws Exception
    {
        Iteration iteration = new Iteration();
        iteration.setStartDate(new Date(System.currentTimeMillis() - 7200000));
        assertFalse(iteration.isFuture());
        iteration.setStartDate(new Date(System.currentTimeMillis() + 7200000));
        assertTrue(iteration.isFuture());
    }

   public void testIsActive() throws Exception
    {
        Iteration iteration = new Iteration();
        iteration.setIterationStatus(IterationStatus.INACTIVE);
        assertFalse(iteration.isActive());
    }

   public void testIsCurrent() throws Exception
    {
        Iteration iteration = new Iteration();
        iteration.setStartDate(new Date(System.currentTimeMillis() - 2000));
        iteration.setEndDate(new Date(System.currentTimeMillis() - 1000));
        assertFalse("Past iteration", iteration.isCurrent());
        iteration.setStartDate(new Date(System.currentTimeMillis() - 1000));
        iteration.setEndDate(new Date(System.currentTimeMillis() + 1000));
        assertTrue("Current iteration", iteration.isCurrent());
        iteration.setStartDate(new Date(System.currentTimeMillis() + 1000));
        iteration.setEndDate(new Date(System.currentTimeMillis() + 2000));
        assertFalse("Future iteration", iteration.isCurrent());
    }

   public void testGetCompletedOriginalHours() throws Exception {
      Iteration iteration = setUpTestIteration();
      assertEquals(9.0, iteration.getCompletedOriginalHours(), 0.0);
   }

   public void testGetCompletedHours() throws Exception {
      Iteration iteration = setUpTestIteration();
      assertEquals(6.0, iteration.getTaskActualCompletedHours(), 0.0);
   }

   private Iteration setUpTestIteration() throws Exception {
      Iteration iteration = new Iteration();
      iteration.setIterationStatus(IterationStatus.INACTIVE);

      List tasks1 = new ArrayList();
      List tasks2 = new ArrayList();
      ArrayList twoHourTimeEntry = getTimeEntriesForDurationInHours(2);

      UserStory story1 = new UserStory();
      story1.setDisposition(StoryDisposition.ADDED);
      story1.setEstimatedOriginalHours(new Double(10.0));

      UserStory story2 = new UserStory();
      story2.setDisposition(StoryDisposition.CARRIED_OVER);
      story2.setPostponedHours(2.0d);
      story2.setEstimatedOriginalHours(new Double(10.0));

      Task task1 = new Task();
      task1.setEstimatedHours(1.0);
      task1.setTimeEntries(twoHourTimeEntry);
      task1.setUserStory(story1);
      tasks1.add(task1);

      tasks2.add(createTask(4.0d, twoHourTimeEntry, TaskDisposition.PLANNED, story2));
      tasks2.add(createTask(4.0d, twoHourTimeEntry, TaskDisposition.DISCOVERED, story2));

      story1.setTasks(tasks1);

      story2.setTasks(tasks2);

      List userStories = new ArrayList();
      userStories.add(story1);
      userStories.add(story2);
      iteration.setUserStories(userStories);
      iteration.start();
      task1.setEstimatedHours(5.0);
      task1.setCompleted(true);
      task1.setDisposition(TaskDisposition.ADDED);
      return iteration;
   }

   private Task createTask(double estimatedHours, ArrayList twoHourTimeEntry, TaskDisposition disposition) {
      Task task2 = new Task();
      task2.setEstimatedHours(estimatedHours);
      task2.setTimeEntries(twoHourTimeEntry);
      task2.setCompleted(true);
      task2.setDisposition(disposition);
      return task2;
   }

   private Task createTask(double estimatedHours, ArrayList twoHourTimeEntry, TaskDisposition disposition, UserStory story) {
      Task task2 = createTask(estimatedHours, twoHourTimeEntry, disposition);
      task2.setUserStory(story);
      return task2;
   }

   private ArrayList getTimeEntriesForDurationInHours(int duration) {
      long now = new Date().getTime();
      TimeEntry timeEntry = new TimeEntry();
      timeEntry.setStartTime(new Date(now - 7200000));
      timeEntry.setEndTime(new Date(now));
      timeEntry.setDuration(duration);
      ArrayList timeEntries = new ArrayList();
      timeEntries.add(timeEntry);
      return timeEntries;
   }
}