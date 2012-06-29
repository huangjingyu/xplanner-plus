package com.technoetic.xplanner.security.auth;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.xplanner.domain.DomainObject;

import com.technoetic.xplanner.security.AuthenticationException;

public class MockAuthorizer implements Authorizer {
   public int hasPermissionCallCount;
   public int getPeopleForPrincipalOnProjectCount = 0;
   public int hasPermissionPersonId;
   public String hasPermissionResourceType;
   public int hasPermissionResourceId;
   public String hasPermissionPermission;
   public Boolean hasPermissionReturn;
   public Boolean[] hasPermissionReturns;

   public boolean hasPermission(int projectId, int personId, String resourceType,
                                int resourceId, String permission) throws AuthenticationException {
      if (mapPersonToResources != null) {
         Map map = (Map) mapPersonToResources.get(new Integer(personId));
         if (map == null) {
            return false;
         }
         String actualPermission = (String) map.get(new Integer(resourceId));
         return permission.equalsIgnoreCase(actualPermission);
      }

      hasPermissionCallCount++;
      hasPermissionPersonId = personId;
      hasPermissionResourceType = resourceType;
      hasPermissionResourceId = resourceId;
      hasPermissionPermission = permission;
      return hasPermissionReturns != null
             ? (hasPermissionReturns[hasPermissionCallCount - 1]).booleanValue()
             : hasPermissionReturn.booleanValue();
   }

   public int hasPermission2Count;
   public int hasPermission2ProjectId;
   public int hasPermission2PersonId;
   public Object hasPermission2DomainObject;
   public String hasPermission2Permission;
   public Boolean hasPermission2Return;
   public Boolean[] hasPermission2Returns;

   public boolean hasPermission(int projectId, int personId, Object resource,
                                String permission) throws AuthenticationException {
      if (mapPersonToResources != null) {
         // TODO delegate to the other implementation even if the map is null
         return hasPermission(projectId, personId, "", ((DomainObject) resource).getId(), permission);
      }
      hasPermission2Count++;
      hasPermission2ProjectId = projectId;
      hasPermission2PersonId = personId;
      hasPermission2Permission = permission;
      hasPermission2DomainObject = resource;
      return hasPermission2Returns != null
             ? (hasPermission2Returns[hasPermission2Count - 1]).booleanValue()
             : hasPermission2Return.booleanValue();
   }

   public Collection getRolesForPrincipalOnProject(int principalId, int projectId, boolean wildcard)
         throws AuthenticationException {
      throw new UnsupportedOperationException();
   }

   public boolean invalidateCacheCalled;
   public int invalidateCachePrincipalId;

   public void logout(int principalId) {
      invalidateCacheCalled = true;
      invalidateCachePrincipalId = principalId;
   }

   public void invalidateCache() {
      invalidateCacheCalled = true;
      invalidateCachePrincipalId = -1;
   }

   public boolean hasPermissionForSomeProject(int personlId,
                                              String resourceType, int resourceId, String permissions)
         throws AuthenticationException {
      throw new UnsupportedOperationException();
   }

   public boolean hasPermissionForSomeProject(Collection projects,
                                              int personId,
                                              String resourceType,
                                              int resourceId,
                                              String permission) throws AuthenticationException {
      throw new UnsupportedOperationException();
   }

   public Collection getPeopleForPrincipalOnProjectReturn;

   public Collection getPeopleWithPermissionOnProject(List allPeople, int projectId) throws AuthenticationException {
      getPeopleForPrincipalOnProjectCount++;
      return getPeopleForPrincipalOnProjectReturn;
   }

   HashMap mapPersonToResources = null;

   public void givePermission(int personId, DomainObject resource, String permission) {
      Integer personKey = new Integer(personId);
      if (mapPersonToResources == null) {
         mapPersonToResources = new HashMap();
      }
      Map map = (Map) mapPersonToResources.get(personKey);
      if (map == null) {
         map = new HashMap();
         mapPersonToResources.put(personKey, map);
      }
      map.put(new Integer(resource.getId()), permission);
   }
}
