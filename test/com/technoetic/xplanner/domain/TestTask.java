package com.technoetic.xplanner.domain;

import java.util.ArrayList;
import java.util.Date;

import junit.extensions.FieldAccessor;
import junit.framework.TestCase;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.TimeEntry;
import net.sf.xplanner.domain.UserStory;

public class TestTask extends TestCase
{
    public TestTask(String name)
    {
        super(name);
    }

    public void testGetActualHours()
    {
        Task task = setUpTestTask();
        double hours = task.getActualHours();
        assertEquals("wrong actual hours", 2.0, hours, 0.0);
    }

    public void testGetAdjustedEstimatedHoursWithTimeEntriesLessThanEstimate()
    {
        Task task = setUpTestTask();
        task.setEstimatedHours(4.0);

        double hours = task.getAdjustedEstimatedHours();
        assertEquals("wrong adjusted estimated hours", 4.0, hours, 0.0);
    }

    public void testGetAdjustedEstimatedHoursWithTimeEntriesGreaterThanEstimate()
    {
        Task task = setUpTestTask();
        task.setEstimatedHours(1.0);

        double hours = task.getAdjustedEstimatedHours();
        assertEquals("wrong adjusted estimated hours", 2.0, hours, 0.0);
    }

    public void testGetAdjustedEstimatedHoursWithoutTimeEntriesAndNotComplete()
    {
        Task task = new Task();
        task.setEstimatedHours(4.0);
        task.setCompleted(false);
        ArrayList timeEntries = new ArrayList();
        FieldAccessor.set(task, "timeEntries", timeEntries);

        double hours = task.getAdjustedEstimatedHours();
        assertEquals("wrong adjusted estimated hours", 4.0, hours, 0.0);
    }

    public void testGetAdjustedEstimatedHoursWithoutTimeEntriesAndComplete()
    {
        Task task = new Task();
        task.setEstimatedHours(4.0);
        task.setCompleted(true);
        ArrayList timeEntries = new ArrayList();
        FieldAccessor.set(task, "timeEntries", timeEntries);

        double hours = task.getAdjustedEstimatedHours();
        assertEquals("wrong adjusted estimated hours", 0.0, hours, 0.0);
    }

    public void testRemainingHours()
    {
        Task task = setUpTestTask();
        task.setEstimatedHours(4.0);

        double remainingHours = task.getRemainingHours();
        assertEquals("wrong remaining hours", 2.0, remainingHours, 0.0);
    }

    public void testRemainingHoursWhenOverEstimate()
    {
        Task task = setUpTestTask();
        task.setEstimatedHours(1.0);

        double remainingHours = task.getRemainingHours();
        assertEquals("wrong remaining hours", 0.0, remainingHours, 0.0);
    }

    public void testEstimatedOriginalHoursInNotStartedIteration()
    {
        Task task = setUpTestTask();
        task.setEstimatedHours(5.0);

        assertEquals("wrong original estimate", 5.0, task.getEstimatedOriginalHours(), 0);

        task.setEstimatedHours(4.0);

        assertEquals("wrong original estimate", 4.0, task.getEstimatedOriginalHours(), 0);
    }

   public void testEstimatedOriginalHoursInStartedIteration()
   {
       Task task = setUpTestTask();
       task.setEstimatedOriginalHoursField(0);
       task.setEstimatedHours(5.0);
       //return the current estimation
       assertEquals("wrong original estimate", 5.0, task.getEstimatedOriginalHours(), 0);

       //start iteration
       task.start();

       task.setEstimatedHours(4.0);
      //returns the orginal one
       assertEquals("wrong original estimate", 5.0, task.getEstimatedOriginalHours(), 0);
   }

    private Task setUpTestTask()
    {
        long now = new Date().getTime();
        TimeEntry t1 = new TimeEntry();
        t1.setStartTime(new Date(now - 3600000));
        t1.setEndTime(new Date(now));
        t1.setDuration(1);
        TimeEntry t2 = new TimeEntry();
        t2.setStartTime(new Date(now + 3600000));
        t2.setEndTime(new Date(now + 7200000));
        t2.setDuration(1);
        ArrayList timeEntries = new ArrayList();
        timeEntries.add(t1);
        timeEntries.add(t2);
        Task task = new Task();
        task.setTimeEntries(timeEntries);
        UserStory story = new UserStory();
        task.setUserStory(story);
        return task;
    }

    public void testGetStatus() throws Exception
    {
        Task taskWithTimeEntries = setUpTestTask();
        assertEquals("started task", TaskStatus.STARTED, taskWithTimeEntries.getStatus());

        Task taskWithoutTimeEntries = new Task();
        taskWithoutTimeEntries.setEstimatedHours(1.2);
        assertEquals("no status task", TaskStatus.NON_STARTED, taskWithoutTimeEntries.getStatus());

        Task finihsedTasks = new Task();
        finihsedTasks.setCompleted(true);
        assertTrue("task completed", finihsedTasks.isCompleted());
        assertEquals("completed task", TaskStatus.COMPLETED, finihsedTasks.getStatus());
    }

   public void testStart() throws Exception
   {
      Task task = setUpTestTask();
      task.setEstimatedHours(2.0);
      assertEquals("Wrong estimated original hours", 2.0, task.getEstimatedOriginalHours(), 0.0);
      task.setEstimatedHours(3.0);
      assertEquals("Wrong estimated original hours", 3.0, task.getEstimatedOriginalHours(), 0.0);
      task.start();
      assertEquals("Wrong estimated original hours", 3.0, task.getEstimatedOriginalHours(), 0.0);
      task.setEstimatedHours(4.0);
      assertEquals("Wrong estimated original hours", 3.0, task.getEstimatedOriginalHours(), 0.0);
   }
}