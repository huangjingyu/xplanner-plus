package com.technoetic.xplanner.security.module;

import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.security.auth.Subject;

import junit.framework.TestCase;

import org.easymock.MockControl;

import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.LoginModule;
import com.technoetic.xplanner.util.MainBeanFactory;

public abstract class AbstractLoginModuleTestCase extends TestCase
{
    protected static final String PASSWORD = "password";
    protected static final String USER_ID = "user";
    protected LoginModule loginModule;
    protected MockControl mockLoginSupportControl;
    protected LoginSupport mockLoginSupport;
    protected Subject mockSubject;

    public AbstractLoginModuleTestCase(){
    }

    public AbstractLoginModuleTestCase(String s){
        super (s);
    }

    public void setUp() throws Exception {
       super.setUp();
       mockLoginSupportControl = MockControl.createControl(LoginSupport.class);
       mockLoginSupport = (LoginSupport) mockLoginSupportControl.getMock();
       mockSubject = new Subject();
       loginModule = createLoginModule();
    }

   protected LoginModule createLoginModule() {
      return null;
   }

   protected void tearDown() throws Exception {
      MainBeanFactory.reset();
      super.tearDown();
   }

   protected void replay()
    {
        mockLoginSupportControl.replay();
    }

    protected void verify()
    {
        mockLoginSupportControl.verify();
    }

    protected void authenticateAndCheckException(String errorMessageKey)
    {
        try
        {
            loginModule.authenticate(USER_ID, PASSWORD);
            fail("Exception should be thrown");
        }
        catch (AuthenticationException e)
        {
            assertEquals("Wrong error message", errorMessageKey, e.getMessage());
        }
    }

   protected Subject createSubjectWithPrincipal(Principal principal) {
      Set principalSet = new HashSet();
      principalSet.add(principal);
      return new Subject(true, principalSet, Collections.EMPTY_SET, Collections.EMPTY_SET);
   }
}
