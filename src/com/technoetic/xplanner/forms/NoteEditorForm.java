package com.technoetic.xplanner.forms;

import javax.servlet.http.HttpServletRequest;

import net.sf.xplanner.domain.File;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

public class NoteEditorForm extends AbstractEditorForm {
    private int authorId;
    private String subject;
    private String body;
    private FormFile formFile;
    private String attachedToType;
    private int attachedToId;
    private int attachmentId;
    private File file;

    public String getContainerId() {
        return Integer.toString(getAttachedToId());
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (isSubmitted()) {
            require(errors, subject, "note.editor.missing_subject");
            require(errors, authorId, "note.editor.missing_author");
            if (formFile == null || StringUtils.isEmpty(formFile.getFileName())) {
                require(errors, body, "note.editor.missing_body");
            }
        }
        return errors;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        subject = null;
        body = null;
        formFile = null;
        authorId = 0;
        attachedToId = 0;
        attachedToType = null;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setFormFile(FormFile formFile) {
        this.formFile = formFile;
    }

    public FormFile getFormFile() {
        return formFile;
    }

    public void setAttachedToType(String attachedToType) {
        this.attachedToType = attachedToType;
    }

    public String getAttachedToType() {
        return attachedToType;
    }

    public void setAttachedToId(int attachedToId) {
        this.attachedToId = attachedToId;
    }

    public int getAttachedToId() {
        return attachedToId;
    }

    public int getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(int attachmentId) {
        this.attachmentId = attachmentId;
    }

    public File getAttachedFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
