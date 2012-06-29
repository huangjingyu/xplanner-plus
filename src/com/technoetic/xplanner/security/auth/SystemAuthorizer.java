package com.technoetic.xplanner.security.auth;

import org.apache.log4j.Logger;

public class SystemAuthorizer {
    private static Authorizer authorizer;
    private static Logger log = Logger.getLogger(SystemAuthorizer.class);

   /**
    * @deprecated DEBT(SPRING) Should be injected
    */
    public static Authorizer get() {
        if (authorizer == null){
            log.warn("The authorizer has not been set yet $$$$$$$$");
        }

        return authorizer;
    }

    public static void set(Authorizer authorizer) {
        SystemAuthorizer.authorizer = authorizer;
    }
}

