package com.technoetic.xplanner.security.module;

import java.util.ArrayList;
import java.util.HashMap;

import javax.security.auth.Subject;

import junit.framework.Assert;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Role;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import com.technoetic.xplanner.XPlannerTestSupport;
import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.LoginModule;
import com.technoetic.xplanner.security.SecurityHelper;

public class TestXPlannerLoginModule extends AbstractLoginModuleTestCase
    {
    private Person person;
    private HashMap options;
    private XPlannerTestSupport support;

    public TestXPlannerLoginModule(String s)
    {
        super(s);
    }

    protected LoginModule createLoginModule()
    {
        options = new HashMap();
        return new XPlannerLoginModule(new LoginSupportImpl());
    }

    public void setUp() throws Exception
    {
        super.setUp();
        Logger.getRootLogger().setLevel(Level.OFF);
        ArrayList roles = new ArrayList();
        roles.add(new Role("viewer"));
        person = new Person("foo");
        person.setUserId("userId");
        person.setPassword("KcH6P1khBqP9/JdLSkF/jUTaAJroY6K5oljIkQ==");
        support = new XPlannerTestSupport();
        ThreadSession.set(support.hibernateSession);
        loginModule = createLoginModule();
    }

    protected void tearDown() throws Exception
    {
        ThreadSession.set(null);
        super.tearDown();
    }

    public void testSuccessfulAuthenticate() throws Exception
    {
        support.hibernateSession.find2Return = new ArrayList();
        support.hibernateSession.find2Return.add(person);

        Subject subject = loginModule.authenticate("userId", "test");

        assertNotNull(subject);
        assertEquals(1, subject.getPrincipals().size());
        Assert.assertEquals(person.getUserId(), SecurityHelper.getUserPrincipal(subject).getName());
        assertFalse(SecurityHelper.isUserInRole(support.request, "viewer"));
        assertFalse(SecurityHelper.isUserInRole(support.request, "editor"));
        assertFalse(SecurityHelper.isUserInRole(support.request, "admin"));
    }

    public void testFailedAuthenticate_NoUser() throws Exception
    {
        support.hibernateSession.find2Return = new ArrayList();
        authenticateAndCheckException(XPlannerLoginModule.MESSAGE_USER_NOT_FOUND_KEY);
    }

    public void testFailedAuthenticate_WrongPassword() throws Exception
    {
        support.hibernateSession.find2Return = new ArrayList();
        support.hibernateSession.find2Return.add(person);
        authenticateAndCheckException(XPlannerLoginModule.MESSAGE_AUTHENTICATION_FAILED_KEY);
    }

    public void testFailedAuthenticate_ServerError() throws Exception
    {
        support.hibernateSession.find2HibernateException = new HibernateException(new Exception());
        authenticateAndCheckException(LoginSupportImpl.MESSAGE_STORAGE_ERROR_KEY);
    }

    public void testChangingPassword() throws Exception
    {
        support.hibernateSession.find2Return = new ArrayList();
        support.hibernateSession.find2Return.add(person);
        assertTrue(loginModule.isCapableOfChangingPasswords());
        loginModule.changePassword("userId", "newpass");
        assertTrue(((XPlannerLoginModule) loginModule).isPasswordMatched(person, "newpass"));
        assertTrue(support.hibernateSession.flushCalled);
        assertTrue(support.connection.commitCalled);
    }

    public void testChangingPasswordWithError() throws Exception
    {
        support.hibernateSession.find2HibernateException = new HibernateException("test");
        assertTrue(loginModule.isCapableOfChangingPasswords());
        try
        {
            loginModule.changePassword("userId", "newpass");
            fail("no exception");
        }
        catch (AuthenticationException e)
        {
            // expected
            assertTrue(support.connection.rollbackCalled);
        }
    }

    public void testLogout() throws Exception
    {
        loginModule.logout(support.request);
        try
        {
            support.servletSession.isNew();
        }
        catch (IllegalStateException ex)
        {
            assertTrue(ex.getMessage().indexOf("invalidated") != -1);
        }
    }
}
