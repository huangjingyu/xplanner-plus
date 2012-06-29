/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.db;
/**
 * User: mprokopowicz
 * Date: Apr 2, 2006
 * Time: 11:12:22 AM
 */

import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;
import net.sf.xplanner.domain.Person;

public class TestCacheInvalidateForPersonAnywhereInAttrListInterceptor extends TestCase {
   private CacheInvalidateForPersonAnywhereInAttrListInterceptor cacheInvalidateForPersonAnywhereInAttrListInterceptor;
   private int personId = 1;
   private Person person = new Person("userId");
   private Object[] args = new Object[]{"arg0", person, "arg2"};

   protected void setUp() throws Exception {
      super.setUp();
      person.setId(personId);
      cacheInvalidateForPersonAnywhereInAttrListInterceptor =
            new CacheInvalidateForPersonAnywhereInAttrListInterceptor(new HashMap());
   }

   public void testGetMethodCacheKey() throws Exception {
      List methodCacheKey = cacheInvalidateForPersonAnywhereInAttrListInterceptor.getMethodCacheKey(args);
      assertEquals(1, methodCacheKey.size());
      assertEquals(new Integer(personId), methodCacheKey.get(0));
   }
}