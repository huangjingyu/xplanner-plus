package com.technoetic.xplanner.actions;

import static org.easymock.EasyMock.expect;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import net.sf.xplanner.dao.TaskDao;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.TimeEntry;
import net.sf.xplanner.domain.UserStory;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.technoetic.mocks.hibernate.MockSession;
import com.technoetic.mocks.hibernate.MockSessionFactory;
import com.technoetic.xplanner.db.hibernate.GlobalSessionFactory;
import com.technoetic.xplanner.db.hibernate.HibernateHelper;
import com.technoetic.xplanner.domain.IterationStatus;
import com.technoetic.xplanner.domain.StoryDisposition;
import com.technoetic.xplanner.domain.repository.RepositoryException;
import com.technoetic.xplanner.format.DecimalFormat;
import com.technoetic.xplanner.forms.TimeEditorForm;

public class TestUpdateTimeAction extends AbstractActionTestCase {

   private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";
   private static final String DATE_FORMAT = "yyyy-MM-dd";
   private static final String DATE_1a = "2002-02-02 00:00";
   private static final String DATE_1b = "2002-02-02 00:06";
   private static final String DATE_1c = "2002-02-02";
   private static final String DATE_2a = "2003-03-02 00:00";
   private static final String DATE_2b = "2003-03-02";
   private static final String DESCRIPTION_1 = "Description 1";
   private static final String DESCRIPTION_2 = "Description 2";
   private static final String DESCRIPTION_3 = "Description 3";

   private static final Locale LOCALE = new Locale("en", "us");
   private static final String DECIMAL_FORMAT = "#0.0";

   // Test for other locales, formats
//    private static final Locale LOCALE = new Locale("da", "be");
//    private static final String DECIMAL_FORMAT = "#0,0";

   private TimeEditorFormUnderTest editorForm;
   private TimeEntry timeEntry1;
   private TimeEntry timeEntry2;
   private SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
   private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
   private Level logLevel = Logger.getRootLogger().getLevel();
   private DecimalFormat decimalFormat;
   private Iteration iteration;
   private UserStory story;

   // DEBT: Rename mockSession to something else. It hides the super class mockSession!
   private MockSession mockSession;
   private MockSessionFactory mockSessionFactory;
   private TaskDao taskDao;

   public void setUp() throws Exception {
      action = new UpdateTimeAction();
      super.setUp();

      support.setForward(AbstractAction.TYPE_KEY, "com.technoetic.xplanner.actions.MockPersistentObject");
      support.setForward("edit/task/estimate", "estimate.jsp");
      support.setForward("start/iteration", "editIterationStatus.jsp");
      editorForm = new TimeEditorFormUnderTest();
      editorForm.setOid("99");
      support.form = editorForm;
      support.resources.setMessage("format.datetime", DATE_TIME_FORMAT);
      support.resources.setMessage("format.date", DATE_FORMAT);
      support.request.setLocale(LOCALE);
      support.resources.setMessage("format.decimal", DECIMAL_FORMAT);
      decimalFormat = new DecimalFormat(support.request);
      support.mapping.setInput("editor.jsp");
      mockSession = new MockSession();
      taskDao = createLocalMock(TaskDao.class);

      ((UpdateTimeAction)action).setTaskDao(taskDao);
      
      mockSession.connectionReturn = support.connection;
      mockSessionFactory = new MockSessionFactory();
      mockSessionFactory.openSessionReturn = mockSession;
      GlobalSessionFactory.set(mockSessionFactory);
      support.servletContext.setAttribute("xplanner.sessions", mockSessionFactory);
      logLevel = Logger.getRootLogger().getLevel();
      Logger.getRootLogger().setLevel(Level.OFF);
      HibernateHelper.setSession(support.request, mockSession);
   }

   public void tearDown() throws Exception {
      super.tearDown();
      Logger.getRootLogger().setLevel(logLevel);
      GlobalSessionFactory.set(null);
   }

   public void testPopulateFormWithoutTimeEntries() throws Exception {
      Task task = new Task();
      task.setAcceptorId(44);
      expect(taskDao.getById(99)).andReturn(task);
      replay();

      support.executeAction(action);

      verify();
      assertEquals("wrong oid", "99", editorForm.getOid());
      assertEquals("wrong rowcount", 1, editorForm.getRowcount());
      assertEquals("wrong start date", "", editorForm.getStartTime(0));
      assertEquals("wrong end date", "", editorForm.getEndTime(0));
      assertEquals("wrong default person", "44", editorForm.getPerson1Id(0));
      assertEquals("wrong person id", null, editorForm.getPerson2Id(0));

      // Report date defaults to current date (no time)
      assertEquals("wrong report date",
                   dateFormat.format(new Date()),
                   editorForm.getReportDate(0));
      assertEquals("wrong description", null, editorForm.getDescription(0));
      assertTrue("txn rollback not called", support.connection.rollbackCalled);
   }

   public void testPopulateFormWithTimeEntriesNoNewRow() throws Exception {
      setUpTimeEntries();
      replay();

      ActionForward forward = support.executeAction(action);

      verify();
      assertEquals("wrong oid", "99", editorForm.getOid());
      assertEquals("wrong rowcount", 2, editorForm.getRowcount());
      assertEquals("wrong entry id", "1", editorForm.getEntryId(0));
      assertEquals("wrong start date", DATE_1a, editorForm.getStartTime(0));
      assertEquals("wrong end date", DATE_1b, editorForm.getEndTime(0));
      assertEquals("wrong report date", DATE_1c, editorForm.getReportDate(0));
      assertEquals("wrong duration", decimalFormat.format(0.1), editorForm.getDuration(0));
      assertEquals("wrong person id 1", "1", editorForm.getPerson1Id(0));
      assertEquals("wrong person id 2", "2", editorForm.getPerson2Id(0));
      assertEquals("wrong description", DESCRIPTION_1, editorForm.getDescription(0));
      assertEquals("wrong entry id", "2", editorForm.getEntryId(1));
      assertEquals("wrong start date", DATE_2a, editorForm.getStartTime(1));
      assertEquals("wrong end date", "", editorForm.getEndTime(1));
      assertEquals("wrong report date", DATE_2b, editorForm.getReportDate(1));
      assertEquals("wrong person id 1", "0", editorForm.getPerson1Id(1));
      assertEquals("wrong person id 2", "0", editorForm.getPerson2Id(1));
      assertEquals("wrong description", DESCRIPTION_2, editorForm.getDescription(1));
      assertEquals("wrong forward", "editor.jsp", forward.getPath());
      assertTrue("txn rollback not called", support.connection.rollbackCalled);
   }

   public void testPopulateFormWithTimeEntriesWithNewRow() throws Exception {
      setUpTimeEntries();
      timeEntry2.setEndTime(dateTimeFormat.parse(DATE_2a));
      timeEntry2.setPerson1Id(12);
      timeEntry2.setPerson2Id(21);
      timeEntry2.setDescription(DESCRIPTION_3);
      replay();

      ActionForward forward = support.executeAction(action);

      verify();
      assertEquals("wrong oid", "99", editorForm.getOid());
      assertEquals("wrong rowcount", 3, editorForm.getRowcount());
      assertEquals("wrong entry id", "1", editorForm.getEntryId(0));
      assertEquals("wrong start date", DATE_1a, editorForm.getStartTime(0));
      assertEquals("wrong end date", DATE_1b, editorForm.getEndTime(0));
      assertEquals("wrong report date", DATE_1c, editorForm.getReportDate(0));
      assertEquals("wrong person id 1", "1", editorForm.getPerson1Id(0));
      assertEquals("wrong person id 2", "2", editorForm.getPerson2Id(0));
      assertEquals("wrong description", DESCRIPTION_1, editorForm.getDescription(0));
      assertEquals("wrong duration", decimalFormat.format(0.1), editorForm.getDuration(0));
      assertEquals("wrong entry id", "2", editorForm.getEntryId(1));
      assertEquals("wrong start date", DATE_2a, editorForm.getStartTime(1));
      assertEquals("wrong end date", DATE_2a, editorForm.getEndTime(1));
      assertEquals("wrong report date", DATE_2b, editorForm.getReportDate(1));
      assertEquals("wrong person id 1", "12", editorForm.getPerson1Id(1));
      assertEquals("wrong person id 2", "21", editorForm.getPerson2Id(1));
      assertEquals("wrong description", DESCRIPTION_3, editorForm.getDescription(1));
      assertEquals("wrong entry id", "0", editorForm.getEntryId(2));
      assertEquals("wrong start date", "", editorForm.getStartTime(2));
      assertEquals("wrong end date", "", editorForm.getEndTime(2));
      assertEquals("wrong person id 1", "12", editorForm.getPerson1Id(2));
      assertEquals("wrong person id 2", "21", editorForm.getPerson2Id(2));
      assertEquals("wrong description", null, editorForm.getDescription(2));
      assertEquals("wrong forward", "editor.jsp", forward.getPath());
      assertTrue("txn rollback not called", support.connection.rollbackCalled);
   }

   public void testPopulateFormWithTimeEntriesWithDurationOnlyRow() throws Exception {
      setUpTimeEntries();
      timeEntry2.setStartTime(null);
      timeEntry2.setEndTime(null);
      timeEntry2.setDuration(0.1);
      timeEntry2.setPerson1Id(12);
      timeEntry2.setPerson2Id(21);
      replay();

      ActionForward forward = support.executeAction(action);

      verify();
      assertEquals("wrong oid", "99", editorForm.getOid());
      assertEquals("wrong rowcount", 3, editorForm.getRowcount());
      assertEquals("wrong entry id", "1", editorForm.getEntryId(0));
      assertEquals("wrong start date", DATE_1a, editorForm.getStartTime(0));
      assertEquals("wrong end date", DATE_1b, editorForm.getEndTime(0));
      assertEquals("wrong person id 1", "1", editorForm.getPerson1Id(0));
      assertEquals("wrong person id 2", "2", editorForm.getPerson2Id(0));
      assertEquals("wrong report date", DATE_1c, editorForm.getReportDate(0));
      assertEquals("wrong duration", decimalFormat.format(0.1), editorForm.getDuration(0));
      assertEquals("wrong entry id", "2", editorForm.getEntryId(1));
      assertEquals("wrong start date", "", editorForm.getStartTime(1));
      assertEquals("wrong end date", "", editorForm.getEndTime(1));
      assertEquals("wrong report date", DATE_2b, editorForm.getReportDate(1));
      assertEquals("wrong duration", decimalFormat.format(0.1), editorForm.getDuration(1));
      assertEquals("wrong person id 1", "12", editorForm.getPerson1Id(1));
      assertEquals("wrong person id 2", "21", editorForm.getPerson2Id(1));
      assertEquals("wrong entry id", "0", editorForm.getEntryId(2));
      assertEquals("wrong start date", "", editorForm.getStartTime(2));
      assertEquals("wrong end date", "", editorForm.getEndTime(2));
      assertEquals("wrong person id 1", "12", editorForm.getPerson1Id(2));
      assertEquals("wrong person id 2", "21", editorForm.getPerson2Id(2));
      assertEquals("wrong forward", "editor.jsp", forward.getPath());
      assertTrue("txn rollback not called", support.connection.rollbackCalled);
   }


   public void testActionCreateEntry() throws Exception {
      Task task = new Task();
      task.setEstimatedHours(4.0);
      setUpStory(task, IterationStatus.ACTIVE);
      task.setUserStory(story);
      ArrayList entries = new ArrayList();
      TimeEntry timeEntry = new TimeEntry();
      timeEntry.setId(0);
      timeEntry.setStartTime(dateTimeFormat.parse(DATE_1a));
      timeEntry.setEndTime(dateTimeFormat.parse(DATE_2a));
      timeEntry.setReportDate(dateFormat.parse(DATE_1c));
      timeEntry.setPerson1Id(1);
      timeEntry.setPerson2Id(2);
      timeEntry.setDescription(DESCRIPTION_1);
      entries.add(timeEntry);
      task.setTimeEntries(entries);


      editorForm.setAction(UpdateTimeAction.UPDATE_TIME_ACTION);
      editorForm.setEntryId(0, "0");
      editorForm.setStartTime(0, DATE_1a);
      editorForm.setEndTime(0, DATE_2a);
      editorForm.setReportDate(0, DATE_1c);
      editorForm.setPerson1Id(0, "1");
      editorForm.setPerson2Id(0, "2");
      editorForm.setDescription(0, DESCRIPTION_1);
      editorForm.setRowcount(1);

      expect(taskDao.getById(99)).andReturn(task).times(3);


      support.request.setParameterValue("returnto", new String[]{"return.jsp"});
      replay();

      ActionForward forward = support.executeAction(action);

      verify();
      Iterator it = mockSession.saveObjects.iterator();
      assertTrue("object not inserted", mockSession.saveCalled);
      Task actualTask = (Task) it.next();
      assertEquals("Orginal estimated hours", 4.0, actualTask.getEstimatedOriginalHours(), 0);
      TimeEntry entry = (TimeEntry) it.next();
      assertEquals("wrong start time", dateTimeFormat.parse(DATE_1a), entry.getStartTime());
      assertEquals("wrong start time", dateTimeFormat.parse(DATE_2a), entry.getEndTime());
      assertEquals("wrong report date", dateFormat.parse(DATE_1c), entry.getReportDate());
      assertEquals("wrong person id", 1, entry.getPerson1Id());
      assertEquals("wrong person id", 2, entry.getPerson2Id());
      assertEquals("wrong description", DESCRIPTION_1, entry.getDescription());
      assertNotNull("null forward", forward);
      assertEquals("wrong forward", "return.jsp", forward.getPath());
      assertTrue("not redirected", forward.getRedirect());
      assertTrue("txn commit not called", support.connection.commitCalled);
      assertTrue("reset called", !editorForm.resetCalled);
   }

   public void testAction_InactiveIteration() throws Exception {
      Task task = new Task();
      task.setEstimatedHours(4.0);
      setUpStory(task, IterationStatus.INACTIVE);
      editorForm.setAction(UpdateTimeAction.UPDATE_TIME_ACTION);
      editorForm.setEntryId(0, "0");
      editorForm.setStartTime(0, DATE_1a);
      editorForm.setEndTime(0, DATE_2a);
      editorForm.setReportDate(0, DATE_1c);
      editorForm.setPerson1Id(0, "1");
      editorForm.setPerson2Id(0, "2");
      editorForm.setRowcount(1);

      expect(taskDao.getById(99)).andReturn(task);

      support.request.setParameterValue("returnto", new String[]{"return.jsp"});
      replay();

      ActionForward forward = support.executeAction(action);

      verify();
      Iterator it = mockSession.saveObjects.iterator();
      assertFalse("object have been inserted", mockSession.saveCalled);
      assertEquals("wrong forward", "/do/start/iteration", forward.getPath());
      assertTrue("redirected", !forward.getRedirect());
   }

   public void testActionEditEntryNoEndTime() throws Exception {
      Task task = new Task();
      setUpStory(task, IterationStatus.ACTIVE);
      task.setUserStory(story);
      task.setEstimatedHours(4.0);
      ArrayList tasks = new ArrayList();
      tasks.add(task);
      story.setTasks(tasks);

      expect(taskDao.getById(99)).andReturn(task);
//       expectObjectRepositoryAccess(Iteration.class);
//       mockObjectRepositoryControl.expectAndReturn(mockObjectRepository.load(11), iteration);
      setUpTimeEntries();

      editorForm.setAction(UpdateTimeAction.UPDATE_TIME_ACTION);
      editorForm.setEntryId(0, "1");
      editorForm.setStartTime(0, DATE_1a);
      editorForm.setEndTime(0, null);
      editorForm.setReportDate(0, DATE_1c);
      editorForm.setPerson1Id(0, "1");
      editorForm.setPerson2Id(0, "2");
      editorForm.setRowcount(1);
      support.request.setParameterValue("returnto", new String[]{"return.jsp"});
//      mockSession.loadAddReturnByClassById(11, iteration);
//        expectObjectRepositoryAccess(Iteration.class);
//        mockObjectRepositoryControl.expectAndReturn(mockObjectRepository.load(11), iteration);
      replay();
      ActionForward forward = support.executeAction(action);

      verify();
      assertEquals("wrong person id", 1, timeEntry1.getPerson1Id());
      assertEquals("wrong person id", 2, timeEntry1.getPerson2Id());
      assertEquals("wrong forward", "return.jsp", forward.getPath());
      assertTrue("not redirected", forward.getRedirect());
      assertTrue("txn commit not called", support.connection.commitCalled);
   }

   public void testActionCreateEntryNoStartTime() throws Exception {
      Task task = new Task();
      task.setEstimatedHours(4.0);
      setUpStory(task, IterationStatus.ACTIVE);
      task.setUserStory(story);
      expect(taskDao.getById(99)).andReturn(task);
      editorForm.setAction(UpdateTimeAction.UPDATE_TIME_ACTION);
      editorForm.setEntryId(0, "0");
      editorForm.setEndTime(0, DATE_2a);
      editorForm.setReportDate(0, DATE_1c);
      editorForm.setPerson1Id(0, "1");
      editorForm.setPerson2Id(0, "2");
      editorForm.setDescription(0, DESCRIPTION_3);
      editorForm.setRowcount(1);
      support.request.setParameterValue("returnto", new String[]{"return.jsp"});
      replay();

      ActionForward forward = support.executeAction(action);

      verify();
      assertTrue("object inserted", !mockSession.saveCalled);
      assertEquals("wrong forward", "return.jsp", forward.getPath());
      assertTrue("not redirected", forward.getRedirect());
      assertTrue("txn commit not called", support.connection.commitCalled);
      assertTrue("reset called", !editorForm.resetCalled);
   }

   public void testActionUpdateEntry() throws Exception {
      Task task = new Task();
      task.setEstimatedHours(4.0);
      setUpStory(task, IterationStatus.ACTIVE);
      task.setUserStory(story);
      expect(taskDao.getById(99)).andReturn(task);
      setUpTimeEntries();
      editorForm.setAction(UpdateTimeAction.UPDATE_TIME_ACTION);
      editorForm.setEntryId(0, "1");
      editorForm.setStartTime(0, DATE_1a);
      editorForm.setEndTime(0, DATE_2a);
      editorForm.setReportDate(0, DATE_1c);
      editorForm.setPerson1Id(0, "100");
      editorForm.setPerson2Id(0, "200");
      editorForm.setDescription(0, DESCRIPTION_3);
      editorForm.setRowcount(1);


      support.request.setParameterValue("returnto", new String[]{"return.jsp"});
      replay();

      ActionForward forward = support.executeAction(action);

      verify();
      assertEquals("wrong person id", 100, timeEntry1.getPerson1Id());
      assertEquals("wrong person id", 200, timeEntry1.getPerson2Id());
      assertEquals("wrong description", DESCRIPTION_3, timeEntry1.getDescription());
      assertNotNull("null forward", forward);
      assertEquals("wrong forward", "return.jsp", forward.getPath());
      assertTrue("not redirected", forward.getRedirect());
      assertTrue("txn commit not called", support.connection.commitCalled);
      assertTrue("reset called", !editorForm.resetCalled);
   }

   public void testActionDeleteEntry() throws Exception {
      Task task = new Task();
      task.setEstimatedHours(4.0);
      expect(taskDao.getById(99)).andReturn(task);
      setUpStory(task, IterationStatus.ACTIVE);
      task.setUserStory(story);

      setUpTimeEntries();
      editorForm.setAction(UpdateTimeAction.UPDATE_TIME_ACTION);
      editorForm.setEntryId(0, "1");
      editorForm.setStartTime(0, DATE_1a);
      editorForm.setEndTime(0, DATE_2a);
      editorForm.setReportDate(0, DATE_1c);
      editorForm.setPerson1Id(0, "100");
      editorForm.setPerson2Id(0, "200");
      editorForm.setDeleted(0, "true");
      editorForm.setRowcount(1);
      support.request.setParameterValue("returnto", new String[]{"return.jsp"});
      replay();

      ActionForward forward = support.executeAction(action);

      verify();
      assertTrue("delete not called", mockSession.deleteCalled);
      assertEquals("wrong object deleted",
                   mockSession.find2Return.get(0),
                   mockSession.deleteObject);
      assertEquals("wrong forward", "return.jsp", forward.getPath());
      assertTrue("not redirected", forward.getRedirect());
      assertTrue("txn commit not called", support.connection.commitCalled);
      assertTrue("reset not called", editorForm.resetCalled);
   }

   public void testActionDeleteAndUpdate() throws Exception {
      Task task = new Task();
      task.setEstimatedHours(4.0);
      setUpStory(task, IterationStatus.ACTIVE);
      task.setUserStory(story);
      expect(taskDao.getById(99)).andReturn(task);
      setUpTimeEntries();
      editorForm.setAction(UpdateTimeAction.UPDATE_TIME_ACTION);
      editorForm.setEntryId(0, "1");
      editorForm.setStartTime(0, DATE_1a);
      editorForm.setEndTime(0, DATE_2a);
      editorForm.setReportDate(0, DATE_1c);
      editorForm.setPerson1Id(0, "100");
      editorForm.setPerson2Id(0, "200");
      editorForm.setDescription(0, DESCRIPTION_2);
      editorForm.setRowcount(2);
      editorForm.setDeleted(1, "true");
      editorForm.setEntryId(1, "2");

      support.request.setParameterValue("returnto", new String[]{"return.jsp"});
      replay();

      ActionForward forward = support.executeAction(action);

      verify();
      assertEquals("wrong person id", 100, timeEntry1.getPerson1Id());
      assertEquals("wrong person id", 200, timeEntry1.getPerson2Id());
      assertEquals("wrong description", DESCRIPTION_2, timeEntry1.getDescription());
      assertNotNull("null forward", forward);
      assertEquals("wrong forward", "return.jsp", forward.getPath());
      assertTrue("delete not called", mockSession.deleteCalled);
      assertEquals("wrong object deleted",
                   mockSession.find2Return.get(1),
                   mockSession.deleteObject);
      assertTrue("not redirected", forward.getRedirect());
      assertTrue("txn commit not called", support.connection.commitCalled);
   }

   private void setUpTimeEntries() throws Exception {
      ArrayList entries = new ArrayList();
      timeEntry1 = new TimeEntry();
      timeEntry1.setId(1);
      timeEntry1.setStartTime(dateTimeFormat.parse(DATE_1a));
      timeEntry1.setEndTime(dateTimeFormat.parse(DATE_1b));
      timeEntry1.setReportDate(dateFormat.parse(DATE_1c));
      timeEntry1.setDuration(0.1);
      timeEntry1.setPerson1Id(1);
      timeEntry1.setPerson2Id(2);
      timeEntry1.setDescription(DESCRIPTION_1);
      entries.add(timeEntry1);
      timeEntry2 = new TimeEntry();
      timeEntry2.setId(2);
      timeEntry2.setStartTime(dateTimeFormat.parse(DATE_2a));
      timeEntry2.setReportDate(dateFormat.parse(DATE_2b));
      timeEntry2.setDescription(DESCRIPTION_2);
      entries.add(timeEntry2);
      mockSession.find2Return = entries;
   }

   private void setUpStory(Task task, IterationStatus iterationStatus) throws RepositoryException {
      iteration = new Iteration();
      iteration.setId(11);
      iteration.setIterationStatus(iterationStatus);
      story = new UserStory();
      ArrayList tasks = new ArrayList();
      tasks.add(task);
      story.setTasks(tasks);
      task.setUserStory(story);
      story.setIteration(iteration);
      story.setDisposition(StoryDisposition.PLANNED);
      ArrayList stories = new ArrayList();
      stories.add(story);
      iteration.setUserStories(stories);
//        mockSession.loadAddReturnByClassById(new Integer(11), iteration);
      expectObjectRepositoryAccess(Iteration.class);
//      expect(mockObjectRepository.load(11)).andReturn(iteration);
      mockSession.delete4Return = new Integer(0);
      mockSession.iterate2Return = new Iterator() {
         public void remove() {
         }

         public boolean hasNext() {
            return false;
         }

         public Object next() {
            return null;
         }
      };
   }

   private static class TimeEditorFormUnderTest extends TimeEditorForm {
      public boolean resetCalled;

      public void reset(ActionMapping mapping, HttpServletRequest request) {
         resetCalled = true;
      }
   }


}

