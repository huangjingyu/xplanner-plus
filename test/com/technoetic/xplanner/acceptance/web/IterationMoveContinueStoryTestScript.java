package com.technoetic.xplanner.acceptance.web;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.xplanner.domain.History;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.HibernateException;

import com.meterware.httpunit.WebForm;
import com.technoetic.xplanner.actions.MoveContinueStoryAction;
import com.technoetic.xplanner.actions.MoveContinueTaskAction;
import com.technoetic.xplanner.domain.Disposition;
import com.technoetic.xplanner.domain.IterationStatus;
import com.technoetic.xplanner.domain.StoryDisposition;
import com.technoetic.xplanner.domain.TaskDisposition;
import com.technoetic.xplanner.domain.repository.RepositoryException;
import com.technoetic.xplanner.testing.DateHelper;
import com.technoetic.xplanner.views.HistoryPage;


public class IterationMoveContinueStoryTestScript extends AbstractPageTestScript {
   public static final String STORIES_TABLE_ID = "objecttable";
   public static final String STORY_DISPOSITION_COLUMN_KEY = "stories.tableheading.disposition";
   public static final String TASK_DISPOSITION_COLUMN_KEY = "tasks.tableheading.disposition";
   public static final String TASKS_TABLE_ID = "objecttable";

   private final String testStoryNameForMoveTest = "Test story name";
   private String fromStoryName = "From Story";
   private Iteration fromIteration;
   private String fromIterationName = "From Iteration";
   private Iteration toIteration;
   private String testFromTaskName = "";
   private String testTaskName = "Test task name";

   private final String estimatedHours = "23.5";
   private Project project;
   private UserStory fromStory;
   private UserStory toStory;

  public IterationMoveContinueStoryTestScript(String test) {
     super(test);
  }

   @Override
public void setUp() throws Exception {
      super.setUp();
      project = newProject();
      testProjectName = project.getName();
      setUpTestPerson();
      setUpFromIterationWithOneStory();
      UserStory firstStory = newUserStory(fromIteration);
      firstStory.setOrderNo(1);
      fromStory.setOrderNo(2);
      setUpToIteration();
      commitCloseAndOpenSession();
      tester.login();
      gotoIteration(fromIterationName);

   }

   @Override
public void tearDown() throws Exception {
      super.tearDown();
   }

   public void testMoveStoryAndTaskToStartedIteration() {
      gotoIteration(toIteration.getName());
      startIteration();
      gotoIteration(fromIteration.getName());
      moveStory(this.fromStoryName, fromIteration.getName(), toIteration.getName());
      gotoIteration(toIteration.getName());
      String expectedDisposition = Disposition.ADDED_TEXT;
      assertStoryDisposition(this.fromStoryName, expectedDisposition);
      tester.clickLinkWithText(this.fromStoryName);
      tester.assertTextPresent(expectedDisposition);
   }

   private void assertStoryDisposition(String storyName, String expectedDisposition) {
      tester.assertCellTextForRowWithTextAndColumnKeyEquals("objecttable",
                                                            storyName,
                                                            "stories.tableheading.disposition",
                                                            expectedDisposition);
   }

   public void testMoveStoryAndTaskToFutureIteration() throws Exception {
      startIteration();
      String newStoryId = tester.addUserStory(testStoryNameForMoveTest, "Test story description", "14", "1");
      tester.assertTextPresent(Disposition.ADDED_TEXT);
      tester.clickLinkWithText(testStoryNameForMoveTest);

      tester.addTask("Test task name", "sysadmin", "Test task desc.", "14");
      tester.assertTextPresent(Disposition.DISCOVERED_TEXT);
      gotoIteration(fromIterationName);

      moveStory(testStoryNameForMoveTest, fromIterationName, toIteration.getName());

      gotoIteration(toIteration.getName());

      assertStoryOrder(newStoryId, 1);
      assertStoryOrder(String.valueOf(toStory.getId()), 2);
      tester.clickLinkWithText(testStoryNameForMoveTest);

      tester.assertKeyInTable("objecttable", TaskDisposition.PLANNED.getNameKey());
   }

  private void assertStoryOrder(String storyId, int expectedOrder) {
//    tester.getDialog().setWorkingForm("reordering");
//    WebForm form = tester.getDialog().getForm();
//    int index = getIndexOfFormArrayParamWithValue(form, "storyId", storyId);
//    String actualOrder = form.getParameterValue("orderNo[" + index + "]");
    assertEquals("story " + storyId + " order ", String.valueOf(expectedOrder), "111111111111"); //actualOrder);
  }

  private int getIndexOfFormArrayParamWithValue(WebForm form, String arrayParamName, String value) {
    List params = getParamsWithNameStartingWith(form, arrayParamName);
    for (int i = 0; i < params.size(); i++) {
      String param = (String) params.get(i);
      if (value.equals(form.getParameterValue(param))) {
        return getIndexFromArrayParam(param);
      }
    }
    fail("Could not find storyId[] param with story id " + value + " in " + dumpFormParamsAndTheirValues(form));
    return -1;
  }

  private String dumpFormParamsAndTheirValues(WebForm form) {
    StringBuffer buf = new StringBuffer(100);
    for (int i = 0; i < form.getParameterNames().length; i++) {
      String name = form.getParameterNames()[i];
      String value = form.getParameterValue(name);
      if (i > 0) buf.append(", ");
      buf.append(name + "=" + value);
    }
    return buf.toString();
  }

  private int getIndexFromArrayParam(String param) {
    Pattern pattern = Pattern.compile("\\w+\\[(\\d+)\\]" );
    Matcher matcher = pattern.matcher(param);
    if (!matcher.matches()) throw new IllegalArgumentException("Cannot find index in param " + param);
    String index = matcher.group(1);
    return Integer.parseInt(index);
  }
  
  private List getParamsWithNameStartingWith(WebForm form, final String prefix) {
    List params = new ArrayList(Arrays.asList(form.getParameterNames()));
    CollectionUtils.filter(params, new Predicate() {
      public boolean evaluate(Object object) {
        return ((String) object).startsWith(prefix);
      }
    });
    return params;
  }

  public void testMoveTaskToPlannedStoryInFutureIteration() throws Exception {
     gotoIteration(toIteration.getName());
     tester.clickLinkWithText(storyName);
     tester.clickDeleteLinkForRowWithText(testTaskName);
     moveTaskAndCheckDisposition(Disposition.ADDED_TEXT, Disposition.PLANNED_TEXT);
  }

   public void testMoveTaskToAddedStoryInFutureIteration() throws Exception {
      gotoIteration(toIteration.getName());
      tester.clickLinkWithText(storyName);
      tester.clickDeleteLinkForRowWithText(testTaskName);
      tester.clickLinkWithText("Edit");
      tester.selectOption("dispositionName", Disposition.ADDED_TEXT);
      tester.submit();
      moveTaskAndCheckDisposition(Disposition.PLANNED_TEXT, Disposition.DISCOVERED_TEXT);
   }

   public void testMoveTaskToStoryInStartedIteration() throws Exception {
      gotoIteration(toIteration.getName());
      startIteration();
      tester.clickLinkWithText(storyName);
      tester.clickDeleteLinkForRowWithText(testTaskName);
      moveTaskAndCheckDisposition(Disposition.PLANNED_TEXT, Disposition.ADDED_TEXT);
   }

   public void testContinueUnfinishiedStories() throws Exception {
      //TODO remove iteration param since it does not do anything
      tester.assertTextPresent(estimatedHours); //storyEstimate;
      startIteration();
      closeIteration();

      closeIterationAndContinueUnfinishedStories(testIterationName);

      gotoIteration(fromIterationName);
      iterationTester.assertCurrentIterationClosed();
   }

   public void testNoFutureIterationToContinueUnfishiedStories() throws Exception {
      // remove the toIteration so that there are no future iteration
      deleteObject(toIteration);
      commitCloseAndOpenSession();

      startIteration();
      closeIteration();

      tester.assertKeyPresent("iteration.continue.unfinished.stories");
      // Note: If following assert fails verify there isn't any other iteration later than the fromIteration
      tester.assertKeyPresent("iteration.status.editor.no_future_iteration");
   }

   public void testDefaultDisposition() throws Exception {
      iterationTester.assertCurrentIterationClosed();
      assertNewStoryDisposition("Planned Story", tester.getMessage(StoryDisposition.PLANNED.getNameKey()));

      startIteration();

      assertNewStoryDisposition("Added Story", tester.getMessage(StoryDisposition.ADDED.getNameKey()));
   }

   public void testNoContinueStoryInSameIteration() throws Exception {
      tester.clickContinueLinkInRowWithText(fromStoryName);
      tester.assertOnMoveContinueStoryPage(fromStoryName);
      tester.selectOption("targetIterationId", testProjectName + " :: " + fromIterationName);
      tester.clickButton(MoveContinueStoryAction.CONTINUE_ACTION);
      tester.assertKeyPresent("story.editor.same_iteration");
//      TODO modify tag to exclude the original iteration from the options
//        tester.assertOptionNotListed("targetIterationId", testProjectName + " :: " + fromIterationName);
   }

   public void testMoveStoryToDifferentIteration() throws Exception {
      moveContinueStory(MoveContinueStoryAction.MOVE_ACTION, testIterationName, fromIterationName);
      tester.assertTextNotPresent(fromStoryName);
      verifyHistorys(History.MOVED_OUT, fromStoryName);
      gotoIteration(testIterationName);
      tester.assertTextPresent(fromStoryName);
      verifyDisposition(fromStoryName, StoryDisposition.PLANNED);
      verifyTargetIterationHistory(History.MOVED_IN, fromStoryName);
      verifyMovedStoryHistory();
   }

   public void testContinueStoryInDifferentIteration() throws Exception {
      moveContinueStory(MoveContinueStoryAction.CONTINUE_ACTION, testIterationName, fromIterationName);
      tester.assertTextPresent(fromStoryName);

      assertStoryOrder(String.valueOf(fromStory.getId()), 2);
      verifyHistorys(History.CONTINUED, fromStoryName + " in iteration: " + testIterationName);
      gotoIteration(testIterationName);
      tester.assertTextPresent(fromStoryName);
      verifyDisposition(fromStoryName, StoryDisposition.CARRIED_OVER);
      tester.assertCellTextForRowWithTextAndColumnKeyEquals("objecttable",
                                                            fromStoryName,
                                                            "stories.tableheading.estimated_original_hours",
                                                            estimatedHours);
      gotoIteration(toIteration.getName());
      tester.clickLinkWithText(fromStoryName);
      tester.assertCellTextForRowWithTextAndColumnKeyEquals(TASKS_TABLE_ID,
                                                            testFromTaskName,
                                                            TASK_DISPOSITION_COLUMN_KEY,
                                                            tester.getMessage(TaskDisposition.CARRIED_OVER.getNameKey()));
      tester.gotoProjectsPage();
      tester.clickLinkWithText(testProjectName);
      gotoIteration(testIterationName);
      verifyHistorys(History.CONTINUED, fromStoryName + " in iteration: " + fromIterationName);
      verifyTargetIterationHistory(History.CONTINUED, fromStoryName);
   }

   public void testContinueStoryInDifferentIteration_Localized() throws Exception {
      try {
         tester.changeLocale("fr");
         moveContinueStory(MoveContinueStoryAction.CONTINUE_ACTION, testIterationName,
                           fromIterationName);
      }
      catch (Exception ex) {
         fail("Cannot continue story");
      }
      finally {
         tester.changeLocale(null);
      }
   }

   public void testContinueFromFutureIteration_IsNotAvailable() throws Exception {
      setIterationDatesFromNow(fromIteration, 4, 8);
      tester.clickContinueLinkInRowWithText(fromStoryName);
      tester.assertOnMoveContinueStoryPage(fromStoryName);
      tester.selectOption("targetIterationId", testProjectName + " :: " + toIteration.getName());
      tester.assertButtonNotPresent(MoveContinueStoryAction.CONTINUE_ACTION);

   }

   public void testMoveFromFutureIteration_ToCurrentIterationDefaultDispositionIsAdded() throws Exception {
      setIterationDatesFromNow(fromIteration, 4, 8);
      setIterationDatesFromNow(toIteration, -1, 3);
      moveContinueStory(MoveContinueStoryAction.MOVE_ACTION, testIterationName, fromIterationName);
      gotoIteration(testIterationName);
      assertStoryDisposition(fromStoryName, Disposition.PLANNED_TEXT);
      moveContinueStory(MoveContinueStoryAction.MOVE_ACTION, fromIterationName, testIterationName);
      gotoIteration(fromIterationName);
      assertStoryDisposition(fromStoryName, Disposition.PLANNED_TEXT);

   }

   public void testMoveFromFutureIteration_ToFutureIterationDefaultDispositionIsPlanned() throws Exception {
      setIterationDatesFromNow(fromIteration, 4, 8);
      setIterationDatesFromNow(toIteration, 9, 13);
      moveContinueStory(MoveContinueStoryAction.MOVE_ACTION, testIterationName, fromIterationName);
      gotoIteration(testIterationName);
      assertStoryDisposition(fromStoryName, Disposition.PLANNED_TEXT);
   }

   public void testContinueStoryInNotStartedIteration() throws Exception {
      assertContinuingStoryDisposition(IterationStatus.INACTIVE, StoryDisposition.CARRIED_OVER);
   }

   public void testContinueStoryInStartedIteration() throws Exception {
      assertContinuingStoryDisposition(IterationStatus.ACTIVE, StoryDisposition.ADDED);
   }

   private void moveTaskAndCheckDisposition(String sourceDisposition, String targetDisposition) {
      gotoIteration(fromIterationName);
      tester.addUserStory(testStoryNameForMoveTest, "Test story description", "14", "1");
      tester.clickLinkWithText(testStoryNameForMoveTest);
      tester.addTask(testTaskName, null, "Test task desc.", "14", sourceDisposition);
      moveContinueTask(MoveContinueTaskAction.MOVE_ACTION,
                       toIteration.getName(),
                       fromIterationName,
                       storyName,
                       testTaskName);
      gotoIteration(toIteration.getName());
      tester.clickLinkWithText(storyName);
      tester.assertTextPresent(testTaskName);
      tester.assertTextInTable("objecttable", targetDisposition);
   }

   private void closeIterationAndContinueUnfinishedStories(String targetIterationName) {
      tester.assertKeyPresent("iteration.continue.unfinished.stories");
      tester.setWorkingForm("continue");
      tester.selectOption("targetIterationId", testProjectName + " :: " + targetIterationName);
      tester.clickButton("Ok");
   }

   private void assertNewStoryDisposition(String storyName, String expectedDisposition) {
      gotoIteration(fromIterationName);
      tester.addUserStory(storyName, "Some description", "11", "1");
      iterationTester.assertOnIterationPage();
      tester.assertCellTextForRowWithTextAndColumnKeyEquals(STORIES_TABLE_ID,
                                                            storyName,
                                                            STORY_DISPOSITION_COLUMN_KEY,
                                                            expectedDisposition);
   }

   private void setIterationDatesFromNow(Iteration iteration, int daysFromDate, int daysToDate)
         throws HibernateException, SQLException {
      Date currentDate = new Date();
      iteration.setStartDate(DateHelper.getDateDaysFromDate(currentDate, daysFromDate));
      iteration.setEndDate(DateHelper.getDateDaysFromDate(currentDate, daysToDate));
      getSession().update(iteration);
      commitCloseAndOpenSession();
   }

   private void assertContinuingStoryDisposition(IterationStatus targetIterationStatus,
                                                 StoryDisposition expectedDisposition) throws Exception {
      toIteration.setIterationStatus(targetIterationStatus);
      getSession().update(toIteration);
      commitCloseAndOpenSession();
      moveContinueStory(MoveContinueStoryAction.CONTINUE_ACTION, testIterationName, fromIterationName);
      tester.assertTextPresent(fromStoryName);
      gotoIteration(testIterationName);
      tester.assertTextPresent(fromStoryName);
      verifyDisposition(fromStoryName, expectedDisposition);
   }

   //DEBT Test attachement copy behavior

   private void setUpFromIterationWithOneStory() throws Exception {
      fromIteration = newIteration(project);
      fromIteration.setStartDate(new Date());
      fromIteration.setEndDate(DateHelper.getDateDaysFromDate(fromIteration.getStartDate(), 10));
      fromIterationName = fromIteration.getName();
      fromStory = newUserStory(fromIteration);
      fromStory.setDisposition(StoryDisposition.PLANNED);
      fromStoryName = fromStory.getName();
      newNote(fromStory, developer);
      createTestTask(fromStory);
      testFromTaskName = testTaskName;
   }

   private void setUpToIteration() throws Exception {
      toIteration = newIteration(project);
      toIteration.setStartDate(DateHelper.getDateDaysFromDate(fromIteration.getEndDate(), 1));
      toIteration.setEndDate(DateHelper.getDateDaysFromDate(toIteration.getStartDate(), 10));
      testIterationName = toIteration.getName();
      toStory = newUserStory(toIteration);
      storyName = toStory.getName();
      toStory.setDescription(testStoryDescription);
      toStory.setEstimatedHoursField(Double.parseDouble(estimatedHours));
      Task task = newTask(toStory);
      testTaskName = task.getName();
      task.setEstimatedHours(Double.parseDouble(testTaskEstimatedHours));
   }

   private void createTestTask(UserStory story) throws HibernateException, RepositoryException {
      Task task = newTask(story);
      testTaskName = task.getName();
      task.setEstimatedHours(Double.parseDouble(estimatedHours));
//       testTaskId = tester.addTask(testTaskName, developerName, testTaskDescription, testTaskEstimatedHours);
   }

   private void gotoTestProject() {
      if (!tester.isTextPresent(tester.getMessage("project.prefix") + " " + testProjectName)) {
         if (!tester.isLinkPresentWithText(testProjectName)) {
            tester.gotoProjectsPage();
         }
         tester.clickLinkWithText(testProjectName);
      }
      clickLinkWithKeyIfPresent("navigation.project");
   }

   private void gotoIteration(String iterationName) {
      if (!tester.isTextPresent(tester.getMessage("iteration.prefix") + " " + iterationName)) {
         if (!tester.isLinkPresentWithText(iterationName)) {
            gotoTestProject();
         }
         tester.clickLinkWithText(iterationName);
         iterationTester.assertOnIterationPage();
      }
      clickLinkWithKeyIfPresent("navigation.iteration"); // for history view
      clickLinkWithKeyIfPresent("iteration.link.stories"); // for all other views than Stories
   }

   private void startIteration() { iterationTester.startCurrentIteration(); }

   private void closeIteration() { iterationTester.closeCurrentIteration(); }

   private void moveContinueStory(String actionType, String targetIteration, String origIteration, String storyName) {
      tester.clickContinueLinkInRowWithText(storyName);
      tester.assertOnMoveContinueStoryPage(storyName);
      tester.selectOption("targetIterationId", testProjectName + " :: " + targetIteration);
      tester.clickButton(actionType);
      iterationTester.assertOnIterationPage();
      tester.assertTextPresent(origIteration);
   }

   private void moveStory(String fromStoryName, String fromIterationName,
                          String toIterationName) {
      moveContinueStory(MoveContinueStoryAction.MOVE_ACTION, toIterationName, fromIterationName, fromStoryName);
   }

   private void moveContinueStory(String actionType, String targetIteration, String origIteration) {
      moveContinueStory(actionType, targetIteration, origIteration, fromStoryName);
   }

   private void moveContinueTask(String actionType,
                                 String targetIteration,
                                 String origIteration,
                                 String targetStory,
                                 String taskName) {
      tester.clickContinueLinkInRowWithText(taskName);
      tester.assertOnMoveContinueTaskPage(taskName);
      tester.selectOption("targetStoryId", testProjectName + " :: " + targetIteration + " :: " + targetStory);
      tester.clickButton(actionType);
      tester.assertOnStoryPage();
      tester.assertTextPresent(origIteration);
   }

   private void verifyMovedStoryHistory() throws Exception {
      gotoIteration(testIterationName);
      tester.clickLinkWithText(fromStoryName);
      verifyHistorys(History.MOVED, "from " + fromIterationName + " to " + testIterationName);
   }

   private void verifyTargetIterationHistory(String eventType, String description) throws Exception {
      gotoIteration(testIterationName);
      verifyHistorys(eventType, description);
   }

   private void verifyHistorys(String eventType, String description) {
      tester.clickLink("aKH");
      tester.assertTitleEquals("History");
      tester.assertTextInTable(HistoryPage.EVENT_TABLE_ID, eventType);
      tester.assertCellTextForRowWithTextAndColumnKeyEquals(HistoryPage.EVENT_TABLE_ID,
                                                            description,
                                                            "history.tableheading.action",
                                                            eventType);
   }

   private void verifyDisposition(String taskOrStoryName, StoryDisposition disposition) {
      tester.assertTextPresent(taskOrStoryName);
      tester.assertTextPresent(tester.getMessage(disposition.getNameKey()));
   }

   private void verifyContinuedAttachments() {
      tester.clickLinkWithText(fromStoryName);
      tester.assertTextPresent(testTaskName);
      tester.assertTextPresent(testNoteSubject);
      tester.assertLinkPresentWithText(
            testNoteAttachmentFilename.substring(testNoteAttachmentFilename.lastIndexOf("/") + 1));
      tester.assertTextPresent(testNoteSubject + "without att.");

      tester.clickLinkWithText(testTaskName);
      tester.assertTextPresent(testTaskName);
      tester.assertTextPresent(testNoteSubject);
      tester.assertLinkPresentWithText(
            testNoteAttachmentFilename.substring(testNoteAttachmentFilename.lastIndexOf("/") + 1));
      tester.assertTextPresent(testNoteSubject + "without att.");
   }

   private void clickLinkWithKeyIfPresent(String key) {
      if (tester.isLinkPresentWithKey(key)) {
         tester.clickLinkWithKey(key);
      }
   }

}
