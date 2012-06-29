package com.technoetic.xplanner.acceptance.export;

import net.sf.jasperreports.engine.JRException;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript;
import com.technoetic.xplanner.export.IterationDataSource;

public class IterationDataSourceTestScript extends AbstractDatabaseTestScript {
    IterationDataSource source;
    protected Iteration iteration;
    protected UserStory story;
    protected Task task;

    @Override
	public void setUp() throws Exception {
        super.setUp();

        Project project = newProject();
        iteration = newIteration(project);
        story = newUserStory(iteration);
    }

    public void testNext_OneStoryNoTask() throws Exception {
        source = new IterationDataSource(iteration, getSession());
        assertFieldValues(story, null);
        assertFalse("next", source.next());
    }

    public void testNext_OneStoryWithTask() throws Exception {
        task = newTask(story);
        source = new IterationDataSource(iteration, getSession());
        assertFieldValues(story, task);
        assertFalse("next", source.next());
    }

    public void testNext_TwoStoriesWithDifferentPriority () throws Exception {
        story.setName("aaaa"); //first in alphabetical order
        story.setPriority(4); //second in priority order
        task = newTask(story);
        task.setName("aaa task");
        UserStory anotherStory = newUserStory(iteration);
        anotherStory.setName("zzzz"); //second in alphabetical order
        anotherStory.setPriority(1); //first in priority order
        Task anotherTask = newTask(anotherStory);
        anotherTask.setName("zzz task");
        source = new IterationDataSource(iteration, getSession());
        assertFieldValues(anotherStory, anotherTask);
        assertFieldValues(story, task);
        assertFalse("next", source.next());
    }

    private void assertFieldValues(UserStory story, Task task) throws JRException {
        assertTrue("no next", source.next());
        assertEquals("story name", story.getName(),source.getFieldValue("StoryName"));
        if (task != null)
            assertEquals("task name", task.getName(),source.getFieldValue("TaskName"));
    }
}