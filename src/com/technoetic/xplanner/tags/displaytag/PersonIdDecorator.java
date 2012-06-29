package com.technoetic.xplanner.tags.displaytag;

import net.sf.xplanner.domain.Person;

import org.hibernate.HibernateException;

import com.technoetic.xplanner.db.hibernate.ThreadSession;

public class PersonIdDecorator {
    public static String getPersonName(int id) {
        if (id <= 0) return "";
        Person person = getPerson(id);
        return person == null ? "" : person.getName();
    }

    private static Person getPerson(int id) {
        try {
            return (Person) ThreadSession.get().get(Person.class, new Integer(id));
        } catch (HibernateException e) {
            return null;
        }
    }
}