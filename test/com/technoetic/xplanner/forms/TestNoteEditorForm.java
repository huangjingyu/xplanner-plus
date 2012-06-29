package com.technoetic.xplanner.forms;

import java.util.Iterator;

import junit.framework.TestCase;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.upload.DiskFile;
import org.apache.struts.upload.FormFile;

import com.technoetic.xplanner.XPlannerTestSupport;

public class TestNoteEditorForm extends TestCase {
    public TestNoteEditorForm(String name) {
        super(name);
    }

    private XPlannerTestSupport support;
    private NoteEditorForm form;

    protected void setUp() throws Exception {
        support = new XPlannerTestSupport();
        form = new NoteEditorForm();
        form.setSubject("subject");
        form.setBody("body");
        FormFile testFile = new DiskFile("data/TestAttachment.txt");
        form.setFormFile(testFile);
        form.setAuthorId(100);
    }

    public void testReset() {
        form.reset(support.mapping, support.request);

        assertNull("variable not reset", form.getAction());
        assertNull("variable not reset", form.getSubject());
        assertNull("variable not reset", form.getBody());
        assertNull("variable not reset", form.getFormFile());
        assertNull("variable not reset", form.getAttachedToType());
        assertEquals("variable not reset", 0, form.getAttachedToId());
    }

    public void testValidateFormOk() {
        ActionErrors errors = form.validate(support.mapping, support.request);

        assertEquals("unexpected errors", 0, errors.size());
    }

    public void testValidateMissingSubjectNotSubmitted() {
        form.setSubject(null);

        ActionErrors errors = form.validate(support.mapping, support.request);

        assertEquals("unexpected errors", 0, errors.size());
    }

    public void testValidateMissingSubject() {
        form.setSubject(null);
        form.setAction("Update");

        ActionErrors errors = form.validate(support.mapping, support.request);

        assertEquals("unexpected errors", 1, errors.size());
        Iterator errorItr = errors.get();
        ActionError error = (ActionError)errorItr.next();
        assertEquals("wrong key", "note.editor.missing_subject", error.getKey());
    }

    public void testValidateMissingBody() {
        form.setBody(null);
        form.setAction("Update");

        ActionErrors errors = form.validate(support.mapping, support.request);

        assertEquals("unexpected errors", 1, errors.size());
        Iterator errorItr = errors.get();
        ActionError error = (ActionError)errorItr.next();
        assertEquals("wrong key", "note.editor.missing_body", error.getKey());
    }

    public void testValidateMissingAuthor() {
        form.setAuthorId(0);
        form.setAction("Update");

        ActionErrors errors = form.validate(support.mapping, support.request);

        assertEquals("unexpected errors", 1, errors.size());
        Iterator errorItr = errors.get();
        ActionError error = (ActionError)errorItr.next();
        assertEquals("wrong key", "note.editor.missing_author", error.getKey());
    }

    public void testValidateMissingFileOnUpdateOk() {
        form.setFormFile(null);
        form.setAction(com.technoetic.xplanner.actions.EditObjectAction.UPDATE_ACTION);

        ActionErrors errors = form.validate(support.mapping, support.request);

        assertEquals("unexpected errors", 0, errors.size());
    }

    public void testValidateMissingFileOnCreateOk() {
        form.setFormFile(null);
        form.setAction(com.technoetic.xplanner.actions.EditObjectAction.CREATE_ACTION);

        ActionErrors errors = form.validate(support.mapping, support.request);

        assertEquals("unexpected errors", 0, errors.size());
    }
}
