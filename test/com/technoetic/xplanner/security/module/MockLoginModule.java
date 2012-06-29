package com.technoetic.xplanner.security.module;

import java.util.Map;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;

import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.LoginModule;

public class MockLoginModule implements LoginModule {
    public Map options;
    private String name = null;

    public MockLoginModule() {
    }

    public boolean authenticateCalled;
    public String authenticateUserId;
    public String authenticatePassword;
    public Subject authenticateReturn;
    public AuthenticationException authenticateException;

    public Subject authenticate(String userId, String password) throws AuthenticationException {
        authenticateCalled = true;
        authenticateUserId = userId;
        authenticatePassword = password;
        if (authenticateException != null) {
            throw authenticateException;
        }
        return authenticateReturn;
    }

    public Boolean isCapableOfChangingPasswordsReturn;

    public boolean isCapableOfChangingPasswords() {
        return isCapableOfChangingPasswordsReturn.booleanValue();
    }

    public boolean changePasswordCalled;
    public String changePasswordUserId;
    public String changePasswordPassword;

    public void changePassword(String userId, String password)
            throws AuthenticationException {
        changePasswordCalled = true;
        changePasswordUserId = userId;
        changePasswordPassword = password;
    }

    public boolean logoutCalled;
    public HttpServletRequest logoutRequest;

    public void logout(HttpServletRequest request) throws AuthenticationException {
        logoutCalled = true;
        logoutRequest = request;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

   public void setOptions(Map options) {
      this.options = options;
   }
}
