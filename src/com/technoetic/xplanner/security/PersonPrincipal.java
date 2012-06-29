package com.technoetic.xplanner.security;

import java.io.Serializable;
import java.security.Principal;

import net.sf.xplanner.domain.Person;

public class PersonPrincipal implements Principal, Serializable {
    private Person person;

    public PersonPrincipal(Person person) {
        this.person = person;
    }

    public String getName() {
        return person.getUserId();
    }

    public Person getPerson() {
        return person;
    }
}
