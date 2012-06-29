package com.technoetic.xplanner.acceptance.web;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import junit.framework.Assert;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;

import com.technoetic.xplanner.DomainSpecificPropertiesFactory;
import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.acceptance.web.MailTester.Email;
import com.technoetic.xplanner.db.hibernate.GlobalSessionFactory;
import com.technoetic.xplanner.domain.TaskDisposition;
import com.technoetic.xplanner.mail.EmailNotificationSupport;
import com.technoetic.xplanner.mail.MissingTimeEntryNotifier;


public class TaskPageTestScript extends AbstractPageTestScript {
   public int lastTimeSlotNbr;
   private static final String TIME_ENTRY_TABLE_ID = "time_entries";
   private MailTester mailTester = new MailTester(tester);
   private String newTestTaskEstimatedHours = "15.0";
   public XPlannerProperties xplannerProperties;

   public TaskPageTestScript(String test) {
      super(test);
   }

   public void setUp() throws Exception {
//      new Timer().run(new Callable() {
//         public void run() throws Exception { mySetUp(); }
//      });
//   }
//
//   private void mySetUp() throws Exception {
      super.setUp();
//       new Timer().run("simpleSetUp_", new Callable() { public void run() throws Exception {
      simpleSetUp_();
//       } });
      mailTester.setUp();
//       new Timer().run("rest of mySetUp", new Callable() { public void run() throws Exception {
      tester.clickLinkWithText(testIterationName);
      iterationTester.startCurrentIteration();
      tester.clickLinkWithText(storyName);
      tester.addTask(testTaskName, developer.getName(), testTaskDescription, testTaskEstimatedHours);
      tester.clickLinkWithText(testTaskName);
      lastTimeSlotNbr = 0;
//       } });
      xplannerProperties = new XPlannerProperties();
   }

   public void tearDown() throws Exception {
//      new Timer().run(new Callable() {
//         public void run() throws Exception { myTearDown(); }
//      });
//   }
//
//   private void myTearDown() throws Exception {
//       if (testTaskId != null) {
//          deleteLocalTimeEntry(testTaskId);
//       }

      mailTester.tearDown();
      simpleTearDown_();
      super.tearDown();
   }

   public void testContentAndLinks() {
      tester.assertOnTaskPage();
      tester.assertTextPresent(testTaskName);
      tester.assertTextPresent(testTaskEstimatedHours);
      tester.assertTextPresent(developer.getName());
      tester.assertTextPresent(testTaskDescription);
      tester.clickLinkWithKey("navigation.story");
      tester.assertOnStoryPage();
      tester.clickLinkWithText(testTaskName);
      tester.assertOnTaskPage();
      tester.clickLinkWithKey("navigation.iteration");
      iterationTester.assertOnIterationPage();
      tester.clickLinkWithText(storyName);
      tester.clickLinkWithText(testTaskName);
      tester.assertOnTaskPage();
      tester.clickLinkWithKey("navigation.project");
      tester.assertOnProjectPage();
      tester.clickLinkWithText(testIterationName);
      tester.clickLinkWithText(storyName);
      tester.clickLinkWithText(testTaskName);
      //Test javascript on edit time page
      tester.clickLinkWithKey("action.edittime.task");
      int hoursToLog = 1;
      double remainingHours = Double.parseDouble(testTaskEstimatedHours);
      tester.assertFormElementEquals("remainingHours", testTaskEstimatedHours);
      tester.setFormElement("duration[0]", "" + hoursToLog);
      remainingHours -= hoursToLog;
      hoursToLog = (int) remainingHours + 2;
      String startTime = tester.dateTimeStringForNHoursAway(0);
      String endTime = tester.dateTimeStringForNHoursAway(hoursToLog);
      tester.setFormElement("startTime[0]", startTime);
      tester.setFormElement("endTime[0]", endTime);
      tester.assertFormElementEquals("remainingHours", "0");
      tester.setFormElement("endTime[0]", "");
      tester.setFormElement("duration[0]", "");
      tester.setFormElement("description[0]", "");
      // Test adding partial time entry
      tester.submit();
      tester.assertOnTaskPage();
      tester.assertTextInTable("time_entries", startTime);
      Assert.fail();
//      WebTable table = tester.getDialog().getWebTableBySummaryOrId("time_entries");
//      assertTrue("endTime not empty", StringUtils.isBlank(table.getCellAsText(1, 1)));
//      assertTrue("duration not empty", StringUtils.isBlank(table.getCellAsText(1, 3)));
//      assertTrue("description not empty", StringUtils.isBlank(table.getCellAsText(1, 5)));
      tester.clickLinkWithKey("action.edittime.task");
      hoursToLog = 2;
      double expectedRemainingHours = Double.parseDouble(testTaskEstimatedHours) - hoursToLog;
      endTime = tester.dateTimeStringForNHoursAway(hoursToLog);
      tester.setFormElement("endTime[0]", endTime);
      tester.submit();
      tester.assertTextInTable(TIME_ENTRY_TABLE_ID, "" + hoursToLog);
      tester.clickLinkWithText(storyName);
      tester.assertCellNumberForRowWithTextAndColumnKeyEquals(XPlannerWebTester.MAIN_TABLE_ID,
                                                              testTaskName,
                                                              "tasks.tableheading.estimated_hours",
                                                              new Double(testTaskEstimatedHours));
      if (!"image".equals(xplannerProperties.getProperty("xplanner.progressbar.impl"))) {
         tester.assertCellNumberForRowWithTextAndColumnKeyEquals(XPlannerWebTester.MAIN_TABLE_ID,
                                                                 testTaskName,
                                                                 "tasks.tableheading.actual",
                                                                 new Double(hoursToLog));
      }
      tester.assertCellNumberForRowWithTextAndColumnKeyEquals(XPlannerWebTester.MAIN_TABLE_ID,
                                                              testTaskName,
                                                              "tasks.tableheading.remaining",
                                                              new Double(expectedRemainingHours));
      tester.clickLinkWithText(testTaskName);
   }

   public void testAddingTimeLog_NoReestimation() {
      double hoursToLog = 3.0;
      addTimeEntry("" + hoursToLog, "");
      double expectedRemainingHours = Double.parseDouble(testTaskEstimatedHours) - hoursToLog;
      assertTimeEntry("" + hoursToLog);
      assertTaskTiming(Double.parseDouble(testTaskEstimatedHours),
                       INITIAL_TASK_ESTIMATED_HOURS,
                       expectedRemainingHours,
                       hoursToLog);
      tester.clickLinkWithKey("action.edittime.task");
   }

   public void testDeleteTimeLog() {
      addTimeEntry("1.0", "");
      tester.clickLinkWithKey("action.edittime.task");
      tester.checkCheckbox("deleted[0]", "true");
      tester.submit();
      tester.assertTableNotPresent("objecttable");

   }

   public void testAddingTimeLog_LessRemainingHours() {
      tester.assertOnTaskPage();
      double hoursToLog = 1.0;
      double hoursToReestimate = 2.0;
      double remainingHours = Double.parseDouble(testTaskEstimatedHours) - (hoursToLog + hoursToReestimate);
      double expectedEstimatedHours = Double.parseDouble(testTaskEstimatedHours) - hoursToReestimate;
      double expectedActualHours = hoursToLog;
      addTimeEntry("" + expectedActualHours, "" + remainingHours);
      assertTaskTiming(expectedEstimatedHours, INITIAL_TASK_ESTIMATED_HOURS, remainingHours, expectedActualHours);
      assertTimeEntry("" + expectedActualHours);
   }

   public void testAddingTimeLog_MoreRemainingHours() {
      tester.assertOnTaskPage();
      double hoursToLog = 1.0;
      double hoursToReestimate = 2.0;
      double remainingHours = (Double.parseDouble(testTaskEstimatedHours) + hoursToReestimate) - hoursToLog;
      double expectedEstimatedHours = Double.parseDouble(testTaskEstimatedHours) + hoursToReestimate;
      double expectedActualHours = hoursToLog;
      addTimeEntry("" + hoursToLog, "" + remainingHours);
      assertTaskTiming(expectedEstimatedHours, INITIAL_TASK_ESTIMATED_HOURS, remainingHours, expectedActualHours);
      assertTimeEntry("" + expectedActualHours);
   }


   public void testDefaultDisposition() {
      tester.assertOnTaskPage();
      String hoursToLog = "3";
      addTimeEntryInHours(hoursToLog);
      assertTestTaskColumnEquals("tasks.tableheading.disposition",
                                 tester.getMessage(TaskDisposition.DISCOVERED.getNameKey()));
   }

   public void testDescription() {
      addTimeEntry("1.0", "", null, "The description");
      tester.assertOnTaskPage();
      Assert.fail();
//      WebTable table = tester.getDialog().getWebTableBySummaryOrId("time_entries");
//      assertEquals("Wrong description", "The description", table.getCellAsText(1, 5));
      tester.clickLinkWithKey("action.edittime.task");
      tester.assertFormElementEquals("description[0]", "The description");
   }

   public void testSendMissingTimeEntryNotificationToProjectSpecificLeads() throws Exception {
      turnOffAcceptorMissingTimeEntryReminderEmail();
      Person editor = createLeadPerson("Editor");
      Person admin = createLeadPerson("Admin");
      ResourceBundle resource = ResourceBundle.getBundle("ResourceBundle");
      String expectedFrom = xplannerProperties.getProperty(XPlannerProperties.EMAIL_FROM);
      String expectedSubject = resource.getString(MissingTimeEntryNotifier.SUBJECT_FOR_PROJECT_LEADS);
      List expectedBodyElements = getExpectedBodyElementsOfLeadsEmailNotification(resource);
      Email expectedEmail = new Email(expectedFrom, expectedSubject, expectedBodyElements);
      gotoTestTaskPage();

      //no email is task hasn't started yet
      waitOneDay();
      mailTester.assertNumberOfEmailReceivedBy(0, editor.getEmail());
      mailTester.assertNumberOfEmailReceivedBy(0, admin.getEmail());

      //no email if task has a time entry the day before
      addTodayTimeEntryToTask();
      waitOneDay();
      mailTester.assertNumberOfEmailReceivedBy(0, editor.getEmail());
      mailTester.assertNumberOfEmailReceivedBy(0, admin.getEmail());

      //no email to project leads if none is registered even though time entry is missing
      waitOneDay();
      mailTester.assertNumberOfEmailReceivedBy(0, editor.getEmail());
      mailTester.assertNumberOfEmailReceivedBy(0, admin.getEmail());

      addPersonToRecipientsOfMissingTimeEntryReport(editor.getName());
      addPersonToRecipientsOfMissingTimeEntryReport(admin.getName());

      //email sent to all recipients
      waitOneDay();
      mailTester.assertEmailHasBeenReceived(editor.getEmail(), expectedEmail);
      mailTester.assertEmailHasBeenReceived(admin.getEmail(), expectedEmail);

      mailTester.resetSmtp();

      //email sent to only the one left
      removePersonToRecipientsOfMissingTimeEntryReport(admin.getName());
      waitOneDay();
      mailTester.assertEmailHasBeenReceived(editor.getEmail(), expectedEmail);
      mailTester.assertNumberOfEmailReceivedBy(1, editor.getEmail());
      mailTester.assertNumberOfEmailReceivedBy(0, admin.getEmail());

      mailTester.resetSmtp();

      //No email sent since there is a time entry the previous day
      addTodayTimeEntryToTask();
      waitOneDay();
      mailTester.assertNumberOfEmailReceivedBy(0, editor.getEmail());
      mailTester.assertNumberOfEmailReceivedBy(0, admin.getEmail());

//      tester.deleteObjects(Person.class, "name", editorLeadName);
//      tester.deleteObjects(Person.class, "name", adminLeadName);
   }

   private void addTodayTimeEntryToTask() throws Exception {
      gotoTestTaskPage();
      final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      final String reportingDate = dateFormat.format(tester.getCurrentDate());
      addTimeEntry("3", "", reportingDate, null);
   }


   public void testMissingTimeEntryNotificationReminder() throws Exception {
      //DEBT(DYNAMICFIELDS) Should have a way to force this project notification option
      assertSendEmailNotificationFromProject(project);
      ResourceBundle resource = ResourceBundle.getBundle("ResourceBundle");
      String subject = resource.getString(MissingTimeEntryNotifier.SUBJECT_FOR_ACCEPTORS);
      String emailFrom = xplannerProperties.getProperty(XPlannerProperties.EMAIL_FROM);
      List emailBodyElements = new ArrayList();
      emailBodyElements.add(resource.getString(MissingTimeEntryNotifier.EMAIL_BODY_HEADER_FOR_ACCEPTORS));
      emailBodyElements.add(storyName);
      emailBodyElements.add(testTaskName);
      emailBodyElements.add(resource.getString(MissingTimeEntryNotifier.EMAIL_BODY_FOOTER));
      addTodayTimeEntryToTask();
      waitNDays(2);
      String[] recipients = new String[]{"ap@nowhere.com"};
      Email expectedEmail = new Email(emailFrom, recipients, subject, emailBodyElements);
      mailTester.assertEmailHasBeenReceived(expectedEmail);
      mailTester.resetSmtp();
      addTodayTimeEntryToTask();
      waitOneDay();
      expectedEmail.recipients = new String[0];
      mailTester.assertEmailHasNotBeenReceived(expectedEmail);
   }

   private void assertSendEmailNotificationFromProject(Project project) {
      DomainSpecificPropertiesFactory propertiesFactory =
            new DomainSpecificPropertiesFactory(GlobalSessionFactory.get(), new XPlannerProperties().get());
      EmailNotificationSupport emailNotificationSupport = new EmailNotificationSupport(null, null, propertiesFactory);
      assertTrue("Set " + XPlannerProperties.SEND_NOTIFICATION_KEY + " to 'true' to make this test pass",
                 emailNotificationSupport.isProjectToBeNotified(project));
   }

   private void waitNDays(int days) throws UnsupportedEncodingException {
      mailTester.moveCurrentDayAndSendEmail(days);
   }

   private void waitOneDay() throws Exception {
      waitNDays(1);
   }

   public void testAddingAndDeletingNotes() {
      runNotesTests(XPlannerWebTester.TASK_PAGE);
   }

   public void testPdfExport() throws Exception {
      checkExportUri("task", "pdf");
   }

   public void testXmlExport() throws Exception {
      tester.clickLinkWithText(testIterationName);
      checkExportUri("iteration", "xml");
   }

   public void testReportExport() throws Exception {
      checkExportUri("task", "jrpdf");
   }

   public void testOrgEstHoursInNotStartedIteration() throws Exception {
      setUpProjectForOrgEstHoursTest();
      tester.clickLinkWithKey("action.edit.task");
      tester.setFormElement("estimatedHours", newTestTaskEstimatedHours);
      tester.submit();
      tester.assertTextPresent(newTestTaskEstimatedHours);
      tester.assertTextPresent("(" + newTestTaskEstimatedHours + ")");
   }

   public void testOrgEstHoursInStartedIteration() throws Exception {
      setUpProjectForOrgEstHoursTest();
      tester.gotoProjectsPage();
      tester.clickLinkWithText(testIterationName);
      iterationTester.startCurrentIteration();
      tester.clickLinkWithText(storyName);
      tester.clickLinkWithText(testTaskName);
      tester.clickLinkWithKey("action.edit.task");
      tester.setFormElement("estimatedHours", newTestTaskEstimatedHours);
      tester.submit();
      tester.assertTextPresent(newTestTaskEstimatedHours);
      tester.assertTextPresent("(" + testTaskEstimatedHours + ")");
   }

   private void setUpProjectForOrgEstHoursTest() throws Exception {
      setUpTestProject();
      setUpTestIterationAndStory_();
      setUpTestPerson();
      setUpTestRole("editor");
      testIterationName = iteration.getName();
      storyName = story.getName();
      commitCloseAndOpenSession();
      goToTestStoryPage_();
      tester.addTask(testTaskName, developerName, testTaskDescription, testTaskEstimatedHours);
      tester.clickLinkWithText(testTaskName);
   }

   protected void traverseLinkWithKeyAndReturn(String key) throws Exception {
      tester.clickLinkWithKey(key);
      tester.gotoPage("view", "projects", 0);
   }

   private List getExpectedBodyElementsOfLeadsEmailNotification(ResourceBundle resource) {
      List emailBodyElements = new ArrayList();
      emailBodyElements.add(resource.getString(MissingTimeEntryNotifier.EMAIL_BODY_HEADER_FOR_PROJECT_LEADERS));
      emailBodyElements.add(developerName);
      emailBodyElements.add(storyName);
      emailBodyElements.add(testTaskName);
      emailBodyElements.add(resource.getString(MissingTimeEntryNotifier.EMAIL_BODY_FOOTER));
      return emailBodyElements;
   }

   private Person createLeadPerson(String role) throws Exception {
      Person person = mom.newPerson("lead");
      person.setInitials(developerInitials);
      person.setPhone(userPhone);
      setUpPersonRole(project, person, role);
      commitCloseAndOpenSession();
      return person;
   }

   private void turnOffAcceptorMissingTimeEntryReminderEmail() {
      gotoTestProjectEditPage();
      tester.uncheckCheckbox("sendemail");
      tester.submit();
   }

   private void gotoTestProjectEditPage() {
      tester.gotoProjectsPage();
      tester.clickLinkWithText(testProjectName);
      tester.clickLinkWithKey("action.edit.project");
   }

   private void addPersonToRecipientsOfMissingTimeEntryReport(String personName) throws Exception {
      gotoTestProjectEditPage();
      tester.selectOption("personToAddId", personName);
      tester.clickButton("add");
      tester.assertTextInTable("objecttable", personName);
      gotoTestTaskPage();
   }

   private void removePersonToRecipientsOfMissingTimeEntryReport(String personName) throws Exception {
      gotoTestProjectEditPage();
      tester.clickDeleteLinkForRowWithText(personName);
      tester.assertTextNotInTable("objecttable", personName);
      tester.submit();
   }

   private void assertTaskTiming(double expectedEstimatedHours,
                                 double expectedOriginalEstimatedHours, double expectedRemainingHours,
                                 double expectedActualHours) {
      tester.clickLinkWithText(storyName);
      tester.assertCellNumberForRowWithTextAndColumnKeyEquals(XPlannerWebTester.MAIN_TABLE_ID,
                                                              testTaskName,
                                                              "tasks.tableheading.remaining",
                                                              new Double(expectedRemainingHours));
      if (!"image".equals(xplannerProperties.getProperty("xplanner.progressbar.impl"))) {
         tester.assertCellNumberForRowWithTextAndColumnKeyEquals(XPlannerWebTester.MAIN_TABLE_ID,
                                                                 testTaskName,
                                                                 "tasks.tableheading.actual",
                                                                 new Double(expectedActualHours));
      }
      tester.assertCellNumberForRowWithTextAndColumnKeyEquals(XPlannerWebTester.MAIN_TABLE_ID,
                                                              testTaskName,
                                                              "tasks.tableheading.estimated_hours",
                                                              new Double(expectedEstimatedHours));
      tester.clickLinkWithText(testTaskName);
      tester.assertTextPresent("(" + expectedOriginalEstimatedHours + ")");

   }


   private void addTimeEntryInHours(String val) {
      this.addTimeEntry(val, null);
   }

   private void addTimeEntry(String val, String remainingHours) {
      this.addTimeEntry(val, remainingHours, null, null);
   }

   private void addTimeEntry(String val, String remainingHours, String reportDate, String description) {
      tester.assertOnTaskPage();
      tester.clickLinkWithKey("action.edittime.task");
      tester.setFormElement("duration[" + lastTimeSlotNbr + "]", val);
      if (reportDate != null) tester.setFormElement("reportDate[" + lastTimeSlotNbr + "]", reportDate);
      if (remainingHours != null) tester.setFormElement("remainingHours", "" + remainingHours);
      if (description != null) tester.setFormElement("description[" + lastTimeSlotNbr + "]", description);

      tester.selectOption("person1Id[" + lastTimeSlotNbr + "]", developer.getName());
      tester.submit();
      lastTimeSlotNbr++;
   }

   private void assertTimeEntry(String value) {
      tester.assertOnTaskPage();
      tester.assertTextInTable(TIME_ENTRY_TABLE_ID, value);
   }

   private void assertTestTaskColumnEquals(String columnKey, String expectedText) {
      tester.clickLinkWithText(storyName);
      tester.assertCellTextForRowWithTextAndColumnKeyEquals(XPlannerWebTester.MAIN_TABLE_ID,
                                                            testTaskName,
                                                            columnKey,
                                                            expectedText);
      tester.assertTextInTable(XPlannerWebTester.MAIN_TABLE_ID, expectedText);
      tester.clickLinkWithText(testTaskName);
   }

   private void gotoTestTaskPage() throws Exception {
      tester.gotoProjectsPage();
      tester.clickLinkWithText(testProjectName);
      tester.clickLinkWithText(testIterationName);
      tester.clickLinkWithText(storyName);
      tester.clickLinkWithText(testTaskName);

   }
}
