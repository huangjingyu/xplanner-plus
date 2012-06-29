package com.technoetic.xplanner.soap.domain;

import java.util.Calendar;

import net.sf.xplanner.domain.Task;

public class TaskData extends DomainData {
    private int storyId;
    private int acceptorId;
    private Calendar createdDate;
    private String name;
    private String description;
    private boolean completionFlag;
    private String type;
    private String dispositionName;
    private double estimatedOriginalHours;
    private double adjustedEstimatedHours;
    private double estimatedHours;
    private double actualHours;
    private double remainingHours;

    public void setStoryId(int storyId) {
        this.storyId = storyId;
    }

    public int getStoryId() {
        return storyId;
    }

    public void setAcceptorId(int acceptorId) {
        this.acceptorId = acceptorId;
    }

    public int getAcceptorId() {
        return acceptorId;
    }

    public void setCreatedDate(Calendar createdDate) {
        this.createdDate = createdDate;
    }

    public Calendar getCreatedDate() {
        return createdDate;
    }

    public void setEstimatedHours(double estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public double getEstimatedHours() {
        return estimatedHours;
    }

    public double getRemainingHours() {
        return remainingHours;
    }

    public void setRemainingHours(double remainingHours) {
        this.remainingHours = remainingHours;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completionFlag;
    }

    public void setCompleted(boolean completionFlag) {
        this.completionFlag = completionFlag;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setDispositionName(String dispositionName) {
        this.dispositionName = dispositionName;
    }

    public String getDispositionName() {
        return dispositionName;
    }

    public double getActualHours() {
        return actualHours;
    }

    public void setActualHours(double actualHours) {
        this.actualHours = actualHours;
    }

    public double getAdjustedEstimatedHours() {
        return adjustedEstimatedHours;
    }

    public void setAdjustedEstimatedHours(double adjustedEstimatedHours) {
        this.adjustedEstimatedHours = adjustedEstimatedHours;
    }

    public double getEstimatedOriginalHours() {
        return estimatedOriginalHours;
    }

    public void setEstimatedOriginalHours(double estimatedOriginalHours) {
        this.estimatedOriginalHours = estimatedOriginalHours;
    }

    public static Class getInternalClass() {
        return Task.class;
    }
}