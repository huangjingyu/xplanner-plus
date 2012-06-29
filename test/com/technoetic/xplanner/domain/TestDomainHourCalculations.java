package com.technoetic.xplanner.domain;

import java.util.Date;

import junit.framework.TestCase;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.TimeEntry;
import net.sf.xplanner.domain.UserStory;

public class TestDomainHourCalculations extends TestCase {
   public static final int HOURS = 60*60*1000;
   public static final double PRECISION = 0.001;

   private void addTimeEntryToTask(Task task, int workedHours) {
      long now = new Date().getTime();
      TimeEntry t = new TimeEntry();
      t.setStartTime(new Date(now - workedHours*HOURS));
      t.setEndTime(new Date(now));
      task.getTimeEntries().add(t);
   }

   public void test() throws Exception {
      UserStory story1 = new UserStory();
      story1.setEstimatedHoursField(10);
      //                                    original      actual      postponed
      //                                 story       current     remaining    added
      assertStoryStatisticsEqual(story1,    10,   0,   10,    0,   10,     0,    0);
      Task task1_1 = addNewTaskToStory(story1);
      task1_1.setEstimatedHours(5);
      assertStoryStatisticsEqual(story1,    10,    5,    5,    0,    5,     0,    0);
      Task task1_2 = addNewTaskToStory(story1);
      task1_2.setEstimatedHours(7);
      assertStoryStatisticsEqual(story1,    10,  5+7,  5+7,    0,  5+7,     0,    0);
      story1.start();
      addTimeEntryToTask(task1_1, 4);
      task1_1.setCompleted(true);
      assertStoryStatisticsEqual(story1,    10,  5+7,  4+7,    4,    7,     0,    0);
      Task task1_3 = addNewTaskToStory(story1);
      task1_3.setEstimatedHours(2);
      task1_3.setDisposition(TaskDisposition.DISCOVERED);
      assertStoryStatisticsEqual(story1,    10,  5+7,4+7+2,    4,  7+2,     0,    0);

   }

   private Task addNewTaskToStory(UserStory story1) {
      Task task1_1 = new Task();
      story1.getTasks().add(task1_1);
      task1_1.setUserStory(story1);
      return task1_1;
   }

   private void assertStoryStatisticsEqual(UserStory story,
                                           double storyEstimate,
                                           double original,
                                           double current,
                                           double actual,
                                           double remaining,
                                           double postponed,
                                           double added) {
     assertEquals("story", storyEstimate, story.getEstimatedHoursField(),PRECISION);
      assertEquals("original", original, story.getEstimatedOriginalHours(),PRECISION);
      assertEquals("current", current, story.getEstimatedHours(),PRECISION);
      assertEquals("actual", actual, story.getCachedActualHours(),PRECISION);
      assertEquals("remaining", remaining, story.getTaskBasedRemainingHours(),PRECISION);
      assertEquals("postponed", postponed, story.getPostponedHours(),PRECISION);
      assertEquals("added", added, story.getEstimatedHoursOfAddedTasks(),PRECISION);
   }

}