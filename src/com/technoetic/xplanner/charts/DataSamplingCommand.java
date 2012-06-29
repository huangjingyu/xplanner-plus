/*
 * Copyright (c) Mateusz Prokopowicz. All Rights Reserved.
 */

package com.technoetic.xplanner.charts;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.xplanner.domain.Iteration;

import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.technoetic.xplanner.Command;
import com.technoetic.xplanner.util.TimeGenerator;

public class DataSamplingCommand extends HibernateDaoSupport implements Command {
   public static final Logger LOG = Logger.getLogger(DataSamplingCommand.class);
   public static final String ITERATION_TO_SAMPLE_QUERY = "com.technoetic.xplanner.domain.IterationToSample";
   private DataSampler dataSampler;
   private TimeGenerator timeGenerator;

   public void setTimeGenerator(TimeGenerator timeGenerator) {
      this.timeGenerator = timeGenerator;
   }

   public void setDataSampler(DataSampler dataSampler) {
      this.dataSampler = dataSampler;
   }

   public void execute() {
      final Date samplingDate = timeGenerator.getTodaysMidnight();
      List iterations = getIterationList(samplingDate);
      for (int i = 0; i < iterations.size(); i++) {
         try {
            final Iteration iteration = (Iteration) iterations.get(i);
            LOG.debug("Generate datasamples at " + samplingDate.toString());
            LOG.debug(" for iteration: projectId [" +
                      iteration.getProject().getId() +
                      "], id [" + iteration.getId() + "], name [" +
                      iteration.getName() +
                      "]");

            dataSampler.generateDataSamples(iteration);


         }
         catch (Exception e) {
            LOG.error("Error saving datasamples " + e.getMessage());
            LOG.debug("Stack trace: ", e);
         }
      }
   }


   public List getIterationList(Date samplingDate) {
      Date prevSamplingDate = TimeGenerator.shiftDate(samplingDate, Calendar.DAY_OF_MONTH, -1);
      List iterationToSampleList = getHibernateTemplate().findByNamedQueryAndNamedParam(ITERATION_TO_SAMPLE_QUERY,
                                                                                        new String[]{
                                                                                              "prevSamplingDate",
                                                                                              "samplingDate"},
                                                                                        new Object[]{prevSamplingDate,
                                                                                                     samplingDate});
      for (Iterator iterator = iterationToSampleList.iterator(); iterator.hasNext();) {
         Iteration iteration = (Iteration) iterator.next();
         LOG.debug("Iteration " + iteration.getName() + " contains " + iteration.getUserStories().size() + " stories.");
      }
      LOG.debug("Iterations to sample on " + samplingDate + " size: " + iterationToSampleList.size());
      return iterationToSampleList;

   }
}
