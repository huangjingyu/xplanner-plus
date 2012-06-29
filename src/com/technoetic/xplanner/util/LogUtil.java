package com.technoetic.xplanner.util;

import org.apache.log4j.Logger;

public class LogUtil {
   public static String getCallerMethod() {
      String methodName = "";
      StackTraceElement[] stackTrace = new Throwable().getStackTrace();
      if (stackTrace != null) {
         methodName = stackTrace[2].getClassName() + "." + stackTrace[2].getMethodName();
      }

      return methodName;
   }

   public static String getCallerClass() {
      String className = "";
      StackTraceElement[] stackTrace = new Throwable().getStackTrace();
      if (stackTrace != null) {
         className = stackTrace[2].getClassName();
      }

      return className;
   }

   public static Logger getLogger(Class c) {
      return Logger.getLogger(c.getName());
   }

   public static Logger getLogger() {
      return Logger.getLogger(getCallerClass());
   }
}
