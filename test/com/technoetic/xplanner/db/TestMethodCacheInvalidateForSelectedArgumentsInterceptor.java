/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.db;
/**
 * User: mprokopowicz
 * Date: Apr 2, 2006
 * Time: 11:04:46 AM
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

public class TestMethodCacheInvalidateForSelectedArgumentsInterceptor extends TestCase {
   private MethodCacheInvalidateForSelectedArgumentsInterceptor methodCacheInvalidateForSelectedArgumentsInterceptor;
   private Map cacheMap;
   private Object[] args = new Object[]{"arg0", "arg1", "arg2", "arg3"};

   protected void setUp() throws Exception {
      super.setUp();
      cacheMap = new HashMap();
      methodCacheInvalidateForSelectedArgumentsInterceptor =
            new MethodCacheInvalidateForSelectedArgumentsInterceptor(cacheMap);
   }

   public void testGetMethodCacheKey() throws Exception {
      int [] argumentIndexes = new int[]{1, 3};
      methodCacheInvalidateForSelectedArgumentsInterceptor.setArgumentIndexes(argumentIndexes);
      List methodCacheKey = methodCacheInvalidateForSelectedArgumentsInterceptor.getMethodCacheKey(args);
      assertEquals(2, methodCacheKey.size());
      assertEquals("arg1", methodCacheKey.get(0));
      assertEquals("arg3", methodCacheKey.get(1));
   }
}