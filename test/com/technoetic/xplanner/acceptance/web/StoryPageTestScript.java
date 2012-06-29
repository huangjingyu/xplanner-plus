package com.technoetic.xplanner.acceptance.web;

import net.sf.xplanner.domain.History;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import com.technoetic.xplanner.actions.MoveContinueStoryAction;
import com.technoetic.xplanner.actions.MoveContinueTaskAction;
import com.technoetic.xplanner.domain.StoryDisposition;
import com.technoetic.xplanner.domain.TaskDisposition;
import com.technoetic.xplanner.views.HistoryPage;
import com.technoetic.xplanner.views.IterationAccuracyPage;

public class StoryPageTestScript extends AbstractStoryPageTestScript {
   //todo jm: remove dependency on TASK_XXX
   public static final String TASK_NAME = "Test Task";
   public static final String TASK_DESCRIPTION = "A test task";
   public static final String ESTIMATED_HOURS = "23.5";
   public static final String ESTIMATED_ORIGINAL_HOURS = "23.5";
   public static final String STORY_NAME = "Test Story For Origi. Hour.";
   public static final String ESTIMATED_HOURS_FOR_ADDED_OBJECT = "20";
   public static final String fromIterationName = "From Iteration";
   public static final String fromStoryName = "From Story";
   public static final String toStoryName = "To story";
   private static final String TASK_ESTIMATED_ORIGINAL_HOURS = "23.5";
   private static final String STORY_ESTIMATED_ORIGINAL_HOURS = "15.5";
   private static final String ADDED_TASK_HOURS = "23.5";
   private static final String ADDED_STORY_HOURS = "10.0";
   private final String ITERATION_NAME_FOR_ORG_EST_TEST = "Started Iteration for OrgEstHours test";
   private final String STORY_NAME_FOR_ORG_EST_TEST = "Story for OrgEstHours test";
   public StoryPageTestScript(String test) {
      super(test);
   }

   //todo jm convert class setup to direct db.
   public void setUp() throws Exception {
      super.setUp();
      simpleSetUp();
   }
   public void tearDown() throws Exception {
      tearDownLocalObjects();
      simpleTearDown();
      super.tearDown();
   }
   private void tearDownLocalObjects() {
      tester.deleteObjects(Task.class, "name", TASK_NAME);
      tester.deleteObjects(UserStory.class, "name", fromStoryName);
      tester.deleteObjects(Iteration.class, "name", fromIterationName);
   }
   public void testAddingAndDeletingNotes() {
      runNotesTests(XPlannerWebTester.STORY_PAGE);
   }
   public void testAddingAndDeletingTasks() {
      addTaskToCurrentStory();
      tester.clickEditLinkInRowWithText(TASK_NAME);

      // modify the task name
      String newTaskName = "A new name for task";
      tester.assertFormElementEquals("name", TASK_NAME);
      tester.assertFormElementEquals("estimatedHours", ESTIMATED_HOURS);
      tester.assertFormElementEquals("description", TASK_DESCRIPTION);
      tester.setFormElement("name", newTaskName);
      tester.submit();
      tester.assertOnStoryPage();
      tester.assertTextNotPresent(TASK_NAME);
      tester.assertTextPresent(newTaskName);

      // put back the original task
      addTaskToCurrentStory();
      // clean up both tasks
      tester.clickDeleteLinkForRowWithText(newTaskName);
      tester.assertTextNotPresent(newTaskName);
      tester.clickDeleteLinkForRowWithText(TASK_NAME);
      tester.assertTextNotPresent(TASK_NAME);
   }
   private void addTaskToCurrentStory() {
      tester.addTask(TASK_NAME, developerName, TASK_DESCRIPTION, ESTIMATED_HOURS);
      tester.assertOnStoryPage();
      tester.assertTextPresent(TASK_NAME);
      tester.assertKeyPresent("task.disposition.planned.name");
   }
   public void testChangeStoryDisposition() {
// todo jm why is it called action.edit.story? It should be generic and called action.edit (was story.link.edit before merge)
      tester.clickLinkWithKey("action.edit.story");
      tester.assertOptionEquals("dispositionName", tester.getMessage(StoryDisposition.PLANNED.getNameKey()));
      tester.selectOption("dispositionName", tester.getMessage(StoryDisposition.CARRIED_OVER.getNameKey()));
      tester.submit();
      assertDispositionEquals(storyName, StoryDisposition.CARRIED_OVER);
   }

   // todo jm reenable this test
   public void testContentAndLinks() {
        tester.assertOnStoryPage();
        tester.assertKeyPresent("story.label.status");
//        tester.assertKeyPresent("story.no_tasks");
//        tester.assertTextPresent(testStoryName);
//        tester.assertTextPresent(testStoryDescription);
//        tester.clickLinkWithKey("navigation.iteration");
//        iterationTester.assertOnIterationPage();
//        tester.clickLinkWithText(testStoryName);
//        tester.assertOnStoryPage();
//        tester.clickLinkWithKey("navigation.project");
//        tester.assertOnProjectPage();
//        tester.assertActualHoursColumnPresent();
   }
   public void testContinueTaskInDifferentStory() throws Exception {
      setUpMoveContinueTask();
      moveContinueTask(MoveContinueTaskAction.CONTINUE_ACTION);
      tester.assertTextPresent(TASK_NAME);
      assertDispositionEquals(TASK_NAME, TaskDisposition.PLANNED);
      verifyHistorys(History.CONTINUED);
      assertContinuingTaskExistsInTargetIteration();
      assertDispositionEquals(TASK_NAME, TaskDisposition.CARRIED_OVER);
      verifyTargetStoryHistory(History.CONTINUED);
      verifyTaskHistory(History.CONTINUED);
   }
   public void testMoveTaskToDifferentStory() throws Exception {
      setUpMoveContinueTask();
      moveContinueTask(MoveContinueTaskAction.MOVE_ACTION);
      tester.assertTextNotPresent(TASK_NAME);
//       assertDispositionEquals(TASK_NAME, TaskDisposition.PLANNED);
      verifyHistorys(History.MOVED_OUT);
      assertContinuingTaskExistsInTargetIteration();
      assertDispositionEquals(TASK_NAME, TaskDisposition.PLANNED);
      verifyTargetStoryHistory(History.MOVED_IN);
      verifyTaskHistory(History.MOVED);
   }
   private void moveContinueTask(String actionType) {
      tester.clickContinueLinkInRowWithText(TASK_NAME);
      tester.assertOnMoveContinueTaskPage(TASK_NAME);
      tester.selectOption("targetStoryId", testProjectName + " :: " + testIterationName + " :: " + storyName);
      tester.clickButton(actionType);
      tester.assertOnStoryPage(fromStoryName);
//        assertDispositionEquals(TASK_NAME, TaskDisposition.PLANNED);
   }
   private void verifyHistorys(String moveType) {
      tester.clickLink("aKH");
      tester.assertTitleEquals("History");
      tester.assertTextInTable(HistoryPage.EVENT_TABLE_ID, moveType);
   }

   //todo jm change navigation to new optimized one
   private void assertContinuingTaskExistsInTargetIteration() throws Exception {
      tester.gotoRelativeUrl("/do/view/projects");
      tester.clickLinkWithText(testProjectName);
      tester.clickLinkWithText(testIterationName);
      tester.clickLinkWithText(storyName);
      tester.assertOnStoryPage(storyName);
      tester.assertTextPresent(TASK_NAME);
   }
   private void verifyTargetStoryHistory(String actionType) throws Exception {
      tester.gotoProjectsPage();
      tester.clickLinkWithText(testProjectName);
      tester.clickLinkWithText(testIterationName);
      tester.clickLinkWithText(storyName);
      verifyHistorys(actionType);
   }
   private void verifyTaskHistory(String actionType) throws Exception {
      tester.gotoProjectsPage();
      tester.clickLinkWithText(testProjectName);
      tester.clickLinkWithText(testIterationName);
      tester.clickLinkWithText(storyName);
      tester.clickLinkWithText(TASK_NAME);
      verifyHistorys(actionType);
   }
   public void testMoveTaskToDifferentStoryInTheSameStartedIteration() throws Exception {
      setUpMoveContinueTask();
      tester.gotoProjectsPage();
      tester.clickLinkWithText(project.getName());
      tester.clickLinkWithText(fromIterationName);
      tester.addUserStory(toStoryName, "desc.", "10", "1");
      tester.assertTextPresent(toStoryName);
      iterationTester.startCurrentIteration();
      tester.clickLinkWithText(fromStoryName);
      moveContinueTask(MoveContinueTaskAction.MOVE_ACTION, TASK_NAME, fromStoryName, toStoryName, fromIterationName);
      tester.gotoProjectsPage();
      tester.clickLinkWithText(project.getName());
      tester.clickLinkWithText(fromIterationName);
      tester.clickLinkWithText(toStoryName);
      tester.assertTextPresent(TASK_NAME);
      tester.assertKeyInTable("objecttable", TaskDisposition.PLANNED.getNameKey());
   }
   private void setUpMoveContinueTask() throws Exception {
      tester.gotoRelativeUrl("/do/view/projects");
      tester.clickLinkWithText(testProjectName);
      // create the to story and to task
      createIterationAndStory(fromIterationName, fromStoryName, 15, 28);
      // create the from story and from task
      addTaskToCurrentStory();
   }
   private void moveContinueTask(String actionType, String taskName, String fromStoryName, String targetStoryName, String targetIterationName) {
      tester.clickContinueLinkInRowWithText(taskName);
      tester.assertOnMoveContinueTaskPage(taskName);
      tester.selectOption("targetStoryId", testProjectName + " :: " + targetIterationName + " :: " + targetStoryName);
      tester.clickButton(actionType);
      tester.assertOnStoryPage(fromStoryName);
   }
   public void testMoveTaskToDifferentStory_Localized() throws Exception {
      setUpMoveContinueTask();
      try {
         tester.changeLocale("fr");
         moveContinueTask(MoveContinueTaskAction.MOVE_ACTION);
      } catch (Exception ex) {
         fail("Cannot continue task");
      } finally {
         tester.changeLocale(null);
      }
   }
   public void testNoMoveContinueTaskInSameStory() throws Exception {
      addTaskToCurrentStory();
      tester.clickContinueLinkInRowWithText(TASK_NAME);
      tester.assertOnMoveContinueTaskPage(TASK_NAME);
      tester.assertOptionNotListed("targetStoryId", testProjectName + " :: " + testIterationName + " :: " + storyName);
   }
   public void testOrgEstHoursInRestartedIterationWithAddedStory() throws Exception {
      performOrgEstHoursWithDeletedTaskTest();
      tester.gotoProjectsPage();
      tester.clickLinkWithText(testIterationName);
      iterationTester.closeCurrentIteration();
      tester.gotoProjectsPage();
      tester.clickLinkWithText(testIterationName);
      tester.addUserStory(STORY_NAME, "", ESTIMATED_HOURS_FOR_ADDED_OBJECT, "1");
      tester.gotoProjectsPage();
      tester.clickLinkWithText(testIterationName);
      iterationTester.startCurrentIteration();
      tester.clickLinkWithKey("iteration.link.accuracy");
      tester.assertCellTextForRowIndexAndColumnKeyContains(IterationAccuracyPage.ITERATION_STATUS_TABLE_ID,
                                                           1,
                                                           "iteration.label.story",
                                                           "35.5");
      tester.assertCellTextForRowIndexAndColumnKeyContains(IterationAccuracyPage.ITERATION_STATUS_TABLE_ID,
                                                           1,
                                                           "iteration.label.task",
                                                           ESTIMATED_ORIGINAL_HOURS);
   }
   public void testOrgEstHoursInRestartedIterationWithDeletedAndAddedTask() throws Exception {
      performOrgEstHoursWithDeletedTaskTest();
      tester.gotoProjectsPage();
      tester.clickLinkWithText(testIterationName);
      iterationTester.closeCurrentIteration();
      goToTestStoryPage();
      tester.addTask(TASK_NAME, developerName, TASK_DESCRIPTION, ESTIMATED_HOURS_FOR_ADDED_OBJECT);
      tester.gotoProjectsPage();
      tester.clickLinkWithText(testIterationName);
      iterationTester.startCurrentIteration();
      tester.clickLinkWithKey("iteration.link.accuracy");
      tester.assertCellTextForRowIndexAndColumnKeyContains(IterationAccuracyPage.ITERATION_STATUS_TABLE_ID,
                                                           1,
                                                           "iteration.label.story",
                                                           "15.5");
      tester.assertCellTextForRowIndexAndColumnKeyContains(IterationAccuracyPage.ITERATION_STATUS_TABLE_ID,
                                                           1,
                                                           "iteration.label.task",
                                                           ESTIMATED_ORIGINAL_HOURS);
   }
   public void testOrgEstHoursInStartedIterationWithDeletedTask() throws Exception {
      performOrgEstHoursWithDeletedTaskTest();
   }
   private void performOrgEstHoursWithDeletedTaskTest() throws Exception {
      addTaskToCurrentStory();
      tester.gotoProjectsPage();
      tester.clickLinkWithText(testIterationName);
      iterationTester.startCurrentIteration();
      tester.clickLinkWithKey("iteration.link.accuracy");
      tester.assertCellTextForRowIndexAndColumnKeyContains(IterationAccuracyPage.ITERATION_STATUS_TABLE_ID,
                                                           1,
                                                           "iteration.label.story",
                                                           "15.5");
      tester.assertCellTextForRowIndexAndColumnKeyContains(IterationAccuracyPage.ITERATION_STATUS_TABLE_ID,
                                                           1,
                                                           "iteration.label.task",
                                                           ESTIMATED_ORIGINAL_HOURS);
      goToTestStoryPage();
      tester.clickDeleteLinkForRowWithText(TASK_NAME);
      tester.assertTextNotPresent(TASK_NAME);
      tester.gotoProjectsPage();
      tester.clickLinkWithText(testIterationName);
      tester.clickLinkWithKey("iteration.link.accuracy");
      tester.assertCellTextForRowIndexAndColumnKeyContains(IterationAccuracyPage.ITERATION_STATUS_TABLE_ID,
                                                           1,
                                                           "iteration.label.story",
                                                           "15.5");
      tester.assertCellTextForRowIndexAndColumnKeyContains(IterationAccuracyPage.ITERATION_STATUS_TABLE_ID,
                                                           1,
                                                           "iteration.label.task",
                                                           ESTIMATED_ORIGINAL_HOURS);
   }
   public void testOrgEstHoursWhileMovedFromNotStartedToStartedIteration() throws Exception {
      goToTestStoryPage();
      tester.addTask(TASK_NAME, developerName, TASK_DESCRIPTION, ESTIMATED_HOURS);
      tester.gotoProjectsPage();
      tester.clickLinkWithText(testIterationName);
      iterationTester.startCurrentIteration();
      tester.gotoProjectsPage();
      tester.clickLinkWithText(project.getName());
      iterationTester.addIteration(ITERATION_NAME_FOR_ORG_EST_TEST,
                                   tester.dateStringForNDaysAway(1),
                                   tester.dateStringForNDaysAway(15),
                                   "desc.");
      tester.clickLinkWithText(ITERATION_NAME_FOR_ORG_EST_TEST);
      tester.addUserStory(STORY_NAME_FOR_ORG_EST_TEST, "desc.", "10", "1");
      tester.clickLinkWithText(STORY_NAME_FOR_ORG_EST_TEST);
      tester.addTask(TASK_NAME, developerName, TASK_DESCRIPTION, ESTIMATED_HOURS);
      tester.gotoProjectsPage();
      tester.clickLinkWithText(project.getName());
      tester.clickLinkWithText(ITERATION_NAME_FOR_ORG_EST_TEST);
      tester.clickContinueLinkInRowWithText(STORY_NAME_FOR_ORG_EST_TEST);
      tester.assertOnMoveContinueStoryPage(STORY_NAME_FOR_ORG_EST_TEST);
      tester.selectOption("targetIterationId", project.getName() + " :: " + iteration.getName());
      tester.clickButton(MoveContinueStoryAction.MOVE_ACTION);
      iterationTester.assertOnIterationPage();
      tester.gotoProjectsPage();
      tester.clickLinkWithText(project.getName());
      tester.clickLinkWithText(iteration.getName());
      tester.clickLinkWithKey("iteration.link.accuracy");
      tester.assertCellTextForRowIndexAndColumnKeyContains(IterationAccuracyPage.ITERATION_STATUS_TABLE_ID,
                                                           1,
                                                           "iteration.label.story",
                                                           STORY_ESTIMATED_ORIGINAL_HOURS);
      tester.assertCellTextForRowIndexAndColumnKeyContains(IterationAccuracyPage.ITERATION_STATUS_TABLE_ID,
                                                           1,
                                                           "iteration.label.task",
                                                           TASK_ESTIMATED_ORIGINAL_HOURS);
      tester.assertCellTextForRowIndexAndColumnKeyContains(IterationAccuracyPage.ITERATION_STATUS_TABLE_ID,
                                                           2,
                                                           "iteration.label.story",
                                                           ADDED_STORY_HOURS);
      tester.assertCellTextForRowIndexAndColumnKeyContains(IterationAccuracyPage.ITERATION_STATUS_TABLE_ID,
                                                           2,
                                                           "iteration.label.task",
                                                           ADDED_TASK_HOURS);
      tester.gotoProjectsPage();
      tester.clickLinkWithText(project.getName());
      tester.clickLinkWithText(iteration.getName());
      tester.clickLinkWithText(STORY_NAME_FOR_ORG_EST_TEST);
      tester.addTask(TASK_NAME, developerName, TASK_DESCRIPTION, ESTIMATED_HOURS);
      tester.clickLinkWithText(project.getName());
      tester.clickLinkWithText(iteration.getName());
      tester.clickLinkWithKey("iteration.link.accuracy");
      tester.assertCellTextForRowIndexAndColumnKeyContains(IterationAccuracyPage.ITERATION_STATUS_TABLE_ID,
                                                           1,
                                                           "iteration.label.story",
                                                           STORY_ESTIMATED_ORIGINAL_HOURS);
      tester.assertCellTextForRowIndexAndColumnKeyContains(IterationAccuracyPage.ITERATION_STATUS_TABLE_ID,
                                                           1,
                                                           "iteration.label.task",
                                                           TASK_ESTIMATED_ORIGINAL_HOURS);
   }
   public void testPdfExport() throws Exception {
      checkExportUri("userstory", "pdf");
   }
   public void testReportExport() throws Exception {
      checkExportUri("userstory", "jrpdf");
   }
   protected void traverseLinkWithKeyAndReturn(String key) throws Exception {
      tester.clickLinkWithKey(key);
      tester.gotoPage("view", "projects", 0);
   }
}