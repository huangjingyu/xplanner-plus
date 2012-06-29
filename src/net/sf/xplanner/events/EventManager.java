package net.sf.xplanner.events;

import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.Person;

import org.apache.struts.action.ActionForm;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;

import com.technoetic.xplanner.domain.Identifiable;
import com.technoetic.xplanner.domain.Nameable;

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

public class EventManager implements ApplicationContextAware {
	private ApplicationContext applicationContext;
	
	public void setApplicationContext(ApplicationContext applicationContext){
		this.applicationContext = applicationContext;
	}
	
	public void publishUpdateEvent(ActionForm actionForm, Nameable domainObject, Person editor) {
		ApplicationEvent event = new ObjectUpdated(actionForm, new EventSource(domainObject, editor));
		applicationContext.publishEvent(event);
	}
	
	public void publishCreateEvent(Identifiable domainObject, Person editor){
		ApplicationEvent event = new ObjectCreated(new EventSource(domainObject, editor));
		applicationContext.publishEvent(event);
	}

	public void publishDeleteEvent(DomainObject domainObject, Person editor){
		ApplicationEvent event = new ObjectDeleted(new EventSource(domainObject, editor));
		applicationContext.publishEvent(event);
	}
}
