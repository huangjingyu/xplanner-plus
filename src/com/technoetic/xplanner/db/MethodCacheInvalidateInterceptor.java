/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.db;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.aop.AfterReturningAdvice;

/**
 * User: Mateusz Prokopowicz
 * Date: Sep 6, 2005
 * Time: 6:40:13 AM
 */
public class MethodCacheInvalidateInterceptor implements AfterReturningAdvice {
   private Map resultByArgsByMethodName;
   private List methodNamesToInvalidate;
   Logger LOG = Logger.getLogger(MethodCacheInvalidateInterceptor.class);

   public void setMethodsToInvalidate(List methodNamesToInvalidate) {
      this.methodNamesToInvalidate = methodNamesToInvalidate;
   }

   public MethodCacheInvalidateInterceptor(Map cacheMap) {
      this.resultByArgsByMethodName = cacheMap;
   }

   public List getMethodCacheKey(Object args[]) {
      return Arrays.asList(args);
   }


   public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
      List argumentList = getMethodCacheKey(args);
      invalidate(argumentList);
   }

   void invalidate(List argumentList) {
      for (int i = 0; i < methodNamesToInvalidate.size(); i++) {
         String methodName = (String) methodNamesToInvalidate.get(i);
         LOG.debug("Invalidate cache for method " +
                   methodName +
                   "(" +
                   StringUtils.join(argumentList.iterator(), ", ") +
                   ")");
         Map methodCache = (Map) resultByArgsByMethodName.get(methodName);
         if (methodCache != null) {
            if (!methodCache.containsKey(argumentList)) {
               argumentList = Collections.EMPTY_LIST;
            }
            if (methodCache.containsKey(argumentList)) {
               methodCache.remove(argumentList);
            }
         }
      }
   }
}
