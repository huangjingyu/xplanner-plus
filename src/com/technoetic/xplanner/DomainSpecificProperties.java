/*
 * Copyright (c) Mateusz Prokopowicz. All Rights Reserved.
 */

package com.technoetic.xplanner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import net.sf.xplanner.domain.Attribute;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * User: mprokopowicz
 * Date: Feb 6, 2006
 * Time: 12:59:52 PM
 */
public class DomainSpecificProperties extends Properties {
   private final SessionFactory sessionFactory;
   private final Object object;

   protected DomainSpecificProperties(Properties defaultProperties,
                                      SessionFactory sessionFactory,
                                      Object domainObject) {
      super(defaultProperties);
      this.sessionFactory = sessionFactory;
      this.object = domainObject;
   }

   @Override
public String getProperty(String key) {
      HibernateTemplate hibernateTemplate = new HibernateTemplate(this.sessionFactory);
      final Integer attributeTargetId = getAttributeTargetId(object, hibernateTemplate);
      if (attributeTargetId != null) {
         HibernateCallback action = new GetAttributeHibernateCallback(attributeTargetId, key);
         Attribute attribute = (Attribute) hibernateTemplate.execute(action);
         if (attribute != null) {
            return attribute.getValue();
         }
      }
      return super.getProperty(key);
   }


   private Integer getAttributeTargetId(Object object, HibernateTemplate hibernateTemplate) {
      if (object instanceof Project) {
         Project project = (Project) object;
         return new Integer(project.getId());
      }
      if (object instanceof Iteration) {
         Iteration iteration = (Iteration) object;
         return new Integer(iteration.getProject().getId());
      }
      Integer iterationId = null;
      if (object instanceof UserStory) {
         UserStory story = (UserStory) object;
         iterationId = new Integer(story.getIteration().getId());
      }
      if (object instanceof Task) {
         Task task = (Task) object;
         iterationId = new Integer(task.getUserStory().getIteration().getId());
      }
      if (iterationId != null) {
         final Integer id = iterationId;
         Iteration iteration = (Iteration) hibernateTemplate.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
               return session.load(Iteration.class, id);
            }
         });
         return new Integer(iteration.getProject().getId());
      }
      return null;
   }

   @Override
public synchronized Enumeration keys() {
      Set keys = keySet();
      return Collections.enumeration(keys);
   }

   @Override
public Set keySet() {
      Set keys = new HashSet(defaults.keySet());
      keys.addAll(super.keySet());
      return keys;
   }

   @Override
public Collection values() {
      List values = new ArrayList(defaults.values());
      values.addAll(super.values());
      return values;
   }


   private static class GetAttributeHibernateCallback implements HibernateCallback {
      private final Integer attributeTargetId;
      private final String key;

      public GetAttributeHibernateCallback(Integer attributeTargetId, String key) {
         this.attributeTargetId = attributeTargetId;
         this.key = key;
      }

      @Override
      public Object doInHibernate(Session session) throws HibernateException {
         Attribute attribute = null;
         Query query = session.createQuery("select attribute from Attribute attribute where attribute.id.targetId = ? and attribute.id.name= ? ");
         query.setParameter(0, attributeTargetId);
         query.setParameter(1, key);
         List attributeList = query.list();
         if (attributeList.size() > 0) {
            attribute = (Attribute) attributeList.get(0);
         }
         return attribute;
      }
   }
}
