package com.technoetic.xplanner.security.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

public class SecurityConfiguration {
    private ArrayList securityConstraints = new ArrayList();
    private ArrayList securityRoles = new ArrayList();
    private WebResourceCollection securityBypass;

    public void addSecurityConstraint(SecurityConstraint constraint) {
        securityConstraints.add(constraint);
    }

    public void addSecurityRole(SecurityRole securityRole) {
        securityRoles.add(securityRole);
    }

    public ArrayList getSecurityConstraints() {
        return securityConstraints;
    }

    public WebResourceCollection getSecurityBypass() {
        return securityBypass;
    }

    public void setSecurityBypass(WebResourceCollection securityBypass) {
        this.securityBypass = securityBypass;
    }

    public boolean isAuthorized(HttpServletRequest request) {
        if (!isSecureRequest(request)) {
            return true;
        } else {
            Iterator securityConstraints = getSecurityConstraints().iterator();
            while (securityConstraints.hasNext()) {
                SecurityConstraint securityConstraint = (SecurityConstraint)securityConstraints.next();
                if (securityConstraint.isApplicable(request)) {
                    return securityConstraint.isAuthorized(request);
                }
            }
            return true;
        }
    }

    public boolean isSecureRequest(HttpServletRequest request) {
        return !(securityBypass != null && securityBypass.matches(request));
    }

    public ArrayList getSecurityRoles() {
        return securityRoles;
    }

    public static SecurityConfiguration load(String filename) throws SAXException, IOException {
        return load(new FileInputStream(filename));
    }

    public static SecurityConfiguration load(InputStream in) throws SAXException, IOException {
        Digester digester = new Digester();
        digester.setValidating(false);
        digester.setClassLoader(SecurityConfiguration.class.getClassLoader());

        digester.addObjectCreate("security", SecurityConfiguration.class);

        digester.addObjectCreate("security/security-constraint", SecurityConstraint.class);
        digester.addBeanPropertySetter("security/security-constraint/display-name", "displayName");
        digester.addSetNext("security/security-constraint", "addSecurityConstraint");

        digester.addObjectCreate(
                "security/security-bypass", WebResourceCollection.class);
        digester.addCallMethod(
                "security/security-bypass/url-pattern", "addUrlPattern", 0);
        digester.addSetNext(
                "security/security-bypass", "setSecurityBypass");

        digester.addObjectCreate(
                "security/security-constraint/web-resource-collection", WebResourceCollection.class);
        digester.addBeanPropertySetter(
                "security/security-constraint/web-resource-collection/web-resource-name", "name");
        digester.addCallMethod(
                "security/security-constraint/web-resource-collection/url-pattern", "addUrlPattern", 0);
        digester.addSetNext(
                "security/security-constraint/web-resource-collection", "addWebResourceCollection");

        digester.addObjectCreate(
                "security/security-constraint/auth-constraint", AuthConstraint.class);
        digester.addCallMethod(
                "security/security-constraint/auth-constraint/role-name", "addRoleName", 0);
        digester.addSetNext(
                "security/security-constraint/auth-constraint", "addAuthConstraint");

        digester.addObjectCreate(
                "security/security-role", SecurityRole.class);
        digester.addBeanPropertySetter(
                "security/security-role/role-name", "name");
        digester.addSetNext(
                "security/security-role", "addSecurityRole");

        return (SecurityConfiguration)digester.parse(in);
    }
}
