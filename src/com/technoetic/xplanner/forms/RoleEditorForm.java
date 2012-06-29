package com.technoetic.xplanner.forms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import net.sf.xplanner.domain.Role;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;

public class RoleEditorForm extends AbstractEditorForm {
    private ArrayList personIds = new ArrayList();
    private ArrayList personRoles = new ArrayList();

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        return errors;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
    }

    public void reset() {

    }

    public int getPersonCount() {
        return personIds.size();
    }


    public String getPersonId(int index) {
        return (String)personIds.get(index);
    }

    public int getPersonIdAsInt(int index) {
        if (getPersonId(index) == null) {
            return -1;
        } else
            return Integer.parseInt(getPersonId(index));
    }

    public void setPersonId(int index, String personId) {
        ensureSize(personIds, index + 1);
        personIds.set(index, personId);

    }

    /* D�finir le r�le d'une personne*/
    public void setPersonRole(int index, String role) {
        ensureSize(personRoles, index + 1);
        personRoles.set(index, role);
    }

    /* Renvoi du r�le d'une personne*/
    public String getPersonRole(int index) {
        return (String)personRoles.get(index);
    }


    private boolean hasRole(Collection roles, String name) {
        for (Iterator iterator = roles.iterator(); iterator.hasNext();) {
            Role role = (Role)iterator.next();
            if (role.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }


    private String getEffectiveRole(Collection roles) {
        if (hasRole(roles, "admin")) {
            return "admin";
        } else if (hasRole(roles, "editor")) {
            return "editor";
        } else if (hasRole(roles, "viewer")) {
            return "viewer";
        } else {
            return "none";
        }
    }


    public String isRoleSelected(String role, int personId) throws AuthenticationException {
        return getEffectiveRole(getRoles(personId)).equals(role) ? "selected='selected'" : "";
    }

    public Collection getRoles(int personId) throws AuthenticationException {
        return SystemAuthorizer.get().getRolesForPrincipalOnProject(personId, getId(),false);
    }


    public boolean isSysAdmin() throws AuthenticationException {
        return CollectionUtils.find(getRoles(0), new Predicate() {
            public boolean evaluate(Object o) {
                return (((Role)o).getName().equals("sysadmin"));
            }
        }) != null;
    }


}
