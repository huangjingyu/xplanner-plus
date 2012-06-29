package com.technoetic.xplanner.acceptance.web;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.UserStory;

import com.technoetic.xplanner.views.IterationAccuracyPage;
import com.technoetic.xplanner.views.IterationPage;

public class IterationAccuracyPageTestScript extends AbstractPageTestScript {

   @Override
protected void setUp() throws Exception {
      super.setUp();
   }

   public void testScenario() throws Exception {
      double storyEstimatedOriginalHours = 10.0;
      setupStory(storyEstimatedOriginalHours);

      goTo(story);
      int task1OriginalEstimate = 5;
      int task2OriginalEstimate = 7;
      String task1_1 = addTask("task 1.1", task1OriginalEstimate);
      String task1_2 = addTask("task 1.2", task2OriginalEstimate);

      assertCurrentStatusEquals(task1OriginalEstimate + task2OriginalEstimate,   // Estimated (original)
                                task1OriginalEstimate + task2OriginalEstimate,   // Estimated (current)
                                0,    // Actual
                                task1OriginalEstimate + task2OriginalEstimate,   // Remaining
                                0,    // Overestimated
                                0,    // Underestimated
                                0,    // Added
                                0     // Postponed
      );

      assertIterationStatusEquals(storyEstimatedOriginalHours, task1OriginalEstimate + task2OriginalEstimate,
                                  0.0, 0.0,
                                  0.0, 0.0,
                                  storyEstimatedOriginalHours, task1OriginalEstimate + task2OriginalEstimate,
                                  0.0, 0.0,
                                  storyEstimatedOriginalHours, task1OriginalEstimate + task2OriginalEstimate);

      start(iteration);
      gotoTask(task1_1);
      workOnTask(4);
      completeTask();
      goTo(story);
      String task1_3 = addTask("task 1.3", 2.0);

      //todo jm @jira XPR-54 finish this test
      assertCurrentStatusEquals(task1OriginalEstimate + task2OriginalEstimate,   // Estimated (original)
                                13,   // Estimated (current)
                                4,    // Actual
                                9,    // Remaining
                                1,    // Overestimated
                                2,    // Underestimated
                                0,    // Added
                                0     // Postponed
      );
   }

   public void testStoryAddedAfterIterationHasBeenStarted() throws Exception {
      setUpTestProject();
      tester.login();
      setUpTestPerson();
      setUpTestRole("editor");
      iteration = newIteration(project);
      commitCloseAndOpenSession();
      double storyEstimatedOriginalHours = 10.0;
      int task1OriginalEstimate = 5;
      int task2OriginalEstimate = 7;
      goTo(iteration);
      start(iteration);
      tester.addUserStory(storyName, "", ""+storyEstimatedOriginalHours, "1");
      tester.clickLinkWithText(storyName);
      tester.assertTextPresent("Added");
      String task1_1 = addTask("task 1.1", task1OriginalEstimate);
      String task1_2 = addTask("task 1.2", task2OriginalEstimate);
      assertCurrentStatusEquals(0,   // Estimated (original)
                                task1OriginalEstimate + task2OriginalEstimate,   // Estimated (current)
                                0,    // Actual
                                task1OriginalEstimate + task2OriginalEstimate,    // Remaining
                                0,    // Overestimated
                                task1OriginalEstimate + task2OriginalEstimate,    // Underestimated
                                task1OriginalEstimate + task2OriginalEstimate,    // Added
                                0     // Postponed
      );

   }

   public void testPercentageDifference_ShouldBeBlankIfZeroOrginalEstimate() throws Exception {
      setupStory(0);
      start(iteration);
      goTo(story);
      String task1_1 = addTask("task 1.1", 0);
      gotoTask(task1_1);
      workOnTask(4);
      goToAccuracyPage();
      tester.assertCellTextForRowWithTextAndColumnKeyEquals("objecttable",
                                                            story.getName(),
                                                            "stories.tableheading.percent_diff",
                                                            "");
   }

   private void assertCurrentStatusEquals(double originalEstimated,
                                          double currentEstimated,
                                          double actual,
                                          double remaining,
                                          double overestimated,
                                          double underestimated,
                                          double added,
                                          double postponed) {
      goToAccuracyPage();
      tester.assertTableEquals(IterationAccuracyPage.CURRENT_STATUS_TABLE_ID, new String[][]{
            new String[]{"", tester.getMessage("iteration.label.task")},
            new String[]{tester.getMessage("iteration.label.estimated_original_hours"), "" + originalEstimated},
            new String[]{tester.getMessage("iteration.label.estimated_hours"), "" + currentEstimated},
            new String[]{tester.getMessage("iteration.label.completed"), "" + actual},
            new String[]{tester.getMessage("iteration.label.remaining"), "" + remaining},
            new String[]{tester.getMessage("iteration.label.overestimated"), "" + overestimated},
            new String[]{tester.getMessage("iteration.label.underestimated"), "" + underestimated},
            new String[]{tester.getMessage("iteration.label.added"), "" + added},
            new String[]{tester.getMessage("iteration.label.postponed"), "" + postponed}
      });
   }

   private void assertIterationStatusEquals(double storyOriginal, double taskOriginal,
                                            double storyAdded, double taskAdded,
                                            double storyPostponed, double taskPostponed,
                                            double storyTotal, double taskTotal,
                                            double storyCompleted, double taskCompleted,
                                            double storyRemaining, double taskRemaining) {
      goToAccuracyPage();
      tester.assertTableEquals(IterationAccuracyPage.ITERATION_STATUS_TABLE_ID, new String[][]{
            new String[]{"", tester.getMessage("iteration.label.story"), tester.getMessage("iteration.label.task")},
            new String[]{tester.getMessage("iteration.label.original"), "" + storyOriginal, "" + taskOriginal},
            new String[]{tester.getMessage("iteration.label.added"), "" + storyAdded, "" + taskAdded},
            new String[]{tester.getMessage("iteration.label.postponed"), "" + storyPostponed, "" + taskPostponed},
            new String[]{tester.getMessage("iteration.label.total"), "" + storyTotal, "" + taskTotal},
            new String[]{tester.getMessage("iteration.label.completed"), "" + storyCompleted, "" + taskCompleted},
            new String[]{tester.getMessage("iteration.label.remaining"), "" + storyRemaining, "" + taskRemaining},
      });
   }

   private void completeTask() {tester.completeCurrentTask();}

   private void workOnTask(int durationInHours) {
      editTimeEntry();
      enterTime(durationInHours);
   }


   private void setupStory(double estimatedHours) throws Exception {
      setUpTestProject();
      setUpTestIterationAndStory_();
      story.setEstimatedHoursField(estimatedHours);
      commitCloseAndOpenSession();
      tester.login();
      setUpTestPerson();
      setUpTestRole("editor");
   }

   private void enterTime(int durationInHours) {tester.setTimeEntry(0, durationInHours, developer.getName());}

   private void editTimeEntry() {tester.clickLinkWithKey("action.edittime.task");}

   private String addTask(String name, double estimate) {return tester.addTask(name, developerName, "", "" + estimate);}

   private void start(Iteration iteration) { iterationTester.start(iteration); }
   private void goTo(Iteration iteration) {iterationTester.goToDefaultView(iteration);}
   private void goToAccuracyPage() {
      iterationTester.goToView(project, iteration, IterationPage.ACCURACY_VIEW);
   }
   private void goTo(UserStory story) {tester.gotoPage("view", "userstory", story.getId());}
   private void gotoTask(String taskId) {tester.gotoPage("view", "task", Integer.parseInt(taskId));}
}
