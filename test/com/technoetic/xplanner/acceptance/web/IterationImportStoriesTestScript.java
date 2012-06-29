/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: May 12, 2005
 * Time: 10:49:29 PM
 */
package com.technoetic.xplanner.acceptance.web;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.technoetic.xplanner.forms.ImportStoriesForm;
import com.technoetic.xplanner.importer.spreadsheet.SpreadsheetHeaderConfiguration;
import com.technoetic.xplanner.views.IterationStoriesPage;

public class IterationImportStoriesTestScript extends AbstractIterationTestScript
   {

   private String startDate;
   private String endDate;

   public IterationImportStoriesTestScript()
   {
      startDate = getFormatedDate(2004, 6, 13);
      endDate = getFormatedDate(2004, 6, 26);
   }

   protected void setUpTestIteration()
      throws Exception
   {
      tester.gotoProjectsPage();
      tester.clickLinkWithText(testProjectName);
      iterationTester.addIteration(testIterationName,
                          startDate, endDate, testIterationDescription);
      tester.clickLinkWithText(testIterationName);
      iterationId = tester.getCurrentPageObjectId();
   }

   public void testImportSuccess() throws Exception
   {
      runImport(IterationImportStoriesTestScript.class.getResourceAsStream("/data/Cookbook.xls"), false);
      assertImportedStory("Story 1", 1, 1);
      assertImportedStory("Story 2", 1, 1);
   }

   public void testDoubleImport_everyStoryShouldApearTwoTimes() throws Exception
   {
      runImport(IterationImportStoriesTestScript.class.getResourceAsStream("/data/Cookbook.xls"), false);
      runImport(IterationImportStoriesTestScript.class.getResourceAsStream("/data/Cookbook.xls"), false);
      assertImportedStory("Story 1", 1, 2);
      assertImportedStory("Story 2", 1, 2);
   }

   public void testImportFailed_DueToMissingCookbookFields() throws Exception
   {
      runImport(IterationImportStoriesTestScript.class.getResourceAsStream("/data/Cookbook-MissingName.xls"), false);
      tester.assertTextPresent("Some stories did not have <i>name</i> specified");
   }

   public void testImportFailed_DueToMissingWorksheet() throws Exception
   {
      runImport(IterationImportStoriesTestScript.class.getResourceAsStream("/data/Cookbook-MissingWorksheet.xls"), false);
      tester.assertTextPresent("Could not find worksheet named <i>" + SpreadsheetHeaderConfiguration.DEFAULT_WORKSHEET_NAME + "</i>. Please check the name and try again.");
   }

   public void testImportFailed_DueToWrongCookbookFileFormat() throws Exception
   {
      ByteArrayInputStream corruptedCookbookStream = new ByteArrayInputStream("file is corrupted".getBytes());
      runImport(corruptedCookbookStream, false);
      tester.assertKeyPresent("import.status.corrupted_file");
   }

   public void testImportFailed_MissingRequiredHeaderName() throws Exception
   {
      tester.clickLinkWithKey(IterationStoriesPage.IMPORT_STORIES_LINK);
      tester.setFormElement("worksheetName", "");
      tester.setFormElement("titleColumn", "");
      tester.setFormElement("endDateColumn", "");
      tester.setFormElement("statusColumn", "");
      tester.setFormElement("priorityColumn", "");
      tester.setFormElement("estimateColumn", "");
      tester.submit();
      tester.assertKeyPresent(ImportStoriesForm.NO_END_DATE_COLUMN_KEY);
      tester.assertKeyPresent(ImportStoriesForm.NO_PRIORITY_COLUMN_KEY);
      tester.assertKeyPresent(ImportStoriesForm.NO_TITLE_COLUMN_KEY);
   }

   public void testImportFailed_WrongRequiredHeaderName() throws Exception
   {
      tester.clickLinkWithKey(IterationStoriesPage.IMPORT_STORIES_LINK);
      tester.setFormElement("titleColumn", "Wrong name");
      tester.uploadFile("formFile",
                        "/data/Cookbook.xls",
                        IterationImportStoriesTestScript.class.getResourceAsStream("/data/Cookbook.xls"));
      tester.submit();
      tester.assertTextPresent("Could not find a column with <i>Wrong name</i> as the header in the cookbook file. Please check the headers and try again.");
   }

   public void testImportSuccess_onlyUncompletedStories() throws Exception
   {
      runImport(IterationImportStoriesTestScript.class.getResourceAsStream("/data/Cookbook.xls"), true);
      assertImportedStory("Story 2", 1, 1);
      tester.assertTextNotPresent("Story 1");
   }

   public void testReset() throws Exception
   {
      tester.clickLinkWithKey(IterationStoriesPage.IMPORT_STORIES_LINK);
      tester.checkCheckbox("onlyIncomplete");
      tester.setFormElement("completedStatus", "");
      tester.uploadFile("formFile",
                        "/data/Cookbook.xls",
                        IterationImportStoriesTestScript.class.getResourceAsStream("/data/Cookbook.xls"));
      tester.submit();
      tester.assertTextPresent("Please enter the status for completed stories.");
      tester.uncheckCheckbox("onlyIncomplete");
      tester.submit();
      tester.assertTextNotPresent("Please enter the status for completed stories.");
   }

   //todo MPP investigate why  httpunitdialog doesn't support cookies
   public void atestSaveFormInCookies()
   {
      final String newStatusHeader = "New status header";
      tester.clickLinkWithKey(IterationStoriesPage.IMPORT_STORIES_LINK);
      tester.setFormElement("statusColumn", newStatusHeader);
      tester.uploadFile("formFile",
                        "/data/Cookbook.xls",
                        IterationImportStoriesTestScript.class.getResourceAsStream("/data/Cookbook.xls"));
      tester.submit();
//      assertEquals("New column header has not been saved as a cookie",
//                   newStatusHeader,
//                   tester.getDialog().getCookieValue(ImportStoriesAction.STORY_STATUS_PROPERTY_KEY));
//      tester.clickLinkWithKey(IterationStoriesPage.IMPORT_STORIES_LINK);
      tester.assertTextInElement("statusColumn", newStatusHeader);
   }

   private String getFormatedDate(int year, int month, int day)
   {
      Calendar calendar = Calendar.getInstance();
      calendar.set(year, month, day);
      String dateFormatString = tester.getMessage("format.date");
      DateFormat format = new SimpleDateFormat(dateFormatString);
      Date date = calendar.getTime();
      return format.format(date);
   }

   private void runImport(InputStream contents, boolean onlyIncomplete)
   {
      assertNotNull("no contents - is the speadsheet really there?", contents);
      tester.clickLinkWithKey(IterationStoriesPage.IMPORT_STORIES_LINK);
      if (onlyIncomplete)
      {
         tester.checkCheckbox("onlyIncomplete");
      }
      tester.uploadFile("formFile", "/data/Cookbook.xls", contents);
      tester.submit();
   }

   private void assertImportedStory(String storyName, int priority, int occurCnt)
   {
      tester.assertCellNumberForRowWithKey("objecttable",
                                           storyName,
                                           IterationStoriesPage.STORIES_PRIORITY_COLUMN,
                                           new Integer(priority), occurCnt);
      tester.assertCellNumberForRowWithKey("objecttable",
                                           storyName,
                                           IterationStoriesPage.STORIES_ESTIMATED_ORIGINAL_HOURS_COLUMN,
                                           new Double(0), occurCnt);
   }

}