package net.sf.xplanner.email;

import net.sf.xplanner.events.ObjectCreated;
import net.sf.xplanner.events.ObjectDeleted;
import net.sf.xplanner.events.ObjectUpdated;
import net.sf.xplanner.events.XplannerEvent;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;


/**
*    XplannerPlus, agile planning software
*    @author Maksym. 
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

public class EmailPerChangeListener  implements ApplicationListener {
	private EmailHelper emailHelper;
	
	public void setEmailHelper(EmailHelper emailHelper) {
		this.emailHelper = emailHelper;
	}

	public void onApplicationEvent(ApplicationEvent event) {
		if(!event.getClass().isAnnotationPresent(XplannerEvent.class)){
			return;
		}
		if (event instanceof ObjectUpdated) {
			sendObjectWasUpdatedEmail((ObjectUpdated) event);
		}else if (event instanceof ObjectCreated) {
			sendObjectWasCreatedEmail((ObjectCreated) event);
		}else if (event instanceof ObjectDeleted) {
			sendObjectWasDeletedEmail((ObjectDeleted) event);
		}
	}

	private void sendObjectWasDeletedEmail(ObjectDeleted event) {
		emailHelper.sendEmail(event);
	}

	private void sendObjectWasCreatedEmail(ObjectCreated event) {
		emailHelper.sendEmail(event);
	}

	private void sendObjectWasUpdatedEmail(ObjectUpdated event) {
		emailHelper.sendEmail(event);
	}

}
