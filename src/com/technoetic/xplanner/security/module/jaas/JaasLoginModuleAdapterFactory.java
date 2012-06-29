/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.technoetic.xplanner.security.module.jaas;

import java.util.Map;
import java.util.MissingResourceException;

import com.technoetic.xplanner.security.LoginModule;
import com.technoetic.xplanner.security.module.ConfigurationException;
import com.technoetic.xplanner.security.module.LoginModuleFactory;
import com.technoetic.xplanner.security.module.LoginSupport;

public class JaasLoginModuleAdapterFactory implements LoginModuleFactory {
   static final String JAAS_USER_PRINCIPAL_CLASS_NAME_KEY = "jaas.principalClass";
   static final String JAAS_LOGIN_MODULE_CLASS_NAME_KEY = "jaas.loginModuleClass";
   private LoginSupport support;

   public JaasLoginModuleAdapterFactory(LoginSupport loginSupport) {this.support = loginSupport;}

   public LoginModule newInstance(Map options) throws ConfigurationException {
      Class principalClass = getClassFromName(options, JAAS_USER_PRINCIPAL_CLASS_NAME_KEY);
      return new JaasLoginModuleAdapter(support, getJAASLoginModule(options), principalClass, options);
   }

   private Class getClassFromName(Map options, String property) {
      String className = (String) options.get(property);
      if (className == null)
      {
         Exception cause = new MissingResourceException("Missing property", this.getClass().getName(), property);
         throw new ConfigurationException(cause);
      }
      Class aClass;
      try {
         aClass = Class.forName(className);
      } catch (ClassNotFoundException e) {
         throw new ConfigurationException(e);
      }
      return aClass;
   }

   private javax.security.auth.spi.LoginModule getJAASLoginModule(Map options) {
      Class loginModuleClass = getClassFromName(options, JAAS_LOGIN_MODULE_CLASS_NAME_KEY);
      try {
         return (javax.security.auth.spi.LoginModule) loginModuleClass.newInstance();
      } catch (Exception e) {
         throw new ConfigurationException(e);
      }
   }


}