package com.technoetic.xplanner.forms;

import java.util.Iterator;

import junit.framework.TestCase;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import com.technoetic.xplanner.XPlannerTestSupport;

public class TestFeatureEditorForm extends TestCase {
    public TestFeatureEditorForm(String name) {
        super(name);
    }

    private XPlannerTestSupport support;
    private FeatureEditorForm form;

    protected void setUp() throws Exception {
        support = new XPlannerTestSupport();
        support.resources.setMessage("format.date", "yyyy-MM-dd");
        form = new FeatureEditorForm();
        form.setName("name");
    }

    public void testReset() {
        form.reset(support.mapping, support.request);

        assertNull("variable not reset", form.getAction());
        assertNull("variable not reset", form.getName());
        assertNull("variable not reset", form.getDescription());
        assertEquals("variable not reset", 0, form.getStoryId());
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
        assertEquals("wrong key", "feature.editor.missing_name", error.getKey());
    }
}