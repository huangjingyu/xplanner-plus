package com.technoetic.xplanner.soap.domain;

import java.util.Calendar;

import net.sf.xplanner.domain.TimeEntry;

public class TimeEntryData extends DomainData {
    private int taskId;
    private Calendar startTime;
    private Calendar endTime;
    private double duration;
    private int person1Id;
    private int person2Id;
    private Calendar reportDate;
    private String description = "";

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setPerson1Id(int person1Id) {
        this.person1Id = person1Id;
    }

    public int getPerson1Id() {
        return person1Id;
    }

    public void setPerson2Id(int person2Id) {
        this.person2Id = person2Id;
    }

    public int getPerson2Id() {
        return person2Id;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public Calendar getReportDate() {
        return reportDate;
    }

    public void setReportDate(Calendar reportDate) {
        this.reportDate = reportDate;
    }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public static Class getInternalClass() {
        return TimeEntry.class;
    }
}
