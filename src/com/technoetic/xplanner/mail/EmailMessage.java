package com.technoetic.xplanner.mail;

import java.io.File;
import java.io.PrintWriter;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.technoetic.xplanner.domain.repository.ObjectRepository;

public interface EmailMessage {
    void setFrom(String from) throws AddressException, MessagingException;

    void setRecipients(String recipients) throws AddressException, MessagingException;

    void setRecipients(String[] recipients) throws AddressException, MessagingException;

    void setBody(String body) throws MessagingException;

    PrintWriter getBodyWriter();

    void setSubject(String subject) throws MessagingException;

    void setSentDate(Date sentDate) throws MessagingException;

    void addAttachment(String filename) throws MessagingException;

    void addAttachment(String filename, File file) throws MessagingException;

    void send() throws MessagingException;

    void setCcRecipients(String recipients) throws MessagingException, AddressException;

    void setRecipient(int personId) throws MessagingException;

   void setObjectRepository (ObjectRepository objectRepository);
}
