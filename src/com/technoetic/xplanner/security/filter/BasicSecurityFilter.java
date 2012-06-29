package com.technoetic.xplanner.security.filter;

import java.io.IOException;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.Authenticator;
import com.technoetic.xplanner.security.util.Base64;
import com.technoetic.xplanner.util.MainBeanFactory;

public class BasicSecurityFilter extends AbstractSecurityFilter {
    private Authenticator authenticator;
    private String BASIC_PREFIX = "Basic ";

    public BasicSecurityFilter() {
    }

    public BasicSecurityFilter(Authenticator authenticator) {
        this.authenticator = authenticator;
    }

    protected void doInit(FilterConfig filterConfig) throws ServletException {
       authenticator = (Authenticator) WebApplicationContextUtils.getRequiredWebApplicationContext(filterConfig.getServletContext()).getBean("authenticator");
    }

    protected boolean isAuthenticated(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        return isSubjectInSession(request) || isChallengeAuthenticated(request);
    }

    private boolean isChallengeAuthenticated(HttpServletRequest request) throws ServletException {
        boolean isAuthenticated = false;
        String credentials = request.getHeader("Authorization");
        if (credentials != null && credentials.startsWith(BASIC_PREFIX)) {
            credentials = credentials.substring(BASIC_PREFIX.length());
            if (credentials != null) {
                String[] userIdAndPassword = new String(Base64.decode(credentials.getBytes())).split(":");
                try {
                    authenticator.authenticate(request, userIdAndPassword[0], userIdAndPassword[1]);
                    isAuthenticated = true;
                } catch (AuthenticationException e) {
                    log.info("basic authentication failed: user=" + userIdAndPassword[0] + ", reason=" + e.getMessage());
                } catch (Exception e) {
                    throw new ServletException(e);
                }
            }
        }
        return isAuthenticated;
    }

    protected boolean onAuthenticationFailure(
            HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        challenge(response);
        return false;
    }

    private void challenge(HttpServletResponse response) {
        response.setHeader("WWW-Authenticate", "Basic realm=\"XPlanner\"");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
