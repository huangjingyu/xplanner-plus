package com.technoetic.xplanner.security.module.jaas;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import net.sf.xplanner.domain.Person;

import org.easymock.MockControl;
import org.easymock.internal.AlwaysMatcher;
import org.hibernate.HibernateException;

import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.module.AbstractLoginModuleTestCase;

public class TestJaasLoginModuleAdapter extends AbstractLoginModuleTestCase
{
    LoginModule mockLoginModule;
    MockControl mockLoginModuleControl;
    MockControl mockPrincipalControl;
    Principal mockPrincipal;

   public void setUp() throws Exception
   {
      super.setUp();

      mockPrincipalControl = MockControl.createControl(Principal.class);
      mockPrincipal = (Principal) mockPrincipalControl.getMock();
      mockLoginModuleControl = MockControl.createControl(LoginModule.class);
      mockLoginModule = (LoginModule) mockLoginModuleControl.getMock();
      loginModule = new JaasLoginModuleAdapter(mockLoginSupport, mockLoginModule, mockPrincipal.getClass(), new HashMap());
   }

   public void testAuthenticate_Successful() throws AuthenticationException, LoginException, HibernateException
   {
       mockSubject = createSubjectWithPrincipal(mockPrincipal);
       mockLoginSupportControl.expectAndReturn(mockLoginSupport.createSubject(), mockSubject);
       mockLoginModule.initialize(mockSubject, null, null, Collections.EMPTY_MAP);
       mockLoginModuleControl.setMatcher(new AlwaysMatcher());
       mockLoginModuleControl.expectAndReturn(mockLoginModule.login(), true);
       mockLoginModuleControl.expectAndReturn(mockLoginModule.commit(), true);
       mockPrincipal.getName();
       mockPrincipalControl.setReturnValue(USER_ID);
       mockLoginSupport.populateSubjectPrincipalFromDatabase(mockSubject, USER_ID);
       mockLoginSupportControl.setReturnValue(new Person());
       MockJaasLoginModule.commitReturn = true;
       MockJaasLoginModule.loginReturn = true;
       replay();
       loginModule.authenticate(USER_ID, PASSWORD);
       verify();
   }

    public void testAuthenticate_AuthenticationFailed() throws AuthenticationException, LoginException, HibernateException
    {
        mockSubject = createSubjectWithPrincipal(null);
        mockLoginSupportControl.expectAndReturn(mockLoginSupport.createSubject(), mockSubject);
        mockLoginModule.initialize(mockSubject, null, null, Collections.EMPTY_MAP);
        mockLoginModuleControl.setMatcher(new AlwaysMatcher());
        mockLoginModuleControl.expectAndReturn(mockLoginModule.login(), false);

        replay();
        authenticateAndCheckException(JaasLoginModuleAdapter.MESSAGE_AUTHENTICATION_FAILED_KEY);
        verify();
    }

    public void testAuthenticate_UserNotFound() throws AuthenticationException, LoginException, HibernateException
    {
        mockSubject = createSubjectWithPrincipal(mockPrincipal);
        mockLoginSupportControl.expectAndReturn(mockLoginSupport.createSubject(), mockSubject);
        mockLoginModule.initialize(mockSubject, null, null, Collections.EMPTY_MAP);
        mockLoginModuleControl.setMatcher(new AlwaysMatcher());
        mockLoginModuleControl.expectAndReturn(mockLoginModule.login(), true);
        mockLoginModuleControl.expectAndReturn(true, mockLoginModule.commit());
        mockPrincipal.getName();
        mockPrincipalControl.setReturnValue(USER_ID);
        mockLoginSupport.populateSubjectPrincipalFromDatabase(mockSubject, USER_ID);
        mockLoginSupportControl.setThrowable(new AuthenticationException(JaasLoginModuleAdapter.MESSAGE_USER_NOT_FOUND_KEY));
        replay();
        authenticateAndCheckException(JaasLoginModuleAdapter.MESSAGE_USER_NOT_FOUND_KEY);
    }

    public void testAuthenticate_ServerError() throws AuthenticationException, LoginException, HibernateException
    {
        mockSubject = createSubjectWithPrincipal(null);
        mockLoginSupportControl.expectAndReturn(mockLoginSupport.createSubject(), mockSubject);
        mockLoginModule.initialize(mockSubject, null, null, Collections.EMPTY_MAP);
        mockLoginModuleControl.setMatcher(new AlwaysMatcher());
        mockLoginModuleControl.expectAndThrow(mockLoginModule.login(), new LoginException());
        replay();
        authenticateAndCheckException(JaasLoginModuleAdapter.MESSAGE_SERVER_ERROR_KEY);
        verify();
    }

    public void replay()
    {
        super.replay();
        mockLoginModuleControl.replay();
        mockPrincipalControl.replay();
    }

    public void verify()
    {
        super.verify();
        mockLoginModuleControl.verify();
        mockPrincipalControl.verify();
    }

   public static class MockJaasLoginModule implements LoginModule {
      static public boolean loginReturn = false;
      static public boolean commitReturn = false;

      public boolean abort() throws LoginException {
         return false;
      }

      public boolean commit() throws LoginException {
         return commitReturn;
      }

      public boolean login() throws LoginException {
         return loginReturn;
      }

      public boolean logout() throws LoginException {
         return false;
      }

      public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options) {

      }
   }

}

