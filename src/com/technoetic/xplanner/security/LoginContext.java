package com.technoetic.xplanner.security;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.technoetic.xplanner.security.module.ConfigurationException;
import com.technoetic.xplanner.security.module.LoginModuleLoader;
import com.technoetic.xplanner.util.LogUtil;

public class LoginContext implements Serializable
{
   protected static final Logger LOG = LogUtil.getLogger();

   private transient LoginModule[] loginModules;
   private transient LoginModuleLoader moduleLoader;
   private Subject subject;
   private int loginModuleIndex;

   public LoginContext(LoginModuleLoader moduleLoader) {this.moduleLoader = moduleLoader;}

   public void authenticate(String userId, String password) throws AuthenticationException
   {
       HashMap errorMap = new HashMap();
       LoginModule[] modules = getLoginModules();
       LoginModule loginModule = null;
       for (int i = 0; i < modules.length; i++) {
         try {
            loginModule = modules[i];
            subject = loginModule.authenticate(userId, password);
            loginModuleIndex = i;
            LOG.debug("Authenticating successfully " + this);
            return;
         }
         catch (AuthenticationException aex) {
            errorMap.put(loginModule.getName(), aex.getMessage());
         }
      }
      loginModuleIndex = -1;
      LOG.debug("Failure to authenticate " + this);
      throw new AuthenticationException(errorMap);
   }

   public LoginModule getLoginModule() throws ConfigurationException {
      LoginModule[] modules = getLoginModules();
      if (loginModuleIndex >= modules.length) {
         throw new RuntimeException("index of used login module=" + loginModuleIndex +
                                    ", number of modules=" + modules.length);
      }
      return modules[loginModuleIndex];
   }

   public Subject getSubject()
   {
       return subject;
   }

   public void logout(HttpServletRequest request) throws AuthenticationException
    {
        LoginModule[] modules = getLoginModules();
        for (int i = 0; i < modules.length; i++)
        {
            modules[i].logout(request);
        }
    }

    protected LoginModule[] getLoginModules() throws ConfigurationException
    {
        if (loginModules == null) {
           loginModules = moduleLoader.loadLoginModules();
        }
        return loginModules;
    }

   // DEBT: The deserialization creating new LoginModule should be cleaned up when we have a bean context per session/request...
    private final void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
       in.defaultReadObject();
       loginModules = getLoginModules();
       LOG.debug("Deserializing... " + this);
    }

   public String toString() {
      return "LoginContext{" +
             "subject=" + subject +
             ", loginModuleIndex=" + loginModuleIndex +
             ", loginModules =" + (loginModules == null ? null : Arrays.asList(loginModules)) +
             ", moduleLoader=" + moduleLoader +
             '}';
   }
}

