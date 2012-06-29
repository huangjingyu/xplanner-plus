package com.technoetic.xplanner.security.module.jndi;

import org.easymock.MockControl;

import com.sabre.security.jndi.AuthenticationException;
import com.sabre.security.jndi.JNDIAuthenticator;
import com.technoetic.xplanner.security.LoginModule;
import com.technoetic.xplanner.security.module.AbstractLoginModuleTestCase;


/**
 * Created by IntelliJ IDEA.
 * User: sg897500
 * Date: Oct 27, 2005
 * Time: 9:32:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestJNDILoginModule extends AbstractLoginModuleTestCase {


   MockControl mockJNDIAuthenticatorControl = null;
   JNDIAuthenticator mockJNDIAuthenticator = null;

   public void setUp() throws Exception
   {
      super.setUp();
      mockJNDIAuthenticatorControl = MockControl.createControl(JNDIAuthenticator.class);
      mockJNDIAuthenticator = (JNDIAuthenticator)mockJNDIAuthenticatorControl.getMock();
      loginModule = createLoginModule();
   }

   protected void tearDown() throws Exception
   {
       super.tearDown();
   }

   protected LoginModule createLoginModule() {
      return new JNDILoginModule(mockJNDIAuthenticator, mockLoginSupport);
   }

   public void testFailedLogin_CommunicationError() throws Exception {
      AuthenticationException authenticationException = new AuthenticationException(JNDIAuthenticator.MESSAGE_COMMUNICATION_ERROR_KEY);
      mockJNDIAuthenticatorControl.expectAndThrow(mockJNDIAuthenticator.authenticate(USER_ID, PASSWORD), authenticationException);
      replay();
      authenticateAndCheckException(JNDIAuthenticator.MESSAGE_COMMUNICATION_ERROR_KEY);
      verify();
   }

   public void testFailedLogin() throws Exception {
      AuthenticationException authenticationException = new AuthenticationException(JNDIAuthenticator.MESSAGE_AUTHENTICATION_FAILED_KEY);
      mockJNDIAuthenticatorControl.expectAndThrow(mockJNDIAuthenticator.authenticate(USER_ID, PASSWORD), authenticationException);
      replay();
      authenticateAndCheckException(JNDIAuthenticator.MESSAGE_AUTHENTICATION_FAILED_KEY);
      verify();
   }

   public void testSuccessLogin() throws Exception {
      mockJNDIAuthenticatorControl.expectAndReturn(mockJNDIAuthenticator.authenticate(USER_ID, PASSWORD), mockSubject);
      mockLoginSupportControl.expectAndReturn(mockLoginSupport.populateSubjectPrincipalFromDatabase(mockSubject, USER_ID),null);
      replay();
      loginModule.authenticate(USER_ID, PASSWORD);
      verify();
   }

   protected void replay()
   {
      super.replay();
      mockJNDIAuthenticatorControl.replay();
   }

   protected void verify()
   {
      super.verify();
      mockJNDIAuthenticatorControl.verify();
   }

}
