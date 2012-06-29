package com.technoetic.xplanner.acceptance.repository;

import java.util.Map;

import junit.framework.TestCase;

import org.hibernate.Transaction;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.db.hibernate.GlobalSessionFactory;
import com.technoetic.xplanner.db.hibernate.HibernateHelper;
import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.domain.repository.AttributeRepositoryImpl;

public class AttributeRepositoryTestScript extends TestCase {
    private Session session;
    private AttributeRepositoryImpl repository;

    protected void setUp() throws Exception {
        super.setUp();
        HibernateHelper.initializeHibernate();
        repository = new AttributeRepositoryImpl();
        session = GlobalSessionFactory.get().openSession();
        ThreadSession.set(session);
    }

    protected void tearDown() throws Exception {
        session.close();
        ThreadSession.set(null);
        super.tearDown();
    }

    public void testAttributeManagement() throws Exception {
        Transaction txn;

        txn = session.beginTransaction();
        repository.setAttribute(0, "test.x", "xvalue0");
        repository.setAttribute(0, "test.y", "yvalue0");
        repository.setAttribute(10000, "test.y", "foo");
        txn.commit();

        txn = session.beginTransaction();
        assertEquals("wrong attribute value", "xvalue0", repository.getAttribute(0, "test.x"));
        assertEquals("wrong attribute value", "yvalue0", repository.getAttribute(0, "test.y"));
        assertEquals("wrong attribute value", "foo", repository.getAttribute(10000, "test.y"));

        repository.delete(10000, "test.y");
        txn.commit();

        txn = session.beginTransaction();
        assertNull("wrong attribute value", repository.getAttribute(10000, "test.y"));

        Map attributes = repository.getAttributes(0, "test.");
        assertEquals("wrong attribute map", 2, attributes.size());
        assertEquals("wrong attribute value", "xvalue0", attributes.get("x"));
        assertEquals("wrong attribute value", "yvalue0", attributes.get("y"));

        // modification
        repository.setAttribute(0, "test.x", "another value");
        txn.commit();

        txn = session.beginTransaction();
        assertEquals("wrong attribute value", "another value", repository.getAttribute(0, "test.x"));;

        repository.delete(0, "test.x");
        repository.delete(0, "test.y");
        txn.commit();

        txn = session.beginTransaction();
        assertNull("wrong attribute value", repository.getAttribute(0, "test.x"));
        assertNull("wrong attribute value", repository.getAttribute(0, "test.y"));
        txn.commit();
    }

}
