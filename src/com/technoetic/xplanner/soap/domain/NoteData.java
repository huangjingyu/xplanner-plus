package com.technoetic.xplanner.soap.domain;

import java.util.Calendar;

import net.sf.xplanner.domain.Note;

public class NoteData extends DomainData {
    private int attachedToId;
    private int authorId;
    private String subject;
    private String body;
    private Calendar submissionTime;
    private int attachmentId;

    public void setAttachedToId(int attachedToId) {
        this.attachedToId = attachedToId;
    }

    public int getAttachedToId() {
        return attachedToId;
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

    public void setSubmissionTime(Calendar submissionTime) {
        this.submissionTime = submissionTime;
    }

    public Calendar getSubmissionTime() {
        return submissionTime;
    }

    public static Class getInternalClass() {
        return Note.class;
    }

    public int getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(int attachmentId) {
        this.attachmentId = attachmentId;
    }
}
