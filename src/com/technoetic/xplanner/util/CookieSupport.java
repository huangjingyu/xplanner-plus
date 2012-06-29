package com.technoetic.xplanner.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: Mateusz Prokopowicz
 * Date: Jun 13, 2005
 * Time: 12:15:00 PM
 */
public class CookieSupport
{
   public static void createCookie(String name, String value, HttpServletResponse response) {
       Cookie cookie = new Cookie(name, value);
       cookie.setMaxAge(Integer.MAX_VALUE);
       response.addCookie(cookie);
   }

   public static void deleteCookie(String name, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

   public static Cookie getCookie(String name, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }
}
