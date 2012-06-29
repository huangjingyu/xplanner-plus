/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: Apr 21, 2005
 * Time: 10:33:00 PM
 */
package com.technoetic.xplanner.acceptance.web;

public abstract class AbstractIterationTestScript extends AbstractPageTestScript {
   protected String iterationId;
   protected String taskId;

   public AbstractIterationTestScript(String test) { super(test); }

   public AbstractIterationTestScript() {
   }

   public void setUp() throws Exception
   {
       super.setUp();
       setUpTestPerson();
       setUpTestProject();
       setUpTestRole("editor");
       tester.login();
       tester.clickLinkWithText(testProjectName);
       setUpTestIteration();
   }

   protected void setUpTestIteration()
        throws Exception
    {
        tester.gotoProjectsPage();
        tester.clickLinkWithText(testProjectName);
        iterationTester.addIteration(testIterationName,
                            tester.dateStringForNDaysAway(0),
                            tester.dateStringForNDaysAway(14),
                            testIterationDescription);
        tester.clickLinkWithText(testIterationName);
        iterationId = tester.getCurrentPageObjectId();
   }

   protected void setUpTestStoryAndTask() {
      tester.addUserStory(storyName, testStoryDescription, "23.5", "1");
      tester.assertTextPresent(storyName);
      tester.clickLinkWithText(storyName);
      tester.assertOnStoryPage();
      taskId = tester.addTask(testTaskName, developerName, testTaskDescription, testTaskEstimatedHours);
      tester.assertOnStoryPage();
      tester.assertTextPresent(testTaskName);
      tester.clickLinkWithText(testIterationName);
   }

   public void tearDown() throws Exception
    {
        super.tearDown();
    }

   protected void addIteration(String iterationName)
       throws Exception
   {
       tester.gotoProjectsPage();
       tester.clickLinkWithText(testProjectName);
       tester.assertOnProjectPage();
       tester.clickLinkWithKey("project.link.create_iteration");

       tester.setFormElement("name", iterationName);
       tester.setFormElement("startDateString", tester.dateStringForNDaysAway(15));
       tester.setFormElement("endDateString", tester.dateStringForNDaysAway(28));
       tester.setFormElement("description", testIterationDescription);
       tester.submit();
       tester.assertOnProjectPage();
   }
}