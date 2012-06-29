package com.technoetic.xplanner.charts;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class DataSamplingJob extends QuartzJobBean {
   private DataSamplingCommand dataSamplingCommand;
   public static final String GROUP = "xplanner";
   public static final String NAME = "datasamplingJob";

   private final Logger LOG = Logger.getLogger(DataSamplingJob.class);

   public DataSamplingCommand getDataSamplingCommand() {
      return dataSamplingCommand;
   }

   public void setDataSamplingCommand(DataSamplingCommand dataSamplingSupport) {
      this.dataSamplingCommand = dataSamplingSupport;
   }

   protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
      LOG.info("generating data samples...");
      dataSamplingCommand.execute();
      LOG.info("generating data samples...Done");
   }

}
