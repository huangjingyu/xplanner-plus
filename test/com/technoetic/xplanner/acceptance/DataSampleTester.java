/*
 * Copyright (c) Mateusz Prokopowicz. All Rights Reserved.
 */

package com.technoetic.xplanner.acceptance;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;
import net.sf.xplanner.domain.DataSample;
import net.sf.xplanner.domain.Iteration;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.technoetic.xplanner.acceptance.web.XPlannerWebTester;
import com.technoetic.xplanner.charts.DataSampleData;
import com.technoetic.xplanner.charts.DataSamplerImpl;
import com.technoetic.xplanner.db.hibernate.HibernateHelper;
import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.testing.DateHelper;

public class DataSampleTester {
   private static final Logger LOG = Logger.getLogger(DataSampleTester.class);
   private final IterationTester iterationTester;
   private final XPlannerWebTester tester;
   private DefaultCategoryDataset dataSampleSet;
   private String dataSampleSetIterationId;

   public DataSampleTester(IterationTester iterationTester) {
      this.iterationTester = iterationTester;
      this.tester = iterationTester.tester;
   }

   public void setAutomaticallyExtendEndDate(String value) throws UnsupportedEncodingException {
      tester.editProperty(DataSamplerImpl.AUTOMATICALLY_EXTEND_END_DATE_PROP, value);
   }

   public void assertNoDataSample(String aspect, String date) {
      Double value = (Double) dataSampleSet.getValue(aspect, date);
      if (value == null) {
         String dayOfMonth = date.substring(date.lastIndexOf("-") + 1);
         value = (Double) dataSampleSet.getValue(aspect, dayOfMonth);
      }
      try {
         Assert.assertNull("data sample " + aspect + " in " + date + " exists", value);
      } catch (AssertionFailedError e) {
         throw new AssertionFailedError(e.getMessage() + "\n" + dumpDataSampleSet(dataSampleSet));
      }
   }

   public void setUp() throws Exception {
   }

   public void tearDown() throws Exception {
      HibernateTemplate template = new HibernateTemplate(HibernateHelper.getSessionFactory());
      int deletedCount = template.bulkUpdate("delete from " + DataSample.class.getName());
      LOG.debug("Deleted " + deletedCount + " datasamples");
   }

   public void collectDataSamples(Iteration iteration, String startDate, String endDate) throws Exception{
      dataSampleSetIterationId = ""+iteration.getId();
      iterationTester.assertOnIterationPage();
      ThreadSession.set(tester.getSession());
      DateFormat dateConverter = new SimpleDateFormat(tester.getMessage("format.date"));
      Iteration iterationNew = new Iteration();
      iterationNew.setId(iteration.getId());
      iterationNew.setStartDate(dateConverter.parse(startDate));
      iterationNew.setEndDate(dateConverter.parse(endDate));
      DataSampleData dataSampleData = new DataSampleData();
      dataSampleData.setAspects("estimatedHours,actualHours");
      dataSampleData.setCategories(tester.getMessage("iteration.statistics.progress.series_estimated")
                                   +
                                   "," +
                                   tester.getMessage("iteration.statistics.progress.series_actual"));
      dataSampleData.setIncludeWeekends(true);
      dataSampleData.setIteration(iterationNew);
      dataSampleSet = (DefaultCategoryDataset) dataSampleData.produceDataset(null);
   }

   public void assertDataSample(String aspect, String date, double expectedValue) {
      Double value = (Double) dataSampleSet.getValue(aspect, date);
      if (value == null) {
         int dayOfMonth = Integer.parseInt(date.substring(date.lastIndexOf("-") + 1));
         value = (Double) dataSampleSet.getValue(aspect, String.valueOf(dayOfMonth));
      }
      try {
         Assert.assertEquals("iteration " + dataSampleSetIterationId +
                             ": wrong value for '" + aspect + "' in " + date + " ",
                             new Double(expectedValue), value);
      } catch (AssertionFailedError ex) {
         throw new AssertionFailedError(ex.getMessage() + "\n" + dumpDataSampleSet(dataSampleSet));
      }
   }

   public void collectDataSamples(Iteration iteration)
         throws Exception {
      String dateFormatString = DateHelper.getMessage("format.date");
      DateFormat format = new SimpleDateFormat(dateFormatString);
      collectDataSamples(iteration,
                         format.format(iteration.getStartDate()),
                         format.format(iteration.getEndDate()));
   }

   public String dumpDataSampleSet(DefaultCategoryDataset dataSet) {
      StringBuffer buf = new StringBuffer();
      buf.append("Dataset [")
            .append(dataSet.getColumnCount())
            .append(",")
            .append(dataSet.getRowCount())
            .append("]={\n");
      buf.append(StringUtils.rightPad("", 20));
      for (int j = 0; j < dataSet.getRowCount(); j++) {
         buf.append(",");
         buf.append(StringUtils.rightPad(toString(dataSet.getRowKey(j)), 20));
      }
      for (int i = 0; i < dataSet.getColumnCount(); i++) {
         buf.append("\n");
         buf.append(StringUtils.rightPad(toString(dataSet.getColumnKey(i)), 20));
         for (int j = 0; j < dataSet.getRowCount(); j++) {
            buf.append(",");
            buf.append(StringUtils.rightPad(toString(dataSet.getValue(j, i)), 20));
         }
      }
      buf.append("}\n\n");
      return buf.toString();
   }


   public void moveCurrentDayAndGenerateDataSample(int days) throws Exception {
      tester.moveCurrentDay(days);
      tester.executeTask("/do/edit/dataSample");
   }

   private static String toString(Object value) {
      return value == null ? "" : value.toString();
   }
}

