package net.sf.xplanner.domain;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.technoetic.xplanner.domain.Describable;
import com.technoetic.xplanner.domain.NoteAttachable;
import com.technoetic.xplanner.domain.TaskDisposition;
import com.technoetic.xplanner.domain.TaskStatus;

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
@Table(name = "task")
public class Task extends NamedObject implements java.io.Serializable, Describable,
		NoteAttachable {
	private static final long serialVersionUID = 6196936706046250433L;
	private String type;
	private int acceptorId;
	private Date createdDate;
	private double estimatedHours;
	private double originalEstimate;
	private boolean completed;
	private char disposition = TaskDisposition.PLANNED.getCode();
	private List<TimeEntry> timeEntries = new ArrayList<TimeEntry>();
	private UserStory userStory;
	private double postponedHours;
	public static final String ADDED_ORIGINAL_HOURS = getValidProperty("addedOriginalHours");
	   public static final String ESTIMATED_ORIGINAL_HOURS = getValidProperty("estimatedOriginalHours");
	   public static final String ITERATION_START_ESTIMATED_HOURS = getValidProperty("iterationStartEstimatedHours");

	   private static String getValidProperty(String property) { return getValidProperty(Task.class, property); }

	
	public Task() {
	}

	@Column(name = "type")
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "acceptor_id")
	public int getAcceptorId() {
		return this.acceptorId;
	}

	public void setAcceptorId(int acceptorId) {
		this.acceptorId = acceptorId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "created_date", length = 10)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "estimated_hours", precision = 22, scale = 0)
	public double getEstimatedHours() {
		return this.estimatedHours;
	}

	public void setEstimatedHours(double estimatedHours) {
		this.estimatedHours = estimatedHours;
	}

	@Column(name = "original_estimate", precision = 22, scale = 0)
	public double getOriginalEstimate() {
		return this.originalEstimate;
	}

	public void setOriginalEstimate(double originalEstimate) {
		this.originalEstimate = originalEstimate;
	}

	@Column(name = "is_complete")
	public boolean getCompleted() {
		return this.completed;
	}

	public void setCompleted(boolean isComplete) {
		this.completed = isComplete;
	}

	@Column(name = "disposition", nullable = false, length = 1)
	public char getDisposition() {
		return this.disposition;
	}

	public void setDisposition(char disposition) {
		this.disposition = disposition;
	}

	@OrderBy("reportDate")
	@OneToMany(mappedBy="task", cascade=CascadeType.REMOVE)
	public List<TimeEntry> getTimeEntries() {
		return timeEntries;
	}

	public void setTimeEntries(List<TimeEntry> timeEntries) {
		this.timeEntries = timeEntries;
	}

	@ManyToOne
	@JoinColumn(name="story_id")
	public UserStory getUserStory() {
		return userStory;
	}

	public void setUserStory(UserStory userStory) {
		this.userStory = userStory;
	}

	@Deprecated
	@Transient
	public double getActualHours() {
		double actualHours = 0.0;
		if (timeEntries != null && timeEntries.size() > 0) {
			for (TimeEntry entry : timeEntries) {
				actualHours += entry.getEffort();
			}
		}
		return actualHours;
	}

	@Deprecated
	@Transient
	public double getEstimatedOriginalHours() {
		if (isStarted()) {
			return getOriginalEstimate();
		}
		return getEstimatedHours();
	}

	@Transient
	public boolean isStarted() {
		return originalEstimate > 0;
	}

	@Transient
	@Deprecated
	public double getIterationStartEstimatedHours() {
		TaskDisposition taskDisposition = TaskDisposition.fromCode(disposition);
		if (!taskDisposition.isOriginal())
			return 0;
		return getEstimatedOriginalHours();
	}
	
	@Transient
	@Deprecated
	public double getAdjustedEstimatedHours() {
		if (isCompleted()) {
			return getActualHours();
		} 
		return Math.max(getEstimatedHours(), getActualHours());
	}

	@Transient
	public boolean isCompleted() {
		return completed;
	}
	
	@Transient
	@Deprecated
	public double getCompletedOriginalHours() {return isCompleted() ? getEstimatedOriginalHours() : 0;}

	@Transient
	@Deprecated
	public double getCompletedHours() {return isCompleted() ? getActualHours() : 0;}

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
	         throw new RuntimeException("Could not find property " + property + " in " + beanClass);
	      }

	      return property;

	   }

	@Transient
	@Deprecated
	public double getRemainingHours() {
	      return isCompleted() ? 0.0 :
	             Math.max(getEstimatedHours() - getActualHours() - getPostponedHours(), 0.0);
	   }
	@Transient
	@Deprecated
	  public double getPostponedHours() {
	      return postponedHours;
	   }

	   public void setPostponedHours(double postponedHours) {
	      this.postponedHours = postponedHours;
	   }
	   
	   @Transient
	   @Deprecated
	   public void postpone() {
		    postponeRemainingHours();
		    setCompleted(false);
		  }
	   
	   @Transient
	   public void postponeRemainingHours() {setPostponedHours(getRemainingHours());}


	public void setDisposition(TaskDisposition taskDisposition) {
		setDisposition(taskDisposition.getCode());
	}

	@Transient
	public String getDispositionName(){
		return TaskDisposition.fromCode(disposition).getName();
	}

	@Deprecated
	public void setEstimatedOriginalHours(double d) {
		setOriginalEstimate(d);
	}

	public void setEstimatedOriginalHoursField(double d) {
		setOriginalEstimate(d);
	}


	  public void start() {
	      if (!isStarted()){
	         setEstimatedOriginalHours(getEstimatedHours());
	      }
	   }

	    public boolean isCurrentlyActive(int personId) {
	        if (timeEntries != null && timeEntries.size() > 0) {
	           Iterator itr = timeEntries.iterator();
	           while (itr.hasNext()) {
	              TimeEntry entry = (TimeEntry) itr.next();
	              if (entry.isCurrentlyActive(personId)) {
	                 return true;
	              }
	           }
	        }
	        return false;
	     }


	    @Transient
		public double getEstimatedOriginalHoursField() {
			return getOriginalEstimate();
		}

		@Transient
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

	@Transient
	public net.sf.xplanner.domain.enums.TaskStatus getNewStatus() {
		if (isCompleted()) {
			return net.sf.xplanner.domain.enums.TaskStatus.COMPLETED;
		} else if (isStarted()) {
			return net.sf.xplanner.domain.enums.TaskStatus.STARTED;
		}
		return net.sf.xplanner.domain.enums.TaskStatus.NON_STARTED;
	}

		@Transient
		   public double getEstimatedHoursBasedOnActuals() {
			      return getActualHours() + getRemainingHours();
			   }


		   @Transient
		   public double getAddedHours() { return isAdded() ? getEstimatedHours() : 0; }
		   @Transient
		   private boolean isAdded() {return getDisposition() == TaskDisposition.ADDED.getCode();}

		   @Transient
		public String getDispositionNameKey() {
			return TaskDisposition.fromCode(disposition).getNameKey();
		}
		   @Transient
	   public double getAddedOriginalHours() { return TaskDisposition.fromCode(getDisposition()).isOriginal() ? 0 : getEstimatedOriginalHours(); }
		 
		@Transient
	   @Deprecated
	   public double getOverestimatedOriginalHours() {
		      if (isOverestimated(getEstimatedOriginalHours())) {
		          return getEstimatedOriginalHours() - getActualHours();
		      }
		      return 0.0;
		   }
	   
	   private boolean isOverestimated(double estimatedHours) {return isCompleted() && getActualHours() < estimatedHours;}
	   
	   @Transient
	   public double getUnderestimatedOriginalHours() {
		      return isUnderestimated(getEstimatedOriginalHours()) ? getActualHours() - getEstimatedOriginalHours() : 0.0;
		   }

		   private boolean isUnderestimated(double estimatedHours) {return getActualHours() > estimatedHours;}

		   @Transient
		   @Deprecated
		   public double getCompletedRemainingHours() {
			      return isCompleted() ? 0.0 : getEstimatedOriginalHours();
			   }
		   
		   @Transient
		   @Deprecated
		   public double getOverestimatedHours() {
			      return isOverestimated(getEstimatedHours()) ? getEstimatedHours() - getActualHours() : 0.0;
			   }
		   
		   @Transient
		   public double getUnderestimatedHours() {
			    if (isDiscovered()) {
			       double result = isCompleted() ? 0.0 :
			                       Math.max(getEstimatedHours() - getActualHours(), 0.0);
			       return getActualHours() + result;
			    }
			    return isUnderestimated(getEstimatedHours()) ? getActualHours() - getEstimatedHours() : 0.0;
			  }
		   @Transient
		   private boolean isDiscovered() {
		        return TaskDisposition.DISCOVERED.getCode() == getDisposition();
		    }


}
