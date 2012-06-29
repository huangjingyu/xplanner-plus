package com.technoetic.xplanner.security;

import java.util.HashMap;
import java.util.Map;

/**
 * This exception should be used when a user's identity can't be verified.
 */
public class AuthenticationException extends Exception {
   private Map errorByModule = new HashMap();

   public AuthenticationException() {
   }

    public AuthenticationException(String message) {
        super(message);
        errorByModule.put("Default", message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
        errorByModule.put("", message);
    }

    public AuthenticationException(Throwable cause) {
        super(cause);
    }

   public AuthenticationException(Map errorByModule) {
      this.errorByModule = errorByModule;
   }

   public Map getErrorsByModule()
   {
       return this.errorByModule;
   }


}
