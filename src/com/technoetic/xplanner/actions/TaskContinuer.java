package com.technoetic.xplanner.actions;

import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import org.hibernate.HibernateException;

import com.technoetic.xplanner.domain.TaskDisposition;

public class TaskContinuer extends Continuer {
   private TaskDisposition dispositionOfContinuedTasks;


   protected void doContinueObject(DomainObject fromObject, DomainObject toParent, DomainObject toObject)
         throws HibernateException {

      Task fromTask = (Task) fromObject;
      UserStory toStory = (UserStory) toParent;
      Task toTask = (Task) toObject;

      toTask.setCreatedDate(when);
      toTask.setDisposition(TaskDisposition.fromNameKey(getDispositionOfContinuedTasks(toStory).getNameKey()));
      toTask.setAcceptorId(0);
      toTask.setEstimatedHours(fromTask.getRemainingHours());
      toTask.setEstimatedOriginalHoursField(0.0);
      toTask.setTimeEntries(null);
      fromTask.postpone();
   }

  public void setDispositionOfContinuedTasks(TaskDisposition dispositionOfContinuedTasks) {
     this.dispositionOfContinuedTasks = dispositionOfContinuedTasks;
  }

   public TaskDisposition getDispositionOfContinuedTasks(UserStory toStory) throws HibernateException {
      if (dispositionOfContinuedTasks == null) {
         determineTaskDisposition(toStory);
      }
      return dispositionOfContinuedTasks;
   }

   private void determineTaskDisposition(UserStory toStory) throws HibernateException {
      Iteration iteration = (Iteration) metaDataRepository.getParent(toStory);
      dispositionOfContinuedTasks = iteration.isActive() ? TaskDisposition.ADDED : TaskDisposition.CARRIED_OVER;
   }
}
