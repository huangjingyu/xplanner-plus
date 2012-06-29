/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.technoetic.xplanner.security.module.jaas;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import com.technoetic.xplanner.security.module.LoginSupportImpl;
import com.technoetic.xplanner.security.module.jaas.TestJaasLoginModuleAdapter.MockJaasLoginModule;

public class TestJaasLoginModuleAdapterFactory extends TestCase
{
   public static final String JAAS_LOGIN_MODULE_CLASS_NAME_KEY = JaasLoginModuleAdapterFactory.JAAS_LOGIN_MODULE_CLASS_NAME_KEY;
   public static final String JAAS_USER_PRINCIPAL_CLASS_NAME_KEY = JaasLoginModuleAdapterFactory.JAAS_USER_PRINCIPAL_CLASS_NAME_KEY;

   public void test() throws Exception
   {
      Map options = new HashMap();
      options.put(JAAS_LOGIN_MODULE_CLASS_NAME_KEY, MockJaasLoginModule.class.getName());
      options.put(JAAS_USER_PRINCIPAL_CLASS_NAME_KEY, Integer.class.getName());
      JaasLoginModuleAdapterFactory factory = new JaasLoginModuleAdapterFactory(new LoginSupportImpl());
      JaasLoginModuleAdapter loginModule = (JaasLoginModuleAdapter) factory.newInstance(options);
      assertNotNull(loginModule);
      assertNotNull(loginModule.getJAASLoginModule());
      assertTrue(loginModule.getJAASLoginModule() instanceof MockJaasLoginModule);
      assertNotNull(loginModule.getPrincipalClass());
      assertEquals(Integer.class, loginModule.getPrincipalClass() );
   }
}

