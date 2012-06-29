package com.technoetic.xplanner.acceptance.repository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junitx.framework.ArrayAssert;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Role;
import net.sf.xplanner.domain.UserStory;

import org.hibernate.HibernateException;

import com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript;
import com.technoetic.xplanner.domain.repository.RepositoryException;
import com.technoetic.xplanner.domain.repository.UserStoryRepository;
import com.technoetic.xplanner.security.auth.Authorizer;
import com.technoetic.xplanner.util.TimeGenerator;

//TODO Setup hypersonic to run these tests in memory only

public class UserStoryRepositoryTestScript extends AbstractDatabaseTestScript {
   private Project project;
   private Iteration iteration;
   private UserStory story;
   private Date currentDate;
   private Project project2;
   private Project project1;
   private Person person;
   private TimeGenerator timeGenerator;
   private Date startDate;
   private Date endDate;

   @Override
protected void setUp() throws Exception {
      timeGenerator = new TimeGenerator();
      currentDate = timeGenerator.getTodaysMidnight();
      super.setUp();
      project = newProject();
      iteration = newIteration(project);
      startDate = TimeGenerator.shiftDate(currentDate, Calendar.DATE, -7);
      endDate = TimeGenerator.shiftDate(currentDate, Calendar.DATE, 7);
      iteration.setStartDate(startDate);

      iteration.setEndDate(endDate);
      story = newUserStory(iteration);
      project2 = newProject();
      project1 = newProject();
      person = newPerson();
      commitCloseAndOpenSession();

      setUpPersonRole(project, person, Role.EDITOR);
      setUpPersonRole(project1, person, Role.EDITOR);
      setUpPersonRole(project2, person, Role.VIEWER);
   }

   public void testFetchStoriesWeCanMoveTasksTo_CheckPermissionForStoriesInCurrentIterations() throws Exception {

      Iteration secondWritableIteration = newIteration(project1, startDate, endDate);
      Iteration firstNonWritableIteration = newIteration(project2, startDate, endDate);
      Iteration secondNonWritableIteration = newIteration(project2, startDate, endDate);
      startDate = TimeGenerator.shiftDate(currentDate, Calendar.DATE, -8);
      Iteration firstWritableIteration = newIteration(project1, startDate, endDate);

      // test that stories added out of order are returned in the proper order

      UserStory[] expectedStories = new UserStory[4];
      UserStory firstNotWritableStory = newUserStory(firstNonWritableIteration);
      expectedStories[0] = newUserStory(firstWritableIteration);
      UserStory secondNotWritableStory = newUserStory(secondNonWritableIteration);
      expectedStories[1] = newUserStory(firstWritableIteration);
      expectedStories[2] = newUserStory(secondWritableIteration);
      expectedStories[3] = newUserStory(secondWritableIteration);

      commitCloseAndOpenSession();

      UserStoryRepository dao = createUserStoryRepository();
      List list = dao.fetchStoriesWeCanMoveTasksTo(story.getId());

      ArrayAssert.assertEquals("stories returned", expectedStories, list.toArray());
   }

   public void testFetchStoriesWeCanMoveTasksTo_ShowOnlyCurrentAndFutureIteration() throws Exception {
      Iteration previousIterationInTheSameProject =
            newIteration(project,
                         TimeGenerator.shiftDate(currentDate, Calendar.DATE, -22),
                         TimeGenerator.shiftDate(currentDate, Calendar.DATE, -8));
      Iteration previousIterationInAnotherProject =
            newIteration(project1,
                         TimeGenerator.shiftDate(currentDate, Calendar.DATE, -22),
                         TimeGenerator.shiftDate(currentDate, Calendar.DATE, -8));
      Iteration futureIterationInTheSameProject =
            newIteration(project,
                         TimeGenerator.shiftDate(currentDate, Calendar.DATE, 8),
                         TimeGenerator.shiftDate(currentDate, Calendar.DATE, 22));
      Iteration futureIterationInAnotherProject =
            newIteration(project1,
                         TimeGenerator.shiftDate(currentDate, Calendar.DATE, 8),
                         TimeGenerator.shiftDate(currentDate, Calendar.DATE, 22));
      Iteration currentIterationInTheSameProject = newIteration(project, startDate, endDate);
      Iteration currentIterationInAnotherProject = newIteration(project1, startDate, endDate);
      UserStory[] expectedStories = new UserStory[4];
      UserStory storyInPreviousIterationInTheSameProject = newUserStory(previousIterationInTheSameProject);
      UserStory storyInPreviousIterationInAnotherProject = newUserStory(previousIterationInAnotherProject);
      expectedStories[0] = newUserStory(currentIterationInTheSameProject);
      expectedStories[1] = newUserStory(currentIterationInAnotherProject);
      expectedStories[2] = newUserStory(futureIterationInTheSameProject);
      expectedStories[3] = newUserStory(futureIterationInAnotherProject);
      commitCloseAndOpenSession();

      UserStoryRepository dao = createUserStoryRepository();
      List list = dao.fetchStoriesWeCanMoveTasksTo(story.getId());

      ArrayAssert.assertEquivalenceArrays("stories returned", expectedStories, list.toArray());
   }

   private UserStoryRepository createUserStoryRepository() throws Exception {
      Authorizer authorizer = createAuthorizer();
      return new UserStoryRepository(getSession(), authorizer, person.getId());
   }

   private Iteration newIteration(Project project, Date startDate, Date endDate) throws HibernateException,
                                                                                        RepositoryException {
      Iteration iteration = newIteration(project);
      iteration.setStartDate(startDate);
      iteration.setEndDate(endDate);
      return iteration;
   }

}