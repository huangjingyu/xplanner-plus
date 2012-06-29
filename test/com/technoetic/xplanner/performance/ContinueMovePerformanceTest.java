package com.technoetic.xplanner.performance;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.clarkware.junitperf.TimedTest;
import com.technoetic.xplanner.acceptance.web.IterationMoveContinueStoryTestScript;
import com.technoetic.xplanner.acceptance.web.StoryPageTestScript;

public class ContinueMovePerformanceTest extends TestCase {
    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite();
        suite.addTest(
            new TimedTest(new IterationMoveContinueStoryTestScript("testContinueStoryInNotStartedIteration"), 290000));
        suite.addTest(
            new TimedTest(new IterationMoveContinueStoryTestScript("testContinueStoryInStartedIteration"), 300000));
       suite.addTest(
            new TimedTest(new IterationMoveContinueStoryTestScript("testMoveStoryToDifferentIteration"), 294000));
       suite.addTest(
            new TimedTest(new StoryPageTestScript("testContinueTaskInDifferentStory"), 300000));
       suite.addTest(
            new TimedTest(new StoryPageTestScript("testMoveTaskToDifferentStory"), 290000));
        return suite;
    }
}
