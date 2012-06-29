package com.technoetic.xplanner.security.auth;

/**
 * This exception is for situation where a user has been authenticated but they
 * are not authorized to perform a specified action.
 */
public class AuthorizationException extends RuntimeException {
    public AuthorizationException() {
    }

    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthorizationException(Throwable cause) {
        super(cause);
    }
}
