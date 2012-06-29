package com.technoetic.xplanner.security.auth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import net.sf.xplanner.domain.Permission;

import org.easymock.MockControl;
import org.easymock.classextension.MockClassControl;

/**
 * User: Mateusz Prokopowicz
 * Date: Sep 6, 2005
 * Time: 1:50:49 PM
 */
public class TestPrincipalSpecificPermissionHelper extends TestCase {
   static final int PRINCIPAL_ID = 1;
   PrincipalSpecificPermissionHelper principalSpecificPermissionHelper;
   private AuthorizerQueryHelper mockAuthorizerQueryHelper;
   private MockControl mockAuthorizerQueryHelperControl;

   private Permission permission2;
   private Permission permission1;
   private List allPermissions;
   private Permission permission3;

   protected void setUp() throws Exception {
      super.setUp();
      mockAuthorizerQueryHelperControl = MockClassControl.createControl(AuthorizerQueryHelper.class);
      mockAuthorizerQueryHelper = (AuthorizerQueryHelper) mockAuthorizerQueryHelperControl.getMock();
      principalSpecificPermissionHelper = new PrincipalSpecificPermissionHelper();
      principalSpecificPermissionHelper.setAuthorizerQueryHelper(mockAuthorizerQueryHelper);
      permission1 = new Permission("system.person", 1, 1, "edit%");
      permission2 = new Permission("system.person", 0, 2, "read%");
      permission3 = new Permission("system.person", 0, PRINCIPAL_ID, "read%");
      allPermissions = new ArrayList();
      allPermissions.add(new Object[]{new Integer(1), permission1});
      allPermissions.add(new Object[]{new Integer(2), permission2});
      allPermissions.add(new Object[]{new Integer(1), permission3});


   }

   public void testGetPermissionsBasedOnPersonRoles() throws Exception {
      mockAuthorizerQueryHelperControl.expectAndReturn(mockAuthorizerQueryHelper.getAllPermissions(),
                                                       allPermissions);
      mockAuthorizerQueryHelperControl.replay();
      Collection permissions = principalSpecificPermissionHelper.getPermissionsBasedOnPersonRoles(PRINCIPAL_ID);
      assertFalse("permission list is empty", permissions.isEmpty());
      Permission actualPermission = (Permission) ((Object[]) permissions.iterator().next())[1];
      assertEquals("permission not found", permission1, actualPermission);
      mockAuthorizerQueryHelperControl.verify();
   }

   public void testGetPermissionsSpecificToPerson() throws Exception {
      mockAuthorizerQueryHelperControl.expectAndReturn(mockAuthorizerQueryHelper.getAllPermissionsToPerson(),
                                                       allPermissions);
      mockAuthorizerQueryHelperControl.replay();
      Collection permissions = principalSpecificPermissionHelper.getPermissionsSpecificToPerson(PRINCIPAL_ID);
      assertFalse("permission list is empty", permissions.isEmpty());
      Iterator permissionIterator = permissions.iterator();
      Permission actualPermission = (Permission) ((Object[]) permissionIterator.next())[PRINCIPAL_ID];
      assertEquals("permission not found", permission1, actualPermission);
      actualPermission = (Permission) ((Object[]) permissionIterator.next())[1];
      assertEquals("permission not found", permission3, actualPermission);
   }

   public void testAddPersonPermissions() throws Exception {
      Map<Integer,List<Permission>> projectSpecificPersonPermissionMap = new HashMap<Integer,List<Permission>>();
      projectSpecificPersonPermissionMap.put(new Integer(1), Arrays.asList(new Permission[]{permission1}));
      projectSpecificPersonPermissionMap.put(new Integer(2), Arrays.asList(new Permission[]{permission2}));
      List personSpecificPersonPermissionList = new ArrayList();
      personSpecificPersonPermissionList.add(new Object[]{new Integer(PRINCIPAL_ID), permission3});
      mockAuthorizerQueryHelperControl.expectAndReturn(mockAuthorizerQueryHelper.getAllPermissionsToPerson(),
                                                       personSpecificPersonPermissionList);
      mockAuthorizerQueryHelperControl.replay();
      principalSpecificPermissionHelper.addPersonPermissions(PRINCIPAL_ID, projectSpecificPersonPermissionMap);
      mockAuthorizerQueryHelperControl.verify();
      List permissionForAllProjectList = (List) projectSpecificPersonPermissionMap.get(new Integer(0));
      assertEquals(3, permissionForAllProjectList.size());
      assertTrue(permissionForAllProjectList.contains(permission1));
      assertTrue(permissionForAllProjectList.contains(permission2));
      assertTrue(permissionForAllProjectList.contains(permission3));
   }

   public void testAddPermissionForProject() throws Exception {
      Map permissionMap = new HashMap();
      int projectId = 10;
      Permission permission = new Permission("project.edit", 45, PRINCIPAL_ID, "edit%");
      principalSpecificPermissionHelper.addPermissionForProject(permissionMap, projectId, permission);
      assertFalse(permissionMap.isEmpty());
      List projectPermission = (List) permissionMap.get(new Integer(projectId));
      assertEquals(permission, projectPermission.get(0));
   }
}
