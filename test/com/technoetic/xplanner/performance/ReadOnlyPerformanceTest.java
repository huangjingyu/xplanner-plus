package com.technoetic.xplanner.performance;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.clarkware.junitperf.TimedTest;
import com.technoetic.xplanner.acceptance.web.AbstractPageTestScript;

// DEBT Should not hardcode urls. Refactor into a gotoProject...
public class ReadOnlyPerformanceTest extends AbstractPageTestScript {

    public void setUp() throws Exception {
        super.setUp();
        tester.login();
    }

    public void tearDown() throws Exception {
        tester.logout();
        super.tearDown();
    }

    public ReadOnlyPerformanceTest(String test) {
        super(test);
    }

    public void showMainPages() throws Exception {
        tester.gotoProjectsPage();
        tester.gotoPage("view", "project", 1);
        tester.gotoPage("view", "iteration", 76742);
        tester.gotoPage("view", "userstory", 76742);
        tester.gotoPage("view", "task", 804);
    }

    public void showHistoryPages() {
        tester.gotoPage("/do/view/history?oid=1&@type=net.sf.xplanner.domain.Project");
        tester.gotoPage(
            "/do/view/history?oid=76742&@type=net.sf.xplanner.domain.Iteration");
        tester.gotoPage("/do/view/history?oid=4747&@type=net.sf.xplanner.domain.UserStory");
        tester.gotoPage("/do/view/history?oid=804&@type=net.sf.xplanner.domain.Task");
    }

    public void showIterationDetailPages() {
        tester.gotoPage("/do/view/iteration/tasks?oid=76742");
        tester.gotoPage("/do/view/iteration/metrics?oid=76742");
        tester.gotoPage("/do/view/iteration/statistics?oid=76742");
        tester.gotoPage("/do/view/iteration/accuracy?oid=76742");
    }

    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite();
        suite.addTest(new TimedTest(new ReadOnlyPerformanceTest("showMainPages"), 30000));
       suite.addTest(new TimedTest(new ReadOnlyPerformanceTest("showIterationDetailPages"), 70000));
       suite.addTest(new TimedTest(new ReadOnlyPerformanceTest("showHistoryPages"), 20000));
       return suite;
    }
}
