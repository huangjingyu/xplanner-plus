package com.technoetic.xplanner.domain.virtual;

import java.io.Serializable;
import java.math.BigDecimal;

public class TimesheetEntry implements Serializable {

    private String projectName;
    private String iterationName;
    private String storyName;
    private BigDecimal totalDuration = new BigDecimal(0.0);
    private String personName;
    private int projectId;
    private int iterationId;
    private int storyId;

    public TimesheetEntry() {
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getIterationName() {
        return iterationName;
    }

    public void setIterationName(String iterationName) {
        this.iterationName = iterationName;
    }

    public String getStoryName() {
        return storyName;
    }

    public void setStoryName(String storyName) {
        this.storyName = storyName;
    }

    public BigDecimal getTotalDuration() {
        return totalDuration.setScale(1, BigDecimal.ROUND_HALF_EVEN);
    }

    public void setTotalDuration(BigDecimal totalDuration) {
        this.totalDuration = totalDuration;
    }

    public void setTotalDuration(double totalDuration) {
        this.totalDuration = this.totalDuration.add(new BigDecimal(totalDuration));
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getIterationId() {
        return iterationId;
    }

    public void setIterationId(int iterationId) {
        this.iterationId = iterationId;
    }

    public int getStoryId() {
        return storyId;
    }

    public void setStoryId(int storyId) {
        this.storyId = storyId;
    }

}