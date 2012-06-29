package com.technoetic.xplanner.views;

public interface IterationPage {
   //FIELDS
   String START_TIME_FIELD = "startDate";

   //ACTIONS
   String START_ACTION = "iteration.status.editor.start";
   String CLOSE_ACTION = "iteration.status.editor.close";

   //VIEWS
   String ACCURACY_VIEW = "iteration.link.accuracy";
   String STATISTICS_VIEW = "iteration.link.statistics";
   String METRICS_VIEW = "iteration.link.metrics";
}
