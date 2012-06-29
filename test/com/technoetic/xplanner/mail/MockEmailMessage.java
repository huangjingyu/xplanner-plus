package com.technoetic.xplanner.mail;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.orm.hibernate3.HibernateOperations;

import com.technoetic.xplanner.domain.repository.MetaRepository;
import com.technoetic.xplanner.domain.repository.ObjectRepository;

public class MockEmailMessage implements EmailMessage {
    public boolean setFromCalled;
    public AddressException setFromAddressException;
    public MessagingException setFromMessagingException;
    public String setFromFrom;

    public void setFrom(String from) throws AddressException, MessagingException {
        setFromCalled = true;
        setFromFrom = from;
        if (setFromAddressException != null) {
            throw setFromAddressException;
        }
        if (setFromMessagingException != null) {
            throw setFromMessagingException;
        }
    }

    public boolean setRecipientsCalled;
    public AddressException setRecipientsAddressException;
    public MessagingException setRecipientsMessagingException;
    public String setRecipientsRecipients;

    public void setRecipients(String recipients) throws AddressException, MessagingException {
        setRecipientsCalled = true;
        setRecipientsRecipients = recipients;
        if (setRecipientsAddressException != null) {
            throw setRecipientsAddressException;
        }
        if (setRecipientsMessagingException != null) {
            throw setRecipientsMessagingException;
        }
    }

    public boolean setRecipients2Called;
    public AddressException setRecipients2AddressException;
    public MessagingException setRecipients2MessagingException;
    public String[] setRecipients2Recipients;

    public void setRecipients(String[] recipients) throws AddressException, MessagingException {
        setRecipients2Called = true;
        setRecipients2Recipients = recipients;
        if (setRecipientsAddressException != null) {
            throw setRecipients2AddressException;
        }
        if (setRecipientsMessagingException != null) {
            throw setRecipients2MessagingException;
        }
    }

    public boolean setBodyCalled;
    public MessagingException setBodyMessagingException;
    public String setBodyBody;

    public void setBody(String body) throws MessagingException {
        setBodyCalled = true;
        setBodyBody = body;
        if (setBodyMessagingException != null) {
            throw setBodyMessagingException;
        }
    }

    public boolean getBodyWriterCalled;
    public java.io.PrintWriter getBodyWriterReturn;

    public java.io.PrintWriter getBodyWriter() {
        getBodyWriterCalled = true;
        return getBodyWriterReturn;
    }

    public boolean setSubjectCalled;
    public MessagingException setSubjectMessagingException;
    public String setSubjectSubject;

    public void setSubject(String subject) throws MessagingException {
        setSubjectCalled = true;
        setSubjectSubject = subject;
        if (setSubjectMessagingException != null) {
            throw setSubjectMessagingException;
        }
    }

    public boolean setSentDateCalled;
    public MessagingException setSentDateMessagingException;
    public java.util.Date setSentDateSentDate;

    public void setSentDate(java.util.Date sentDate) throws MessagingException {
        setSentDateCalled = true;
        setSentDateSentDate = sentDate;
        if (setSentDateMessagingException != null) {
            throw setSentDateMessagingException;
        }
    }

    public boolean addAttachmentCalled;
    public MessagingException addAttachmentMessagingException;
    public String addAttachmentFilename;

    public void addAttachment(String filename) throws MessagingException {
        addAttachmentCalled = true;
        addAttachmentFilename = filename;
        if (addAttachmentMessagingException != null) {
            throw addAttachmentMessagingException;
        }
    }

    public File addAttachmentFile;

    public void addAttachment(String filename, File file) throws MessagingException {
        addAttachmentCalled = true;
        addAttachmentFilename = filename;
        addAttachmentFile = file;
        if (addAttachmentMessagingException != null) {
            throw addAttachmentMessagingException;
        }
    }

    public boolean sendCalled;
    public MessagingException sendMessagingException;

    public void send() throws MessagingException {
        sendCalled = true;
        if (sendMessagingException != null) {
            throw sendMessagingException;
        }
    }

    public boolean setCcRecipientsCalled;
    public String setCcRecipientsRecipients;

    public void setCcRecipients(String recipients) throws MessagingException, AddressException {
        setCcRecipientsCalled = true;
        setCcRecipientsRecipients = recipients;
    }

    public boolean setRecipientCalled;
    public int setRecipientPersonId;

    public void setRecipient(int personId) throws MessagingException {
        setRecipientCalled = true;
        setRecipientPersonId = personId;
    }

   public void setObjectRepository(ObjectRepository objectRepository) {
      
   }

   public void setObjectRepository(MetaRepository repository) {

   }

   public void setHibernateOperations(HibernateOperations hibernateOperations)
   {
   }
}
