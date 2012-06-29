package net.sf.xplanner.domain;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.sf.xplanner.domain.enums.IterationType;

import com.technoetic.xplanner.domain.IterationStatus;
import com.technoetic.xplanner.domain.NoteAttachable;
import com.technoetic.xplanner.domain.StoryDisposition;
import com.technoetic.xplanner.domain.StoryOrderNoChangeComparator;
import com.technoetic.xplanner.domain.TaskDisposition;
import com.technoetic.xplanner.util.CollectionUtils;
import com.technoetic.xplanner.util.CollectionUtils.DoublePropertyFilter;


/**
*    XplannerPlus, agile planning software
*    @author Maksym_Chyrkov. 
*    Copyright (C) 2009  Maksym Chyrkov
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>
* 	 
*/

@Entity
@Table(name = "iteration")
public class Iteration extends NamedObject implements java.io.Serializable, NoteAttachable {
	public static final int STORY_ID_INDEX = 0;
	public static final int ORDER_NO_INDEX = 1;
	private static final long serialVersionUID = 7878797325126050044L;
	private Project project = new Project();
	private Date startDate;
	private Date endDate;
	private short status = IterationStatus.INACTIVE.toInt();
	private Double daysWorked;
	private List<UserStory> userStories = new ArrayList<UserStory>();
	private double estimatedHours;
	public Iteration() {
	}

	@ManyToOne
	@JoinColumn(name="project_id")
	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "start_date", length = 10)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "end_date", length = 10)
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "status")
	public short getStatus() {
		return this.status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	@Column(name = "days_worked", precision = 22, scale = 0)
	public Double getDaysWorked() {
		return this.daysWorked;
	}

	public void setDaysWorked(Double daysWorked) {
		this.daysWorked = daysWorked;
	}
	
	@Transient
	public double getEstimatedHours() {
	      if (estimatedHours != 0) {return estimatedHours;}

	      estimatedHours = getTaskCurrentEstimatedHours();
	      return estimatedHours;
	   }

	@OneToMany(mappedBy="iteration", cascade=CascadeType.REMOVE)
	public List<UserStory> getUserStories() {
		return userStories;
	}
	
	@Transient
	public double getAdjustedEstimatedHours() {
		return getSumOfStoryProperty(UserStory.TASK_BASED_ADJUSTED_ESTIMATED_HOURS);
	}

	@Transient
	public double getRemainingHours() {
		return getTaskRemainingHours();
	}
	@Transient
	public double getTaskRemainingHours() {
		return getSumOfStoryProperty(UserStory.TASK_BASED_REMAINING_HOURS);
	}
	@Transient
	@Deprecated
	private double getSumOfStoryProperty(String name) {
	      return CollectionUtils.sum(getUserStories(), new DoublePropertyFilter(name));
	   }
	@Transient
	   public boolean isActive() {
		      return IterationStatus.ACTIVE.toInt() == getStatus();
		   }
	@Transient
	   @Deprecated
	   public boolean isCurrent() {
		      return startDate.getTime() <= System.currentTimeMillis()
		             && endDate.getTime() > System.currentTimeMillis();
		   }
	@Transient
	   @Deprecated
	   public boolean isFuture() {
		      return startDate.getTime() > System.currentTimeMillis();
		   }

	@Deprecated
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
		   @Transient
		   public double getPostponedHours() {
			      return getSumOfStoryProperty(UserStory.TASK_BASED_POSTPONED_HOURS);
			   }

		public void setIterationStatus(IterationStatus active) {
			setStatus(active.toInt());
		}

		   public void start() {
			      setIterationStatus(IterationStatus.ACTIVE);
			      startStories();
			   }

			   private void startStories() {
			      for (Iterator it = getUserStories().iterator(); it.hasNext();) {
			         UserStory story = (UserStory) it.next();
			         story.start();
			      }
			   }

			public void setUserStories(List<UserStory> stories) {
				userStories=stories;
			}

			public void setStatusKey(String inactiveKey) {
				// TODO Auto-generated method stub
				
			}
			@Transient
			@Deprecated
			  public double getAddedOriginalHours() {
			      return getSumOfStoryProperty(UserStory.TASK_BASED_ADDED_ORIGINAL_HOURS);
			   }

			   /**
			    * @return sum(task.originalEstimate) where task.isComplete=true
			    */
			@Transient
			   public double getCompletedOriginalHours() {
			      return getSumOfStoryProperty(UserStory.TASK_BASED_COMPLETED_ORIGINAL_HOURS);
			   }
			@Transient
			   public double getTaskEstimatedOriginalHours() {
			      return getSumOfStoryProperty(UserStory.TASK_BASED_ESTIMATED_ORIGINAL_HOURS);
			   }
			@Transient
			   public double getOverestimatedOriginalHours() {
			      return getSumOfStoryProperty(UserStory.TASK_BASED_OVERESTIMATED_ORIGINAL_HOURS);
			   }
			@Transient
			   public double getUnderestimatedOriginalHours() {
			      return getSumOfStoryProperty(UserStory.TASK_BASED_UNDERESTIMATED_ORIGINAL_HOURS);
			   }
			@Transient
			   public double getTaskOverestimatedHours() {
			      return getSumOfStoryProperty(UserStory.TASK_BASED_OVERESTIMATED_HOURS);
			   }
			@Transient
			   public double getStoryRemainingHours() {
			      return getSumOfStoryProperty(UserStory.REMAINING_HOURS) - getStoryPostponedHours();
			   }

			@Transient
			   public double getTaskCompletedRemainingHours() {
			      return getSumOfStoryProperty(UserStory.TASK_BASED_COMPLETED_REMAINING_HOURS) - getTaskPostponedHours();
			   }
			@Transient
			   public double getTaskPostponedHours() {
				      return getSumOfStoryProperty(UserStory.TASK_BASED_POSTPONED_HOURS);
				   }
			@Transient
				   public double getStoryPostponedHours() {
				      return getSumOfStoryProperty(UserStory.POSTPONED_STORY_HOURS);
				   }
			@Transient
				   public double getTaskTotalHours() {
				      return getTaskOriginalHours() + getTaskAddedHours() - getTaskPostponedHours();
				   }
			@Transient
				   public double getStoryTotalHours() {
				      return getStoryOriginalHours() + getStoryAddedHours() - getStoryPostponedHours();
				   }
			@Transient
				   public double getStoryAddedHours() {
				      return getSumOfStoryProperty(UserStory.STORY_ESTIMATED_HOURS_IF_STORY_ADDED);
				   }
			@Transient
				   public double getStoryEstimatedHours() {
				      return getSumOfStoryProperty(UserStory.STORY_ESTIMATED_HOURS);
				   }
			@Transient
				   public double getStoryOriginalHours() {
				      return getSumOfStoryProperty(UserStory.STORY_ESTIMATED_ORIGINAL_HOURS);
				   }
			@Transient
				   public double getTaskOriginalHours() {
				      return getSumOfStoryProperty(UserStory.TASK_ESTIMATED_HOURS_IF_ORIGINAL_STORY);
				   }
			@Transient
				   public double getTaskUnderestimatedOriginalHours() {
					      return getSumOfStoryProperty(UserStory.TASK_BASED_UNDERESTIMATED_ORIGINAL_HOURS);
					   }
			@Transient
					   public double getTaskUnderestimatedHours() {
					      return getSumOfStoryProperty(UserStory.TASK_BASED_UNDERESTIMATED_HOURS);
					   }
			@Transient
					   public double getEstimatedHoursOfAddedTasks() {
						      return getSumOfStoryProperty(UserStory.TASK_BASED_ADDED_HOURS);
						   }
			@Transient
						   public double getTaskActualCompletedHours() {
						      return getSumOfStoryProperty(UserStory.TASK_BASED_COMPLETED_HOURS);
						   }
			@Transient
						   public double getStoryCompletedHours() {
						      return getSumOfStoryProperty(UserStory.COMPLETED_HOURS);
						   }
			@Transient
						   public double getTaskCompletedHours() {
						      return getSumOfStoryProperty(UserStory.TASK_BASED_COMPLETED_ORIGINAL_HOURS);
						   }
			@Transient
						   public double getTaskAddedHours() {
							      return getSumOfStoryProperty(UserStory.TASK_ESTIMATED_HOURS_IF_STORY_ADDED);
							   }

			@Transient
						public String getStatusKey() {
					      return IterationStatus.fromInt(getStatus()).getKey();
						}

						   public String getNewTaskDispositionName(UserStory story) {
							      TaskDisposition disposition = getNewTaskDisposition(story);
							      return disposition.getName();
							   }
						   public TaskDisposition getNewTaskDisposition(UserStory story) {
							      TaskDisposition disposition;
//							    if(story.getDisposition().equals(StoryDisposition.ADDED){
//							         disposition = TaskDisposition.DISCOVERED;
//							    }
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
						   @Transient
						   public double getCachedActualHours() {
							      return getSumOfStoryProperty(UserStory.CACHED_TASK_BASED_ACTUAL_HOURS);
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

						   public void close() {
							      setIterationStatus(IterationStatus.INACTIVE);
							   }
						   @Transient
						   public double getTaskCurrentEstimatedHours() {
							      return getSumOfStoryProperty(UserStory.ESTIMATED_HOURS);
							   }
						   @Transient
						   public double getTaskActualHours() {
							      return getSumOfStoryProperty(UserStory.TASK_BASED_ACTUAL_HOURS);
							   }


}
