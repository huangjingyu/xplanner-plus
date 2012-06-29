package net.sf.xplanner.domain.view;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import net.sf.xplanner.domain.NamedObject;

import com.technoetic.xplanner.domain.StoryDisposition;
import com.technoetic.xplanner.domain.StoryStatus;

@XmlRootElement
public class UserStoryView extends NamedObject {
	private double estimatedHours;
	private int priority;
	private char status = StoryStatus.DRAFT.getCode();
	private Double originalEstimatedHours;
	private char dispositionCode = StoryDisposition.PLANNED.getCode();
	private double postponedHours;
	private int orderNo;
	private double actualHours;
	private int trackerId;
	private List<TaskView> tasks = new ArrayList<TaskView>();

	public double getEstimatedHours() {
		return estimatedHours;
	}
	public void setEstimatedHours(double estimatedHours) {
		this.estimatedHours = estimatedHours;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	public Double getOriginalEstimatedHours() {
		return originalEstimatedHours;
	}
	public void setOriginalEstimatedHours(Double originalEstimatedHours) {
		this.originalEstimatedHours = originalEstimatedHours;
	}
	public char getDispositionCode() {
		return dispositionCode;
	}
	public void setDispositionCode(char dispositionCode) {
		this.dispositionCode = dispositionCode;
	}
	public double getPostponedHours() {
		return postponedHours;
	}
	public void setPostponedHours(double postponedHours) {
		this.postponedHours = postponedHours;
	}
	public int getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	public double getActualHours() {
		return actualHours;
	}
	public void setActualHours(double actualHours) {
		this.actualHours = actualHours;
	}
	@XmlElement(name="task")
	public List<TaskView> getTasks() {
		return tasks;
	}
	public void setTasks(List<TaskView> tasks) {
		this.tasks = tasks;
	}
	public int getTrackerId() {
		return trackerId;
	}
	public void setTrackerId(int trackerId) {
		this.trackerId = trackerId;
	}
}
