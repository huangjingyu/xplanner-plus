package com.technoetic.xplanner.acceptance.web;



/**
 * Created by IntelliJ IDEA.
 * User: sg897500
 * Date: Nov 18, 2004
 * Time: 1:49:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProjectPageFormatingTestScript extends AbstractPageTestScript
{
    private String startDate;
    private String endDate;
    private String testProjectDescriptionWiki = "|SD1.2|jbond 10/11/04 12:00p|SD1.2 jbond 10/11/04 12:00p |";
    private String firstColumn = "SD1.2";
    private String secondColumn = "jbond 10-11-04 12:00p";
    private String thirdColumn = "SD1.2 jbond 10-11-04 12:00p";
    private String projectId = null;


    public void setUp() throws Exception
    {
        startDate = tester.dateStringForNDaysAway(0);
        endDate = tester.dateStringForNDaysAway(14);
        super.setUp();
        setUpTestPerson();
        setUpTestProject(testProjectName, testProjectDescriptionWiki);
        tester.login();
        tester.gotoProjectsPage();
        tester.clickLinkWithText(project.getName());
        projectId = tester.getCurrentPageObjectId();
    }

    public void tearDown() throws Exception
    {
        tester.logout();
        super.tearDown();
    }

    public void testWikiTableFormating() throws Exception
    {
        tester.assertOnProjectPage();
        tester.assertTextNotPresent(testProjectDescriptionWiki);
        tester.assertTextPresent(firstColumn);
        tester.assertTextPresent(secondColumn);
        tester.assertTextPresent(thirdColumn);
    }

    public void testProjectLinkFormating() throws Exception
    {
        tester.assertOnProjectPage();
        changeProjectDescription("project:" + projectId + "?");
        tester.assertLinkPresentWithText("project: " + testProjectName + "?");
    }

    public void testRrojectBracketsRendering() throws Exception
    {
        tester.assertOnProjectPage();
        changeProjectDescription("<>");
        changeRenderBracketsOption(true);
        tester.assertTextPresent("&lt;&gt;");
        changeRenderBracketsOption(false);
        tester.assertTextPresent("<>");
    }

    private void changeProjectDescription(String description)
    {
        tester.clickLinkWithKey("action.edit.project");
        tester.setFormElement("description", description);
        tester.submit();
        tester.assertOnProjectPage();
    }

    private void changeRenderBracketsOption(boolean checkIt)
    {
        tester.clickLinkWithKey("action.edit.project");
        if (checkIt)
        {
            tester.checkCheckbox("optEscapeBrackets", "true");
        }
        else
        {
            tester.uncheckCheckbox("optEscapeBrackets");
        }
        tester.submit();
        tester.assertOnProjectPage();

    }

    public void testClearQuestLinkFormating() throws Exception
    {
        tester.assertOnProjectPage();
        changeProjectDescription("cq:12345");
        tester.assertLinkPresentWithText("cq:12345");
    }

}
