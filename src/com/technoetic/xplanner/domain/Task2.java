package com.technoetic.xplanner.domain;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import net.sf.xplanner.domain.NamedObject;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

// TODO management of original/current estimate should be done through a status
public class Task2 extends NamedObject implements Nameable, NoteAttachable, Describable {
// ------------------------------ FIELDS ------------------------------

   private int acceptorId;
   private int completionFlag;
   private String type;
   private TaskDisposition disposition = TaskDisposition.PLANNED;
   private double estimatedOriginalHours;
   private double estimatedHours;
   private double postponedHours;
   private Date createdDate;
   private UserStory story;
   private Collection timeEntries = new HashSet();
   public static final String ADDED_ORIGINAL_HOURS = getValidProperty("addedOriginalHours");
   public static final String ESTIMATED_ORIGINAL_HOURS = getValidProperty("estimatedOriginalHours");
   public static final String ITERATION_START_ESTIMATED_HOURS = getValidProperty("iterationStartEstimatedHours");

   private static String getValidProperty(String property) { return getValidProperty(Task.class, property); }

// --------------------- GETTER / SETTER METHODS ---------------------

   public int getAcceptorId() {
      return acceptorId;
   }

   public void setAcceptorId(int acceptorId) {
      this.acceptorId = acceptorId;
   }

   public double getActualHours() {
      double actualHours = 0.0;
      if (timeEntries != null && timeEntries.size() > 0) {
         Iterator itr = timeEntries.iterator();
         while (itr.hasNext()) {
            TimeEntry2 entry = (TimeEntry2) itr.next();
            actualHours += entry.getEffort();
         }
      }
      return actualHours;
   }

   public Date getCreatedDate() {
      return createdDate;
   }

   public void setCreatedDate(java.util.Date createdDate) {
      this.createdDate = createdDate;
   }

   public TaskDisposition getDisposition() {
      return disposition;
   }

   public void setDisposition(TaskDisposition disposition) {
      this.disposition = disposition;
   }

   public double getEstimatedHours() {
      return estimatedHours;
   }

   public void setEstimatedHours(double estimatedHours) {
      this.estimatedHours = estimatedHours;
   }

   public TaskStatus getStatus() {
      TaskStatus status;
      if (isCompleted()) {
         status = TaskStatus.COMPLETED;
      } else if (getActualHours() > 0.0) {
         status = TaskStatus.STARTED;
      } else {
         status = TaskStatus.NON_STARTED;
      }
      return status;
   }

   public boolean isCompleted() {
      return completionFlag == 1;
   }

   public UserStory getUserStory() {
      return story;
   }

   // TODO: add management of the inverse relationship tasks
   public void setStory(UserStory story) {
      this.story = story;
   }

   public Collection getTimeEntries() {
      return timeEntries;
   }

   public void setTimeEntries(Collection timeEntries) {
      this.timeEntries = timeEntries;
   }

   public String getType() {
      return type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public void setEstimatedOriginalHours(double estimatedOriginalHours) {
      this.estimatedOriginalHours = estimatedOriginalHours;
   }

// ------------------------ CANONICAL METHODS ------------------------

   public String toString() {
      return "Task(id=" + this.getId() +
             ", userStoryId=" + (this.getUserStory()==null?"null":""+this.getUserStory().getId()) +
             ", name=" + this.getName() +
             ", acceptorId=" + this.getAcceptorId() +
             ", createDate=" + this.getCreatedDate() +
             ", desc=" + this.getDescription() +
             ", title=" + this.getName();
   }

// -------------------------- OTHER METHODS --------------------------

   public String getDispositionName() {
      return disposition != null ? disposition.getName() : null;
   }

   public void setDispositionName(String dispositionName) {
       this.disposition = TaskDisposition.fromName(dispositionName);
    }

   public double getAddedHours() { return isAdded() ? getEstimatedHours() : 0; }

   private boolean isAdded() {return getDisposition().equals(TaskDisposition.ADDED);}

   public double getAdjustedEstimatedHours() {
      if (isCompleted()) {
         return getActualHours();
      } else {
         return Math.max(getEstimatedHours(), getActualHours());
      }
   }

   public double getCompletedHours() {return isCompleted() ? getActualHours() : 0;}

   public double getEstimatedHoursBasedOnActuals() {
      return getActualHours() + getRemainingHours();
   }

   public double getCompletedRemainingHours() {
      return isCompleted() ? 0.0 : getEstimatedOriginalHours();
   }

   public double getRemainingHours() {
      return isCompleted() ? 0.0 :
             Math.max(getEstimatedHours() - getActualHours() - getPostponedHours(), 0.0);
   }

   public double getAddedOriginalHours() { return getDisposition().isOriginal() ? 0 : getEstimatedOriginalHours(); }

   public double getEstimatedOriginalHours() {
      if (isStarted())
         return estimatedOriginalHours;
      else
         return getEstimatedHours();
   }

   public void setEstimatedOriginalHoursField(double estimatedOriginalHours){
      this.estimatedOriginalHours = estimatedOriginalHours;
   }

   public double getEstimatedOriginalHoursField(){
      return estimatedOriginalHours;
   }

   public double getCompletedOriginalHours() {return isCompleted() ? getEstimatedOriginalHours() : 0;}

   public double getOverestimatedOriginalHours() {
      if (isOverestimated(getEstimatedOriginalHours())) {
          return getEstimatedOriginalHours() - getActualHours();
      }
      return 0.0;
   }

   private boolean isOverestimated(double estimatedHours) {return isCompleted() && getActualHours() < estimatedHours;}

   public double getUnderestimatedOriginalHours() {
      return isUnderestimated(getEstimatedOriginalHours()) ? getActualHours() - getEstimatedOriginalHours() : 0.0;
   }

   private boolean isUnderestimated(double estimatedHours) {return getActualHours() > estimatedHours;}

   public double getOverestimatedHours() {
      return isOverestimated(getEstimatedHours()) ? getEstimatedHours() - getActualHours() : 0.0;
   }

  public double getUnderestimatedHours() {
    if (isDiscovered()) {
       double result = isCompleted() ? 0.0 :
                       Math.max(getEstimatedHours() - getActualHours(), 0.0);
       return getActualHours() + result;
    }
    return isUnderestimated(getEstimatedHours()) ? getActualHours() - getEstimatedHours() : 0.0;
  }



   public double getIterationStartEstimatedHours() {
      if (!disposition.isOriginal()) return 0;
      return getEstimatedOriginalHours();
   }

    private boolean isDiscovered() {
        return TaskDisposition.DISCOVERED.equals(getDisposition());
    }

    public boolean isCurrentlyActive(int personId) {
       if (timeEntries != null && timeEntries.size() > 0) {
          Iterator itr = timeEntries.iterator();
          while (itr.hasNext()) {
             TimeEntry2 entry = (TimeEntry2) itr.next();
             if (entry.isCurrentlyActive(personId)) {
                return true;
             }
          }
       }
       return false;
    }

   public void setCompleted(boolean flag) {
      completionFlag = flag ? 1 : 0;
   }

   public double getPostponedHours() {
      return postponedHours;
   }

   public void setPostponedHours(double postponedHours) {
      this.postponedHours = postponedHours;
   }

  public void postponeRemainingHours() {setPostponedHours(getRemainingHours());}

  public void postpone() {
    postponeRemainingHours();
    setCompleted(false);
  }

   public void start() {
      if (!isStarted()){
         setEstimatedOriginalHours(getEstimatedHours());
      }
   }

   public boolean isStarted() {
      return estimatedOriginalHours > 0;
   }
   protected static String getValidProperty(Class beanClass, String property) {
	      BeanInfo beanInfo;
	      try {
	         beanInfo = Introspector.getBeanInfo(beanClass);
	      } catch (IntrospectionException e) {
	         throw new RuntimeException("could not introspect " + beanClass, e);
	      }
	      PropertyDescriptor[] properties = beanInfo.getPropertyDescriptors();
	      boolean found = false;
	      for (int i = 0; i < properties.length; i++) {
	         if (properties[i].getName().equals(property)) {
	            found = true;
	            break;
	         }
	      }
	      if (!found) {
	         throw new RuntimeException("Could not find property " + property + " in " + beanClass);
	      }

	      return property;

	   }
}