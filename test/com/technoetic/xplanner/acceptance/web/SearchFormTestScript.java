package com.technoetic.xplanner.acceptance.web;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

public class SearchFormTestScript extends AbstractPageTestScript {
   private String projectToSearch = testProjectName + "-findMe";
   private String iterationToFind = testIterationName + "yoohoo";
   private String storyToFind = storyName + "peekaboo";
   private String taskToFind = testTaskName + "heythere";
   private String projectDescriptionToFind = testProjectDescription + "proojaaactooor";
   private String iterationDescriptionToFind = testIterationDescription + "widget";
   private String storyDescriptionToFind = testStoryDescription + "gadget";
   private String taskDescriptionToFind = testTaskDescription + "thingamajig";
   private String forbiddenProjectName = projectToSearch + "-duplicated";

   public static final String SUBJECT_SUFFIX = " Note Subject";
   public static final String PROJECT_NOTE_SUBJECT = "Project" + SUBJECT_SUFFIX;
   public static final String ITERATION_NOTE_SUBJECT = "Iteration" + SUBJECT_SUFFIX;
   public static final String STORY_NOTE_SUBJECT = "Story" + SUBJECT_SUFFIX;
   public static final String TASK_NOTE_SUBJECT = "Task" + SUBJECT_SUFFIX;

   public static final String NOTE_BODY_SUFFIX = " Note Body";
   public static final String PROJECT_NOTE_BODY = "Project" + NOTE_BODY_SUFFIX;
   public static final String ITERATION_NOTE_BODY = "Iteration" + NOTE_BODY_SUFFIX;
   public static final String STORY_NOTE_BODY = "Story" + NOTE_BODY_SUFFIX;
   public static final String TASK_NOTE_BODY = "Task" + NOTE_BODY_SUFFIX;

   public static final String GLOBAL_SCOPE_ENABLE_PROPERTY = "search.content.globalScopeEnable";
   private String taskId;
   private String storyId;
   private String iterationId;

   public void setUp() throws Exception {
//      new Timer().run(new Callable() { public void run() throws Exception { mySetUp(); } });
//   }
//
//   private void mySetUp() throws Exception {
      super.setUp();
      String startDate = tester.dateStringForNDaysAway(0);
      String endDate = tester.dateStringForNDaysAway(14);
      tester.login();
      setUpTestPerson();
      setUpTestProject(projectToSearch, projectDescriptionToFind);
      //projectId = tester.addProject(projectToSearch, projectDescriptionToFind);
      setUpTestRole("editor");
      tester.gotoProjectsPage();
      tester.gotoProjectsPage();
      tester.clickLinkWithText(projectToSearch);
      tester.addNote(PROJECT_NOTE_SUBJECT, PROJECT_NOTE_BODY, "SearchFormTestScript");
      iterationId = iterationTester.addIteration(iterationToFind, startDate, endDate, iterationDescriptionToFind);
      tester.clickLinkWithText(iterationToFind);
      tester.addNote(ITERATION_NOTE_SUBJECT, ITERATION_NOTE_BODY, "SearchFormTestScript");

      storyId = tester.addUserStory(storyToFind, storyDescriptionToFind, "8.0", "1");
      tester.clickLinkWithText(storyToFind);
      tester.addNote(STORY_NOTE_SUBJECT, STORY_NOTE_BODY, "SearchFormTestScript");
      taskId = tester.addTask(taskToFind, developerName, taskDescriptionToFind, testTaskEstimatedHours);
      tester.clickLinkWithText(taskToFind);
      tester.addNote(TASK_NOTE_SUBJECT, TASK_NOTE_BODY, "SearchFormTestScript");
      tester.gotoProjectsPage();
      tester.editProperty(GLOBAL_SCOPE_ENABLE_PROPERTY, "true");
   }

   public void tearDown() throws Exception {
//         new Timer().run(new Callable() { public void run() throws Exception { myTearDown(); } });
//      }
//
//      private void myTearDown() throws Exception {
      tester.editProperty(GLOBAL_SCOPE_ENABLE_PROPERTY, "true");
      tester.clickLinkWithText("logout");
      super.deleteLocalNote(taskId);
      tester.deleteObjects(Task.class, "name", taskToFind);
      super.deleteLocalNote(storyId);
      tester.deleteObjects(UserStory.class, "name", storyToFind);
      super.deleteLocalNote(iterationId);
      tester.deleteObjects(Iteration.class, "name", iterationToFind);
      super.deleteLocalNote(Integer.toString(project.getId()));
      tester.deleteObjects(Project.class, "name", projectToSearch);
      tester.deleteObjects(Project.class, "name", forbiddenProjectName);
      super.tearDown();
      tearDownTestPerson();
   }

   public void testSearchForm() throws Exception {
      searchForTextInTitle();
      searchForTextInDescription();
   }

   public void testSearchWithoutPermissions() throws Exception {
      createUserWithoutPermission();

      tester.login(noPermissionUserName, XPlannerWebTester.DEFAULT_PASSWORD);
      searchFor("proojaaactooor");
      tester.assertKeyPresent("empty.search.results");
   }

   public void testSearchWithEditorPermissions() throws Exception {
      createUserWithEditorRoleForProject(projectToSearch);

      tester.login(editorRoleUserName, XPlannerWebTester.DEFAULT_PASSWORD);
      searchFor("proojaaactooor");
      searchForTextInTitle();
      searchForTextInDescription();
   }

   public void testSearchWithEditorPermissions_OneProjectShouldNotBeFound() throws Exception {
      tester.gotoProjectsPage();
      tester.addProject(forbiddenProjectName, projectDescriptionToFind);
      createUserWithEditorRoleForProject(projectToSearch);

      tester.login(editorRoleUserName, XPlannerWebTester.DEFAULT_PASSWORD);
      searchFor("proojaaactooor");
      searchForTextInTitle();
      searchForTextInDescription();
   }

   public void testSearchWithEditorPermissions_RestrictedScope() throws Exception {
      tester.editProperty(GLOBAL_SCOPE_ENABLE_PROPERTY, "false");
      tester.gotoProjectsPage();
      tester.addProject(forbiddenProjectName, projectDescriptionToFind);
      personTester.gotoPeoplePage();
      personTester.addPersonWithRole(editorRoleUserName,
                               editorRoleUserName,
                               developerInitials,
                               userEmail,
                               userPhone,
                               projectToSearch, tester.getMessage("person.editor.role.editor"));
      personTester.assignRoleToPersonOnProject(editorRoleUserName,
                                         forbiddenProjectName,
                                         tester.getMessage("person.editor.role.editor"));
      tester.logout();
      tester.login(editorRoleUserName, XPlannerWebTester.DEFAULT_PASSWORD);
      assertNoContentSearchForm();
      tester.gotoProjectsPage();
      tester.assertTextPresent(forbiddenProjectName);
      tester.clickLinkWithText(projectToSearch);
      searchForTextInTitle();
   }

   private void assertNoContentSearchForm() {
      tester.assertFormNotPresent("search");
   }

   private void searchForTextInDescription() {
      searchFor("proojaaactooor");
      tester.assertLinkPresentWithText(projectToSearch);
      tester.clickLinkWithText(projectToSearch);
      tester.assertOnProjectPage();

      searchFor("widget");
      tester.assertLinkPresentWithText(iterationToFind);
      tester.clickLinkWithText(iterationToFind);
      iterationTester.assertOnIterationPage();

      searchFor("gadget");
      tester.assertLinkPresentWithText(storyToFind);
      tester.clickLinkWithText(storyToFind);
      tester.assertOnStoryPage(storyToFind);

      searchFor("thingamajig");
      tester.assertLinkPresentWithText(taskToFind);
      tester.clickLinkWithText(taskToFind);
      tester.assertOnTaskPage();
   }

   private void searchForTextInTitle() {
      searchFor("findMe");
      tester.assertLinkPresentWithText(projectToSearch);
      tester.assertLinkNotPresentWithText(forbiddenProjectName);
      tester.clickLinkWithText(projectToSearch);
      tester.assertOnProjectPage();

      searchFor("yoohoo");
      tester.assertLinkPresentWithText(iterationToFind);
      tester.clickLinkWithText(iterationToFind);
      iterationTester.assertOnIterationPage();

      searchFor("peekaboo");
      tester.assertLinkPresentWithText(storyToFind);
      tester.clickLinkWithText(storyToFind);
      tester.assertOnStoryPage(storyToFind);

      searchFor("heythere");
      tester.assertLinkPresentWithText(taskToFind);
      tester.clickLinkWithText(taskToFind);
      tester.assertOnTaskPage();
   }

   private void searchFor(String textToFind) {
      tester.assertFormPresent("search");
      tester.setWorkingForm("search");
      tester.setFormElement("searchedContent", textToFind);
      tester.submit();
      tester.assertKeyPresent("search.results");
   }

   public void testNoteLinks() throws Exception {
      searchForNote("Project");
      searchForNote("Iteration");
      searchForNote("Story");
      searchForNote("Task");
   }

   public void testSearchOrphanNote() throws Exception {
      tester.deleteObjects(UserStory.class, "name", storyToFind);
      searchFor("Story" + SUBJECT_SUFFIX);
      tester.assertLinkNotPresentWithText("Story" + SUBJECT_SUFFIX);
   }

   private void searchForNote(String prefix) {
      searchFor(prefix + SUBJECT_SUFFIX);
      tester.assertLinkPresentWithText(prefix + SUBJECT_SUFFIX);
      tester.clickLinkWithText(prefix + SUBJECT_SUFFIX);
      tester.assertTextPresent(prefix + SUBJECT_SUFFIX);
      tester.assertTextPresent(prefix + NOTE_BODY_SUFFIX);
   }
}
