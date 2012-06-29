package com.technoetic.xplanner.performance;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.clarkware.junitperf.TimedTest;
import com.technoetic.xplanner.acceptance.web.MetricsPageTestScript;

/**
 * User: Mateusz Prokopowicz
 * Date: Feb 3, 2005
 * Time: 3:01:23 PM
 */
public class MetricsPerformanceTest extends TestCase{
    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite();
        suite.addTest(
            new TimedTest(new MetricsPageTestScript("testActualTime"), 305000));
       suite.addTest(
            new TimedTest(new MetricsPageTestScript("testHoursAcceptedGraph"), 326000));
       suite.addTest(
            new TimedTest(new MetricsPageTestScript("testZeroHourTask"), 312000));
        return suite;
    }
}
