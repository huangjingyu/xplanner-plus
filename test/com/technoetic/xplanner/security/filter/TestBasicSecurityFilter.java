package com.technoetic.xplanner.security.filter;

import javax.servlet.ServletException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.MockAuthenticator;
import com.technoetic.xplanner.security.util.Base64;

public class TestBasicSecurityFilter extends AbstractSecurityFilterTestCase {
    private BasicSecurityFilter securityFilter;
    private MockAuthenticator mockAuthenticator;

    public TestBasicSecurityFilter(String s) {
        super(s);
    }

    protected void setUp() throws Exception {
        mockAuthenticator = new MockAuthenticator();
        securityFilter = new BasicSecurityFilter(mockAuthenticator);
        super.setUp();
        Logger.getLogger(BasicSecurityFilter.class).setLevel(Level.ERROR);
    }

    protected AbstractSecurityFilter getSecurityFilter() {
        return securityFilter;
    }

    public void testOnAuthentiationRequestWhenAuthenticated() throws Exception {
        support.request.setHeader("Authorization", "Basic " + new String(Base64.encode("user:pass".getBytes())));

        securityFilter.doFilter(support.request, support.response, mockFilterChain);

        assertAuthenticatorUsage();
    }

    private void assertAuthenticatorUsage() {
        assertTrue("authenticator not called", mockAuthenticator.authenticateCalled);
        assertEquals("wrong auth userId", "user", mockAuthenticator.authenticateUserId);
        assertEquals("wrong auth password", "pass", mockAuthenticator.authenticatePassword);
    }

    public void testIgnoredAuthenticationException() throws Exception {
        // Auth exception simply means the user will not have any credentials in session
        support.request.setHeader("Authorization", "Basic " + new String(Base64.encode("user:pass".getBytes())));
        mockAuthenticator.authenticateException = new AuthenticationException("test");

        securityFilter.doFilter(support.request, support.response, mockFilterChain);

        assertAuthenticatorUsage();
    }

    public void testConvertedOtherException() throws Exception {
        support.request.setHeader("Authorization", "Basic " + new String(Base64.encode("user:pass".getBytes())));
        mockAuthenticator.authenticateException = new AuthenticationException("test");

        try {
            securityFilter.doFilter(support.request, support.response, mockFilterChain);
        } catch (ServletException ex) {
            // expected
            assertEquals("wrong nested exception", mockAuthenticator.authenticateException, ex.getRootCause());
            assertAuthenticatorUsage();
        }

    }

    public void testAlreadyAuthenticated() throws Exception {
        setUpSubject();

        securityFilter.doFilter(support.request, support.response, mockFilterChain);

        assertNoChallenge();
    }

    public void testNotAuthenticated() throws Exception {
        setUpRequest("/x/*", "foo");

        securityFilter.doFilter(support.request, support.response, mockFilterChain);

        assertChallenge();
    }

    private void assertNoChallenge() {
        assertNull("redirect", support.response.getRedirect());
        assertTrue("filter chain not called", mockFilterChain.doFilterCalled);
    }

    private void assertChallenge() {
        assertEquals("no challenge", "Basic realm=\"XPlanner\"", support.response.getHeader("WWW-Authenticate"));
        assertEquals("wrong HTTP status", 401, support.response.getStatus());
        assertFalse("filter chain called", mockFilterChain.doFilterCalled);
    }

}
