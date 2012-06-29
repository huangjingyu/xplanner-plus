package com.technoetic.xplanner.security.module;

import com.technoetic.xplanner.security.LoginModule;

public class ConfigurationException extends RuntimeException {
   public ConfigurationException() {
   }

   public ConfigurationException(String message) {
      super(message);
   }

   public ConfigurationException(Throwable cause) {
      super(LoginModule.MESSAGE_CONFIGURATION_ERROR_KEY, cause);
   }

   public ConfigurationException(String message, Throwable cause) {
      super(message, cause);
   }
}