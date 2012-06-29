package net.sf.xplanner.domain;


import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
*    XplannerPlus, agile planning software
*    @author Maksym_Chyrkov. 
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

@Entity
@Table(name = "person_role")
public class PersonRole implements java.io.Serializable {
	private static final long serialVersionUID = 4005621505117736352L;
	private PersonRoleId id;

	public PersonRole() {
	}

	public PersonRole(PersonRoleId id) {
		this.id = id;
	}

	public PersonRole(int projectId, int personId, int roleId) {
		id = new PersonRoleId(roleId,personId, projectId);
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "roleId", column = @Column(name = "role_id", nullable = false)),
			@AttributeOverride(name = "personId", column = @Column(name = "person_id", nullable = false)),
			@AttributeOverride(name = "projectId", column = @Column(name = "project_id", nullable = false)) })
	public PersonRoleId getId() {
		return this.id;
	}

	public void setId(PersonRoleId id) {
		this.id = id;
	}
	
	@Transient
	public int getRoleId() {
		return id.getRoleId();
	}
	
	@Transient
	public int getPersonId() {
		return id.getPersonId();
	}
	
	@Transient
	public int getProjectId() {
		return this.getProjectId();
	}

}
