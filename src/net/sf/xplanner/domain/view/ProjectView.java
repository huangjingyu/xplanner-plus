package net.sf.xplanner.domain.view;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.NamedObject;
import net.sf.xplanner.domain.Note;
import net.sf.xplanner.domain.Person;

//@Entity
//@Loader(namedQuery="project.loadView")
//@NamedQuery(name="project.loadView", query="select project.id as id,project.name as name from project in class net.sf.xplanner.domain.Project where project.id=?")
@XmlRootElement
public class ProjectView extends NamedObject{
	private Boolean hidden;
    private List<Person> notificationReceivers;
	private List<Note> notes;
	private Iteration currentIteration;
	
	public Boolean getHidden() {
		return hidden;
	}
	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}

//	@OneToMany()
//    @JoinTable(name="notification_receivers", joinColumns=@JoinColumn(name="attachedTo_id"))
	@XmlTransient
	public List<Person> getNotificationReceivers() {
		return notificationReceivers;
	}
	public void setNotificationReceivers(List<Person> notificationReceivers) {
		this.notificationReceivers = notificationReceivers;
	}

//	@ManyToMany()
//    @JoinTable(name="note", joinColumns=@JoinColumn(name="project_id"), inverseJoinColumns=@JoinColumn(name="person_id"))
	@XmlTransient
	public List<Note> getNotes() {
		return notes;
	}
	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}
	
	@XmlTransient
	public Iteration getCurrentIteration() {
		return currentIteration;
	}
	public void setCurrentIteration(Iteration currentIteration) {
		this.currentIteration = currentIteration;
	}
	
}
