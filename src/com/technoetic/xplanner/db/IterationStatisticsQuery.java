package com.technoetic.xplanner.db;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Task;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.util.MessageResources;
import org.hibernate.Hibernate;
import org.hibernate.type.Type;

import com.technoetic.xplanner.db.hibernate.ThreadSession;

public class IterationStatisticsQuery {
    private final Logger log = Logger.getLogger(getClass());

    private String query;
    private int iterationId = -1;
    private Collection tasks = null;
    private Iteration iteration = null;

    private Hashtable taskTypeCount = null;
    private Hashtable taskDispositionCount = null;
    private Hashtable taskTypeEstimatedHours = null;
    private Hashtable taskDispositionEstimatedHours = null;
    private Hashtable taskTypeActualHours = null;
    private Hashtable taskDispositionActualHours = null;

    private Locale locale = null;
    private MessageResources resources = null;

    /**
     * Clears any data that this class may be caching.
     */
    private void clearCache() {
        tasks = null;
        iteration = null;

        taskTypeCount = null;
        taskDispositionCount = null;
        taskTypeEstimatedHours = null;
        taskDispositionEstimatedHours = null;
        taskTypeActualHours = null;
        taskDispositionActualHours = null;
    }

    /**
     * Used to set the HTTP request object. This may be required by other classes to access resource strings.
     *
     * @param request the HTTP request object.
     */
    public void setRequest(HttpServletRequest request) {
        try {
            locale = (Locale)request.getSession().getAttribute(Globals.LOCALE_KEY);
        } catch (IllegalStateException e) {	// Invalidated session
            locale = null;
        }
        if (locale == null) {
            locale = Locale.getDefault();
        }

        resources = (MessageResources)request.getAttribute(Globals.MESSAGES_KEY);
    }

    /**
     * Returns the string, stored using the specified key from the resource bundle. Note, this method should
     * only be invoked after <code>setRequest()</code> has been used to specify the HTTP request object.
     *
     * @param key used to idenfity the string to be returned
     * @return the string loaded from the resource bundle (in the request locale).
     */
    public String getResourceString(String key) {
        return resources.getMessage(locale, key);
    }

    /**
     * Specifies the iteration for which data should be gathered.
     *
     * @param iterationId the iteration for which data is to be gathered.
     */
    public synchronized void setIterationId(int iterationId) {
        clearCache();
        this.iterationId = iterationId;
    }

    /**
     * Returns the iteration for this class is gathering data.
     *
     * @return the iteration id for which data is being gathered.
     */
    public synchronized int getIterationId() {
        return iterationId;
    }

    /**
     * Returns the set of tasks which make up the iteration. Note, this method should only be invoked after
     * <code>setIterationId(int)</code> has been used to specify the iteration and <code>setHibernateSession(Session)</code>
     * has been used to set the database session.
     *
     * @return all the tasks which make up the iteration.
     */
    public synchronized java.util.Collection getIterationTasks() {
        if (tasks == null) {
            try {
                try {
                    if (query == null) {
                       //DEBT: externalize the query
                        query = "select distinct task " +
                                " from task in class net.sf.xplanner.domain.Task, " +
                                " iteration in class net.sf.xplanner.domain.Iteration, " +
                                " story in class net.sf.xplanner.domain.UserStory " +
                                " where " +
                                " task.userStory.id = story.id and story.iteration.id = iteration.id and" +
                                " iteration.id = ?";
                    }
                    tasks = ThreadSession.get().find(query,
                                                     new Object[]{new Integer(iterationId)},
                                                     new Type[]{Hibernate.INTEGER});
                } catch (Exception ex) {
                    log.error("query error", ex);
                }
            } catch (Exception ex) {
                log.error("error in iteration query", ex);
            }
        }

        return tasks;
    }


    /**
     * Returns information about the iteration for which data is being gathered.
     * Note, this method should only be invoked after <code>setIterationId(int)</code> has been used to
     * specify the iteration and <code>setHibernateSession(Session)</code> has been used to set the database session.
     *
     * @return information about the iteration.
     */
    public synchronized Iteration getIteration() {
        if (iteration == null) {
            try {
                iteration = (Iteration)ThreadSession.get().load(Iteration.class, new Integer(iterationId));
            } catch (Exception ex) {
                log.error("error loading iteration [" + iterationId + "]", ex);
            }
        }
        return iteration;
    }

    public Hashtable getTaskCountByType() {
        if (taskTypeCount == null) {
            taskTypeCount = new TypeAggregator() {
              @Override
			protected double getValue(Task task) { return 1; }
           }.aggregateByGroup();
        }
        return taskTypeCount;
    }

    public Hashtable getTaskCountByDisposition() {
        if (taskDispositionCount == null) {
           taskDispositionCount = new DispositionAggregator() {
              @Override
			protected double getValue(Task task) { return 1; }
           }.aggregateByGroup();
        }
       return taskDispositionCount;
    }

   public Hashtable getTaskEstimatedHoursByType() {
       if (taskTypeEstimatedHours == null) {
           taskTypeEstimatedHours = new TypeAggregator(true) {
              @Override
			protected double getValue(Task task) { return task.getEstimatedHours(); }
           }.aggregateByGroup();
       }
       return taskTypeEstimatedHours;
   }

    public Hashtable getTaskEstimatedHoursByDisposition() {
        if (taskDispositionEstimatedHours == null) {
            taskDispositionEstimatedHours = new DispositionAggregator(true) {
             @Override
			protected double getValue(Task task) {return task.getEstimatedHours();}
          }.aggregateByGroup();
        }
        return taskDispositionEstimatedHours;
    }

   public Hashtable getTaskActualHoursByType() {
       if (taskTypeActualHours == null) {
           taskTypeActualHours = new TypeAggregator(true) {
            @Override
			protected double getValue(Task task) {return task.getActualHours();}
         }.aggregateByGroup();
       }
       return taskTypeActualHours;
   }

    public Hashtable getTaskActualHoursByDisposition() {
        if (taskDispositionActualHours == null) {
            taskDispositionActualHours = new DispositionAggregator(true) {
             @Override
			protected double getValue(Task task) {return task.getActualHours();}
          }.aggregateByGroup();
        }
        return taskDispositionActualHours;
    }

   abstract class Aggregator {
      protected boolean onlyCompletedTask;
      public Aggregator() {}
      public Aggregator(boolean onlyCompletedTask) {
         this.onlyCompletedTask = onlyCompletedTask;
      }

      public Hashtable aggregateByGroup() {
         Hashtable valuesByGroup = new Hashtable();

         Iterator taskItr = getIterationTasks().iterator();

         while (taskItr.hasNext()) {
            Task task = (Task)taskItr.next();

            if (!apply(task)) continue;

            Double sum = (Double)valuesByGroup.get(getGroup(task));
            if (sum == null) sum = new Double(0);

            Double newSum = new Double(sum.intValue() + getValue(task));
            valuesByGroup.put(getGroup(task), newSum);
         }
         return valuesByGroup;
      }

      protected boolean apply(Task task) { return !onlyCompletedTask || task.isCompleted(); }
      abstract protected double getValue(Task task);
      abstract protected String getGroup(Task task);
   }

   private abstract class TypeAggregator extends Aggregator {

      public TypeAggregator() {}
      public TypeAggregator(boolean onlyCompletedTask) {
         super(onlyCompletedTask);
      }
      @Override
	protected String getGroup(Task task) {return task.getType();}
   }

   private abstract class DispositionAggregator extends Aggregator {

      public DispositionAggregator() {}
      public DispositionAggregator(boolean onlyCompletedTask) {
         super(onlyCompletedTask);
      }
      @Override
	protected String getGroup(Task task) {
         return getResourceString(task.getDispositionNameKey());}
   }
}
