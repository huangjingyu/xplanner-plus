/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.technoetic.xplanner.domain;


public interface Disposition {
   String PLANNED_TEXT = "Planned";
   String CARRIED_OVER_TEXT = "Carried Over";
   String DISCOVERED_TEXT = "Discovered";
   String ADDED_TEXT = "Added";
   String OVERHEAD_TEXT = "Overhead";

   String PLANNED_NAME = "planned";
   String CARRIED_OVER_NAME = "carriedOver";
   String DISCOVERED_NAME = "discovered";
   String ADDED_NAME = "added";
   String OVERHEAD_NAME = "overhead";

   String PLANNED_KEY = "disposition.planned";
   String CARRIED_OVER_KEY = "disposition.carriedOver";
   String DISCOVERED_KEY = "disposition.discovered";
   String ADDED_KEY = "disposition.added";
   String OVERHEAD_KEY = "disposition.overhead";

   String PLANNED_ABBREVIATION_KEY = "disposition.planned.abbreviation";
   String CARRIED_OVER_ABBREVIATION_KEY = "disposition.carriedOver.abbreviation";
   String DISCOVERED_ABBREVIATION_KEY = "disposition.discovered.abbreviation";
   String ADDED_ABBREVIATION_KEY = "disposition.added.abbreviation";
   String OVERHEAD_ABBREVIATION_KEY = "disposition.overhead.abbreviation";
}
