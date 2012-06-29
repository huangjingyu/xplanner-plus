package com.technoetic.xplanner.acceptance.web;

import java.sql.SQLException;

import net.sf.xplanner.domain.Task;

import org.hibernate.HibernateException;

import com.technoetic.xplanner.domain.repository.RepositoryException;
import com.technoetic.xplanner.testing.DateHelper;

public class PersonTasksTestScript extends AbstractPageTestScript {
   private Task testTask;

   public PersonTasksTestScript() {
   }

   protected void setUp() throws Exception {
      super.setUp();
      setUpTestPerson();
      setUpTestProject();
      setUpTestIterationAndStory_();
      setUpPersonRole(project, developer, "editor");
      testTask = newTask(story);
      testTask.setAcceptorId(developer.getId());
      testTask.setName(testTaskName);
      commitSession();
      tester.login();
   }
   
   /**
    * Checks for the table displaying any tasks in current iterations,
    * where this person is the acceptor, and the task hasn't been started yet 
    * i.e. current and pending.
    */
   public void testCurrentPendingTasksForPerson() throws Exception {
      tester.gotoProjectsPage();
      tester.clickLinkWithKey("projects.link.people");
      tester.clickLinkWithText(developerName);
      tester.assertTextInTable("currentPendingTasksForPerson", testTaskName);
   }

   /**
    * Checks for the table displaying any tasks in future iterations
    * where this person is the acceptor.
    */
   public void testFutureTasksForPerson() throws Exception {
      moveIterationForward();
      tester.gotoProjectsPage();
      tester.clickLinkWithKey("projects.link.people");
      tester.clickLinkWithText(developerName);
      tester.assertTextInTable("futureTasksForPerson", testTaskName);
   }

   /**
    * Checks for the table displaying any tasks in current iterations,
    * where this person is the acceptor, and the task has been started 
    * i.e. current and active.
    */
   public void testCurrentActiveTasksForPerson() throws Exception {
      setUpTimeEntry();
      tester.clickLinkWithKey("projects.link.people");
      tester.clickLinkWithText(developerName);
      tester.assertTextInTable("currentActiveTasksForPerson", testTaskName);
      tester.assertLinkPresentWithImage(EDIT_IMAGE);
      tester.assertLinkPresentWithImage(EDIT_TIME_IMAGE);
      tester.logout();
      tester.login(developerUserId, "test");
      tester.clickLinkWithKey("projects.link.people");
      tester.clickLinkWithText(developerName);
      tester.assertTextInTable("currentActiveTasksForPerson", testTaskName);
      tester.assertLinkPresentWithImage(EDIT_IMAGE);
      tester.assertLinkPresentWithImage(EDIT_TIME_IMAGE);
   }

   /**
    * Checks for the table displaying any tasks in current iterations,
    * where this person is the acceptor, and the task has been completed. 
    */
   public void testCurrentCompletedTasksForPerson() throws Exception {
      // mark the test task as completed
      testTask.setCompleted(true);
      commitCloseAndOpenSession();

      tester.gotoProjectsPage();
      tester.clickLinkWithKey("projects.link.people");
      tester.clickLinkWithText(developerName);
      tester.assertTextInTable("currentCompletedTasksForPerson", testTaskName);
   }

   /**
    * Checks for the table displaying any stories in current iterations,
    * where this person is the customer.
    * 
    * @author James Beard
    */
   public void testStoriesForCustomer() throws Exception {
      // record the test user as the customer
      story.setCustomer(developer);
      commitCloseAndOpenSession();
      
      tester.gotoProjectsPage();
      tester.clickLinkWithKey("projects.link.people");
      tester.clickLinkWithText(developerName);
      tester.assertTextInTable("storiesForCustomer", story.getName());
   }

   /**
    * Checks for the table displaying any stories in current iterations,
    * where this person is the tracker. 
    * 
    * @author James Beard
    */
   public void testStoriesForTracker() throws Exception {
      // record the test user as the tracker
      story.setTrackerId(developer.getId());
      commitCloseAndOpenSession();
      
      tester.gotoProjectsPage();
      tester.clickLinkWithKey("projects.link.people");
      tester.clickLinkWithText(developerName);
      tester.assertTextInTable("storiesForTracker", story.getName());
   }

   private void setUpTimeEntry() throws HibernateException, SQLException, RepositoryException {
      newTimeEntry(testTask, developer, 2.0);
      commitCloseAndOpenSession();
   }

   private void moveIterationForward() throws SQLException, HibernateException {
      iteration.setStartDate(DateHelper.getDateDaysFromToday(10));
      iteration.setEndDate(DateHelper.getDateDaysFromToday(24));
      commitCloseAndOpenSession();
   }

   protected void tearDown() throws Exception {
      super.tearDown();
   }
}
