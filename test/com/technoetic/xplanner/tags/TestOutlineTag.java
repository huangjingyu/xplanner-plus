package com.technoetic.xplanner.tags;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.security.auth.Subject;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTag;

import junit.framework.TestCase;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import org.easymock.MockControl;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

import com.technoetic.mocks.hibernate.MockSessionFactory;
import com.technoetic.xplanner.XPlannerTestSupport;
import com.technoetic.xplanner.db.hibernate.GlobalSessionFactory;
import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.security.PersonPrincipal;
import com.technoetic.xplanner.security.auth.MockAuthorizer;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;

public class TestOutlineTag extends TestCase {
   private XPlannerTestSupport support;
   private OutlineTag outlineTag;
   Session mockSession = null;
   MockControl mockSessionControl = null;

   public void setUp() throws Exception {
      super.setUp();
      Person person = new Person();
      person.setId(2);
      PersonPrincipal personPrincipal = new PersonPrincipal(person);
      Set principalSet = new HashSet();
      principalSet.add(personPrincipal);
      Subject mockSubject = new Subject(true, principalSet, Collections.EMPTY_SET, Collections.EMPTY_SET);
      MockAuthorizer mockAuthorizer = new MockAuthorizer();
      mockAuthorizer.hasPermission2Return = new Boolean("true");
      SystemAuthorizer.set(mockAuthorizer);
      support = new XPlannerTestSupport();
      support.request.getSession().setAttribute("SECURITY_SUBJECT", mockSubject);
      outlineTag = new OutlineTag();
      outlineTag.setPageContext(support.pageContext);
      support.mapping.setPath("/page.jsp");
      support.request.setContextPath("/xplanner");

      setUpThreadSession();
   }

   private void executeTag() throws JspException {
      int result = outlineTag.doStartTag();
      assertEquals("wrong result", BodyTag.EVAL_BODY_INCLUDE, result);
   }

   public void testRender1() throws Exception {
      DomainContext context = new DomainContext();
      context.setProjectId(1);
      context.setProjectName("Test Project");
      context.save(support.request);
      support.request.setParameterValue("oid", new String[]{"222"});

      setMockObjects(context);

      executeTag();

      String out = support.jspWriter.printValue;

      assertTrue("unexpected project link",
                 out.indexOf("<a href='/do/view/project?oid=" + context.getProjectId() + "'>Test Project</a>") == -1);
      assertTrue("missing project name", out.indexOf("Test Project") != -1);
   }

   public void testRender2() throws Exception {
      DomainContext context = new DomainContext();
      context.setProjectId(1);
      context.setProjectName("Test Project");
      context.setIterationId(2);
      context.setIterationName("Test Iteration");
      context.save(support.request);
      support.request.setParameterValue("oid", new String[]{"222"});

      setMockObjects(context);

      executeTag();

      String out = support.jspWriter.printValue;

      assertTrue("missing test project link",
                 out.indexOf("<a href='/xplanner/do/view/project?oid=1'>Test Project</a>") != -1);
      assertTrue("unexpected iteration link",
                 out.indexOf("<a href='/xplanner/do/view/iteration?oid=2'>Test Iteration</a>") == -1);
      assertTrue("missing iteration name", out.indexOf("Test Iteration") != -1);
   }

   public void testRender3() throws Exception {
      DomainContext context = new DomainContext();
      context.setProjectId(1);
      context.setProjectName("Test Project");
      context.setIterationId(2);
      context.setIterationName("Test Iteration");
      context.setStoryId(3);
      context.setStoryName("Test Story");
      context.save(support.request);
      support.request.setParameterValue("oid", new String[]{"222"});

      setMockObjects(context);

      executeTag();

      String out = support.jspWriter.printValue;

      assertTrue("missing test project link",
                 out.indexOf("<a href='/xplanner/do/view/project?oid=1'>Test Project</a>") != -1);
      assertTrue("missing test iteration link", out.indexOf("<a href='/xplanner/do/view/iteration?oid=2'>") != -1);
      assertTrue("unexpected test story link", out.indexOf("<a href='/xplanner/do/view/userstory?oid=3'>") == -1);
      assertTrue("missing story name", out.indexOf("Test Story") != -1);
   }

   public void testRender4() throws Exception {
      DomainContext context = new DomainContext();
      context.setProjectId(1);
      context.setProjectName("Test Project");
      context.setIterationId(2);
      context.setIterationName("Test Iteration");
      context.setStoryId(3);
      context.setStoryName("Test Story");
      context.setTaskId(4);
      context.setTaskName("Test Task");
      context.save(support.request);
      support.request.setParameterValue("oid", new String[]{"222"});

      setMockObjects(context);

      executeTag();

      String out = support.jspWriter.printValue;

      assertTrue("missing test project link",
                 out.indexOf("<a href='/xplanner/do/view/project?oid=1'>Test Project</a>") != -1);
      assertTrue("missing test iteration link", out.indexOf("<a href='/xplanner/do/view/iteration?oid=2'>") != -1);
      assertTrue("missing test story link", out.indexOf("<a href='/xplanner/do/view/userstory?oid=3'>") != -1);
      assertTrue("missing test task link", out.indexOf("<a href='/xplanner/do/view/task?oid=4'>") == -1);
      assertTrue("missing task name", out.indexOf("Test Task") != -1);
   }

   private void setUpThreadSession() {
      mockSessionControl = MockControl.createControl(Session.class);
      mockSession = (Session) mockSessionControl.getMock();
      MockControl mockTransactionControl = MockControl.createNiceControl(Transaction.class);
      Transaction mockTransaction = (Transaction) mockTransactionControl.getMock();
      try {
         mockSessionControl.expectAndReturn(mockSession.beginTransaction(), mockTransaction);
      } catch (HibernateException e) {
         e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
      }
      ThreadSession.set(mockSession);

      MockSessionFactory mockSessionFactory = new MockSessionFactory();
      mockSessionFactory.openSessionReturn = ThreadSession.get();
      GlobalSessionFactory.set(mockSessionFactory);

      mockSessionControl.reset();
   }

   private void setMockObjects(DomainContext context) {
      try {
         if (context.getProjectId() != 0) {
            List projectList = new LinkedList();
            Project project = new Project();
            project.setId(context.getProjectId());
            project.setName("TestProject");
            projectList.add(project);

            mockSession.find("from project in " + Project.class + " order by project.name asc");
            mockSessionControl.setReturnValue(projectList);
            mockSession.load(Project.class, new Integer(context.getProjectId()));
            mockSessionControl.setReturnValue(projectList.get(0));
         }
         if (context.getIterationId() != 0) {
            List itrationList = new LinkedList();
            Iteration iteration = new Iteration();
            iteration.setId(context.getIterationId());
            iteration.setName("TestIteration");
            itrationList.add(iteration);
            mockSession.find("from iteration in " +
                             Iteration.class +
                             " where project_id=" +
                             context.getProjectId() +
                             " order by iteration.startDate asc");
            mockSessionControl.setReturnValue(itrationList);
            mockSession.load(Iteration.class, new Integer(context.getIterationId()));
            mockSessionControl.setReturnValue(itrationList.get(0));
         }
         if (context.getStoryId() != 0) {
            List storyList = new LinkedList();
            UserStory userStory = new UserStory();
            userStory.setId(context.getStoryId());
            userStory.setName("TestStory");
            storyList.add(userStory);
            mockSession.find("from story in " +
                             UserStory.class +
                             " where iteration_id=" +
                             context.getIterationId() +
                             " order by story.orderNo asc");
            mockSessionControl.setReturnValue(storyList);
            mockSession.load(UserStory.class, new Integer(context.getStoryId()));
            mockSessionControl.setReturnValue(storyList.get(0));
         }
         if (context.getTaskId() != 0) {
            List taskList = new LinkedList();
            Task task = new Task();
            task.setId(context.getTaskId());
            task.setName("TestTask");
            taskList.add(task);
            mockSession.find("from task in " +
                             Task.class +
                             " where story_id=" +
                             context.getStoryId() +
                             " order by task.name asc");
            mockSessionControl.setReturnValue(taskList);
            mockSession.load(Task.class, new Integer(context.getTaskId()));
            mockSessionControl.setReturnValue(taskList.get(0));
         }
         mockSessionControl.replay();
      } catch (HibernateException e) {
         e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
      }
   }

}
