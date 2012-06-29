package com.technoetic.xplanner.export;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

public class PdfReportExporter implements Exporter {
   public void initializeHeaders(HttpServletResponse response)
   {
      response.setHeader("Content-type", "application/pdf");
      response.setHeader("Content-disposition", "inline; filename=report.pdf");
   }

   public byte[] export(Session session, Object object) throws ExportException
   {
      InputStream reportStream = null;
      JRDataSource ds = null;
      Map parameters = new HashMap();

      if (object instanceof Iteration) {
         Iteration iteration = (Iteration)object;

         try {
            ds = new IterationDataSource(iteration, session);
         } catch (HibernateException he) {
            throw new ExportException(he);
         }

         reportStream = PdfReportExporter.class.getClassLoader().getResourceAsStream("com/technoetic/xplanner/export/reports/JRIteration.jrxml");
         parameters.put("IterationName", iteration.getName());
         parameters.put("IterationStartDate", iteration.getStartDate());
         parameters.put("IterationEndDate", iteration.getEndDate());
      } else if (object instanceof UserStory) {
         UserStory story = (UserStory)object;

         try {
            ds = new UserStoryDataSource(story, session);
         } catch (HibernateException he) {
            throw new ExportException(he);
         }

         reportStream = PdfReportExporter.class.getClassLoader().getResourceAsStream("com/technoetic/xplanner/export/reports/JRStory.jrxml");
         parameters.put("StoryName", story.getName());
         Person cust = story.getCustomer(); parameters.put("StoryCustomerName", (cust != null) ? cust.getName() : null);
         parameters.put("StoryEstimatedHours", new java.lang.Double(story.getEstimatedHours()));
         parameters.put("StoryDescription", story.getDescription());
      } else if (object instanceof Task) {
         Task task = (Task)object;

         try {
            ds = new TaskDataSource(task, session);
         } catch (HibernateException he) {
            throw new ExportException(he);
         }

         reportStream = PdfReportExporter.class.getClassLoader().getResourceAsStream("com/technoetic/xplanner/export/reports/JRTask.jrxml");
         parameters.put("TaskName", task.getName());
         parameters.put("TaskDescription", task.getDescription());
         double actual = task.getActualHours();
         parameters.put("TaskPercentage", new java.lang.Integer((int)((actual * 100) / (actual + task.getRemainingHours()))));
         parameters.put("TaskDisposition", task.getDispositionName());
         parameters.put("TaskType", task.getType());
         parameters.put("TaskAcceptor", getPersonName(session, new Integer(task.getAcceptorId())));
         parameters.put("TaskEstimate", new java.lang.Double(task.getEstimatedHours()));
         parameters.put("TaskCompleted", new java.lang.Boolean(task.isCompleted()));
      } else if (object instanceof Person) {
         Person person = (Person)object;

         try {
            ds = new PersonDataSource(person, session);
         } catch (HibernateException he) {
            throw new ExportException(he);
         }

         reportStream = PdfReportExporter.class.getClassLoader().getResourceAsStream("com/technoetic/xplanner/export/reports/JRPerson.jrxml");
         parameters.put("PersonName", person.getName());
      }

      if (ds == null) {
         throw new ExportException("Could not instantiate data source");
      }

      if (reportStream == null) {
         throw new ExportException("Could not open compiled report");
      }

      try {
    	 JasperDesign jasperDesign = JasperManager.loadXmlDesign(reportStream);
    	 JasperReport report = JasperManager.compileReport(jasperDesign);
         return JasperManager.runReportToPdf(report, parameters, ds); 
      } catch (JRException jre) {
         throw new ExportException(jre);
      }
   }

   public static String getPersonName(Session session, Integer id) 
   {
      if (id == null || id.intValue() == 0) {
         return null;
      }

      Person person = null;
      try {
         person = (Person)session.load(Person.class, id);
      } catch (HibernateException he) {
         return null;
      }

      if (person == null) { return null; }

      return person.getName();
   }

   private class UserStoryDataSource implements JRDataSource
   {
      private Iterator iterator = null;
      private Task task = null;
      private String acceptor = null;
      private Session session = null;

      public UserStoryDataSource(UserStory story, Session session) throws HibernateException
      {
         super();

         List data = session.find(  "select task"
                                    + " from net.sf.xplanner.domain.Task task"
                                    + " where task.story = ?"
                                    + " order by task.name",
                                    new java.lang.Integer(story.getId()), Hibernate.INTEGER);

         this.session = session;
         if (data != null) { iterator = data.iterator(); }
      }

      public boolean next() throws JRException
      {
         if (iterator == null || !iterator.hasNext()) { return false; }

         task = (Task)iterator.next();
         acceptor = getPersonName(session, new Integer(task.getAcceptorId()));
         return true;
      }

      public Object getFieldValue(JRField field) throws JRException
      {
         String fieldName = field.getName();

         if ("TaskName".equals(fieldName)) {
            return task.getName();
         }

         if ("TaskPercentage".equals(fieldName)) {
            double actual = task.getActualHours();
            return new java.lang.Integer((int)((actual * 100) / (actual + task.getRemainingHours())));
         }

         if ("TaskDisposition".equals(fieldName)) {
            return task.getDispositionName();
         }

         if ("TaskType".equals(fieldName)) {
            return task.getType();
         }

         if ("TaskAcceptor".equals(fieldName)) {
            return acceptor;
         }

         if ("TaskEstimate".equals(fieldName)) {
            return new java.lang.Double(task.getEstimatedHours());
         }

         if ("TaskCompleted".equals(fieldName)) {
            return new java.lang.Boolean(task.isCompleted());
         }

         throw new JRException("Unexpected field name '" + fieldName + "'");
      }
   }

   private class TaskDataSource implements JRDataSource
   {
      private Iterator iterator = null;
      private java.util.Date start = null;
      private Double duration = null;
      private String pair1 = null;
      private String pair2 = null;
      private Session session = null;

      public TaskDataSource(Task task, Session session) throws HibernateException
      {
         super();

         // I don't get this... what is the relationship between
         // last_update, start_time, end_time and report_date, and why
         // do we have a duration that can be non-zero when the
         // end_time is NULL?
         //
         // How can I do a left join on the personXIds to get a null when they haven't been assigned?
         List data = session.find(  "select te.startTime, te.endTime, te.duration, te.person1Id, te.person2Id"
                                    + " from net.sf.xplanner.domain.TimeEntry te"
                                    + " where te.task.id = ?"
                                    + " order by te.startTime",
                                    new java.lang.Integer(task.getId()), Hibernate.INTEGER);
         this.session = session;
         if (data != null) { iterator = data.iterator(); }
      }

      public boolean next() throws JRException
      {
         if (iterator == null || !iterator.hasNext()) { return false; }

         Object[] result = (Object[])iterator.next();

         start = (java.util.Date)result[0];
         duration = (result[1] == null) ? null : (Double)result[2];

         pair1 = getPersonName(session, (Integer)result[3]);
         pair2 = getPersonName(session, (Integer)result[4]);

         return true;
      }

      public Object getFieldValue(JRField field) throws JRException
      {
         String fieldName = field.getName();

         if ("TimeEntryStart".equals(fieldName)) {
             if(start != null) {
                 return start.toString();
             } else {
                 return "";
             }
         }

         if ("TimeEntryDuration".equals(fieldName)) {
            return duration;
         }

         if ("TimeEntryPair1".equals(fieldName)) {
            return pair1;
         }

         if ("TimeEntryPair2".equals(fieldName)) {
            return pair2;
         }

         throw new JRException("Unexpected field name '" + fieldName + "'");
      }
   }

   private class PersonDataSource implements JRDataSource
   {
      private Iterator iterator = null;
      private Task task = null;
      private Session session = null;
      private int userId = 0;
      private boolean currentlyActive = false;
      private String iterationName = null;
      private Date iterationStart = null;
      private Date iterationEnd = null;
      private String storyName = null;
      private Double storyEstimate = null;

      public PersonDataSource(Person person, Session session) throws HibernateException
      {
         super();

         // return user tasks for current iteration
         List data = session.find(  "select task, iteration.name, iteration.startDate, iteration.endDate, story"
                                    + " from net.sf.xplanner.domain.Task task,"
                                    + " story in class net.sf.xplanner.domain.UserStory,"
                                    + " iteration in class net.sf.xplanner.domain.Iteration"
                                    + " where task.story = story.id and story.iteration.id = iteration.id"
                                    + " and ? between iteration.startDate and iteration.endDate"
                                    + " order by iteration.name, story.name, task.name",
                                    new java.util.Date(), Hibernate.DATE);

         this.userId = person.getId();
         this.session = session;
         if (data != null) { iterator = data.iterator(); }
      }

      public boolean next() throws JRException
      {
         if (iterator == null || !iterator.hasNext()) { return false; }

         Object[] result = (Object[])iterator.next();

         task = (Task)result[0];

         currentlyActive = task.isCurrentlyActive(userId);

         // user is not involved with this task
         if (task.getAcceptorId() != userId && !currentlyActive) { return next(); }

         iterationName = (String)result[1];
         iterationStart = (Date)result[2];
         iterationEnd = (Date)result[3];
         UserStory story = (UserStory)result[4];
         storyName = story.getName();
         storyEstimate = new Double(story.getEstimatedHours());

         return true;
      }

      public Object getFieldValue(JRField field) throws JRException
      {
         String fieldName = field.getName();

         if ("IterationName".equals(fieldName)) {
            return iterationName;
         }

         if ("IterationStartDate".equals(fieldName)) {
            return iterationStart;
         }

         if ("IterationEndDate".equals(fieldName)) {
            return iterationEnd;
         }

         if ("StoryName".equals(fieldName)) {
            return storyName;
         }

         if ("StoryEstimatedHours".equals(fieldName)) {
            return storyEstimate;
         }

         if ("TaskName".equals(fieldName)) {
            return task.getName();
         }

         if ("TaskPercentage".equals(fieldName)) {
            double actual = task.getActualHours();
            return new java.lang.Integer((int)((actual * 100) / (actual + task.getRemainingHours())));
         }

         if ("TaskDisposition".equals(fieldName)) {
            return task.getDispositionName();
         }

         if ("TaskType".equals(fieldName)) {
            return task.getType();
         }

         if ("TaskCompleted".equals(fieldName)) {
            return new java.lang.Boolean(task.isCompleted());
         }

         if ("TaskEstimate".equals(fieldName)) {
            return new java.lang.Double(task.getEstimatedHours());
         }

         if ("TaskActive".equals(fieldName)) {
            return new java.lang.Boolean(currentlyActive);
         }

         throw new JRException("Unexpected field name '" + fieldName + "'");
      }
   }
}
