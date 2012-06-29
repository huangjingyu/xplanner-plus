package com.technoetic.xplanner.db;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.domain.repository.RepositoryException;

public class SearchContentQuery {
   private int restrictedProjectId = 0;

   public List findWhereNameOrDescriptionContains(String searchCriteria, Class objectClass)
         throws RepositoryException {
      try {
         return runSearchQuery(searchCriteria, objectClass);
      } catch (HibernateException e) {
         throw new RepositoryException(e);
      }
   }

   public void setRestrictedProjectId(int restrictedProjectId) {
      this.restrictedProjectId = restrictedProjectId;
   }

   private List runSearchQuery(String searchCriteria, Class objectClass) throws HibernateException {
      Query query = ThreadSession.get().getNamedQuery(getQueryName(objectClass));
      query.setString("contents", "%" + searchCriteria + "%");
      if (restrictedProjectId > 0){
        query.setInteger("projectId", restrictedProjectId);
      }
      return copyResults(query.list());
   }

   private String getQueryName(Class objectClass) {
      return objectClass.getName()  + (restrictedProjectId == 0 ? "SearchQuery" : "RestrictedSearchQuery");
   }

   private List copyResults(List results) {
      ArrayList returnValue = new ArrayList();
      if (results != null) {
         returnValue.addAll(results);
      }
      return returnValue;
   }
}
