package com.technoetic.xplanner.domain.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.UserStory;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.auth.Authorizer;
import com.technoetic.xplanner.util.TimeGenerator;

//DOORDIE Test THIS!

public class UserStoryRepository {
   private final Session session;
   private final int personId;
   private final Authorizer authorizer;
   private final IterationRepository iterationRepository;
   protected static final String USER_STORY_WE_CAN_MOVE_TASKS_TO_QUERY =
         "com.technoetic.xplanner.domain.StoriesOfCurrentAndFutureIterationOfAllVisibleProjects";

   public UserStoryRepository(Session session, Authorizer authorizer, int personId) {
      this.session = session;
      this.personId = personId;
      this.authorizer = authorizer;
      iterationRepository = new IterationRepository(session, authorizer, personId);
   }

   public Iteration getIteration(UserStory story) throws HibernateException {
      return iterationRepository.getIterationForStory(story);
   }

   public List fetchStoriesWeCanMoveTasksTo(int actualStoryId) throws HibernateException, AuthenticationException {
      TimeGenerator timeGenerator = new TimeGenerator();
      Date currentDate = timeGenerator.getTodaysMidnight();
      Query query = getSession().getNamedQuery(USER_STORY_WE_CAN_MOVE_TASKS_TO_QUERY);
      query.setParameter("currentDate", currentDate);
      query.setParameter("actualStoryId", new Integer(actualStoryId));
      List allStories = query.list();
      List acceptedStories = new ArrayList();
      for (int i = 0; i < allStories.size(); i++) {
         UserStory it = (UserStory) allStories.get(i);
         if (accept(it)) {
            acceptedStories.add(it);
         }
      }
      return acceptedStories;
   }

   public UserStory getUserStory(int storyId) throws HibernateException {
      return (UserStory) session.get(UserStory.class, new Integer(storyId));
   }

   private boolean accept(UserStory story) throws AuthenticationException, HibernateException {
      return authorizer.hasPermission(getIteration(story).getProject().getId(), personId, story, "edit");
   }

   protected Session getSession() {
      return session;
   }


}