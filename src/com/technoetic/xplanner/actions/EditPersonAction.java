package com.technoetic.xplanner.actions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.dao.impl.CommonDao;
import net.sf.xplanner.domain.Person;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.technoetic.xplanner.forms.PersonEditorForm;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.AuthenticatorImpl;
import com.technoetic.xplanner.security.SecurityHelper;

public class EditPersonAction extends EditObjectAction<Person> {
	private EditPersonHelper editPersonHelper;

	public void setEditPersonHelper(EditPersonHelper editPersonHelper) {
		this.editPersonHelper = editPersonHelper;
	}

	@Override
	protected void beforeObjectCommit(Person object, ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse reply) throws Exception {
		PersonEditorForm personForm = (PersonEditorForm) actionForm;
		Person person = (Person) object;
		Map<String, String> projectRoleMap = getProjectRoleMap(personForm.getProjectIds(), personForm.getProjectRoles());
		editPersonHelper.modifyRoles(projectRoleMap, person, personForm.isSystemAdmin(),
				SecurityHelper.getRemoteUserId(request));
	}

	@Override
	protected void afterObjectCommit(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse reply) throws ServletException {
		PersonEditorForm personForm = (PersonEditorForm) actionForm;
		try {
			editPersonHelper.changeUserPassword(personForm.getNewPassword(), personForm.getUserId(),
					AuthenticatorImpl.getLoginModule(request));
		} catch (AuthenticationException e) {
			throw new ServletException(e);
		}
	}

	private Map<String, String> getProjectRoleMap(List<String> projectIds, List<String> projectRoles) {
		Map<String, String> projectRoleMap = new HashMap<String, String>();
		for (int i = 0; i < projectIds.size() && i < projectRoles.size(); i++) {
			projectRoleMap.put(projectIds.get(i), projectRoles.get(i));
		}
		return projectRoleMap;
	}

}
