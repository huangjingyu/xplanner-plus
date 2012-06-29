package com.technoetic.xplanner.tags;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.hibernate.classic.Session;

import net.sf.xplanner.domain.Person;

import com.technoetic.xplanner.db.hibernate.HibernateHelper;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.tags.db.DatabaseTagSupport;

public class AuthenticatedUserTag extends DatabaseTagSupport {
	 
	protected int doStartTagInternal() throws Exception {
		if (SecurityHelper.isUserAuthenticated((HttpServletRequest) pageContext.getRequest())) {
			Principal userPrincipal = SecurityHelper.getUserPrincipal((HttpServletRequest) pageContext.getRequest());
			// DEBT(DAO) : Move this to a dao
			if (getSession() != null) {
				List users = getSession().createQuery("from p in " + Person.class + " where p.userId = :userId")
						.setString("userId", userPrincipal.getName()).setCacheable(true).list();
				if (users.size() > 0) {
					pageContext.setAttribute(id, users.get(0));
				}
			}
		}
		return SKIP_BODY;
	}

	@Override
	public void doCatch(Throwable throwable) throws Throwable {
		pageContext.getServletContext().log("error getting authenticated user",	throwable);
		throw new JspException(throwable.getMessage());
	}

   public void release() {
      super.release();
   }
}