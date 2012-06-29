/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: May 31, 2005
 * Time: 1:42:57 AM
 */
package com.technoetic.xplanner.testing;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class DateHelper {
   public static final String RESOURCE_BUNDLE_NAME = "ResourceBundle";

   static ResourceBundle messages = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME);

   public static String getDateStringDaysFromToday(int daysFromToday)
   {
      String dateFormatString = getMessage("format.date");
      DateFormat format = new SimpleDateFormat(dateFormatString);
      return format.format(getDateDaysFromToday(daysFromToday));
   }

   public static Date getDateDaysFromToday(int daysFromToday) {
      return getDateDaysFromDate(new Date(), daysFromToday);
   }

   public static Date getDateDaysFromDate(Date today, int daysFromDate) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(today);
      calendar.add(Calendar.DATE, daysFromDate);
      return calendar.getTime();
   }

   public static String getDateTimeStringHoursFromNow(int hoursFromNow)
   {
      String dateFormatString = getMessage("format.datetime");
      DateFormat format = new SimpleDateFormat(dateFormatString);
      return format.format(getDateHoursFromNow(hoursFromNow));
   }

   private static Date getDateHoursFromNow(int hoursFromNow) {
      Date now = new Date();
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(now);
      calendar.add(Calendar.HOUR, hoursFromNow);
      return calendar.getTime();
   }

   public static String getMessage(String key) {
      String message;
      try {
         message = messages.getString(key);
      } catch (Exception e) {
          throw new RuntimeException("No message found for key [" + key + "]." );
//FIXME:                  "\nError: " + ExceptionUtility.stackTraceToString(e));
      }
      return message;
   }

   public static Date createDate(int year, int month, int day)
   {
      Calendar cal = Calendar.getInstance();
      cal.set(year, month, day, 0, 0, 0);
      cal.set(Calendar.MILLISECOND, 0);
      return cal.getTime();
   }
}