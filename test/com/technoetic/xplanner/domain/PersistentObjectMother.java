/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: May 20, 2006
 * Time: 9:04:16 PM
 */
package com.technoetic.xplanner.domain;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.xplanner.domain.*;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.technoetic.xplanner.domain.repository.HibernateObjectRepository;
import com.technoetic.xplanner.domain.repository.MetaRepository;
import com.technoetic.xplanner.domain.repository.MetaRepositoryImpl;
import com.technoetic.xplanner.domain.repository.ObjectRepository;
import com.technoetic.xplanner.domain.repository.PersonHibernateObjectRepository;
import com.technoetic.xplanner.domain.repository.RepositoryException;
import com.technoetic.xplanner.domain.repository.RoleAssociationRepositoryImpl;
import com.technoetic.xplanner.domain.repository.RoleRepositoryImpl;
import com.technoetic.xplanner.security.AuthenticationException;

public class PersistentObjectMother extends ObjectMother{

  protected List testObjects = new ArrayList();
  private Session session;
  private HibernateTemplate hibernateTemplateSimulation;
  private MetaRepository metaRepository;
  
  public void deleteTestObjects()
        throws HibernateException, SQLException, RepositoryException, AuthenticationException {
     if (testObjects.size() == 0) return;
     removeObjects(Note.class);
     removeObjects(File.class);
     removeObjects(Directory.class);
     removeObjects(Project.class);
     removeObjects(Iteration.class);
     removeObjects(UserStory.class);
     removeObjects(Task.class);
     removeObjects(TimeEntry.class);

     for (int i = testObjects.size() - 1; i >= 0; i--) {
        Object o = testObjects.get(i);
        if (o == null) continue;
        if (o instanceof Person) {
           metaRepository.getRepository(Person.class).delete(((Person) o).getId());
        } else {
          session.delete("from object in class " + o.getClass().getName() + " where object.id = ?",
                         new Integer(((Identifiable) o).getId()), Hibernate.INTEGER);
          // Don't use session.delete(o); since the object might have been deleted in the first place.
        }
     }
  }

  private void removeObjects(Class classType) throws HibernateException {
     List particularObjects = getSavedInstancesOf(testObjects, classType);
     removeSavedObjectsOf(particularObjects);
  }

  private void removeSavedObjectsOf(List savedObject) throws HibernateException {
     for (int i = savedObject.size() - 1; i >= 0; i--) {
        Object object = savedObject.get(i);
        deleteObject(object);
     }
//       commitSession();
  }

  public void deleteObject(Object object) throws HibernateException {
     if (PersistentObjectMother.LOG.isDebugEnabled()) PersistentObjectMother.LOG.debug("deleting " + object);
     try {
        session.delete("from object in class " + object.getClass().getName() + " where object.id = ?",
                       new Integer(((Identifiable) object).getId()), Hibernate.INTEGER);
     } catch (HibernateException e) {
        // Don't care if the object was deleted by the test. Just need to make sure it is gone!!!
     }
//      session.delete(object); // Cannot use because the object might have already been deleted through cascading -> error on flush
     testObjects.remove(object);
  }

  public List getSavedInstancesOf(List testObjects, Class objectClass) {
     List foundObjects = new ArrayList();
     Iterator it = testObjects.iterator();
     while (it.hasNext()) {
        Object obj = it.next();
        if (obj != null) {
           if (objectClass.isAssignableFrom(obj.getClass())) {
              foundObjects.add(obj);
           }
        }
     }
     return foundObjects;
  }

  public void registerObjectToBeDeletedOnTearDown(Object object) {
     if (object == null) {
        RuntimeException e = new RuntimeException("Cannot register a null object for deletion");
        PersistentObjectMother.LOG.error(e);
        throw e;
     }
     testObjects.add(object);
  }

  public void setSession(final Session session) throws HibernateException {
     this.session = session;
     this.hibernateTemplateSimulation = new HibernateTemplateSimulation(session);

     //DEBT(REPOSITORY) Should be created only once either through spring or through programmatic builder/factory
     this.metaRepository = new MetaRepositoryImpl();
     createRepository(Project.class);
     createRepository(Iteration.class);
     createRepository(UserStory.class);
     createRepository(Task.class);
     createRepository(Role.class);
     createRepository(Note.class);
     HibernateObjectRepository repository = new PersonHibernateObjectRepository(Person.class);
     repository.setHibernateTemplate(hibernateTemplateSimulation);
     metaRepository.setRepository(Person.class, repository);
  }

  public HibernateObjectRepository createRepository(Class objectClass) throws HibernateException {
     return createRepository(objectClass, HibernateObjectRepository.class);
  }

  public HibernateObjectRepository createRepository(Class objectClass, Class repositoryClass)
        throws HibernateException {
     HibernateObjectRepository repository = new HibernateObjectRepository(objectClass);
     repository.setHibernateTemplate(hibernateTemplateSimulation);
     metaRepository.setRepository(objectClass, repository);
     return repository;
  }

  public RoleAssociationRepositoryImpl createRoleAssociationRepository() throws HibernateException {
     RoleAssociationRepositoryImpl roleAssociationRepository = new RoleAssociationRepositoryImpl();
     RoleRepositoryImpl roleRepository = new RoleRepositoryImpl();
     roleRepository.setHibernateTemplate(hibernateTemplateSimulation);
     roleAssociationRepository.setRoleRepository(roleRepository);
     roleAssociationRepository.setHibernateTemplate(hibernateTemplateSimulation);
     return roleAssociationRepository;
  }

  protected void saveAndRegisterForDelete(Object object) throws HibernateException, RepositoryException {
     ObjectRepository repository = metaRepository.getRepository(object.getClass());
     if (repository != null) {
        repository.insert((NamedObject) object);
     } else {
        session.save(object);
     }
     registerObjectToBeDeletedOnTearDown(object);
  }
}
