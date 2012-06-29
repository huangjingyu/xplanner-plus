package com.technoetic.xplanner.acceptance.soap;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SoapTestScript extends TestCase {

    public SoapTestScript(String s) {
        super(s);
    }

    static public Test suite() {
        TestSuite suite = new TestSuite();
//        suite.addTestSuite(LocalSoapTestDeprecated.class);
        suite.addTestSuite(RemoteSoapTest.class);
        return suite;
    }
}
