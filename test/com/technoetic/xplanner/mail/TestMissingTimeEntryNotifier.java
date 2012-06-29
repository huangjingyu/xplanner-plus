package com.technoetic.xplanner.mail;

/**
 * User: Tomasz Siwiec
 * Date: Nov 02, 2004
 * Time: 10:20:08 PM
 */

import static org.easymock.EasyMock.expect;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import com.technoetic.xplanner.AbstractUnitTestCase;
import com.technoetic.xplanner.db.TaskQueryHelper;

public class TestMissingTimeEntryNotifier extends AbstractUnitTestCase {
   private MissingTimeEntryNotifier missingTimeEntryNotifier;
   private Project project;
   UserStory story;
   private Task task;
   private Person receiver;
   int receiverId = 1;

   TaskQueryHelper mockTaskQueryHelper;
   EmailNotificationSupport mockEmailNotificationSupport;
   private Date endDate;
   private List taskWithNoTimeEntryList;
   private Map projectsToBeNotified;
   private Map notificationEmailMap;

   protected void setUp() throws Exception {
      super.setUp();
      project = new Project();
      story = new UserStory();
      task = new Task();
      project.setName("testProject");
      story.setName("testStory");
      task.setName("testTask");
      task.setId(2);
      receiver = new Person();
      receiver.setId(1);
      endDate = new Date();
      mockEmailNotificationSupport = createLocalMock(EmailNotificationSupport.class);
      mockTaskQueryHelper = createLocalMock(TaskQueryHelper.class);
      missingTimeEntryNotifier = new MissingTimeEntryNotifier(mockTaskQueryHelper, mockEmailNotificationSupport);
      taskWithNoTimeEntryList = new ArrayList();
      projectsToBeNotified = new HashMap();
      notificationEmailMap = new HashMap();
      taskWithNoTimeEntryList.add(new Object[]{task, story, project});
   }

   public void tearDown() throws Exception {
      super.tearDown();
   }

   public void testSendMissingTimeEntryReportToLeads() throws Exception {
      Person acceptor = new Person();
      acceptor.setId(2);
      List taskWithNoTimeEntryList = new ArrayList();
      taskWithNoTimeEntryList.add(new Object[]{task, story, receiver, acceptor});
      expect(mockTaskQueryHelper.getProjectLeedsEmailNotification(endDate)).andReturn(
                                                 taskWithNoTimeEntryList);
      Map notificationEmailMap = new HashMap();
      mockEmailNotificationSupport.compileEmail(notificationEmailMap, receiver.getId(), acceptor, task, story);
      mockEmailNotificationSupport.sendNotifications(notificationEmailMap,
                                                     MissingTimeEntryNotifier.EMAIL_BODY_HEADER_FOR_PROJECT_LEADERS,
                                                     MissingTimeEntryNotifier.SUBJECT_FOR_PROJECT_LEADS);
      replay();
      missingTimeEntryNotifier.sendMissingTimeEntryReportToLeads(endDate);
      verify();
   }

   public void testSendMissingTimeEntryReminderToAcceptors_ProjectToBeNotifiedSet() throws Exception {
      expect(mockTaskQueryHelper.getTaskAcceptorsEmailNotification(endDate)).andReturn(
                                                 taskWithNoTimeEntryList);
      Map projectsToBeNotified = new HashMap();
      expect(mockEmailNotificationSupport.isProjectToBeNotified(
            projectsToBeNotified,
            project)).andReturn(true);
      Map notificationEmailMap = new HashMap();
      mockEmailNotificationSupport.compileEmail(notificationEmailMap, task.getAcceptorId(), null, task, story);
      mockEmailNotificationSupport.sendNotifications(notificationEmailMap,
                                                     MissingTimeEntryNotifier.EMAIL_BODY_HEADER_FOR_ACCEPTORS,
                                                     MissingTimeEntryNotifier.SUBJECT_FOR_ACCEPTORS);
      replay();
      missingTimeEntryNotifier.sendMissingTimeEntryReminderToAcceptors(endDate);
      verify();
   }

   public void testSendMissingTimeEntryReminderToAcceptors_ProjectToBeNotifiedNotSet() throws Exception {
      expect(mockTaskQueryHelper.getTaskAcceptorsEmailNotification(endDate)).andReturn(taskWithNoTimeEntryList);
      expect(mockEmailNotificationSupport.isProjectToBeNotified(
            projectsToBeNotified,
            project)).andReturn(false);
      mockEmailNotificationSupport.sendNotifications(notificationEmailMap,
                                                     MissingTimeEntryNotifier.EMAIL_BODY_HEADER_FOR_ACCEPTORS,
                                                     MissingTimeEntryNotifier.SUBJECT_FOR_ACCEPTORS);
      replay();
      missingTimeEntryNotifier.sendMissingTimeEntryReminderToAcceptors(endDate);
      verify();
   }
}
