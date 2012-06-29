/*
 * @(#)IterationDataSource.java
 *
 * Copyright (c) 2003 Sabre Inc. All rights reserved.
 */

package com.technoetic.xplanner.export;

import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

public class IterationDataSource implements JRDataSource
{
   private Iterator iterator = null;
   private Task task = null;
   private UserStory story = null;
   private String acceptor = null;
   private Session session = null;

   public IterationDataSource(Iteration iteration, Session session) throws HibernateException
   {

      List data = session.find(  "select story, task"
                                 + " from " + UserStory.class.getName() + " story"
                                 + " left join story.tasks as task"
                                 + " where story.iteration.id = ? order by story.priority, story.name, task.name",
                                 new Integer(iteration.getId()), Hibernate.INTEGER);
      this.session = session;
      if (data != null) { iterator = data.iterator(); }
   }

   public boolean next() throws JRException
   {
      if (iterator == null || !iterator.hasNext()) { return false; }

      Object[] result = (Object[])iterator.next();

      story = (UserStory)result[0];
      task = (Task)result[1];
      if (task != null)
          acceptor = PdfReportExporter.getPersonName(session, new Integer(task.getAcceptorId()));
      return true;
   }

   public Object getFieldValue(JRField field) throws JRException
   {
       return getFieldValue(field.getName());
   }

    public Object getFieldValue(String fieldName) throws JRException {
        if ("StoryName".equals(fieldName)) {
           return story.getName();
        }

        if ("StoryCustomerName".equals(fieldName)) {
           Person cust = story.getCustomer();
           return (cust != null) ? cust.getName() : "<no customer>";
        }

        if ("StoryEstimatedHours".equals(fieldName)) {
           return new Double(story.getEstimatedHours());
        }

            if ("TaskName".equals(fieldName)) {
                if (task == null)
                    return "No Tasks Defined";
               return task.getName();
            }

            if ("TaskPercentage".equals(fieldName)) {
                if (task == null)
                    return new Integer(0);
               double actual = task.getActualHours();
               return new Integer((int)((actual * 100) / (actual + task.getRemainingHours())));
            }

            if ("TaskDisposition".equals(fieldName)) {
                if (task == null)
                    return "";
               return task.getDispositionName();
            }

            if ("TaskType".equals(fieldName)) {
                if (task == null)
                    return "";
               return task.getType();
            }

            if ("TaskEstimate".equals(fieldName)) {
                if (task == null)
                    return new Double(0.0);
               return new Double(task.getEstimatedHours());
            }

            if ("TaskCompleted".equals(fieldName)) {
                if (task == null)
                    return new Boolean(false);
               return new Boolean(task.isCompleted());
            }

        if ("TaskAcceptor".equals(fieldName)) {
            if (task == null)
                return null;
           return acceptor;
        }

        throw new JRException("Unexpected field name '" + fieldName + "'");
    }
}
