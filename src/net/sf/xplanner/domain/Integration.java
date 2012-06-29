package net.sf.xplanner.domain;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
@Table(name = "integration")
public class Integration implements java.io.Serializable {
	private static final long serialVersionUID = 6948499568758432723L;
	private int id;
	private Integer projectId;
	private Date lastUpdate;
	private Integer personId;
	private Date whenStarted;
	private Date whenRequested;
	private Date whenComplete;
	private Character state;
	private String comments;

	public Integration() {
	}

	public Integration(int id) {
		this.id = id;
	}

	public Integration(int id, Integer projectId, Date lastUpdate,
			Integer personId, Date whenStarted, Date whenRequested,
			Date whenComplete, Character state, String comments) {
		this.id = id;
		this.projectId = projectId;
		this.lastUpdate = lastUpdate;
		this.personId = personId;
		this.whenStarted = whenStarted;
		this.whenRequested = whenRequested;
		this.whenComplete = whenComplete;
		this.state = state;
		this.comments = comments;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "project_id")
	public Integer getProjectId() {
		return this.projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_update", length = 19)
	public Date getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@Column(name = "person_id")
	public Integer getPersonId() {
		return this.personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "when_started", length = 19)
	public Date getWhenStarted() {
		return this.whenStarted;
	}

	public void setWhenStarted(Date whenStarted) {
		this.whenStarted = whenStarted;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "when_requested", length = 19)
	public Date getWhenRequested() {
		return this.whenRequested;
	}

	public void setWhenRequested(Date whenRequested) {
		this.whenRequested = whenRequested;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "when_complete", length = 19)
	public Date getWhenComplete() {
		return this.whenComplete;
	}

	public void setWhenComplete(Date whenComplete) {
		this.whenComplete = whenComplete;
	}

	@Column(name = "state", length = 1)
	public Character getState() {
		return this.state;
	}

	public void setState(Character state) {
		this.state = state;
	}

	@Column(name = "comments")
	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

}
