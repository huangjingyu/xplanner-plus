package com.technoetic.xplanner.security;

import javax.servlet.http.HttpServletRequest;

public interface Authenticator {
   void authenticate(HttpServletRequest request, String userId, String password) throws AuthenticationException;

   void logout(HttpServletRequest request, int principalId) throws AuthenticationException;
}
