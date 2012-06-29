package com.technoetic.xplanner.filters;

import javax.servlet.http.HttpServletRequest;

public class ThreadServletRequest {
    private static ThreadLocal request = new ThreadLocal();

    public static HttpServletRequest get() {
        return (HttpServletRequest)request.get();
    }

    public static void set(HttpServletRequest request) {
        ThreadServletRequest.request.set(request);
    }
}
