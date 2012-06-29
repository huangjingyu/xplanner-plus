/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: May 26, 2005
 * Time: 9:55:22 PM
 */
package com.technoetic.xplanner.domain;

import java.util.Date;

import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.History;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Note;
import net.sf.xplanner.domain.Permission;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.TimeEntry;
import net.sf.xplanner.domain.UserStory;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.util.StringUtils;

import com.technoetic.xplanner.XPlannerTestSupport;
import com.technoetic.xplanner.actions.EditPersonHelper;
import com.technoetic.xplanner.domain.repository.RepositoryException;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.module.LoginSupportImpl;
import com.technoetic.xplanner.security.module.XPlannerLoginModule;
import com.technoetic.xplanner.tx.CheckedExceptionHandlingTransactionTemplate;
import com.technoetic.xplanner.util.Callable;
import com.technoetic.xplanner.util.LogUtil;
import com.technoetic.xplanner.util.MainBeanFactory;
import com.technoetic.xplanner.util.PropertyUtils;

public class ObjectMother {
   protected static final Logger LOG = LogUtil.getLogger();

   public static final long HOUR = 60 * 60 * 1000L;
   public static final long DAY = 24 * HOUR;

  private Date now = new Date();


  public Project newProject(String name, String description) throws HibernateException, RepositoryException {
     Project project = new Project();
     project.setName(name);
     project.setDescription(description);
     save(project);
     return project;
  }

  public Project newProject() throws HibernateException, RepositoryException {
      Project project = new Project();
      project.setName("");
      project.setDescription("descripton");
      save(project);
      return project;
   }

   public Iteration newIteration(Project project) throws HibernateException, RepositoryException {
      Iteration iteration = new Iteration();
      iteration.setName("");
      iteration.setProject(project);
      iteration.setIterationStatus(IterationStatus.INACTIVE);
      iteration.setStartDate(new Date(System.currentTimeMillis() - DAY));
      iteration.setEndDate(new Date(System.currentTimeMillis() + DAY));
      project.getIterations().add(iteration);
      save(iteration);
      return iteration;
   }

   public UserStory newUserStory(Iteration iteration) throws HibernateException, RepositoryException {
      UserStory story = new UserStory();
      story.setName("");
      story.setIteration(iteration);
      story.setPriority(4);
      story.setDisposition(iteration.determineNewStoryDisposition());
      story.setOrderNo(1);
      iteration.getUserStories().add(story);
      save(story);

      return story;
   }

   public Task newTask(UserStory story) throws HibernateException, RepositoryException {
      Task task = new Task();
      task.setName("");
      task.setUserStory(story);
      story.getTasks().add(task);
      task.setType("");
      task.setDisposition(TaskDisposition.PLANNED);
      save(task);
      return task;
   }

   public Feature newFeature(UserStory story) throws HibernateException, RepositoryException {
      Feature feature = new Feature();
      feature.setStory(story);
      story.getFeatures().add(feature);
      feature.setDescription("Description of test feature");
      save(feature);
      return feature;
   }

   public TimeEntry newTimeEntry(Task task, Person person1) throws HibernateException, RepositoryException {
      return newTimeEntry(task, person1, 1.0);
   }

   public TimeEntry newTimeEntry(Task task, Person person1, double durationInHours) throws HibernateException,
                                                                                           RepositoryException {
      net.sf.xplanner.domain.TimeEntry timeEntry = new net.sf.xplanner.domain.TimeEntry();
      Date startTime = getNow();
      timeEntry.setStartTime(startTime);
      timeEntry.setEndTime(new Date(startTime.getTime() + (long) (durationInHours * HOUR)));
      timeEntry.setPerson1Id(person1.getId());
      timeEntry.setTask(task);
      task.getTimeEntries().add(timeEntry);
      save(timeEntry);
      return timeEntry;
   }

   public Note newNote(net.sf.xplanner.domain.DomainObject container, Person author) throws HibernateException, RepositoryException {
      Note note = new Note();
      note.setSubject("");
      note.setAuthorId(author.getId());
      note.setBody("body");
      note.setAttachedToId(container.getId());
      save(note);
      return note;
   }

   public Person newPerson() throws HibernateException, RepositoryException, AuthenticationException {
      return newPerson("TestPerson");
   }

   //TODO remove the userId parameter
   public Person newPerson(String userId) throws HibernateException, RepositoryException, AuthenticationException {
      Person person = new Person();
      person.setUserId(userId + Math.random() * 1000);
      person.setPassword("EoZY1WHEQjfY56/36L+D79fbePdLouvR55CG6g=="); // password = test
      save(person);
      String uniqueUserId = userId + person.getId();
      person.setUserId(uniqueUserId);
      person.setEmail(uniqueUserId + "@noreply.com");
      person.setInitials(uniqueUserId);
      return person;
   }

   private String encodePassword(String password) {
      XPlannerLoginModule loginModule = new XPlannerLoginModule(new LoginSupportImpl());
      try {
         return loginModule.encodePassword(password, null);
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }


   public Permission newPermission() throws Exception {
      Permission permission = new Permission();
      permission.setPrincipal(0);
      permission.setName("testPermission");
      permission.setResourceType("system.person");
      permission.setResourceId(0);
      save(permission);
      return permission;
   }

   public History newHistory() throws HibernateException, RepositoryException {
      int targetObjectId = 0;
      String type = "project";
      String action = History.DELETED;
      return newHistory(targetObjectId, type, action, "PROJECT_NAME");
   }

   public History newHistory(int targetObjectId, String type, String action, String description)
         throws HibernateException, RepositoryException {
      return newHistory(0, targetObjectId, type, action, description);
   }

   public History newHistory(Integer containerId,
		   									 Integer targetObjectId,
                                             String type,
                                             String action,
                                             String description)
         throws HibernateException, RepositoryException {
      History historyEvent = new History(getNow(), containerId, targetObjectId, type, action, description,
                                                         XPlannerTestSupport.DEFAULT_PERSON_ID);
      save(historyEvent);
      return historyEvent;
   }

   public Integration newIntegration() throws HibernateException, RepositoryException {
      Integration integration = new Integration();
      save(integration);
      return integration;
   }

  public void save(Object object) throws HibernateException, RepositoryException {
    saveAndRegisterForDelete(object);
    if (LOG.isDebugEnabled()) LOG.debug("creating " + object);
  }

  public void save(DomainObject object) throws HibernateException, RepositoryException {
     saveAndRegisterForDelete(object);
     object.setLastUpdateTime(getNow());
     setDefaultName(object);
     if (LOG.isDebugEnabled()) LOG.debug("creating " + object);
  }

  protected void saveAndRegisterForDelete(Object object) throws HibernateException, RepositoryException {
    PropertyUtils.setProperty(object, "id", new Integer(++nextId));
  }

   private void setDefaultName(DomainObject object) {
      //DEBT Once Nameable is renamed to Nameable and setters introduced
      if (PropertyUtils.isWriteable(object, "name")) {
         String name = (String) PropertyUtils.getProperty(object, "name");
         if (!StringUtils.hasText(name)) {
            PropertyUtils.setProperty(object, "name", "Test " + getClassName(object) + " " + object.getId());
         }
      }
   }

   static int nextId = 0;

   private String getClassName(Object object) {
      String name = object.getClass().getName();
      return name.substring(name.lastIndexOf(".") + 1);
   }

  public Date getNow() {
     return now;
  }

   public void setNow(Date now) {
      this.now = now;
   }

}