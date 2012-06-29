package com.technoetic.mocks.servlets.jsp;

import java.io.IOException;
import java.util.HashMap;

import javax.el.ELContext;
import javax.servlet.ServletException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;

public class MockPageContext extends PageContext {

    public HashMap pageAttributes = new HashMap();

    public boolean initializeCalled;
    public java.io.IOException initializeIOException;
    public java.lang.IllegalStateException initializeIllegalStateException;
    public java.lang.IllegalArgumentException initializeIllegalArgumentException;
    public javax.servlet.Servlet initializeServlet;
    public javax.servlet.ServletRequest initializeRequest;
    public javax.servlet.ServletResponse initializeResponse;
    public java.lang.String initializeErrorPageURL;
    public boolean initializeNeedsSession;
    public int initializeBufferSize;
    public boolean initializeAutoFlush;

    @Override
	public void initialize(javax.servlet.Servlet servlet, javax.servlet.ServletRequest request,
            javax.servlet.ServletResponse response, java.lang.String errorPageURL,
            boolean needsSession, int bufferSize, boolean autoFlush)
            throws java.io.IOException,
            java.lang.IllegalStateException,
            java.lang.IllegalArgumentException {
        initializeCalled = true;
        initializeServlet = servlet;
        initializeRequest = request;
        initializeResponse = response;
        initializeErrorPageURL = errorPageURL;
        initializeNeedsSession = needsSession;
        initializeBufferSize = bufferSize;
        initializeAutoFlush = autoFlush;
        if (initializeIOException != null) {
            throw initializeIOException;
        }
        if (initializeIllegalStateException != null) {
            throw initializeIllegalStateException;
        }
        if (initializeIllegalArgumentException != null) {
            throw initializeIllegalArgumentException;
        }
    }

    public boolean releaseCalled;

    @Override
	public void release() {
        releaseCalled = true;
    }

    public boolean setAttributeCalled;
    public java.lang.String setAttributeName;
    public java.lang.Object setAttributeAttribute;

    @Override
	public void setAttribute(java.lang.String name, java.lang.Object attribute) {
        setAttributeCalled = true;
        setAttributeName = name;
        setAttributeAttribute = attribute;
        pageAttributes.put(name, attribute);
    }

    public boolean setAttribute2Called;
    public String setAttribute2Name;
    public java.lang.Object setAttribute2Attribute;
    public int setAttribute2Scope;

    @Override
	public void setAttribute(java.lang.String name, java.lang.Object o, int scope) {
        setAttribute2Called = true;
        setAttribute2Name = name;
        setAttribute2Attribute = o;
        setAttribute2Scope = scope;
    }

    public boolean getAttributeCalled;
    public java.lang.Object getAttributeReturn;
    public java.lang.String getAttributeName;

    @Override
	public java.lang.Object getAttribute(java.lang.String name) {
        getAttributeCalled = true;
        getAttributeName = name;
        if (getAttributeReturn != null) {
            return getAttributeReturn;
        } else {
            return pageAttributes.get(name);
        }
    }

    public boolean getAttribute2Called;
    public java.lang.Object getAttribute2Return;
    public java.lang.String getAttribute2Name;
    public int getAttribute2Scope;

    @Override
	public java.lang.Object getAttribute(java.lang.String name, int scope) {
        getAttribute2Called = true;
        getAttribute2Name = name;
        getAttribute2Scope = scope;
        return getAttribute2Return;
    }

    public boolean findAttributeCalled;
    public java.lang.Object findAttributeReturn;
    public java.lang.String findAttributeName;

    @Override
	public java.lang.Object findAttribute(java.lang.String name) {
        findAttributeCalled = true;
        findAttributeName = name;
        if (findAttributeReturn != null) {
            return findAttributeReturn;
        } else {
            return pageAttributes.get(name);
        }
    }

    public boolean removeAttributeCalled;
    public java.lang.String removeAttributeName;

    @Override
	public void removeAttribute(java.lang.String name) {
        removeAttributeCalled = true;
        removeAttributeName = name;
    }

    public boolean removeAttribute2Called;
    public java.lang.String removeAttribute2Name;
    public int removeAttribute2Scope;

    @Override
	public void removeAttribute(java.lang.String name, int scope) {
        removeAttribute2Called = true;
        removeAttribute2Name = name;
        removeAttribute2Scope = scope;
    }

    public boolean getAttributesScopeCalled;
    public Integer getAttributesScopeReturn;
    public java.lang.String getAttributesScopeName;

    @Override
	public int getAttributesScope(java.lang.String name) {
        getAttributesScopeCalled = true;
        getAttributesScopeName = name;
        return getAttributesScopeReturn.intValue();
    }

    public boolean getAttributeNamesInScopeCalled;
    public java.util.Enumeration getAttributeNamesInScopeReturn;
    public int getAttributeNamesInScopeScope;

    @Override
	public java.util.Enumeration getAttributeNamesInScope(int scope) {
        getAttributeNamesInScopeCalled = true;
        getAttributeNamesInScopeScope = scope;
        return getAttributeNamesInScopeReturn;
    }

    public boolean getOutCalled;
    public javax.servlet.jsp.JspWriter getOutReturn;

    @Override
	public javax.servlet.jsp.JspWriter getOut() {
        getOutCalled = true;
        return getOutReturn;
    }

    @Override
	public ExpressionEvaluator getExpressionEvaluator() {
        return null;
    }

    @Override
	public VariableResolver getVariableResolver() {
        return null;
    }

    public boolean getSessionCalled;
    public javax.servlet.http.HttpSession getSessionReturn;

    @Override
	public javax.servlet.http.HttpSession getSession() {
        getSessionCalled = true;
        return getSessionReturn;
    }

    public boolean getPageCalled;
    public java.lang.Object getPageReturn;

    @Override
	public java.lang.Object getPage() {
        getPageCalled = true;
        return getPageReturn;
    }

    public boolean getRequestCalled;
    public javax.servlet.ServletRequest getRequestReturn;

    @Override
	public javax.servlet.ServletRequest getRequest() {
        getRequestCalled = true;
        return getRequestReturn;
    }

    public boolean getResponseCalled;
    public javax.servlet.ServletResponse getResponseReturn;

    @Override
	public javax.servlet.ServletResponse getResponse() {
        getResponseCalled = true;
        return getResponseReturn;
    }

    public boolean getExceptionCalled;
    public java.lang.Exception getExceptionReturn;

    @Override
	public java.lang.Exception getException() {
        getExceptionCalled = true;
        return getExceptionReturn;
    }

    public boolean getServletConfigCalled;
    public javax.servlet.ServletConfig getServletConfigReturn;

    @Override
	public javax.servlet.ServletConfig getServletConfig() {
        getServletConfigCalled = true;
        return getServletConfigReturn;
    }

    public boolean getServletContextCalled;
    public javax.servlet.ServletContext getServletContextReturn;

    @Override
	public javax.servlet.ServletContext getServletContext() {
        getServletContextCalled = true;
        return getServletContextReturn;
    }

    public boolean forwardCalled;
    public javax.servlet.ServletException forwardServletException;
    public java.io.IOException forwardIOException;
    public java.lang.String forwardRelativeUrlPath;

    @Override
	public void forward(java.lang.String relativeUrlPath) throws javax.servlet.ServletException, java.io.IOException {
        forwardCalled = true;
        forwardRelativeUrlPath = relativeUrlPath;
        if (forwardServletException != null) {
            throw forwardServletException;
        }
        if (forwardIOException != null) {
            throw forwardIOException;
        }
    }

    public boolean includeCalled;
    public javax.servlet.ServletException includeServletException;
    public java.io.IOException includeIOException;
    public java.lang.String includeRelativeUrlPath;

    @Override
	public void include(java.lang.String relativeUrlPath) throws javax.servlet.ServletException, java.io.IOException {
        includeCalled = true;
        includeRelativeUrlPath = relativeUrlPath;
        if (includeServletException != null) {
            throw includeServletException;
        }
        if (includeIOException != null) {
            throw includeIOException;
        }
    }

    @Override
	public void include(String string, boolean b) throws ServletException, IOException {

    }

    public boolean handlePageExceptionCalled;
    public javax.servlet.ServletException handlePageExceptionServletException;
    public java.io.IOException handlePageExceptionIOException;
    public java.lang.Exception handlePageExceptionE;

    @Override
	public void handlePageException(java.lang.Exception e) throws javax.servlet.ServletException, java.io.IOException {
        handlePageExceptionCalled = true;
        handlePageExceptionE = e;
        if (handlePageExceptionServletException != null) {
            throw handlePageExceptionServletException;
        }
        if (handlePageExceptionIOException != null) {
            throw handlePageExceptionIOException;
        }
    }

    public boolean pushBodyCalled;
    public javax.servlet.jsp.tagext.BodyContent pushBodyReturn;

    @Override
	public javax.servlet.jsp.tagext.BodyContent pushBody() {
        pushBodyCalled = true;
        return pushBodyReturn;
    }

    public boolean popBodyCalled;
    public javax.servlet.jsp.JspWriter popBodyReturn;

    @Override
	public javax.servlet.jsp.JspWriter popBody() {
        popBodyCalled = true;
        return popBodyReturn;
    }

    @Override
	public void handlePageException(Throwable ex) {
        throw new UnsupportedOperationException();
    }

	@Override
	public ELContext getELContext() {
		return null;
	}

}