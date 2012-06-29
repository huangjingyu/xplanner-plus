package com.technoetic.xplanner.soap.domain;

import net.sf.xplanner.domain.Person;

public class PersonData extends DomainData {
    private String name;
    private String email;
    private String phone;
    private String initials;
    private String userId;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getInitials() {
        return initials;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static Class getInternalClass() {
        return Person.class;
    }
}