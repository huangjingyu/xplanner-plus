/*
 * Copyright (c) Mateusz Prokopowicz. All Rights Reserved.
 */

package com.technoetic.xplanner.actions;

import java.util.Iterator;
import java.util.Map;

import net.sf.xplanner.domain.Person;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.technoetic.xplanner.domain.repository.RepositoryException;
import com.technoetic.xplanner.domain.repository.RoleAssociationRepository;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.LoginModule;
import com.technoetic.xplanner.security.auth.Authorizer;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;

/**
 * User: Mateusz Prokopowicz
 * Date: Dec 9, 2004
 * Time: 11:03:47 AM
 */
public class EditPersonHelper {
   private static Logger log = Logger.getLogger(EditPersonHelper.class);
   public static final String SYSADMIN_ROLE_NAME = "sysadmin";
   private static final int ANY_PROJECT_ID = 0;
   private RoleAssociationRepository roleAssociationRepository;
   private Authorizer authorizer;

   public void setAuthorizer(Authorizer authorizer) {
      Authorizer systemAuthorizer = SystemAuthorizer.get();
      if (authorizer != systemAuthorizer) {
         log.warn("Which authorizer do you want me to use? " +
                  authorizer +
                  " or what SystemAuthorizer has, " +
                  systemAuthorizer +
                  "?????");
      }

      this.authorizer = authorizer;
   }

   public void setRoleAssociationRepository(RoleAssociationRepository roleAssociationRepository) {
      this.roleAssociationRepository = roleAssociationRepository;
   }

  public void modifyRoles(Map<String, String> projectRoleMap,
                           Person person,
                           boolean isSystemAdmin,
                           int remoteUserId)
         throws AuthenticationException, RepositoryException {
      for (Iterator<String> iterator = projectRoleMap.keySet().iterator(); iterator.hasNext();) {
         String projectId = iterator.next();
         String role = projectRoleMap.get(projectId);
         if (isCurrentUserAdminOfProject(Integer.parseInt(projectId), remoteUserId)) {
            setRoleOnProject(Integer.parseInt(projectId), person, role);
         }
      }
      if (isCurrentUserAdminOfProject(ANY_PROJECT_ID, remoteUserId)) {
         roleAssociationRepository.deleteForPersonOnProject(SYSADMIN_ROLE_NAME,
                                                            person.getId(),
                                                            ANY_PROJECT_ID);
         if (isSystemAdmin) {
            setSysadmin(person);
         }
      }
   }

   void setSysadmin(Person person) throws RepositoryException {
      addRoleAssociationForProject(ANY_PROJECT_ID, person.getId(), SYSADMIN_ROLE_NAME);
   }

   public void setRoleOnProject(int projectId, Person person, String role) throws RepositoryException {
      deleteRoleAssociationsForProject(projectId, person.getId());
      addRoleAssociationForProject(projectId, person.getId(), role);
   }

   private boolean isCurrentUserAdminOfProject(int projectId, int remoteUserId)
         throws AuthenticationException {
      return authorizer.hasPermission(projectId, remoteUserId,
                                      "system.project", projectId, "admin.edit.role");
   }

   private void addRoleAssociationForProject(int projectId, int personId, String roleName)
         throws RepositoryException {
      roleAssociationRepository.insertForPersonOnProject(roleName, personId, projectId);
   }

   private void deleteRoleAssociationsForProject(int projectId, int personId)
         throws RepositoryException {
      roleAssociationRepository.deleteAllForPersonOnProject(personId, projectId);
   }

   public void changeUserPassword(String newPassword, String userId, LoginModule loginModule)
         throws AuthenticationException {
      if (StringUtils.isNotEmpty(newPassword)) {
         if (loginModule != null) {
            loginModule.changePassword(userId, newPassword);
         }
      }
   }

}
