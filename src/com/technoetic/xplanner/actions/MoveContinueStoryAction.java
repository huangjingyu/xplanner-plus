package com.technoetic.xplanner.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.UserStory;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.domain.RelationshipConvertor;
import com.technoetic.xplanner.domain.RelationshipMappingRegistry;
import com.technoetic.xplanner.domain.repository.RepositoryException;
import com.technoetic.xplanner.forms.MoveContinueStoryForm;

public class MoveContinueStoryAction extends EditObjectAction {
	public static final String CONTINUE_ACTION = "Continue";
	public static final String MOVE_ACTION = "Move";

	private MoveContinueStory moveContinueStory;

	public void setMoveContinueStory(MoveContinueStory moveContinueStory) {
		this.moveContinueStory = moveContinueStory;
	}

	@Override
	public ActionForward doExecute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse reply) throws Exception {
		Session session = getSession(request);
		MoveContinueStoryForm storyForm = (MoveContinueStoryForm) actionForm;
		if (storyForm.isSubmitted()) {
			saveForm(storyForm, session, request);
			String returnto = request.getParameter(RETURNTO_PARAM);
			if (returnto != null) {
				return new ActionForward(returnto, true);
			} else {
				return actionMapping.findForward("view/projects");
			}
		} else {
			populateForm(storyForm, session);
			return new ActionForward(actionMapping.getInput());
		}
	}

	private void saveForm(MoveContinueStoryForm storyForm, Session session, HttpServletRequest request)
			throws Exception {
		UserStory story = getStory(Integer.parseInt(storyForm.getOid()));
		populateObject(request, story, storyForm);
		Iteration originalIteration = getIteration(story.getIteration().getId());
		Iteration targetIteration = getIteration(storyForm.getTargetIterationId());

		if (storyForm.getAction().equals(MOVE_ACTION)) {
			moveContinueStory.moveStory(story, targetIteration, originalIteration, request, session);
		} else if (storyForm.getAction().equals(CONTINUE_ACTION)) {
			moveContinueStory.continueStory(story, originalIteration, targetIteration, request, session);
		} else {
			throw new ServletException("Unknown editor action: " + storyForm.getAction());
		}
		moveContinueStory.reorderIterationStories(originalIteration);
		moveContinueStory.reorderIterationStories(targetIteration);
		storyForm.setAction(null);
	}

	private UserStory getStory(int id) throws RepositoryException {
		return (UserStory) getCommonDao().getById(UserStory.class, id);
	}

	private void populateForm(MoveContinueStoryForm storyForm, Session session) throws Exception {
		String oid = storyForm.getOid();
		if (oid != null) {
			UserStory story = (UserStory) session.load(UserStory.class, new Integer(oid));
			Iteration iteration = story.getIteration();
			storyForm.setFutureIteration(iteration.isFuture());
			storyForm.setIterationId(iteration.getId());
			PropertyUtils.copyProperties(storyForm, story);
			populateManyToOneIds(storyForm, story);
		}
	}

	private void populateManyToOneIds(ActionForm form, UserStory story) throws IllegalAccessException,
			NoSuchMethodException, InvocationTargetException {
		Collection mappings = RelationshipMappingRegistry.getInstance().getRelationshipMappings(story);
		for (Iterator iterator = mappings.iterator(); iterator.hasNext();) {
			RelationshipConvertor convertor = (RelationshipConvertor) iterator.next();
			convertor.populateAdapter(form, story);
		}
	}

}
