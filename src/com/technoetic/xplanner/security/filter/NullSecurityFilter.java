package com.technoetic.xplanner.security.filter;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.security.auth.Subject;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.domain.Person;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.security.PersonPrincipal;
import com.technoetic.xplanner.security.SecurityHelper;

public class NullSecurityFilter extends AbstractSecurityFilter {
    public final String DEFAULT_USERID_KEY = "defaultUserId";
    public final String AUTHENTICATION_URL_KEY = "authenticatorUrl";
    private String authenticatorUrl;
    private String defaultUserId;

    @Override
	protected void doInit(FilterConfig filterConfig) throws ServletException {
        authenticatorUrl = getInitParameter(filterConfig, AUTHENTICATION_URL_KEY, null);
        defaultUserId = getInitParameter(filterConfig, DEFAULT_USERID_KEY, "sysadmin");
    }

    private String getInitParameter(FilterConfig filterConfig, String parameterName, String defaultValue)
            throws ServletException {
        String value = filterConfig.getInitParameter(parameterName);
        if (StringUtils.isEmpty(value)) {
            if (defaultValue == null) {
                throw new ServletException(getClass().getName() + ": " + parameterName + " is required");
            } else {
                value = defaultValue;
            }
        }
        return value;
    }

    public void setAuthenticatorUrl(String authenticatorUrl) {
        this.authenticatorUrl = authenticatorUrl;
    }

    public void setDefaultUserId(String defaultUserId) {
        this.defaultUserId = defaultUserId;
    }

    @Override
	protected boolean isAuthenticated(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // This could also be implemented in a special login module
        if (!isSubjectInSession(request)) {
            try {
                Person person = getPerson(ThreadSession.get(), defaultUserId);
                Subject subject = new Subject();
                subject.getPrincipals().add(new PersonPrincipal(person));
                SecurityHelper.setSubject(request, subject);
            } catch (HibernateException e) {
                throw new ServletException(e);
            }
        }
        return true;
    }

    // todo - these methods should be moved to an object repository
    private Person getPerson(Session session, String userId) throws HibernateException {
        List people = session.find("from person in class " +
                Person.class.getName() + " where userid = ?",
                userId, Hibernate.STRING);
        Person person = null;
        Iterator peopleIterator = people.iterator();
        if (peopleIterator.hasNext()) {
            person = (Person)peopleIterator.next();
        }
        return person;
    }

    @Override
	public boolean onAuthenticationFailure(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        // Should never happen
        log.error("default authentication failed!");
        String redirectUrl = authenticatorUrl;
        log.debug(request.getRequestURL() + " being redirected to " + redirectUrl);
        response.sendRedirect(request.getContextPath() + redirectUrl);
        return false;
    }
}

