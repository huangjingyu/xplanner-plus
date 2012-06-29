package com.technoetic.xplanner.tags.displaytag;

/*
 * $Header$
 * $Revision: 33 $
 * $Date: 2004-07-22 19:15:30 +0300 (Чт, 22 июл 2004) $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */

import junit.framework.TestCase;

public class TestHoursDecorator extends TestCase {
    UserStoryDecorator decorator;

    public void testGetPercentCompletedScore() throws Exception {
        double pct1 = HoursDecorator.getPercentCompletedScore(10, 10, 0, true);
        double pct2 = HoursDecorator.getPercentCompletedScore(10, 9, 0, true);
        double pct3 = HoursDecorator.getPercentCompletedScore(5, 5, 0, true);
        double pct4 = HoursDecorator.getPercentCompletedScore(4, 2, 2, false);
        double pct5 = HoursDecorator.getPercentCompletedScore(0, 0, 0, false);
        assertTrue(pct1 > pct2);
        assertTrue(pct2 > pct3);
        assertTrue(pct3 > pct4);
        assertTrue(pct4 > pct5);
    }

}