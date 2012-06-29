package com.technoetic.xplanner.domain.virtual;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.TreeMap;

public class Timesheet implements Serializable {

    private ArrayList entries = new ArrayList();
    private TreeMap dailyEntries = new TreeMap();
    private String personName;
    private BigDecimal total = new BigDecimal(0.0);
    private Hashtable projectData = new Hashtable();
    private Hashtable iterationData = new Hashtable();
    private Hashtable storyData = new Hashtable();

    public Timesheet() {
    }

    public Timesheet(Date startDate, Date endDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);

        for (; cal.getTimeInMillis() <= endDate.getTime(); cal.add(Calendar.DATE, 1)) {
            dailyEntries.put(cal.getTime(), new DailyTimesheetEntry(cal.getTime(),
                    new BigDecimal(0.0)));
        }
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public Collection getEntries() {
        return entries;
    }

    public Collection getDailyEntries() {
        return dailyEntries.values();
    }

    public void addEntry(TimesheetEntry entry) {
        this.total = this.total.add(entry.getTotalDuration());
        this.personName = entry.getPersonName();
        this.entries.add(entry);
        this.updateGroupedData(entry);
    }

    public void addDailyEntry(DailyTimesheetEntry entry) {
        DailyTimesheetEntry dailyEntry = (DailyTimesheetEntry)
                this.dailyEntries.get(entry.getEntryDate());
        dailyEntry.setTotalDuration(dailyEntry.getTotalDuration().
                add(entry.getTotalDuration()));

        this.dailyEntries.put(dailyEntry.getEntryDate(), dailyEntry);
    }

    public BigDecimal getTotal() {
        return this.total.setScale(1, BigDecimal.ROUND_HALF_EVEN);
    }

    private void updateGroupedData(TimesheetEntry entry) {

        // Project Data
        BigDecimal projectTotal = (BigDecimal)
                this.projectData.get(entry.getProjectName());
        if (projectTotal == null) {
            projectTotal = new BigDecimal(0.0);
        }
        projectTotal = projectTotal.add(entry.getTotalDuration())
                .setScale(1, BigDecimal.ROUND_HALF_EVEN);
        this.projectData.put(entry.getProjectName(), projectTotal);

        // Iteration Data
        BigDecimal iterationTotal = (BigDecimal)
                this.iterationData.get(entry.getIterationName());
        if (iterationTotal == null) {
            iterationTotal = new BigDecimal(0.0);
        }
        iterationTotal = iterationTotal.add(entry.getTotalDuration())
                .setScale(1, BigDecimal.ROUND_HALF_EVEN);
        this.iterationData.put(entry.getIterationName(), iterationTotal);

        // Iteration Data
        BigDecimal storyTotal = (BigDecimal)
                this.storyData.get(entry.getStoryName());
        if (storyTotal == null) {
            storyTotal = new BigDecimal(0.0);
        }
        storyTotal = storyTotal.add(entry.getTotalDuration())
                .setScale(1, BigDecimal.ROUND_HALF_EVEN);
        this.storyData.put(entry.getStoryName(), storyTotal);
    }

    public Hashtable getIterationData() {
        return iterationData;
    }

    public Hashtable getProjectData() {
        return projectData;
    }

    public Hashtable getStoryData() {
        return storyData;
    }
}