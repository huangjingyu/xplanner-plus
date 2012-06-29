package com.technoetic.xplanner.metrics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.domain.repository.ObjectRepository;
import com.technoetic.xplanner.domain.repository.RepositoryException;
import com.technoetic.xplanner.security.AuthenticationException;

public class IterationMetrics {
    private final Logger log = Logger.getLogger(getClass());
    protected HashMap developerMetrics = new HashMap();
    private double totalHours;
    private double totalPairedHours;
    private int iterationId;
    private final String iterationName = null;
    private double maxDeveloperHours;
    protected HashMap names = new HashMap();
    public static final int UNASSIGNED_ID = 0;
    public static final String UNASSIGNED_NAME = "Unassigned";
    private Session session;
    private ObjectRepository iterationRepository;

    public void setIterationId(int iterationId) {
        this.iterationId = iterationId;
    }

    public void analyze() {
//DEBT Spring load
        session = ThreadSession.get();
        try {
            try {
                names.clear();
                developerMetrics.clear();
                getNamesMap(session);
                calculateDeveloperMetrics();
                getHoursWorked(session, "iterationHoursWorkedQuery", names);
            } catch (Exception ex) {
                if (session.isConnected()) {
                    session.connection().rollback();
                }
                log.error("error", ex);
            }
        } catch (Exception ex) {
            log.error("error", ex);
        }
    }

    public void calculateDeveloperMetrics() throws HibernateException, AuthenticationException, RepositoryException {
        Iteration iteration = getIterationObject();
        Collection stories = iteration.getUserStories();
        getMetricsData(stories);
    }

    protected Iteration getIterationObject() throws HibernateException,
                                                    AuthenticationException,
                                                    RepositoryException {
        ObjectRepository repository = getIterationRepository();
        return (Iteration) repository.load(getIterationId());
    }


    protected HashMap getMetricsData(Collection stories) {
        for (Iterator iterator = stories.iterator(); iterator.hasNext();) {
            UserStory story = (UserStory) iterator.next();
            Collection tasks = story.getTasks();
            if (tasks.isEmpty()) {
                assignHoursToUser(story.getTrackerId(), story.getEstimatedHours(), true);
            }
            else {
                for (Iterator iterator1 = tasks.iterator(); iterator1.hasNext();) {
                    Task task = (Task) iterator1.next();
                    assignHoursToUser(task.getAcceptorId(), task.getEstimatedHours(), false);
                }
            }
        }
        return developerMetrics;
    }

    protected void assignHoursToUser(int personId, double estimatedHours, boolean isStoryHour) {
        if (estimatedHours == 0) {
            return;
        }
        DeveloperMetrics developerMetrics = getDeveloperMetrics(getName(personId),
                                    personId, iterationId);
        if (isStoryHour) {
            double prevAcceptedStoryHours = developerMetrics.getAcceptedStoryHours();
            developerMetrics.setAcceptedStoryHours(prevAcceptedStoryHours + estimatedHours);
        }
        else {
            double prevAcceptedTaskHours = developerMetrics.getAcceptedTaskHours();
            developerMetrics.setAcceptedTaskHours(prevAcceptedTaskHours + estimatedHours);
        }
    }

    protected void getNamesMap(Session session) throws HibernateException {
        List nameResults = session.getNamedQuery("namesQuery").list();
        Iterator iter = nameResults.iterator();
        while (iter.hasNext()) {
            Object[] result = (Object[]) iter.next();
            names.put(result[1], result[0]);
        }
        addUnassignedName();
    }

    protected void addUnassignedName() {
        names.put(new Integer(UNASSIGNED_ID), UNASSIGNED_NAME);
    }

    protected String getName(int personId) {
        return (String) names.get(new Integer(personId));
    }

    void getHoursWorked(Session session, String hoursQuery, HashMap names)
        throws HibernateException {
        totalHours = 0.0;
        totalPairedHours = 0.0;
        maxDeveloperHours = 0.0;
        List hoursResults = session.getNamedQuery(hoursQuery).setInteger("iterationId", iterationId).list();
        Iterator hoursIterator = hoursResults.iterator();
        while (hoursIterator.hasNext()) {
            Object[] result = (Object[])hoursIterator.next();
            int person1Id = toInt(result[0]);
            int person2Id = toInt(result[1]);
            Date startTime = (Date)result[2];
            Date endTime = (Date)result[3];
            double duration = toDouble(result[4]);
            int trackerId = toInt(result[5]);
            if ((endTime != null && startTime != null) || duration != 0) {
                double hours = duration == 0 ?
                        (endTime.getTime() - startTime.getTime()) / 3600000.0 :
                        duration;
                boolean isPaired = person1Id != 0 && person2Id != 0;
                if (person1Id != 0) {
                    boolean isOwnTask = person1Id == trackerId;
                    updateWorkedHours(iterationId, (String)names.get(result[0]),
                            person1Id, hours, isPaired, isOwnTask);
                    totalHours += hours;
                }
                if (person2Id != 0) {
                    boolean isOwnTask = person2Id == trackerId;
                    updateWorkedHours(iterationId, (String)names.get(result[1]),
                            person2Id, hours, isPaired, isOwnTask);
                    totalHours += hours;
                }
                if (isPaired){
                    totalPairedHours += hours;
                }
            }
        }
    }

    private int toInt(Object object) {
        return ((Integer)object).intValue();
    }

    private double toDouble(Object object) {
        return object != null ? ((Double)object).doubleValue() : 0;
    }

    protected DeveloperMetrics getDeveloperMetrics(String name, int id, int iterationId) {
        DeveloperMetrics dm = (DeveloperMetrics)developerMetrics.get(name);
        if (dm == null) {
            dm = new DeveloperMetrics();
            dm.setId(id);
            dm.setName(name);
            dm.setIterationId(iterationId);
            developerMetrics.put(name, dm);
        }
        return dm;
    }

    private void updateWorkedHours(int iterationId, String name, int id,
                                   double hours, boolean isPaired, boolean isOwnTask) {
        DeveloperMetrics dm = getDeveloperMetrics(name, id, iterationId);
        dm.setHours(dm.getHours() + hours);
        if (isOwnTask)
            dm.setOwnTasksWorkedHours(dm.getOwnTaskHours() + hours);
        if (dm.getHours() > maxDeveloperHours) {
            maxDeveloperHours = dm.getHours();
        }
        if (isPaired) {
            dm.setPairedHours(dm.getPairedHours() + hours);
        }
    }

    public double getTotalHours() {
        return totalHours;
    }

    public double getTotalPairedHours() {
        return totalPairedHours;
    }

    public double getTotalPairedPercentage(){
        double result = (totalPairedHours * 100) / (totalHours - totalPairedHours);
        if (Double.isNaN(result))
            result = 0.0;
        return result;
    }

    public void setTotalHours(double totalHours) {
        this.totalHours = totalHours;
    }

    public void setTotalPairedHours(double totalPairedHours) {
        this.totalPairedHours = totalPairedHours;
    }

    public Collection getDeveloperTotalTime() {
        ArrayList metrics = new ArrayList(developerMetrics.values());
        Collections.sort(metrics,
                new Comparator() {
                    public int compare(Object object1, Object object2) {
                        DeveloperMetrics dm1 = (DeveloperMetrics)object1;
                        DeveloperMetrics dm2 = (DeveloperMetrics)object2;
                        return (dm1.getHours() < dm2.getHours()) ? 1 :
                                (dm1.getHours() == dm2.getHours() ? 0 : -1);
                    }
                }
        );
        return metrics;
    }

    public Collection getDeveloperOwnTasksWorkedTime() {
        ArrayList metrics = new ArrayList(developerMetrics.values());
        Collections.sort(metrics,
                         new Comparator() {
                             public int compare(Object object1, Object object2) {
                                 DeveloperMetrics dm1 = (DeveloperMetrics)object1;
                                 DeveloperMetrics dm2 = (DeveloperMetrics)object2;
                                 return (dm1.getOwnTaskHours() < dm2.getOwnTaskHours()) ? 1 :
                                        (dm1.getOwnTaskHours() == dm2.getOwnTaskHours() ? 0 : -1);
                             }
                         }
        );
        return metrics;
    }

    public double getMaxTotalTime() {
        double maxTotalTime = 0;
        Iterator itr = developerMetrics.values().iterator();
        while (itr.hasNext()) {
            double hours = ((DeveloperMetrics)itr.next()).getHours();
            if (hours > maxTotalTime) {
                maxTotalTime = hours;
            }
        }
        return maxTotalTime;
    }

    public Collection getDeveloperAcceptedTime() {
        ArrayList metrics = new ArrayList(developerMetrics.values());
        Collections.sort(metrics,
                new Comparator() {
                    public int compare(Object object1, Object object2) {
                        DeveloperMetrics dm1 = (DeveloperMetrics)object1;
                        DeveloperMetrics dm2 = (DeveloperMetrics)object2;
                        return (dm1.getAcceptedHours() < dm2.getAcceptedHours()) ? 1 :
                                (dm1.getAcceptedHours() == dm2.getAcceptedHours() ? 0 : -1);
                    }
                }
        );
        return metrics;
    }

    public double getMaxAcceptedTime() {
        double maxAcceptedTime = 0;
        Iterator itr = developerMetrics.values().iterator();
        while (itr.hasNext()) {
            double hours = ((DeveloperMetrics)itr.next()).getAcceptedHours();
            if (hours > maxAcceptedTime) {
                maxAcceptedTime = hours;
            }
        }
        return maxAcceptedTime;
    }

    public int getIterationId() {
        return iterationId;
    }

    public String getIterationName() {
        return iterationName;
    }

    public void analyze(int iterationId) {
        setIterationId(iterationId);
        analyze();
    }

    private ObjectRepository getIterationRepository() {
        return iterationRepository;
    }

    public void setIterationRepository(ObjectRepository iterationRepository) {
        this.iterationRepository = iterationRepository;
    }
}
