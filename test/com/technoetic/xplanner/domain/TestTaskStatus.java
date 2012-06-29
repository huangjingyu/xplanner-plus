package com.technoetic.xplanner.domain;


import junit.framework.TestCase;

public class TestTaskStatus extends TestCase
{

    public void testCompareTo() throws Exception
    {
        assertTrue(TaskStatus.STARTED.compareTo(TaskStatus.NON_STARTED) < 0);
        assertTrue(TaskStatus.STARTED.compareTo(TaskStatus.COMPLETED) < 0);
        assertTrue(TaskStatus.NON_STARTED.compareTo(TaskStatus.COMPLETED) < 0);
    }
}