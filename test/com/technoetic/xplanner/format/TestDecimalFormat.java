package com.technoetic.xplanner.format;

import java.util.Locale;

import junit.framework.TestCase;

import com.technoetic.xplanner.XPlannerTestSupport;

public class TestDecimalFormat extends TestCase {
    private XPlannerTestSupport support;
    final String LANGUAGE = "da";
    final String COUNTRY = "nl";
    final String INPUT_VALUE = "2,5";

    public TestDecimalFormat(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        support = new XPlannerTestSupport();
    }

    public void testLocaleDanish() throws Exception {
        support.request.setLocale(new Locale(LANGUAGE, COUNTRY));

        double value = new DecimalFormat(support.request).parse(INPUT_VALUE);

        assertEquals(2.5, value, 0);
    }


    public void testLocaleDanishError() throws Exception {
        support.request.setLocale(new Locale(LANGUAGE, COUNTRY));

        double value = new DecimalFormat(support.request).parse("2.5");

        assertEquals(25, value, 0);
    }

    public void testLocaleUS() throws Exception {
        support.request.setLocale(new Locale("en", "us"));

        double value = new DecimalFormat(support.request).parse("2.5");

        assertEquals(2.5, value, 0);
    }

    public void testLocaleUsError() throws Exception {
        support.request.setLocale(new Locale("en", "us"));

        double value = new DecimalFormat(support.request).parse(INPUT_VALUE);

        assertEquals(25, value, 0);
    }

}