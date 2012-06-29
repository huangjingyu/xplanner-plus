package com.technoetic.xplanner.forms;

import java.util.Calendar;
import java.util.Iterator;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

public class TestIterationEditorForm extends AbstractEditorFormTestCase {
    private IterationEditorForm iterationEditorForm;

    protected void setUp() throws Exception {
       form = iterationEditorForm = new IterationEditorForm();
       super.setUp();
       iterationEditorForm.setName("name");
       iterationEditorForm.setStartDateString("2002-02-02");
       iterationEditorForm.setEndDateString("2002-02-03");
    }

    public void testReset() {
        iterationEditorForm.reset(support.mapping, support.request);

        assertNull("variable not reset", iterationEditorForm.getAction());
        assertNull("variable not reset", iterationEditorForm.getName());
        assertNull("variable not reset", iterationEditorForm.getDescription());
        assertNull("variable not reset", iterationEditorForm.getStartDateString());
        assertNull("variable not reset", iterationEditorForm.getEndDateString());
        assertEquals("variable not reset", 0, iterationEditorForm.getProjectId());
    }

    public void testValidateFormOk() {
        ActionErrors errors = iterationEditorForm.validate(support.mapping, support.request);

        assertEquals("unexpected errors", 0, errors.size());
    }

    public void testValidateMissingNameNotSubmitted() {
        iterationEditorForm.setName(null);

        ActionErrors errors = iterationEditorForm.validate(support.mapping, support.request);

        assertEquals("unexpected errors", 0, errors.size());
    }

    public void testValidateMissingName() {
        iterationEditorForm.setName(null);
        iterationEditorForm.setAction("Update");

        ActionErrors errors = iterationEditorForm.validate(support.mapping, support.request);

        assertEquals("unexpected errors", 1, errors.size());
        Iterator errorItr = errors.get();
        ActionError error = (ActionError)errorItr.next();
        assertEquals("wrong key", "iteration.editor.missing_name", error.getKey());
    }


    public void testValidateBadStartDate() {
        iterationEditorForm.setStartDateString("bogus");
        iterationEditorForm.setAction("Update");

        ActionErrors errors = iterationEditorForm.validate(support.mapping, support.request);

        assertEquals("unexpected errors", 1, errors.size());
        Iterator errorItr = errors.get();
        ActionError error = (ActionError)errorItr.next();
        assertEquals("wrong key", "iteration.editor.bad_start_date", error.getKey());
    }

    public void testValidateMissingEndDate() {
        iterationEditorForm.setEndDateString(null);
        iterationEditorForm.setAction("Update");

        ActionErrors errors = iterationEditorForm.validate(support.mapping, support.request);

        assertEquals("unexpected errors", 1, errors.size());
        Iterator errorItr = errors.get();
        ActionError error = (ActionError)errorItr.next();
        assertEquals("wrong key", "iteration.editor.bad_end_date", error.getKey());
    }

    public void testValidateBadEndDate() {
        iterationEditorForm.setEndDateString("bogus");
        iterationEditorForm.setAction("Update");

        ActionErrors errors = iterationEditorForm.validate(support.mapping, support.request);

        assertEquals("unexpected errors", 1, errors.size());
        Iterator errorItr = errors.get();
        ActionError error = (ActionError)errorItr.next();
        assertEquals("wrong key", "iteration.editor.bad_end_date", error.getKey());
    }

    public void testValidateNegativeInterval() {
        iterationEditorForm.setEndDateString("2001-02-02 00:00");
        iterationEditorForm.setAction("Update");
        ActionErrors errors = iterationEditorForm.validate(support.mapping, support.request);

        assertEquals("unexpected errors", 1, errors.size());
        Iterator errorItr = errors.get();
        ActionError error = (ActionError)errorItr.next();
        assertEquals("wrong key", "iteration.editor.nonpositive_interval", error.getKey());
    }

    public void testSetStartDateWithNullDate() {
        iterationEditorForm.setStartDate(null);
        assertEquals("", iterationEditorForm.getStartDateString());
    }

    public void testSetStartDateWithNotNullDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(2003, 1, 2);
        iterationEditorForm.setStartDate(cal.getTime());
        assertEquals("2003-02-02", iterationEditorForm.getStartDateString());
    }

    public void testSetEndDateWithNullDate() {
        iterationEditorForm.setEndDate(null);
        assertEquals("", iterationEditorForm.getEndDateString());
    }

    public void testSetEndDateWithNotNullDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(2003, 1, 2);
        iterationEditorForm.setEndDate(cal.getTime());
        assertEquals("2003-02-02", iterationEditorForm.getEndDateString());
    }


}