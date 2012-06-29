/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.technoetic.xplanner.security.auth;

import static org.easymock.EasyMock.expect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import junitx.framework.ListAssert;
import net.sf.xplanner.domain.Person;

import com.technoetic.xplanner.AbstractUnitTestCase;
import com.technoetic.xplanner.security.AuthenticationException;

public class TestPermissionHelper extends AbstractUnitTestCase {
   public static final int PROJECT_ID = 1;
   private Authorizer authorizer;
   private int nextId = 2;

   public void testGetPeopleWithProjectRole_WithNoProjectReturnsAllPeople() throws Exception {
      Person person = new Person("test");
      List people = Arrays.asList(new Person[]{person});
      Collection peopleWithProjectRole = PermissionHelper.getPeopleWithProjectRole("0", people);
      ListAssert.assertEquals(people, new ArrayList(peopleWithProjectRole));
   }

   public void testGetPeopleWithProjectRole_WithAProjectReturnsAllPeopleWithPermissionOnProject() throws Exception {
      SystemAuthorizer.set(authorizer);
      Person personWithPermission1 = createPerson("viewer");
      Person personWithPermission2 = createPerson("editor");
      Person personWithPermission3 = createPerson("admin");
      Person personWithoutPermission1 = createPerson("nobody");
      Person personWithoutPermission2 = createPerson("anybody");

      expectCallToHasPermissionForPerson(personWithPermission1, true);
      expectCallToHasPermissionForPerson(personWithoutPermission1, false);
      expectCallToHasPermissionForPerson(personWithPermission2, true);
      expectCallToHasPermissionForPerson(personWithoutPermission2, false);
      expectCallToHasPermissionForPerson(personWithPermission3, true);

      replay();

      List people = Arrays.asList(new Person[]{personWithPermission1,
                                               personWithoutPermission1,
                                               personWithPermission2,
                                               personWithoutPermission2,
                                               personWithPermission3});
      List expectedPeople = Arrays.asList(new Person[]{personWithPermission1,
                                                       personWithPermission2,
                                                       personWithPermission3});

      Collection peopleWithProjectRole = PermissionHelper.getPeopleWithProjectRole(""+PROJECT_ID, people);
      ListAssert.assertEquals(expectedPeople, new ArrayList(peopleWithProjectRole));

      verify();
   }

   private Person createPerson(String userId) {
      Person personWithPermission = new Person(userId);
      personWithPermission.setId(nextId++);
      return personWithPermission;
   }

   private void expectCallToHasPermissionForPerson(Person person, boolean hasPermission) throws AuthenticationException {
      expect(authorizer.hasPermission(PROJECT_ID,
                               person.getId(),
                               "system.project",
                               PROJECT_ID,
                               "read%")).andReturn(hasPermission);
   }

   protected void tearDown() throws Exception {
      super.tearDown();
      SystemAuthorizer.set(null);
   }

   protected void setUp() throws Exception {
      super.setUp();
      authorizer = createGlobalMock(Authorizer.class);
   }
}