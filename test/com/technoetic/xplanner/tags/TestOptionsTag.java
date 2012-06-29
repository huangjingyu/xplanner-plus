package com.technoetic.xplanner.tags;

import java.util.Arrays;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.XPlannerTestSupport;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.auth.Authorizer;

public class TestOptionsTag extends AbstractOptionsTagTestCase {

    protected void setUp() throws Exception {
        tag = new DummyOptionsTag();
        super.setUp();
    }

    public DummyOptionsTag getTag() {
        return (DummyOptionsTag) tag;
    }
    private String filterOutput(String s) {
        return s.trim().replaceAll("[\r\n]", "");
    }

    public void testImplicitMode() throws Throwable {

        assertDoEndTagReturn(Tag.EVAL_PAGE);

        assertEquals("wrong output",
                "<option value=\"1_id\">1_name</option>" +
                "<option value=\"2_id\">2_name</option>",
                filterOutput(support.jspWriter.output.toString().trim()));
    }

    public void testExplicitProperty() throws Throwable {
        tag.setProperty("userId");

        assertDoEndTagReturn(Tag.EVAL_PAGE);

        assertEquals("wrong output",
                "<option value=\"1_userId\">1_name</option>" +
                "<option value=\"2_userId\">2_name</option>",
                filterOutput(support.jspWriter.output.toString().trim()));
    }

    public void testExplicitLabelProperty() throws Throwable {
        tag.setLabelProperty("userId");

        assertDoEndTagReturn(Tag.EVAL_PAGE);

        assertEquals("wrong output",
                "<option value=\"1_id\">1_userId</option>" +
                "<option value=\"2_id\">2_userId</option>",
                filterOutput(support.jspWriter.output.toString().trim()));
    }

    private void assertDoEndTagReturn(int expectedReturnValue) throws Throwable {
        try {
            int result = tag.doEndTag();
            assertEquals("wrong result", expectedReturnValue, result);
            assertSame(support.hibernateSession, getTag().session);
            assertSame(authorizer, getTag().authorizer);
            assertEquals(XPlannerTestSupport.DEFAULT_PERSON_ID, getTag().userId);
        } catch (JspException e) {
            if (e.getRootCause() != null) {
                throw e.getRootCause();
            } else {
                throw e;
            }
        }
    }

    public static class DummyOptionsTag extends OptionsTag {
        public Session session;
        public Authorizer authorizer;
        public int userId;

        protected List getOptions() throws HibernateException, AuthenticationException {
            session = getSession();
            authorizer = getAuthorizer();
            userId = getLoggedInUserId();
            return Arrays.asList(new Bean[]{new Bean("1_id", "1_name", "1_userId"), new Bean("2_id", "2_name", "2_userId")});
        }
    }

    public static class Bean {
        private String id;
        private String name;
        private String userId;

        public Bean(String s1, String s2, String s3) {
            this.id = s1;
            this.name = s2;
            this.userId = s3;
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
    }
}
