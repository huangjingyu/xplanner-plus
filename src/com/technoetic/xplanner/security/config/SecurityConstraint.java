package com.technoetic.xplanner.security.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import com.technoetic.xplanner.security.SecurityHelper;

public class SecurityConstraint {
    private ArrayList webResourceCollections = new ArrayList();
    private ArrayList authConstraints = new ArrayList();
    private String displayName;

    public void addWebResourceCollection(WebResourceCollection collection) {
        webResourceCollections.add(collection);
    }

    public void addAuthConstraint(AuthConstraint authConstraint) {
        authConstraints.add(authConstraint);
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Collection getWebResourceCollection() {
        return webResourceCollections;
    }

    public Collection getAuthConstraints() {
        return authConstraints;
    }

    public boolean isApplicable(HttpServletRequest request) {
        Iterator webResourceCollections = getWebResourceCollection().iterator();
        while (webResourceCollections.hasNext()) {
            WebResourceCollection webResourceCollection = (WebResourceCollection)webResourceCollections.next();
            if (webResourceCollection.matches(request)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAuthorized(HttpServletRequest request) {
        Iterator authConstraints = getAuthConstraints().iterator();
        while (authConstraints.hasNext()) {
            AuthConstraint authConstraint = (AuthConstraint)authConstraints.next();
            Iterator roleNames = authConstraint.getRoleNames().iterator();
            while (roleNames.hasNext()) {
                String role = (String)roleNames.next();
                if ((SecurityHelper.getSubject(request) != null && role.equals("*")) ||
                        SecurityHelper.isUserInRole(request, role)) {
                    return true;
                }
            }
        }
        return false;
    }
}
