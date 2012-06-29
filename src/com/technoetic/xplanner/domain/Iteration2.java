package com.technoetic.xplanner.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.UserStory;

import com.technoetic.xplanner.util.CollectionUtils;
import com.technoetic.xplanner.util.CollectionUtils.DoublePropertyFilter;

public class Iteration2 extends DomainObject implements Nameable, NoteAttachable, Describable {
   private int projectId;
   private String name;
   private String description;
   private Short statusShort;
   private Date startDate;
   private Date endDate;
   private List<UserStory> userStories = new ArrayList<UserStory>();
   private double daysWorked;
   private double estimatedHours;
   public static final int STORY_ID_INDEX = 0;
   public static final int ORDER_NO_INDEX = 1;

// --------------------- Interface Describable ---------------------

   public void setDescription(String description) {
      this.description = description;
   }

   public String getDescription() {
      return description;
   }

   public StoryDisposition determineContinuedStoryDisposition() {
      StoryDisposition disposition;
      if (this.isCurrent() && this.isActive()) {
         disposition = StoryDisposition.ADDED;
      } else {
         disposition = StoryDisposition.CARRIED_OVER;
      }
      return disposition;
   }

   public boolean isCurrent() {
      return startDate.getTime() <= System.currentTimeMillis()
             && endDate.getTime() > System.currentTimeMillis();
   }

   public int getProjectId() {
      return projectId;
   }

   public void setProjectId(int projectId) {
      this.projectId = projectId;
   }

// TODO: Once we get a full object model with bi-directional relationship this code must move to the story

   public StoryDisposition determineNewStoryDisposition() {
      StoryDisposition disposition;
      if (this.isActive()) {
         disposition = StoryDisposition.ADDED;
      } else {
         disposition = StoryDisposition.PLANNED;
      }
      return disposition;
   }

   public boolean isActive() {
      return IterationStatus.ACTIVE == getStatus();
   }

   public double getActualHours() {
      return getCachedActualHours();
   }

   public double getCachedActualHours() {
      return getSumOfStoryProperty(UserStory.CACHED_TASK_BASED_ACTUAL_HOURS);
   }

   public double getTaskActualHours() {
      return getSumOfStoryProperty(UserStory.TASK_BASED_ACTUAL_HOURS);
   }

   public double getEstimatedHoursOfAddedTasks() {
      return getSumOfStoryProperty(UserStory.TASK_BASED_ADDED_HOURS);
   }

   public double getTaskAddedHours() {
      return getSumOfStoryProperty(UserStory.TASK_ESTIMATED_HOURS_IF_STORY_ADDED);
   }

   public double getAdjustedEstimatedHours() {
      return getSumOfStoryProperty(UserStory.TASK_BASED_ADJUSTED_ESTIMATED_HOURS);
   }

   public double getTaskActualCompletedHours() {
      return getSumOfStoryProperty(UserStory.TASK_BASED_COMPLETED_HOURS);
   }

   public double getStoryCompletedHours() {
      return getSumOfStoryProperty(UserStory.COMPLETED_HOURS);
   }

   public double getTaskCompletedHours() {
      return getSumOfStoryProperty(UserStory.TASK_BASED_COMPLETED_ORIGINAL_HOURS);
   }

   public double getDaysWorked() {
      return daysWorked;
   }

   public void setDaysWorked(double daysWorked) {
      this.daysWorked = daysWorked;
   }

   public Date getEndDate() {
      return endDate;
   }

   public void setEndDate(Date endDate) {
      this.endDate = endDate;
   }

   public double getEstimatedHours() {
      if (estimatedHours != 0) {return estimatedHours;}

      estimatedHours = getTaskCurrentEstimatedHours();
      return estimatedHours;
   }

   public double getTaskCurrentEstimatedHours() {
      return getSumOfStoryProperty(UserStory.ESTIMATED_HOURS);
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public double getAddedOriginalHours() {
      return getSumOfStoryProperty(UserStory.TASK_BASED_ADDED_ORIGINAL_HOURS);
   }

   /**
    * @return sum(task.originalEstimate) where task.isComplete=true
    */
   public double getCompletedOriginalHours() {
      return getSumOfStoryProperty(UserStory.TASK_BASED_COMPLETED_ORIGINAL_HOURS);
   }

   public double getTaskEstimatedOriginalHours() {
      return getSumOfStoryProperty(UserStory.TASK_BASED_ESTIMATED_ORIGINAL_HOURS);
   }

   public double getOverestimatedOriginalHours() {
      return getSumOfStoryProperty(UserStory.TASK_BASED_OVERESTIMATED_ORIGINAL_HOURS);
   }

   public double getUnderestimatedOriginalHours() {
      return getSumOfStoryProperty(UserStory.TASK_BASED_UNDERESTIMATED_ORIGINAL_HOURS);
   }

   public double getTaskOverestimatedHours() {
      return getSumOfStoryProperty(UserStory.TASK_BASED_OVERESTIMATED_HOURS);
   }

   public double getPostponedHours() {
      return getSumOfStoryProperty(UserStory.TASK_BASED_POSTPONED_HOURS);
   }

   public double getStoryRemainingHours() {
      return getSumOfStoryProperty(UserStory.REMAINING_HOURS) - getStoryPostponedHours();
   }

   public double getRemainingHours() {
      return getTaskRemainingHours();
   }

   public double getTaskRemainingHours() {
      return getSumOfStoryProperty(UserStory.TASK_BASED_REMAINING_HOURS);
   }

   public double getTaskCompletedRemainingHours() {
      return getSumOfStoryProperty(UserStory.TASK_BASED_COMPLETED_REMAINING_HOURS) - getTaskPostponedHours();
   }

   public Date getStartDate() {
      return startDate;
   }

   public void setStartDate(Date startDate) {
      this.startDate = startDate;
   }

   public IterationStatus getStatus() {
      return IterationStatus.fromInt(statusShort);
   }

   public void setStatus(IterationStatus status) {
      this.statusShort = new Integer(status.code).shortValue();
   }

   public String getStatusKey() {
      return getStatus() != null ? getStatus().getKey() : null;
   }

   public double getTaskUnderestimatedOriginalHours() {
      return getSumOfStoryProperty(UserStory.TASK_BASED_UNDERESTIMATED_ORIGINAL_HOURS);
   }

   public double getTaskUnderestimatedHours() {
      return getSumOfStoryProperty(UserStory.TASK_BASED_UNDERESTIMATED_HOURS);
   }

   public List<UserStory> getUserStories() {
      return userStories;
   }

   public void setUserStories(List<UserStory> userStories) {
      this.userStories = userStories;
   }

   public boolean isFuture() {
      return startDate.getTime() > System.currentTimeMillis();
   }

   public void setStatusKey(String status) {
      setStatus(IterationStatus.fromKey(status));
   }

   public void start() {
      setStatus(IterationStatus.ACTIVE);
      startStories();
   }

   private void startStories() {
      for (Iterator it = getUserStories().iterator(); it.hasNext();) {
         UserStory story = (UserStory) it.next();
         story.start();
      }
   }

   public void close() {
      setStatus(IterationStatus.INACTIVE);
   }

   private double getSumOfStoryProperty(String name) {
      return CollectionUtils.sum(getUserStories(), new DoublePropertyFilter(name));
   }

   @Override
public String toString() {
      return "Iteration{" +
             "id='" + getId() + ", " +
             "projectId='" + projectId + ", " +
             "name='" + name + "'" +
             "}";
   }

   public double getTaskPostponedHours() {
      return getSumOfStoryProperty(UserStory.TASK_BASED_POSTPONED_HOURS);
   }

   public double getStoryPostponedHours() {
      return getSumOfStoryProperty(UserStory.POSTPONED_STORY_HOURS);
   }

   public double getTaskTotalHours() {
      return getTaskOriginalHours() + getTaskAddedHours() - getTaskPostponedHours();
   }

   public double getStoryTotalHours() {
      return getStoryOriginalHours() + getStoryAddedHours() - getStoryPostponedHours();
   }

   public double getStoryAddedHours() {
      return getSumOfStoryProperty(UserStory.STORY_ESTIMATED_HOURS_IF_STORY_ADDED);
   }

   public double getStoryEstimatedHours() {
      return getSumOfStoryProperty(UserStory.STORY_ESTIMATED_HOURS);
   }

   public double getStoryOriginalHours() {
      return getSumOfStoryProperty(UserStory.STORY_ESTIMATED_ORIGINAL_HOURS);
   }

   public double getTaskOriginalHours() {
      return getSumOfStoryProperty(UserStory.TASK_ESTIMATED_HOURS_IF_ORIGINAL_STORY);
   }

   public String getNewTaskDispositionName(UserStory story) {
      TaskDisposition disposition = getNewTaskDisposition(story);
      return disposition.getName();
   }

   public TaskDisposition getNewTaskDisposition(UserStory story) {
      TaskDisposition disposition;
//    if(story.getDisposition().equals(StoryDisposition.ADDED){
//         disposition = TaskDisposition.DISCOVERED;
//    }
      if (story.isStarted()) {
         disposition = TaskDisposition.DISCOVERED;
      } else {
         if (isActive()) {
            disposition = TaskDisposition.DISCOVERED;
         } else {
            disposition = TaskDisposition.PLANNED;
         }
      }
      return disposition;
   }

   public void modifyStoryOrder(int[][] storyIdAndNewOrder) {
      Map storiesById = mapStoriesById(getUserStories());
      List orderChanges = getOrderChanges(storyIdAndNewOrder, storiesById);
      reorderStories(orderChanges);
   }

   private Map mapStoriesById(Collection stories) {
      Map storiesById = new TreeMap();
      for (Iterator iterator = stories.iterator(); iterator.hasNext();) {
         UserStory userStory = (UserStory) iterator.next();
         storiesById.put(new Integer(userStory.getId()), userStory);
      }
      return storiesById;
   }

   private void reorderStories(List storiesByOrder) {
      for (int index = 0, newOrderNo = 1; index < storiesByOrder.size(); index++, newOrderNo++) {
         UserStory orderNoChange = (UserStory) storiesByOrder.get(index);
         orderNoChange.setOrderNo(newOrderNo);
      }
   }

   private List getOrderChanges(int[][] storyIdAndNewOrder, Map storiesById) {
      List storiesByOrder = new ArrayList();
      for (int index = 0; index < storyIdAndNewOrder.length; index++) {
         Integer storyId = new Integer(storyIdAndNewOrder[index][STORY_ID_INDEX]);
         int newOrderNo = storyIdAndNewOrder[index][ORDER_NO_INDEX];
         UserStory userStory = (UserStory) storiesById.get(storyId);
         userStory.setOrderNo(newOrderNo);
         storiesByOrder.add(userStory);
      }
      Collections.sort(storiesByOrder, new StoryOrderNoChangeComparator());
      return storiesByOrder;
   }
   
   public Short getStatusShort() {
		return this.statusShort;
	}

	public void setStatusShort(Short status) {
		this.statusShort = status;
	}
}
