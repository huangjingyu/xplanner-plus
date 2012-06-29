package com.technoetic.xplanner.actions;

import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.sf.xplanner.domain.History;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.UserStory;

import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.history.HistorySupport;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.SecurityHelper;

public class MoveContinueStory {
	private StoryContinuer storyContinuer;
	private HistorySupport historySupport;

	public void setStoryContinuer(StoryContinuer storyContinuer) {
		this.storyContinuer = storyContinuer;
	}

	public void setHistorySupport(HistorySupport historySupport) {
		this.historySupport = historySupport;
	}

	public void moveStory(UserStory story, Iteration targetIteration, Iteration originalIteration,
			HttpServletRequest request, Session session) throws AuthenticationException {

		originalIteration.getUserStories().remove(story);
		story.moveTo(targetIteration);
		updateStoryOrderNoInTargetIteration(story, originalIteration, targetIteration);
		saveMoveHistory(story, originalIteration, targetIteration, session, SecurityHelper.getRemoteUserId(request));
	}

	public void continueStory(UserStory story, Iteration originalIteration, Iteration targetIteration,
			HttpServletRequest request, Session session) throws AuthenticationException, HibernateException {

		storyContinuer.init(session, request);
		UserStory targetStory = (UserStory) storyContinuer.continueObject(story, originalIteration, targetIteration);
		updateStoryOrderNoInTargetIteration(targetStory, originalIteration, targetIteration);
	}

	public void reorderIterationStories(Iteration iteration) throws Exception {
		Collection stories = iteration.getUserStories();
		iteration.modifyStoryOrder(DomainOrderer.buildStoryIdNewOrderArray(stories));
	}

	private void updateStoryOrderNoInTargetIteration(UserStory story, Iteration originalIteration,
			Iteration targetIteration) {

		if (originalIteration.getStartDate().compareTo(targetIteration.getStartDate()) <= 0) {
			story.setOrderNo(0);
		} else {
			story.setOrderNo(Integer.MAX_VALUE);
		}
	}

	private void saveMoveHistory(UserStory story, Iteration originIteration, Iteration targetIteration,
			Session session, int currentUserId) {

		Date now = new Date();
		historySupport.saveEvent(story, History.MOVED,
				"from " + originIteration.getName() + " to " + targetIteration.getName(), currentUserId, now);
		historySupport.saveEvent(originIteration, History.MOVED_OUT, story.getName() + " to " + targetIteration.getName(),
				currentUserId, now);
		historySupport.saveEvent(targetIteration, History.MOVED_IN, story.getName() + " from " + originIteration.getName(),
				currentUserId, now);
	}
}
