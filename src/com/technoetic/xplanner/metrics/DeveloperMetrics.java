package com.technoetic.xplanner.metrics;

public class DeveloperMetrics {
    private int id;
    private String name;
    private int iterationId;
    private double hours;
    private double pairedHours;
    private double acceptedTaskHours;
    private double acceptedStoryHours;
    private double ownTasksWorkedHours;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setIterationId(int iterationId) {
        this.iterationId = iterationId;
    }

    public int getIterationId() {
        return iterationId;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }

    public double getHours() {
        return hours;
    }

    public void setPairedHours(double pairedHours) {
        this.pairedHours = pairedHours;
    }

    public double getPairedHours() {
        return pairedHours;
    }

    public double getPairedHoursPercentage() {
        if (hours == 0) return 0;
        return (pairedHours * 100) / hours;
    }

    public double getUnpairedHours() {
        return hours - pairedHours;
    }

    public double getAcceptedHours() {
        return getAcceptedStoryHours() + getAcceptedTaskHours();
    }

    public double getAcceptedTaskHours() {
        return acceptedTaskHours;
    }

    public void setAcceptedTaskHours(double acceptedTaskHours) {
        this.acceptedTaskHours = acceptedTaskHours;
    }

    public double getAcceptedStoryHours() {
        return acceptedStoryHours;
    }

    public void setAcceptedStoryHours(double acceptedStoryHours) {
        this.acceptedStoryHours = acceptedStoryHours;
    }

    public double getOwnTaskHours() {
        return ownTasksWorkedHours;
    }

    public void setOwnTasksWorkedHours(double ownTasksWorkedHours) {
        this.ownTasksWorkedHours = ownTasksWorkedHours;
    }

   public double getRemainingTaskHours() {
      return getAcceptedTaskHours() + getAcceptedStoryHours() - getOwnTaskHours();
   }
}
