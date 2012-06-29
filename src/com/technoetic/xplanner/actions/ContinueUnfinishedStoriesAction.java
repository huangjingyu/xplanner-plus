package com.technoetic.xplanner.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.dao.impl.CommonDao;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.UserStory;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.technoetic.xplanner.domain.IterationStatus;
import com.technoetic.xplanner.forms.ContinueUnfinishedStoriesForm;

public class ContinueUnfinishedStoriesAction extends EditObjectAction<UserStory> {
   public static final String OK_ACTION = "Ok";
   public static final String CANCEL_ACTION = "Cancel";
   private StoryContinuer storyContinuer;
@Override
protected ActionForward doExecute(ActionMapping mapping,
                                     ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {
      ContinueUnfinishedStoriesForm continueUnfinishedStoriesForm;
      continueUnfinishedStoriesForm = (ContinueUnfinishedStoriesForm) form;
      Session session = getSession(request);
      try {
         if (continueUnfinishedStoriesForm.isSubmitted()) {
            saveForm(continueUnfinishedStoriesForm, mapping, session, request, form, response);
            String returnto = request.getParameter(RETURNTO_PARAM);
            if (returnto != null) {
               return new ActionForward(returnto, true);
            } else {
               return mapping.findForward("view/projects");
            }
         } else {
            populateForm(continueUnfinishedStoriesForm, session);
            return new ActionForward(mapping.getInput());
         }
      } catch (Exception ex) {
         session.connection().rollback();
         throw new ServletException(ex);
      }
   }

   private void populateForm(ContinueUnfinishedStoriesForm continueUnfinishedStoriesForm, Session session)
         throws HibernateException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      int iterationId = continueUnfinishedStoriesForm.getIterationId();
      if (iterationId != 0) {
         Iteration iteration = (Iteration) session.load(Iteration.class, new Integer(iterationId));
         PropertyUtils.copyProperties(continueUnfinishedStoriesForm, iteration);
         populateManyToOneIds(continueUnfinishedStoriesForm, iteration);
      }
   }

   protected void saveForm(ContinueUnfinishedStoriesForm selectionForm,
                           ActionMapping mapping,
                           Session session,
                           HttpServletRequest request,
                           ActionForm form,
                           HttpServletResponse response) throws Exception {
      String action = selectionForm.getAction();
      if (action.equals(OK_ACTION)) {
         int currentIterationId = selectionForm.getIterationId();
         int targetIterationId = selectionForm.getTargetIterationId();
         continueIteration(currentIterationId, targetIterationId, request, session);
      }
   }

   private void continueIteration(int currentIterationId,
                                  int targetIterationId,
                                  HttpServletRequest request,
                                  Session session) throws Exception {
      Iteration originalIteration = (Iteration) getCommonDao().getById(Iteration.class, currentIterationId);
      Iteration targetIteration = (Iteration) getCommonDao().getById(Iteration.class, targetIterationId);
      continueUnfinishedStoriesInIteration(request, session, originalIteration, targetIteration);
   }

   protected void continueUnfinishedStoriesInIteration(HttpServletRequest request,
                                                       Session session,
                                                       Iteration originalIteration, Iteration targetIteration)
         throws Exception {
      storyContinuer.init(session, request);
      if (originalIteration != null) {
         continueIteration(originalIteration, targetIteration);
      }
   }

   private void continueIteration(Iteration originalIteration, Iteration targetIteration)
         throws HibernateException {
      originalIteration.setStatusKey(IterationStatus.INACTIVE_KEY);
      Collection stories = originalIteration.getUserStories();
      if (targetIteration.getId() != 0) {
         for (Iterator iterator = stories.iterator(); iterator.hasNext();) {
            continueCompleteStory((UserStory) iterator.next(), originalIteration, targetIteration);
         }
      }
   }

   private void continueCompleteStory(UserStory userStory, Iteration originalIteration, Iteration targetIteration)
         throws
         HibernateException {
      if (!userStory.isCompleted()) {
         continueStory(userStory, originalIteration, targetIteration);
      }
   }

   private void continueStory(UserStory userStory,
                              Iteration originalIteration,
                              Iteration targetIteration) throws HibernateException {
      UserStory continuedUserStory = (UserStory) storyContinuer.continueObject(userStory, originalIteration,
                                                                               targetIteration);
      List<UserStory> targetIterationStories = targetIteration.getUserStories();
      if(!targetIterationStories.contains(continuedUserStory)){
    	  targetIterationStories.add(continuedUserStory);
      }
      targetIteration.setUserStories(targetIterationStories);
   }

   public void setStoryContinuer(StoryContinuer storyContinuer) {
      this.storyContinuer = storyContinuer;
   }
}
