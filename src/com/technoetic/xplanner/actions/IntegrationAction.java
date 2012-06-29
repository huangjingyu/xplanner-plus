package com.technoetic.xplanner.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.hibernate.Hibernate;
import org.hibernate.classic.Session;
import org.hibernate.type.Type;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.domain.Integration;

// todo - add history event for completed integration.

public class IntegrationAction extends AbstractAction implements BeanFactoryAware {
   private final Logger log = Logger.getLogger(getClass());
   private static final String NOTIFICATIONS_DISABLED = "nonotify";

   private Collection listeners = Collections.synchronizedList(new ArrayList());
   private AutowireCapableBeanFactory beanFactory;


   public void init() {
      XPlannerProperties props = new XPlannerProperties();
      String listenersString = props.getProperty("xplanner.integration.listeners");
      if (StringUtils.isNotEmpty(listenersString)) {
         String[] listeners = listenersString.split(",");
         for (int i = 0; i < listeners.length; i++) {
            try {
               addIntegrationListener((IntegrationListener) Class.forName(listeners[i]).newInstance());
            } catch (Exception ex) {
               log.error("error initializing listeners", ex);
            }
         }
      }
   }

   @Override
protected ActionForward doExecute(ActionMapping actionMapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse reply)
         throws Exception {
      try {
         Session session = getSession(request);
         session.connection().setAutoCommit(false);
         try {
            ActionForward forward = actionMapping.findForward("display");
            int projectId = Integer.parseInt(request.getParameter("projectId"));
            if (request.getParameter("action.join") != null) {
               forward = onJoinRequest(session, request, actionMapping, projectId);
            } else if (isLeaveRequest(request)) {
               onLeaveRequest(session, request, projectId);
            } else if (request.getParameter("action.start") != null) {
               forward = onStartRequest(session, actionMapping, request, projectId);
            } else if (request.getParameter("action.finish") != null) {
               onFinishRequest(session, request, projectId);
            } else if (request.getParameter("action.cancel") != null) {
               onCancelRequest(session, request, projectId);
            } else if (request.getParameter("personId") != null &&
                       request.getParameter("comment") != null) {
               // default if no action, <cr> in comment field instead of pressing button
               forward = onStartRequest(session, actionMapping, request, projectId);
            }
            session.flush();
            session.connection().commit();
            return addProjectId(request, forward);
         } catch (Exception ex) {
            session.connection().rollback();
            return actionMapping.findForward("error");
         }
      } catch (Exception ex) {
         throw new ServletException("session error", ex);
      }
   }

   private ActionForward addProjectId(HttpServletRequest request, ActionForward forward) {
      return new ActionForward(forward.getPath() + "?projectId=" + request.getParameter("projectId"),
                               forward.getRedirect());
   }

   private ActionForward onJoinRequest(Session session, HttpServletRequest request,
                                       ActionMapping actionMapping, int projectId) throws Exception {
      String personId = request.getParameter("personId");
      String comment = request.getParameter("comment");

      if (StringUtils.isEmpty(personId) || personId.equals("0")) {
         saveError(request, "integrations.error.noperson");
         return actionMapping.findForward("error");
      }
      Integration integration = new Integration();
      integration.setProjectId(Integer.parseInt(request.getParameter("projectId")));
      integration.setPersonId(Integer.parseInt(personId));
      integration.setComment(comment);
      integration.setState(Integration.PENDING);
      integration.setWhenRequested(new Date());

      List pendingIntegrations = getIntegrationsInState(session, Integration.PENDING, projectId);
      if (pendingIntegrations.size() == 0) {
         Integration activeIntegration = getFirstIntegrationInState(session, Integration.ACTIVE, projectId);
         if (activeIntegration == null) {
            startIntegration(integration);
         }
      }

      session.save(integration);
      return actionMapping.findForward("display");
   }

   private void onLeaveRequest(Session session, HttpServletRequest request, int projectId) throws Exception {
      Integer oid = getLeaveOid(request);

      List pendingIntegrations = getIntegrationsInState(session, Integration.PENDING, projectId);
      Integration firstPendingIntegration = (Integration) pendingIntegrations.get(0);

      Integration leavingIntegration = (Integration) session.load(Integration.class, oid);
      session.delete(leavingIntegration);

      if (isNotificationEnabled(request) &&
          leavingIntegration.getId() == firstPendingIntegration.getId() &&
          pendingIntegrations.size() > 1) {
         fireIntegrationEvent(IntegrationListener.INTEGRATION_READY_EVENT,
                              (Integration) pendingIntegrations.get(1), request);
      }
   }

   private Integer getLeaveOid(HttpServletRequest request) {
      return new Integer(getLeavesParameter(request).substring(13));
   }

   private boolean isLeaveRequest(HttpServletRequest request) {
      return getLeavesParameter(request) != null;
   }

   private String getLeavesParameter(HttpServletRequest request) {
      Enumeration names = request.getParameterNames();
      while (names.hasMoreElements()) {
         String name = (String) names.nextElement();
         if (name.startsWith("action.leave")) {
            return name;
         }
      }
      return null;
   }

   private ActionForward onStartRequest(Session session,
                                        ActionMapping actionMapping,
                                        HttpServletRequest request,
                                        int projectId)
         throws Exception {
      Integration activeIntegration = getFirstIntegrationInState(session, Integration.ACTIVE, projectId);
      if (activeIntegration == null) {
         Integration integration = getFirstIntegrationInState(session, Integration.PENDING, projectId);
         startIntegration(integration);
         return actionMapping.findForward("display");
      } else {
         saveError(request, "integrations.error.alreadyactive");
         return actionMapping.findForward("error");
      }
   }

   private void saveError(HttpServletRequest request, String key) {
      ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
      if (errors == null) {
         errors = new ActionErrors();
         // saveErrors() will not save an empty error collection
         request.setAttribute(Globals.ERROR_KEY, errors);
      }
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionError(key));
   }

   private void startIntegration(Integration integration) {
      integration.setState(Integration.ACTIVE);
      integration.setWhenStarted(new Date());
   }

   private void onFinishRequest(Session session, HttpServletRequest request, int projectId) throws Exception {
      terminateIntegration(session, Integration.FINISHED, request, projectId);
   }

   private void terminateIntegration(Session session, char terminalState, HttpServletRequest request, int projectId)
         throws Exception {
      Integration integration = getFirstIntegrationInState(session, Integration.ACTIVE, projectId);
      integration.setState(terminalState);
      integration.setWhenComplete(new Date());

      Integration readyIntegration = getFirstIntegrationInState(session, Integration.PENDING, projectId);
      if (isNotificationEnabled(request) && readyIntegration != null) {
         fireIntegrationEvent(IntegrationListener.INTEGRATION_READY_EVENT, readyIntegration, request);
      }
   }

   private List getIntegrationsInState(Session session, char state, int projectId) throws Exception {
      return session.find("from integration in " + Integration.class +
                          " where integration.state = ? and integration.projectId = ?",
                          new Object[]{new Character(state), new Integer(projectId)},
                          new Type[]{Hibernate.CHARACTER, Hibernate.INTEGER});
   }

   private Integration getFirstIntegrationInState(Session session, char state, int projectId) throws Exception {
      Iterator iter = getIntegrationsInState(session, state, projectId).iterator();
      return (Integration) (iter.hasNext() ? iter.next() : null);
   }

   private void onCancelRequest(Session session, HttpServletRequest request, int projectId) throws Exception {
      terminateIntegration(session, Integration.CANCELED, request, projectId);
   }

   public void setIntegrationListeners(ArrayList listeners) {
      this.listeners = listeners;
   }

   public void addIntegrationListener(IntegrationListener listener) {
      beanFactory.autowireBeanProperties(listener, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, true);
      listeners.add(listener);
   }

   private void fireIntegrationEvent(int eventType, Integration integration, HttpServletRequest request) {
      Iterator iter = listeners.iterator();
      while (iter.hasNext()) {
         IntegrationListener listener = (IntegrationListener) iter.next();
         try {
            listener.onEvent(eventType, integration, request);
         } catch (Throwable ex) {
            log.error("error dispatching integration event", ex);
         }
      }
   }

   // This can be used to disable notifications during acceptance/functional testing
   private boolean isNotificationEnabled(HttpServletRequest request) {
      return StringUtils.isEmpty(request.getParameter(NOTIFICATIONS_DISABLED)) &&
             StringUtils.isEmpty((String) request.getAttribute(NOTIFICATIONS_DISABLED));
   }

   public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
      this.beanFactory = (AutowireCapableBeanFactory) beanFactory;
   }
}