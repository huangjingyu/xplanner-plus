package com.technoetic.xplanner.tags;

/*
 * $Header$
 * $Revision: 70 $
 * $Date: 2004-09-27 21:26:11 +0300 (Пн, 27 сен 2004) $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */

import java.awt.Color;

import junit.framework.TestCase;

import com.technoetic.xplanner.XPlannerTestSupport;

public class TestProgressBarImageTag extends TestCase {
    ProgressBarImageTag tag = new ProgressBarImageTag();
    private XPlannerTestSupport support;

    protected void setUp() throws Exception {
        super.setUp();
        support = new XPlannerTestSupport();
        tag.setPageContext( support.pageContext );
    }

    public void testGetForegroundColor() throws Exception {
        tag.setComplete(true);
        assertEquals(ProgressBarImageTag.COMPLETED_COLOR, tag.getForegroundColor());
        tag.setComplete(false);
        assertEquals(ProgressBarImageTag.UNCOMPLETED_COLOR, tag.getForegroundColor());
    }

    public void testGetBackgroundColor() throws Exception {
        tag.setActual(10.0);
        tag.setEstimate(11.0);
        assertEquals(ProgressBarImageTag.UNWORKED_COLOR, tag.getBackgroundColor());
        tag.setActual(10.0);
        tag.setEstimate(9.0);
        assertEquals(ProgressBarImageTag.EXCEEDED_COLOR, tag.getBackgroundColor());        
    }

    public void testGetForegroundColorInPrintMode() throws Exception {
        support.request.addParameter( PrintLinkTag.PRINT_PARAMETER_NAME, "" );
        tag.setComplete(true);
        assertEquals(Color.GRAY, tag.getForegroundColor());
        tag.setComplete(false);
        assertEquals(Color.DARK_GRAY, tag.getForegroundColor());
    }

     public void testGetBackgroundColorInPrintMode() throws Exception {
        support.request.addParameter( PrintLinkTag.PRINT_PARAMETER_NAME, "" );
        tag.setActual(10.0);
        tag.setEstimate(11.0);
        assertEquals(Color.LIGHT_GRAY, tag.getBackgroundColor());
        tag.setActual(10.0);
        tag.setEstimate(9.0);
        assertEquals(Color.BLACK, tag.getBackgroundColor());
    }


    public void testGetBarValue() throws Exception {
        tag.setActual(10.0);
        tag.setEstimate(11.0);
        assertEquals(10.0, tag.getBarValue(), 0);
        tag.setActual(10.0);
        tag.setEstimate(9.0);
        assertEquals(9.0, tag.getBarValue(), 0);
    }

    public void testGetMaxValue() throws Exception {
        tag.setActual(10.0);
        tag.setEstimate(11.0);
        assertEquals(11.0, tag.getMaxValue(), 0);
        tag.setActual(10.0);
        tag.setEstimate(9.0);
        assertEquals(10.0, tag.getMaxValue(), 0);
    }
}