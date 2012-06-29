/*
 * Copyright (c) Mateusz Prokopowicz. All Rights Reserved.
 */

package com.technoetic.xplanner.domain.repository;

import java.util.Iterator;
import java.util.List;

import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.Permission;
import net.sf.xplanner.domain.Person;

import org.hibernate.HibernateException;

import com.technoetic.xplanner.domain.Nameable;

/**
 * User: mprokopowicz
 * Date: Feb 15, 2006
 * Time: 4:09:12 PM
 */
public class PersonHibernateObjectRepository extends HibernateObjectRepository {
   static final String CHECK_PERSON_UNIQUENESS_QUERY = "com.technoetic.xplanner.domain.CheckPersonUniquenessQuery";

   public PersonHibernateObjectRepository(Class objectClass) throws HibernateException {
      super(objectClass);
   }

   @Override
public int insert(Nameable domainObject) throws RepositoryException {
      Person person = (Person) domainObject;
      checkPersonUniquness(person);
      int id = super.insert(person);
      setUpEditPermission(person);
      return id;
   }

   @Override
public void delete(final int objectIdentifier) throws RepositoryException {
      super.delete(objectIdentifier);
      getHibernateTemplate().bulkUpdate("delete from " + Permission.class.getName() + " where resource_id = ?",
                                    new Integer(objectIdentifier));
   }

   void setUpEditPermission(DomainObject person) {
      getHibernateTemplate().save(new Permission("system.person", person.getId(), person.getId(), "edit%"));
//        session.save(new Permission("system.person",person.getId(),person.getId(),"read%"));
      getHibernateTemplate().save(new Permission("system.person", 0, person.getId(), "read%"));
//        roleAssociationRepository.insertForPersonOnProject("viewer",person.getId(),0);
   }

   void checkPersonUniquness(Person person) throws DuplicateUserIdException {
      List potentialDuplicatePeople =
            getHibernateTemplate().findByNamedQuery(CHECK_PERSON_UNIQUENESS_QUERY,
                                                    new Object[]{new Integer(person.getId()), person.getUserId()});

      Iterator iterator = potentialDuplicatePeople.iterator();
      if (iterator.hasNext()) {
         while (iterator.hasNext()) {
            Person potentialDuplicatePerson = (Person) iterator.next();
            if (hasSameUserId(person, potentialDuplicatePerson)) {
               throw new DuplicateUserIdException();
            }
         }
      }
   }

   boolean hasSameUserId(Person person1, Person person2) {
//     if(SecurityHelper.isAuthenticationCaseSensitive())
//       return person1.getUserId().equals(person2.getUserId());
//     else
       return person1.getUserId().equalsIgnoreCase(person2.getUserId());
   }
}
