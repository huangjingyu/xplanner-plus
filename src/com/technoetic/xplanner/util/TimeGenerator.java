package com.technoetic.xplanner.util;

import java.util.Calendar;
import java.util.Date;

/**
 * User: Mateusz Prokopowicz Date: Aug 24, 2004 Time: 4:47:22 PM
 */
public class TimeGenerator {

   int daysOffset = 0;

   public int moveCurrentDay(int days){
      return daysOffset+= days;
   }

   public void reset(){
      daysOffset = 0;
   }

    public Date getCurrentTime(){
       Date now = new Date();
       if (daysOffset != 0){
          return shiftDate(now, Calendar.DATE, daysOffset);
       }
       return now;
    }

    public Date getTodaysMidnight(){
        return getMidnight(getCurrentTime());
    }

    public static Date getMidnight(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }


    public static Date shiftDate(Date date, int code, int value){
        Calendar cal = Calendar.getInstance();
        cal.setTime((Date) date.clone());
        cal.add(code, value);
        return cal.getTime();
    }
}

