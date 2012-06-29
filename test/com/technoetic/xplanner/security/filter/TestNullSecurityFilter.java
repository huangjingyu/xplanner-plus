package com.technoetic.xplanner.security.filter;

import java.util.Arrays;

import javax.security.auth.Subject;

import junit.framework.Assert;
import net.sf.xplanner.domain.Person;

import com.technoetic.xplanner.security.PersonPrincipal;
import com.technoetic.xplanner.security.SecurityHelper;

public class TestNullSecurityFilter extends AbstractSecurityFilterTestCase {
    private NullSecurityFilter securityFilter;
    private final String AUTHENTICATOR_URL = "/login";

    public TestNullSecurityFilter(String s) {
        super(s);
    }

    protected void setUp() throws Exception {
        securityFilter = new NullSecurityFilter();
        securityFilter.setAuthenticatorUrl(AUTHENTICATOR_URL);
        super.setUp(); // super.setUp depends on securityFilter
    }

    protected AbstractSecurityFilter getSecurityFilter() {
        return securityFilter;
    }

    public void testBypassedRequest() throws Exception {
        setUpRequest("", "/do/login");

        securityFilter.doFilter(support.request, support.response, mockFilterChain);

        assertNull("redirect", support.response.getRedirect());
        assertTrue("filter chain not called", mockFilterChain.doFilterCalled);
    }

    public void testAuthenticated() throws Exception {
        setUpSubject();
        setUpRequest("", "/do/something");

        securityFilter.doFilter(support.request, support.response, mockFilterChain);

        assertNull("redirect", support.response.getRedirect());
        assertTrue("filter chain not called", mockFilterChain.doFilterCalled);
    }

    public void testDefaultAuthenticationAsSysadmin() throws Exception {
        String userId = "sysadmin";
        Person person = new Person(userId);
        support.hibernateSession.find2Return = Arrays.asList(new Object[]{person});
        person.setId(11);
        Subject subject = new Subject();
        subject.getPrincipals().add(new PersonPrincipal(person));
        setUpRequest("/x/*", "/do/something");

        securityFilter.doFilter(support.request, support.response, mockFilterChain);

        assertNull("redirect", support.response.getRedirect());
        assertTrue("filter chain not called", mockFilterChain.doFilterCalled);
        Assert.assertEquals("wrong principal", userId, SecurityHelper.getUserPrincipal(support.request).getName());
    }
}
