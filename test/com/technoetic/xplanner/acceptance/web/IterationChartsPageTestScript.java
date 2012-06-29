package com.technoetic.xplanner.acceptance.web;

public class IterationChartsPageTestScript extends AbstractIterationTestScript {

   public void setUp() throws Exception {
      super.setUp();
      setUpTestStoryAndTask();
   }

   public void testNoExceptionOnChartsPage() throws Exception {
      tester.clickLinkWithText(storyName);
      tester.clickLinkWithText(testTaskName);
      tester.completeCurrentTask();
      tester.clickLinkWithText(testIterationName);
      tester.clickLinkWithKey("iteration.link.statistics");
      tester.assertKeyPresent("iteration.statistics.progress.label");
   }
}
