package com.technoetic.xplanner.acceptance.web;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.xplanner.domain.DataSample;

import org.hibernate.HibernateException;

import com.technoetic.xplanner.acceptance.DataSampleTester;
import com.technoetic.xplanner.acceptance.IterationTester;
import com.technoetic.xplanner.charts.DataSamplerImpl;
import com.technoetic.xplanner.domain.IterationStatus;
import com.technoetic.xplanner.util.TimeGenerator;

public class DataSamplingJobTestScript extends AbstractPageTestScript {
   private final String startDateString = tester.dateStringForNDaysAway(0);
   private final String endDateString = tester.dateStringForNDaysAway(14);
   private IterationTester iterationTester = new IterationTester(tester);
   private MailTester mailTester = new MailTester(tester);
   private DataSampleTester dataSampleTester;

   protected void setUp() throws Exception {
      super.setUp();
      setUpTestProject();
      setUpTestIterationAndStory_();
      story.setEstimatedHoursField(1.0);
      commitCloseAndOpenSession();
      tester.login();
      iterationTester.goToDefaultView(iteration);
      mailTester.setUp();
      tester.editProperty(DataSamplerImpl.AUTOMATICALLY_EXTEND_END_DATE_PROP, "false");
      dataSampleTester = new DataSampleTester(iterationTester);
   }

   protected void tearDown() throws Exception {
      dataSampleTester.setAutomaticallyExtendEndDate("false");
      mailTester.tearDown();
      super.tearDown();
      tester.tearDown();
   }

   public void testGenerateNPlusOneDataSamples() throws Exception {
      iterationTester.start(iteration);
      dataSampleTester.moveCurrentDayAndGenerateDataSample(4);
      changeEstimatedHours(2.0);
      dataSampleTester.moveCurrentDayAndGenerateDataSample(10);
      dataSampleTester.moveCurrentDayAndGenerateDataSample(1);
      dataSampleTester.moveCurrentDayAndGenerateDataSample(1);
      dataSampleTester.collectDataSamples(iteration);
      dataSampleTester.assertDataSample("Estimated Hours", tester.dateStringForNDaysAway(5), 1.0);
      dataSampleTester.assertDataSample("Estimated Hours", tester.dateStringForNDaysAway(15), 2.0);
      dataSampleTester.assertDataSample("Estimated Hours", tester.dateStringForNDaysAway(16), 2.0);
      dataSampleTester.assertNoDataSample("Estimated Hours", tester.dateStringForNDaysAway(17));
   }

   public void testGenerateDataSamplesForClosedIteration() throws Exception {
      iterationTester.start(iteration);
      dataSampleTester.moveCurrentDayAndGenerateDataSample(4);
      changeEstimatedHours(2.0);
      dataSampleTester.moveCurrentDayAndGenerateDataSample(9);
      changeEstimatedHours(5.0);
      dataSampleTester.moveCurrentDayAndGenerateDataSample(2);
      iteration.setIterationStatus(IterationStatus.INACTIVE);
      commitCloseAndOpenSession();
      dataSampleTester.moveCurrentDayAndGenerateDataSample(2);
      dataSampleTester.collectDataSamples(iteration, startDateString, tester.dateStringForNDaysAway(18));
      dataSampleTester.assertDataSample("Estimated Hours", tester.dateStringForNDaysAway(5), 1.0);
      dataSampleTester.assertDataSample("Estimated Hours", tester.dateStringForNDaysAway(14), 2.0);
      dataSampleTester.assertDataSample("Estimated Hours", tester.dateStringForNDaysAway(16), 5.0);
      dataSampleTester.assertNoDataSample("Estimated Hours", tester.dateStringForNDaysAway(18));
   }

   public void testDataSamplesForNonClosedIterations() throws Exception {
      iterationTester.start(iteration);
      tester.clickLinkWithText(project.getName());
      tester.assertCellTextForRowWithTextAndColumnKeyEquals("objecttable",
                                                            iteration.getName(),
                                                            "iterations.tableheading.endDate",
                                                            endDateString);
      tester.editProperty(DataSamplerImpl.AUTOMATICALLY_EXTEND_END_DATE_PROP, "true");
      dataSampleTester.moveCurrentDayAndGenerateDataSample(4);
      changeEstimatedHours(2.0);
      dataSampleTester.moveCurrentDayAndGenerateDataSample(11);
      dataSampleTester.moveCurrentDayAndGenerateDataSample(1);
      dataSampleTester.collectDataSamples(iteration, startDateString, tester.dateStringForNDaysAway(20));
      dataSampleTester.assertDataSample("Estimated Hours", tester.dateStringForNDaysAway(5), 1.0);
      dataSampleTester.assertDataSample("Estimated Hours", tester.dateStringForNDaysAway(16), 2.0);
      dataSampleTester.assertDataSample("Estimated Hours", tester.dateStringForNDaysAway(17), 2.0);
   }

   public void testDataSamplesUpdate() throws Exception {
      iterationTester.start(iteration);
      int oneDay = 1;
      generateTestDataSamples(oneDay);
      dataSampleTester.moveCurrentDayAndGenerateDataSample(oneDay);
      dataSampleTester.collectDataSamples(iteration, startDateString, tester.dateStringForNDaysAway(2));
      dataSampleTester.assertDataSample("Estimated Hours", tester.dateStringForNDaysAway(2), 1.0);
      dataSampleTester.assertDataSample("Actual Hours", tester.dateStringForNDaysAway(2), 0.0);
   }

   private void generateTestDataSamples(int offSet) throws HibernateException, SQLException {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(new Date());
      calendar.add(Calendar.DATE, offSet);
      List dataSamples = new ArrayList();
      dataSamples.add(new DataSample(TimeGenerator.getMidnight(calendar.getTime()),
                                     iteration.getId(),
                                     "estimatedHours",
                                     200));
      dataSamples.add(new DataSample(TimeGenerator.getMidnight(calendar.getTime()),
                                     iteration.getId(),
                                     "actualHours",
                                     200));
      saveDataSamples(dataSamples);
   }

   private void saveDataSamples(List samples) throws HibernateException, SQLException {
      Iterator iterator = samples.iterator();
      while (iterator.hasNext()) {
         DataSample sample = (DataSample) iterator.next();
         dbSupport.getSession().save(sample);
      }
      commitCloseAndOpenSession();
   }

   private void changeEstimatedHours(double estimatedHours) throws Exception {
      tester.gotoProjectsPage();
      tester.clickLinkWithText(iteration.getName());
      tester.clickEditLinkInRowWithText(story.getName());
      tester.setFormElement("estimatedHours", "" + estimatedHours);
      tester.submit();
   }
}
