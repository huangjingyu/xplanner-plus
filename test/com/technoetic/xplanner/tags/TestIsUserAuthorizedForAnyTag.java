package com.technoetic.xplanner.tags;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.tagext.Tag;

import net.sf.xplanner.domain.Project;

import com.technoetic.mocks.hibernate.MockSessionFactory;
import com.technoetic.xplanner.AbstractRequestUnitTestCase;
import com.technoetic.xplanner.XPlannerTestSupport;
import com.technoetic.xplanner.db.hibernate.GlobalSessionFactory;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.security.auth.MockAuthorizer;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;
import static org.easymock.EasyMock.*;

public class TestIsUserAuthorizedForAnyTag extends AbstractRequestUnitTestCase {
    private IsUserAuthorizedForAnyTag tag;
    private MockAuthorizer mockAuthorizer;
    private MockSessionFactory mockSessionFactory;

    public static class MockObject {
        private ArrayList items = new ArrayList();

        public MockObject() {
            items.add(new Object());
            items.add(new Object());
        }

        public List getItems() {
            return items;
        }
    }

    protected void setUp() throws Exception {
        super.setUp();
        support = new XPlannerTestSupport();

        mockAuthorizer = new MockAuthorizer();
        GlobalSessionFactory.set(mockSessionFactory);
        SystemAuthorizer.set(mockAuthorizer);
        tag = new IsUserAuthorizedForAnyTag();
        tag.setPageContext(pageContext);
    }

    public void testWhenAuthorized() throws Exception {
    	setUpDomainContext(10);
    	expect(pageContext.findAttribute("object")).andReturn(new MockObject());
    	replay();
        mockAuthorizer.hasPermission2Returns = new Boolean[]{Boolean.FALSE, Boolean.TRUE};
        tag.setName("object");
        tag.setProperty("items");
        tag.setPermissions("x,y");
        int result = tag.doStartTag();
        assertEquals("wrong tag result", Tag.EVAL_BODY_INCLUDE, result);
        verify();
    }

    public void testWhenAuthorizedWithCollection() throws Exception {
    	setUpDomainContext(10);
    	replay();
        mockAuthorizer.hasPermission2Returns = new Boolean[]{Boolean.FALSE, Boolean.TRUE};
        tag.setCollection(new MockObject().getItems());
        tag.setPermissions("x,y");

        int result = tag.doStartTag();

        assertEquals("wrong tag result", Tag.EVAL_BODY_INCLUDE, result);
        verify();
    }

    public void testGetProjectIdFromContext() throws Exception {
    	setUpDomainContext(44);
    	replay();
        mockAuthorizer.hasPermission2Returns = new Boolean[]{Boolean.TRUE};
        tag.setCollection(new MockObject().getItems());
        tag.setPermissions("x,y");

        tag.doStartTag();

        assertEquals("wrong project id", 44, mockAuthorizer.hasPermission2ProjectId);
        verify();
    }

    public void testGetProjectIdFromResource() throws Exception {
    	expect(request.getAttribute(DomainContext.REQUEST_KEY)).andReturn(null);
    	expect(request.getParameter("projectId")).andReturn(null);
        replay();
        Project project = new Project();
        project.setId(55);
        ArrayList items = new ArrayList();
        items.add(project);
        mockAuthorizer.hasPermission2Returns = new Boolean[]{Boolean.TRUE};
        tag.setCollection(items);
        tag.setPermissions("x,y");

        tag.doStartTag();

        assertEquals("wrong project id", 55, mockAuthorizer.hasPermission2ProjectId);
        verify();
    }

    public void testGetProjectIdFromRequestParameter() throws Exception {
    	expect(request.getAttribute(DomainContext.REQUEST_KEY)).andReturn(null);
    	expect(request.getParameter("projectId")).andReturn("66");
    	replay();
        mockAuthorizer.hasPermission2Returns = new Boolean[]{Boolean.TRUE};
        tag.setCollection(new MockObject().getItems());
        tag.setPermissions("x,y");

        tag.doStartTag();

        assertEquals("wrong project id", 66, mockAuthorizer.hasPermission2ProjectId);
        verify();
    }

    public void testWhenNotAuthorized() throws Exception {
    	setUpDomainContext(10);
    	expect(pageContext.findAttribute("object")).andReturn(new MockObject());
    	replay();
        mockAuthorizer.hasPermission2Returns = new Boolean[]{
            Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE};
        support.pageContext.setAttribute("object", new MockObject());
        tag.setName("object");
        tag.setProperty("items");
        tag.setPermissions("x,y");

        int result = tag.doStartTag();

        assertEquals("wrong tag result", Tag.SKIP_BODY, result);
        verify();
    }
}
