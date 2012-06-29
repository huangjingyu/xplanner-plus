package com.technoetic.xplanner.db.hibernate;

import org.apache.log4j.Logger;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.util.LogUtil;

public class ThreadSession {
   protected static final Logger LOG = LogUtil.getLogger();
   private static ThreadLocal threadSession = new ThreadLocal();

   /**
    * @deprecated DEBT(SPRING) Should be injected instead
    */
   public static Session get() {
      Object session = threadSession.get();
      if (LOG.isDebugEnabled()) {
         LOG.debug("get() --> " + session);
      }
      return (Session) session;
   }

   public static void set(Session session) {
      if (LOG.isDebugEnabled()) {
         LOG.debug("set(" + session + ")");
      }
      threadSession.set(session);
   }
}
