package com.technoetic.xplanner.forms;

import java.text.SimpleDateFormat;
import java.util.Locale;

import junit.framework.TestCase;

import org.apache.struts.action.ActionErrors;

import com.technoetic.xplanner.XPlannerTestSupport;
import com.technoetic.xplanner.domain.virtual.Timesheet;
import com.technoetic.xplanner.domain.virtual.TimesheetEntry;

public class TestAggregateTimesheetForm extends TestCase {
    public TestAggregateTimesheetForm(String name) {
        super(name);
    }

    private XPlannerTestSupport support;
    private AggregateTimesheetForm form;

    protected void setUp() throws Exception {
        support = new XPlannerTestSupport();
        support.resources.setMessage("format.date", "yyyy-MM-dd");
        support.request.setLocale(new Locale("da", "nl"));
        form = new AggregateTimesheetForm();
        form.setServlet(support.actionServlet);
        form.setTimesheet(this.buildTimesheet());
        form.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    }

    protected Timesheet buildTimesheet() {
        Timesheet timesheet = new Timesheet();
        TimesheetEntry entry1 = new TimesheetEntry();
        entry1.setPersonName("Test User");
        entry1.setProjectName("Unit Testing");
        entry1.setIterationName("Test Iteration");
        entry1.setStoryName("Test Story");
        entry1.setTotalDuration(6.5);
        timesheet.addEntry(entry1);
        return timesheet;
    }

    public void testReset() {
        form.reset(support.mapping, support.request);
        assertNotNull(form.getStartDate());
        assertNotNull(form.getEndDate());
        assertNotNull(form.getStartDateString());
        assertNotNull(form.getEndDateString());
    }

    public void testValidateFormOk() {
        ActionErrors errors = form.validate(support.mapping, support.request);
        assertEquals("wrong # of expected errors", 0, errors.size());
    }

    public void testLoadStartDate() {

        // Bogus date
        form.setStartDateString("010101");
        ActionErrors errors = form.validate(support.mapping, support.request);
        assertEquals("wrong # of expected errors", 1, errors.size());
    }

}
