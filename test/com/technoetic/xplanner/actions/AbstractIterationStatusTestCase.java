/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Dec 23, 2005
 * Time: 12:01:09 PM
 */
package com.technoetic.xplanner.actions;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;

import java.util.ArrayList;
import java.util.List;

import net.sf.xplanner.domain.History;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import com.technoetic.xplanner.charts.DataSampler;
import com.technoetic.xplanner.db.hibernate.GlobalSessionFactory;
import com.technoetic.xplanner.domain.DomainMetaDataRepository;
import com.technoetic.xplanner.forms.IterationStatusEditorForm;

public class AbstractIterationStatusTestCase extends AbstractActionTestCase {
   public static final int ITERATION_ID = 99;
   public static final int PROJECT_ID = 100;
   Project project;
   Iteration iteration;
   UserStory story;
   IterationStatusEditorForm editorForm;
   DataSampler mockDataSampler;
   public History event;

   @Override
public void setUp() throws Exception
   {
      super.setUp();
      setUpProjectAndIterationData();
      support.setUpSubjectInRole("editor");
      support.setForward(AbstractAction.TYPE_KEY, Iteration.class.getName());
      support.setForward("view/projects", "projects.jsp");
      editorForm = new IterationStatusEditorForm();
      editorForm.setOid(""+ITERATION_ID);
      support.form = editorForm;
      AbstractIterationAction iterationAction = (AbstractIterationAction) action;
      mockDataSampler = createMock(DataSampler.class);
      iterationAction.setDataSampler(mockDataSampler);
      event = mom.newHistory(project.getId(),
                                     iteration.getId(),
                                     DomainMetaDataRepository.ITERATION_TYPE_NAME,
                                     null,
                                     null);
      event.setId(0);


      expectObjectRepositoryAccess(Iteration.class);
      expectObjectRepositoryAccess(Project.class);
      expect(mockObjectRepository.load(ITERATION_ID)).andReturn(iteration);
      expect(mockObjectRepository.load(PROJECT_ID)).andReturn(project);
      mockObjectRepository.update(iteration);

   }

   private void setUpProjectAndIterationData() {
      project = new Project();
      project.setId(PROJECT_ID);
      iteration = new Iteration();
      iteration.setId(ITERATION_ID);
      iteration.setProject(project);
      story = new UserStory();
      story.setEstimatedHoursField(3.0);
      Task task = new Task();
      task.setEstimatedHours(4.0);
      List<Task> tasks = new ArrayList<Task>();
      tasks.add(task);
      story.setTasks(tasks);
      List<UserStory> stories = new ArrayList<UserStory>();
      stories.add(story);
      iteration.setUserStories(stories);
      List<Iteration> iterations = new ArrayList<Iteration>();
      iterations.add(iteration);
      project.setIterations(iterations);
   }

   @Override
public void tearDown() throws Exception
   {
      super.tearDown();
      GlobalSessionFactory.set(null);
   }

   protected void resetHibernateSession()
   {
//      mockSession = new MockSession()
//      {
//         public Transaction beginTransaction()
//         {
//            return mockTransaction;
//         }
//      };
//      support.hibernateSession = (MockSession) mockSession;
//      ThreadSession.set(mockSession);
   }
}