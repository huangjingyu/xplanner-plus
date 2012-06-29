package com.technoetic.xplanner.acceptance.web;


public class FeaturePageTestScript extends AbstractPageTestScript {

    public void setUp() throws Exception {
        super.setUp();
        simpleSetUp();
        tester.addFeature(testFeatureName, testFeatureDescription);
        tester.clickLinkWithText(testFeatureName);

    }

    public void tearDown() throws Exception {
        simpleTearDown();
        super.tearDown();
    }

    public void testContentAndLinks() {
        tester.assertOnFeaturePage();
        tester.assertTextPresent(testFeatureName);
        tester.assertTextPresent(testFeatureDescription);
        tester.clickLinkWithKey("navigation.story");
        tester.assertOnStoryPage();
        tester.clickLinkWithText(testFeatureName);
        tester.assertOnFeaturePage();
        tester.clickLinkWithKey("navigation.iteration");
        iterationTester.assertOnIterationPage();
        tester.clickLinkWithText(storyName);
        tester.clickLinkWithText(testFeatureName);
        tester.assertOnFeaturePage();
        tester.clickLinkWithKey("navigation.project");
        tester.assertOnProjectPage();
        tester.clickLinkWithText(testIterationName);
        tester.clickLinkWithText(storyName);
        tester.clickLinkWithText(testFeatureName);
        tester.assertOnFeaturePage();
    }

    public void testAddingAndDeletingNotes() {
        runNotesTests(XPlannerWebTester.FEATURE_PAGE);
    }

    protected void traverseLinkWithKeyAndReturn(String key) throws Exception {
        tester.clickLinkWithKey(key);
        tester.gotoPage("view", "projects", 0);
    }
}
