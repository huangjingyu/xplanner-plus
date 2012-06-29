package com.technoetic.xplanner.acceptance;

import java.sql.SQLException;

import junit.framework.TestCase;
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

import org.hibernate.HibernateException;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.technoetic.xplanner.acceptance.security.ServerCacheManager;
import com.technoetic.xplanner.db.hibernate.GlobalSessionFactory;
import com.technoetic.xplanner.domain.Feature;
import com.technoetic.xplanner.domain.HibernateTemplateSimulation;
import com.technoetic.xplanner.domain.Integration;
import com.technoetic.xplanner.domain.PersistentObjectMother;
import com.technoetic.xplanner.domain.repository.RepositoryException;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.auth.Authorizer;
import com.technoetic.xplanner.security.auth.AuthorizerImpl;
import com.technoetic.xplanner.security.auth.AuthorizerQueryHelper;
import com.technoetic.xplanner.security.auth.PrincipalSpecificPermissionHelper;

public abstract class AbstractDatabaseTestScript extends TestCase {
   protected PersistentObjectMother mom;
   protected DatabaseSupport dbSupport;
   protected HibernateTemplate hibernateTemplate;
   private ServerCacheManager serverCacheManager;

   public AbstractDatabaseTestScript() { }

   public AbstractDatabaseTestScript(String name) {
      super(name);
   }

   @Override
protected void setUp() throws Exception {
      super.setUp();
      serverCacheManager = new ServerCacheManager();
      dbSupport = new DatabaseSupport();
      dbSupport.setUp();
      mom = dbSupport.getMom();
      dbSupport.openSession();
       hibernateTemplate = new HibernateTemplate(GlobalSessionFactory.get());
   }

   @Override
protected void tearDown() throws Exception {
      super.tearDown();
      dbSupport.tearDown();
      System.gc();
   }

   public Session commitCloseAndOpenSession() throws HibernateException, SQLException {
      commitAndCloseSession();
      return openSession();
   }

   private void invalidateServerCacheIfNeeded() {
      serverCacheManager.invalidateServerCacheIfNeeded();
   }

   protected void requestServerCacheInvalidation() {
      serverCacheManager.requestServerCacheInvalidation();
   }

   public Session openSession() throws HibernateException {
      invalidateServerCacheIfNeeded();
      return dbSupport.openSession();
   }

   public void rollbackSession() {
      dbSupport.rollbackSession();
   }

   public void commitSession() throws HibernateException, SQLException {
      dbSupport.commitSession();
   }

   public void closeSession() {
      dbSupport.closeSession();
   }

   public Project newProject() throws HibernateException, RepositoryException {
      return mom.newProject();
   }

   public Project newProject(String name, String description) throws HibernateException, RepositoryException {
      return mom.newProject(name, description);
   }

   public Iteration newIteration(Project project) throws HibernateException, RepositoryException {
      return mom.newIteration(project);
   }

   public UserStory newUserStory(Iteration iteration) throws HibernateException, RepositoryException {
      return mom.newUserStory(iteration);
   }

   public Task newTask(UserStory story) throws HibernateException, RepositoryException {
      return mom.newTask(story);
   }

   public Feature newFeature(UserStory story) throws HibernateException, RepositoryException {
      return mom.newFeature(story);
   }

   public TimeEntry newTimeEntry(Task task, Person person1) throws HibernateException, RepositoryException {
      return mom.newTimeEntry(task, person1);
   }

   public TimeEntry newTimeEntry(Task task, Person person1, double durationInHours) throws HibernateException,
                                                                                           RepositoryException {
      return mom.newTimeEntry(task, person1, durationInHours);
   }

   public Note newNote(DomainObject container, Person author) throws HibernateException, RepositoryException {
      return mom.newNote(container, author);
   }

   public History newHistory() throws HibernateException, RepositoryException {
      return mom.newHistory();
   }

   public Integration newIntegration() throws HibernateException, RepositoryException {
      return mom.newIntegration();
   }

   public Person newPerson() throws HibernateException, RepositoryException, AuthenticationException {
      requestServerCacheInvalidation();
      return mom.newPerson();
   }

   public Person newPerson(String userId) throws HibernateException, RepositoryException, AuthenticationException {
      requestServerCacheInvalidation();
      return mom.newPerson(userId);
   }

   public Permission newPermission() throws Exception {
      requestServerCacheInvalidation();
      return mom.newPermission();
   }

   public void setUpPersonRole(final Project project, final Person person, final String roleName)
         throws Exception {
	 //FIXME: rewrite in spring     mom.setUpPersonRole(project, person, roleName);

      requestServerCacheInvalidation();
   }

  public void save(Object object) throws HibernateException, RepositoryException {
     mom.save(object);
  }

//   public void deleteTestObjects() throws Exception, SQLException, RepositoryException, AuthenticationException {
//      commitCloseAndOpenSession();
//      mom.deleteTestObjects();
//   }

   public void deleteObject(Object toIteration) throws HibernateException {
      mom.deleteObject(toIteration);
   }

   public void registerObjectToBeDeletedOnTearDown(Object object) {
      mom.registerObjectToBeDeletedOnTearDown(object);
   }

   public Session getSession() throws HibernateException {
      return dbSupport.getSession();
   }

   public void commitAndCloseSession() throws HibernateException, SQLException {
      commitSession();
      closeSession();
   }

   public Authorizer createAuthorizer() {
      AuthorizerImpl authorizer = new AuthorizerImpl();
      AuthorizerQueryHelper authorizerQueryHelper = new AuthorizerQueryHelper();
      authorizerQueryHelper.setHibernateTemplate(new HibernateTemplateSimulation(dbSupport));
      PrincipalSpecificPermissionHelper principalSpecificPermissionHelper = new PrincipalSpecificPermissionHelper();
      principalSpecificPermissionHelper.setAuthorizerQueryHelper(authorizerQueryHelper);
      authorizer.setAuthorizerQueryHelper(authorizerQueryHelper);
      authorizer.setPrincipalSpecificPermissionHelper(principalSpecificPermissionHelper);
      return authorizer;
   }

}