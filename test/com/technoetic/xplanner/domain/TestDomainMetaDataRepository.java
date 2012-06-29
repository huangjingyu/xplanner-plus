/*
 * Copyright (c) &#36;today.year Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.domain;

import static org.easymock.EasyMock.expect;

import java.util.ArrayList;
import java.util.Arrays;

import junitx.framework.ListAssert;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import com.technoetic.xplanner.AbstractUnitTestCase;

public class TestDomainMetaDataRepository extends AbstractUnitTestCase {
   //DEBT(DATADRIVEN) Should not depend on any production configuration of the repository. This test should create its own so not to be impacted by changes in xplanner business model
   DomainMetaDataRepository repository = DomainMetaDataRepository.getInstance();

   public void testSetParent() throws Exception
   {
      Project project = new Project();
      project.setId(1);
      Iteration iteration = new Iteration();
      iteration.setId(2);
      repository.setParent(iteration, project);
      assertEquals(1, iteration.getProject().getId());
      ListAssert.assertEquals(Arrays.asList(new Object []{iteration}), new ArrayList(project.getIterations()));
   }

   public void testGetParent() throws Exception {
      setUpThreadSession(false);
      Project project = new Project();
      project.setId(1);
      Iteration iteration = new Iteration();
      iteration.setProject(project);

      expect(mockSession.get(Project.class, new Integer(1))).andReturn(project);

      replay();

      assertSame(project, repository.getParent(iteration));

      verify();
   }

   public void testGetParentIdWhenParentIsAnIdField() throws Exception {
      Iteration iteration = new Iteration();
      Project project = new Project();
      project.setId(1);
      iteration.setProject(project);
      assertEquals(1, repository.getParentId(iteration));
   }

   public void testGetParentIdWhenParentIsARelationship() throws Exception {
      Task task = new Task();
      UserStory userStory = new UserStory();
      userStory.setId(1);
      task.setUserStory(userStory);
      assertEquals(1, repository.getParentId(task));
   }

   public void testGetObject() throws Exception
   {
     setUpThreadSession(false);
      Project project = new Project();
      expect(mockSession.get(Project.class, new Integer(1))).andReturn(project);

      replay();

      assertSame(project, repository.getObject(DomainMetaDataRepository.PROJECT_TYPE_NAME, 1));

      verify();

   }

   public void testClassToTypeName() throws Exception {
      assertEquals(DomainMetaDataRepository.PROJECT_TYPE_NAME, repository.classToTypeName(Project.class));
   }
}