package com.technoetic.xplanner.acceptance.soap;

import javax.servlet.http.HttpServletRequest;

import com.kizna.servletunit.HttpServletRequestSimulator;
import com.technoetic.xplanner.filters.ThreadServletRequest;
import com.technoetic.xplanner.security.SecurityTestHelper;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;
import com.technoetic.xplanner.soap.XPlanner;

/**
 * @deprecated Decide if this test is valuable once migrated to XFire
 */
public class LocalSoapTestDeprecated extends AbstractSoapTestCase {

    public LocalSoapTestDeprecated(String s) {
        super(s);
    }

    public void setUp() throws Exception {
        super.setUp();
        SystemAuthorizer.set(createAuthorizer());
    }

    private HttpServletRequest setUpRequest() throws Exception {
       HttpServletRequestSimulator request = new HttpServletRequestSimulator();
       SecurityTestHelper.setRemoteUserId(getUserId(), request, getSession());
       return request;
    }

   public void tearDown() throws Exception {
        ThreadServletRequest.set(null);
        super.tearDown();
    }

    public XPlanner createXPlanner() throws Exception {
        ThreadServletRequest.set(setUpRequest());
        return XPlannerTestAdapter.create();
    }
}
