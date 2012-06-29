/*
 * Copyright (c) Mateusz Prokopowicz. All Rights Reserved.
 */

package com.technoetic.xplanner.domain.repository;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.hibernate.HibernateException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

/**
 * User: mprokopowicz
 * Date: Feb 8, 2006
 * Time: 10:05:47 AM
 */
public class ObjectRepositororyFactory implements BeanFactoryAware {
   private AutowireCapableBeanFactory beanFactory;
   private List delegates;

   public void setDelegates(List delegateList) {
      this.delegates = delegateList;
   }

   public ObjectRepository create(Class objectClass) throws HibernateException, NoSuchMethodException,
                                                            IllegalAccessException, InvocationTargetException,
                                                            InstantiationException {
      return create(objectClass, HibernateObjectRepository.class);
   }

   public ObjectRepository create(Class objectClass, Class objectRepositoryClass)
         throws HibernateException, NoSuchMethodException,
                IllegalAccessException, InvocationTargetException,
                InstantiationException {
      ObjectRepository repository = (ObjectRepository) objectRepositoryClass.getConstructor(new Class[]{Class.class})
            .newInstance(new Object[]{objectClass});
      beanFactory.autowireBeanProperties(repository, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
      for (int i = 0; i < delegates.size(); i++) {
         Class aClass = (Class) delegates.get(i);
         repository =
               (ObjectRepository) aClass.getConstructor(new Class[]{Class.class, ObjectRepository.class})
                     .newInstance(new Object[]{objectClass, repository});
         beanFactory.autowireBeanProperties(repository, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
      }
      return repository;
   }

   public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
      this.beanFactory = (AutowireCapableBeanFactory) beanFactory;
   }
}
