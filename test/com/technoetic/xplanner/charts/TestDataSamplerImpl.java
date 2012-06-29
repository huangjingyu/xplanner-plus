
/*
 * Copyright (c) Mateusz Prokopowicz. All Rights Reserved.
 */

package com.technoetic.xplanner.charts;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import net.sf.xplanner.dao.DataSampleDao;
import net.sf.xplanner.domain.DataSample;
import net.sf.xplanner.domain.Iteration;

import org.hibernate.HibernateException;
import org.springframework.orm.hibernate3.HibernateOperations;

import com.technoetic.xplanner.AbstractUnitTestCase;
import com.technoetic.xplanner.domain.IterationStatus;
import com.technoetic.xplanner.util.TimeGenerator;

public class TestDataSamplerImpl extends AbstractUnitTestCase {
   DataSamplerImpl dataSampler;
   Iteration iteration;
   TimeGenerator timeGenerator;
   HibernateOperations mockHibernateOperations;
   Iteration activeIteration;
   private Date todayMidnight;
   private Date tomorrowMidnight;
   private Properties properties;
   private TimeGenerator mockTimeGenerator;
   private final List estimatedHoursDataSamples = new ArrayList();
   private final List actualHoursDataSamples = new ArrayList();
   private final List remainingHoursDataSample = new ArrayList();
   private DataSampleDao dataSampleDao;

   @Override
public void tearDown() throws Exception {
      super.tearDown();
   }

   @Override
protected void setUp() throws Exception {
      super.setUp();
      properties = new Properties();
      setAutomaticallyExtendIterationEndDate(false);
      iteration = new Iteration();
      iteration.setId(99);
      timeGenerator = new TimeGenerator();
      mockHibernateOperations = createLocalMock(HibernateOperations.class);
      mockTimeGenerator = createLocalMock(TimeGenerator.class);
      dataSampler = new DataSamplerImpl();
      dataSampler.setProperties(properties);
      dataSampler.setTimeGenerator(timeGenerator);
      dataSampler.setHibernateOperations(mockHibernateOperations);
      dataSampleDao = createLocalMock(DataSampleDao.class);
      dataSampler.setDataSampleDao(dataSampleDao);
      todayMidnight = TimeGenerator.getMidnight(new Date());
      tomorrowMidnight = TimeGenerator.shiftDate(todayMidnight,
                                                 Calendar.DAY_OF_MONTH,
                                                 1);
      DataSample dataSampleToDel1 = new DataSample(tomorrowMidnight,
                                        iteration.getId(),
                                        "remainingHours",
                                        1.0);
      DataSample dataSampleToDel2 = new DataSample(tomorrowMidnight,
                                        iteration.getId(),
                                        "actualHours",
                                        1.0);
      DataSample dataSampleToDel3 = new DataSample(tomorrowMidnight,
                                        iteration.getId(),
                                        "estimatedHours",
                                        1.0);

      estimatedHoursDataSamples.add(dataSampleToDel3);
      actualHoursDataSamples.add(dataSampleToDel2);
      remainingHoursDataSample.add(dataSampleToDel1);

      activeIteration = new Iteration();
      activeIteration.setId(99);
      activeIteration.setIterationStatus(IterationStatus.ACTIVE);
   }

   public void testGenerateOpeningDatasample() throws HibernateException {
      checkIfDataSamplesHaveNotBeenAlreadyGenerated(todayMidnight, Collections.EMPTY_LIST,
                                                    Collections.EMPTY_LIST,
                                                    Collections.EMPTY_LIST);
      saveDataSamples();
      replay();
      dataSampler.generateOpeningDataSamples(iteration);
      verify();
   }

   public void testGenerateDatasample() {
      checkIfDataSamplesHaveNotBeenAlreadyGenerated(tomorrowMidnight, Collections.EMPTY_LIST,
                                                    Collections.EMPTY_LIST,
                                                    Collections.EMPTY_LIST);
      saveDataSamples();
      replay();
      dataSampler.generateDataSamples(iteration);
      verify();
   }

   public void testUpdateDatasample() {
      checkIfDataSamplesHaveNotBeenAlreadyGenerated(tomorrowMidnight, estimatedHoursDataSamples,
                                                    actualHoursDataSamples,
                                                    remainingHoursDataSample);
      updateDataSamples();
      replay();
      dataSampler.generateDataSamples(iteration);
      verify();
   }


   public void testGenerateClosingDatasampleOnIterationEndDate() {
      iteration.setEndDate(TimeGenerator.shiftDate(timeGenerator.getCurrentTime(),
                                                 Calendar.MINUTE,
                                                 10));

      checkIfDataSamplesHaveNotBeenAlreadyGenerated(tomorrowMidnight, Collections.EMPTY_LIST,
                                                    Collections.EMPTY_LIST,
                                                    Collections.EMPTY_LIST);
      saveDataSamples();
      replay();
      dataSampler.generateClosingDataSamples(iteration);
      verify();
   }

   public void testGenerateClosingDatasampleAfterIterationEndDate() {
      iteration.setEndDate(TimeGenerator.shiftDate(timeGenerator.getCurrentTime(),
                                                 Calendar.MINUTE,
                                                 -10));
      checkIfDataSamplesHaveNotBeenAlreadyGenerated(todayMidnight, estimatedHoursDataSamples,
                                                    actualHoursDataSamples,
                                                    remainingHoursDataSample);
      updateDataSamples();
      replay();
      dataSampler.generateClosingDataSamples(iteration);
      verify();


   }

   public void testExtendIterationEndDateIfNeeded_TurnedOff() throws Exception {
      iteration.setEndDate(todayMidnight);

      setAutomaticallyExtendIterationEndDate(false);
      assertEquals(todayMidnight, iteration.getEndDate());
      replay();
      dataSampler.extendIterationEndDateIfNeeded(iteration, tomorrowMidnight);
      verify();
      assertEquals(todayMidnight, iteration.getEndDate());
   }

   public void testExtendIterationEndDateIfNeeded_TurnedOnIterationIsActive() throws Exception {
      iteration.setEndDate(todayMidnight);
      iteration.setIterationStatus(IterationStatus.ACTIVE);
      setAutomaticallyExtendIterationEndDate(true);
      expect(mockHibernateOperations.save(iteration)).andReturn(null);
      replay();
      dataSampler.extendIterationEndDateIfNeeded(iteration, tomorrowMidnight);
      verify();
      assertEquals(tomorrowMidnight, iteration.getEndDate());
   }

   public void testExtendIterationEndDateIfNeeded_TurnedOnIterationIsInactive() throws Exception {
      iteration.setEndDate(todayMidnight);
      setAutomaticallyExtendIterationEndDate(true);
      assertEquals(todayMidnight, iteration.getEndDate());
      replay();
      dataSampler.extendIterationEndDateIfNeeded(iteration, tomorrowMidnight);
      verify();
      assertEquals(todayMidnight, iteration.getEndDate());
   }

   private void checkIfDataSamplesHaveNotBeenAlreadyGenerated(Date samplingDate, List estimatedHoursDataSamples,
                                                              List actualHoursDataSamples,
                                                              List remainingHoursDataSample) {
	   expect(dataSampleDao.getDataSamples(samplingDate, iteration, "estimatedHours")).andReturn(estimatedHoursDataSamples).times(0, 1);
	   expect(dataSampleDao.getDataSamples(samplingDate, iteration, "actualHours")).andReturn(actualHoursDataSamples).times(0, 1);
	   expect(dataSampleDao.getDataSamples(samplingDate, iteration, "remainingHours")).andReturn(remainingHoursDataSample).times(0, 1);
   }

   private void saveDataSamples() {
      expect(dataSampleDao.save((DataSample) anyObject())).andReturn(1).times(0,3);
   }

   private void updateDataSamples() {
	   expect(dataSampleDao.save((DataSample) anyObject())).andReturn(1).times(3);
   }

   private void setAutomaticallyExtendIterationEndDate(boolean automaticallyExtend) {
      properties.setProperty(DataSamplerImpl.AUTOMATICALLY_EXTEND_END_DATE_PROP, Boolean.toString(automaticallyExtend));
   }
}