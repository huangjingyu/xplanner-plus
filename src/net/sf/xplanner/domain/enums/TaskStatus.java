package net.sf.xplanner.domain.enums;

import org.apache.commons.lang.StringUtils;

public enum TaskStatus {
	NON_STARTED(' ', "notStarted"), STARTED('S', "started"), COMPLETED('C', "completed");
	
	private char status;
	private String statusName;

	private TaskStatus(char status, String statusName) {
		this.status = status;
		this.statusName = statusName;
	}
	
	public static TaskStatus fromCode(char code) {
		TaskStatus[] values = TaskStatus.values();
		for (TaskStatus taskStatus : values) {
			if(taskStatus.status == code) {
				return taskStatus;
			}
		}
		return TaskStatus.NON_STARTED;
	}
	
	public static TaskStatus fromName(String name) {
		TaskStatus[] values = TaskStatus.values();
		for (TaskStatus taskStatus : values) {
			if(StringUtils.equalsIgnoreCase(taskStatus.statusName,name)) {
				return taskStatus;
			}
		}
		return TaskStatus.NON_STARTED;
	}
}
