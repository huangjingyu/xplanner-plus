/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.security.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.xplanner.domain.Permission;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * User: mprokopowicz
 * Date: Mar 29, 2006
 * Time: 6:47:12 PM
 */
public class PrincipalSpecificPermissionHelper {
   private AuthorizerQueryHelper authorizerQueryHelper;

   public void setAuthorizerQueryHelper(AuthorizerQueryHelper authorizerQueryHelper) {
      this.authorizerQueryHelper = authorizerQueryHelper;
   }

   public Map<Integer,List<Permission>> getPermissionsForPrincipal(int principalId) {
      Map<Integer,List<Permission>> map = new HashMap<Integer,List<Permission>>();
      addProjectsPermissions(principalId, map);
      addPersonPermissions(principalId, map);
      return map;
   }

   public Collection getPermissionsSpecificToPerson(int principalId) {
      Collection personPermissionsCol = authorizerQueryHelper.getAllPermissionsToPerson();
      return CollectionUtils.select(personPermissionsCol, new PersonFilter(principalId) {
         protected boolean isEqual(Integer actualId) {
            return actualId.intValue() == 0 || actualId.intValue() == id;
         }
      });
   }

   public Collection getPermissionsBasedOnPersonRoles(int principalId) {
      return CollectionUtils.select(authorizerQueryHelper.getAllPermissions(),
                                    new PersonFilter(principalId));
   }

   void addPersonPermissions(
         int principalId,
         Map<Integer,List<Permission>> map) {
      List<List<Permission>> permissionsLists = new ArrayList<List<Permission>>(map.values());
      for (Iterator<List<Permission>> it = permissionsLists.iterator(); it.hasNext();) {
         List<Permission> list = new ArrayList<Permission>(it.next());
         for (Iterator<Permission> itPerm = list.iterator(); itPerm.hasNext();) {
            Permission p = itPerm.next();
            if (p.getResourceType().equals("%") || p.getResourceType().equals("system.person")) {
               Permission personPermission = new Permission("system.person",
                                                            p.getResourceId(),
                                                            p.getPrincipal(),
                                                            p.getName());
               addPermissionForProject(map, 0, personPermission);
            }
         }
      }
      List permissions = (List) getPermissionsSpecificToPerson(principalId);
      for (int i = 0; i < permissions.size(); i++) {
         Object permission = ((Object[]) permissions.get(i))[1];
         addPermissionForProject(map, 0, (Permission) permission);
      }
   }

   void addPermissionForProject(Map<Integer,List<Permission>> permissionMap, int projectId, Permission permission) {
      Integer projectKey = new Integer(projectId);
      List<Permission> permissions = permissionMap.get(projectKey);
      if (permissions == null) {
         permissions = new ArrayList<Permission>();
         permissionMap.put(projectKey, permissions);
      }
      permissions.add(permission);
   }

   private void addProjectsPermissions(
         int principalId,
         Map<Integer,List<Permission>> map) {

      List permissions = (List) getPermissionsBasedOnPersonRoles(principalId);
      for (int i = 0; i < permissions.size(); i++) {
         Object[] result = (Object[]) permissions.get(i);
         addPermissionForProject(map, ((Integer) result[1]).intValue(), (Permission) result[2]);
      }
   }

   protected class PersonFilter implements Predicate {
      int id;

      public PersonFilter(int id) {
         this.id = id;
      }

      public boolean evaluate(Object obj) {
         Object[] row = (Object[]) obj;
         final Integer actualId = (Integer) row[0];
         return isEqual(actualId);
      }

      protected boolean isEqual(Integer actualId) {return actualId.intValue() == id;}
   }
}
