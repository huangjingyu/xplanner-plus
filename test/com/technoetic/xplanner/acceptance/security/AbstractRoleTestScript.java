package com.technoetic.xplanner.acceptance.security;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Role;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript;
import com.technoetic.xplanner.db.hibernate.IdGenerator;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.auth.AuthorizerImpl;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;
import com.technoetic.xplanner.security.module.LoginSupportImpl;
import com.technoetic.xplanner.security.module.XPlannerLoginModule;

public abstract class AbstractRoleTestScript extends AbstractDatabaseTestScript {
    protected XPlannerLoginModule authenticator;
    private Project project;

    public void setUp() throws Exception {
        super.setUp();
        project = newProject();
        SystemAuthorizer.set(new AuthorizerImpl());
        authenticator = new XPlannerLoginModule(new LoginSupportImpl());
    }

    public void tearDown() throws Exception {
        SystemAuthorizer.set(null);
        super.tearDown();
    }

    protected void removeTestPeople() throws HibernateException {
        getSession().delete("from person in "+Person.class+" where person.userId like 'testperson%'");
    }

    protected Person createPerson(String userId, String password) throws Exception {
        Person person = new Person(IdGenerator.getUniqueId(userId));
        person.setName(userId);
        person.setInitials("");
        person.setEmail("");
        if (password != null) {
            person.setPassword(authenticator.encodePassword(password, null));
        }
        getSession().save(person);
        super.registerObjectToBeDeletedOnTearDown(person);
        return person;
    }

    protected void assertPersonPresentInRoleWithPassword(String userId, String rolename, String password)
            throws HibernateException, AuthenticationException {
        Person person = getPerson(getSession(), userId);
        assertNotNull("person doesn't exist: "+userId, person);
        if (password != null) {
            try {
                authenticator.authenticate(userId, password);
            } catch (AuthenticationException e) {
                fail("auth failed: "+e.getMessage());
            }
        }
        Collection roles = SystemAuthorizer.get().
                getRolesForPrincipalOnProject(person.getId(), project.getId(), true);
        for (Iterator iterator = roles.iterator(); iterator.hasNext();) {
            Role role = (Role)iterator.next();
            if (role.getName().equals(rolename)) {
                return;
            }
        }
        fail("missing role: person="+userId+", role="+rolename);
    }

    protected Person getPerson(Session session, String userId) throws HibernateException {
        List people = session.find("from person in class " +
                Person.class.getName() + " where userid = ?", userId, Hibernate.STRING);
        Person person = null;
        Iterator peopleIterator = people.iterator();
        if (peopleIterator.hasNext()) {
            person = (Person)peopleIterator.next();
        }
        return person;
    }
}
