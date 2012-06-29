package com.technoetic.xplanner.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.SecurityHelper;

public class IsUserInRoleTag extends TagSupport {
    private String role;
    private String adminRole;
    private boolean negate;
    private String userid;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setNegate(boolean negate) {
        this.negate = negate;
    }

    public boolean isNegate() {
        return negate;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserid() {
        return userid;
    }

    public String getAdminRole() {
        return adminRole;
    }

    public void setAdminRole(String adminRole) {
        this.adminRole = adminRole;
    }

    public int doStartTag() throws JspException {
        boolean skipBody = true;
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        if (adminRole != null && SecurityHelper.isUserInRole(request, adminRole)) {
            skipBody = false;
        } else {
            String[] roles = role.split(",");
            for (int i = 0; i < roles.length; i++) {
                String role = roles[i];
                if (SecurityHelper.isUserInRole(request, role)) {
                    skipBody = false;
                    break;
                }
            }
            try {
                if (userid != null && !SecurityHelper.getUserPrincipal(request).getName().equals(userid)) {
                    skipBody = true;
                }
            } catch (AuthenticationException e) {
                throw new JspException(e);
            }
        }
        return (negate ? !skipBody : skipBody) ? SKIP_BODY : EVAL_BODY_INCLUDE;
    }
}