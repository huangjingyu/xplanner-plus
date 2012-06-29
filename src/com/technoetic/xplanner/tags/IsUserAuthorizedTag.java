package com.technoetic.xplanner.tags;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import net.sf.xplanner.domain.Project;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.util.RequestUtils;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.security.auth.AuthorizationHelper;
import com.technoetic.xplanner.tags.db.DatabaseTagSupport;

public class IsUserAuthorizedTag extends DatabaseTagSupport {
    private int projectId;
    private int principalId;
    private Object object;
    private String name;
    private String property;
    private String resourceType;
    private int resourceId;
    private String permission;
    private int allowedUser;
    private boolean negate;

    @Override
	protected int doStartTagInternal() throws Exception {
		boolean skipBody = true;
		if (allowedUser != 0 && allowedUser == SecurityHelper.getRemoteUserId((HttpServletRequest) pageContext.getRequest())) {
			skipBody = false;
		} else {
			Session session;
			int projectId = getProjectId();
			
			session = getSession();
			skipBody = AuthorizationHelper.hasPermission(projectId, principalId, resourceId, resourceType, permission, getResource(), pageContext.getRequest());
			if (skipBody == true && projectId == 0) {
				// Has permission for any...
				Collection projects = session.find("from project in " + Project.class);
				skipBody = AuthorizationHelper.hasPermissionToAny(new String[] { permission }, projects, pageContext.getRequest());
			}
		}
		return (negate ? !skipBody : skipBody) ? SKIP_BODY : EVAL_BODY_INCLUDE;
    }

   private Object getResource() throws JspException {
        Object resource = object;
        if (object instanceof String) {
            resource = pageContext.findAttribute((String)object);
        }
        if (resource == null && name != null) {
            resource = RequestUtils.lookup(pageContext, name, property, null);
        }
        if (resource == null) {
            resource = pageContext.findAttribute("project");
        }
        if (resource == null && resourceType == null) {
            throw new JspException("object or resource type/id must be specified");
        }
        return resource;
    }

    private int getProjectId() throws JspException {
        if (projectId != 0) {
            return projectId;
        }
        DomainContext context = DomainContext.get(pageContext.getRequest());
        if (context != null && context.getProjectId() != 0) {
            return context.getProjectId();
        }
        if (object instanceof Project) {
            return ((Project)object).getId();
        }
        String id = pageContext.getRequest().getParameter("projectId");
        if (!StringUtils.isEmpty(id)) {
            return Integer.parseInt(id);
        }
        Object resource = getResource();
        if (resource instanceof Project) {
            return ((Project)resource).getId();
        }
        return 0;
    }

    @Override
	public void release() {
        projectId = 0;
        principalId = 0;
        resourceType = null;
        resourceId = 0;
        permission = null;
        object = null;
        name = null;
        property = null;
        super.release();
    }

    public void setNegate(boolean negate) {
        this.negate = negate;
    }

    public boolean isNegate() {
        return negate;
    }

    public void setPrincipalId(int principalId) {
        this.principalId = principalId;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public void setAllowedUser(int allowedUser) {
        this.allowedUser = allowedUser;
    }
}