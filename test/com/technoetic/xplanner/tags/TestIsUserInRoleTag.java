package com.technoetic.xplanner.tags;

import javax.servlet.jsp.tagext.Tag;

import junit.framework.TestCase;

import com.technoetic.xplanner.XPlannerTestSupport;

public class TestIsUserInRoleTag extends TestCase {
    private IsUserInRoleTag tag;
    private XPlannerTestSupport support;

    public TestIsUserInRoleTag(String s) {
        super(s);
    }

    protected void setUp() throws Exception {
        super.setUp();
        support = new XPlannerTestSupport();
        tag = new IsUserInRoleTag();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testRoleMatch() throws Exception {
        support.setUpSubjectInRole("viewer");
        tag.setPageContext(support.pageContext);
        tag.setRole("viewer");

        int result = tag.doStartTag();

        assertEquals("wrong result", Tag.EVAL_BODY_INCLUDE, result);

    }

    public void testRoleNonmatch() throws Exception {
        support.setUpSubjectInRole("bogus");
        tag.setPageContext(support.pageContext);
        tag.setRole("viewer");

        int result = tag.doStartTag();

        assertEquals("wrong result", Tag.SKIP_BODY, result);
    }

    public void testRoleAndUserIdMatch() throws Exception {
        support.setUpSubject("foo", new String[]{"viewer"});
        tag.setPageContext(support.pageContext);
        tag.setRole("viewer");
        tag.setUserid("foo");

        int result = tag.doStartTag();

        assertEquals("wrong result", Tag.EVAL_BODY_INCLUDE, result);
    }

    public void testRoleMatchAndUserIdNonmatch() throws Exception {
        support.setUpSubject("foo", new String[]{"viewer"});
        tag.setPageContext(support.pageContext);
        tag.setRole("viewer");
        tag.setUserid("bar");

        int result = tag.doStartTag();

        assertEquals("wrong result", Tag.SKIP_BODY, result);
    }

    public void testRoleMatchAndUserIdMatchAndAdmin() throws Exception {
        support.setUpSubject("bar", new String[]{"viewer"});
        tag.setPageContext(support.pageContext);
        tag.setRole("viewer");
        tag.setUserid("bar");
        tag.setAdminRole("admin");

        int result = tag.doStartTag();

        assertEquals("wrong result", Tag.EVAL_BODY_INCLUDE, result);
    }

    public void testRoleNonmatchAndUserIdNonmatchAndAdmin() throws Exception {
        support.setUpSubject("foo", new String[]{"admin"});
        tag.setPageContext(support.pageContext);
        tag.setRole("viewer");
        tag.setUserid("foo");
        tag.setAdminRole("admin");

        int result = tag.doStartTag();

        assertEquals("wrong result", Tag.EVAL_BODY_INCLUDE, result);
    }

    public void testMultipleRoles() throws Exception {
        support.setUpSubject("foo", new String[]{"viewer"});
        tag.setPageContext(support.pageContext);
        tag.setRole("viewer,editor");
        tag.setUserid("foo");
        tag.setAdminRole("admin");

        int result = tag.doStartTag();

        assertEquals("wrong result", Tag.EVAL_BODY_INCLUDE, result);
    }
}
