/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.security.auth;

import java.util.Collection;
import java.util.List;

import net.sf.xplanner.domain.Project;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * User: mprokopowicz
 * Date: Mar 29, 2006
 * Time: 5:36:07 PM
 */
public class AuthorizerQueryHelper extends HibernateDaoSupport {

public Collection getAllPermissions() {
      Collection permission = getHibernateTemplate().findByNamedQuery("security.personal.permissions");
      permission.addAll(getHibernateTemplate().findByNamedQuery("security.role.permissions"));
      return permission;
   }

   public Collection getAllPermissionsToPerson() {
      return getHibernateTemplate().findByNamedQuery("security.person.permissions");
   }

   public Collection getRolesForPrincipalOnProject(int principalId, int projectId, boolean includeWildcardProject) {
      return getHibernateTemplate().findByNamedQueryAndNamedParam("security.roles",
                                                                  new String[]{"personId",
                                                                               "projectId",
                                                                               "includeWildcardProject"},
                                                                  new Object[]{new Integer(principalId),
                                                                               new Integer(projectId),
                                                                               new Integer(includeWildcardProject ?
                                                                                           1 :
                                                                                           0)});
   }

   public List getAllUnhidenProjects() {
      return getHibernateTemplate().find("from project in " + Project.class + " where project.hidden = false");
   }
}
