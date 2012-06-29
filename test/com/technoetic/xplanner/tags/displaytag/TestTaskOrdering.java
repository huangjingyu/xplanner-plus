package com.technoetic.xplanner.tags.displaytag;


import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

public class TestTaskOrdering extends TestCase
{
    TaskOrdering taskStatusOrder;
    private Task task1;
    private Task task2;

    private Task createTask(String taskName, boolean completed, String storyName, int storyOrder)
    {
        Task task = new Task();
        task.setName(taskName);
        task.setCompleted(completed);
        UserStory story = new UserStory();
        story.setOrderNo(storyOrder);
        story.setName(storyName);
        task.setUserStory(story);
        return task;
    }

    public void testCompareToByTaskName() throws Exception
    {
        task1 = createTask("Task 1", true, "Story 1", 1);
        task2 = createTask("Task 2", true, "Story 1", 1);

        TaskOrdering taskStatusOrder1 = new TaskOrdering(task1);
        TaskOrdering taskStatusOrder2 = new TaskOrdering(task2);
        assertTrue(taskStatusOrder1.compareTo(taskStatusOrder2) < 0);
    }

    public void testCompareToByStoryName() throws Exception
    {
        task1 = createTask("Task 1", true, "Story 2", 1);
        task2 = createTask("Task 2", true, "Story 1", 1);

        TaskOrdering taskStatusOrder1 = new TaskOrdering(task1);
        TaskOrdering taskStatusOrder2 = new TaskOrdering(task2);
        assertTrue(taskStatusOrder1.compareTo(taskStatusOrder2) > 0);
    }

    public void testCompareToByOrderNo() throws Exception
    {
        task1 = createTask("Task 1", true, "Story 1", 1);
        task2 = createTask("Task 2", true, "Story 1", 2);

        TaskOrdering taskStatusOrder1 = new TaskOrdering(task1);
        TaskOrdering taskStatusOrder2 = new TaskOrdering(task2);
        assertTrue(taskStatusOrder1.compareTo(taskStatusOrder2) < 0);
    }

    public void testCompareToByTaskStatus_StartedLessThanNonStarted() throws Exception
    {
        task1 = createTask("Task 1", false, "Story 1", 1);
        List<net.sf.xplanner.domain.TimeEntry> timeEntries = new ArrayList<net.sf.xplanner.domain.TimeEntry>();
        net.sf.xplanner.domain.TimeEntry timeEntry = new net.sf.xplanner.domain.TimeEntry();
        timeEntry.setDuration(1.2);
        timeEntries.add(timeEntry);
        task1.setTimeEntries(timeEntries);

        task2 = createTask("Task 2", false, "Story 1", 1);
        TaskOrdering startedTask = new TaskOrdering(task1);
        TaskOrdering nonStartedTask = new TaskOrdering(task2);

        assertTrue("started < nonstarted", startedTask.compareTo(nonStartedTask) < 0);
    }

    public void testCompareToByTaskStatus_NonStartedLessThanCompleted() throws Exception
    {
        task1 = createTask("Task 1", false, "Story 1", 1);
        task2 = createTask("Task 2", true, "Story 1", 1);

        TaskOrdering nonStartedTask = new TaskOrdering(task1);
        TaskOrdering completedTask = new TaskOrdering(task2);
        assertTrue("non-started < completed", nonStartedTask.compareTo(completedTask) < 0);
    }
}