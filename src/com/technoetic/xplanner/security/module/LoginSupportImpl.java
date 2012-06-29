package com.technoetic.xplanner.security.module;

import java.util.Iterator;
import java.util.List;

import javax.security.auth.Subject;

import net.sf.xplanner.domain.Person;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.LoginModule;
import com.technoetic.xplanner.security.PersonPrincipal;

public class LoginSupportImpl implements LoginSupport {
   public static final String MESSAGE_STORAGE_ERROR_KEY = "authentication.module.message.storageError";

   public Person populateSubjectPrincipalFromDatabase(Subject subject, String userId)
         throws AuthenticationException {
      Person person = null;
      try {
         person = getPerson(userId);
      } catch (HibernateException e) {
         throw new AuthenticationException(MESSAGE_STORAGE_ERROR_KEY);
      }
      if (person == null) {
         throw new AuthenticationException(LoginModule.MESSAGE_USER_NOT_FOUND_KEY);
      }
      subject.getPrincipals().clear();
      subject.getPrincipals().add(new PersonPrincipal(person));
      return person;
   }

   public Person getPerson(String userId) throws HibernateException {
      Session session = ThreadSession.get();
      List people = session.find("from person in class " +
                                 Person.class.getName() + " where userid = ?",
                                 userId, Hibernate.STRING);
      Iterator peopleIterator = people.iterator();
      if (peopleIterator.hasNext()) {
         return (Person) peopleIterator.next();
      } else {
         return null;
      }
   }

   public Subject createSubject() {
      return new Subject();
   }
}