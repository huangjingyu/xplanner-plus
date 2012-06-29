package com.technoetic.xplanner.tags;

import java.util.List;

import javax.servlet.jsp.JspException;

import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.auth.Authorizer;

public abstract class OptionsTag extends org.apache.struts.taglib.html.OptionsTag {
    public final String DEFAULT_LABEL_PROPERTY = "name";
    public final String DEFAULT_PROPERTY = "id";
    protected ContextInitiator contextInitiator;

    public int doStartTag() throws JspException {
        createContextInitiator();
        return super.doStartTag();
    }

    private void createContextInitiator()
    {
        contextInitiator = new ContextInitiator(pageContext);
        contextInitiator.initStaticContext();
    }

    public int doEndTag() throws JspException {
        try {
            List selectedBeans = getOptions();

            if (getProperty() == null) {
                setProperty(DEFAULT_PROPERTY);
            }
            if (getLabelProperty() == null) {
                setLabelProperty(DEFAULT_LABEL_PROPERTY);
            }
            pageContext.setAttribute(ContextInitiator.COLLECTION_KEY, selectedBeans);
            setCollection(ContextInitiator.COLLECTION_KEY);
            return super.doEndTag();
        } catch (JspException e) {
            throw e;
        } catch (Exception e) {
            throw new JspException(e);
        }
    }

    protected abstract List getOptions() throws HibernateException, AuthenticationException;

    public Session getSession() {
        return contextInitiator.getSession();
    }

    public Authorizer getAuthorizer() {
        return contextInitiator.getAuthorizer();
    }

    public int getLoggedInUserId() {
        return contextInitiator.getLoggedInUserId();
    }

    public void setSession(Session session) {
        if (contextInitiator == null) createContextInitiator();
        contextInitiator.setSession(session);
    }

    public void setAuthorizer(Authorizer authorizer) {
        if (contextInitiator == null) createContextInitiator();
        contextInitiator.setAuthorizer(authorizer);
    }

    public void setLoggedInUserId(int loggedInUserId) {
        if (contextInitiator == null) createContextInitiator();
        contextInitiator.setLoggedInUserId(loggedInUserId);
    }
}