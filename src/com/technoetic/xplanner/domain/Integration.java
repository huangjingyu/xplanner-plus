package com.technoetic.xplanner.domain;

import java.util.Date;

import net.sf.xplanner.domain.DomainObject;

public class Integration extends DomainObject {
    public static final char PENDING = 'P';
    public static final char ACTIVE = 'A';
    public static final char FINISHED = 'F';
    public static final char CANCELED = 'X';

    private int personId;
    private char state;
    private String comment;
    private Date whenStarted;
    private Date whenRequested;
    private Date whenComplete;
    private int projectId;

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public void setState(char state) {
        this.state = state;
    }

    public char getState() {
        return state;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public String getDescription() {return getComment();}

    public void setWhenStarted(Date whenStarted) {
        this.whenStarted = whenStarted;
    }

    public Date getWhenStarted() {
        return whenStarted;
    }

    public void setWhenRequested(Date whenRequested) {
        this.whenRequested = whenRequested;
    }

    public Date getWhenRequested() {
        return whenRequested;
    }

    public Date getWhenComplete() {
        return whenComplete;
    }

    public void setWhenComplete(Date whenComplete) {
        this.whenComplete = whenComplete;
    }

    public double getDuration() {
        if (whenStarted != null && whenComplete != null) {
            long delta = whenComplete.getTime() - whenStarted.getTime();
            return delta / 3600000.0;
        } else {
            return 0;
        }
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

   //DEBT: LSP violation. Should have a class of domain object that are not Nameable
   public String getName() {
      return "";
   }

   public String toString(){
        return "Integration(id="+this.getId()+
               ", personId="+this.getPersonId()+
               ", projectId="+this.getProjectId()+
               ", comment="+this.getComment()+
               ", lastUpdateTime="+this.getLastUpdateTime()+
               ", whenComplete="+this.getWhenComplete()+
               ", whenStarted="+this.getWhenStarted()+")";
    }
}