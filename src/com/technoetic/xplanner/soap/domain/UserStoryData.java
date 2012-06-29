package com.technoetic.xplanner.soap.domain;

import net.sf.xplanner.domain.UserStory;


public class UserStoryData extends DomainData {
    private int iterationId;
    private int trackerId;
    private String name;
    private String description;
    private double adjustedEstimatedHours = 0;
    private double estimatedOriginalHours;
    private double remainingHours;
    private double estimatedHours;
    private double actualHours;
    private double postponedHours;
    private int priority;
    private String dispositionName;
    private int customerId;
    private boolean completed;

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

    public void setTrackerId(int trackerId) {
        this.trackerId = trackerId;
    }

    public int getTrackerId() {
        return trackerId;
    }

    public void setIterationId(int iterationId) {
        this.iterationId = iterationId;
    }

    public int getIterationId() {
        return iterationId;
    }

    public double getEstimatedHours() {
        return estimatedHours;
    }

    public void setEstimatedHours(double estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public double getActualHours() {
        return actualHours;
    }

    //TODO : should not be available in generated DTO. Can it be done? JM 2-22-4
    public void setActualHours(double actualHours) {
        this.actualHours = actualHours;
    }

    public double getAdjustedEstimatedHours() {
        return adjustedEstimatedHours;
    }

    public void setAdjustedEstimatedHours(double adjustedEstimatedHours) {
        this.adjustedEstimatedHours = adjustedEstimatedHours;
    }

    public double getRemainingHours() {
        return remainingHours;
    }

    public void setRemainingHours(double remainingHours) {
        this.remainingHours = remainingHours;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public String getDispositionName() {
        return dispositionName;
    }

    public void setDispositionName(String dispositionName) {
        this.dispositionName = dispositionName;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public double getEstimatedOriginalHours() {
        return estimatedOriginalHours;
    }

    public void setEstimatedOriginalHours(double estimatedOriginalHours) {
        this.estimatedOriginalHours = estimatedOriginalHours;
    }

    public double getPostponedHours() {
        return postponedHours;
    }

    public void setPostponedHours(double postponedHours) {
        this.postponedHours = postponedHours;
    }

    public static Class getInternalClass() {
        return UserStory.class;
    }
}