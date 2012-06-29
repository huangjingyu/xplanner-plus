package com.technoetic.xplanner.actions;

import junit.framework.TestCase;

import org.apache.struts.action.ActionForward;

import com.technoetic.xplanner.XPlannerTestSupport;
import com.technoetic.xplanner.security.auth.MockAuthorizer;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;

public class TestDispatchForwardAction extends TestCase {
    private XPlannerTestSupport support;
    private DispatchForward action;
    private MockAuthorizer authorizer;

    public TestDispatchForwardAction(String testName) {
        super(testName);
    }

    public void setUp() throws Exception {
        support = new XPlannerTestSupport();
        support.setUpMockAppender();
        support.setForward("@secure", "false");
        authorizer = new MockAuthorizer();
        authorizer.hasPermissionReturns = new Boolean[]{Boolean.TRUE};
        action = new DispatchForward();
        action.setAuthorizer(authorizer);
    }

    protected void tearDown() throws Exception {
        SystemAuthorizer.set(null);
        support.tearDownMockAppender();
        super.tearDown();
    }

    public void testForwardUsingActionParameter() throws Exception {
        support.mapping.setParameter("view/projects");
        support.setForward("view/projects", "projects.jsp");

        ActionForward forward = support.executeAction(action);

        assertEquals("wrong forward", "projects.jsp", forward.getPath());
    }

    public void testSecureAccessWithoutProjectId() throws Exception {
        support.setForward("@secure", "true");
        support.setForward("security/notAuthorized", "AUTH_FAILED");

        ActionForward forward = support.executeAction(action);

        assertEquals("wrong forward", "AUTH_FAILED", forward.getPath());

    }

    public void testSecureAccess() throws Exception {
        support.request.setParameterValue("projectId", new String[]{"33"});
        support.setForward("@secure", "true");
        support.setForward("security/notAuthorized", "AUTH_FAILED");
        support.setUpSubject("user", new String[0]);
        support.setForward("forward", "AUTH_SUCCESS");
        support.mapping.setParameter("forward");

        ActionForward forward = support.executeAction(action);

        assertEquals("wrong forward", "AUTH_SUCCESS", forward.getPath());

    }

    public void testSecureAccessWithoutPermission() throws Exception {
        support.request.setParameterValue("projectId", new String[]{"33"});
        support.setForward("@secure", "true");
        support.setForward("security/notAuthorized", "AUTH_FAILED");
        support.setUpSubject("user", new String[0]);
        authorizer.hasPermissionReturns = new Boolean[]{Boolean.FALSE};

        ActionForward forward = support.executeAction(action);

        assertEquals("wrong forward", "AUTH_FAILED", forward.getPath());
    }
}

