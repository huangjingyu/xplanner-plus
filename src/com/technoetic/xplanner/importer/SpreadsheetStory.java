package com.technoetic.xplanner.importer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SpreadsheetStory
{
   public static final String STATUS_COMPLETED = "C";
   public final static SimpleDateFormat formatter = new SimpleDateFormat("ddMMMyy");
   private String title = "Default title";
   private double estimate;
   // TODO status should be an enum
   private String status = "";
   private boolean complete = false;
   private Date endDate;
   private int priority = 4;

   SpreadsheetStory(String title, String status, double estimate)
   {
      this.title = title;
      this.estimate = estimate;
      setStatus(status);
   }

   public SpreadsheetStory(Date storyEndDate,
                           String title,
                           String status,
                           double estimate, int priority)
   {
      this(title, status, estimate);
      this.endDate = storyEndDate;
      this.priority = priority;
   }

   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (!(o instanceof SpreadsheetStory)) return false;

      final SpreadsheetStory spreadsheetStory = (SpreadsheetStory) o;

      if (status != null ? !status.equals(spreadsheetStory.status) : spreadsheetStory.status != null) return false;
      if (title != null ? !title.equals(spreadsheetStory.title) : spreadsheetStory.title != null) return false;
      if (estimate != spreadsheetStory.estimate) return false;

      return true;
   }

   public int hashCode()
   {
      int result;
      result = (title != null ? title.hashCode() : 0);
      result = 29 * result + (status != null ? status.hashCode() : 0);
      return result;
   }

   public String toString()
   {
      return "Story{" +
         " priority=" + priority +
         ", title='" + title + "'" +
         ", status='" + status + "'" +
         ", estimate=" + estimate +
         ", endDate=" + formatter.format(endDate) +
         "}";
   }

   public String getTitle()
   {
      return title;
   }

   public void setTitle(String title)
   {
      this.title = title;
   }

   public String getStatus()
   {
      return status;
   }

   public void setStatus(String status)
   {
      this.status = status;
      if (status.equals(STATUS_COMPLETED))
      {
         complete = true;
      }
   }

   public boolean isCompleted()
   {
      return complete;
   }

   public Date getEndDate()
   {
      return endDate;
   }

   public double getEstimate()
   {
      return estimate;
   }

   public int getPriority()
   {
      return priority;
   }
}
