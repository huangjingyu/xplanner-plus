package com.technoetic.xplanner.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.UserStory;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.domain.repository.RepositoryException;
import com.technoetic.xplanner.forms.MoveStoriesForm;

public class MoveStoriesAction extends AbstractAction<UserStory> {
	public static final String MOVE_ACTION = "Move";

	private MoveContinueStory moveContinueStory;

	public void setMoveContinueStory(MoveContinueStory moveContinueStory) {
		this.moveContinueStory = moveContinueStory;
	}

	public MoveContinueStory getMoveContinueStory() {
		return moveContinueStory;
	}

	@Override
	protected ActionForward doExecute(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse reply) throws Exception {

		MoveStoriesForm moveStoriesForm = (MoveStoriesForm) actionForm;
		int targetIterationId = moveStoriesForm.getTargetIterationId();

		if (targetIterationId > 0) {
			Session session = getSession(request);
			Iteration targetIteration = getIteration(targetIterationId);
			Iteration iteration = getIteration(Integer.parseInt(moveStoriesForm.getIterationId()));

			if (targetIteration.getId() != iteration.getId()) {
				for (String storyId : moveStoriesForm.getStoryIds()) {
					UserStory story = getStory(Integer.parseInt(storyId));
					moveContinueStory.moveStory(story, targetIteration,
							iteration, request, session);
				}
				moveContinueStory.reorderIterationStories(iteration);
				moveContinueStory.reorderIterationStories(targetIteration);
			}

			ActionForward actionForward = new ActionForward(
					"/do/view/iteration?oid=" + iteration.getId());
			actionForward.setRedirect(true);
			return actionForward;
		} else {
			return new ActionForward(actionMapping.getInput());
		}
	}

	private UserStory getStory(int id) throws RepositoryException {
		return getCommonDao().getById(UserStory.class, id);
	}
}
