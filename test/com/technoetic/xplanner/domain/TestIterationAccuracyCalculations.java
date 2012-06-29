package com.technoetic.xplanner.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.TimeEntry;
import net.sf.xplanner.domain.UserStory;

import org.apache.commons.beanutils.BeanUtils;

public class TestIterationAccuracyCalculations extends TestCase {
  private Iteration iteration;
  public static final int TASK_1_INDEX = 0;
  public static final int TASK_2_INDEX = 1;

  public void testIterationSummaryHours() throws Exception {
    iteration = new Iteration();
    iteration.setUserStories(new ArrayList());
    UserStory story1 = createStory(10, new double[]{5, 7});
    iteration.getUserStories().add(story1);

    assertTaskValueOfIteration("estimatedOriginal", 12.0);
    assertTaskValueOfIteration("currentEstimated",  12.0);
    assertTaskValueOfIteration("actual",             0.0);
    assertTaskValueOfIteration("remaining",         12.0);
    assertTaskValueOfIteration("overestimated",      0.0);
    assertTaskValueOfIteration("underestimated",     0.0);
    assertTaskValueOfIteration("added",              0.0);
    assertTaskValueOfIteration("postponed",          0.0);

    assertPropsOfIteration("original",  10.0, 12.0);
    assertPropsOfIteration("added",      0.0, 0.0);
    assertPropsOfIteration("postponed",  0.0, 0.0);
    assertPropsOfIteration("total",     10.0, 12.0);
    assertPropsOfIteration("completed",  0.0, 0.0);
    assertPropsOfIteration("remaining", 10.0, 12.0);

    iteration.start();
    workAndCompleteTask(story1, TASK_1_INDEX, 4.0);
    addTasksToStory(new double[]{2.0}, TaskDisposition.DISCOVERED, story1);

    assertTaskValueOfIteration("estimatedOriginal", 12.0);
    assertTaskValueOfIteration("currentEstimated",  13.0);
    assertTaskValueOfIteration("actual",             4.0);
    assertTaskValueOfIteration("remaining",          9.0);
    assertTaskValueOfIteration("overestimated",      1.0);
    assertTaskValueOfIteration("underestimated",     2.0);
    assertTaskValueOfIteration("added",              0.0);
    assertTaskValueOfIteration("postponed",          0.0);

    assertPropsOfIteration("original",  10.0, 12.0);
    assertPropsOfIteration("added",      0.0,  0.0);
    assertPropsOfIteration("postponed",  0.0,  0.0);
    assertPropsOfIteration("total",     10.0, 12.0);
    assertPropsOfIteration("completed",  0.0,  5.0);
    assertPropsOfIteration("remaining", 10.0,  9.0);

    UserStory story2 = addStoryWithTasks(20, new double[]{15});

    assertTaskValueOfIteration("estimatedOriginal", 12.0);
    assertTaskValueOfIteration("currentEstimated",  28.0);
    assertTaskValueOfIteration("actual",             4.0);
    assertTaskValueOfIteration("remaining",         24.0);
    assertTaskValueOfIteration("overestimated",      1.0);
    assertTaskValueOfIteration("underestimated",     2.0);
    assertTaskValueOfIteration("added",             15.0);
    assertTaskValueOfIteration("postponed",          0.0);

    assertPropsOfIteration("original",  10.0, 12.0);
    assertPropsOfIteration("added",     20.0, 15.0);
    assertPropsOfIteration("postponed",  0.0,  0.0);
    assertPropsOfIteration("total",     30.0, 27.0);
    assertPropsOfIteration("completed",  0.0,  5.0);
    assertPropsOfIteration("remaining", 30.0, 24.0);

    workAndCompleteTask(story1, TASK_2_INDEX, 10.0);

    assertTaskValueOfIteration("estimatedOriginal", 12.0);
    assertTaskValueOfIteration("currentEstimated",  31.0);
    assertTaskValueOfIteration("actual",            14.0);
    assertTaskValueOfIteration("remaining",         17.0);
    assertTaskValueOfIteration("overestimated",      1.0);
    assertTaskValueOfIteration("underestimated",     5.0);
    assertTaskValueOfIteration("added",             15.0);
    assertTaskValueOfIteration("postponed",          0.0);

    assertPropsOfIteration("original",  10.0, 12.0);
    assertPropsOfIteration("added",     20.0, 15.0);
    assertPropsOfIteration("postponed",  0.0,  0.0);
    assertPropsOfIteration("total",     30.0, 27.0);
    assertPropsOfIteration("completed",  0.0, 12.0);
    assertPropsOfIteration("remaining", 30.0, 17.0);

    continueStory(story1);

    assertTaskValueOfIteration("estimatedOriginal", 12.0);
    assertTaskValueOfIteration("currentEstimated",  29.0);
    assertTaskValueOfIteration("actual",            14.0);
    assertTaskValueOfIteration("remaining",         15.0);
    assertTaskValueOfIteration("overestimated",      1.0);
    assertTaskValueOfIteration("underestimated",     5.0);
    assertTaskValueOfIteration("added",             15.0);
    assertTaskValueOfIteration("postponed",          2.0);

    assertPropsOfIteration("original",  10.0, 12.0);
    assertPropsOfIteration("added",     20.0, 15.0);
    assertPropsOfIteration("postponed", 10.0,  2.0);
    assertPropsOfIteration("total",     20.0, 25.0);
    assertPropsOfIteration("completed",  0.0, 12.0);
    assertPropsOfIteration("remaining", 20.0, 15.0);

    workAndCompleteTask(story2, TASK_1_INDEX, 10.0);

    assertTaskValueOfIteration("estimatedOriginal", 12.0);
    assertTaskValueOfIteration("currentEstimated",  24.0);
    assertTaskValueOfIteration("actual",            24.0);
    assertTaskValueOfIteration("remaining",          0.0);
    assertTaskValueOfIteration("overestimated",      6.0);
    assertTaskValueOfIteration("underestimated",     5.0);
    assertTaskValueOfIteration("added",             15.0);
    assertTaskValueOfIteration("postponed",          2.0);

    assertPropsOfIteration("original",  10.0, 12.0);
    assertPropsOfIteration("added",     20.0, 15.0);
    assertPropsOfIteration("postponed", 10.0,  2.0);
    assertPropsOfIteration("total",     20.0, 25.0);
    assertPropsOfIteration("completed", 20.0, 27.0);
    assertPropsOfIteration("remaining", 0.0,  0.0);
  }

  private void workAndCompleteTask(UserStory workedStory, int workedTaskIndex, double hoursWorked) {
    workTask(workedStory, workedTaskIndex, hoursWorked);
    completeTask(workedStory, workedTaskIndex);
  }

  private void continueStory(UserStory story) {
    story.postponeRemainingHours();
    Collection tasks = story.getTasks();
    for (Iterator iterator = tasks.iterator(); iterator.hasNext();) {
      Task task = (Task) iterator.next();
      if (!task.isCompleted()) {//todo this if() doesn't belong here. Find it a good home or put it out of it's misery
        task.postpone();
      }
    }
  }

  private UserStory addStoryWithTasks(int storyEstimate, double[] taskEstimates) {
    UserStory story = createStory(storyEstimate, taskEstimates);
    story.setDisposition(StoryDisposition.ADDED);
    for (Iterator iterator = story.getTasks().iterator(); iterator.hasNext();) {
      Task task = (Task) iterator.next();
      task.setDisposition(TaskDisposition.ADDED);
    }
    iteration.getUserStories().add(story);
    return story;
  }

  private void completeTask(UserStory story, int taskIndex) {
    Task task = getTask(story, taskIndex);
    task.setCompleted(true);
  }

  private void workTask(UserStory story, int taskIndex, double hoursWorked) {
    Task task = getTask(story, taskIndex);
    addTimeEntry(hoursWorked, task);
  }

  private Task getTask(UserStory story, int taskIndex) {
    List tasks = new ArrayList(story.getTasks());
    return (Task) tasks.get(taskIndex);
  }

  private void addTimeEntry(double hoursWorked, Task task) {
    TimeEntry timeEntry = new TimeEntry();
    timeEntry.setDuration(hoursWorked);
    task.getTimeEntries().add(timeEntry);
  }


  private UserStory createStory(double storyEstimate, double[] taskEstimates) {
    UserStory story = new UserStory();
    story.setEstimatedHoursField(storyEstimate);
    story.setTasks(new ArrayList());
    addTasksToStory(taskEstimates, story);
     story.start();
    return story;
  }

  private void addTasksToStory(double[] taskEstimates, UserStory story) {
    addTasksToStory(taskEstimates, iteration.getNewTaskDisposition(story), story);
  }

  private void addTasksToStory(double[] taskEstimates, TaskDisposition disposition, UserStory story) {
    for (int i = 0; i < taskEstimates.length; i++) {
      double taskEstimate = taskEstimates[i];
      Task task = new Task();
      task.setTimeEntries(new ArrayList());
      task.setEstimatedHours(taskEstimate);
      task.setDisposition(disposition);
      story.getTasks().add(task);
    }
  }

  private void assertPropsOfIteration(String typeOfHours, double storyHours, double taskHours) throws Exception {
    assertThat(typeOfHours, is(storyHours, taskHours));
  }

  private double[] is(double storyHours, double taskHours) {return new double[]{storyHours, taskHours};}

  private void assertThat(String typeOfHours, double[] expectedEstimates) throws Exception {
    assertValue(typeOfHours, "story", expectedEstimates[0]);
    assertTaskValueOfIteration(typeOfHours, expectedEstimates[1]);

  }

  private void assertTaskValueOfIteration(String typeOfHours, double expectedEstimate) throws Exception {
    assertValue(typeOfHours, "task", expectedEstimate);
  }

  private void assertValue(String typeOfHours, String objectType,
                           double expectedEstimate) throws Exception {
    assertEquals("iteration." + getPropertyName(typeOfHours, objectType),
                 expectedEstimate,
                 value(typeOfHours, objectType),
                 0.0);
  }

  private double value(String propertyCore, String objectType) throws Exception {
    String value = BeanUtils.getProperty(iteration, getPropertyName(propertyCore, objectType));
    return Double.valueOf(value).doubleValue();
  }

  private String getPropertyName(String propertyCore, String objectType) {
    return objectType + makeFirstLetterUpperCase(propertyCore) +  "Hours";
  }

   private String makeFirstLetterUpperCase(String text) {
      return new String(new char[]{text.charAt(0)}).toUpperCase() + text.substring(1);
   }
}
