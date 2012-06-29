package com.technoetic.xplanner.security.auth;

import junit.framework.TestCase;

public class TestAuthorizerInitializer extends TestCase {
    AuthorizerInitializer authorizerInitializer;
    private MockAuthorizer authorizer;

    public void testInit() throws Exception {
        SystemAuthorizer.set(null);
        assertNull(SystemAuthorizer.get());
        authorizer = new MockAuthorizer();
        authorizerInitializer = new AuthorizerInitializer(authorizer);
        authorizerInitializer.init();
        assertSame(authorizer,  SystemAuthorizer.get());
    }
}