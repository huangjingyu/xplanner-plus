package com.technoetic.xplanner.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.dao.impl.CommonDao;
import net.sf.xplanner.domain.PersonRole;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Role;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.technoetic.xplanner.forms.RoleEditorForm;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;

public class EditRoleAction extends EditObjectAction {
    public static final String SYSADMIN_ROLE_NAME = "sysadmin";
	protected void beforeObjectCommit(Object object, Session session, ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse reply) throws Exception {

        RoleEditorForm roleForm = (RoleEditorForm)actionForm;

        Project project = (Project)object;

        int projectId = project.getId();

        for (int index = 0; index < roleForm.getPersonCount(); index++) {
            int personId = roleForm.getPersonIdAsInt(index);

            if (isAuthorizedRoleAdministratorForProject(request, projectId)) {

                deleteRoleAssociationsForProject(session, projectId, personId);
                addRoleAssociationForProject(session, projectId, personId, roleForm.getPersonRole(index));
            }
        }
    }


    private boolean isAuthorizedRoleAdministratorForProject(HttpServletRequest request, int projectId)
            throws AuthenticationException {
        return SystemAuthorizer.get().hasPermission(projectId, SecurityHelper.getRemoteUserId(request),
                "system.project", projectId, "admin.edit.role");
    }

    private void addRoleAssociationForProject(Session session, int projectId, int personId, String roleName)
            throws HibernateException {
        Role role = getRoleByName(session, roleName);
        if (role != null) {
            session.save(new PersonRole(projectId, personId, role.getId()));
        }
    }

    private void deleteRoleAssociationsForProject(Session session, int projectId, int personId)
            throws HibernateException {
        session.delete("from assoc in " + PersonRole.class +
                " where assoc.id.personId = ? and assoc.id.projectId = ?",
                new Object[]{new Integer(personId), new Integer(projectId)},
                new Type[]{Hibernate.INTEGER, Hibernate.INTEGER});
    }

    private Role getRoleByName(Session session, String roleName) throws HibernateException {
        List role = session.find("from role in " + Role.class + " where role.name = ?", roleName, Hibernate.STRING);
        if (role.size() > 0) {
            return (Role)role.get(0);
        }
        return null;
    }

}
