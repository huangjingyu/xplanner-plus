package com.technoetic.xplanner.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.domain.virtual.DailyTimesheetEntry;
import com.technoetic.xplanner.domain.virtual.Timesheet;
import com.technoetic.xplanner.domain.virtual.TimesheetEntry;

// todo These queries should be converted to Hibernate

public class PersonTimesheetQuery {
    private final Logger log = Logger.getLogger(getClass());
    private static String summaryQuery =
            "SELECT person.name as person_name, " +
            "project.id as project_id, " +
            "project.name as project_name, " +
            "iteration.id as iteration_id, " +
            "iteration.name as iteration_name, " +
            "story.id as story_id, " +
            "story.name as story_name, " +
            "Sum(time_entry.duration) AS total_duration " +
            "FROM person, project, iteration, story, task, time_entry " +
            "WHERE project.id = iteration.project_id " +
            "AND iteration.id = story.iteration_id " +
            "AND story.id = task.story_id " +
            "AND task.id = time_entry.task_id " +
            "AND (person.id = time_entry.person1_id OR person.id = time_entry.person2_id) " +
            "AND " +
            "  ((time_entry.report_date >= ? AND time_entry.report_date <= ? AND time_entry.start_time IS NULL) OR " +
            "   (time_entry.start_time >= ? AND time_entry.start_time <= ?)) " +
            "AND person.id = ? " +
            "GROUP BY person.id, person.name, project.id, project.name, iteration.id,  " +
            "iteration.name, story.id, story.name " +
            "ORDER BY person.name, project.name, iteration.name, story.name ";

    private static String dailyQueryByReportDate =
            "SELECT " +
            "time_entry.report_date as report_date, " +
            "Sum(time_entry.duration) AS total_duration " +
            "FROM time_entry " +
            "WHERE ? in (time_entry.person1_id, time_entry.person2_id) " +
            "AND time_entry.start_time IS NULL " +
            "AND time_entry.report_date >= ?  " +
            "AND time_entry.report_date <= ? " +
            "GROUP BY time_entry.report_date " +
            "ORDER BY time_entry.report_date ";

    private static String dailyQueryByStartDate =
            "SELECT " +
            "time_entry.start_time as report_date, " +
            "Sum(time_entry.duration) AS total_duration " +
            "FROM time_entry " +
            "WHERE ? in (time_entry.person1_id, time_entry.person2_id) " +
            "AND time_entry.start_time IS NOT NULL " +
            "AND time_entry.start_time >= ?  " +
            "AND time_entry.start_time <= ? " +
            "GROUP BY time_entry.start_time " +
            "ORDER BY time_entry.start_time ";

    private int personId;
    private java.util.Date endDate = new Date();
    private java.util.Date startDate = new Date();
    private final Session session;

    public PersonTimesheetQuery(Session session) {
        this.session = session;
    }

    public Timesheet getTimesheet() {
        Timesheet timesheet = new Timesheet(this.startDate, this.endDate);
        try {
            try {
                Connection conn = session.connection();
                PreparedStatement stmt = conn.prepareStatement(summaryQuery);
                stmt.setDate(1, new java.sql.Date(this.startDate.getTime()));
                stmt.setDate(2, new java.sql.Date(this.endDate.getTime()));
                stmt.setDate(3, new java.sql.Date(this.startDate.getTime()));
                stmt.setDate(4, new java.sql.Date(this.endDate.getTime()));
                stmt.setInt(5, this.personId);
                ResultSet results = stmt.executeQuery();
                for (boolean isRow = results.next(); isRow; isRow = results.next()) {
                    TimesheetEntry time = new TimesheetEntry();
                    time.setPersonName(results.getString("person_name"));
                    time.setProjectId(results.getInt("project_id"));
                    time.setProjectName(results.getString("project_name"));
                    time.setIterationId(results.getInt("iteration_id"));
                    time.setIterationName(results.getString("iteration_name"));
                    time.setStoryId(results.getInt("story_id"));
                    time.setStoryName(results.getString("story_name"));
                    time.setTotalDuration(results.getDouble("total_duration"));
                    timesheet.addEntry(time);
                }
                stmt.close();

                doDailyQuery(conn, timesheet, dailyQueryByReportDate);
                doDailyQuery(conn, timesheet, dailyQueryByStartDate);
            } catch (Exception ex) {
                log.error("query error", ex);
            } finally {
                session.connection().rollback();
            }
        } catch (Exception ex) {
            log.error("error in PersonTimesheetQuery", ex);
        }
        return timesheet;
    }

    private void doDailyQuery(Connection conn, Timesheet timesheet, String query) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(query);
        try {
            stmt.setInt(1, this.personId);
            stmt.setDate(2, new java.sql.Date(this.startDate.getTime()));
            stmt.setDate(3, new java.sql.Date(this.endDate.getTime()));
            ResultSet results = stmt.executeQuery();
            for (boolean isRow = results.next(); isRow; isRow = results.next()) {
                DailyTimesheetEntry time = new DailyTimesheetEntry();
                time.setEntryDate(results.getDate("report_date"));
                time.setTotalDuration(results.getDouble("total_duration"));
                timesheet.addDailyEntry(time);
            }
        } finally {
            stmt.close();
        }
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getPersonId() {
        return personId;
    }

    public java.util.Date getStartDate() {
        return startDate;
    }

    public void setStartDate(java.util.Date startDate) {
        this.startDate = startDate;
    }

    public java.util.Date getEndDate() {
        return endDate;
    }

    public void setEndDate(java.util.Date endDate) {
        this.endDate = endDate;
    }
}
