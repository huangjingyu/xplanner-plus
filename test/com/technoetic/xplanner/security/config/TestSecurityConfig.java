package com.technoetic.xplanner.security.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import junit.framework.TestCase;

import org.xml.sax.SAXException;

import com.technoetic.xplanner.XPlannerTestSupport;

public class TestSecurityConfig extends TestCase {
    private XPlannerTestSupport support;

    public TestSecurityConfig(String s) {
        super(s);
    }

    protected void setUp() throws Exception {
        super.setUp();
        support = new XPlannerTestSupport();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testParsing() throws Exception {
        SecurityConfiguration config = SecurityConfiguration.load(loadTestConfiguration());

        assertEquals("wrong # of constraints", 2, config.getSecurityConstraints().size());
        SecurityConstraint securityConstraint = (SecurityConstraint)config.getSecurityConstraints().get(0);
        assertEquals("wrong # of resource collections", 2, securityConstraint.getWebResourceCollection().size());
        Iterator resourceItr = securityConstraint.getWebResourceCollection().iterator();
        WebResourceCollection webResourceCollection = (WebResourceCollection)resourceItr.next();
        assertEquals("wrong name", "XPlanner Viewing 1", webResourceCollection.getName());
        assertEquals("wrong # of urlPatterns", 1, webResourceCollection.getUrlPatterns().size());
        assertTrue("missing urlPattern", webResourceCollection.getUrlPatterns().contains("/do/view/*"));
        webResourceCollection = (WebResourceCollection)resourceItr.next();
        assertEquals("wrong name", "XPlanner Viewing 2", webResourceCollection.getName());
        assertEquals("wrong # of urlPatterns", 2, webResourceCollection.getUrlPatterns().size());
        assertTrue("missing urlPattern", webResourceCollection.getUrlPatterns().contains("*.foo"));
        assertTrue("missing urlPattern", webResourceCollection.getUrlPatterns().contains("/soap/XPlannerView"));
        assertEquals("wrong # of authConstraints", 1, securityConstraint.getAuthConstraints().size());
        AuthConstraint authConstraint = (AuthConstraint)securityConstraint.getAuthConstraints().iterator().next();
        assertEquals("wrong # of role names", 3, authConstraint.getRoleNames().size());
        assertTrue("missing role name", authConstraint.getRoleNames().contains("editor"));
        assertTrue("missing role name", authConstraint.getRoleNames().contains("viewer"));
        assertTrue("missing role name", authConstraint.getRoleNames().contains("admin"));

        securityConstraint = (SecurityConstraint)config.getSecurityConstraints().get(1);
        assertEquals("wrong # of resource collections", 1, securityConstraint.getWebResourceCollection().size());
        resourceItr = securityConstraint.getWebResourceCollection().iterator();
        webResourceCollection = (WebResourceCollection)resourceItr.next();
        assertEquals("wrong name", "XPlanner Editing", webResourceCollection.getName());
        assertEquals("wrong # of urlPatterns", 2, webResourceCollection.getUrlPatterns().size());
        assertTrue("missing urlPattern", webResourceCollection.getUrlPatterns().contains("/do/edit/*"));
        assertTrue("missing urlPattern", webResourceCollection.getUrlPatterns().contains("/x/*"));
        authConstraint = (AuthConstraint)securityConstraint.getAuthConstraints().iterator().next();
        assertEquals("wrong # of role names", 2, authConstraint.getRoleNames().size());
        assertTrue("missing role name", authConstraint.getRoleNames().contains("editor"));
        assertTrue("missing role name", authConstraint.getRoleNames().contains("admin"));


        assertEquals("wrong # of roles", 3, config.getSecurityRoles().size());
        assertEquals("wrong # of urlPatterns in bypass", 1, config.getSecurityBypass().getUrlPatterns().size());
        assertEquals("wrong urlPattern", "/x/login", config.getSecurityBypass().getUrlPatterns().iterator().next());
    }

    public void testIsAuthorizedExactMatch() throws Exception {
        SecurityConfiguration config = setUpAuthorization("/soap", "/XPlannerView", "viewer");

        boolean isAuthorized = config.isAuthorized(support.request);

        assertTrue("should be authorized", isAuthorized);
    }

    public void testIsAuthorizedPrefixMatch() throws Exception {
        SecurityConfiguration config = setUpAuthorization("/do", "/view/something", "editor");

        boolean isAuthorized = config.isAuthorized(support.request);

        assertTrue("should be authorized", isAuthorized);
    }

    public void testIsAuthorizedExtensionMatch() throws Exception {
        SecurityConfiguration config = setUpAuthorization("", "xyz.foo", "editor");

        boolean isAuthorized = config.isAuthorized(support.request);

        assertTrue("should be authorized", isAuthorized);
    }

    public void testIsAuthorizedNoConstraints() throws Exception {
        SecurityConfiguration config = setUpAuthorization("", "abcdef", "editor");

        boolean isAuthorized = config.isAuthorized(support.request);

        assertTrue("should be authorized", isAuthorized);
    }

//    public void testIsAuthorizedWrongRole() throws Exception {
//        SecurityConfiguration config = setUpAuthorization("/do", "/edit/something", "viewer");
//
//        boolean isAuthorized = config.isAuthorized(support.request);
//
//        assertFalse("should not be authorized", isAuthorized);
//    }

    public void testIsAuthorizedBypass() throws Exception {
        SecurityConfiguration config = setUpAuthorization("/x", "/login", "viewer");

        boolean isAuthorized = config.isAuthorized(support.request);

        assertTrue("should be authorized", isAuthorized);
    }

    private SecurityConfiguration setUpAuthorization(String servletPath, String pathInfo, String role) throws SAXException, IOException {
        SecurityConfiguration config = SecurityConfiguration.load(loadTestConfiguration());
        support.request.setServletPath(servletPath);
        support.request.setPathInfo(pathInfo);
        support.setUpSubjectInRole(role);
        return config;
    }

    private InputStream loadTestConfiguration() {
        return new ByteArrayInputStream(testConfiguration.getBytes());
    }

    private String testConfiguration =
            "<security>\n" +
            "    <security-bypass>\n" +
            "        <url-pattern>/x/login</url-pattern>\n" +
            "    </security-bypass>\n" +
            "\n" +
            "    <security-constraint>\n" +
            "      <display-name>XPlanner View Constraints</display-name>\n" +
            "        <web-resource-collection>\n" +
            "          <web-resource-name>XPlanner Viewing 1</web-resource-name>\n" +
            "          <url-pattern>/do/view/*</url-pattern>\n" +
            "        </web-resource-collection>\n" +
            "        <web-resource-collection>\n" +
            "          <web-resource-name>XPlanner Viewing 2</web-resource-name>\n" +
            "          <url-pattern>*.foo</url-pattern>\n" +
            "          <url-pattern>/soap/XPlannerView</url-pattern>\n" +
            "        </web-resource-collection>\n" +
            "      <auth-constraint>\n" +
            "        <role-name>viewer</role-name>\n" +
            "        <role-name>editor</role-name>\n" +
            "        <role-name>admin</role-name>\n" +
            "      </auth-constraint>\n" +
            "    </security-constraint>\n" +
            "    <security-constraint>\n" +
            "      <display-name>XPlanner Edit Constraints</display-name>\n" +
            "      <web-resource-collection>\n" +
            "        <web-resource-name>XPlanner Editing</web-resource-name>\n" +
            "        <url-pattern>/do/edit/*</url-pattern>\n" +
            "        <url-pattern>/x/*</url-pattern>\n" +
            "      </web-resource-collection>\n" +
            "      <auth-constraint>\n" +
            "        <role-name>editor</role-name>\n" +
            "        <role-name>admin</role-name>\n" +
            "      </auth-constraint>\n" +
            "    </security-constraint>\n" +
            "\n" +
            "    <security-role>\n" +
            "      <role-name>editor</role-name>\n" +
            "    </security-role>\n" +
            "    <security-role>\n" +
            "      <role-name>viewer</role-name>\n" +
            "    </security-role>\n" +
            "    <security-role>\n" +
            "      <role-name>admin</role-name>\n" +
            "    </security-role>\n" +
            "</security>\n";
}
