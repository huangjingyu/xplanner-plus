package com.technoetic.xplanner.acceptance.web;

import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.List;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Note;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.PersonRole;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Role;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.TimeEntry;
import net.sf.xplanner.domain.UserStory;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

import com.meterware.httpunit.WebImage;
import com.meterware.httpunit.WebTable;
import com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript;
import com.technoetic.xplanner.acceptance.IterationTester;
import com.technoetic.xplanner.acceptance.web.console.VisualTesterHandler;
import com.technoetic.xplanner.actions.EditPersonHelper;
import com.technoetic.xplanner.db.hibernate.IdGenerator;
import com.technoetic.xplanner.domain.TaskDisposition;
import com.technoetic.xplanner.domain.repository.RepositoryException;
import com.technoetic.xplanner.domain.repository.RoleAssociationRepositoryImpl;
import com.technoetic.xplanner.testing.DateHelper;
import com.thoughtworks.proxy.toys.echo.Echoing;

public abstract class AbstractPageTestScript extends AbstractDatabaseTestScript {

   protected XPlannerWebTester tester;
   protected IterationTester iterationTester;
   protected PersonTester personTester;
   private static VisualTesterHandler visualHandler = new VisualTesterHandler();

   protected String testProjectName;
   protected String testProjectDescription;
   protected String storyName;
   protected String testStoryDescription;
   protected String estimatedHoursString = "15.5";
   protected double estimatedHours = 15.5;
   protected String developerName;
   protected String developerId;
   protected String developerUserId;
   protected String developerGroupId;
   protected String developerInitials;
   protected String customerName;
   protected String customerUserId;
   protected String customerGroupId;
   protected String customerInitials;
   protected String guestName;
   protected String guestUserId;
   protected String guestGroupId;
   protected String guestInitials;
   protected String testTaskName;
   protected String testTaskDescription;
   protected String testTaskEstimatedHours;
   protected String testFeatureName;
   protected String testFeatureDescription;
   protected String testIterationName;
   protected String testIterationDescription;
   protected String testNoteSubject;
   protected String testNoteBody;
   protected String testNoteAttachmentFilename;
   protected String testWikiUrl;

   protected final String EDIT_IMAGE = "edit.gif";
   protected final String DELETE_IMAGE = "delete.gif";
   protected final String EDIT_TIME_IMAGE = "clock2.gif";
   protected final boolean HIDDEN = true;
   protected final boolean NOT_HIDDEN = false;

   protected String noPermissionUserName = "noPermUser";
   protected String editorRoleUserName = "editorRole";
   protected String userEmail = "ap@nowhere.com";
   protected String userPhone = "212-555-5555";
   protected static final String ROLES_TABLE = "roles";
   public Person developer;
   public Project project;
   public Iteration iteration;
   public UserStory story;
   public Task task;
   public static final double INITIAL_TASK_ESTIMATED_HOURS = 20.5;

   public AbstractPageTestScript(String test) {
      super(test);
      safeInit();
   }

   public AbstractPageTestScript() {
      safeInit();
   }

   private void safeInit() {
      try {
         init();
      } catch (Throwable e) {
         e.printStackTrace();
         fail();
      }
   }

   private void init() {
      // todo - This should be in the setUp method
      tester = createTester();
      iterationTester = new IterationTester(tester);
      personTester = new PersonTester(tester);

      int descriptionSize = 4000;

      testProjectName = generateUniqueName("project");
      testProjectDescription = padStringToSize("A Test Project", descriptionSize);
      storyName = generateUniqueName("story");
      testStoryDescription = padStringToSize("This is a story", descriptionSize);

      developerName = generateUniqueName("developer");
      developerUserId = generateUniqueName("apid");
      developerInitials = developerName;

      customerName = generateUniqueName("customer");
      customerUserId = generateUniqueName("cust");
      customerInitials = "tc";

      guestName = generateUniqueName("guest");
      guestUserId = generateUniqueName("gpid");
      guestInitials = "GST";

      testTaskName = generateUniqueName("task");
      testTaskDescription = padStringToSize("Task Description", descriptionSize);
      testTaskEstimatedHours = "" + INITIAL_TASK_ESTIMATED_HOURS;

      testFeatureName = generateUniqueName("feature");
      testFeatureDescription = padStringToSize("Feature Description", descriptionSize);

      testIterationName = generateUniqueName("iteration");
      testIterationDescription = padStringToSize("A test iteration", descriptionSize);

      testNoteSubject = generateUniqueName("note");
      testNoteBody = padStringToSize("ABC TEST Body ABC", descriptionSize);
      testNoteAttachmentFilename = "/data/TestAttachment.xml";
      testWikiUrl = generateUniqueName("URL");
   }

   private XPlannerWebTester createTester() {
      XPlannerWebTester tester = new XPlannerWebTesterImpl();
      try {
         if (System.getProperty("test.ui") != null) {
            visualHandler.setTester(tester);
            tester = (XPlannerWebTester) Proxy.newProxyInstance(XPlannerWebTester.class.getClassLoader(),
                                                                new Class[]{XPlannerWebTester.class},
                                                                visualHandler);
         } else if (System.getProperty("test.console") != null) {
            tester = (XPlannerWebTester) Echoing.object(XPlannerWebTester.class, tester);
         }
      }
      catch (Throwable e) {
         e.printStackTrace();
      }
      return tester;
   }

   private String padStringToSize(String base, int size) {
      StringBuffer buffer = new StringBuffer(base);
      while (buffer.length() < size) {
         String padding = "X";
         buffer.append(padding);

      }
      return buffer.toString();
   }

   /**
    * @deprecated Use setUpTestIterationAndStory_ instead
    */
   @Deprecated
public void setUpTestIterationAndStory() throws Exception {
      tester.gotoRelativeUrl("/do/view/projects");
      tester.clickLinkWithText(testProjectName);
      createIterationAndStory(testIterationName, storyName, 0, 14);
   }

   public void setUpTestIterationAndStory_() throws Exception {
      createIterationAndStory_(project, 0, 14);
   }

   /**
    * @deprecated Use createIterationAndStory_ instead
    */
   @Deprecated
protected void createIterationAndStory(String iterationName,
                                          String storyName,
                                          int startDateOffset,
                                          int endDateOffset) {
      iterationTester.addIteration(iterationName,
                                   tester.dateStringForNDaysAway(startDateOffset),
                                   tester.dateStringForNDaysAway(endDateOffset),
                                   "A test iteration");
      tester.clickLinkWithText(iterationName);
      tester.addUserStory(storyName,
                          testStoryDescription,
                          estimatedHoursString, "1");
      tester.clickLinkWithText(storyName);
   }

   protected void createIterationAndStory_(Project project,
                                           int startDateOffset,
                                           int endDateOffset) throws Exception {
      iteration = newIteration(project);
      iteration.setStartDate(DateHelper.getDateDaysFromToday(startDateOffset));
      iteration.setEndDate(DateHelper.getDateDaysFromToday(endDateOffset));
      story = newUserStory(iteration);
      story.setDescription(testStoryDescription);
      story.setEstimatedHoursField(estimatedHours);
   }

   protected void createIterationAndStoryWithAttachments(String iterationName,
                                                         String storyName,
                                                         int startDateOffset,
                                                         int endDateOffset,
                                                         boolean started) {
      iterationTester.addIteration(iterationName,
                                   tester.dateStringForNDaysAway(startDateOffset),
                                   tester.dateStringForNDaysAway(endDateOffset),
                                   "A test iteration");
      tester.clickLinkWithText(iterationName);
      if (started) iterationTester.startCurrentIteration();
      tester.addUserStory(storyName,
                          testStoryDescription,
                          estimatedHoursString, "1");
      tester.clickLinkWithText(storyName);
      tester.addNote(testNoteSubject, testNoteBody, developerName, testNoteAttachmentFilename);
      tester.addNote(testNoteSubject + "without att.", testNoteBody, developerName);
   }

   public void setUpTestProject(String name, String description)
         throws HibernateException, SQLException, RepositoryException {
      project = newProject(name, description);
      testProjectDescription = project.getDescription();
      testProjectName = project.getName();
      commitCloseAndOpenSession();
   }

   public void setUpTestProject() throws SQLException, HibernateException, RepositoryException {
     setUpTestProject("", "");
   }
   public void setUpTestPerson() throws Exception {
      developer = newPerson();
      developerId = "" + developer.getId();
      developerName = "User " + developerId;
      developerUserId = "userId" + developer.getId();
      developer.setName(developerName);
      developer.setEmail(userEmail);
      developer.setUserId(developerUserId);
      commitCloseAndOpenSession();
   }

   //DEBT Move to EditPersonHelper
   public void setUpTestRole(String roleName) throws Exception {
      EditPersonHelper editPersonHelper = new EditPersonHelper();
      RoleAssociationRepositoryImpl roleAssociationRepository = mom.createRoleAssociationRepository();
      editPersonHelper.setRoleAssociationRepository(roleAssociationRepository);
      editPersonHelper.setRoleOnProject(project.getId(), developer, roleName);
      commitCloseAndOpenSession();
   }

   protected void scheduleObjectDeletion(Object object) {
      mom.registerObjectToBeDeletedOnTearDown(object);
   }

   @Override
protected void tearDown() throws Exception {
      try {
         tester.deleteObjects(Person.class, "name", noPermissionUserName);
         tester.deleteObjects(Person.class, "name", editorRoleUserName);
//      tester.releaseSession();
         tester.gotoRelativeUrl("/do/invalidateHibernateCache");
         tester.tearDown();
      } finally {
         super.tearDown();
      }
   }

   public void tearDownTestPerson() {
      tester.deleteObjects(Person.class, "name", developerName);
   }

   public void tearDownTestProject() {
      tearDownTestIteration();
      tester.deleteObjects(Project.class, "name", testProjectName);
   }

   public void tearDownTestIteration() {
      tearDownTestStory();
      tester.deleteObjects(Iteration.class, "name", testIterationName);
   }

   public void tearDownTestStory() {
      tearDownTestTask();
      tester.deleteObjects(UserStory.class, "name", storyName);
   }

   public void tearDownTestTask() {
      tester.deleteObjects(Task.class, "name", testTaskName);
   }

   public void simpleSetUp() throws Exception {
      simpleSetUp_();
   }

   public void simpleSetUp_() throws Exception {
      setUpTestPerson();
      setUpTestProject();
      setUpTestRole("editor");
      setUpTestIterationAndStory_();
      testIterationName = iteration.getName();
      storyName = story.getName();
      commitCloseAndOpenSession();
      tester.login();
      goToTestStoryPage_();
   }

   public void setUpTestTask_() throws Exception {
      task = newTask(story);
      task.setName(testTaskName);
      task.setDescription(testTaskDescription);
      task.setAcceptorId(developer.getId());
      task.setEstimatedHours(Double.parseDouble(testTaskEstimatedHours));
      task.setDisposition(TaskDisposition.PLANNED);
      commitCloseAndOpenSession();
   }

   public void simpleTearDown() throws Exception {
      tester.tearDown();
      tearDownTestProject();
      tearDownTestPerson();
   }

   public void simpleTearDown_() throws Exception {
      tester.tearDown();
   }

   /**
    * The current implementation of <code>runNotesTests</code> is incomplete,
    * but serves as a place holder for the acceptance test that fails due to a
    * JavaScript incompatibility with the Rhino jar (js.jar).
    * <p/>
    * JavaScript functionality is required on each page that utilizes the new
    * feature on Notes due to a bug/design flaw? in Struts.  A multipart form
    * loses its request parameters along the way if any validation errors
    * occur.  (These parameters probably should be mapped on line 1061 of:
    * org.apache.struts.action.RequestProcessor).  We are sending them as
    * part of the query string as well to work around this bug.
    */
   public void runNotesTests(String fromPage) {
      tester.verifyNotesLink();

      tester.addNote(testNoteSubject, testNoteBody, developerName);
      tester.assertTextPresent(testNoteSubject);
      tester.editNoteWithSubject(testNoteSubject);
      tester.assertFormElementEquals("subject", testNoteSubject);
      tester.assertFormElementEquals("body", testNoteBody);
      String newSubject = "XX New Test Note XX";
      tester.setFormElement("subject", newSubject);
      tester.submit();
      tester.assertOnPage(fromPage);
      tester.assertTextNotPresent(testNoteSubject);
      tester.assertTextPresent(newSubject);
      tester.deleteNoteWithSubject(newSubject);
      tester.assertTextNotPresent(newSubject);

      /**  This is the attachments portion **/
//          	tester.addNote(testNoteSubject, testNoteBody, developerName, testNoteAttachmentFilename );
// 		String attachedName =
//                testNoteAttachmentFilename.substring(testNoteAttachmentFilename.lastIndexOf("/") + 1);
// 		tester.assertTextPresent(attachedName);
// 		tester.editNoteWithSubject(testNoteSubject);
// 		tester.assertFormElementEquals("body", testNoteBody);
// 		String newBody = "XX New Attachment Desc XX";
// 		tester.setFormElement("body", newBody);
// 		tester.submit();
// 		tester.assertTextNotPresent(testNoteBody);
// 		tester.assertTextPresent(newBody);
// 		tester.deleteNoteWithSubject(testNoteSubject);
// 		tester.assertTextNotPresent(newBody);
   }

   protected String generateUniqueName() {
      return generateUniqueName("");
   }

   protected String generateUniqueName(String baseName) {
      return IdGenerator.getUniqueId(baseName);
   }

   // DEBT(AT) Must move to a helper/tester object.
   // EditPersonHelper.setRoleOnProject
   protected void setUpRole(Person person, Project project, String roleName) throws Exception {
      Session session = tester.getSession();
      List roles = session.find("from role in class " + Role.class.getName() +
                                " where role.name = ?", roleName, Hibernate.STRING);
      Role role = null;
      if (roles.size() > 0) {
         role = (Role) roles.get(0);
      } else {
         fail("invalid role: " + roleName);
      }
      addRoleAssociation(session, role.getId(), person.getId(), project != null ? project.getId() : 0);
      tester.releaseSession();

   }

   private void addRoleAssociation(Session session, int roleId, int personId, int projectId) throws HibernateException {
      session.save(new PersonRole(projectId, personId, roleId));
      requestServerCacheInvalidation();
   }

   protected void assertImageInCell(WebTable table, int r, int c, String name) {
      WebImage[] images = table.getTableCell(r, c).getImages();
      for (int i = 0; i < images.length; i++) {
         WebImage image = images[i];
         if (image.getSource().endsWith(name)) {
            return;
         }
      }
      fail("missing image in cell[" + r + "," + c + "]: " + name);
   }

   protected void checkExportUri(String type, String format) throws Exception {
//      String context = XPlannerTestSupport.getRelativeTestURL();
//      Pattern pattern = Pattern.compile("href=" + context + "/(\\S+/export/" + type + "/" + format + "[^>]+)");
//      Matcher matcher = pattern.matcher(tester.getDialog().getResponseText());
//      if (matcher.find()) {
//         tester.gotoRelativeUrl(matcher.group(1).replaceAll("&amp;", "&"));
//      } else {
//         fail("couldn't find export uri");
//      }
   }

   public void assertHeaderValue(String headerName, String expectedHeaderValue) {
//      String headerContent = tester.getDialog().getResponse().getHeaderField(headerName);
//      String[] headersValues = headerContent.split(";");
//      boolean isFounded = false;
//      for (int i = 0; i < headersValues.length; i++) {
//         if (headersValues[i].trim().equals(expectedHeaderValue)) {
//            isFounded = true;
//            break;
//         }
//      }
//      assertTrue("header value not found for " + headerName, isFounded);
   }

   protected void traverseLinkWithKeyAndReturn(String key) throws Exception {
//      String url = tester.getDialog().getResponse().getURL().toString().
//            replaceAll(tester.getTestContext().getBaseUrl(), "");
//      if (!url.startsWith("/")) {
//         url = "/" + url;
//      }
//      tester.clickLinkWithKey(key);
//      tester.gotoRelativeUrl(url);
   }

/**
 * @deprecated
 */
   @Deprecated
public Project setUpProject(boolean isHidden) throws Exception {
      Project project = new Project();
      project.setName("@Test Project@");
      project.setHidden(isHidden);
      tester.getSession().save(project);
      project.setName("@Test Project " + project.getId() + "@");
      scheduleObjectDeletion(project);
      tester.releaseSession();
      return project;
   }

   public Note createNoteFor(int attachedToId, int personId) throws Exception {
      Note note = new Note();
      note.setAttachedToId(attachedToId);
      note.setSubject("@Note Subject");
      note.setBody("@Note Body");
      note.setAuthorId(personId);
      tester.getSession().save(note);
      note.setSubject("@Note Subject " + note.getId() + "@");
      note.setBody("@Note Body " + note.getId() + "@");
      note.setAuthorId(personId);
      scheduleObjectDeletion(note);
      tester.releaseSession();
      return note;
   }

   public void createUserWithoutPermission()
         throws Exception {
      personTester.gotoPeoplePage();
      personTester.addPerson(noPermissionUserName,
                             noPermissionUserName,
                             developerInitials,
                             userEmail,
                             userPhone,
                             false);
      tester.clickLinkWithText("logout");
   }

   protected void createUserWithEditorRoleForProject(String projectName)
         throws Exception {
      personTester.gotoPeoplePage();
      personTester.addPersonWithRole(editorRoleUserName,
                                     editorRoleUserName,
                                     developerInitials,
                                     userEmail,
                                     userPhone,
                                     projectName, tester.getMessage("person.editor.role.editor"));
      tester.clickLinkWithText("logout");
   }

   //DEBT: can we make delete time entry part of tearDownTestTask()?
   public void deleteLocalTimeEntry(String taskId) {
      try {
         Session session = openSession();
         session.delete("from object in class " + TimeEntry.class.getName() + " where object.taskId = ?",
                        new Integer(taskId), Hibernate.INTEGER);
         commitSession();
      }
      catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public void deleteLocalNote(String attachedToId) {
      try {
         Session session = openSession();
         session.delete("from object in class " + Note.class.getName() + " where object.attachedToId = ?",
                        new Integer(attachedToId), Hibernate.INTEGER);
         commitSession();
      }
      catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public void goToTestStoryPage() throws Exception {
      tester.gotoProjectsPage();
      tester.clickLinkWithText(testProjectName);
      tester.clickLinkWithText(testIterationName);
      tester.clickLinkWithText(storyName);
   }

   public void goToTestStoryPage_() throws Exception {
      tester.gotoProjectsPage();
      tester.clickLinkWithText(Integer.toString(project.getId()));
      tester.clickLinkWithText(testIterationName);
      tester.clickLinkWithText(storyName);
   }

}
