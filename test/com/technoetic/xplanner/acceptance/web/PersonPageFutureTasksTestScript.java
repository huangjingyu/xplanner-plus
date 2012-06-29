package com.technoetic.xplanner.acceptance.web;

import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Task;

import com.technoetic.xplanner.testing.DateHelper;

public class PersonPageFutureTasksTestScript extends AbstractPageTestScript {
   private Person person;
   Task futureTaskPersonIsAcceptorFor;
   Task futureTaskWithNoAcceptor;

   protected void setUp() throws Exception {
      super.setUp();
      project = newProject();
      iteration = newIteration(project);
      iteration.setStartDate(DateHelper.getDateDaysFromToday(7));
      iteration.setEndDate(DateHelper.getDateDaysFromToday(21));
      story = newUserStory(iteration);
      futureTaskPersonIsAcceptorFor = newTask(story);
      futureTaskWithNoAcceptor= newTask(story);
      person = newPerson();
      futureTaskPersonIsAcceptorFor.setAcceptorId(person.getId());
      commitCloseAndOpenSession();
      tester.login(person.getUserId(),"test");
      tester.clickLinkWithKey("navigation.me");
   }

   public void testFutureTasks() throws Exception {
      tester.assertKeyNotPresent("person.label.future_tasks.none");
      tester.assertTextPresent(futureTaskPersonIsAcceptorFor.getName());
      tester.assertTextNotPresent(futureTaskWithNoAcceptor.getName());
   }
}
