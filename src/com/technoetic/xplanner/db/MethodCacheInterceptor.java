package com.technoetic.xplanner.db;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * User: Mateusz Prokopowicz
 * Date: Sep 6, 2005
 * Time: 6:40:13 AM
 */
public class MethodCacheInterceptor implements MethodInterceptor {
   private Map resultByArgsByMethodName;
   Logger LOG = Logger.getLogger(MethodCacheInterceptor.class);

   public MethodCacheInterceptor(Map cacheMap) {
      this.resultByArgsByMethodName = cacheMap;
   }

   public Object invoke(MethodInvocation invocation) throws Throwable {
      String methodName = invocation.getMethod().getName();
      List argumentList = Arrays.asList(invocation.getArguments());
      return cache(methodName, argumentList, invocation);
   }

   Object cache(String methodName, List argumentList, MethodInvocation invocation) throws Throwable {
      Object retVal;
      LOG.debug("Processing caching for method " +
                methodName +
                "(" +
                StringUtils.join(argumentList.iterator(), ", ") +
                ")");
      Map resultByArgs = (Map) resultByArgsByMethodName.get(methodName);
      if (resultByArgs == null) {
         LOG.debug("No method cache");
         resultByArgs = new HashMap();
         resultByArgsByMethodName.put(methodName, resultByArgs);
      }
      if (!resultByArgs.containsKey(argumentList)) {
         LOG.debug("No arguments cache. Calling the orginal method and cache the result");
         retVal = invocation.proceed();
         resultByArgs.put(argumentList, retVal);
      } else {
         LOG.debug("The cache found. Returning the cached result");
         retVal = resultByArgs.get(argumentList);
      }
      return retVal;
   }
}
