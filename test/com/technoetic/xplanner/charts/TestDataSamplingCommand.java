/*
 * Copyright (c) Mateusz Prokopowicz. All Rights Reserved.
 */

package com.technoetic.xplanner.charts;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Project;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.technoetic.xplanner.AbstractUnitTestCase;
import com.technoetic.xplanner.domain.IterationStatus;
import com.technoetic.xplanner.util.TimeGenerator;

public class TestDataSamplingCommand extends AbstractUnitTestCase {
   private DataSamplingCommand dataSamplingSupport;
   private TimeGenerator mockTimeGenerator;
   private HibernateTemplate mockHibernateTemplate;
   private Iteration activeIteration;
   private DataSampler mockDataSampler;

   public void tearDown() throws Exception {
      super.tearDown();
   }

   protected void setUp() throws Exception {
      super.setUp();
      mockTimeGenerator = createLocalMock(TimeGenerator.class);
      mockHibernateTemplate = createLocalMock(HibernateTemplate.class);
      mockDataSampler = createLocalMock(DataSampler.class);
      dataSamplingSupport = new DataSamplingCommand();
      dataSamplingSupport.setTimeGenerator(mockTimeGenerator);
      dataSamplingSupport.setDataSampler(mockDataSampler);
      dataSamplingSupport.setHibernateTemplate(mockHibernateTemplate);
      activeIteration = new Iteration();
      activeIteration.setId(99);
      activeIteration.setProject(new Project());
      activeIteration.setIterationStatus(IterationStatus.ACTIVE);
   }


   public void testGenerateDataSamples() {
      Date todayMidnight = TimeGenerator.getMidnight(new Date());
      Date yesterdayMidnight = TimeGenerator.shiftDate(todayMidnight, Calendar.DATE, -1);
      expect(mockTimeGenerator.getTodaysMidnight()).andReturn(todayMidnight);
      List iterationList = new ArrayList();
      iterationList.add(activeIteration);
      expect(mockHibernateTemplate.findByNamedQueryAndNamedParam(eq(DataSamplingCommand.ITERATION_TO_SAMPLE_QUERY),
                                                            aryEq(new String[]{"prevSamplingDate", "samplingDate"}),
                                                            aryEq(new Object[]{yesterdayMidnight, todayMidnight}))).andReturn(iterationList);
      
      mockDataSampler.generateDataSamples(activeIteration);
      replay();
      dataSamplingSupport.execute();
      verify();
   }
}