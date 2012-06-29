package com.technoetic.xplanner.security.auth;

public class AuthorizerInitializer {
    private Authorizer authorizer;

    public AuthorizerInitializer(Authorizer authorizer) {
        this.authorizer = authorizer;
    }

    public void init(){
        SystemAuthorizer.set(authorizer);
    }
}
