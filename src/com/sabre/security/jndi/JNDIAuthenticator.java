package com.sabre.security.jndi;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.security.auth.Subject;

public interface JNDIAuthenticator {

   String MESSAGE_COMMUNICATION_ERROR_KEY = "authentication.module.message.communicationError";
   String MESSAGE_AUTHENTICATION_FAILED_KEY = "authentication.module.message.authenticationFailed";

   Subject authenticate(String username, String credentials) throws AuthenticationException;

   void setOptions(Map options);

   void setDigest(String algorithm) throws NoSuchAlgorithmException;

   void setUserSubtree(String userSubtree);

   void setRoleSubtree(String roleSubtree);

   void setContextFactory(String contextFactory);

   void setAuthentication(String authentication);

   void setConnectionUser(String connectionUser);

   void setConnectionPassword(String connectionPassword);

   void setConnectionURL(String connectionURL);

   void setProtocol(String protocol);

   void setReferrals(String referrals);

   void setUserBase(String userBase);

   void setUserSearch(String userSearch);

   void setUserPassword(String userPassword);

   void setUserPattern(String userPattern);

   void setRoleBase(String roleBase);

   void setUserRoleName(String userRoleName);

   void setRoleName(String roleName);

   void setRoleSearch(String roleSearch);
}
