package net.sf.xplanner.domain;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.technoetic.xplanner.domain.Describable;
import com.technoetic.xplanner.domain.Feature;
import com.technoetic.xplanner.domain.NoteAttachable;
import com.technoetic.xplanner.domain.StoryDisposition;
import com.technoetic.xplanner.domain.StoryStatus;
import com.technoetic.xplanner.domain.TaskDisposition;
import com.technoetic.xplanner.util.CollectionUtils;
import com.technoetic.xplanner.util.CollectionUtils.DoubleFilter;
import com.technoetic.xplanner.util.CollectionUtils.DoublePropertyFilter;

/**
 * XplannerPlus, agile planning software
 * 
 * @author Maksym_Chyrkov. Copyright (C) 2009 Maksym Chyrkov This program is
 *         free software: you can redistribute it and/or modify it under the
 *         terms of the GNU General Public License as published by the Free
 *         Software Foundation, either version 3 of the License, or (at your
 *         option) any later version.
 * 
 *         This program is distributed in the hope that it will be useful, but
 *         WITHOUT ANY WARRANTY; without even the implied warranty of
 *         MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *         General Public License for more details.
 * 
 *         You should have received a copy of the GNU General Public License
 *         along with this program. If not, see <http://www.gnu.org/licenses/>
 * 
 */

@Entity
@Table(name = "story")
public class UserStory extends NamedObject implements java.io.Serializable,
		NoteAttachable, Describable {
	private static final long serialVersionUID = 5603178238283216187L;
	private Iteration iteration;
	private int trackerId;
	private double estimatedHoursField;
	private int priority;
	private Person customer;
	private char status = StoryStatus.DRAFT.getCode();
	private Double originalEstimatedHours;
	private char dispositionCode = StoryDisposition.PLANNED.getCode();
	private double postponedHours;
	private double iterationStartEstimatedHours;
	private int orderNo;
	private double actualHours;
	private List<Task> tasks = new ArrayList<Task>();
	private int previousOrderNo;

	public static final String ITERATION_START_ESTIMATED_HOURS = getValidProperty("iterationStartEstimatedHours");
	public static final String TASK_BASED_ESTIMATED_ORIGINAL_HOURS = getValidProperty("taskBasedEstimatedOriginalHours");
	public static final String TASK_BASED_COMPLETED_ORIGINAL_HOURS = getValidProperty("taskBasedCompletedOriginalHours");
	public static final String TASK_BASED_ADDED_ORIGINAL_HOURS = getValidProperty("taskBasedAddedOriginalHours");
	// public static final String TASK_BASED_REMAINING_ORIGINAL_HOURS =
	// getValidProperty("taskBasedRemainingOriginalHours");
	// public static final String TASK_BASED_POSTPONED_ORIGINAL_HOURS =
	// getValidProperty("taskBasedPostponedOriginalHours");
	public static final String TASK_BASED_OVERESTIMATED_ORIGINAL_HOURS = getValidProperty("taskBasedOverestimatedOriginalHours");
	public static final String TASK_BASED_UNDERESTIMATED_ORIGINAL_HOURS = getValidProperty("taskBasedUnderestimatedOriginalHours");
	public static final String TASK_BASED_ESTIMATED_HOURS = getValidProperty("estimatedHours");
	public static final String TASK_BASED_ADJUSTED_ESTIMATED_HOURS = getValidProperty("adjustedEstimatedHours");
	public static final String TASK_BASED_COMPLETED_HOURS = getValidProperty("completedTaskHours");
	public static final String CACHED_TASK_BASED_ACTUAL_HOURS = getValidProperty("cachedActualHours");
	public static final String TASK_BASED_ACTUAL_HOURS = getValidProperty("actualHours");
	public static final String TASK_BASED_ADDED_HOURS = getValidProperty("estimatedHoursOfAddedTasks");
	public static final String TASK_BASED_POSTPONED_HOURS = getValidProperty("postponedHours");
	public static final String TASK_BASED_REMAINING_HOURS = getValidProperty("taskBasedRemainingHours");
	public static final String TASK_BASED_COMPLETED_REMAINING_HOURS = getValidProperty("taskBasedCompletedRemainingHours");
	public static final String TASK_BASED_OVERESTIMATED_HOURS = getValidProperty("overestimatedHours");
	public static final String TASK_BASED_UNDERESTIMATED_HOURS = getValidProperty("underestimatedHours");
	public static final String ADJUSTED_ESTIMATED_HOURS = getValidProperty("adjustedEstimatedHours");
	public static final String ESTIMATED_HOURS = getValidProperty("estimatedHoursField");
	public static final String STORY_ESTIMATED_HOURS = getValidProperty("totalHours");
	public static final String REMAINING_HOURS = getValidProperty("remainingHours");
	public static final String COMPLETED_HOURS = getValidProperty("completedHours");
	public static final String TOTAL_HOURS = getValidProperty("totalHours");
	public static final String TASK_BASED_TOTAL_HOURS = getValidProperty("taskBasedTotalHours");
	public static final String STORY_ESTIMATED_ORIGINAL_HOURS = getValidProperty("nonTaskBasedOriginalHours");
	public static final String TASK_ESTIMATED_ORIGINAL_HOURS = getValidProperty("taskEstimatedOriginalHours");
	public static final String TASK_BASED_ORIGINAL_HOURS = getValidProperty("taskBasedOriginalHours");
	public static final String TASK_ESTIMATED_HOURS_IF_STORY_ADDED = getValidProperty("taskEstimatedHoursIfStoryAdded");
	public static final String TASK_ESTIMATED_HOURS_IF_ORIGINAL_STORY = getValidProperty("taskEstimatedHoursIfOriginalStory");
	public static final String STORY_ESTIMATED_HOURS_IF_STORY_ADDED = getValidProperty("storyEstimatedHoursIfStoryAdded");
	public static final String POSTPONED_STORY_HOURS = getValidProperty("postponedStoryHours");

	private static String getValidProperty(String property) {
		return getValidProperty(UserStory.class, property);
	}

	@ManyToOne
	@JoinColumn(name = "iteration_id")
	public Iteration getIteration() {
		return this.iteration;
	}

	public void setIteration(Iteration iteration) {
		this.iteration = iteration;
	}

	@Column(name = "tracker_id")
	public int getTrackerId() {
		return this.trackerId;
	}

	public void setTrackerId(int trackerId) {
		this.trackerId = trackerId;
	}

	@Column(name = "estimated_hours", precision = 22, scale = 0)
	public double getEstimatedHoursField() {
		return this.estimatedHoursField;
	}

	public void setEstimatedHoursField(double estimatedHours) {
		this.estimatedHoursField = estimatedHours;
	}

	@Transient
	public double getEstimatedHours() {
	      if (tasks.size() > 0) return getTaskBasedEstimatedHours();
	      return getEstimatedHoursField();
	   }

	
	@Column(name = "priority")
	public int getPriority() {
		return this.priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	@ManyToOne(cascade=CascadeType.REFRESH, optional=true )
	@JoinColumn(name = "customer_id")
	public Person getCustomer() {
		return customer;
	}

	public void setCustomer(Person person) {
		this.customer = person;
	}

	@Column(name = "status", nullable = false, length = 1)
	public char getStatus() {
		return this.status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	@Column(name = "original_estimated_hours", precision = 22, scale = 0)
	public Double getOriginalEstimatedHours() {
		return this.originalEstimatedHours;
	}

	public void setOriginalEstimatedHours(Double originalEstimatedHours) {
		this.originalEstimatedHours = originalEstimatedHours;
	}

	@Column(name = "disposition", nullable = false, length = 1)
	public char getDispositionCode() {
		return this.dispositionCode;
	}

	@Transient
	public StoryDisposition getDisposition() {
		return StoryDisposition.fromCode(dispositionCode);
	}

	public void setDispositionCode(char disposition) {
		this.dispositionCode = disposition;
	}

	@Column(name = "postponed_hours", precision = 22, scale = 0)
	public double getPostponedHours() {
		return this.postponedHours;
	}

	public void setPostponedHours(double postponedHours) {
		this.postponedHours = postponedHours;
	}

	@Column(name = "it_start_estimated_hours", precision = 22, scale = 0)
	public double getItStartEstimatedHours() {
		return this.iterationStartEstimatedHours;
	}

	public void setItStartEstimatedHours(double itStartEstimatedHours) {
		this.iterationStartEstimatedHours = itStartEstimatedHours;
	}

	@Column(name = "orderNo")
	public int getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(int orderNo) {
      this.previousOrderNo = this.orderNo;
		this.orderNo = orderNo;
	}

	@OneToMany(mappedBy="userStory", cascade=CascadeType.REMOVE)
	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	@Transient
	@Deprecated
	public double getCachedActualHours() {
		if (actualHours == 0) {
			actualHours = getActualHours();
		}
		return actualHours;
	}

	@Transient
	@Deprecated
	public double getActualHours() {
		return CollectionUtils.sum(getTasks(), new DoubleFilter() {
			public double filter(Object o) {
				return ((Task) o).getActualHours();
			}
		});
	}

	@Deprecated
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
			throw new RuntimeException("Could not find property " + property
					+ " in " + beanClass);
		}

		return property;

	}

	@Transient
	@Deprecated
	public double getTaskBasedCompletedOriginalHours() {
		return CollectionUtils.sum(getTasks(), new DoubleFilter() {
			public double filter(Object o) {
				return ((Task) o).getCompletedOriginalHours();
			}
		});
	}
	@Transient
	@Deprecated
	public double getCompletedTaskHours() {
		return CollectionUtils.sum(getTasks(), new DoubleFilter() {
			public double filter(Object o) {
				return ((Task) o).getCompletedHours();
			}
		});
	}
	@Transient
	@Deprecated
	public double getTaskBasedEstimatedOriginalHours() {
		return CollectionUtils.sum(tasks, new DoublePropertyFilter(
				Task.ITERATION_START_ESTIMATED_HOURS));
	}
	@Transient
	@Deprecated
	public double getTaskBasedRemainingHours() {
		if (tasks.size() == 0) {
			return estimatedHoursField;
		}

		double remainingHours = 0;
		boolean isTaskEstimatePresent = false;
		Iterator<Task> itr = tasks.iterator();
		while (itr.hasNext()) {
			Task task = itr.next();
			remainingHours += task.getRemainingHours();
			isTaskEstimatePresent |= (task.getEstimatedHours() > 0);
		}
		return isTaskEstimatePresent ? remainingHours : estimatedHoursField;
	}
	@Transient
	@Deprecated
	public boolean isCompleted() {
		if (tasks.size() > 0) {
			Iterator<Task> itr = tasks.iterator();
			while (itr.hasNext()) {
				Task task = itr.next();
				if (!task.isCompleted()) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	@Transient
	@Deprecated
	public double getAdjustedEstimatedHours() {
		double adjustedEstimatedHours = 0;
		if (!tasks.isEmpty()) {
			Iterator<Task> itr = tasks.iterator();
			while (itr.hasNext()) {
				Task task = itr.next();
				adjustedEstimatedHours += task.getAdjustedEstimatedHours();
			}
			if (adjustedEstimatedHours == 0) {
				adjustedEstimatedHours = estimatedHoursField;
			}
		} else {
			adjustedEstimatedHours = getEstimatedHours();
		}
		return adjustedEstimatedHours;
	}
	@Transient
	   public boolean isStarted() {
		      return originalEstimatedHours != null;
		   }
	@Transient
	   public double getEstimatedOriginalHours() {
		      if (isStarted()) {
		         return originalEstimatedHours;
		      }
			return getTaskBasedEstimatedOriginalHours();
		   }
	@Transient
	   public double getEstimatedOriginalHoursField(){
		   return getOriginalEstimatedHours();
	   }
	
	   @Deprecated
	public void setEstimatedOriginalHours(double double1) {
		setOriginalEstimatedHours(double1);
	}

	   @Deprecated
	public void setDisposition(StoryDisposition added) {
		   setDispositionCode(added.getCode());
	}

	   public void start() {
		      if (!isStarted()) {
		         setEstimatedOriginalHours(new Double(getTaskBasedEstimatedOriginalHours()));
		      }
		      for (Iterator<Task> iterator = tasks.iterator(); iterator.hasNext();) {
		         Task task = iterator.next();
		         task.start();
		      }
		   }

	   //FIXME: implement
	public void setFeatures(List features) {
		// TODO Auto-generated method stub
		
	}

	   public void postponeRemainingHours() {
		      setPostponedHours(getPostponedHours() + getTaskBasedRemainingHours());
		   }

	   @Deprecated
	   public void moveTo(Iteration targetIteration) {
		      setIteration(targetIteration);
		      targetIteration.getUserStories().add(this);

		      if (targetIteration.isActive() && !isStarted()) {
		         start();
		      }

		      if (targetIteration.isActive()) {
		         setDisposition(StoryDisposition.ADDED);
		         setTasksDisposition(TaskDisposition.ADDED);
		      } else {
		         setDisposition(StoryDisposition.PLANNED);
		         setTasksDisposition(TaskDisposition.PLANNED);
		      }
		   }
	   @Deprecated
	   private void setTasksDisposition(TaskDisposition disposition) {
		      Iterator<Task> iterator = tasks.iterator();
		      while (iterator.hasNext()) {
		         Task task = iterator.next();
		         task.setDisposition(disposition);
		      }
		   }

	   @Transient
	   public double getTaskBasedEstimatedHours() {
		      double taskBasedEstimatedHours = 0;
		      Iterator<Task> itr = tasks.iterator();
		      while (itr.hasNext()) {
		         Task task = itr.next();
		         taskBasedEstimatedHours += task.getEstimatedHoursBasedOnActuals();
		      }
		      return taskBasedEstimatedHours;
		   }

	   @Transient
	   public int getPreviousOrderNo() {
		      return previousOrderNo;
		   }
//FIXME: Implement
	   @Transient
	public List<Feature> getFeatures() {
		return null;
	}

	   @Transient
	 public double getEstimatedHoursOfAddedTasks() {
	      return CollectionUtils.sum(getTasks(), new DoubleFilter() {
	         public double filter(Object o) {
	            return ((Task) o).getAddedHours();
	         }
	      });
	   }
	   @Transient
	   public double getIterationStartEstimatedHours() {
		      return iterationStartEstimatedHours;
		   }

	   @Transient
	   public double getTaskBasedAddedOriginalHours() {
		      return CollectionUtils.sum(getTasks(), new DoubleFilter() {
		         public double filter(Object o) {
		            return ((Task) o).getAddedOriginalHours();
		         }
		      });
		   }
	   @Transient
	   public double getTaskBasedOverestimatedOriginalHours() {
		      return CollectionUtils.sum(getTasks(), new DoubleFilter() {
		         public double filter(Object o) {
		            return ((Task) o).getOverestimatedOriginalHours();
		         }
		      });
		   }
	   
	   @Transient
	   public double getTaskBasedUnderestimatedOriginalHours() {
	      return CollectionUtils.sum(getTasks(), new DoubleFilter() {
	         public double filter(Object o) {
	            return ((Task) o).getUnderestimatedOriginalHours();
	         }
	      });
	   }
	   
	   @Transient
	   public double getTaskBasedCompletedRemainingHours() {
		      if (tasks.size() == 0) {
		         return 0.0;
		      }

		      double remainingHours = 0;
		      Iterator<Task> itr = tasks.iterator();
		      while (itr.hasNext()) {
		         Task task = itr.next();
		         remainingHours += task.getCompletedRemainingHours();
		      }
		      return remainingHours;
		   }
	   @Transient
	   public double getOverestimatedHours() {
		      return CollectionUtils.sum(getTasks(), new DoubleFilter() {
		         public double filter(Object o) {
		            return ((Task) o).getOverestimatedHours();
		         }
		      });
		   }
	   @Transient
	   public double getUnderestimatedHours() {
		      return CollectionUtils.sum(getTasks(), new DoubleFilter() {
		         public double filter(Object o) {
		            return ((Task) o).getUnderestimatedHours();
		         }
		      });
		   }
	   @Transient
	   public double getTotalHours() {
		      return getEstimatedHoursField();
		   }
	   
	   @Transient
	   @Deprecated
	   public double getRemainingHours() {
		      return isCompleted() ? 0.0 : getEstimatedHoursField();
		   }
	   
	   @Transient
	   public double getCompletedHours() {
		      return isCompleted() ? getEstimatedHoursField() : 0.0;
		   }
	   
	   @Transient
	   @Deprecated
	   public double getTaskBasedTotalHours() {
		      return CollectionUtils.sum(getTasks(), new DoubleFilter() {
		         public double filter(Object o) {
		            return ((Task) o).getEstimatedHours();
		         }
		      });
		   }
	   
	   @Transient
	   @Deprecated
	   public double getNonTaskBasedOriginalHours() {
		      return isAdded() ? 0.0 : getEstimatedHoursField();
		   }
	   
	   @Transient
	   private boolean isAdded() {
		    return StoryDisposition.ADDED==getDisposition();
		  }
	   
	   @Transient
	   @Deprecated
	   public double getTaskBasedOriginalHours() {
		      return isAdded() ? 0.0 : getTaskEstimatedOriginalHours();
		   }
	   @Transient
	   @Deprecated
	   public double getTaskEstimatedOriginalHours() {
		      return getEstimatedOriginalHours();
		   }
	   @Transient
	   @Deprecated
	   public double getTaskEstimatedHoursIfStoryAdded() {
		      if (isAdded()) {
		         double result = getTaskBasedEstimatedOriginalHours();
		         result += getSumOfTaskProperty(Task.ADDED_ORIGINAL_HOURS);
		         return result;
		      }
			return 0.0;
		   }
	   @Transient
	   public double getTaskEstimatedHoursIfOriginalStory() {
		      return isAdded() ? 0.0 : getTaskEstimatedOriginalHours();
		   }
	   
	   private double getSumOfTaskProperty(String name) {
		      return CollectionUtils.sum(getTasks(), new DoublePropertyFilter(name));
		   }
	   @Transient
	   public double getStoryEstimatedHoursIfStoryAdded() {
		      return isAdded() ? getEstimatedHoursField() : 0.0;
		   }
	   @Transient
	   public double getPostponedStoryHours() {
		      return getPostponedHours() > 0.0 ? getEstimatedHoursField() : 0.0;
		   }
	   
	   @Transient
	   public StoryStatus getStatusEnum() {
			return StoryStatus.fromCode(this.status);
		}
}
