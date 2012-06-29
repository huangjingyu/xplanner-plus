package com.technoetic.xplanner.domain;

import java.util.Date;

import junit.framework.TestCase;
import net.sf.xplanner.domain.TimeEntry;

public class TestTimeEntry extends TestCase {
    private TimeEntry timeEntry;

    public TestTimeEntry(String s) {
        super(s);
    }

    protected void setUp() throws Exception {
        super.setUp();
        timeEntry = new TimeEntry();
    }

    public void testEditedTimeEntry() {
        timeEntry.setStartTime(new Date(0));
        timeEntry.setEndTime(new Date(3600000));
        timeEntry.setDuration(0.5);

        double duration = timeEntry.getDuration();

        assertEquals("wrong duration", 1, duration, 0);
    }
}
