package com.technoetic.xplanner.charts;

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

import org.jfree.data.general.DefaultPieDataset;

import com.technoetic.xplanner.db.IterationStatisticsQuery;

import de.laures.cewolf.DatasetProduceException;
import de.laures.cewolf.DatasetProducer;

/**
 * User: Mateusz Prokopowicz
 * Date: Apr 12, 2005
 * Time: 3:25:26 PM
 */
public abstract class XplannerPieChartData implements DatasetProducer {
   protected DefaultPieDataset dataSet = new DefaultPieDataset();

   public void setStatistics(IterationStatisticsQuery statistics) {
      Hashtable data = getData(statistics);
      Enumeration enumeration = data.keys();

      while (enumeration.hasMoreElements()) {

         Object group = enumeration.nextElement();
         String groupName = (group !=null)?group.toString():"null";

         double value = ((Double) data.get(groupName)).doubleValue();

         // Note, doubles are rounded to the nearest integer for clarity and ease of display
         Long roundedValue = new Long(Math.round(value));
         if (roundedValue.longValue() != 0) {
            dataSet.setValue(groupName + " (" + roundedValue + ")", roundedValue);
         }
      }
   }

   protected abstract Hashtable getData(IterationStatisticsQuery statistics);

   public Object produceDataset(Map params) throws DatasetProduceException {
      return dataSet;
   }

   public boolean hasExpired(Map params, Date since) {
      return true;
   }

   public String getProducerId() {
      return getClass().getName();
   }
}
