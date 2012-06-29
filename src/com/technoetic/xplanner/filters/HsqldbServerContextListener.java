/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Mar 16, 2006
 * Time: 11:16:45 PM
 */
package com.technoetic.xplanner.filters;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.db.hsqldb.HsqlServer;
import com.technoetic.xplanner.util.LogUtil;

public class HsqldbServerContextListener  implements ServletContextListener {
   protected static final Logger LOG = LogUtil.getLogger();
   protected static final String HSQLDB_DATABASE_TYPE = "hsqldb";

   public void contextInitialized(ServletContextEvent event) {
      String databaseType = new XPlannerProperties().getProperty(XPlannerProperties.PATCH_DATABASE_TYPE_KEY);
      if (HSQLDB_DATABASE_TYPE.equalsIgnoreCase(databaseType)) {
         try {
            HsqlServer.start(getWebappRoot(event.getServletContext()));
         } catch (Throwable e) {
            LOG.error("Problem during the start up of the in-process HSQLDB", e);
         }
      } else {
         LOG.debug("HSQL: " + XPlannerProperties.PATCH_DATABASE_TYPE_KEY + " is not defined or is not set to " + HSQLDB_DATABASE_TYPE);
      }
   }

   private String getWebappRoot(ServletContext servletContext) {
         return servletContext.getRealPath("/");
   }

   public void contextDestroyed(ServletContextEvent event) {
      HsqlServer.shutdown();
   }

}