package com.technoetic.xplanner.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import junit.extensions.FieldAccessor;
import junit.framework.TestCase;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.TimeEntry;
import net.sf.xplanner.domain.UserStory;

public class TestUserStory extends TestCase {
   public static final int ONE_HOUR = 60 * 60 * 1000;
   public static final int TWO_HOURS = 2 * ONE_HOUR;

   public TestUserStory(String name) {
      super(name);
   }

   private UserStory story;
   private Task task1;
   private Task task2;
   private Task task3;
   private List<Task> tasks;

   protected void setUp() throws Exception {
      super.setUp();
      List<net.sf.xplanner.domain.TimeEntry> twoActualHours = createTimeEntriesForTwoHoursOfWork();
      story = new UserStory();

      task1 = newTask(2.0, 1.0, twoActualHours, story);
      task2 = newTask(3.0, 4.0, twoActualHours, story);
      task3 = newTask(4.0, 6.0, new ArrayList<net.sf.xplanner.domain.TimeEntry>(), story);
      tasks = new ArrayList<Task>();
      tasks.add(task1);
      tasks.add(task2);
      tasks.add(task3);
      story.setTasks(tasks);
   }

   private Task newTask(double estimatedOriginalHours, double currentEstimatedHours, List<net.sf.xplanner.domain.TimeEntry> timeEntries) {
      Task task = new Task();
      task.setEstimatedHours(currentEstimatedHours);
      task.setEstimatedOriginalHours(estimatedOriginalHours);
      task.setTimeEntries(timeEntries);
      return task;
   }

   private Task newTask(double estimatedOriginalHours,
                        double currentEstimatedHours,
                        List<net.sf.xplanner.domain.TimeEntry> timeEntries,
                        UserStory story1) {
      Task task = newTask(estimatedOriginalHours, currentEstimatedHours, timeEntries);
      task.setUserStory(story1);
      return task;
   }

   private List<net.sf.xplanner.domain.TimeEntry> createTimeEntriesForTwoHoursOfWork() {
      long now = new Date().getTime();
      net.sf.xplanner.domain.TimeEntry t1 = new net.sf.xplanner.domain.TimeEntry();
      t1.setStartTime(new Date(now - ONE_HOUR));
      t1.setEndTime(new Date(now));
      net.sf.xplanner.domain.TimeEntry t2 = new net.sf.xplanner.domain.TimeEntry();
      t2.setStartTime(new Date(now + ONE_HOUR));
      t2.setEndTime(new Date(now + TWO_HOURS));
      List<net.sf.xplanner.domain.TimeEntry> timeEntries = new ArrayList<net.sf.xplanner.domain.TimeEntry>();
      timeEntries.add(t1);
      timeEntries.add(t2);
      return timeEntries;
   }

   public void testGetEstimatedHours() {
      assertEstimatedHours(12.0);
   }

   public void testGetEstimatedHours_CompletedStory() {
      completeAllTasks();
      assertEstimatedHours(4.0);
   }

   private void assertEstimatedHours(double expectedEstimatedHours) {
      assertEquals("wrong estimated hours", expectedEstimatedHours, story.getEstimatedHours(), 0);
   }

   public void testStoryEstimatedHoursFieldNotChanged() {
      story.setEstimatedHoursField(1.0);

      Double storyHours = (Double) FieldAccessor.get(story, UserStory.ESTIMATED_HOURS);
      assertEquals("original story estimate changed", 1.0, storyHours.doubleValue(), 0);
      assertEstimatedHours(12.0);
   }

   @SuppressWarnings("unchecked")
public void testGetEstimatedOriginalHoursInNotStartedIteration() {
      story.setTasks(Collections.EMPTY_LIST);
      story.setEstimatedHoursField(5.0);

      assertEquals("wrong original estimated hours", 0, story.getEstimatedOriginalHours(), 0);

      story.setTasks(tasks);

      assertEquals("wrong original estimated hours", 9.0, story.getTaskBasedEstimatedOriginalHours(), 0);
      assertEquals(5.0, story.getEstimatedHoursField(), 0);
   }

   public void testGetEstimatedOriginalHoursWithAllZeroEffortTasksInStartedIteration() {
      story.setEstimatedHoursField(5.0);
      resetOrginalEstimates();
      //start iteration
      story.start();
      resetEstimates();
      assertEquals("wrong original estimated hours", 11.0, story.getTaskBasedEstimatedOriginalHours(), 0);
   }

   public void testGetEstimatedOriginalHoursWithAllZeroEffortTasksInNotStartedIteration() {
      story.setEstimatedHoursField(5.0);
      resetOrginalEstimates();
      resetEstimates();
      assertEquals("wrong original estimated hours", 0.0, story.getTaskBasedEstimatedOriginalHours(), 0);
   }

   private void resetOrginalEstimates() {
      task1.setEstimatedOriginalHoursField(0.0);
      task2.setEstimatedOriginalHoursField(0.0);
      task3.setEstimatedOriginalHoursField(0.0);
   }

   private void resetEstimates() {
      task1.setEstimatedHours(0);
      task2.setEstimatedHours(0);
      task3.setEstimatedHours(0);
   }

   public void testGetTaskBasedEstimatedHours() {
      double hours = story.getTaskBasedEstimatedHours();

      assertEquals("wrong original estimated hours", 12.0, hours, 0);
   }

   public void testGetAdjustedEstimatedHours() {
      double adjustedEstimated = story.getAdjustedEstimatedHours();

      assertEquals("wrong adjusted estimated hours", 12.0, adjustedEstimated, 0);
   }

   public void testGetAdjustedEstimatedHoursWithUnworkedTaskCompleted() {
      task3.setCompleted(true);
      double adjustedEstimated = story.getAdjustedEstimatedHours();

      assertEquals("wrong adjusted estimated hours", 6.0, adjustedEstimated, 0);
   }

   public void testGetAdjustedEstimatedHoursWithUnestimatedTasks() {
      Task localTask1 = new Task();
      FieldAccessor.set(localTask1, "timeEntries", new ArrayList<TimeEntry>());
      Task localTask2 = new Task();
      List<Task> tasksList = new ArrayList<Task>();
      FieldAccessor.set(localTask2, "timeEntries", new ArrayList<TimeEntry>());
      tasksList.add(localTask1);
      tasksList.add(localTask2);
      UserStory localStory = new UserStory();
      localStory.setEstimatedHoursField(8.0);
      FieldAccessor.set(localStory, "tasks", tasksList);
      double adjustedEstimated = localStory.getAdjustedEstimatedHours();

      assertEquals("wrong adjusted estimated hours", 8.0, adjustedEstimated, 0);
   }

   public void testGetActualHours() {
      double actual = story.getCachedActualHours();

      assertEquals("wrong actual hours", 4.0, actual, 0);
      assertEstimatedHours(12.0);
   }

   public void testIsCompletedWhenComplete() {
      completeAllTasks();
      boolean isComplete = story.isCompleted();

      assertTrue("not complete", isComplete);
   }

   private void completeAllTasks() {
      task1.setCompleted(true);
      task2.setCompleted(true);
      task3.setCompleted(true);
   }

   public void testIsCompletedWhenNotComplete() {
      task1.setCompleted(true);
      task2.setCompleted(true);
      task3.setCompleted(false);

      assertTrue("was complete", !story.isCompleted());
   }

   public void testNotCompletedWhenNoTasks() {
      tasks.clear();

      boolean isComplete = story.isCompleted();

      assertTrue("was complete", !isComplete);
   }

   public void testGetRemainingHours() {
// 0 + 2 + 6
      assertEquals("wrong remaining hours", 8.0, story.getTaskBasedRemainingHours(), 0.0);
   }

   public void testGetRemainingHoursWithNoTasks() {
      UserStory noTaskStory = new UserStory();
      double estimatedHours = 10.0;
      noTaskStory.setEstimatedHoursField(estimatedHours);
      assertEquals("wrong remaining hours when no tasks",
                   estimatedHours,
                   noTaskStory.getTaskBasedRemainingHours(),
                   0.0);
   }

   public void testGetRemainingHoursWithNoNonzeroEffortTasks() {
      resetEstimates();
      story.setEstimatedHoursField(10.0);
      assertEquals("wrong remaining hours when no nonzero estimate tasks",
                   10.0, story.getTaskBasedRemainingHours(), 0.0);
   }

   public void testGetRemainingHoursWithAllCompletedTasks() {
      completeAllTasks();
      story.setEstimatedHoursField(10.0);
      assertEquals("wrong remaining hours when no nonzero estimate tasks",
                   0.0, story.getTaskBasedRemainingHours(), 0.0);
   }

   public void testGetCompletedOriginalHours() throws Exception {
      assertEquals(0, story.getTaskBasedCompletedOriginalHours(), 0);
      task1.setCompleted(true);
      assertEquals(task1.getEstimatedOriginalHours(), story.getTaskBasedCompletedOriginalHours(), 0);
      task2.setCompleted(true);
      assertEquals(task1.getEstimatedOriginalHours() + task2.getEstimatedOriginalHours(),
                   story.getTaskBasedCompletedOriginalHours(),
                   0);
      task3.setCompleted(true);
      assertEquals(task1.getEstimatedOriginalHours() +
                   task2.getEstimatedOriginalHours() +
                   task3.getEstimatedOriginalHours(), story.getTaskBasedCompletedOriginalHours(), 0);
      task2.setCompleted(false);
      assertEquals(task1.getEstimatedOriginalHours() + task3.getEstimatedOriginalHours(),
                   story.getTaskBasedCompletedOriginalHours(),
                   0);
   }

   public void testGetCompletedHours() throws Exception {
      assertEquals(0, story.getCompletedTaskHours(), 0);
      task1.setCompleted(true);
      assertEquals(task1.getActualHours(), story.getCompletedTaskHours(), 0);
      task2.setCompleted(true);
      assertEquals(task1.getActualHours() + task2.getActualHours(), story.getCompletedTaskHours(), 0);
      task3.setCompleted(true);
      assertEquals(task1.getActualHours() + task2.getActualHours() + task3.getActualHours(),
                   story.getCompletedTaskHours(),
                   0);
      task2.setCompleted(false);
      assertEquals(task1.getActualHours() + task3.getActualHours(), story.getCompletedTaskHours(), 0);
   }
   
   @SuppressWarnings("unchecked")
public void testStart_storyWithTasks() throws Exception {
      double task1OriginalEstimatedHours = 1.0;
      double task2OriginalEstimatedHours = 4.0;
      double storyOriginalEstimatedHours = 3.0;
      task1 = newTask(0.0, task1OriginalEstimatedHours, Collections.EMPTY_LIST, story);
      task2 = newTask(0.0, task2OriginalEstimatedHours, Collections.EMPTY_LIST, story);
      story.setTasks(Arrays.asList(new Task[]{task1, task2}));
      story.setEstimatedHoursField(storyOriginalEstimatedHours);
      assertFalse(story.isStarted());
      assertFalse(task1.isStarted());
      assertFalse(task2.isStarted());
      story.start();
      assertTrue(story.isStarted());
      assertTrue(task1.isStarted());
      assertTrue(task2.isStarted());
      assertEquals(task1OriginalEstimatedHours + task2OriginalEstimatedHours,
                   story.getEstimatedOriginalHoursField(),
                   0.0);
      assertEquals(task1OriginalEstimatedHours, task1.getEstimatedOriginalHoursField(), 0.0);
      assertEquals(task2OriginalEstimatedHours, task2.getEstimatedOriginalHoursField(), 0.0);
   }
}

