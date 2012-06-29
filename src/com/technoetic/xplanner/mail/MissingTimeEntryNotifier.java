/*
 * Copyright (c) Mateusz Prokopowicz. All Rights Reserved.
 */

package com.technoetic.xplanner.mail;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import org.apache.log4j.Logger;

import com.technoetic.xplanner.db.TaskQueryHelper;
import com.technoetic.xplanner.util.TimeGenerator;

/**
 * User: Mateusz Prokopowicz
 * Date: Apr 19, 2005
 * Time: 1:34:22 PM
 */
public class MissingTimeEntryNotifier implements com.technoetic.xplanner.Command {
   private static Logger log = Logger.getLogger(MissingTimeEntryNotifier.class);

   public static final String EMAIL_TASK_HEADER = "iteration.metrics.tableheading.task";
   public static final String EMAIL_STORY_HEADER = "iteration.metrics.tableheading.story";
   public static final String SUBJECT_FOR_ACCEPTORS = "job.emailnotifier.subjectForAcceptors";
   public static final String SUBJECT_FOR_PROJECT_LEADS = "job.emailnotifier.subjectForProjectLeeds";
   public static final String EMAIL_BODY_HEADER_FOR_ACCEPTORS = "job.emailnotifier.bodyHeaderForAcceptors";
   public static final String EMAIL_BODY_HEADER_FOR_PROJECT_LEADERS = "job.emailnotifier.bodyheaderForProjectLeeds";
   public static final String EMAIL_BODY_FOOTER = "job.emailnotifier.bodyFooter";

   private final TaskQueryHelper taskQueryHelper;
   private final EmailNotificationSupport emailNotificationSupport;
   private TimeGenerator timeGenerator;
   
   public void setTimeGenerator(TimeGenerator timeGenerator) {
      this.timeGenerator = timeGenerator;
   }


   public MissingTimeEntryNotifier(TaskQueryHelper taskQueryHelper, EmailNotificationSupport emailNotificationSupport) {
      this.taskQueryHelper = taskQueryHelper;
      this.emailNotificationSupport = emailNotificationSupport;
   }

   public void sendMissingTimeEntryReportToLeads(Date endDate) {
      Collection col;
      HashMap notificationEmails = new HashMap();
      col = taskQueryHelper.getProjectLeedsEmailNotification(endDate);
      for (Iterator iterator = col.iterator(); iterator.hasNext();) {
         Object[] objects = (Object[]) iterator.next();
         Task task = (Task) objects[0];
         UserStory story = (UserStory) objects[1];
         Person receiver = (Person) objects[2];
         Person acceptor = (Person) objects[3];

         emailNotificationSupport.compileEmail(notificationEmails, receiver.getId(), acceptor, task, story);
      }
      emailNotificationSupport.sendNotifications(notificationEmails,
                                                 EMAIL_BODY_HEADER_FOR_PROJECT_LEADERS,
                                                 SUBJECT_FOR_PROJECT_LEADS);
      log.debug("Notifications have been sent to project leads.");
   }

   public void sendMissingTimeEntryReminderToAcceptors(Date endDate) {
      Map projectsToBeNotified = new HashMap();
      Map notificationEmails = new HashMap();
      Collection col = taskQueryHelper.getTaskAcceptorsEmailNotification(endDate);
      for (Iterator it = col.iterator(); it.hasNext();) {
         Object[] objects = (Object[]) it.next();
         Task task = (Task) objects[0];
         UserStory story = (UserStory) objects[1];
         Project project = (Project) objects[2];
         if (emailNotificationSupport.isProjectToBeNotified(projectsToBeNotified, project)) {
            emailNotificationSupport.compileEmail(notificationEmails,
                                                  task.getAcceptorId(),
                                                  null,
                                                  task,
                                                  story);
         }
      }
      emailNotificationSupport.sendNotifications(notificationEmails,
                                                 EMAIL_BODY_HEADER_FOR_ACCEPTORS,
                                                 SUBJECT_FOR_ACCEPTORS);
      log.debug("Notifications have been sent to acceptors.");
   }

   public void execute() {
      //fixme the notifier should be configured with a time window to check for idle started tasks. Right now we hard code the previous day which may not be the right day if the job schedule is changed and if team is distributed
      Date yesterdayMidnight = TimeGenerator.shiftDate(timeGenerator.getTodaysMidnight(), Calendar.DATE, -1);
      log.debug("Send notification for tasks with no time entry after "+ yesterdayMidnight);
      sendMissingTimeEntryReminderToAcceptors(yesterdayMidnight);
      sendMissingTimeEntryReportToLeads(yesterdayMidnight);
   }
}
