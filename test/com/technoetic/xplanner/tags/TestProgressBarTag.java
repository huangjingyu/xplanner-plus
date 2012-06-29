package com.technoetic.xplanner.tags;

import junit.framework.TestCase;

import com.technoetic.xplanner.XPlannerTestSupport;

public class TestProgressBarTag extends TestCase {
    public TestProgressBarTag(String name) {
        super(name);
    }

    private XPlannerTestSupport support;
    private ProgressBarHtmlTag htmlTag = new ProgressBarHtmlTag();

    protected void setUp() throws Exception {
        support = new XPlannerTestSupport();
        htmlTag = new ProgressBarHtmlTag();
        htmlTag.setPageContext(support.pageContext);
    }

    public void testNoActualUncomplete() throws Exception {
        htmlTag.setActual(0.0);
        htmlTag.setEstimate(10.0);
        htmlTag.setComplete(false);

        htmlTag.doEndTag();

        String output = support.jspWriter.output.toString();
        assertTrue("wrong cell class", output.indexOf("progressbar_unworked") != -1);
        assertTrue("wrong cell %", output.indexOf("100%") != -1);
    }

    public void testSomeActualUncomplete() throws Exception {
        htmlTag.setActual(2.0);
        htmlTag.setEstimate(10.0);
        htmlTag.setComplete(false);

        htmlTag.doEndTag();

        String output = support.jspWriter.output.toString();
        assertTrue("wrong cell class", output.indexOf("progressbar_uncompleted") != -1);
        assertTrue("wrong cell class", output.indexOf("progressbar_unworked") != -1);
        assertTrue("wrong cell %", output.indexOf("20%") != -1);
        assertTrue("wrong cell %", output.indexOf("80%") != -1);
    }

    public void testEqualActualEstimate() throws Exception {
        htmlTag.setActual(10.0);
        htmlTag.setEstimate(10.0);
        htmlTag.setComplete(false);

        htmlTag.doEndTag();

        String output = support.jspWriter.output.toString();
        assertTrue("wrong cell class", output.indexOf("progressbar_uncompleted") != -1);
        assertTrue("wrong cell %", output.indexOf("100%") != -1);
    }

    public void testNoActualComplete() throws Exception {
        htmlTag.setActual(0.0);
        htmlTag.setEstimate(10.0);
        htmlTag.setComplete(true);

        htmlTag.doEndTag();

        String output = support.jspWriter.output.toString();
        assertTrue("wrong cell class", output.indexOf("progressbar_unworked") != -1);
        assertTrue("wrong cell %", output.indexOf("100%") != -1);
    }

    public void testSomeActualComplete() throws Exception {
        htmlTag.setActual(2.0);
        htmlTag.setEstimate(10.0);
        htmlTag.setComplete(true);

        htmlTag.doEndTag();

        String output = support.jspWriter.output.toString();
        assertTrue("wrong cell class", output.indexOf("progressbar_completed") != -1);
        assertTrue("wrong cell class", output.indexOf("progressbar_unworked") != -1);
        assertTrue("wrong cell %", output.indexOf("20%") != -1);
        assertTrue("wrong cell %", output.indexOf("80%") != -1);
    }

    public void testExceededActualUncomplete() throws Exception {
        htmlTag.setActual(15.0);
        htmlTag.setEstimate(10.0);
        htmlTag.setComplete(false);

        htmlTag.doEndTag();

        String output = support.jspWriter.output.toString();
        assertTrue("wrong cell class", output.indexOf("progressbar_uncompleted") != -1);
        assertTrue("wrong cell class", output.indexOf("progressbar_exceeded") != -1);
        assertTrue("wrong cell %", output.indexOf("67%") != -1);
        assertTrue("wrong cell %", output.indexOf("33%") != -1);
    }

    public void testExceededActualComplete() throws Exception {
        htmlTag.setActual(15.0);
        htmlTag.setEstimate(10.0);
        htmlTag.setComplete(true);

        htmlTag.doEndTag();

        String output = support.jspWriter.output.toString();
        assertTrue("wrong cell class", output.indexOf("progressbar_completed") != -1);
        assertTrue("wrong cell class", output.indexOf("progressbar_exceeded") != -1);
        assertTrue("wrong cell %", output.indexOf("67%") != -1);
        assertTrue("wrong cell %", output.indexOf("33%") != -1);
    }

    public void testExceededActualNoEstimate() throws Exception {
        htmlTag.setActual(15.0);
        htmlTag.setEstimate(0.0);

        htmlTag.doEndTag();

        String output = support.jspWriter.output.toString();
        assertTrue("wrong cell class", output.indexOf("progressbar_exceeded") != -1);
        assertTrue("wrong cell %", output.indexOf("100%") != -1);
    }

    public void testNoActualNoEstimateUncomplete() throws Exception {
        htmlTag.setActual(0.0);
        htmlTag.setEstimate(0.0);

        htmlTag.doEndTag();

        String output = support.jspWriter.output.toString();
        assertTrue("wrong cell class", output.indexOf("progressbar_unworked") != -1);
        assertTrue("wrong cell %", output.indexOf("100%") != -1);
    }

    public void testNoActualNoEstimateComplete() throws Exception {
        htmlTag.setActual(0.0);
        htmlTag.setEstimate(0.0);
        htmlTag.setComplete(true);

        htmlTag.doEndTag();

        String output = support.jspWriter.output.toString();
        assertTrue("wrong cell class", output.indexOf("progressbar_completed") != -1);
        assertTrue("wrong cell %", output.indexOf("100%") != -1);
    }
}