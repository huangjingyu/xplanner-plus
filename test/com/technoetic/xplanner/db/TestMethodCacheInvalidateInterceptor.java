/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.db;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

/**
 * User: mprokopowicz
 * Date: Mar 31, 2006
 * Time: 12:21:35 PM
 */


public class TestMethodCacheInvalidateInterceptor extends TestCase {
   static final Integer RETURN_VALUE = new Integer(1);
   static final String TEST_METHOD_NAME = "testMethodName";
   private MethodCacheInvalidateInterceptor methodCacheInvalidateInterceptor;
   private Map cacheMap;
   private Object[] attrs;

   protected void setUp() throws Exception {
      attrs = new Object[]{"arg1"};
      super.setUp();
      cacheMap = new HashMap();
      methodCacheInvalidateInterceptor = new MethodCacheInvalidateInterceptor(cacheMap);
      methodCacheInvalidateInterceptor.setMethodsToInvalidate(Arrays.asList(new Object[]{TEST_METHOD_NAME}));
   }

   public void testInvalidate_methodWithArguments() throws Exception {
      List argList = Arrays.asList(new Object[]{"arg1"});
      Map methodCache = new HashMap();
      methodCache.put(argList, RETURN_VALUE);
      cacheMap.put(TEST_METHOD_NAME, methodCache);
      methodCacheInvalidateInterceptor.invalidate(argList);
      assertTrue(methodCache.isEmpty());
   }

   public void testInvalidate_methodWithNoArguments() throws Exception {
      List callerArgList = Arrays.asList(attrs);
      List cachedMethodArgList = Collections.EMPTY_LIST;
      Map methodCache = new HashMap();
      methodCache.put(cachedMethodArgList, RETURN_VALUE);
      cacheMap.put(TEST_METHOD_NAME, methodCache);
      methodCacheInvalidateInterceptor.invalidate(callerArgList);
      assertTrue(methodCache.isEmpty());
   }

   public void testGetMethodCacheKey() throws Exception {
      List methodCacheKey = methodCacheInvalidateInterceptor.getMethodCacheKey(attrs);
      assertEquals(1, methodCacheKey.size());
      assertEquals("arg1", methodCacheKey.get(0));
   }
}