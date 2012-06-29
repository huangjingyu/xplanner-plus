package com.technoetic.xplanner.security;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.technoetic.xplanner.security.util.Base64;
import com.technoetic.xplanner.util.CookieSupport;

public class CredentialCookie {
    private static final String USERID_COOKIE_NAME = "userid";
    private static final String PASSWORD_COOKIE_NAME = "password";
    private HttpServletResponse response;
    private Cookie userId;
    private Cookie password;

    public CredentialCookie(HttpServletRequest request, HttpServletResponse response) {
        this.response = response;
        userId = CookieSupport.getCookie(USERID_COOKIE_NAME, request);
        password = CookieSupport.getCookie(PASSWORD_COOKIE_NAME, request);
    }

    public void set(String userId, String password) {
        CookieSupport.createCookie(USERID_COOKIE_NAME, userId, response);
        CookieSupport.createCookie(PASSWORD_COOKIE_NAME, new String(Base64.encode(password.getBytes())), response);
    }

    public void remove() {
        CookieSupport.deleteCookie(USERID_COOKIE_NAME, response);
        CookieSupport.deleteCookie(PASSWORD_COOKIE_NAME, response);
    }

    public boolean isPresent() {
        return userId != null && password != null;
    }

    public String getUserId() {
        return userId != null ? userId.getValue() : null;
    }

    public String getPassword() {
        return password != null
                ? new String(Base64.decode(password.getValue().getBytes()))
                : null;
    }

}
