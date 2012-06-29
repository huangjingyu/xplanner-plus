package com.technoetic.xplanner.acceptance.web;

import net.sf.xplanner.domain.History;

import com.technoetic.xplanner.domain.StoryStatus;
import com.technoetic.xplanner.views.HistoryPage;


public class IterationPageTestScript extends AbstractIterationTestScript {

   public IterationPageTestScript(String test) { super(test); }

   public IterationPageTestScript() {
   }

   public void setUp() throws Exception {
      super.setUp();
      setUpTestStoryAndTask();
   }

   //FIXME: The "All" text is not present on build machine
    public void _testDisplayAllWithMoreThan10Iterations() throws Exception{
        int lastIterationId = 12;
        String iterationName = "Iteration";

        for(int i=0;i<lastIterationId;i++){
            addIteration(iterationName+i);
        }

        tester.gotoProjectsPage();
        tester.clickLinkWithText(testProjectName);
        tester.assertTextPresent(iterationName+"0");
        tester.assertTextNotPresent(iterationName+lastIterationId);
        tester.clickLinkWithText("All");
        tester.assertTextPresent(iterationName+"0");
        tester.assertTextPresent(iterationName+(lastIterationId-1));

    }

    public void testContentAndLinks()
    {
        iterationTester.assertOnIterationPage();
        tester.assertTextPresent(testIterationName);
        tester.assertTextPresent(testIterationDescription);
        tester.clickLinkWithKey("navigation.top");
        tester.assertOnTopPage();
        tester.clickLinkWithText(testProjectName);
        tester.clickLinkWithText(testIterationName);
        iterationTester.assertOnIterationPage();
        tester.clickLinkWithKey("navigation.project");
        tester.assertOnProjectPage();
        tester.clickLinkWithText(testIterationName);
        tester.clickLinkWithKey("iteration.link.all_tasks");
        tester.assertKeyPresent("iteration.alltasks.prefix");
        tester.clickLinkWithKey("iteration.link.stories");
        iterationTester.assertOnIterationPage();
        tester.assertActualHoursColumnPresent();
        tester.assertKeyPresent("iteration.link.save_order");
    }

    public void testAddingAndDeletingStories() throws Exception
    {
        String storyName = "A test story";
        String storyDescription = "A story description";
        String estimatedHours = "23.5";
        tester.addUserStory(storyName, storyDescription, estimatedHours, "2");
        verifyHistorys(History.CREATED, storyName);
        tester.gotoProjectsPage();
        tester.clickLinkWithText(testProjectName);
        tester.clickLinkWithText(testIterationName);
        iterationTester.assertOnIterationPage();
        tester.assertTextPresent(storyName);
        tester.assertTextPresent(tester.getMessage(StoryStatus.DRAFT.getNameKey()));
        tester.assertKeyPresent("iteration.link.save_order");
        tester.assertFormElementEquals("orderNo[1]","1");
        tester.assertFormElementEquals("orderNo[0]","2");
       //tester.assertCellTextForRowIndexAndColumnKeyContains("objecttable",1,"stories.tableheading.order","0");
        // this doesn't work though it seems like
        // it should?
        //tester.assertTextPresent(estimatedHours);
        tester.clickDeleteLinkForRowWithText(storyName);
        verifyHistorys(History.DELETED, storyName);
        tester.gotoProjectsPage();
        tester.clickLinkWithText(testProjectName);
        tester.clickLinkWithText(testIterationName);
        tester.assertTextNotPresent(storyName);

    }

    public void testAddingAndDeletingNotes()
    {
        runNotesTests(XPlannerWebTester.ITERATION_PAGE);
    }

    public void testXmlExport() throws Exception
    {
        checkExportUri("iteration", "xml");
    }

    public void testMpxExport() throws Exception
    {
        checkExportUri("iteration", "mpx");
    }

    public void testPdfExport() throws Exception
    {
        checkExportUri("iteration", "pdf");
    }

    public void testReportExport() throws Exception
    {
        checkExportUri("iteration", "jrpdf");
    }

    public void testAccuracyPageLoad()
    {
        tester.clickLinkWithKey("iteration.link.accuracy");
        tester.assertKeyNotPresent("security.notauthorized");
    }

    public void testStatisticsPageLoad()
    {
        tester.clickLinkWithKey("iteration.link.statistics");
        tester.assertKeyNotPresent("security.notauthorized");
    }

    public void testHistoryPageLoad()
    {
        tester.clickLinkWithKey("history.link");
        tester.assertKeyNotPresent("security.notauthorized");
    }

    private void verifyHistorys(String eventType, String description)
    {
        tester.clickLink("aKH");
        tester.assertTitleEquals("History");
        tester.assertTextInTable(HistoryPage.CONTAINER_EVENT_TABLE_ID, eventType);
        tester.assertCellTextForRowWithTextAndColumnKeyEquals(HistoryPage.CONTAINER_EVENT_TABLE_ID,
                                                              description,
                                                              "history.tableheading.action",
                                                              eventType);
    }

}
