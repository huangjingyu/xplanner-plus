package com.technoetic.xplanner.actions;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import net.sf.xplanner.domain.Person;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.util.MessageResources;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.domain.Integration;
import com.technoetic.xplanner.domain.repository.MetaRepository;
import com.technoetic.xplanner.mail.EmailMessage;
import com.technoetic.xplanner.mail.EmailMessageFactory;

public class IntegrationEmailNotifier implements IntegrationListener {
   private Logger log = Logger.getLogger(getClass());

   Properties properties;
   MetaRepository metaRepository;

   public void setProperties(Properties properties) {
      this.properties = properties;
   }

   public void setMetaRepository(MetaRepository metaRepository) {
      this.metaRepository = metaRepository;
   }

   public void onEvent(int eventType, Integration integration, HttpServletRequest request) {
      if (eventType == INTEGRATION_READY_EVENT) {
         try {
            MessageResources resources = (MessageResources) request.getAttribute(Globals.MESSAGES_KEY);
            EmailMessage email =
                  new EmailMessageFactory(metaRepository.getRepository(Person.class)).createMessage();
            email.setFrom(properties.getProperty(XPlannerProperties.EMAIL_FROM));
            email.setRecipient(integration.getPersonId());
            email.setCcRecipients(properties.getProperty("xplanner.integration.mail.cc"));
            email.setSubject(resources.getMessage("integrations.notification.subject"));
            String link = request.getScheme() + "://" + request.getServerName() + ":" +
                          request.getServerPort() + request.getContextPath() +
                          "/do/view/integrations?projectId=" + integration.getProjectId();
            email.setBody(resources.getMessage("integrations.notification.text", link));
            email.send();
         } catch (Exception ex) {
            log.error("couldn't send notification", ex);
         }
      }
   }
}