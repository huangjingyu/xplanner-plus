/*
 * Copyright (c) Mateusz Prokopowicz. All Rights Reserved.
 */

package com.technoetic.xplanner.domain.repository;
/**
 * User: mprokopowicz
 * Date: Feb 15, 2006
 * Time: 4:58:42 PM
 */

import static org.easymock.EasyMock.*;

import java.util.Arrays;
import java.util.List;

import net.sf.xplanner.domain.Permission;
import net.sf.xplanner.domain.Person;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.technoetic.xplanner.AbstractUnitTestCase;
import com.technoetic.xplanner.XPlannerTestSupport;

public class TestPersonHibernateObjectRepository extends AbstractUnitTestCase {
   private PersonHibernateObjectRepository personHibernateObjectRepository;
   private HibernateTemplate mockHibernateTemplate;
   private Person person;
   static final String TEST_PERSON_USER_ID = "testPerson";

   protected void setUp() throws Exception {
	   super.setUp();
      person = new Person(TEST_PERSON_USER_ID);
      person.setId(XPlannerTestSupport.DEFAULT_PERSON_ID);
      mockHibernateTemplate = createLocalMock(HibernateTemplate.class);
      personHibernateObjectRepository = new PersonHibernateObjectRepository(Person.class);
      personHibernateObjectRepository.setHibernateTemplate(mockHibernateTemplate);
   }

   public void testSetUpEditPermission() throws Exception {
      setUpThreadSession(false);
      expect(mockHibernateTemplate.save(new Permission("system.person",
                                                XPlannerTestSupport.DEFAULT_PERSON_ID,
                                                XPlannerTestSupport.DEFAULT_PERSON_ID,
                                                "edit%"))).andReturn(null);
      expect(mockHibernateTemplate.save(new Permission("system.person", 0, XPlannerTestSupport.DEFAULT_PERSON_ID, "read%"))).andReturn(null);
      replay();
      personHibernateObjectRepository.setUpEditPermission(person);
      verify();
   }


   public void testCheckPersonUniquness() throws Exception {
      Person personWhoAlreadyExist = new Person(TEST_PERSON_USER_ID);
      List expectedPeopleList = Arrays.asList(new Object[]{personWhoAlreadyExist});
      expect(mockHibernateTemplate.findByNamedQuery(eq(PersonHibernateObjectRepository.CHECK_PERSON_UNIQUENESS_QUERY),
                                             aryEq(new Object[]{new Integer(XPlannerTestSupport.DEFAULT_PERSON_ID),
                                                          TEST_PERSON_USER_ID}))).andReturn(expectedPeopleList);
      replay();
      try {
         personHibernateObjectRepository.checkPersonUniquness(person);
         fail(DuplicateUserIdException.class.getName()  +" has not been thrown");
      }
      catch (DuplicateUserIdException ex) {}
      verify();
   }
}