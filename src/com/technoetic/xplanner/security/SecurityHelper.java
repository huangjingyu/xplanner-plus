package com.technoetic.xplanner.security;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import net.sf.xplanner.domain.Role;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.security.module.LoginModuleLoader;

public class SecurityHelper {
    public static final String SECURITY_SUBJECT_KEY = "SECURITY_SUBJECT";
    private static final String SAVED_URL_KEY = "SAVED_URL";

    public static boolean isUserAuthenticated(HttpServletRequest request) {
    	try{
    		return (getSubject(request) != null) && (((PersonPrincipal)getUserPrincipal(getSubject(request))).getPerson().getId()!=0);
    	}catch (Exception e) {
    		return false;
		}
    }

    public static int getRemoteUserId(HttpServletRequest request) throws AuthenticationException {
        return ((PersonPrincipal)getUserPrincipal(request)).getPerson().getId();
    }

    public static int getRemoteUserId(PageContext context) throws AuthenticationException {
        return getRemoteUserId((HttpServletRequest) context.getRequest());
    }

    public static boolean isUserInRole(HttpServletRequest request, String roleName) {
        Subject subject = getSubject(request);
        if (subject != null) {
            Iterator roles = subject.getPrincipals(Role.class).iterator();
            while (roles.hasNext()) {
                Role role = (Role)roles.next();
                if (role.getName().equals(roleName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void setSubject(HttpServletRequest request, Subject subject) {
        request.getSession(true).setAttribute(SECURITY_SUBJECT_KEY, subject);
    }

    public static Subject getSubject(HttpServletRequest request) {
        return (Subject)request.getSession().getAttribute(SECURITY_SUBJECT_KEY);
    }

    public static Subject getSubject(PageContext context) {
        return getSubject((HttpServletRequest) context.getRequest());
    }

    public static Principal getUserPrincipal(Subject subject) throws AuthenticationException {
        if (subject != null) {
            Iterator people = subject.getPrincipals(PersonPrincipal.class).iterator();
            if (people.hasNext()) {
                return (PersonPrincipal)people.next();
            }
        }
        throw new AuthenticationException("no user principal in session");
    }

    public static void saveUrl(HttpServletRequest request) {
        request.getSession().setAttribute(SAVED_URL_KEY,
                request.getRequestURL().toString()+"?"+request.getQueryString());
    }

    public static String getSavedUrl(HttpServletRequest request) {
        return (String)request.getSession().getAttribute(SAVED_URL_KEY);
    }

    public static Principal getUserPrincipal(HttpServletRequest request) throws AuthenticationException {
        return getUserPrincipal(getSubject(request));
    }

    public static Subject addRolesToSubject(Subject subject, ArrayList roles) {
        // This approach is required because some servlet ISP's set up
        // security so that Subject cannot be modified even if it is not
        // in read-only mode.
        java.util.HashSet principals = new java.util.HashSet();
        principals.addAll(subject.getPrincipals());
        principals.addAll(roles);
        subject = new Subject(true, principals, subject.getPublicCredentials(), subject.getPrivateCredentials());
        return subject;
    }

   //FIXME: Is it right that the authentication be case sensitive if at least one module is?
    public static boolean isAuthenticationCaseSensitive(){
      XPlannerProperties properties = new XPlannerProperties();
        Iterator propertiesIterator = properties.getPropertyNames();
        while(propertiesIterator.hasNext()){
            String property = (String)propertiesIterator.next();
            if(property!=null &&
               property.startsWith(LoginModuleLoader.LOGIN_MODULE_PROPERTY_PREFIX + "[") &&
               property.endsWith("].option.userIdCaseSensitive")){
                if(new Boolean(properties.getProperty(property).trim()).booleanValue()){
                    return true;
                }
            }
        }
        return false;
    }

}
