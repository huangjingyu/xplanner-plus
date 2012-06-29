/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.technoetic.xplanner.acceptance.security;

import com.technoetic.xplanner.acceptance.web.XPlannerWebTester;
import com.technoetic.xplanner.acceptance.web.XPlannerWebTesterImpl;

public class ServerCacheManager {
   private boolean invalidateCache;

   public void invalidateServerCacheIfNeeded() {
      if (invalidateCache) {
         XPlannerWebTester tester = new XPlannerWebTesterImpl();
         tester.login();
         tester.gotoRelativeUrl("/do/invalidateCache");
      }
   }

   public void requestServerCacheInvalidation() {
      invalidateCache = true;
   }


}
