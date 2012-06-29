package com.technoetic.xplanner.domain.virtual;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

public class TestDailyTimesheetEntry extends TestCase {
    private DailyTimesheetEntry dailyTimesheetEntry = null;
    public static final Date ENTRY_DATE;

    static {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        ENTRY_DATE = cal.getTime();
    }

    private static final BigDecimal DURATION = new BigDecimal(7.5);
    private static final SimpleDateFormat format =
            DailyTimesheetEntry.shortDateFormat;

    public TestDailyTimesheetEntry(String name) {
        super(name);
    }

    public static DailyTimesheetEntry getTestDailyTimesheetEntry() {
        DailyTimesheetEntry dte = new DailyTimesheetEntry(ENTRY_DATE, DURATION);
        return dte;
    }

    protected void setUp() throws Exception {
        super.setUp();
        this.dailyTimesheetEntry = getTestDailyTimesheetEntry();
    }

    protected void tearDown() throws Exception {
        dailyTimesheetEntry = null;
        super.tearDown();
    }

    public void testAttributes() throws Exception {

        assertEquals("Incorrect Entry Date", ENTRY_DATE,
                this.dailyTimesheetEntry.getEntryDate());
        assertEquals("Incorrect Duration",
                DURATION.setScale(1, BigDecimal.ROUND_HALF_EVEN),
                this.dailyTimesheetEntry.getTotalDuration());
        assertEquals("Entry Date String Invalid", format.format(ENTRY_DATE),
                this.dailyTimesheetEntry.getEntryDateShort());

    }
}
