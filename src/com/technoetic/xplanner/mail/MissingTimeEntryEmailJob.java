package com.technoetic.xplanner.mail;

import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.technoetic.xplanner.util.TimeGenerator;

/**
 * User: Tomasz Siwiec
 * Date: Nov 02, 2004
 * Time: 9:52:55 PM
 */
public class MissingTimeEntryEmailJob {
   private Logger log = Logger.getLogger(MissingTimeEntryEmailJob.class);
   public static final String NAME = "emailnotificationJob";
   public static final String GROUP = "xplanner";

   private MissingTimeEntryNotifier missingTimeEntryNotifier;
   private TimeGenerator timeGenerator;
   private Date date = null;
   public MissingTimeEntryNotifier getMissingTimeEntryNotifier() {
      return missingTimeEntryNotifier;
   }

   public void setMissingTimeEntryNotifier(MissingTimeEntryNotifier missingTimeEntryNotifier) {
      this.missingTimeEntryNotifier = missingTimeEntryNotifier;
   }

   public TimeGenerator getTimeGenerator() {
      return timeGenerator == null ? new TimeGenerator() : timeGenerator;
   }

   public void setTimeGenerator(TimeGenerator timeGenerator) {
      this.timeGenerator = timeGenerator;
   }

   protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
      missingTimeEntryNotifier.execute();
   }

}
