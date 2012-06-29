package com.technoetic.xplanner.util;

import junit.framework.TestCase;

public class TestInterval extends TestCase {

    public TestInterval(String s) {
        super(s);
    }

    public void testCompareTo() {
        Interval i1 = new Interval(5, 10);
        Interval i2 = new Interval(15, 20);

        assertTrue("wrong result", i1.compareTo(i2) < 0);
        assertTrue("wrong result", i2.compareTo(i1) > 0);
        assertTrue("wrong result", i1.compareTo(i1) == 0);

        i2 = new Interval(6, 10);

        assertTrue("wrong result", i1.compareTo(i2) < 0);
        assertTrue("wrong result", i2.compareTo(i1) > 0);

        i2 = new Interval(5, 6);

        assertTrue("wrong result", i1.compareTo(i2) > 0);
        assertTrue("wrong result", i2.compareTo(i1) < 0);
    }

    public void testAdjacent() {
        Interval i1 = new Interval(5, 10);
        Interval i2 = new Interval(15, 20);

        assertFalse("wrong result", i1.adjacent(i2));

        i2 = new Interval(10, 12);

        assertTrue("wrong result", i1.adjacent(i2));

        i2 = new Interval(4, 5);

        assertTrue("wrong result", i1.adjacent(i2));
    }

    public void testOverlaps() {
        Interval i1 = new Interval(5, 10);
        Interval i2 = new Interval(15, 20);

        assertFalse("wrong result", i1.overlaps(i2));

        i2 = new Interval(10, 12);

        assertFalse("wrong result", i1.overlaps(i2));

        i2 = new Interval(4, 5);

        assertFalse("wrong result", i1.overlaps(i2));

        i2 = new Interval(9, 12);

        assertTrue("wrong result", i1.overlaps(i2));

        i2 = new Interval(4, 6);

        assertTrue("wrong result", i1.overlaps(i2));
    }
}
