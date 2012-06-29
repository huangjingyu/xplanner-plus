/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */
package com.technoetic.xplanner.mail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import org.apache.log4j.Logger;

import com.technoetic.xplanner.DomainSpecificPropertiesFactory;
import com.technoetic.xplanner.XPlannerProperties;

/**
 * User: mprokopowicz
 * Date: Feb 3, 2006
 * Time: 5:36:56 PM
 */
public class EmailNotificationSupport {
	private EmailFormatter emailFormatter;
	private DomainSpecificPropertiesFactory propertiesFactory;
	private static final Logger log = Logger.getLogger(EmailNotificationSupport.class);
	private EmailMessageFactory emailMessageFactory;
	static final String XPLANNER_MAIL_FROM_KEY = "xplanner.mail.from";

	public EmailNotificationSupport(EmailFormatter emailFormatter,
		EmailMessageFactory emailMessageFactory,
		DomainSpecificPropertiesFactory propertiesFactory) {
		this.emailFormatter = emailFormatter;
		this.emailMessageFactory = emailMessageFactory;
		this.propertiesFactory = propertiesFactory;
	}

	public void sendNotifications(Map<Integer, List<Object>> notificationEmails, Map<String, Object> params) {
		Set<Integer> keySet = notificationEmails.keySet();
		Iterator<Integer> iterator = keySet.iterator();
		ResourceBundle bundle = ResourceBundle.getBundle("EmailResourceBundle");
		String subject = bundle.getString((String)params.get(EmailFormatterImpl.SUBJECT));
		while (iterator.hasNext()) {
			Integer id = iterator.next();
			List<Object> bodyEntryList = notificationEmails.get(id);
			try {
				EmailMessage emailMessage = emailMessageFactory.createMessage();
				emailMessage.setRecipient(id.intValue());
				emailMessage.setSubject(subject);
				emailMessage.setFrom(propertiesFactory.getDefaultProperties().getProperty(XPLANNER_MAIL_FROM_KEY));
				String formatedText = emailFormatter.formatEmailEntry(bodyEntryList, params);
				emailMessage.setBody(formatedText);
				emailMessage.send();
			} catch (Exception e) {
				log.error("Error sending email: ", e);
			}
		}
	}

	public void sendNotifications(Map<Integer, List<Object>> notificationEmails, String emailHeaderKey, String subjectKey) {
		Set<Integer> keySet = notificationEmails.keySet();
		Iterator<Integer> iterator = keySet.iterator();
		ResourceBundle bundle = ResourceBundle.getBundle("ResourceBundle");
		String subject = bundle.getString(subjectKey);
		String emailHeader = bundle.getString(emailHeaderKey);
		String emailFooter = bundle.getString(MissingTimeEntryNotifier.EMAIL_BODY_FOOTER);
		String emailTaskHeader = bundle.getString(MissingTimeEntryNotifier.EMAIL_TASK_HEADER);
		String emailStoryHeader = bundle.getString(MissingTimeEntryNotifier.EMAIL_STORY_HEADER);
		while (iterator.hasNext()) {
			Integer id = iterator.next();
			List<Object> bodyEntryList = notificationEmails.get(id);
			try {
				EmailMessage emailMessage = emailMessageFactory.createMessage();
				emailMessage.setRecipient(id.intValue());
				emailMessage.setSubject(subject);
				emailMessage.setFrom(propertiesFactory.getDefaultProperties().getProperty(XPLANNER_MAIL_FROM_KEY));
				String formatedText = emailFormatter.formatEmailEntry(emailHeader,
					emailFooter,
					emailStoryHeader,
					emailTaskHeader,
					bodyEntryList);
				emailMessage.setBody(formatedText);
				emailMessage.send();
			} catch (Exception e) {
				log.error("Error sending email: ", e);
			}
		}
	}

	public void compileEmail(Map<Integer, List<Object>> notificationEmails, int receiverId, Person acceptor, Task task,
		UserStory story) {
		List<Object> emailBodyList;
		if (notificationEmails.containsKey(new Integer(receiverId))) {
			emailBodyList = notificationEmails.get(new Integer(receiverId));
		} else {
			emailBodyList = new ArrayList<Object>();
			notificationEmails.put(new Integer(receiverId), emailBodyList);
		}
		List<Object> entryList = new ArrayList<Object>();
		entryList.add(task);
		entryList.add(story);
		if (acceptor != null) {
			entryList.add(acceptor.getName());
		}
		emailBodyList.add(entryList);
	}

	public boolean isProjectToBeNotified(Map<Integer, Boolean> projectsToBeNotified,
		Project project) {
		Boolean isNotified = projectsToBeNotified.get(new Integer(project.getId()));
		if (isNotified == null) {
			isNotified = Boolean.valueOf(isProjectToBeNotified(project));
			projectsToBeNotified.put(new Integer(project.getId()), isNotified);
		}
		return isNotified.booleanValue();
	}

	public boolean isProjectToBeNotified(Project project) {
		Properties projectDynamicProperties = propertiesFactory.createPropertiesFor(project);
		String stringValue = projectDynamicProperties.getProperty(XPlannerProperties.SEND_NOTIFICATION_KEY, Boolean.TRUE.toString());
		return Boolean.valueOf(stringValue).booleanValue();
	}
}
