package com.technoetic.xplanner.security.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AuthConstraint {
    private List<String> roleNames = new ArrayList<String>();

    public void addRoleName(String roleName) {
        roleNames.add(roleName);
    }

    public Collection<String> getRoleNames() {
        return roleNames;
    }
}
