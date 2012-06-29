package com.technoetic.xplanner.security.filter;

import java.io.IOException;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.Authenticator;
import com.technoetic.xplanner.security.CredentialCookie;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.util.MainBeanFactory;

public class FormSecurityFilter extends AbstractSecurityFilter {
    private String authenticatorUrl;
    public final String AUTHENTICATION_URL_KEY = "authenticatorUrl";
    private Authenticator authenticator;

    public FormSecurityFilter() { }

   protected void doInit(FilterConfig filterConfig) throws ServletException {
        authenticator = (Authenticator) WebApplicationContextUtils.getRequiredWebApplicationContext(filterConfig.getServletContext()).getBean("authenticator");
        authenticatorUrl = getInitParameter(filterConfig, AUTHENTICATION_URL_KEY);
    }

    private String getInitParameter(FilterConfig filterConfig, String parameterName)
            throws ServletException {
        String value = filterConfig.getInitParameter(parameterName);
        if (StringUtils.isEmpty(value)) {
            throw new ServletException(getClass().getName() + ": " + parameterName + " is required");
        }
        return value;
    }

    public void setAuthenticatorUrl(String authenticatorUrl) {
        this.authenticatorUrl = authenticatorUrl;
    }

    protected boolean isAuthenticated(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean isAuthenticated = false;
        CredentialCookie credentials = new CredentialCookie(request, response);
        if (!isSubjectInSession(request)) {
            if (credentials.isPresent()) {
                try {
                    authenticator.authenticate(request, credentials.getUserId(), credentials.getPassword());
                    isAuthenticated = true;
                } catch (AuthenticationException e) {
                    log.info("cookie-based authentication failed: user=" + credentials.getUserId() +
                             ", reason=" + e.getMessage());
                } catch (Exception e) {
                    throw new ServletException(e);
                }
            }
        } else {
            isAuthenticated = true;
        }
        return isAuthenticated;
    }

    public boolean onAuthenticationFailure(HttpServletRequest request,
                                           HttpServletResponse response) throws ServletException, IOException {
        CredentialCookie credentials = new CredentialCookie(request, response);
        if (credentials.isPresent()) {
            credentials.remove();
        }
        if (request.getMethod().equals("GET")) {
            SecurityHelper.saveUrl(request);
        }
        String redirectUrl = authenticatorUrl;
        log.debug(request.getRequestURL() + " being redirected to " + redirectUrl);
        response.sendRedirect(request.getContextPath() + redirectUrl);
        return false;
    }
}

