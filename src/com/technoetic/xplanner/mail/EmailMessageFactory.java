/*
 * Copyright (c) Mateusz Prokopowicz. All Rights Reserved.
 */

package com.technoetic.xplanner.mail;

import com.technoetic.xplanner.domain.repository.ObjectRepository;

public class EmailMessageFactory {
   private ObjectRepository personRepository;
    private Class emailMessageClass;
    public EmailMessage emailMessage;

   public EmailMessageFactory(ObjectRepository objectRepository) {
      this.personRepository = objectRepository;
   }

   public EmailMessage createMessage() throws Exception {
        if (emailMessageClass == null) {
            emailMessageClass = EmailMessageImpl.class;
        }

        if(emailMessage == null){
           EmailMessage emailMessage = (EmailMessage) emailMessageClass.newInstance();
           emailMessage.setObjectRepository(personRepository);
           return emailMessage;
        } else {
            return emailMessage;
        }

    }

    public void setEmailMessageClass(Class emailMessageClass) {
        this.emailMessageClass = emailMessageClass;
    }

    public void setEmailMessage(EmailMessage newEmailMessage) throws java.lang.InstantiationException, java.lang.IllegalAccessException {
        emailMessage = newEmailMessage;
    }
}
