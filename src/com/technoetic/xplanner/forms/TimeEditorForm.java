package com.technoetic.xplanner.forms;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.format.DateTimeFormat;
import com.technoetic.xplanner.util.Interval;
import com.technoetic.xplanner.util.RequestUtils;

public class TimeEditorForm extends AbstractEditorForm {
   public static final int MAX_DESCRIPTION_LENGTH = 500;
   public static final int HOURS = 60*60*1000;
   public static final int HOUR_IN_MS = HOURS;
   public static final String WIZARD_MODE_ATTR = "wizard_mode";

   private ArrayList ids = new ArrayList();
   private ArrayList deletes = new ArrayList();
   private ArrayList startTimes = new ArrayList();
   private ArrayList endTimes = new ArrayList();
   private ArrayList durations = new ArrayList();
   private ArrayList people1 = new ArrayList();
   private ArrayList people2 = new ArrayList();
   private ArrayList reportDates = new ArrayList();
   private ArrayList descriptions = new ArrayList();

   private HashSet previousMementos = new HashSet();
   private String remainingHours;

   private int rowcount;
   public static final String UNPARSABLE_TIME_ERROR_KEY = "edittime.error.unparsable_time";
   public static final String UNPARSABLE_NUMBER_ERROR_KEY = "edittime.error.unparsable_number";
   public static final String MISSING_TIME_ERROR_KEY = "edittime.error.missing_time";
   public static final String MISSING_PERSON_ERROR_KEY = "edittime.error.missing_person";
   public static final String SAME_PEOPLE_ERROR_KEY = "edittime.error.same_people";
   public static final String NEGATIVE_INTERVAL_ERROR_KEY = "edittime.error.negative_interval";
   public static final String OVERLAPPING_INTERVAL_ERROR_KEY = "edittime.error.overlapping_interval";
   public static final String BOTH_INTERVAL_AND_DURATION_ERROR_KEY = "edittime.error.both_interval_and_duration";
   public static final String MISSING_REPORT_DATE_ERROR_KEY = "edittime.error.missing_report_date";
   public static final String LONG_DESCRIPTION_ERROR_KEY = "edittime.error.long_description";

   public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
      initConverters(request);

      ActionErrors errors = new ActionErrors();
      previousMementos.clear();

      for (int i = 0; i < rowcount; i++) {
         errors.add(valideRow(i, request));
      }
      return errors;
   }

   private ActionErrors valideRow(int row, HttpServletRequest request) {
      ActionErrors errors = new ActionErrors();
      if (row == rowcount - 1 && isEmpty(row)) return errors;

      int id = 0;
      if (isPresent(getEntryId(row))) {
         id = Integer.parseInt(getEntryId(row));
      }

      if (getDeleted(row) != null && getDeleted(row).equals("true")) {
         return errors;
      }

      String startTimeString = getStartTime(row);
      String endTimeString = getEndTime(row);
      Date startTime = convertToDateTime(startTimeString, UNPARSABLE_TIME_ERROR_KEY, errors);
      Date endTime = convertToDateTime(endTimeString, UNPARSABLE_TIME_ERROR_KEY, errors);
      Date reportDate = convertToDate(getReportDate(row), UNPARSABLE_TIME_ERROR_KEY, errors);

      int person1Id = 0;
      if (isPresent(getPerson1Id(row))) {
         person1Id = Integer.parseInt(getPerson1Id(row));
      }
      int person2Id = 0;
      if (isPresent(getPerson2Id(row))) {
         person2Id = Integer.parseInt(getPerson2Id(row));
      }

      double duration = 0;
      if (isPresent(getDuration(row))) {
         try {
            duration = decimalConverter.parse(getDuration(row));
         } catch (ParseException ex) {
            error(errors, UNPARSABLE_NUMBER_ERROR_KEY);
         }
      }

      // Validation #1
      //   - Start and end must be present in all except the last row
      if (row < getRowcount() - 1 && (startTime == null || endTime == null) && duration == 0) {
         error(errors, MISSING_TIME_ERROR_KEY);
      }

      // Validation #2
      //   - At least one person must be present in all except the last row
      //     unless the last row has no time entry
      if ((id > 0 || startTime != null || endTime != null || duration > 0) &&
          person1Id == 0 && person2Id == 0) {
         error(errors, MISSING_PERSON_ERROR_KEY);
      } else
         // Validation #5
         //  - Different people
         if (startTime != null && person1Id == person2Id) {
            error(errors, SAME_PEOPLE_ERROR_KEY);
         }


      // Validation #3
      //   - End time must be greater than start time
      if (startTime != null && endTime != null && endTime.getTime() <= startTime.getTime()) {
         error(errors, NEGATIVE_INTERVAL_ERROR_KEY);
      } else
      // Validation #4
      //   - no overlapping intervals
      if (isOverlapping(startTime, endTime, duration, person1Id, person2Id)) {
         error(errors, OVERLAPPING_INTERVAL_ERROR_KEY);
      }

      // Validation #6
      //  - End time and Duration
      if (startTime == null && endTime != null && duration != 0.0) {
         error(errors, MISSING_TIME_ERROR_KEY);
      }

      // Validation #7
      //  - Start time and Duration -- calculate end time
      if (startTime != null && endTime != null && duration != 0.0 && row == rowcount-1) {
         // This recovers automatically, no error message
         error(errors, BOTH_INTERVAL_AND_DURATION_ERROR_KEY);
      }

      // Validation #8
      //  - Start time and Duration -- calculate end time
      if (startTime != null && endTime == null && duration != 0.0) {
         // This recovers automatically, no error message
         Calendar calendar = Calendar.getInstance();
         calendar.setTime(startTime);
         calendar.add(Calendar.MILLISECOND, (int) (duration * HOURS));
         setEndTime(row, DateTimeFormat.format(request, calendar.getTime()));
      }

      // Validation #9
      // - Report Date must be present
      if (reportDate == null) {
         error(errors, MISSING_REPORT_DATE_ERROR_KEY);
      }

      if (isPresent(getDescription(row))) {
         if (getDescription(row).length() > MAX_DESCRIPTION_LENGTH) {
            error(errors, LONG_DESCRIPTION_ERROR_KEY);
         }
      }
      return errors;
   }

   public void reset(ActionMapping mapping, HttpServletRequest request) {
      if (!RequestUtils.isAttributeTrue(request, WIZARD_MODE_ATTR)) {
         super.reset(mapping, request);
         ids.clear();
         deletes.clear();
         startTimes.clear();
         endTimes.clear();
         people1.clear();
         people2.clear();
         durations.clear();
         reportDates.clear();
         descriptions.clear();
         rowcount = 0;
      }
   }

   public void setEntryId(int index, String id) {
      ensureSize(ids, index + 1);
      ids.set(index, id);
   }

   public String getEntryId(int index) {
      ensureSize(ids, index + 1);
      return (String) ids.get(index);
   }

   public void setDeleted(int index, String flag) {
      ensureSize(deletes, index + 1);
      deletes.set(index, flag);
   }

   public String getDeleted(int index) {
      ensureSize(deletes, index + 1);
      return (String) deletes.get(index);
   }

   public void setStartTime(int index, String date) {
      ensureSize(startTimes, index + 1);
      startTimes.set(index, date);
   }

   public String getStartTime(int index) {
      ensureSize(startTimes, index + 1);
      return (String) startTimes.get(index);
   }

   public void setEndTime(int index, String date) {
      ensureSize(endTimes, index + 1);
      endTimes.set(index, date);
   }

   public String getEndTime(int index) {
      ensureSize(endTimes, index + 1);
      return (String) endTimes.get(index);
   }

   public void setDuration(int index, String duration) {
      ensureSize(durations, index + 1);
      durations.set(index, duration);
   }

   public String getDuration(int index) {
      ensureSize(durations, index + 1);
      return (String) durations.get(index);
   }

   public void setPerson1Id(int index, String id) {
      ensureSize(people1, index + 1);
      people1.set(index, id);
   }

   public String getPerson1Id(int index) {
      ensureSize(people1, index + 1);
      return (String) people1.get(index);
   }

   public void setPerson2Id(int index, String id) {
      ensureSize(people2, index + 1);
      people2.set(index, id);
   }

   public String getPerson2Id(int index) {
      ensureSize(people2, index + 1);
      return (String) people2.get(index);
   }

   public void setReportDate(int index, String date) {
      ensureSize(reportDates, index + 1);
      reportDates.set(index, date);
   }

   public String getReportDate(int index) {
      ensureSize(reportDates, index + 1);
      return (String) reportDates.get(index);
   }

   public void setDescription(int index, String description) {
      ensureSize(descriptions, index + 1);
      descriptions.set(index, description);
   }

   public String getDescription(int index) {
      ensureSize(descriptions, index + 1);
      return (String) descriptions.get(index);
   }

   public String getRemainingHours() {
      return remainingHours;
   }

   public void setRemainingHours(String remainingHours) {
      this.remainingHours = remainingHours;
   }


   public void setRowcount(int rowcount) {
      this.rowcount = rowcount;
   }

   public int getRowcount() {
      return rowcount;
   }

   public boolean isIntervalReadOnly(int i) {
      return !isDurationReadOnly(i) && !isEmpty(i);
   }

   public boolean isDurationReadOnly(int i) {
      return !isEmpty(i) && (StringUtils.isNotEmpty(getStartTime(i)) || StringUtils.isNotEmpty(getEndTime(i)));
   }

   public boolean isEmpty(int index) {
      return StringUtils.isEmpty(getStartTime(index)) &&
             StringUtils.isEmpty(getEndTime(index)) &&
             StringUtils.isEmpty(getDuration(index));
   }

   private class TimeEntryMemento implements Serializable {
      private int personId1;
      private int personId2;
      private Interval interval;

      public TimeEntryMemento(int personId1, int personId2, Date startTime, Date endTime) {
         this.personId1 = personId1;
         this.personId2 = personId2;
         if (startTime != null && endTime != null) {
            interval = new Interval(startTime.getTime(), endTime.getTime());
         } else if (startTime != null) {
            interval = new Interval(startTime.getTime());
         } else if (endTime != null) {
            interval = new Interval(endTime.getTime());
         }
      }

      public int getPersonId1() {
         return personId1;
      }

      public int getPersonId2() {
         return personId2;
      }

      public Interval getInterval() {
         return interval;
      }

      public boolean overlaps(TimeEntryMemento previousMemento) {
         return interval.overlaps(previousMemento.getInterval()) &&
                (personId1 != 0 &&
                 (personId1 == previousMemento.getPersonId1() || personId1 == previousMemento.getPersonId2())
                 || personId2 != 0 &&
                    (personId2 == previousMemento .getPersonId1() || personId2 == previousMemento .getPersonId2()));
      }
   }

   private boolean isOverlapping(Date startTime, Date endTime, double duration, int personId1, int personId2) {
      if (startTime != null && (endTime != null || duration > 0)) {
         if (endTime == null) {
            endTime = new Date((long) (startTime.getTime() + duration * HOUR_IN_MS));
         }
         TimeEntryMemento memento = new TimeEntryMemento(personId1, personId2, startTime, endTime);
         if (memento.getInterval() != null) {
            Iterator mementoItr = previousMementos.iterator();
            while (mementoItr.hasNext()) {
               TimeEntryMemento previousMemento = (TimeEntryMemento) mementoItr.next();
               if (memento.overlaps(previousMemento)) {
                  return true;
               }
            }
            previousMementos.add(memento);
         }
      }
      return false;
   }
}
