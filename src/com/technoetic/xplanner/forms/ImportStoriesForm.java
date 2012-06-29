/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: Mar 31, 2005
 * Time: 8:55:31 PM
 */
package com.technoetic.xplanner.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class ImportStoriesForm
   extends ImportForm
{

   private String worksheetName = null;
   private String estimateColumn = null;
   private String statusColumn = null;
   private String titleColumn = null;
   private String endDateColumn = null;
   private String priorityColumn = null;
   private boolean onlyIncomplete = false;
   private String completedStatus = null;

   private static final String NO_WORKSHEET_NAME_KEY = "import.status.worksheet_name";
   public static final String NO_TITLE_COLUMN_KEY = "import.status.no_title_column";
   public static final String NO_END_DATE_COLUMN_KEY = "import.status.no_end_date_column";
   public static final String NO_PRIORITY_COLUMN_KEY = "import.status.no_priority_column";
   private static final String NO_COMPLETED_STORY_STATUS = "import.status.no_completed_story_status";

   public void reset(ActionMapping mapping, HttpServletRequest request) {
      super.reset(mapping, request);
      onlyIncomplete = false;
   }

   public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
   {
      ActionErrors errors = super.validate(mapping, request);
      if (isSubmitted())
      {
         validate(isNotEmpty(worksheetName), errors, NO_WORKSHEET_NAME_KEY);
         validate(isNotEmpty(titleColumn), errors, NO_TITLE_COLUMN_KEY);
         validate(isNotEmpty(endDateColumn), errors, NO_END_DATE_COLUMN_KEY);
         validate(isNotEmpty(priorityColumn), errors, NO_PRIORITY_COLUMN_KEY);
         validate(onlyIncomplete && isNotEmpty(completedStatus), errors, ImportStoriesForm.NO_COMPLETED_STORY_STATUS);
      }
      return errors;
   }

   private boolean isNotEmpty(String worksheetName) {return StringUtils.isEmpty(worksheetName);}

   private void validate(boolean condition, ActionErrors errors, String key) {
      if (condition)
      {
         errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError(key));
      }
   }

   public String getTitleColumn()
   {
      return titleColumn;
   }

   public void setTitleColumn(String titleColumn)
   {
      this.titleColumn = titleColumn;
   }

   public String getEstimateColumn()
   {
      return estimateColumn;
   }

   public void setEstimateColumn(String estimateColumn)
   {
      this.estimateColumn = estimateColumn;
   }

   public String getEndDateColumn()
   {
      return endDateColumn;
   }

   public void setEndDateColumn(String endDateColumn)
   {
      this.endDateColumn = endDateColumn;
   }

   public String getPriorityColumn()
   {
      return priorityColumn;
   }

   public void setPriorityColumn(String priorityColumn)
   {
      this.priorityColumn = priorityColumn;
   }

   public String getStatusColumn()
   {
      return statusColumn;
   }

   public void setStatusColumn(String statusColumn)
   {
      this.statusColumn = statusColumn;
   }

   public boolean isOnlyIncomplete()
   {
      return onlyIncomplete;
   }

   public void setOnlyIncomplete(boolean onlyIncomplete)
   {
      this.onlyIncomplete = onlyIncomplete;
   }

   public String getCompletedStatus()
   {
      return completedStatus;
   }

   public void setCompletedStatus(String completedStatus)
   {
      this.completedStatus = completedStatus;
   }

   public void setWorksheetName(String worksheetName) {
      this.worksheetName = worksheetName;
   }

   public String getWorksheetName() {
      return worksheetName;
   }
}