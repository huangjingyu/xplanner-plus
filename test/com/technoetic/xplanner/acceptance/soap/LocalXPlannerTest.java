package com.technoetic.xplanner.acceptance.soap;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;

import net.sf.xplanner.domain.Person;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

import com.kizna.servletunit.HttpServletRequestSimulator;
import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.filters.ThreadServletRequest;
import com.technoetic.xplanner.security.PersonPrincipal;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.security.auth.AuthorizerImpl;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;
import com.technoetic.xplanner.soap.XPlanner;

public class LocalXPlannerTest extends AbstractSoapTestCase {

    public LocalXPlannerTest(String s) {
        super(s);
    }

    public void setUp() throws Exception {
        super.setUp();
        SystemAuthorizer.set(new AuthorizerImpl());
        ThreadServletRequest.set(setUpRequest());
    }

    private HttpServletRequest setUpRequest() throws Exception {
        HttpServletRequestSimulator request = new HttpServletRequestSimulator();
        HashSet principals = new HashSet();
        principals.add(new PersonPrincipal(getPerson(ThreadSession.get(), "sysadmin")));
        request.getSession().setAttribute(SecurityHelper.SECURITY_SUBJECT_KEY,
                new Subject(true, principals, new HashSet(), new HashSet()));
        return request;
    }

    private Person getPerson(Session session, String name) throws HibernateException {
        List people = session.find("from person in class " +
                Person.class.getName() + " where userid = ?",
                name, Hibernate.STRING);
        Person person = null;
        Iterator peopleIterator = people.iterator();
        if (peopleIterator.hasNext()) {
            person = (Person)peopleIterator.next();
        }
        return person;
    }

    public void tearDown() throws Exception {
        ThreadServletRequest.set(null);
        super.tearDown();
    }

    public XPlanner createXPlanner() {
        return XPlannerTestAdapter.create();
    }

}
