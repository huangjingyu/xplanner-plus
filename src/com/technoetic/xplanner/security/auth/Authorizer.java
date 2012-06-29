package com.technoetic.xplanner.security.auth;

import java.util.Collection;
import java.util.List;

import com.technoetic.xplanner.security.AuthenticationException;

//DEBT Should throw AuthorizationException not AuthenticationException

public interface Authorizer {
   boolean hasPermission(int projectId, int personId, String resourceType,
                         int resourceId, String permission) throws AuthenticationException;

   boolean hasPermission(int projectId, int personId,
                         Object resource, String permission) throws AuthenticationException;

   boolean hasPermissionForSomeProject(int personlId,
                                       String resourceType, int resourceId, String permissions)
         throws AuthenticationException;

   boolean hasPermissionForSomeProject(Collection projects,
                                       int personId,
                                       String resourceType,
                                       int resourceId,
                                       String permission)
         throws AuthenticationException;

   Collection getPeopleWithPermissionOnProject(List allPeople, int projectId) throws AuthenticationException;

   Collection getRolesForPrincipalOnProject(int principalId, int projectId, boolean includeWildcardProject)
         throws AuthenticationException;
}
