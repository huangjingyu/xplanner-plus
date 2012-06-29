package com.technoetic.xplanner.acceptance.web;

import org.xml.sax.SAXException;

public class PersonTester {
   private XPlannerWebTester tester;


   public PersonTester(XPlannerWebTester tester) {
      this.tester = tester;
   }

   public void addPerson(String name,
                         String userId,
                         String initials,
                         String email,
                         String phone) throws Exception {
      addPerson(name, userId, initials, email, phone, true);
   }

   public void addPerson(String name,
                         String userId,
                         String initials,
                         String email,
                         String phone,
                         boolean createSystemAdminIfPossible)
         throws Exception {
      gotoPeoplePage();
      tester.clickLinkWithKey("people.link.add_person");
      tester.assertTextPresent("Create Profile:");
      populatePersonForm(name, userId, initials, email, phone, createSystemAdminIfPossible);
      tester.submit();
      tester.assertKeyNotPresent("errors.header");
   }

   public void addPersonWithError(String name,
                                  String userId,
                                  String initials,
                                  String email,
                                  String phone) throws Exception {
      addPersonWithError(name, userId, initials, email, phone, true);
   }

   public void addPersonWithError(String name,
                                  String userId,
                                  String initials,
                                  String email,
                                  String phone,
                                  boolean createSystemAdminIfPossible) throws Exception {
      gotoPeoplePage();
      tester.clickLinkWithKey("people.link.add_person");
      tester.assertTextPresent("Create Profile:");
      populatePersonForm(name, userId, initials, email, phone, createSystemAdminIfPossible);
      tester.submit();
   }

   public void addPersonWithRole(String name,
                                 String userId,
                                 String initials,
                                 String email,
                                 String phone,
                                 String projectName, String roleName)
         throws Exception {
      gotoPeoplePage();
      tester.clickLinkWithKey("people.link.add_person");
      tester.assertTextPresent("Create Profile:");
      populatePersonForm(name, userId, initials, email, phone, false);
      tester.assignRoleOnProject(projectName, roleName);
      tester.submit();
      tester.assertKeyNotPresent("errors.header");
   }

// --------------------- Interface WebTester ---------------------

   public void assertOnPeoplePage() {
      tester.assertKeyPresent("people.title");
      tester.assertKeyPresent("people.tableheading.name");
      tester.assertKeyPresent("people.tableheading.initials");
      tester.assertKeyPresent("people.tableheading.phone");
      tester.assertKeyPresent("people.tableheading.email");
      tester.assertKeyPresent("people.tableheading.actions");
   }

   public void assertOnPersonPage(String userIdentifier) {
      tester.assertKeyPresent("person.editor.edit_prefix");
      tester.assertFormElementEquals("userIdentifier", userIdentifier);
   }

   public void assignRoleToPersonOnProject(String developerName, String projectName, String roleName) throws
                                                                                                      SAXException {
      gotoPeoplePage();
      tester.clickLinkWithText(developerName);
      tester.clickLinkWithKey("action.edit.person");
      tester.assignRoleOnProject(projectName, roleName);
      tester.submit();
   }

   public void gotoPeoplePage() {
      tester.gotoProjectsPage();
      tester.clickLinkWithKey("projects.link.people");
      assertOnPeoplePage();
   }

   public void populatePersonForm(String name,
                                  String userId,
                                  String initials,
                                  String email,
                                  String phone,
                                  boolean createSystemAdminIfPossible) {
      tester.setFormElement("name", name);
      tester.setFormElement("userIdentifier", userId);
      tester.setFormElement("initials", initials);
      tester.setFormElement("email", email);
      tester.setFormElement("phone", phone);
      tester.setFormElement("newPassword", XPlannerWebTesterImpl.DEFAULT_PASSWORD);
      tester.setFormElement("newPasswordConfirm", XPlannerWebTesterImpl.DEFAULT_PASSWORD);
//      if (createSystemAdminIfPossible && tester.getDialog().getForm().hasParameterNamed("systemAdmin")) {
//         tester.setFormElement("systemAdmin", "true");
//      }
   }
}

