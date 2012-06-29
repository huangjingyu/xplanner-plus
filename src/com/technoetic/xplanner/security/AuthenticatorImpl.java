package com.technoetic.xplanner.security;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.technoetic.xplanner.util.LogUtil;

public class AuthenticatorImpl implements Authenticator {
   private static Logger LOG = LogUtil.getLogger();
   public static final String LOGIN_CONTEXT_SESSION_KEY = "LOGIN_CONTEXT";
   static final String GUESTS_KEY = "xplanner.security.guests";

   private LoginContext loginContext;
   public int NO_PARENT = 0;

   public AuthenticatorImpl(LoginContext loginContext) {
      this.loginContext = loginContext;
   }

   public AuthenticatorImpl() {
   }

   public void authenticate(HttpServletRequest request, String userId, String password)
         throws AuthenticationException {
      LoginContext loginContext = getLoginContext();
      if (SecurityHelper.isUserAuthenticated(request)) {
         loginContext.logout(request);
      }
      loginContext.authenticate(userId, password);
      SecurityHelper.setSubject(request, loginContext.getSubject());

      setLoginContext(request, loginContext);
   }

   public LoginContext getLoginContext() {
      return loginContext;
   }

   public static LoginContext getLoginContext(HttpServletRequest request) {
      return (LoginContext) request.getSession().getAttribute(LOGIN_CONTEXT_SESSION_KEY);
   }

   public static void setLoginContext(HttpServletRequest request, LoginContext context) {
      request.getSession().setAttribute(LOGIN_CONTEXT_SESSION_KEY, context);
   }

   public static LoginModule getLoginModule(HttpServletRequest request) {
      LoginContext context = getLoginContext(request);
      if (context == null) return null;
      LoginModule loginModule = null;
      try {
         loginModule = context.getLoginModule();
      } catch (RuntimeException e) {
         LOG.error(e);
      }
      return loginModule;
   }

   public void logout(HttpServletRequest request, int principalId) throws AuthenticationException {
      LoginModule loginModule = getLoginModule(request);
      if (loginModule != null) {
         loginModule.logout(request);
      }

   }

}
