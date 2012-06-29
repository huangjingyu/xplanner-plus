package net.sf.xplanner.email;

import net.sf.xplanner.events.ObjectCreated;
import net.sf.xplanner.events.ObjectDeleted;
import net.sf.xplanner.events.ObjectUpdated;

import com.technoetic.xplanner.mail.EmailNotificationSupport;

/**
*    XplannerPlus, agile planning software
*    @author Maksym.Chyrkov. 
*    Copyright (C) 2009  Maksym Chyrkov
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>
* 	 
*/
public class EmailHelper {
	private EmailNotificationSupport emailNotificationSupport;
	
	public void setEmailNotificationSupport(
			EmailNotificationSupport emailNotificationSupport) {
		this.emailNotificationSupport = emailNotificationSupport;
	}

	public void sendEmail(ObjectDeleted event) {
//		EventSource source = event.getSource();
//		DomainObject domainObject = source.getDomainObject();
//		if(domainObject.getClass().getAnnotation(Content.class) != null){
//			Map<String, Object> context = EmailContextHelper.resolveContext(domainObject);
//		}
//		
//		try{
//
//			Person acceptor = event.getSource();
//			Person oldAcceptor = null;
//
//			if(task.getAcceptorId() !=0){
//				acceptor = (Person) repository.load(task.getAcceptorId());
//				if((oldTask ==null) || (task.getAcceptorId() == oldTask.getAcceptorId())){
//					oldAcceptor = acceptor;
//				}
//			}
//			if(oldAcceptor == null && task.getAcceptorId() != 0){
//				oldAcceptor = (Person) repository.load(task.getAcceptorId());
//			}
//
//			Person editor = (Person) repository.load(remoteUserId);
//			int storyId = NumberUtils.toInt(request.getParameter("fkey"),0);
//			if(storyId ==0){
//				if(task.getUserStory() != null){
//					storyId = task.getUserStory().getId();
//				}else{
//					return;
//				}
//			}
//			UserStory story = (UserStory) getRepository(UserStory.class).load(storyId);
//			Map<Integer, List<Object>> notificationEmails = new HashMap<Integer, List<Object>>();
//			emailNotificationSupport.compileEmail(notificationEmails, remoteUserId, acceptor, task, story);
//			if(acceptor != null){
//				emailNotificationSupport.compileEmail(notificationEmails, acceptor.getId(), editor, task, story);
//			}
//			Map<String, Object> params = new HashMap<String, Object>();
//			if(action.equals(CREATE_ACTION)){
//				params.put(EmailFormatterImpl.TEMPLATE, "com/technoetic/xplanner/mail/velocity/email_notification_task_created.vm");
//				params.put(EmailFormatterImpl.SUBJECT, "task.created.subject");
//				params.put(EmailFormatterImpl.TITLE, "task.created.title");
//				params.put(EmailFormatterImpl.HEADER, "task.created.header");
//				params.put(EmailFormatterImpl.FOOTER, "task.created.footer");
//			}else{
//				if(oldAcceptor != null &&((acceptor == null) || oldAcceptor.equals(acceptor))){
//					emailNotificationSupport.compileEmail(notificationEmails, oldAcceptor.getId(), editor, task, story);
//				}
//				params.put(EmailFormatterImpl.TEMPLATE, "com/technoetic/xplanner/mail/velocity/email_notification_task_updated.vm");
//				params.put(EmailFormatterImpl.SUBJECT, "task.updated.subject");
//				params.put(EmailFormatterImpl.TITLE, "task.updated.title");
//				params.put(EmailFormatterImpl.HEADER, "task.updated.header");
//				params.put(EmailFormatterImpl.FOOTER, "task.updated.footer");
//				params.put("oldTask", oldTask);
//			}
//			if(acceptor != null){
//				params.put("asignee", acceptor.getName());
//			}else{
//				params.put("asignee", "Unassigned");
//			}
//			if(oldAcceptor != null){
//				params.put("oldAsignee", oldAcceptor.getName());
//			}else{
//				params.put("oldAsignee", "Unassigned");
//			}
//
//			params.put("creator", editor.getName());
//			params.put("task", task);
//			emailNotificationSupport.sendNotifications(notificationEmails, params);
//			System.out.println("Email was sent to:");
//			System.out.println(acceptor);
//			System.out.println(oldAcceptor);
//			System.out.println(editor);
			
		// TODO Auto-generated method stub
		
	}

	public void sendEmail(ObjectCreated event) {
		// TODO Auto-generated method stub
		
	}

	public void sendEmail(ObjectUpdated event) {
		// TODO Auto-generated method stub
		
	}

}
