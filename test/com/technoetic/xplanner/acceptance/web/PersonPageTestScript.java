package com.technoetic.xplanner.acceptance.web;

import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;

public class PersonPageTestScript extends AbstractPageTestScript {
   private Project project;

   protected void setUp() throws Exception {
      super.setUp();
//      newProject(); // Need at least one project in order to get the edit person link to show up.
      setUpTestProject();
      Person person = newPerson();
      person.setUserId(guestUserId);
      person.setName(guestName);
      person.setInitials(guestInitials);
      person.setEmail("test@example.com");
      person.setPhone("555-1212");
      commitCloseAndOpenSession();
      tester.login();
      tester.clickLinkWithKey("navigation.people");
      tester.clickLinkWithText(guestName);
   }

   protected void tearDown() throws Exception {
      super.tearDown();
   }

   public void testPersonPage() {
      // Just check that the page shows up
      tester.assertTextNotPresent("error");
   }

   public void testReportExport() throws Exception {
      checkExportUri("person", "jrpdf");
   }

   public void testEditLink() throws Exception {
      tester.assertLinkPresentWithKey("action.edit.person");
      tester.clickLinkWithKey("action.edit.person");
   }

   public void testPeopleLink() throws Exception {
      tester.assertLinkPresentWithKey("projects.link.people");
      tester.clickLinkWithKey("projects.link.people");
   }

   public void testTimesheetLink() throws Exception {
      tester.assertLinkPresentWithKey("person.link.timesheet");
      tester.clickLinkWithKey("person.link.timesheet");
   }

   protected void traverseLinkWithKeyAndReturn(String key) throws Exception {
      tester.clickLinkWithKey(key);
      tester.gotoProjectsPage();
   }

   public void testEditMyProfile() throws Exception {
      tester.clickLinkWithKey("logout");
      tester.login(guestUserId, "test");
      tester.assertOnTopPage();
      tester.clickLinkWithKey("navigation.me");
      tester.clickLinkWithKey("action.edit.person");
      personTester.assertOnPersonPage(guestUserId);
      tester.assertKeyNotPresent("security.notauthorized");
   }

   public void testProjectRoles() throws Exception {
      tester.gotoProjectsPage();
      setUpTestProject();

      tester.clickLinkWithKey("navigation.people");
      tester.clickEditLinkInRowWithText(guestName);
      tester.assertKeyPresent("person.editor.edit_prefix");
      tester.assertOptionEquals("projectRole[0]", "None");
      tester.assertFormElementNotPresentWithLabel("Editor");
   }

   public void testOnlySysAdminCanAddUser() throws Exception {
      assertUserOfRoleCannotAddUser("admin");
      assertUserOfRoleCannotAddUser("editor");
      assertUserOfRoleCannotAddUser("viewer");
   }

   public void testRequiredFields() throws Exception {
      tester.clickLinkWithKey("action.edit.person");
      tester.setFormElement("userIdentifier", "");
      tester.setFormElement("email", "");
      tester.setFormElement("name", "");
      tester.setFormElement("initials", "");
      tester.submit();
      tester.assertKeyPresent("person.editor.missing_name");
      tester.assertKeyPresent("person.editor.missing_initials");
      tester.assertKeyPresent("person.editor.missing_user_id");
      tester.assertKeyPresent("person.editor.missing_email");
   }

   private void assertUserOfRoleCannotAddUser(String roleName) throws Exception {
      project = setUpProject(NOT_HIDDEN);
      Person person = newPerson();
      commitSession();
      setUpRole(person, project, roleName);
      commitCloseAndOpenSession();

      tester.clickLinkWithKey("logout");

      String personUserId = person.getUserId();
      String personName = person.getName();

      tester.login(personUserId, "test");
      tester.assertTextPresent(project.getName());
      tester.clickLinkWithKey("navigation.people");
      tester.assertTextPresent(personName);
      tester.assertLinkNotPresentWithKey("people.link.add_person");
   }
}
