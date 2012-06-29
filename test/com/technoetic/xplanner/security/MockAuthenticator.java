package com.technoetic.xplanner.security;

import javax.servlet.http.HttpServletRequest;

public class MockAuthenticator implements Authenticator {
   public boolean authenticateCalled;
   public HttpServletRequest authenticateRequest;
   public String authenticateUserId;
   public String authenticatePassword;
   public AuthenticationException authenticateException;

   public void authenticate(HttpServletRequest request, String userId, String password) throws AuthenticationException {
      authenticateCalled = true;
      authenticateRequest = request;
      authenticateUserId = userId;
      authenticatePassword = password;
      if (authenticateException != null) {
         throw authenticateException;
      }
   }

   public void logout(HttpServletRequest request, int principalId) throws AuthenticationException {
      //To change body of implemented methods use File | Settings | File Templates.
   }
}
