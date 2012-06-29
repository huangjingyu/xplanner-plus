package com.technoetic.xplanner.db;

import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.classic.Session;
import org.hibernate.type.Type;

import com.technoetic.xplanner.domain.virtual.Timesheet;
import com.technoetic.xplanner.domain.virtual.TimesheetEntry;
import com.technoetic.xplanner.filters.ThreadServletRequest;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;

public class AggregateTimesheetQuery {
    private final Logger log = Logger.getLogger(getClass());
    private static String query;
    private String[] personIds;
    private java.util.Date endDate = new Date();
    private java.util.Date startDate = new Date();
    private static final String IN_CLAUSE = "AND person.id IN (";
    private static final String IN_CLAUSE_REPLACEMENT = "AND 1=1";
    private final Session session;

    public AggregateTimesheetQuery(Session session) {
        this.session = session;
    }

    // todo - review why this is not using the hibernate query language
    // The current implementation will break if the Hibernate mappings change
    public Timesheet getTimesheet() {
        Timesheet timesheet = new Timesheet(this.startDate, this.endDate);
        try {
            try {
                query =
                        "SELECT project.id, project.name, iteration.id, iteration.name,  story.id, story.name, "+
                        "Sum(time_entry.duration) "+
                        "FROM Person as person, Project as project inner join project.iterations as iteration " +
                        "inner join iteration.userStories as story inner join story.tasks as task inner join task.timeEntries as time_entry " +
                        "WHERE (person.id = time_entry.person1Id OR person.id = time_entry.person2Id) " +
                        "AND time_entry.reportDate >= ?  " +
                        "AND time_entry.reportDate <= ? " +
                        IN_CLAUSE_REPLACEMENT + " " +
                        "GROUP BY project.id, project.name, iteration.id,  " +
                        "iteration.name, story.id, story.name " +
                        "ORDER BY project.name, iteration.name, story.name ";


                if (this.personIds != null && this.personIds.length > 0) {
                    // Set the in clause using String Manipulation
                    StringBuffer inClause = new StringBuffer(IN_CLAUSE);
                    for (int i = 0; i < this.personIds.length; i++) {
                        if (i > 0) {
                            inClause.append(",");
                        }
                        inClause.append(this.personIds[i]);
                    }
                    inClause.append(")");
                    query = query.replaceAll(IN_CLAUSE_REPLACEMENT, inClause.toString());
                }
               Iterator iterator = session.iterate(query, new Object[]{this.startDate, this.endDate}, new Type[]{Hibernate.DATE, Hibernate.DATE});
               while (iterator.hasNext())
               {
                  final int remoteUserId = SecurityHelper.getRemoteUserId(ThreadServletRequest.get());
                  Object [] row =  (Object[]) iterator.next();
                  int projectId = ((Integer)row[0]).intValue();
                  String projectName = (String) row[1];
                  int iterationId = ((Integer)row[2]).intValue();
                  String iterationName = (String) row[3];
                  int storyId = ((Integer)row[4]).intValue();
                  String storyName = (String) row[5];
                  double totalDuration = ((Double)row[6]).doubleValue();

                  if (SystemAuthorizer.get().hasPermission(projectId,
                                                           remoteUserId, "system.project", projectId, "read")) {
                                                              TimesheetEntry time = new TimesheetEntry();
                        time.setProjectId(projectId);
                        time.setProjectName(projectName);
                        time.setIterationId(iterationId);
                        time.setIterationName(iterationName);
                        time.setStoryId(storyId);
                        time.setStoryName(storyName);
                        time.setTotalDuration(totalDuration);
                        timesheet.addEntry(time);
                    }
               }
           } catch (Exception ex) {
                log.error("query error", ex);
            }
        } catch (Exception ex) {
            log.error("error in AggregateTimesheetQuery", ex);
        }
        return timesheet;
    }

    public void setPersonIds(String[] personIds) {
        this.personIds = personIds;
    }

    public String[] getPersonId() {
        return personIds;
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
