package com.technoetic.mocks.servlets;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class MockFilterChain implements FilterChain {
    public boolean doFilterCalled;
    public ServletRequest doFilterServletRequest;
    public ServletResponse doFilterServletResponse;

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse)
            throws IOException, ServletException {
        doFilterCalled = true;
        doFilterServletRequest = servletRequest;
        doFilterServletResponse = servletResponse;
    }
}
