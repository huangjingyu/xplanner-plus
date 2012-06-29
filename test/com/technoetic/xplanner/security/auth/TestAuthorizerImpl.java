package com.technoetic.xplanner.security.auth;

import static org.easymock.EasyMock.expect;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.xplanner.domain.Permission;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Role;

import com.technoetic.xplanner.AbstractUnitTestCase;
import com.technoetic.xplanner.XPlannerTestSupport;
import com.technoetic.xplanner.db.hibernate.ThreadSession;

public class TestAuthorizerImpl extends AbstractUnitTestCase {
   private final int PROJECT_ID = 11;
   static final int PRINCIPAL_ID = 0;
   private AuthorizerImpl authorizer;
   private AuthorizerQueryHelper mockAuthorizerQueryHelper;
   private PrincipalSpecificPermissionHelper mockPrincipalSpecificPermissionHelper;


   protected void setUp() throws Exception {
      super.setUp();
      support = new XPlannerTestSupport();
      support.mockPreparedStatement.setBoundVariableCount(2);
      authorizer = new AuthorizerImpl();
      mockAuthorizerQueryHelper = createLocalMock(AuthorizerQueryHelper.class);
      mockPrincipalSpecificPermissionHelper = createLocalMock(PrincipalSpecificPermissionHelper.class);
      authorizer.setPrincipalSpecificPermissionHelper(mockPrincipalSpecificPermissionHelper);
      authorizer.setAuthorizerQueryHelper(mockAuthorizerQueryHelper);
   }

   protected void tearDown() throws Exception {
      ThreadSession.set(null);
      super.tearDown();
   }

   public void testSimplePermissionMatch() throws Exception {
      final int resourceId = 1;
      Map<Integer, List<Permission>> results = new HashMap<Integer, List<Permission>>();
      results.put(new Integer(PRINCIPAL_ID),
                  Arrays.asList(new Permission[]{createPositivePermission("system", resourceId, "set")}));
      expect(
            mockPrincipalSpecificPermissionHelper.getPermissionsForPrincipal(PRINCIPAL_ID)).andReturn(results);
      replay();
      boolean result = authorizer.hasPermission(PROJECT_ID, PRINCIPAL_ID, "system", resourceId, "set");
      verify();
      assertTrue("wrong result", result);
   }

   public void testSimplePermissionNonmatch() throws Exception {
      Map<Integer,List<Permission>> results = new HashMap<Integer,List<Permission>>();
      final int resourceIdThePersonHasPermissionTo = 1;
      final int resourceId = 99;
      results.put(new Integer(PRINCIPAL_ID),
                  Arrays.asList(new Permission[]{createPositivePermission("system.project",
                                                                      resourceIdThePersonHasPermissionTo,
                                                                      "set")}));
      expect(
            mockPrincipalSpecificPermissionHelper.getPermissionsForPrincipal(PRINCIPAL_ID)).andReturn(results);
      replay();
      Project resource = createProject(resourceId);
      boolean result = authorizer.hasPermission(PROJECT_ID, PRINCIPAL_ID, resource, "set");
      verify();
      assertFalse("wrong result", result);
   }

   public void testNegativePermision() throws Exception {
      final int resourceId = 1;
      Map<Integer,List<Permission>> results = new HashMap<Integer,List<Permission>>();
      results.put(new Integer(PRINCIPAL_ID),
                  Arrays.asList(new Permission[]{createPositivePermission("system", resourceId, "%set"),
                                             createNegativePermission("system", resourceId, "set.value")}));
      expect(
            mockPrincipalSpecificPermissionHelper.getPermissionsForPrincipal(PRINCIPAL_ID)).andReturn(results);
      replay();
      boolean result = authorizer.hasPermission(PROJECT_ID, PRINCIPAL_ID, "system", resourceId, "set.value");
      verify();
      assertFalse("wrong result", result);

   }

   public void testGetRolesForPrincipalOnProject() throws Exception {
      final int projectId = 20;
      List<Object> results = Arrays.asList(new Object[]{new Role("viewer"), new Role("editor")});
      expect(mockAuthorizerQueryHelper.getRolesForPrincipalOnProject(PRINCIPAL_ID, projectId, false)).andReturn(results);
      replay();
      authorizer.getRolesForPrincipalOnProject(PRINCIPAL_ID, projectId, false);
      verify();
   }

   public void testHasPermissionForSomeProject() throws Exception {
      Map<Integer,List<Permission>> results = new HashMap<Integer,List<Permission>>();
      final int project1Id = 1;
      final int project2Id = 2;
      final int project3Id = 3;
      final int resourceId = 21;
      results.put(new Integer(project2Id),
                  Arrays.asList(new Permission[]{createPositivePermission("system", resourceId, "set")}));
      results.put(new Integer(project3Id),
                  Arrays.asList(new Permission[]{createPositivePermission("system", resourceId, "set")}));
      List<Object> projects =
            Arrays.asList(new Object[]{createProject(project1Id),
                                       createProject(project2Id),
                                       createProject(project3Id)});
      expect(mockAuthorizerQueryHelper.getAllUnhidenProjects()).andReturn(projects);
      expect(mockPrincipalSpecificPermissionHelper.getPermissionsForPrincipal(PRINCIPAL_ID)).andReturn(results).times(2);
      replay();
      boolean hasPermission = authorizer.hasPermissionForSomeProject(PRINCIPAL_ID, "system", resourceId, "set");
      verify();
      assertTrue(hasPermission);
   }

   public void testGetPeopleWithPermissionOnProject() throws Exception {
      final int person1Id = 1;
      final int person2Id = 2;
      final int project1Id = 11;
      final int project2Id = 21;
      List<Object> personList = Arrays.asList(new Object[]{createPerson(person1Id), createPerson(person2Id)});
      Map<Integer,List<Permission>> results1 = new HashMap<Integer,List<Permission>>();
      Map<Integer,List<Permission>> results2 = new HashMap<Integer,List<Permission>>();
      results1.put(new Integer(project1Id),
                   Arrays.asList(new Permission[]{createPositivePermission("system.project", project1Id, "edit")}));
      results2.put(new Integer(project2Id),
                   Arrays.asList(new Permission[]{createPositivePermission("system.project", project2Id, "edit")}));
      expect(
            mockPrincipalSpecificPermissionHelper.getPermissionsForPrincipal(person1Id)).andReturn(results1);
      expect(
            mockPrincipalSpecificPermissionHelper.getPermissionsForPrincipal(person2Id)).andReturn(results2);
      replay();
      Collection peopleWithPermissionOnProject = authorizer.getPeopleWithPermissionOnProject(personList, project2Id);
      verify();
      assertEquals(1, peopleWithPermissionOnProject.size());
      assertEquals(person2Id, ((Person) peopleWithPermissionOnProject.iterator().next()).getId());
   }

   private Project createProject(int project1Id) {
      Project project = new Project();
      project.setId(project1Id);
      return project;
   }

   private Person createPerson(int id) {
      Person person1 = new Person();
      person1.setId(id);
      return person1;
   }


   private Permission createPositivePermission(String resourceType, int resourceId, String permissionName) {
      return new Permission(resourceType, resourceId, PRINCIPAL_ID, permissionName);
   }

   private Permission createNegativePermission(String resourceType, int resourceId, String permissionName) {
      final Permission permission = createPositivePermission(resourceType, resourceId, permissionName);
      permission.setPositive(false);
      return permission;
   }
}
