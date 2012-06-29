package com.technoetic.xplanner.acceptance.web;

import org.junit.Assert;

import net.sf.xplanner.domain.Project;

import com.technoetic.xplanner.XPlannerProperties;

public class ProjectPageTestScript extends AbstractPageTestScript
{
    private String startDate;
    private String endDate;
    private Project testProject;
    private String testTaskId;

    private String wikiProperty = "twiki.scheme.wiki";
    private String descriptionElementName = "description";
    private String wikiUrlElementName = "wikiUrl";
    private String wikiLinkText = "newLink";
    private String newWikiUrl = "http://newwikilocation/$1";

   public ProjectPageTestScript(String test)
   {
       super(test);
   }


    public void setUp() throws Exception
    {
        startDate = tester.dateStringForNDaysAway(0);
        endDate = tester.dateStringForNDaysAway(14);
        super.setUp();
        tester.login();
        setUpTestPerson();
        testProject = newProject();
        testProject.setName(testProjectName);
        testProject.setDescription(testProjectDescription);
        commitSession();
        tester.gotoProjectsPage();
        tester.clickLinkWithText(""+ testProject.getId());
    }

    public void tearDown() throws Exception
    {
        tester.logout();
        super.tearDown();
    }

    public void testWikiRenderingInDescription() {
       tester.gotoProjectsPage();
       tester.clickLinkWithText(String.valueOf(testProject.getId()));
       tester.clickLinkWithKey("action.edit.project");
       tester.setFormElement(descriptionElementName, "wiki:" + wikiLinkText);
       tester.submit();
       XPlannerProperties properties = new XPlannerProperties();
       String wikiDefaultLink = properties.getProperty(wikiProperty);
       String projectWikiLink = wikiDefaultLink.substring(0, wikiDefaultLink.length()-2) + wikiLinkText;
//       int index = tester.getDialog().getResponseText().indexOf(projectWikiLink);
//       assertTrue("There is no wiki link: " + projectWikiLink, index != -1);
Assert.fail();
       tester.clickLinkWithKey("action.edit.project");
       tester.setFormElement(wikiUrlElementName, newWikiUrl);
       tester.submit();
       projectWikiLink = newWikiUrl.substring(0, newWikiUrl.length()-2) + wikiLinkText;
//       index = tester.getDialog().getResponseText().indexOf(projectWikiLink);
//       assertTrue("There is no wiki link: " + projectWikiLink, index != -1);
    }

    public void testActionButtonsOnViewPage() throws Exception
    {
       tester.gotoProjectsPage();
       tester.clickLinkWithText(String.valueOf(testProject.getId()));
       tester.assertLinkPresentWithKey("action.edit.project");
       tester.assertLinkPresentWithKey("action.delete.project");
    }

    public void testProjectAttributesSettings() throws Exception
    {
        //tester.gotoProjectsPage();
        //tester.clickLinkWithText(String.valueOf(testProject.getId()));
        tester.clickLinkWithKey("action.edit.project");
        tester.uncheckCheckbox("sendemail");
        tester.uncheckCheckbox("hidden");
        tester.uncheckCheckbox("optEscapeBrackets");
        tester.setFormElement("wikiUrl", testWikiUrl);
        tester.submit();
        tester.gotoProjectsPage();
        tester.clickLinkWithText(String.valueOf(testProject.getId()));
        tester.clickLinkWithKey("action.edit.project");
        tester.assertFormElementEquals("wikiUrl", testWikiUrl);
        tester.assertCheckboxNotSelected("sendemail");
        tester.assertCheckboxNotSelected("hidden");
        tester.assertCheckboxNotSelected("optEscapeBrackets");
    }


    public void testProjectPageContentAndLinks() throws Exception
    {
        tester.assertOnProjectPage();
        tester.assertTextPresent(testProjectName);
        tester.assertTextPresent(testProjectDescription);
        tester.assertKeyPresent("iterations.none");
        tester.assertLinkPresentWithKey("action.edit.project");
        tester.assertLinkPresentWithKey("project.link.create_iteration");
    }

    //FIXME Commented out due to failure on the build machine
    public void _testAddingEditingAndDeletingIterations()
    {
        iterationTester.addIteration(testIterationName,
                            startDate,
                            endDate,
                            testIterationDescription);
        assertIterationPresent(testIterationName, "inactive", startDate, endDate, "0.0",
                               "0.0", "0.0", "0.0");
        String originalStoryEstimation = "0.0";
        double taskEntryDuration = 2.0;
        String expectedEstimatedHours = testTaskEstimatedHours;
        String expectedRemainingHours = Double.toString(Double.parseDouble(testTaskEstimatedHours) - taskEntryDuration);
        initializeIteration(originalStoryEstimation, testTaskEstimatedHours, (int) taskEntryDuration);
        assertIterationPresent(testIterationName, "active", startDate, endDate, "0.0",
                               "" + taskEntryDuration, expectedEstimatedHours, expectedRemainingHours);

        tester.clickEditLinkInRowWithText(testIterationName);
        String newIterationName = "A Different Iteration Name";
        tester.setFormElement("name", newIterationName);
        tester.submit();
        tester.assertTextPresent(newIterationName);

        deleteLocalTimeEntry(testTaskId);
        tearDownTestStory();
        tester.clickDeleteLinkForRowWithText(newIterationName);
        tester.assertTextNotPresent(newIterationName);
    }


    public void testAddingAndDeletingNotes() throws Exception
    {
        runNotesTests(XPlannerWebTester.PROJECT_PAGE);
    }

    public void testXmlExport() throws Exception
    {
        checkExportUri("project", "xml");
    }

    public void testMpxExport() throws Exception
    {
        checkExportUri("project", "mpx");
    }

    protected void traverseLinkWithKeyAndReturn(String key) throws Exception
    {
        tester.clickLinkWithKey(key);
        tester.gotoPage("view", "projects", 0);
    }

    private void initializeIteration(String originalStoryEstimation, String taskEstimation, int taskEntryDuration)
    {
        tester.clickLinkWithText(testIterationName);
        iterationTester.startCurrentIteration();
        String testStoryId = tester.addUserStory(storyName, testStoryDescription, originalStoryEstimation, "1");
        tester.clickLinkWithText(testStoryId);
        testTaskId = tester.addTask(testTaskName, developerName, testTaskDescription, taskEstimation);
        tester.clickLinkWithText(testTaskId);
        tester.assertOnTaskPage();
        tester.assertTextPresent(testTaskName);
        tester.assertLinkPresentWithKey("task.link.edit_time");
        tester.clickLinkWithKey("task.link.edit_time");
        tester.setTimeEntry(0, 0, taskEntryDuration, developerInitials);
        tester.clickLinkWithText(testProjectName);
    }

    private void assertIterationPresent(String iterationName, String status, String startDate,
                                        String endDate, String daysWorked, String actualHours,
                                        String estimatedHours, String remainingHours)
    {
        tester.assertKeyNotPresent("iterations.none");
        tester.assertKeyPresent("iterations.tableheading.iteration");
        tester.assertKeyPresent("iterations.tableheading.startDate");
        tester.assertKeyPresent("iterations.tableheading.endDate");
        tester.assertKeyPresent("iterations.tableheading.days_worked");
        //TODO The following columns on iteration page are taken out for performance reason
//        if (!"image".equals(new com.technoetic.xplanner.XPlannerProperties().getProperty("xplanner.progressbar.impl"))) {
//            tester.assertKeyPresent("iterations.tableheading.actual_hours");
//        }
//        tester.assertKeyPresent("iterations.tableheading.estimated_hours");
//        tester.assertKeyPresent("iterations.tableheading.remaining_hours");
        /*tester.assertKeyPresent("iterations.tableheading.orig_estimated_hours");
        tester.assertKeyPresent("iterations.tableheading.overestimated_hours");
        tester.assertKeyPresent("iterations.tableheading.orig_overestimated_hours");
        tester.assertKeyPresent("iterations.tableheading.underestimated_hours");
        tester.assertKeyPresent("iterations.tableheading.orig_underestimated_hours");
        tester.assertKeyPresent("iterations.tableheading.added_hours");
        tester.assertKeyPresent("iterations.tableheading.orig_added_hours");
        tester.assertKeyPresent("iterations.tableheading.postponed_hours");
        tester.assertKeyPresent("iterations.tableheading.actions");*/
        tester.assertCellTextForRowWithTextAndColumnKeyEquals(XPlannerWebTester.MAIN_TABLE_ID, iterationName,
                                                              "iterations.tableheading.startDate", startDate);
        tester.assertCellTextForRowWithTextAndColumnKeyEquals(XPlannerWebTester.MAIN_TABLE_ID, iterationName,
                                                              "iterations.tableheading.endDate", endDate);
        tester.assertCellTextForRowWithTextAndColumnKeyEquals(XPlannerWebTester.MAIN_TABLE_ID, iterationName,
                                                              "iterations.tableheading.days_worked", daysWorked);
        //TODO The following columns on iteration page are taken out for performance reason
//        if (!"image".equals(new com.technoetic.xplanner.XPlannerProperties().getProperty("xplanner.progressbar.impl"))) {
//            tester.assertCellTextForRowWithTextAndColumnKeyEquals(XPlannerWebTester.MAIN_TABLE_ID, iterationName,
//                    "iterations.tableheading.actual_hours", actualHours);
//        }
//        tester.assertCellTextForRowWithTextAndColumnKeyEquals(XPlannerWebTester.MAIN_TABLE_ID, iterationName,
//                "iterations.tableheading.estimated_hours", estimatedHours);
//        tester.assertCellTextForRowWithTextAndColumnKeyEquals(XPlannerWebTester.MAIN_TABLE_ID, iterationName,
//                "iterations.tableheading.remaining_hours", remainingHours);
    }
}
