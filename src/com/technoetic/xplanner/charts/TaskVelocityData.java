package com.technoetic.xplanner.charts;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.TimeEntry;

import org.apache.log4j.Category;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import com.technoetic.xplanner.db.IterationStatisticsQuery;

import de.laures.cewolf.DatasetProduceException;
import de.laures.cewolf.DatasetProducer;
import de.laures.cewolf.links.CategoryItemLinkGenerator;

public class TaskVelocityData implements DatasetProducer, CategoryItemLinkGenerator, org.jfree.chart.labels.CategoryToolTipGenerator, Serializable {
    private static final Category log = Category.getInstance("TaskVelocityData");

    private static final int COMPLETED_SERIES = 0;
    private static final int REQUIRED_SERIES = 1;

    private String[] seriesNames = new String[REQUIRED_SERIES + 1];
    private String[] seriesPrefix = new String[REQUIRED_SERIES + 1];
    private SimpleDateFormat majorFormatter = null;

    private Integer[][] values = null;
    private DefaultCategoryDataset dataSet = null;

    private static final SimpleDateFormat keyFormatter = new SimpleDateFormat("yyyy.MM.dd");

    // Utility methods ======================================================

    /**
     * Indicates if the specified day of the week is a working day. Saturday and Sunday are assumed to not be
     * working days and are not shown on the velocity graph.
     *
     * @param dayOfWeek the specified day (from the <code>Calendar</code> interface) to check the status of.
     *
     * @return <code>true</code> if the specified day is a work day, otherwise <code>false</code>.
     */
    private static boolean isWorkingDay(int dayOfWeek) {
        if ((dayOfWeek == Calendar.SATURDAY) || (dayOfWeek == Calendar.SUNDAY)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Indicates if the specified day of the week is a the first day of the working week.
     *
     * @param dayOfWeek the specified day (from the <code>Calendar</code> interface) to check the status of.
     *
     * @return <code>true</code> if the specified day is the first day of the week, otherwise <code>false</code>.
     */
    private static boolean isFirstDayOfWeek(int dayOfWeek) {
        if (dayOfWeek == Calendar.MONDAY) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the number of working days between the sepcifed dates.
     *
     * @param startTime the start of the date range.
     * @param endTime the end of the date range.
     *
     * @return the number of working days between the specified dates.
     */
    private static int getNumberOfWorkingDays(Date startTime, Date endTime) {
        Calendar start = new GregorianCalendar();
        start.setTime(startTime);
        Calendar end = new GregorianCalendar();
        end.setTime(endTime);

        Calendar current = start;
        int numWorkingDays = 0;

        while (current.before(end)) {
            if (isWorkingDay(current.get(Calendar.DAY_OF_WEEK))) {
                numWorkingDays++;
            }

            current.add(Calendar.DATE, 1);
        }

        return numWorkingDays;
    }

    /**
     * Generates a <code>HashMap</code> key from the supplied date.
     *
     * @param date the date for which a key is to be generated.
     *
     * @return the key associated with the specified date.
     */
    private static String buildKeyFromDate(Date date) {
        return keyFormatter.format(date);
    }

    /**
     * Increments the value stored in the map at the specified date.
     *
     * @param dateValues the map which contains the set of existing dates and their associated values.
     * @param dateToChange the date for which the amount is to be increment.
     * @param incrementAmount the amount to increment by.
     */
    private void incrementValueOnDate(HashMap dateValues, Date dateToChange, double incrementAmount) {
        String dateKey = buildKeyFromDate(dateToChange);
        Double currentAmount = (Double)dateValues.get(dateKey);

        if (currentAmount == null) {
            currentAmount = new Double(incrementAmount);
        } else {
            currentAmount = new Double(currentAmount.doubleValue() + incrementAmount);
        }

        dateValues.put(dateKey, currentAmount);
    }

    /**
     * Returns then data at which the specified task was completed. The completed time is assumed to be the last
     * time entry specified for the task rounded to the end of the day.
     *
     * @param task the task to be examined.
     *
     * @return The date at which the task was completed.
     */
    private Date getCompletedDate(Task task) {
        Date completedTime = null;

        Collection timeEntries = task.getTimeEntries();
        if (timeEntries != null) {
            Iterator iter = timeEntries.iterator();

            while (iter.hasNext()) {
                TimeEntry entry = (TimeEntry)iter.next();
                Date endTime = entry.getEndTime();

                // This is a fix for duration-only entries
                // It may not be quite right.
                if (endTime == null && entry.getDuration() > 0) {
                    endTime = entry.getLastUpdateTime();
                }

                if (endTime != null) {
                    if ((completedTime == null) || (endTime.after(completedTime))) {
                        completedTime = endTime;
                    }
                }
            }
        }

        if (completedTime == null) {
            log.error("Asked for completion time of task [" + task.getId() + "] which wasn't marked as completed");
            return null;
        } else {
            return completedTime;
        }
    }

    /**
     * Returns a two dimensional array, for the category and series information, sized to the duration of the
     * iteration. It is assumed that the data will only contain entries for working days.
     *
     * @param startTime the date at which the iteration started.
     * @param endTime the date at which the iteration ended.
     *
     * @return A two dimensional array where the first dimension contains the hour values and the second
     * dimension contains the series.
     */
    private Integer[][] initializeDataArray(Date startTime, Date endTime) {
        return new Integer[seriesNames.length][getNumberOfWorkingDays(startTime, endTime)];
    }

    /**
     * Addes the supplied data values to the specified series of the chart. The series are culuminative.
     * The data supplied to create the series indicates the increment at each date in the series. Note,
     * since the source date is doubles, but the series data can only contains whole numbers, some small
     * rounding errors may occur.
     *
     * @param lineData two dimensional array to be populated with the series point information.
     * @param seriesNum the index of the series in the line data to which the data should be added.
     * @param startDate the date at which the first entry in the chart starts at.
     * @param dataValues expected to contain a set of <code>Double</code> values keys by <code>Dates</code>.
     * For example it may specify the effort (hours) complete on individual dates.
     */
    private void addSeriesData(Integer[][] lineData, int seriesNum, Date startDate, HashMap dataValues) {
        if (dataValues.size() == 0) {
            return;
        }

        // Step through the points of the series and accumulate their values one day at a time
        //
        Calendar now = new GregorianCalendar();
        Calendar currentDate = new GregorianCalendar();
        currentDate.setTime(startDate);
        double cumulativeValue = 0.0;
        int dayIndex = 0;

        while (dayIndex < lineData[seriesNum].length) {
            Double value = (Double)dataValues.get(buildKeyFromDate(currentDate.getTime()));

            if (value != null) {
                cumulativeValue += value.doubleValue();
            }

            if (isWorkingDay(currentDate.get(Calendar.DAY_OF_WEEK))) {
                if (currentDate.getTimeInMillis() < now.getTimeInMillis()) {
                    int pointValue = (int)Math.round(cumulativeValue);
                    lineData[seriesNum][dayIndex] = new Integer(pointValue);
                } else {
                    lineData[seriesNum][dayIndex] = null;
                }
                dayIndex++;
            }

            currentDate.add(Calendar.DATE, 1);
        }
    }

    /**
     * Returns the list of dates that make up the x-axis of the velocity graph. THe first day of each week
     * is represented by the date all other days are represented by their index within the velocity.
     *
     * @param startDate the date at which the first entry in the chart starts at.
     * @param lineData two dimensional array to be populated with the series point information.
     *
     * @return The names to be displayed for date entries on the x-axis.
     */
    private String[] createCategoryNames(Date startDate, Integer[][] lineData) {
        int numDays = lineData[0].length;
        String[] categories = new String[numDays];

        Calendar currentDate = new GregorianCalendar();
        currentDate.setTime(startDate);
        int dayIndex = 0;

        while (dayIndex < numDays) {
            int dayOfWeek = currentDate.get(Calendar.DAY_OF_WEEK);

            if (isWorkingDay(dayOfWeek)) {
                if (isFirstDayOfWeek(dayOfWeek)) {
                    categories[dayIndex] = majorFormatter.format(currentDate.getTime());
                } else {
                    //categories[dayIndex] = Long.toString(dayIndex);
                    categories[dayIndex] = Long.toString(currentDate.get(Calendar.DAY_OF_MONTH));
                }
                dayIndex++;
            }

            currentDate.add(Calendar.DATE, 1);
        }

        return categories;
    }

    // Public methods =======================================================

    public void setStatistics(IterationStatisticsQuery statistics) {
        // Before generating the graph data ensure all the required objects are set
        //
        seriesNames[COMPLETED_SERIES] = statistics.getResourceString("iteration.statistics.velocity.series_completed");
        seriesNames[REQUIRED_SERIES] = statistics.getResourceString("iteration.statistics.velocity.series_required");

        seriesPrefix[COMPLETED_SERIES] = statistics.getResourceString("iteration.statistics.velocity.prefix_completed");
        seriesPrefix[REQUIRED_SERIES] = statistics.getResourceString("iteration.statistics.velocity.prefix_required");

        majorFormatter = new SimpleDateFormat(statistics.getResourceString("format.date"));

        Date velocityStart = statistics.getIteration().getStartDate();
        Date velocityEnd = statistics.getIteration().getEndDate();

        // Build a list of all the dates at which tasks were added to the system. Any
        // dates past the start or end of the iteration are added to the first and last
        // day of the iteration.
        //
        Collection tasks = statistics.getIterationTasks();
        HashMap effortAdded = new HashMap(tasks.size());

        Iterator iter = tasks.iterator();
        while (iter.hasNext()) {
            Task task = (Task)iter.next();
            Date createdDate = task.getCreatedDate();

            if ((createdDate == null) || (createdDate.getTime() == 0) || (createdDate.before(velocityStart))) {
                createdDate = velocityStart;
            } else if (createdDate.after(velocityEnd)) {
                createdDate = velocityEnd;
            }

            incrementValueOnDate(effortAdded, createdDate, task.getEstimatedOriginalHours());
        }

        // Build a list of all the dates at which tasks are completed.
        //
        HashMap effortCompleted = new HashMap(tasks.size());

        iter = tasks.iterator();
        while (iter.hasNext()) {
            Task task = (Task)iter.next();

            if (task.isCompleted()) {
                Date completedDate = getCompletedDate(task);

                // If a task was completed but it didn't have any time entries then assume that
                // it finished when it was created.
                //
                if (completedDate == null) {
                    completedDate = task.getCreatedDate();

                    if ((completedDate == null) || (completedDate.getTime() == 0)) {
                        completedDate = velocityStart;
                    }
                }

                incrementValueOnDate(effortCompleted, completedDate, task.getEstimatedOriginalHours());
            }
        }

        // Now that we know the range of the iteration we can go through each day calculating the
        // hour completed and required on that single day.
        //
        values = initializeDataArray(velocityStart, velocityEnd);
        addSeriesData(values, REQUIRED_SERIES, velocityStart, effortAdded);
        addSeriesData(values, COMPLETED_SERIES, velocityStart, effortCompleted);
        String[] categoryNames = createCategoryNames(velocityStart, values);

        dataSet = new DefaultCategoryDataset();

        for (int seriesNum = 0; seriesNum < values.length; seriesNum++) {
            for (int categoryNum = 0; categoryNum < values[seriesNum].length; categoryNum++) {
                dataSet.addValue(values[seriesNum][categoryNum], seriesNames[seriesNum], categoryNames[categoryNum]);
            }
        }
    }

    // DatasetProducer methods ===============================================

    public Object produceDataset(Map params) throws DatasetProduceException {
        return dataSet;
    }

    public boolean hasExpired(Map params, Date since) {
        return true;
    }

    public String getProducerId() {
        return TaskVelocityData.class.getName();
    }

    // CategoryItemLinkGenerator methods ====================================

    public String generateLink(Object data, int series, Object category) {
        return seriesNames[series];
    }

    // CategoryToolTipGenerator methods =====================================

    public String generateToolTip(CategoryDataset categoryDataset, int series, int category) {
        return seriesPrefix[series] + values[series][category];
    }
}
