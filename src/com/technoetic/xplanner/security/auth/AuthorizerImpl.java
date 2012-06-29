package com.technoetic.xplanner.security.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Note;
import net.sf.xplanner.domain.Permission;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Setting;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.TimeEntry;
import net.sf.xplanner.domain.UserStory;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.dao.DataAccessException;

import com.technoetic.xplanner.domain.Feature;
import com.technoetic.xplanner.domain.Integration;
import com.technoetic.xplanner.forms.FeatureEditorForm;
import com.technoetic.xplanner.forms.IterationEditorForm;
import com.technoetic.xplanner.forms.PersonEditorForm;
import com.technoetic.xplanner.forms.ProjectEditorForm;
import com.technoetic.xplanner.forms.TaskEditorForm;
import com.technoetic.xplanner.forms.UserStoryEditorForm;
import com.technoetic.xplanner.security.AuthenticationException;


public class AuthorizerImpl implements Authorizer {
   private final Map<Class,String> resourceTypes = new HashMap<Class,String>();
   public static final Integer ANY_PROJECT = new Integer(0);
   private AuthorizerQueryHelper authorizerQueryHelper;
   private PrincipalSpecificPermissionHelper principalSpecificPermissionHelper;

   public AuthorizerImpl() {
      //TODO: Extract these constants
      //DEBT(METADATA) Move these to the DomainMetaDataRepository
      resourceTypes.put(Project.class, "system.project");
      resourceTypes.put(Iteration.class, "system.project.iteration");
      resourceTypes.put(UserStory.class, "system.project.iteration.story");
      resourceTypes.put(Task.class, "system.project.iteration.story.task");
      resourceTypes.put(Feature.class, "system.project.iteration.story.feature");
      resourceTypes.put(TimeEntry.class, "system.project.iteration.story.task.time_entry");
      resourceTypes.put(Integration.class, "system.project.integration");
      resourceTypes.put(Person.class, "system.person");
      resourceTypes.put(Note.class, "system.note");
      resourceTypes.put(ProjectEditorForm.class, "system.project");
      resourceTypes.put(IterationEditorForm.class, "system.project.iteration");
      resourceTypes.put(UserStoryEditorForm.class, "system.project.iteration.story");
      resourceTypes.put(TaskEditorForm.class, "system.project.iteration.story.task");
      resourceTypes.put(FeatureEditorForm.class, "system.project.iteration.story.feature");
      resourceTypes.put(PersonEditorForm.class, "system.person");
      resourceTypes.put(Setting.class, "system.setting");
   }

   //TODO resource should be a DomainObject
   public boolean hasPermission(int projectId, int personId, Object resource, String permission)
         throws AuthenticationException {
      int id;
      try {
         id = ((Integer) PropertyUtils.getProperty(resource, "id")).intValue();
      } catch (Exception e) {
         throw new AuthenticationException(e);
      }
      return hasPermission(projectId, personId, getTypeOfResource(resource), id, permission);
   }


   public boolean hasPermission(int projectId,
                                int personId,
                                String resourceType,
                                int resourceId,
                                String permissionName) throws AuthenticationException {
      try {
         Map permissionsByProjectMap = principalSpecificPermissionHelper.getPermissionsForPrincipal(personId);
         return permissionMatches(permissionName,
                                  resourceType,
                                  resourceId,
                                  (List) permissionsByProjectMap.get(new Integer(projectId)))
                ||
                // For 0.7 only sysadmins have any project permissions
                permissionMatches(permissionName,
                                  resourceType,
                                  resourceId,
                                  (List) permissionsByProjectMap.get(ANY_PROJECT));
      } catch (Exception e) {
         throw new AuthenticationException(e);
      }
   }


   public Collection getPeopleWithPermissionOnProject(List allPeople, int projectId) throws AuthenticationException {
      Collection people = new ArrayList();
      for (int i = 0; i < allPeople.size(); i++) {
         Person person = (Person) allPeople.get(i);
         if (hasPermission(projectId, person.getId(), "system.project", projectId, "edit")) {
            people.add(person);
         }
      }
      return people;
   }

   public Collection getRolesForPrincipalOnProject(int principalId, int projectId, boolean includeWildcardProject)
         throws AuthenticationException {
      try {
         return authorizerQueryHelper.getRolesForPrincipalOnProject(principalId, projectId, includeWildcardProject);
      } catch (DataAccessException e) {
         throw new AuthenticationException(e);
      }
   }

   public boolean hasPermissionForSomeProject(int personId,
                                              String resourceType, int resourceId, String permission)
         throws AuthenticationException {
      try {
         List projects =
               authorizerQueryHelper.getAllUnhidenProjects();
         return hasPermissionForSomeProject(projects, personId, resourceType, resourceId, permission);
      } catch (AuthenticationException e) {
         throw e;
      } catch (Exception e) {
         throw new AuthenticationException(e);
      }
   }

   public boolean hasPermissionForSomeProject(Collection projects,
                                              int personId,
                                              String resourceType,
                                              int resourceId,
                                              String permission)
         throws AuthenticationException {
      for (Iterator iterator = projects.iterator(); iterator.hasNext();) {
         Project project = (Project) iterator.next();
         if (hasPermission(project.getId(), personId, resourceType, resourceId, permission)) {
            return true;
         }
      }
      return false;
   }

   public String getTypeOfResource(Object resource) {
	   String keyClass = resource.getClass().getName();
	   for (Object clazz : resourceTypes.keySet()) {
		   	if (keyClass.startsWith(((Class) clazz).getName())) {
	   			return (String) resourceTypes.get(clazz);
	   		}
   		}
	   return null;
   }

   public void setPrincipalSpecificPermissionHelper(PrincipalSpecificPermissionHelper principalSpecificPermissionHelper) {
      this.principalSpecificPermissionHelper = principalSpecificPermissionHelper;
   }

   public void setAuthorizerQueryHelper(AuthorizerQueryHelper authorizerQueryHelper) {
      this.authorizerQueryHelper = authorizerQueryHelper;
   }

   private boolean isMatching(String pattern, String string) {
      return pattern.endsWith("%")
             ? string.startsWith(pattern.substring(0, pattern.length() - 1))
             : string.equals(pattern);
   }


   private boolean permissionMatches(String permissionName,
                                     String resourceType,
                                     int resourceId,
                                     List permissionsForProject) {
      boolean hasNegativePermission = false;
      boolean hasPositivePermission = false;
      if (permissionsForProject != null) {
         for (int i = 0; i < permissionsForProject.size(); i++) {
            Permission permission = (Permission) permissionsForProject.get(i);
            if (isMatching(permission.getResourceType(), resourceType) &&
                (permission.getResourceId() == 0 || permission.getResourceId() == resourceId)) {
               if (isMatching(permission.getName(), permissionName)) {
                  if (permission.isPositive()) {
                     hasPositivePermission = true;
                  } else {
                     hasNegativePermission = true;
                  }
               }
            }
         }
      }
      return hasPositivePermission && !hasNegativePermission;
   }
}
