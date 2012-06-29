package com.technoetic.xplanner.domain.repository;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.HibernateException;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.technoetic.xplanner.domain.Nameable;
import com.technoetic.xplanner.filters.ThreadServletRequest;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.security.auth.AuthorizationException;
import com.technoetic.xplanner.security.auth.Authorizer;
import com.technoetic.xplanner.tags.DomainContext;

// dao -- how should other methods be protected? repository-specific ones?

public class RepositorySecurityAdapter extends HibernateDaoSupport implements ObjectRepository {
   private Class objectClass;
   private ObjectRepository delegate;
   private Authorizer authorizer;

   public void setAuthorizer(Authorizer authorizer) {
      this.authorizer = authorizer;
   }

   public RepositorySecurityAdapter(Class objectClass, ObjectRepository delegate) throws HibernateException {
      this.objectClass = objectClass;
      this.delegate = delegate;
   }

   public void delete(final int objectIdentifier) throws RepositoryException {
      try {
      // todo How should the project ID be obtained without refs to Hibernate?
         Object object = getHibernateTemplate().load(objectClass, new Integer(objectIdentifier));
         checkAuthorization(object, "delete");
      } catch (HibernateObjectRetrievalFailureException e) {
         throw new ObjectNotFoundException(e.getMessage());
      } catch (DataAccessException e) {
         throw new RepositoryException(e);
      }
      delegate.delete(objectIdentifier);
   }

   public int insert(Nameable object) throws RepositoryException {
      // do-before-release Fix insert authorization - problem with missing project context
//        checkAuthorization(object, "create");
      return delegate.insert(object);
   }

   public Object load(int objectIdentifier) throws RepositoryException {
      Object loadedObject = delegate.load(objectIdentifier);
      checkAuthorization(loadedObject, "read");
      return loadedObject;
   }

   private void checkAuthorization(Object object, String permission)
         throws RepositoryException {
      DomainContext context = new DomainContext();
      try {
         context.populate(object);
         int objectIdentifier = ((Integer) PropertyUtils.getProperty(object, "id")).intValue();
         if (!authorizer.hasPermission(context.getProjectId(),
                                                   SecurityHelper.getRemoteUserId(ThreadServletRequest.get()),
                                                   object,
                                                   permission)) {
            throw new AuthorizationException("not authorized for object " + permission + ": " +
                                             objectClass + " " + objectIdentifier);
         }
      } catch (RuntimeException e) {
         throw e;
      } catch (Exception e) {
         throw new RepositoryException(e);
      }
   }

   public void update(Nameable object) throws RepositoryException {
      checkAuthorization(object, "edit");
      delegate.update(object);
   }
}
