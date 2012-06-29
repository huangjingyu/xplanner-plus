package com.technoetic.xplanner.actions;

import static org.easymock.EasyMock.expect;

import java.util.HashMap;
import java.util.Map;

import net.sf.xplanner.domain.Person;

import com.technoetic.xplanner.AbstractUnitTestCase;
import com.technoetic.xplanner.XPlannerTestSupport;
import com.technoetic.xplanner.domain.repository.RoleAssociationRepository;
import com.technoetic.xplanner.domain.repository.RoleRepository;
import com.technoetic.xplanner.security.auth.Authorizer;

/**
 * User: Mateusz Prokopowicz
 * Date: Dec 9, 2004
 * Time: 1:37:26 PM
 */
public class TestEditPersonHelper extends AbstractUnitTestCase {
   private int PERSON_ID = XPlannerTestSupport.DEFAULT_PERSON_ID;
   private String PROJECT_ID_STRING = "22";
   private int PROJECT_ID = 22;
   private EditPersonHelper editPersonHelper;
   private RoleRepository mockRoleRepository;
   private RoleAssociationRepository mockRoleAssocationRepository;
   private Authorizer mockAuthorizer;
   private Person mockPerson;

   public void setUp() throws Exception {
      super.setUp();
      mockPerson = new Person("mock");
      mockPerson.setId(PERSON_ID);
      editPersonHelper = new EditPersonHelper();
      mockRoleRepository = createLocalMock(RoleRepository.class);
      mockRoleAssocationRepository = createLocalMock(RoleAssociationRepository.class);
      mockAuthorizer = createLocalMock(Authorizer.class);
      editPersonHelper.setAuthorizer(mockAuthorizer);
      editPersonHelper.setRoleAssociationRepository(mockRoleAssocationRepository);
   }


   public void testModifyRoles_WhenAuthorizedAndSysadmin() throws Exception {
      mockRoleAssocationRepository.deleteAllForPersonOnProject(PERSON_ID, PROJECT_ID);
      mockRoleAssocationRepository.insertForPersonOnProject("role", PERSON_ID, PROJECT_ID);
      mockRoleAssocationRepository.deleteForPersonOnProject("sysadmin", PERSON_ID, 0);
      mockRoleAssocationRepository.insertForPersonOnProject("sysadmin", PERSON_ID, 0);
      expect(mockAuthorizer.hasPermission(PROJECT_ID,
                                   PERSON_ID,
                                   "system.project",
                                   PROJECT_ID,
                                   "admin.edit.role")).andReturn(true);
      expect(mockAuthorizer.hasPermission(0, PERSON_ID, "system.project", 0, "admin.edit.role")).andReturn(true);
      replay();
      Map projectRoleMap = new HashMap();
      projectRoleMap.put("" + PROJECT_ID_STRING, "role");
      boolean isSystemAdmin = true;
      editPersonHelper.modifyRoles(projectRoleMap, mockPerson, isSystemAdmin, PERSON_ID);
      verify();
   }

   public void testModifyRoles_WhenNotAuthorizedAndNotSysadmin() throws Exception {
      mockRoleAssocationRepository.deleteAllForPersonOnProject(PERSON_ID, PROJECT_ID);
      mockRoleAssocationRepository.insertForPersonOnProject("role", PERSON_ID, PROJECT_ID);
      expect(mockAuthorizer.hasPermission(PROJECT_ID,
                                   PERSON_ID, "system.project", PROJECT_ID, "admin.edit.role")).andReturn(true);
      expect(mockAuthorizer.hasPermission(0,
                                   PERSON_ID, "system.project", 0, "admin.edit.role")).andReturn(false);
      replay();
      Map projectRoleMap = new HashMap();
      projectRoleMap.put("" + PROJECT_ID_STRING, "role");
      boolean isSystemAdmin = false;
      editPersonHelper.modifyRoles(projectRoleMap, mockPerson, isSystemAdmin, PERSON_ID);
      verify();
   }

   public void testModifyRoles_WhenNotAuthorizedAndSysadmin() throws Exception {
      expect(mockAuthorizer.hasPermission(PROJECT_ID,
                                                                         PERSON_ID,
                                                                         "system.project",
                                                                         PROJECT_ID,
                                                                         "admin.edit.role")).andReturn(false);
      expect(mockAuthorizer.hasPermission(0,
                                                                         PERSON_ID,
                                                                         "system.project",
                                                                         0,
                                                                         "admin.edit.role")).andReturn(true);
      mockRoleAssocationRepository.deleteForPersonOnProject("sysadmin", PERSON_ID, 0);
      mockRoleAssocationRepository.insertForPersonOnProject("sysadmin", PERSON_ID, 0);
      replay();
      Map projectRoleMap = new HashMap();
      projectRoleMap.put("" + PROJECT_ID_STRING, "role");
      boolean isSystemAdmin = true;
      editPersonHelper.modifyRoles(projectRoleMap, mockPerson, isSystemAdmin, PERSON_ID);
      verify();
   }

   public void testModifyRoles_WhenAuthorizedAndNotSysadmin() throws Exception {
      mockRoleAssocationRepository.deleteAllForPersonOnProject(PERSON_ID, PROJECT_ID);
		mockRoleAssocationRepository.insertForPersonOnProject("role",
				PERSON_ID, PROJECT_ID);
		expect(
				mockAuthorizer.hasPermission(PROJECT_ID, PERSON_ID,
						"system.project", PROJECT_ID, "admin.edit.role"))
				.andReturn(true);
		expect(
				mockAuthorizer.hasPermission(0, PERSON_ID, "system.project", 0,
						"admin.edit.role")).andReturn(false);
		replay();
      Map projectRoleMap = new HashMap();
      projectRoleMap.put("" + PROJECT_ID_STRING, "role");
      boolean isSystemAdmin = false;
      editPersonHelper.modifyRoles(projectRoleMap, mockPerson, isSystemAdmin, PERSON_ID);
      //verify();
   }

}
