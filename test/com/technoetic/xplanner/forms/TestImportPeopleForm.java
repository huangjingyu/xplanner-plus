package com.technoetic.xplanner.forms;

import junit.framework.TestCase;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import com.technoetic.xplanner.XPlannerTestSupport;
import com.technoetic.xplanner.actions.EditObjectAction;

public class TestImportPeopleForm extends TestCase {
    public TestImportPeopleForm(String name) {
        super(name);
    }

    private XPlannerTestSupport support;
    private ImportPeopleForm form;

    protected void setUp() throws Exception {
        super.setUp();
        support = new XPlannerTestSupport();
        form = new ImportPeopleForm();
        form.setServlet(support.actionServlet);
    }

    public void testReset() {
        form.reset(support.mapping, support.request);
        assertNull(form.getFormFile());
        assertNull(form.getAction());
        assertEquals(0, form.getResults().size());
    }

    public void testValidateFormOk() {
        ActionErrors errors = form.validate(support.mapping, support.request);
        assertEquals("wrong # of expected errors", 0, errors.size());
    }

    public void testValidateFormFail() {
        form.setAction(EditObjectAction.UPDATE_ACTION);
        ActionErrors errors = form.validate(support.mapping, support.request);
        assertEquals("wrong # of expected errors", 1, errors.size());
        assertEquals("wrong error message",
                     ImportForm.NO_IMPORT_FILE_KEY,
                     ((ActionError) errors.get().next()).getKey());
    }
}