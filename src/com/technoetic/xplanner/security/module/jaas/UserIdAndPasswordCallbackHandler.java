package com.technoetic.xplanner.security.module.jaas;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

public class UserIdAndPasswordCallbackHandler implements CallbackHandler {
    private String userId;
    private String password;

    public UserIdAndPasswordCallbackHandler(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public void handle(Callback[] callbacks) throws java.io.IOException, UnsupportedCallbackException {
        for (int i = 0; i < callbacks.length; i++) {
            if (callbacks[i] instanceof NameCallback) {
                ((NameCallback)callbacks[i]).setName(userId);
            }
            if (callbacks[i] instanceof PasswordCallback) {
                ((PasswordCallback)callbacks[i]).setPassword(password.toCharArray());
            }
        }
    }
}
