package com.technoetic.xplanner.soap.domain;

import java.util.Calendar;

import net.sf.xplanner.domain.Iteration;

public class IterationData extends DomainData {
    private int projectId;
    private String name;
    private String description;
    private Calendar startDate;
    private Calendar endDate;
    private double daysWorked;
    private double adjustedEstimatedHours = 0;
    private double remainingHours;
    private double estimatedHours;
    private double actualHours;
    private double underestimatedHours;
    private double overestimatedHours;
    private double addedHours;
    private double postponedHours;
    private String statusKey;




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

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getProjectId() {
        return projectId;
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

    public double getUnderestimatedHours() {
        return underestimatedHours;
    }

    public void setUnderestimatedHours(double underestimatedHours) {
        this.underestimatedHours = underestimatedHours;
    }

    public double getOverestimatedHours() {
        return overestimatedHours;
    }

    public void setOverestimatedHours(double overestimatedHours) {
        this.overestimatedHours = overestimatedHours;
    }

    public double getAddedHours() {
        return addedHours;
    }

    public void setAddedHours(double addedHours) {
        this.addedHours = addedHours;
    }

    public double getPostponedHours() {
        return postponedHours;
    }

    public void setPostponedHours(double postponedHours) {
        this.postponedHours = postponedHours;
    }

    public static Class getInternalClass() {
        return Iteration.class;
    }

    public String getStatusKey() {
        return statusKey;
    }

    public void setStatusKey(String statusKey) {
        this.statusKey = statusKey;
    }

    public double getDaysWorked() {
        return daysWorked;
    }

    public void setDaysWorked(double daysWorked) {
        this.daysWorked = daysWorked;
    }
}