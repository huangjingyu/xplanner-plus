package net.sf.xplanner.events;

import net.sf.xplanner.domain.Person;

import com.technoetic.xplanner.domain.Identifiable;


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
public class EventSource {
	private Identifiable domainObject;
	private Person editor;
	
	public EventSource(Identifiable domainObject, Person editor){
		this.domainObject = domainObject;
		this.editor = editor;
	}
	
	public Identifiable getDomainObject() {
		return domainObject;
	}
	public Person getEditor() {
		return editor;
	}
}
