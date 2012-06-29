package com.technoetic.xplanner.acceptance.web;

public class IntegrationPageTestScript extends AbstractPageTestScript {

    protected void setUp() throws Exception {
        super.setUp();
        tester.login();
        tester.addProject(testProjectName, testProjectDescription);
        tester.clickLinkWithText(testProjectName);
        tester.clickLinkWithText("Integrations");
    }

    public void tearDown() throws Exception {
        tearDownTestProject();
        super.tearDown();
    }

    public void testIntegrationPage() {
        // Just check that the page doesn't have errors
        tester.assertTextNotPresent("error");
    }

    protected void traverseLinkWithKeyAndReturn(String key) throws Exception {
        tester.clickLinkWithKey(key);
        tester.gotoPage("view", "projects", 0);
    }
}
