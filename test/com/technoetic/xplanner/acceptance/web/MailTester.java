/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.technoetic.xplanner.acceptance.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.lang.StringUtils;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import com.technoetic.xplanner.XPlannerProperties;

public class MailTester {
   private int currentDayOffset = 0;
   private XPlannerWebTester tester;

   public int getCurrentDayOffset() {
      return currentDayOffset;
   }

   public static class Email {
      public String subject;
      public String[] recipients;
      public String from;
      public List bodyElements;

      public Email(String from, String[] recipients, String subject, List bodyElements){
         this.subject = subject;
         this.recipients = recipients;
         this.from = from;
         this.bodyElements = bodyElements;
      }

      public Email(String from, String subject, List bodyElements){
         this(from, new String[0], subject, bodyElements);
      }

      public boolean isEqual(SmtpMessage message) {
         if (!isSubjectEqual(message)) return false;
         if (!isFromAddressEqual(message)) return false;
         if (!isBodyContainingElements(message)) return false;
         return isRecipientsEqual(message);

      }

      private boolean isRecipientsEqual(SmtpMessage message) {
         List messageRecipients = new ArrayList();
         messageRecipients.addAll(Arrays.asList(message.getHeaderValues("To")));
         messageRecipients.addAll(Arrays.asList(message.getHeaderValues("Cc")));
         messageRecipients.addAll(Arrays.asList(message.getHeaderValues("Bcc")));
         return messageRecipients.containsAll(Arrays.asList(recipients));
      }

      private boolean isBodyContainingElements(SmtpMessage message) {
         String trimmedBody = StringUtils.deleteWhitespace(message.getBody());
         for (Iterator iterator = bodyElements.iterator(); iterator.hasNext();) {
            String element = (String) iterator.next();
            if (!StringUtils.contains(trimmedBody,
                                      StringUtils.deleteWhitespace(element))) {
               return false;
            }
         }
         return true;
      }

      protected boolean isFromAddressEqual(SmtpMessage message) {
         return StringUtils.equals(from, message.getHeaderValue("From"));
      }

      private boolean isSubjectEqual(SmtpMessage message) {
         return StringUtils.equals(subject, message.getHeaderValue("Subject"));
      }

      public String toString() {
         String result = "Email(from=" + from + ", " + "to={";
         for (int i=0; i<recipients.length; i++){
            if (i>0) result+=",";
            result+=recipients[i];
         }
         result+="}"+", subject=" + subject + ", body includes {\n";
         for (int i=0; i<bodyElements.size(); i++){
            if (i>0) result+=",\n";
            result+=bodyElements.get(i);
         }
         result+="\n})";
         return result;
      }
   }

   private SimpleSmtpServer smtpServer;

   public MailTester(XPlannerWebTester tester) {
      this.tester = tester;
   }

   public void assertNumberOfEmailReceivedBy(int expectedNumberOfEmailReceived, String receiver) {
      int cnt = 0;
      for (Iterator iterator = smtpServer.getReceivedEmail(); iterator.hasNext();) {
        SmtpMessage message = (SmtpMessage) iterator.next();
         if (Arrays.asList(message.getHeaderValues("To")).contains(receiver)) {
            cnt++;
         }
      }
      if (expectedNumberOfEmailReceived != cnt) {
         String message = "number of email sent to "+ receiver +" expected " + expectedNumberOfEmailReceived + " was " +
                          cnt +"\n messages received=" + getAllEmailsReceived();
         Assert.fail(message);
      }
   }

   public void assertEmailHasNotBeenReceived(Email email) throws InterruptedException {
      boolean isFound = hasEmailBeenReceived(email);
      Assert.assertFalse("A message " + email + " has been sent", isFound);
   }

   public void assertEmailHasNotBeenReceived(String recipient, Email email) throws InterruptedException {
      email.recipients = new String[]{recipient};
      assertEmailHasNotBeenReceived(email);
   }

   public void assertEmailHasBeenReceived(Email expectedEmail) throws InterruptedException {
      boolean isFound = hasEmailBeenReceived(expectedEmail);
      if (!isFound) {
         Assert.fail("A message " + expectedEmail + " not found in \n" + getAllEmailsReceived());
      }
   }

   public void assertEmailHasBeenReceived(String recipient, Email email) throws InterruptedException {
      email.recipients = new String[] {recipient};
      assertEmailHasBeenReceived(email);
   }

   public static final int SECOND = 2;

   private boolean hasEmailBeenReceived(Email email) throws InterruptedException {
      int timeout = 4 * SECOND;
      while (--timeout > 0) {
         Thread.sleep(500);
         if (hasEmailArrived(email)) return true;
      }
      return false;
   }

   private boolean hasEmailArrived(Email email) {
      for (Iterator it = smtpServer.getReceivedEmail(); it.hasNext();) {
         SmtpMessage message = (SmtpMessage) it.next();
         if (email.isEqual(message)) return true;
      }
      return false;
   }

   public void setUp() throws Exception{
      currentDayOffset = 0;
      startSmtp();
   }

   private void startSmtp() throws InterruptedException {
      int port = Integer.parseInt(new XPlannerProperties().getProperty("xplanner.mail.smtp.port"));
      smtpServer = new SimpleSmtpServer(port);
      Thread t = new Thread(smtpServer);
      tryStartNTimes(t, 10);
   }

   private void tryStartNTimes(Thread t, int tries) throws InterruptedException {
      while (tries > 0) {
         try {
            t.start();
            break;
         } catch (Exception e) {
            Thread.sleep(100);
            tries--;
         }
      }
   }

   public void tearDown() {
      currentDayOffset = 0;
      stopSmtp();
   }

   private void stopSmtp() {
      if (smtpServer != null && !smtpServer.isStopped()) {
         smtpServer.stop();
      }
   }

   public void resetSmtp() throws Exception {
      stopSmtp();
      startSmtp();
   }
   public String getAllEmailsReceived() {
      String result = "{\n";
      Iterator email = smtpServer.getReceivedEmail();
      while (email.hasNext()) {
         SmtpMessage message = (SmtpMessage) email.next();
         if (email.hasNext()) {
            result += "  " + message + "\n###########################################################\n";
         }
      }
      result += "}";
      return result;
   }

   public void moveCurrentDayAndSendEmail(int days) throws UnsupportedEncodingException {
      tester.moveCurrentDay(days);
      tester.executeTask("/do/edit/missingTimeEntryNotification");
   }
}