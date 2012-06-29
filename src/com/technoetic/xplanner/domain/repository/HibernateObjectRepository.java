package com.technoetic.xplanner.domain.repository;

import net.sf.xplanner.domain.Project;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.technoetic.xplanner.db.NoteHelper;
import com.technoetic.xplanner.domain.Nameable;
import com.technoetic.xplanner.domain.NoteAttachable;

public class HibernateObjectRepository extends HibernateDaoSupport implements ObjectRepository {
   private Class objectClass;
   private final String deletionQuery;

   public HibernateObjectRepository(Class objectClass) throws HibernateException {
      this.objectClass = objectClass;
      deletionQuery = "delete " + objectClass.getName() + " where id = ?";
   }

   public void delete(final int objectIdentifier) throws RepositoryException {
      try {
         if (NoteAttachable.class.isAssignableFrom(objectClass)) {
            // FIXME This unfortunately is not enough. we have cascade delete on from project down to time entry. Any of these contained entity could have notes that have files. These files won't be deleted
              NoteHelper.deleteNotesFor(objectClass, objectIdentifier, getHibernateTemplate());
         }
         
         getHibernateTemplate().delete(getSession().get(objectClass, objectIdentifier));
      } catch (HibernateObjectRetrievalFailureException e) {
         throw new ObjectNotFoundException(e.getMessage());
      } catch (DataAccessException ex) {
         throw new RepositoryException(ex);
      }
   }

   public Object load(final int objectIdentifier) throws RepositoryException {
      try {
         return getHibernateTemplate().load(objectClass, new Integer(objectIdentifier));

      } catch (HibernateObjectRetrievalFailureException e) {
         throw new ObjectNotFoundException(e.getMessage());
      } catch (DataAccessException ex) {
         throw new RepositoryException(ex);
      }
   }

   public int insert(final Nameable object) throws RepositoryException {
      try {
         Integer id = (Integer) getSession2().save(object);
         return id.intValue();

      } catch (HibernateObjectRetrievalFailureException e) {
         throw new ObjectNotFoundException(e.getMessage());
      } catch (DataAccessException ex) {
         throw new RepositoryException(ex);
      }
   }

   protected Session getSession2() {
	   return getSession();
   }
   
	@Override
	public void update(Nameable object) throws RepositoryException {
		// TODO Auto-generated method stub
	}
}
