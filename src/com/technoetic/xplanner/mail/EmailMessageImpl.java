package com.technoetic.xplanner.mail;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import net.sf.xplanner.domain.Person;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.HibernateOperations;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.db.hibernate.HibernateOperationsWrapper;
import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.domain.repository.ObjectRepository;
import com.technoetic.xplanner.domain.repository.RepositoryException;

// DEBT: Should not have ANY sql or hql in any domain class. Instead it should be injected with one of the repository
public class EmailMessageImpl implements EmailMessage {
    protected Logger log = Logger.getLogger(getClass());
    private Message msg;
    private StringWriter bodyWriter;
    private ArrayList attachments = new ArrayList();
    HibernateOperationsWrapper hibernateOperationsWrapper;
   private ObjectRepository objectRepository;

   /*package scope*/
   EmailMessageImpl() throws MessagingException {
       hibernateOperationsWrapper = new HibernateOperationsWrapper(ThreadSession.get());
       Properties transportProperties = new Properties();
      XPlannerProperties xplannerProperties = new XPlannerProperties();
      Object hostName = xplannerProperties.getProperty("xplanner.mail.smtp.host");
       if (hostName != null){
         transportProperties.put("mail.smtp.host", hostName);
       }
       String userName = xplannerProperties.getProperty("xplanner.mail.smtp.user");
       SmtpAuthenticator auth = null;
       if(StringUtils.isNotBlank(userName)){
           String password = xplannerProperties.getProperty("xplanner.mail.smtp.password");
           auth = new SmtpAuthenticator(userName, password);
    	   transportProperties.put("mail.smtp.auth", "true");
    	   transportProperties.put("mail.smtp.starttls.enable", "true");
       }
      Object portNbr = xplannerProperties.getProperty("xplanner.mail.smtp.port");
       if (portNbr != null){
         transportProperties.put("mail.smtp.port", portNbr);
         transportProperties.put("mail.smtp.socketFactory.port", portNbr);
       }
       Session session = Session.getDefaultInstance(transportProperties, auth);
       session.setDebug(log.isDebugEnabled());
       msg = new MimeMessage(session);
       msg.setSentDate(new Date());
   }

   public void setHibernateOperations(HibernateOperations hibernateOperations)
   {
      this.hibernateOperationsWrapper = new HibernateOperationsWrapper(hibernateOperations);
   }

    public void setFrom(String from) throws AddressException, MessagingException {
        msg.setFrom(new InternetAddress(from));
    }

    public void setRecipient(int personId) throws MessagingException {
        try {
            Person person = getPerson(personId);
            if (StringUtils.isEmpty(person.getEmail())) {
                throw new MessagingException("no email address for user: uid=" + person.getUserId() + ",id=" + person.getId());
            }
            setRecipients(person.getEmail());
        } catch (MessagingException e) {
            throw e;
        } catch (Exception e) {
            throw new MessagingException("error setting recipient", e);
        }
    }

   public void setObjectRepository(ObjectRepository objectRepository) {
       this.objectRepository = objectRepository;
   }

   private Person getPerson(int personId) throws RepositoryException {
      return (Person) objectRepository.load(personId);
   }

    public void setRecipients(String recipients) throws AddressException, MessagingException {
        if (StringUtils.isNotEmpty(recipients)) {
            setRecipients(Message.RecipientType.TO, recipients.split(","));
        }
    }

    public void setRecipients(String[] recipients) throws AddressException, MessagingException {
        InternetAddress[] addresses = new InternetAddress[recipients.length];
        for (int i = 0; i < recipients.length; i++) {
            addresses[i] = new InternetAddress(recipients[i]);
        }
        msg.setRecipients(Message.RecipientType.TO, addresses);
    }

    public void setCcRecipients(String recipients) throws MessagingException, AddressException {
        if (StringUtils.isNotEmpty(recipients)) {
            setRecipients(Message.RecipientType.CC, recipients.split(","));
        }
    }

    private void setRecipients(Message.RecipientType recipientType, String[] recipients) throws AddressException, MessagingException {
        if (recipients.length > 0) {
            InternetAddress[] addresses = new InternetAddress[recipients.length];
            for (int i = 0; i < recipients.length; i++) {
                addresses[i] = new InternetAddress(recipients[i]);
            }
            msg.setRecipients(recipientType, addresses);
        }
    }

    public void setBody(String body) throws MessagingException {
        bodyWriter = new StringWriter();
        bodyWriter.write(body);
    }

    public PrintWriter getBodyWriter() {
        bodyWriter = new StringWriter();
        return new PrintWriter(bodyWriter);
    }

    public void setSubject(String subject) throws MessagingException {
        msg.setSubject(subject);
    }

    public void setSentDate(Date sentDate) throws MessagingException {
        msg.setSentDate(sentDate);
    }

    public void addAttachment(String filename) throws MessagingException {
        File file = new File(filename);
        addAttachment(file.getName(), file);
    }

    public void addAttachment(String filename, File file) throws MessagingException {
        MimeBodyPart part = new MimeBodyPart();
        FileDataSource fds = new FileDataSource(file);
        part.setDataHandler(new DataHandler(fds));
        part.setFileName(filename);
        attachments.add(part);
    }

    public void send() throws MessagingException {
        MimeMultipart parts = new MimeMultipart();
        if (bodyWriter == null) {
            setBody("");
        }
        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(bodyWriter.toString(), "text/html");
        parts.addBodyPart(bodyPart);
        Iterator iter = attachments.iterator();
        while (iter.hasNext()) {
            parts.addBodyPart((MimeBodyPart)iter.next());
        }
        msg.setContent(parts);
        
        Transport.send(msg);
    }
}
