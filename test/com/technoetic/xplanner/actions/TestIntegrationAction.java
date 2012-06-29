package com.technoetic.xplanner.actions;

import java.util.ArrayList;
import java.util.Date;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.hibernate.HibernateException;

import com.technoetic.mocks.hibernate.MockSession;
import com.technoetic.mocks.hibernate.MockSessionFactory;
import com.technoetic.xplanner.db.hibernate.GlobalSessionFactory;
import com.technoetic.xplanner.db.hibernate.HibernateHelper;
import com.technoetic.xplanner.domain.Integration;

public class TestIntegrationAction extends AbstractActionTestCase {

    private MockSession mockSession;
    private MockSessionFactory mockSessionFactory;
    private MockIntegrationListener mockIntegrationListener;
    public static final int PROJECT_ID = 123;

    public void setUp() throws Exception {
       action = new IntegrationAction();
       super.setUp();

       support.setForward("display", "DISPLAY");
       support.setForward("error", "ERROR");
       mockSession = new MockSession();
       mockSession.connectionReturn = support.connection;
       mockSessionFactory = new MockSessionFactory();
       mockSessionFactory.openSessionReturn = mockSession;
       GlobalSessionFactory.set(mockSessionFactory);
       support.servletContext.setAttribute("xplanner.sessions", mockSessionFactory);

        mockIntegrationListener = new MockIntegrationListener();
        ArrayList listeners = new ArrayList();
        listeners.add(mockIntegrationListener);
        HibernateHelper.setSession(support.request, mockSession);
        support.request.setParameterValue("projectId", new String[]{Integer.toString(PROJECT_ID)});
        ((IntegrationAction)action).setIntegrationListeners(listeners);
    }

    public void tearDown() throws Exception {
       GlobalSessionFactory.set(null);
       super.tearDown();
    }

    public void testJoin() throws Exception {
        mockSession.find3Return = new ArrayList();
        mockSession.find3Return.add(new Integration());
        support.request.setParameterValue("action.join", new String[]{""});
        support.request.setParameterValue("personId", new String[]{"1"});
        support.request.setParameterValue("comment", new String[]{"a comment"});

        replay();

        ActionForward forward = support.executeAction(action);

        verify();
        assertEquals(addProjectId("DISPLAY"), forward.getPath());
        assertTrue("no integration saved", mockSession.saveCalled);
        Integration integration = (Integration)mockSession.saveObject;
        assertEquals(PROJECT_ID, integration.getProjectId());
        assertEquals(1, integration.getPersonId());
        assertEquals("a comment", integration.getComment());
        assertEquals(Integration.PENDING, integration.getState());
        assertNotNull(integration.getWhenRequested());
        assertNull(integration.getWhenStarted());
        assertNull(integration.getWhenComplete());

        assertCommit();
    }

    public void testJoinWhenNoLineAndNoActive() throws Exception {
        replay();
        mockSession.find3Returns = new ArrayList();
        mockSession.find3Returns.add(new ArrayList());
        mockSession.find3Returns.add(new ArrayList());
        support.request.setParameterValue("action.join", new String[]{""});
        support.request.setParameterValue("personId", new String[]{"1"});
        support.request.setParameterValue("comment", new String[]{"a comment"});

        ActionForward forward = support.executeAction(action);

        verify();
        assertEquals(addProjectId("DISPLAY"), forward.getPath());
        assertTrue("no integration saved", mockSession.saveCalled);
        Integration integration = (Integration)mockSession.saveObject;
        assertEquals(1, integration.getPersonId());
        assertEquals(PROJECT_ID, integration.getProjectId());
        assertEquals("a comment", integration.getComment());
        assertEquals(Integration.ACTIVE, integration.getState());
        assertNotNull(integration.getWhenRequested());
        assertNotNull(integration.getWhenStarted());
        assertNull(integration.getWhenComplete());

        assertCommit();
    }

    public void testJoinError() throws Exception {
        mockSession.saveHibernateException = new HibernateException("test");
        support.request.setParameterValue("action.join", new String[]{""});
        support.request.setParameterValue("personId", new String[]{"1"});
        support.request.setParameterValue("comment", new String[]{"a comment"});
        replay();

        ActionForward forward = support.executeAction(action);

        verify();
        assertEquals("ERROR", forward.getPath());
        assertRollback();
    }

    public void testJoinErrorNoPersonId() throws Exception {
        support.request.setParameterValue("action.join", new String[]{""});
        support.request.setParameterValue("personId", new String[]{""});
        support.request.setParameterValue("comment", new String[]{"a comment"});
        replay();

        ActionForward forward = support.executeAction(action);

        verify();
        assertEquals(addProjectId("ERROR"), forward.getPath());
        assertFalse("session accessed", mockSession.find3Called);
        assertFalse("session accessed", mockSession.saveCalled);
    }

    public void testJoinErrorZeroPersonId() throws Exception {
        support.request.setParameterValue("action.join", new String[]{""});
        support.request.setParameterValue("personId", new String[]{"0"});
        support.request.setParameterValue("comment", new String[]{"a comment"});
        replay();

        ActionForward forward = support.executeAction(action);

        verify();
        assertEquals(addProjectId("ERROR"), forward.getPath());
        assertFalse("session accessed", mockSession.find3Called);
        assertFalse("session accessed", mockSession.saveCalled);
    }

    public void testLeaveWhenNotFirst() throws Exception {
        Integration integration1 = new Integration();
        integration1.setId(400);
        integration1.setState(Integration.PENDING);
        Integration integration2 = new Integration();
        integration2.setId(234);
        integration2.setState(Integration.PENDING);
        ArrayList results1 = new ArrayList();
        results1.add(integration1);
        results1.add(integration2);
        ArrayList results2 = new ArrayList();
        results2.add(integration1);
        mockSession.find3Returns = new ArrayList();
        mockSession.find3Returns.add(results1);
        mockSession.find3Returns.add(results2);
        Integration integration = new Integration();
        mockSession.loadReturn = integration;
        support.request.setParameterValue("action.leave.234", new String[]{""});
        replay();

        ActionForward forward = support.executeAction(action);

        verify();
        assertEquals(addProjectId("DISPLAY"), forward.getPath());
        assertTrue("integration not loaded", mockSession.loadCalled);
        assertEquals(Integration.class, mockSession.loadTheClass);
        assertEquals(new Integer(234), mockSession.loadId);
        assertTrue(mockSession.deleteCalled);
        assertSame(mockSession.deleteObject, integration);
        assertFalse("listeners notified", mockIntegrationListener.onEventCalled);
        assertCommit();
    }

    public void testLeaveWhenFirst() throws Exception {
        Integration integration1 = new Integration();
        integration1.setId(234);
        integration1.setState(Integration.PENDING);
        Integration integration2 = new Integration();
        integration2.setId(400);
        integration2.setState(Integration.PENDING);
        ArrayList results1 = new ArrayList();
        results1.add(integration1);
        results1.add(integration2);
        ArrayList results2 = new ArrayList();
        results2.add(integration2);
        mockSession.find3Returns = new ArrayList();
        mockSession.find3Returns.add(results1);
        mockSession.find3Returns.add(results2);
        Integration integration = new Integration();
        integration.setId(234);
        mockSession.loadReturn = integration;
        support.request.setParameterValue("action.leave.234", new String[]{""});
        replay();

        ActionForward forward = support.executeAction(action);

        verify();
        assertEquals(addProjectId("DISPLAY"), forward.getPath());
        assertTrue("integration not loaded", mockSession.loadCalled);
        assertEquals(Integration.class, mockSession.loadTheClass);
        assertEquals(new Integer(234), mockSession.loadId);
        assertTrue(mockSession.deleteCalled);
        assertSame(mockSession.deleteObject, integration);
        assertTrue("listeners not notified", mockIntegrationListener.onEventCalled);
        assertEquals(IntegrationListener.INTEGRATION_READY_EVENT, mockIntegrationListener.onEventEventType);
        assertEquals(integration2, mockIntegrationListener.onEventIntegration);
        assertCommit();
    }

    public void testLeaveWhenOnly() throws Exception {
        Integration integration1 = new Integration();
        integration1.setId(234);
        integration1.setState(Integration.PENDING);
        ArrayList results1 = new ArrayList();
        results1.add(integration1);
        ArrayList results2 = new ArrayList();
        mockSession.find3Returns = new ArrayList();
        mockSession.find3Returns.add(results1);
        mockSession.find3Returns.add(results2);
        Integration integration = new Integration();
        integration.setId(234);
        mockSession.loadReturn = integration;
        support.request.setParameterValue("action.leave.234", new String[]{""});
        replay();

        ActionForward forward = support.executeAction(action);

        verify();
        assertEquals(addProjectId("DISPLAY"), forward.getPath());
        assertTrue("integration not loaded", mockSession.loadCalled);
        assertEquals(Integration.class, mockSession.loadTheClass);
        assertEquals(new Integer(234), mockSession.loadId);
        assertTrue(mockSession.deleteCalled);
        assertSame(mockSession.deleteObject, integration);
        assertFalse("listeners notified", mockIntegrationListener.onEventCalled);
        assertCommit();
    }

    public void testLeaveError() throws Exception {
        mockSession.loadHibernateException = new HibernateException("test");
        support.request.setParameterValue("action.leave", new String[]{""});
        support.request.setParameterValue("oid", new String[]{"1"});
        replay();

        ActionForward forward = support.executeAction(action);

        verify();
        assertEquals("ERROR", forward.getPath());
        assertRollback();
    }

    public void testStart() throws Exception {
        ArrayList activeIntegrations = new ArrayList();
        Integration integration1 = new Integration();
        integration1.setState(Integration.PENDING);
        Integration integration2 = new Integration();
        integration2.setState(Integration.PENDING);
        ArrayList pendingIntegrations = new ArrayList();
        mockSession.find3Return = new ArrayList();
        pendingIntegrations.add(integration1);
        pendingIntegrations.add(integration2);
        mockSession.find3Returns = new ArrayList();
        mockSession.find3Returns.add(activeIntegrations);
        mockSession.find3Returns.add(pendingIntegrations);
        support.request.setParameterValue("action.start", new String[]{""});
        replay();

        ActionForward forward = support.executeAction(action);

        verify();
        assertEquals(addProjectId("DISPLAY"), forward.getPath());
        assertTrue("find not called", mockSession.find3Called);
        assertEquals(Integration.ACTIVE, integration1.getState());
        assertNotNull(integration1.getWhenStarted());
        assertEquals(Integration.PENDING, integration2.getState());
        assertNull(integration2.getWhenStarted());
        assertCommit();
    }

    /**
     * This test makes sure that XPlanner does not start duplicate
     * integrations. This could happen if someone has not refreshed their
     * browser window.
     * @throws Exception
     */
    public void testStartWhenAlreadyStarted() throws Exception {
        Integration integration0 = new Integration();
        integration0.setState(Integration.ACTIVE);
        ArrayList activeIntegrations = new ArrayList();
        activeIntegrations.add(integration0);
        Integration integration1 = new Integration();
        integration1.setState(Integration.PENDING);
        Integration integration2 = new Integration();
        integration2.setState(Integration.PENDING);
        ArrayList pendingIntegrations = new ArrayList();
        mockSession.find3Return = new ArrayList();
        pendingIntegrations.add(integration1);
        pendingIntegrations.add(integration2);
        mockSession.find3Returns = new ArrayList();
        mockSession.find3Returns.add(activeIntegrations);
        mockSession.find3Returns.add(pendingIntegrations);
        support.request.setParameterValue("action.start", new String[]{""});
        replay();

        ActionForward forward = support.executeAction(action);

        verify();
        assertEquals(addProjectId("ERROR"), forward.getPath());
        ActionErrors errors = (ActionErrors)support.request.getAttribute(Globals.ERROR_KEY);
        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertEquals("integrations.error.alreadyactive", ((ActionError)errors.get().next()).getKey());
        assertEquals(Integration.ACTIVE, integration0.getState());
        assertEquals(Integration.PENDING, integration1.getState());
        assertNull(integration1.getWhenStarted());
        assertEquals(Integration.PENDING, integration2.getState());
        assertNull(integration2.getWhenStarted());
        assertCommit();
    }

    public void testStartError() throws Exception {
        mockSession.findHibernateException = new HibernateException("test");
        support.request.setParameterValue("action.start", new String[]{""});
        replay();

        ActionForward forward = support.executeAction(action);

        verify();
        assertEquals("ERROR", forward.getPath());
        assertRollback();
    }

    public void testFinish() throws Exception {
        Integration integration1 = new Integration();
        integration1.setState(Integration.ACTIVE);
        integration1.setWhenStarted(new Date());
        Integration integration2 = new Integration();
        ArrayList results1 = new ArrayList();
        results1.add(integration1);
        ArrayList results2 = new ArrayList();
        results2.add(integration2);
        mockSession.find3Returns = new ArrayList();
        mockSession.find3Returns.add(results1);
        mockSession.find3Returns.add(results2);
        support.request.setParameterValue("action.finish", new String[]{""});
        replay();

        ActionForward forward = support.executeAction(action);

        verify();
        assertEquals(addProjectId("DISPLAY"), forward.getPath());
        assertTrue("find not called", mockSession.find3Called);
        assertEquals(Integration.FINISHED, integration1.getState());
        assertNotNull(integration1.getWhenStarted());
        assertNotNull(integration1.getWhenComplete());
        assertCommit();
        assertTrue("listeners not notified", mockIntegrationListener.onEventCalled);
        assertEquals(IntegrationListener.INTEGRATION_READY_EVENT, mockIntegrationListener.onEventEventType);
        assertEquals(integration2, mockIntegrationListener.onEventIntegration);
    }

    public void testFinishError() throws Exception {
        mockSession.findHibernateException = new HibernateException("test");
        support.request.setParameterValue("action.finish", new String[]{""});
        replay();

        ActionForward forward = support.executeAction(action);

        verify();
        assertEquals("ERROR", forward.getPath());
        assertRollback();
    }

    public void testCancel() throws Exception {
        Integration integration1 = new Integration();
        integration1.setState(Integration.ACTIVE);
        integration1.setWhenStarted(new Date());
        Integration integration2 = new Integration();
        ArrayList results1 = new ArrayList();
        results1.add(integration1);
        ArrayList results2 = new ArrayList();
        results2.add(integration2);
        mockSession.find3Returns = new ArrayList();
        mockSession.find3Returns.add(results1);
        mockSession.find3Returns.add(results2);
        support.request.setParameterValue("action.cancel", new String[]{""});
        replay();

        ActionForward forward = support.executeAction(action);

        verify();
        assertEquals(addProjectId("DISPLAY"), forward.getPath());
        assertTrue("find not called", mockSession.find3Called);
        assertEquals(Integration.CANCELED, integration1.getState());
        assertNotNull(integration1.getWhenStarted());
        assertNotNull(integration1.getWhenComplete());
        assertCommit();
        assertTrue("listeners not notified", mockIntegrationListener.onEventCalled);
        assertEquals(IntegrationListener.INTEGRATION_READY_EVENT, mockIntegrationListener.onEventEventType);
        assertEquals(integration2, mockIntegrationListener.onEventIntegration);
    }

    private String addProjectId(String s) {
        return s+"?projectId=123";
    }

    public void testCancelError() throws Exception {
        mockSession.findHibernateException = new HibernateException("test");
        support.request.setParameterValue("action.cancel", new String[]{""});
        replay();

        ActionForward forward = support.executeAction(action);

        verify();
        assertEquals("ERROR", forward.getPath());
        assertRollback();
    }

    private void assertRollback() {
        assertTrue(support.connection.rollbackCalled);
    }

    private void assertCommit() {
        assertTrue(mockSession.flushCalled);
        assertTrue(support.connection.commitCalled);
    }
}

