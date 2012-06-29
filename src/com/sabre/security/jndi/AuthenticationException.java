package com.sabre.security.jndi;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: sg897500
 * Date: Oct 14, 2005
 * Time: 3:14:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class AuthenticationException extends Exception {
   private Map<String,String> errorByModule = new HashMap<String,String>();

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

   public AuthenticationException(Map<String,String> errorByModule) {
      this.errorByModule = errorByModule;
   }

   public Map<String,String> getErrorsByModule() {
       return this.errorByModule;
   }
   
}
