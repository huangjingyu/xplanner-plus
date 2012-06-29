package com.technoetic.xplanner.acceptance.web;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.UserStory;

import com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript;
import com.technoetic.xplanner.db.hibernate.IdGenerator;

public class DatabaseMappingsTestScript extends AbstractDatabaseTestScript {
   public void testMappings() throws Exception {
      Project project = newProject();
      Iteration iteration = newIteration(project);
      UserStory story = newUserStory(iteration);
      Person person = newPerson(IdGenerator.getUniqueId("UserId"));
      story.setCustomer(person);
      commitCloseAndOpenSession();
      UserStory savedStory = (UserStory) getSession().load(UserStory.class, new Integer(story.getId()));
      assertEquals(person, savedStory.getCustomer());
   }

}