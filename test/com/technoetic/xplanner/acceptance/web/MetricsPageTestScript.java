package com.technoetic.xplanner.acceptance.web;

import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;


public class MetricsPageTestScript extends AbstractPageTestScript
{
    private final String sysAdmin = "sysadmin";
    private final String testerTT = "TT";
    private final String testerYY = "YY";
    private final String ttTaskEstimatedHours = "7.7";
    private final String testerTask = "TesterTask";
    private final String otherDeveloperTaskName = "other developer's task";
    private final String anotherTestTaskName = "Another Task";
    private final String secondStoryName = "Second Story";
    private final String thirdStoryName = "Third Story";
    private final String firstTaskInThirdStory = "Third Story First Task";
    private final String secondTaskInThirdStory = "Third Story Second Task";
    private String testTaskId = null;
    private String otherDeveloperTaskId = null;
    public String anotherTestTaskId = null;

    public MetricsPageTestScript(String test)
    {
        super(test);
    }

    public void setUp() throws Exception
    {
        super.setUp();
        simpleSetUp();
    }

    public void tearDown() throws Exception
    {
        deleteLocalObjects();
        super.simpleTearDown();
        super.tearDown();
    }

    public void goToTestIterationMetricsPage() throws Exception
    {
        tester.gotoProjectsPage();
        tester.clickLinkWithText(testProjectName);
        tester.clickLinkWithText(testIterationName);
        tester.clickLinkWithKey("iteration.link.metrics");
    }

   public void testContentAndLinks() throws Exception
   {
       goToTestIterationMetricsPage();
       tester.assertKeyPresent("iteration.metrics.prefix");
       tester.assertKeyPresent("iteration.metrics.total_hours");
   }

    public void testActualTime() throws Exception
    {
        //goToTestStoryPage();
        testTaskId = tester.addTask(testTaskName, developerName, testTaskDescription, testTaskEstimatedHours);
        tester.clickLinkWithText(testTaskName);

        tester.clickLinkWithKey("action.edittime.task");
        tester.setTimeEntry(0, 0, 4, developer.getName());
        iterationTester.assertOnStartIterationPromptPageAndStart();

//        tester.clickEditTimeImage();
        tester.clickLinkWithKey("action.edittime.task");
        tester.setTimeEntry(1, 6, 11, developer.getName());
        goToTestIterationMetricsPage();

        Double workedHoursForOwnTasks = new Double(9.0);
        assertMetricsCellValueForRowWithKey("totalTable",
                                            developerName,
                                            "iteration.metrics.tableheading.total",
                                            workedHoursForOwnTasks);
        assertMetricsCellValueForRowWithKey("acceptedTable",
                                            this.developerName,
                                            "iteration.metrics.tableheading.worked_hours",
                                            workedHoursForOwnTasks);
        goToTestStoryPage();
        otherDeveloperTaskId = tester.addTask(otherDeveloperTaskName,
                                              sysAdmin,
                                              testTaskDescription,
                                              testTaskEstimatedHours);
        tester.clickLinkWithText(otherDeveloperTaskName);
        tester.clickLinkWithKey("action.edittime.task");
        tester.setTimeEntry(0, 0, 4, developer.getName());
        goToTestIterationMetricsPage();
        Double totalWorkedHours = new Double(13.0);
        assertMetricsCellValueForRowWithKey("totalTable",
                                            developerName,
                                            "iteration.metrics.tableheading.total",
                                            totalWorkedHours);
        assertMetricsCellValueForRowWithKey("acceptedTable",
                                            this.developerName,
                                            "iteration.metrics.tableheading.worked_hours",
                                            workedHoursForOwnTasks);
    }


    public void testZeroHourTask() throws Exception
    {
        goToTestStoryPage();
        tester.addTask(testTaskName, developerName, testTaskDescription, "0.0");
        goToTestIterationMetricsPage();
        tester.assertTextNotPresent(developerName);
        tester.assertTextPresent("Total Person Hours Worked: 0.0");
        tester.assertTextPresent("Total Paired Hours Percentage: 0.0%");
    }

    public void testTotalHoursTable() throws Exception
    {
        personTester.addPerson(testerTT, "tt", "tt", "tt", "tt");
        tester.gotoProjectsPage();
        tester.clickLinkWithText(testIterationName);
        iterationTester.startCurrentIteration();
        updateStoryTracker(sysAdmin, storyName);

        goToTestStoryPage();
        anotherTestTaskId = tester.addTask(anotherTestTaskName, sysAdmin, testTaskDescription, testTaskEstimatedHours);
        tester.clickEnterTimeLinkInRowWithText(anotherTestTaskName);
        tester.setTimeEntry(0, 0, 4, sysAdmin);
        goToTestIterationMetricsPage();
        assertTotalWorkedHoursTable(sysAdmin, 4, 0, 4);

        goToTestStoryPage();
        testTaskId = tester.addTask(testTaskName, sysAdmin, testTaskDescription, testTaskEstimatedHours);
        tester.clickEnterTimeLinkInRowWithText(testTaskName);
        tester.setTimeEntry(0, 1, 4, sysAdmin, testerTT);
        goToTestIterationMetricsPage();
        assertTotalWorkedHoursTable(sysAdmin, 7, 3, 4);
        assertTotalWorkedHoursTable(testerTT, 3, 3, 0);
    }

    private void assertTotalWorkedHoursTable(String person, int totalHours, int pairedHours, int soloHours)
        throws Exception
    {
        assertMetricsCellValueForRowWithKey("totalTable",
                                            person,
                                            "iteration.metrics.tableheading.total",
                                            new Double(totalHours));
        assertMetricsCellValueForRowWithKey("totalTable",
                                            person,
                                            "iteration.metrics.tableheading.paired_hours",
                                            new Double(pairedHours));
        assertMetricsCellValueForRowWithKey("totalTable",
                                            person,
                                            "iteration.metrics.tableheading.solo_hours",
                                            new Double(soloHours));
    }

    private void assertMetricsCellValueForRowWithKey(String tableId,
                                                     String person,
                                                     String key, Double hours)
    {
        if (hours.doubleValue() != 0.0d)
        {
            tester.assertCellNumberForRowWithTextAndColumnKeyEquals(tableId,
                                                                    person,
                                                                    key,
                                                                    hours);
        }
        else
        {
            tester.assertCellTextForRowWithTextAndColumnKeyEquals(tableId, person, key, "");
        }
    }

    public void testHoursAcceptedGraph() throws Exception
    {
        tester.gotoProjectsPage();
        tester.clickLinkWithText(testIterationName);
        updateStoryTracker(sysAdmin, storyName);
        goToTestIterationMetricsPage();
        final Double totalHours = new Double(estimatedHoursString);
        assertMetricsCellValueForRowWithKey("acceptedTable",
                                            sysAdmin,
                                            "iteration.metrics.tableheading.total",
                                            totalHours);
        tester.assertCellTextForRowWithTextAndColumnKeyEquals("acceptedTable",
                                                              sysAdmin,
                                                              "iteration.metrics.tableheading.worked_hours",
                                                              "");

        checkTaskHoursAfterAddingTasks();
        checkMetricsAfterAddingSecondPerson();
        checkMetricsAfterAddingSecondStory();
        checkMetricsAfterAddingThirdStory();
    }

    private void deleteLocalObjects()
    {
        tester.deleteObjects(Person.class, "name", testerTT);
        tester.deleteObjects(Person.class, "name", testerYY);
        if (testTaskId != null)
            deleteLocalTimeEntry(testTaskId);
        if (anotherTestTaskId != null)
            deleteLocalTimeEntry(anotherTestTaskId);
        tester.deleteObjects(Task.class, "name", anotherTestTaskName);

        tester.deleteObjects(Task.class, "name", testerTask);
        tester.deleteObjects(Task.class, "name", firstTaskInThirdStory);
        tester.deleteObjects(Task.class, "name", secondTaskInThirdStory);

        if (otherDeveloperTaskId != null)
            deleteLocalTimeEntry(otherDeveloperTaskId);
        tester.deleteObjects(Task.class, "name", otherDeveloperTaskName);

        tester.deleteObjects(UserStory.class, "name", secondStoryName);
        tester.deleteObjects(UserStory.class, "name", thirdStoryName);
    }

    private void checkMetricsAfterAddingThirdStory() throws Exception
    {
        tester.gotoProjectsPage();
        tester.clickLinkWithText(testIterationName);
        tester.addUserStory(thirdStoryName, "Third Story Description", "21.0", "1");
        updateStoryTracker(testerYY, thirdStoryName);
        tester.assertOnStoryPage(thirdStoryName);
        tester.addTask(firstTaskInThirdStory, sysAdmin, "Some Description", "3");
        tester.addTask(secondTaskInThirdStory, testerYY, "Some Description", "7");

        goToTestIterationMetricsPage();
        assertMetricsCellValueForRowWithKey("acceptedTable",
                                            sysAdmin,
                                            "iteration.metrics.tableheading.total",
                                            new Double(23.5));
        assertMetricsCellValueForRowWithKey("acceptedTable",
                                            testerYY,
                                            "iteration.metrics.tableheading.total",
                                            new Double(18.0));
        assertMetricsCellValueForRowWithKey("acceptedTable",
                                            testerTT,
                                            "iteration.metrics.tableheading.total",
                                            new Double(ttTaskEstimatedHours));
    }

    private void checkMetricsAfterAddingSecondStory() throws Exception
    {
        personTester.addPerson(testerYY, "yy", "yy", "yy", "yy");

        String secondStoryEstimatedHours = "11.0";
        String storyDescription = "Second Story Description";

        tester.gotoProjectsPage();
        tester.clickLinkWithText(testIterationName);
        tester.addUserStory(secondStoryName, storyDescription, secondStoryEstimatedHours, "1");
        updateStoryTracker(testerYY, secondStoryName);


        goToTestIterationMetricsPage();
        assertMetricsCellValueForRowWithKey("acceptedTable",
                                            sysAdmin,
                                            "iteration.metrics.tableheading.total",
                                            new Double(testTaskEstimatedHours));
        assertMetricsCellValueForRowWithKey("acceptedTable", testerTT,
                                            "iteration.metrics.tableheading.total",
                                            new Double(ttTaskEstimatedHours));
        assertMetricsCellValueForRowWithKey("acceptedTable", testerYY,
                                            "iteration.metrics.tableheading.total",
                                            new Double(secondStoryEstimatedHours));
    }

    private void checkMetricsAfterAddingSecondPerson() throws Exception
    {
        String anotherTaskDescription = "Tester Task Description";
        personTester.addPerson(testerTT, "tt", "tt", "tt", "tt");
        goToTestStoryPage();
        tester.addTask(testerTask,
                       testerTT,
                       anotherTaskDescription,
                       ttTaskEstimatedHours);

        goToTestIterationMetricsPage();
        assertMetricsCellValueForRowWithKey("acceptedTable",
                                            sysAdmin,
                                            "iteration.metrics.tableheading.task",
                                            new Double(testTaskEstimatedHours));
        assertMetricsCellValueForRowWithKey("acceptedTable",
                                            testerTT,
                                            "iteration.metrics.tableheading.task",
                                            new Double(ttTaskEstimatedHours));
    }

    private void checkTaskHoursAfterAddingTasks() throws Exception
    {
        goToTestStoryPage();
        tester.addTask(testTaskName, sysAdmin, testTaskDescription, testTaskEstimatedHours);
        goToTestIterationMetricsPage();
        assertMetricsCellValueForRowWithKey("acceptedTable",
                                            sysAdmin,
                                            "iteration.metrics.tableheading.task",
                                            new Double(testTaskEstimatedHours));
    }

    private void updateStoryTracker(String tracker, String testStoryName)
    {
        tester.clickLinkWithText(testStoryName);
        tester.assertTextPresent(testStoryName);
        tester.assertLinkPresentWithKey("action.edit.story");
        tester.clickLinkWithKey("action.edit.story");
        tester.selectOption("trackerId", tracker);
        tester.submit();
    }

    protected void traverseLinkWithKeyAndReturn(String key) throws Exception
    {
        tester.clickLinkWithKey(key);
        tester.gotoPage("view", "projects", 0);
    }
}
