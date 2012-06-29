package com.technoetic.xplanner.forms;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class IterationEditorForm extends AbstractEditorForm {
   private String name;
   private String description;
   private Date startDate;
   private Date endDate;
   private double daysWorked;
   private String startDateString;
   private String endDateString;
   private String statusKey;
   private int projectId;

   public String getContainerId() {
      return Integer.toString(getProjectId());
   }

   public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
      initConverters(request);
      ActionErrors errors = new ActionErrors();
      if (isSubmitted()) {
         require(errors, name, "iteration.editor.missing_name");
         require(errors, startDateString, "iteration.editor.bad_start_date");
         require(errors, endDateString, "iteration.editor.bad_end_date");
         requirePositiveInterval(errors);
      }
      return errors;
   }

   public void reset(ActionMapping mapping, HttpServletRequest request) {
      super.reset(mapping, request);
      name = null;
      description = null;
      startDateString = null;
      endDateString = null;
      projectId = 0;
      dateConverter = null;
   }

   private void requirePositiveInterval(ActionErrors errors) {
      if (errors.size() == 0) {
         startDate = convertToDate(startDateString, "iteration.editor.bad_start_date", errors);
         endDate = convertToDate(endDateString, "iteration.editor.bad_end_date", errors);
         if (startDate != null && endDate != null && endDate.compareTo(startDate) <= 0) {
            error(errors, "iteration.editor.nonpositive_interval");
         }
      }
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getStatusKey() {
      return statusKey;
   }

   public void setStatusKey(String statusKey) {
      this.statusKey = statusKey;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getDescription() {
      return description;
   }

   public void setStartDateString(String startDateString) {
      this.startDateString = startDateString;
   }

   public String getStartDateString() {
      return startDateString;
   }

   public void setEndDateString(String endDateString) {
      this.endDateString = endDateString;
   }

   public String getEndDateString() {
      return endDateString;
   }

   public void setProjectId(int projectId) {
      this.projectId = projectId;
   }

   public int getProjectId() {
      return projectId;
   }

   public Date getEndDate() {
      return endDate;
   }

   public void setEndDate(Date endDate) {
      this.endDate = endDate;
      endDateString = toString(endDate);
   }

   public Date getStartDate() {
      return startDate;
   }

   public void setStartDate(Date startDate) {
      this.startDate = startDate;
      startDateString = toString(startDate);
   }

   public double getDaysWorked() {
      return daysWorked;
   }

   public void setDaysWorked(double daysWorked) {
      this.daysWorked = daysWorked;
   }


private String toString(Date date) {
      return (date == null ? "" : dateConverter.format(date));
   }


}
