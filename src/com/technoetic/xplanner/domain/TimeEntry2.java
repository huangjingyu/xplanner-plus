package com.technoetic.xplanner.domain;

import com.technoetic.xplanner.XPlannerProperties;

public class TimeEntry2 extends net.sf.xplanner.domain.DomainObject {
    private int taskId;
    private java.util.Date startTime;
    private java.util.Date endTime;
    private double duration;
    private int person1Id;
    private int person2Id;
    private java.util.Date reportDate;
    private String description;

    public TimeEntry2() {
    }

    public TimeEntry2(int id) {
        setId(id);
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void setStartTime(java.util.Date startTime) {
        this.startTime = startTime;
    }

    public java.util.Date getStartTime() {
        return startTime;
    }

    public void setEndTime(java.util.Date endTime) {
        this.endTime = endTime;
    }

    public java.util.Date getEndTime() {
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
        if (startTime != null && endTime != null) {
            duration = (endTime.getTime() - startTime.getTime()) / 3600000.0;
        }
        return duration;
    }

    /**
     * The classic way for XPlanner to calculate "effort" is in idea wall clock time or
     * pair-programming hours. Some teams would like to do labor tracking using XPlanner
     * and want to double the effort measured for a paired time entry.
     *
     * This behavior is configurable in xplanner.properties.
     * @return measured effort
     */
    public double getEffort() {
       boolean adjustHoursForPairing = "double".equalsIgnoreCase(new XPlannerProperties().getProperty("xplanner.pairprogramming", "single"));
        boolean isPairedEntry = person1Id != 0 && person2Id != 0;
        return (adjustHoursForPairing && isPairedEntry) ? getDuration() * 2 : getDuration();
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public java.util.Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(java.util.Date reportDate) {
        this.reportDate = reportDate;
    }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public boolean isCurrentlyActive(int personId) {
    return startTime != null && endTime == null && duration == 0
        && (personId == person1Id || personId == person2Id);
  }

   //DEBT: LSP violation. Should have a class of domain object that are not Nameable
   public String getName() {
      return "";
   }

   public String toString(){
      return "TimeEntry(id="+this.getId()+
             ", person1Id="+this.getPerson1Id()+
             ", person2Id="+this.getPerson2Id()+
             ", taskId="+this.getTaskId()+
             ", startTime="+this.getStartTime()+
             ", endTime="+this.getEndTime()+
             ", description="+this.getDescription()+")";
  }

}