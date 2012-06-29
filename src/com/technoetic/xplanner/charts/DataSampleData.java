package com.technoetic.xplanner.charts;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.xplanner.domain.DataSample;
import net.sf.xplanner.domain.Iteration;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.type.Type;
import org.jfree.data.category.DefaultCategoryDataset;

import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.util.TimeGenerator;

import de.laures.cewolf.DatasetProduceException;
import de.laures.cewolf.DatasetProducer;

public class DataSampleData implements DatasetProducer {
   private final Logger log = Logger.getLogger(getClass());
   private Iteration iteration;
   private String aspects;
   private String categories;
   private boolean includeWeekends;
   public static final int DAY = 60 * 60 * 1000;

   public Object produceDataset(Map map) throws DatasetProduceException {
      DefaultCategoryDataset data = new DefaultCategoryDataset();
      String[] aspectArray = aspects.split(",");
      String[] categoryArray = categories.split(",");
      for (int i = 0; i < categoryArray.length; i++) {
         String category = categoryArray[i];
         String aspect = aspectArray[i];
         addData(data, aspect, category);
      }

      return data;
   }

   private void addData(DefaultCategoryDataset data, String aspect, String category) {
      try {
         List samples = ThreadSession.get().find(" from s in " + DataSample.class +
                                                 " where s.id.referenceId = ? and s.id.aspect = ? order by id.sampleTime",
                                                 new Object[]{new Integer(iteration.getId()), aspect},
                                                 new Type[]{Hibernate.INTEGER, Hibernate.STRING});
         log.debug("retrieved " + samples.size() + " samples");

         Calendar endDay = Calendar.getInstance();
         if (samples.size() > 0) {
            DataSample latestDataSample = (DataSample) samples.get(samples.size() - 1);
            endDay.setTime(getLatestDate(latestDataSample.getSampleTime(), TimeGenerator.shiftDate(iteration.getEndDate(),Calendar.DATE,1)));
         } else {
            endDay.setTime(TimeGenerator.shiftDate(iteration.getEndDate(),Calendar.DATE,1));
         }
         Calendar currentDay = getMidnightOnIterationStart();

         while (currentDay.getTimeInMillis() <= endDay.getTimeInMillis()) {
            if (includeWeekends || !isWeekendDay(currentDay)) {
               DataSample dataSample = closestSample(samples, currentDay, 2 * DAY);
               Number value = dataSample != null ? new Double(dataSample.getValue()) : null;
               data.addValue(value, category, formatDay(currentDay));
            }
            currentDay.add(Calendar.DAY_OF_MONTH, 1);
         }
      } catch (Exception e) {
         log.error("error loading data samples", e);
      }
   }

   protected Date getLatestDate(Date date1, Date date2) {
      return date1.getTime() > date2.getTime() ? date1 : date2;
   }

   private Calendar getMidnightOnIterationStart() {
      Calendar currentDay = Calendar.getInstance();
      currentDay.setTime(iteration.getStartDate());
      currentDay.set(Calendar.HOUR_OF_DAY, 0);
      currentDay.set(Calendar.MINUTE, 0);
      currentDay.set(Calendar.SECOND, 0);
      currentDay.set(Calendar.MILLISECOND, 0);
      return currentDay;
   }

   private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

   private String formatDay(Calendar currentDay) {
      if (currentDay.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
         return dateFormat.format(currentDay.getTime());
      } else {
         return Integer.toString(currentDay.get(Calendar.DAY_OF_MONTH));
      }
   }

   private boolean isWeekendDay(Calendar currentDay) {
      return currentDay.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
             currentDay.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
   }

   private DataSample closestSample(List samples, Calendar currentDay, long precision) {
      long now = currentDay.getTimeInMillis();
      long delta = Long.MAX_VALUE;
      DataSample sample = null;
      for (int i = 0; i < samples.size(); i++) {
         DataSample dataSample = (DataSample) samples.get(i);
         long d = Math.abs(now - dataSample.getSampleTime().getTime());
         if (d < precision && d < delta) {
            delta = d;
            sample = dataSample;
         }
      }
      return sample;
   }

   public boolean hasExpired(Map map, Date date) {
      return true;
   }

   public String getProducerId() {
      return Long.toString(System.currentTimeMillis());
   }

   public void setIteration(Iteration iteration) {
      this.iteration = iteration;
   }

   public void setAspects(String aspects) {
      this.aspects = aspects;
   }

   public void setCategories(String categories) {
      this.categories = categories;
   }

   public void setIncludeWeekends(boolean includeWeekends) {
      this.includeWeekends = includeWeekends;
   }
}
