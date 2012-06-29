package net.sf.xplanner.domain;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKey;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import com.technoetic.xplanner.domain.NoteAttachable;
import com.technoetic.xplanner.domain.repository.IterationRepository;

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
@Table(name = "project")
@XmlRootElement
public class Project extends NamedObject implements java.io.Serializable, NoteAttachable {
	private static final long serialVersionUID = -6137321799143662647L;
	public static final String HIDDEN = "hidden";
	private Boolean hidden;
	private Iteration backlog;
    private List<Iteration> iterations = new ArrayList<Iteration>();
	private List<Person> notificationReceivers;

	public Project() {
	}

	@Column(name = "is_hidden")
	public Boolean getHidden() {
		return this.hidden;
	}

	public void setHidden(Boolean isHidden) {
		this.hidden = isHidden;
	}

	@OneToMany(mappedBy="project", cascade=CascadeType.ALL)
	public List<Iteration> getIterations() {
		return iterations;
	}

	public void setIterations(List<Iteration> iterations) {
		this.iterations = iterations;
	}

    public void setNotificationReceivers(List<Person> notificationReceivers) {
        this.notificationReceivers = notificationReceivers;
    }
    
    @ManyToMany()
    @JoinTable(name="notification_receivers", joinColumns=@JoinColumn(name="project_id"), inverseJoinColumns=@JoinColumn(name="person_id"))
	public List<Person> getNotificationReceivers() {
		return notificationReceivers;
	}

    @OneToOne(cascade=CascadeType.REMOVE)
    @JoinColumn(name="backlog_id", insertable=true, updatable=true, unique=true, nullable=true)
    public Iteration getBacklog() {
		return backlog;
	}

	public void setBacklog(Iteration backlog) {
		this.backlog = backlog;
	}

	@Transient
    public Iteration getCurrentIteration() {
        return IterationRepository.getCurrentIteration(getId());
    }

    @Transient
	public Boolean isHidden() {
		return this.hidden;
	}
    
    @ElementCollection
    @JoinTable(name="attribute" , joinColumns=@JoinColumn(name="targetId"))
    @MapKeyColumn(name="name")
    @Column(name="\"value\"")
	public Map<String,String> getAttributes() {
		return super.getAttributes();
	}

}
