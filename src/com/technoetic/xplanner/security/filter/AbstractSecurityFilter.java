package com.technoetic.xplanner.security.filter;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.security.config.SecurityConfiguration;

public abstract class AbstractSecurityFilter implements Filter {
    protected Logger log = Logger.getLogger(getClass());
    private SecurityConfiguration securityConfiguration;

    public final void init(FilterConfig filterConfig) throws ServletException {
        try {
            doInit(filterConfig);
            String filename = filterConfig.getInitParameter("securityConfiguration");
            // do-before-release unit test for null filename
            if (filename != null) {
                InputStream configurationStream = filterConfig.getServletContext().getResourceAsStream(filename);
                if (configurationStream == null) {
                    throw new ServletException("could not load security configuration: " + filename);
                }
                securityConfiguration = SecurityConfiguration.load(configurationStream);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    protected abstract void doInit(FilterConfig filterConfig) throws ServletException;

    public final void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        boolean continueFilterChain = true;
        if (isSecureRequest(request) && !isAuthenticated(request, response)) {
            continueFilterChain = onAuthenticationFailure(request, response);
        }
        if (continueFilterChain) {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    // do-before-release unit test
    private boolean isSecureRequest(HttpServletRequest request) {
        return securityConfiguration == null || securityConfiguration.isSecureRequest(request);
    }

    protected abstract boolean isAuthenticated(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;

    protected abstract boolean onAuthenticationFailure(
            HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;


    public void destroy() {
        // empty
    }

    public void setSecurityConfiguration(SecurityConfiguration securityConfiguration) {
        this.securityConfiguration = securityConfiguration;
    }

    protected boolean isSubjectInSession(HttpServletRequest request) {
        return SecurityHelper.isUserAuthenticated(request);
    }
}

