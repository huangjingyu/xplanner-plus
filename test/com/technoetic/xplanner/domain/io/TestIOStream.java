package com.technoetic.xplanner.domain.io;
/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import junitx.framework.EqualAssert;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.TimeEntry;
import net.sf.xplanner.domain.UserStory;

import org.hibernate.HibernateException;

import com.technoetic.xplanner.AbstractUnitTestCase;
import com.technoetic.xplanner.domain.repository.RepositoryException;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.util.ClassUtil;

public class TestIOStream extends AbstractUnitTestCase {
  IOStream ioStream;
  int nextId;

  public void test() throws Exception {
    IOStream ioStream = new IOStream();

    Person[] persons = createPersons(10);
    Project expectedProject = createPopulatedProject(persons);

    String xml = ioStream.toXML(expectedProject);
    System.out.println("xml = " + xml);
    Project actualProject = (Project) ioStream.fromXML(xml);
    new EqualAssert(EqualAssert.FIELDS).assertEquals(expectedProject, actualProject);
  }

  private Person[] createPersons(int count) throws HibernateException, AuthenticationException, RepositoryException {
    Person[] persons = new Person[count];
    for (int i = 0; i < persons.length; i++) {
      persons[i] = mom.newPerson();
    }
    return persons;
  }

  private Project createPopulatedProject(Person[] persons) throws HibernateException, RepositoryException
  {
    Project expectedProject = mom.newProject();
    Iteration iteration = mom.newIteration(expectedProject);
    UserStory story = mom.newUserStory(iteration);
    story.setCustomer(getPerson(persons));
    story.setTrackerId(getPerson(persons).getId());
    Task task = mom.newTask(story);
    task.setAcceptorId(getPerson(persons).getId());
    TimeEntry entry = mom.newTimeEntry(task, getPerson(persons), 1.0);
    return expectedProject;
  }

  private Person getPerson(Person[] persons) {
    return persons[((int) (Math.random()*persons.length))];
  }

  private void fillIn(Object o) throws Exception {
    List fields = ClassUtil.getAllFields(o);
    for (int i = 0; i < fields.size(); i++) {
      Field field = (Field) fields.get(i);
      Object value = getValue(field);
      System.out.println("Setting " + field + "=" + value);
      field.set(o, value);
    }
  }

  private Object getValue(Field field) {
    Object value = null;
    Class type = field.getType();
    ++nextId;
    if (String.class.isAssignableFrom(type)) {
      return "Text " + nextId;
    } else if (Integer.class.isAssignableFrom(type) || Integer.TYPE==type) {
      return new Integer(nextId);
    } else if (Long.class.isAssignableFrom(type) || Long.TYPE==type) {
      return new Long(nextId);
    } else if (Float.class.isAssignableFrom(type) || Float.TYPE==type) {
      return new Float(nextId);
    } else if (Double.class.isAssignableFrom(type) || Double.TYPE==type) {
      return new Double(nextId);
    } else if (Boolean.class.isAssignableFrom(type) || Boolean.TYPE==type) {
      return nextId % 2 == 0 ? Boolean.FALSE : Boolean.TRUE;
    } else if (Date.class.isAssignableFrom(type)) {
      return new Date(new Date().getTime() + nextId*1000*60*60);
    }

    return value;
  }

}