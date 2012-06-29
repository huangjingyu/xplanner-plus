package com.technoetic.xplanner.security.filter;

public class TestFormSecurityFilter extends AbstractSecurityFilterTestCase {
    private FormSecurityFilter securityFilter;
    private final String AUTHENTICATOR_URL = "/login";

    public TestFormSecurityFilter(String s) {
        super(s);
    }

    protected void setUp() throws Exception {
        securityFilter = new FormSecurityFilter();
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

    public void testNotAuthenticated() throws Exception {
        setUpRequest("/x/*", "/do/something");

        securityFilter.doFilter(support.request, support.response, mockFilterChain);

        assertEquals("wrong redirect", CONTEXT + AUTHENTICATOR_URL, support.response.getRedirect());
        assertFalse("filter chain called", mockFilterChain.doFilterCalled);
    }
}
