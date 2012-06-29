package com.technoetic.xplanner.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.hibernate.classic.Session;

import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.security.auth.Authorizer;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;

public class ContextInitiator
{
    public static final String COLLECTION_KEY = "optionCollection";
    private PageContext pageContext;
    private Session session;
    private Authorizer authorizer;
    private int loggedInUserId;

    public ContextInitiator(PageContext pageContext)
    {
        this.pageContext = pageContext;
    }

    public void initStaticContext()
    {
        setSession(ThreadSession.get());
        setAuthorizer(SystemAuthorizer.get());
        try
        {
            setLoggedInUserId(getRemoteUserId());
        }
        catch (JspException e)
        {
            e.printStackTrace();
        }
    }

    private int getRemoteUserId() throws JspException {
        try {
            return SecurityHelper.getRemoteUserId(pageContext);
        }
        catch (AuthenticationException e) {
            throw new JspException(e);
        }
    }

    public Session getSession() {
        return session;
    }

    public Authorizer getAuthorizer() {
        return authorizer;
    }

    public int getLoggedInUserId() {
        return loggedInUserId;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void setAuthorizer(Authorizer authorizer) {
        this.authorizer = authorizer;
    }

    public void setLoggedInUserId(int loggedInUserId) {
        this.loggedInUserId = loggedInUserId;
    }
}
