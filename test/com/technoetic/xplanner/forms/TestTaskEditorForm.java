package com.technoetic.xplanner.forms;

import java.util.Iterator;

import junit.framework.TestCase;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import com.technoetic.xplanner.XPlannerTestSupport;

public class TestTaskEditorForm extends TestCase {
    public TestTaskEditorForm(String name) {
        super(name);
    }

    private XPlannerTestSupport support;
    private TaskEditorForm form;

    protected void setUp() throws Exception {
       super.setUp();
       support = new XPlannerTestSupport();
       support.resources.setMessage("format.date", "yyyy-MM-dd");
       form = new TaskEditorForm();
       form.setName("name");
    }

    public void testReset() {
        form.reset(support.mapping, support.request);

        assertNull("variable not reset", form.getAction());
        assertNull("variable not reset", form.getName());
        assertNull("variable not reset", form.getDescription());
        assertNull("variable not reset", form.getType());
        assertNull("variable not reset", form.getDispositionName());
        assertEquals("variable not reset", 0, form.getUserStoryId());
        assertEquals("variable not reset", 0, form.getAcceptorId());
        assertEquals("variable not reset", 0.0, form.getEstimatedHours(), 0);
        assertEquals("variable not reset", 0.0, form.getActualHours(), 0);
        assertTrue("variable not reset", !form.isCompleted());
    }

    public void testValidateFormOk() {
        ActionErrors errors = form.validate(support.mapping, support.request);

        assertEquals("unexpected errors", 0, errors.size());
    }

    public void testValidateMissingNameNotSubmitted() {
        form.setName(null);

        ActionErrors errors = form.validate(support.mapping, support.request);

        assertEquals("unexpected errors", 0, errors.size());
    }

    public void testValidateMissingName() {
        form.setName(null);
        form.setAction("Update");

        ActionErrors errors = form.validate(support.mapping, support.request);

        assertEquals("unexpected errors", 1, errors.size());
        Iterator errorItr = errors.get();
        ActionError error = (ActionError)errorItr.next();
        assertEquals("wrong key", "task.editor.missing_name", error.getKey());
    }


    public void testValidateNegativeEstimate() {
        form.setEstimatedHours(-10.0);
        form.setAction("Update");

        ActionErrors errors = form.validate(support.mapping, support.request);

        assertEquals("unexpected errors", 1, errors.size());
        Iterator errorItr = errors.get();
        ActionError error = (ActionError)errorItr.next();
        assertEquals("wrong key", "task.editor.negative_estimated_hours", error.getKey());
    }

    public void testDefaultCreatedDate() {
        assertNotNull(form.getCreatedDate());
    }
}