package com.technoetic.xplanner.db;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * User: Mateusz Prokopowicz
 * Date: Aug 25, 2005
 * Time: 10:30:10 AM
 */
public interface TaskQuery {
   Collection query(Collection cachedTasks, int personId, Boolean completed, Boolean active);

   List queryTasks(String queryName, int personId);

   List queryTasks(String queryName, Date date);
}
