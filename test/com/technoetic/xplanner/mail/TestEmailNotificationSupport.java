/*
 * Copyright (c) Mateusz Prokopowicz. All Rights Reserved.
 */

package com.technoetic.xplanner.mail;

/**
 * User: Tomasz Siwiec
 * Date: Nov 02, 2004
 * Time: 10:20:08 PM
 */

import static org.easymock.EasyMock.expect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import com.technoetic.xplanner.AbstractUnitTestCase;
import com.technoetic.xplanner.DomainSpecificPropertiesFactory;
import com.technoetic.xplanner.XPlannerProperties;

public class TestEmailNotificationSupport extends AbstractUnitTestCase {
   private EmailNotificationSupport emailNotificationSupport;
   private int personId;
   Project project;
   UserStory story;
   Task task;

   EmailMessageFactory mockEmailMessageFactory;
   EmailMessage mockEmailMessage;
   EmailFormatter mockEmailFormatter;
   DomainSpecificPropertiesFactory mockDomainSpecificPropertiesFactory;
   protected static final String FROM_EMAIL = "fromEmail";

   protected void setUp() throws Exception {
      super.setUp();
      project = new Project();
      story = new UserStory();
      task = new Task();
      project.setName("testProject");
      project.setId(3);
      story.setName("testStory");
      task.setName("testTask");
      task.setId(2);
      personId = 1;
      mockEmailFormatter = createLocalMock(EmailFormatter.class);
      mockEmailMessageFactory = createLocalMock(EmailMessageFactory.class);
      mockEmailMessage = createLocalMock(EmailMessage.class);
      mockDomainSpecificPropertiesFactory = createLocalMock(DomainSpecificPropertiesFactory.class);
      emailNotificationSupport =
            new EmailNotificationSupport(mockEmailFormatter,
                                         mockEmailMessageFactory,
                                         mockDomainSpecificPropertiesFactory);

   }

   public void tearDown() throws Exception {
      super.tearDown();
   }

   public void testSendNotification() throws Exception {
      ResourceBundle bundle = ResourceBundle.getBundle("ResourceBundle");
      String emailHeader = bundle.getString(MissingTimeEntryNotifier.EMAIL_BODY_HEADER_FOR_PROJECT_LEADERS);
      String emailFooter = bundle.getString(MissingTimeEntryNotifier.EMAIL_BODY_FOOTER);
      String emailTaskHeader = bundle.getString(MissingTimeEntryNotifier.EMAIL_TASK_HEADER);
      String emailStoryHeader = bundle.getString(MissingTimeEntryNotifier.EMAIL_STORY_HEADER);
      expect(mockEmailMessageFactory.createMessage()).andReturn(mockEmailMessage);
      Properties mockProperties = new Properties();
      mockProperties.setProperty(EmailNotificationSupport.XPLANNER_MAIL_FROM_KEY, FROM_EMAIL);
      expect(
            mockDomainSpecificPropertiesFactory.getDefaultProperties()).andReturn(mockProperties);
      Map notificationEmails = new HashMap();
      List emailEntryList = new ArrayList();
      emailEntryList.add(task);
      emailEntryList.add(story);
      notificationEmails.put(new Integer(personId), emailEntryList);
      mockEmailMessage.setRecipient(personId);
      mockEmailMessage.setSubject(bundle.getString(MissingTimeEntryNotifier.SUBJECT_FOR_PROJECT_LEADS));
      mockEmailMessage.setFrom(FROM_EMAIL);
      String formatedBody = "formated body";
      expect(mockEmailFormatter.formatEmailEntry(emailHeader,
                                                                                   emailFooter,
                                                                                   emailStoryHeader,
                                                                                   emailTaskHeader,
                                                                                   emailEntryList)).andReturn(formatedBody);
      mockEmailMessage.setBody(formatedBody);
      mockEmailMessage.send();
      replay();
      emailNotificationSupport.sendNotifications(notificationEmails,
                                                 MissingTimeEntryNotifier.EMAIL_BODY_HEADER_FOR_PROJECT_LEADERS,
                                                 MissingTimeEntryNotifier.SUBJECT_FOR_PROJECT_LEADS);
      verify();
   }

   public void testCompileEmail() throws Exception {
      Map notificationEmails = new HashMap();
      emailNotificationSupport.compileEmail(notificationEmails, personId, null, task, story);
      assertTrue("Entry doesn't exist", notificationEmails.containsKey(new Integer(personId)));
   }


   public void testIsProjectToBeNotified() throws Exception {
      Map projectToBeNotifiedMap = new HashMap();
      Properties properties = new Properties();
      properties.setProperty(XPlannerProperties.SEND_NOTIFICATION_KEY, "false");
      expect(mockDomainSpecificPropertiesFactory.createPropertiesFor(
            project)).andReturn(properties);
      replay();
      emailNotificationSupport.isProjectToBeNotified(projectToBeNotifiedMap, project);
      verify();
      assertTrue(projectToBeNotifiedMap.containsKey(new Integer(project.getId())));
   }
}
