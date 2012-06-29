package com.technoetic.xplanner.forms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.xplanner.domain.Role;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;

public class PersonEditorForm extends AbstractEditorForm {
	private static final long serialVersionUID = -5950275086457397788L;
	
	private String name;
    private String email;
    private String phone;
    private int personId;
    private String initials;
    private String userId;
    private String password;
    private String newPassword;
    private String newPasswordConfirm;
    private boolean isHidden;
    private List<String> projectIds = new ArrayList<String>();
    private List<String> projectRoles = new ArrayList<String>();
    private boolean isSystemAdmin;
    public static final String PASSWORD_MISMATCH_ERROR = "person.editor.password_mismatch";

    @Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (isSubmitted()) {
            require(errors, name, "person.editor.missing_name");
            require(errors, userId, "person.editor.missing_user_id");
            require(errors, email, "person.editor.missing_email");
            require(errors, initials, "person.editor.missing_initials");
            if (StringUtils.isNotEmpty(newPassword) && !StringUtils.equals(newPassword, newPasswordConfirm)) {
                error(errors, PASSWORD_MISMATCH_ERROR);
            }
        }
        return errors;
    }

    @Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        name = null;
        email = null;
        phone = null;
        initials = null;
        userId = null;
        personId = 0;
        newPassword = null;
        newPasswordConfirm = null;
        isHidden = false;
        isSystemAdmin = false;
    }

    public String getContainerId() {
        return Integer.toString(personId);
    }

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

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getPersonId() {
        return personId;
    }


    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getInitials() {
        return initials;
    }

    /** Alias for userId so field won't be filled in automatically by Mozilla, etc. */
    public String getUserIdentifier() {
        return getUserId();
    }

    /** Alias for userId so field won't be filled in automatically by Mozilla, etc. */
    public void setUserIdentifier(String userId) {
        setUserId(userId);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordConfirm() {
        return newPasswordConfirm;
    }

    public void setNewPasswordConfirm(String newPasswordConfirm) {
        this.newPasswordConfirm = newPasswordConfirm;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    public void setProjectId(int index, String projectId) {
        ensureSize(projectIds, index + 1);
        projectIds.set(index, projectId);

    }

    public String getProjectId(int index) {
        return projectIds.get(index);
    }

    public int getProjectIdAsInt(int index) {
        return Integer.parseInt(getProjectId(index));
    }

    public int getProjectCount() {
        return projectIds.size();
    }

    public void setProjectRole(int index, String role) {
        ensureSize(projectRoles, index + 1);
        projectRoles.set(index, role);
    }

    public String getProjectRole(int index) {
        return projectRoles.get(index);
    }

    public void setSystemAdmin(boolean isSystemAdmin) {
        this.isSystemAdmin = isSystemAdmin;
    }

    public boolean isSystemAdmin() {
        return isSystemAdmin;
    }

    // These helper functions should be refactored into the domain objects

    private boolean hasRole(Collection<Role> roles, String name) {
        for (Iterator<Role> iterator = roles.iterator(); iterator.hasNext();) {
            Role role = iterator.next();
            if (role.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    // This is for 0.6. In the future there will be a more general role editing and
    // management framework.
   //DEBT remove all references to roles title. This hierarchy should be captured declaratively somewhere.
    private String getEffectiveRole(Collection<Role> roles) {
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

    public String isRoleSelected(String role, int projectId) throws AuthenticationException {
        return getEffectiveRole(getRoles(projectId)).equals(role)  ? "selected='selected'" : "";
    }

    public Collection<Role> getRoles(int projectId) throws AuthenticationException {
        return SystemAuthorizer.get().
                getRolesForPrincipalOnProject(getId(), projectId, false);
    }

    public List<String> getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(List<String> projectIds) {
        this.projectIds = projectIds;
    }

    public List<String> getProjectRoles() {
        return projectRoles;
    }

    public void setProjectRoles(List<String> projectRoles) {
        this.projectRoles = projectRoles;
    }

    public boolean isSysAdmin() throws AuthenticationException {
        return CollectionUtils.find(getRoles(0), new Predicate() {
            public boolean evaluate(Object o) {
                return (((Role)o).getName().equals("sysadmin"));
            }
        }) != null;
    }
}