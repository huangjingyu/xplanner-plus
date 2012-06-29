package com.technoetic.xplanner.security.filter;

import java.io.ByteArrayInputStream;

import javax.security.auth.Subject;

import junit.framework.TestCase;
import net.sf.xplanner.domain.Person;

import com.technoetic.mocks.servlets.MockFilterChain;
import com.technoetic.xplanner.XPlannerTestSupport;
import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.security.PersonPrincipal;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.security.config.SecurityConfiguration;

public abstract class AbstractSecurityFilterTestCase extends TestCase {
    protected XPlannerTestSupport support;
    protected MockFilterChain mockFilterChain;
    protected final String CONTEXT = "/xplanner";
    protected Subject subject;

    public AbstractSecurityFilterTestCase(String s) {
        super(s);
    }

    protected void setUp() throws Exception {
        super.setUp();
        ByteArrayInputStream config = new ByteArrayInputStream((
                "<security>\n" +
                    "<security-bypass>" +
                        "<url-pattern>/do/login</url-pattern>" +
                    "</security-bypass>" +
                "    <security-constraint>\n" +
                "      <web-resource-collection>\n" +
                "        <web-resource-name>edit</web-resource-name>\n" +
                "        <url-pattern>/x/*</url-pattern>\n" +
                "      </web-resource-collection>\n" +
                "      <auth-constraint>\n" +
                "        <role-name>editor</role-name>\n" +
                "      </auth-constraint>\n" +
                "    </security-constraint>\n" +
                "    <security-role>\n" +
                "      <role-name>editor</role-name>\n" +
                "    </security-role>\n" +
                "</security>"
                ).getBytes()
        );
        getSecurityFilter().setSecurityConfiguration(SecurityConfiguration.load(config));
        mockFilterChain = new MockFilterChain();
        support = new XPlannerTestSupport();
        support.request.setContextPath(CONTEXT);
        support.request.setRequestURI("/do/foo");
        ThreadSession.set(support.hibernateSession);
    }

    protected void tearDown() throws Exception {
        ThreadSession.set(null);
        super.tearDown();
    }

    protected abstract AbstractSecurityFilter getSecurityFilter();

    protected void setUpSubject() {
        subject = new Subject();
        subject.getPrincipals().add(new PersonPrincipal(new Person("xyz")));
        SecurityHelper.setSubject(support.request, subject);
    }

    protected void setUpRequest(String servletPath, String uri) {
        support.request.setMethod(1);
        support.request.setServletPath(servletPath);
        support.request.setPathInfo(uri);
        support.request.setRequestURI(uri);
    }
}
