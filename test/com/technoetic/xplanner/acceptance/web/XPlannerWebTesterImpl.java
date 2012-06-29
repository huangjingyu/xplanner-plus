/*
 * Copyright (c) Mateusz Prokopowicz. All Rights Reserved.
 */

package com.technoetic.xplanner.acceptance.web;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import junit.extensions.FieldAccessor;
import junit.framework.Assert;
import net.sf.xplanner.domain.Project;
import net.sourceforge.jwebunit.api.ITestingEngine;
import net.sourceforge.jwebunit.html.Cell;
import net.sourceforge.jwebunit.html.Row;
import net.sourceforge.jwebunit.html.Table;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;
import org.hibernate.type.Type;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.meterware.httpunit.HTMLElementPredicate;
import com.meterware.httpunit.HttpNotFoundException;
import com.meterware.httpunit.TableCell;
import com.meterware.httpunit.UploadFileSpec;
import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.XPlannerTestSupport;
import com.technoetic.xplanner.acceptance.IterationTester;
import com.technoetic.xplanner.acceptance.web.MailTester.Email;
import com.technoetic.xplanner.actions.EditObjectAction;
import com.technoetic.xplanner.actions.PutTheClockForwardAction;
import com.technoetic.xplanner.db.NoteHelper;
import com.technoetic.xplanner.db.hibernate.GlobalSessionFactory;
import com.technoetic.xplanner.db.hibernate.HibernateHelper;
import com.technoetic.xplanner.domain.NoteAttachable;
import com.technoetic.xplanner.testing.DateHelper;
import com.technoetic.xplanner.util.LogUtil;
import com.technoetic.xplanner.util.TimeGenerator;

/**
 * User: mprokopowicz
 * Date: Feb 15, 2006
 * Time: 3:46:09 PM
 */
public class XPlannerWebTesterImpl extends net.sourceforge.jwebunit.junit.WebTester implements XPlannerWebTester {
   public static final String DEFAULT_PASSWORD = "password";

   private static final Comparator NUMBER_AND_STRING_COMPARATOR = new Comparator() {
      public int compare(Object number, Object string) {
         return Double.compare(((Number) number).doubleValue(), (new Double((String) string)).doubleValue());
      }
   };
   private static final Comparator STRING_COMPARATOR = new Comparator() {
      public int compare(Object string1, Object string2) {
         return ((String) string1).trim().equals(((String) string2).trim()) ? 0 : -1;
      }
   };
   protected static final String RESOURCE_BUNDLE_NAME = DateHelper.RESOURCE_BUNDLE_NAME;
   private static final String MAIN_TABLE_ID = "objecttable";
   private static final String DELETE_IMAGE = "delete.gif";
   private static final String EDIT_IMAGE = "edit.gif";
   private static final String MOVECONTINUE_IMAGE = "movecontinue.gif";
   private static final String CLOCK_IMAGE = "clock2.gif";
   private static Session hibernateSession;
   protected static final Logger LOG = LogUtil.getLogger();
   protected XPlannerProperties properties;
   private MailTester mailTester;
   private IterationTester iterationTester = new IterationTester(this);
   protected String baseUrl;
   private int currentDayIndex;

   public XPlannerWebTesterImpl() {
      properties = new XPlannerProperties();
      baseUrl = XPlannerTestSupport.getAbsoluteTestURL();
      getTestContext().setBaseUrl(baseUrl);
      getTestContext().setResourceBundleName(RESOURCE_BUNDLE_NAME);
   }

   public String addFeature(String name, String description) {
      assertOnStoryPage();
      clickLinkWithKey("story.link.create_feature");
      assertKeyPresent("feature.prefix");
      assertLinkPresentWithKey("form.description.help");
      setFormElement("name", name);
      setFormElement("description", description);
      submit();
      assertOnStoryPage();
      return getIdFromLinkWithText(name);
   }

   public void addNonAttachmentInformationInNote(String subject, String body) {
      clickLinkWithKey("note.create");
      assertLinkPresentWithKey("form.description.help");
      setFormElement("subject", subject);
      //selectOption("authorId", authorName);
      setFormElement("body", body);
   }

   public void addNote(String subject, String body, String authorName) {
      addNonAttachmentInformationInNote(subject, body);
      submit();
   }

   public void addNote(String subject, String body, String authorName, String filename) {
      addNonAttachmentInformationInNote(subject, body);
      InputStream fileFromClassPath = getClass().getResourceAsStream(filename);
      Assert.assertNotNull(fileFromClassPath);
      uploadFile("formFile", filename, fileFromClassPath);
      submit();
   }

   public String addProject(String projectName, String description) {
      assertOnTopPage();
      clickLinkWithKey("projects.link.add_project");
      assertKeyPresent("project.editor.create");
      assertLinkPresentWithKey("form.description.help");
      setFormElement("name", projectName);
      setFormElement("description", description);
      checkCheckbox("sendemail", "true");
      submit();
      assertOnTopPage();
      return getIdFromLinkWithText(projectName);
   }

   public String addTask(String name,
                         String acceptorName,
                         String description,
                         String estimatedHours) {
      assertOnStoryPage();
      clickLinkWithKey("story.link.create_task");
      assertKeyPresent("task.prefix");
      assertLinkPresentWithKey("form.description.help");
      setFormElement("name", name);
      setFormElement("estimatedHours", estimatedHours);
      setFormElement("description", description);
      selectOption("acceptorId", acceptorName);
      submit();
      assertOnStoryPage();
      return getIdFromLinkWithText(name);
   }

   public String addTask(String name,
                         String acceptorName,
                         String description,
                         String estimatedHours,
                         String disposition) {
      assertOnStoryPage();
      clickLinkWithKey("story.link.create_task");
      assertKeyPresent("task.prefix");
      assertLinkPresentWithKey("form.description.help");
      setFormElement("name", name);
      setFormElement("estimatedHours", estimatedHours);
      setFormElement("description", description);
      if (acceptorName != null) {
         selectOption("acceptorId", acceptorName);
      }
      selectOption("dispositionName", disposition);
      submit();
      assertOnStoryPage();
      return getIdFromLinkWithText(name);
   }

   public void setTimeEntry(int index, int durationInHours, String firstPersonInitials) {
      setTimeEntry(index, 0, durationInHours, firstPersonInitials);
   }

   public void setTimeEntry(int index, int startHourOffset, int endHourOffset, String firstPersonInitials
   ) {
      setTimeEntry(index,
                   dateTimeStringForNHoursAway(startHourOffset),
                   dateTimeStringForNHoursAway(endHourOffset),
                   firstPersonInitials,
                   null
      );
   }

   public void setTimeEntry(int index,
                            String startTime,
                            String endTime,
                            String firstPersonInitials,
                            String secondPersonInitials
   ) {
      setFormElement("startTime[" + index + "]", startTime);
      setFormElement("endTime[" + index + "]", endTime);
      selectOption("person1Id[" + index + "]", firstPersonInitials);
      if (secondPersonInitials != null) {
         selectOption("person2Id[" + index + "]", secondPersonInitials);
      }
      submit();
   }

   public void setTimeEntry(int index,
                            int startHourOffset,
                            int endHourOffset,
                            String firstPersonInitials,
                            String secondPersonInitials
   ) {
      setTimeEntry(index, dateTimeStringForNHoursAway(startHourOffset),
                   dateTimeStringForNHoursAway(endHourOffset),
                   firstPersonInitials,
                   secondPersonInitials
      );
   }

   public String addUserStory(String storyName, String storyDescription, String estimatedHours, String orderNo) {
      iterationTester.assertOnIterationPage();
      clickLinkWithKey("iteration.link.create_story");
      assertKeyPresent("story.editor.create");
      assertLinkPresentWithKey("form.description.help");
      setFormElement("name", storyName);
      setFormElement("estimatedHours", estimatedHours);
      setFormElement("description", storyDescription);
      setFormElement("orderNo", orderNo);
      submit();
      iterationTester.assertOnIterationPage();
      return getIdFromLinkWithText(storyName);
   }

   public void assertActualHoursColumnPresent() {
      String progressBar = new XPlannerProperties().getProperty("xplanner.progressbar.impl");
      if (!progressBar.equals("image")) {
         assertLinkPresentWithKey("iterations.tableheading.actual_hours");
      } else {
         assertLinkNotPresentWithKey("iterations.tableheading.actual_hours");
      }
   }

   public void assertCellNumberForRowWithKey(String tableId,
                                             String rowName,
                                             String key,
                                             Number number) {
      assertCellNumberForRowWithText(tableId, rowName, getMessage(key), number);
   }

   public void assertCellNumberForRowWithKey(String tableId,
                                             String rowName,
                                             String key,
                                             Number val, int nbr) {
      assertCellNumberForRowWithTextAndColumnKeyOccurs(tableId, rowName, getMessage(key), val, nbr);
   }

   public void assertCellNumberForRowWithText(String tableId,
                                              String rowName,
                                              String columnName,
                                              Number val) {
      String[] cellValues = extractCellValues(tableId, columnName, rowName);
      Assert.assertTrue("Value [" + val + "] not found in table [" + tableId + "] for row [" + rowName + "]",
                        countOccurences(cellValues, val, NUMBER_AND_STRING_COMPARATOR) > 0);
   }

   public void assertCellNumberForRowWithTextAndColumnKeyOccurs(String tableId,
                                                                String rowName,
                                                                String columnName,
                                                                Number expectedNumber,
                                                                int expectedOccurrenceCount) {
      assertCellContentForRowWithTextAndColumnKeyOccurs(tableId,
                                                        rowName,
                                                        columnName,
                                                        expectedNumber,
                                                        expectedOccurrenceCount,
                                                        NUMBER_AND_STRING_COMPARATOR);
   }

   public void assertCellNumberForRowWithTextAndColumnKeyEquals(String tableId,
                                                                String rowName,
                                                                String columnKey,
                                                                Number expectedNumber) {
      assertCellNumberForRowWithTextAndColumnNameEquals(tableId, rowName, getMessage(columnKey), expectedNumber);
   }

   public void assertCellNumberForRowWithTextAndColumnNameEquals(String tableId,
                                                                 String rowName,
                                                                 String columnName,
                                                                 Number expectedNumber) {
      String[] cellValues = extractCellValues(tableId, columnName, rowName);
      assertEqualsAny("wrong cell value", expectedNumber, cellValues);
   }

   private void assertEqualsAny(String message, Number number, String[] cellValues) {
      for (int i = 0; i < cellValues.length; i++) {
         if (number.equals(new Double(cellValues[i]))) {
            return;
         }
      }
      Assert.fail(message + "; expected: " + number + " was: " + StringUtils.join(cellValues, ", "));
   }

   public void assertCellTextForRowIndexAndColumnKeyContains(String tableId,
                                                             int rowIndex,
                                                             String columnKey,
                                                             String expectedCellText) {
      String cellValue = extractCellValue(tableId, getMessage(columnKey), rowIndex);
      assertContains("table " + tableId + "[" + columnKey + "," + rowIndex + "]=", cellValue, expectedCellText);
   }

   private void assertContains(String message, String text, String expectedContained) {
      Assert.assertTrue(message + " '" + text + "' doesn't contain " + expectedContained,
                        text.indexOf(expectedContained) != -1);
   }

   public void assertCellTextForRowIndexAndColumnKeyEquals(String tableId,
                                                           int rowIndex,
                                                           String columnKey,
                                                           String expectedCellText) {
      String cellValue = extractCellValue(tableId, getMessage(columnKey), rowIndex);
      assertEquals("table " + tableId + "[" + columnKey + "," + rowIndex + "] has wrong cell value",
                   expectedCellText,
                   cellValue);
   }

   private void assertEquals(String message, String text, String cellValue) {
      if (text.equals(cellValue.trim())) {
         return;
      }
      Assert.fail(message + "; expected: " + text + " was: " + cellValue + ",");
   }

   public void assertCellTextForRowWithTextAndColumnKeyOccurs(String tableId,
                                                              String rowName,
                                                              String columnName,
                                                              String expectedText,
                                                              int expectedOccurrenceCount) {
      assertCellContentForRowWithTextAndColumnKeyOccurs(tableId,
                                                        rowName,
                                                        columnName,
                                                        expectedText,
                                                        expectedOccurrenceCount,
                                                        STRING_COMPARATOR);
   }

   private void assertCellContentForRowWithTextAndColumnKeyOccurs(String tableId, String rowName, String columnName,
                                                                  Object expectedText,
                                                                  int expectedOccurrenceCount,
                                                                  Comparator comparator) {
      String[] cellValues = extractCellValues(tableId, columnName, rowName);
      int cnt = countOccurences(cellValues, expectedText, comparator);
      Assert.assertEquals("Text [" + expectedText + "] occurs in " +
                          "table [" + tableId + "] for " +
                          "row [" + rowName + "] " +
                          cnt + " times, " +
                          "expected " + expectedOccurrenceCount,
                          expectedOccurrenceCount, cnt);
   }

   public void assertCellTextForRowWithTextAndColumnKeyEquals(String tableId,
                                                              String rowName,
                                                              String columnKey,
                                                              String expectedText) {
      assertCellTextForRowWithTextAndColumnNameEquals(tableId, rowName, getMessage(columnKey), expectedText);
   }

   public void assertCellTextForRowWithTextAndColumnNameEquals(String tableId,
                                                               String rowName,
                                                               String columnName,
                                                               String expectedText) {
      String[] cellValues = extractCellValues(tableId, columnName, rowName);
      Assert.assertTrue("Text [" + expectedText + "] not found in table [" + tableId + "] for row [" + rowName + "]",
                        countOccurences(cellValues, expectedText, STRING_COMPARATOR) > 0);
   }

   public void assertCellTextForRowWithTextAndColumnKeyEquals(String tableId,
                                                              int rowIndex,
                                                              String columnKey,
                                                              String expectedRowText) {
      String cellValue = extractCellValue(tableId, getMessage(columnKey), rowIndex);
      assertValue("table " + tableId + "[" + columnKey + "," + rowIndex + "] has wrong cell value",
                  expectedRowText,
                  cellValue);
   }

   private String extractCellValue(String tableId, String columnName, int rowIndex) {
      Table objectTable = getTable(tableId);
      int col = getFirstColNumberWithText(objectTable, 0, columnName);
      return getCell(rowIndex, objectTable, col).getValue();
   }

private Cell getCell(int rowIndex, Table objectTable, int col) {
	return ((Cell)((Row)objectTable.getRows().get(rowIndex)).getCells().get(col));
}

   private void assertValue(String message, String text, String cellValue) {
      if (text.equals(cellValue.trim())) {
         return;
      }
      Assert.fail(message + "; expected: " + text + " was: " + cellValue + ",");
   }

   private String[] extractCellValues(String tableId, String columnName, String rowName) {
      Table objectTable = getTable(tableId);
      int col = getFirstColNumberWithText(objectTable, 0, columnName);

      int[] rows = getRowNumbersWithText(objectTable, rowName);
      Assert.assertNotNull("Acceptance tests require table with id = " + tableId, rows);

      return collectCellValues(objectTable, rows, col);
   }

   private int[] getRowNumbersWithText(Table objectTable, String text) {
      List rowNbrList = new ArrayList();
      for (int rowIndex = 0; rowIndex < objectTable.getRowCount(); rowIndex++) {
    	Row row = (Row) objectTable.getRows().get(rowIndex);
		for (Cell cell : ((List<Cell>)row.getCells())) {
			if (cell.getValue().indexOf(text) != -1) {
	               rowNbrList.add(new Integer(rowIndex));
	            }
		}
      }

      if (rowNbrList.isEmpty()) {
         return null;
      }
      int[] retVal = new int[rowNbrList.size()];
      for (int i = 0; i < retVal.length; i++) {
         retVal[i] = ((Integer) rowNbrList.get(i)).intValue();
      }
      return retVal;
   }

   private String[] collectCellValues(Table objectTable, int[] rows, int col) {
      String[] cellValues = new String[rows.length];
      for (int i = 0; i < cellValues.length; i++) {
         cellValues[i] = getCell(rows[i], objectTable, col).getValue();
      }

      return cellValues;
   }

   private int countOccurences(String[] cellValues, Object object, Comparator comparator) {
      int cnt = 0;
      for (int i = 0; i < cellValues.length; i++) {
         if (comparator.compare(object, cellValues[i]) == 0) {
            cnt++;
         }
      }
      return cnt;
   }

   public void assertLinkNotPresentWithKey(String key) {
      assertLinkNotPresentWithText(getMessage(key));
   }

   public void assertLinkPresentWithImage(String imageName, String pathElement) {
      Assert.assertTrue(
            "Link with image [" + imageName + "] contains path element [" + pathElement + "] not found in response.",
            findLinkWithImage(imageName, pathElement) != null);
   }

   public void assertLinkPresentWithKey(String key) {
      assertLinkPresentWithText(getMessage(key));
   }

   public void assertLinkPresentWithText(String linkText) {
      Assert.assertTrue("Link with text [" + linkText + "] not found in response.",
                        isLinkPresentWithText(linkText));
   }

   /**
    * @deprecated Use mailTester directly
    */
//DEBT Use mail tester directly
   public void assertNoEmailMessage(String subject,
                                    String[] recipients,
                                    String from,
                                    List bodyElements) throws InterruptedException {
      Email email = new Email(from, recipients, subject, bodyElements);
      mailTester.assertEmailHasNotBeenReceived(email);
   }

   public void assertEmailNotificationMessage(String subject, String[] recipients, String from, List bodyElements)
         throws InterruptedException {
      Email email = new Email(from, recipients, subject, bodyElements);
      mailTester.assertEmailHasBeenReceived(email);
   }

   public void setUpSmtpServer() throws Exception {
      if (mailTester == null) mailTester = new MailTester(this);
      mailTester.setUp();
   }

   public void tearDownSmtpServer() {
      if (mailTester != null) {
         mailTester.tearDown();
         mailTester = null;
      }
   }

   public void resetTime() throws UnsupportedEncodingException {
      if (currentDayIndex > 0){
         moveCurrentDay(0);
      }
   }

   public void tearDown() throws Exception {
      tearDownSmtpServer();
      resetTime();
   }

   public void assertOnFeaturePage() {
      assertKeyPresent("feature.prefix");
   }

   public void assertOnMoveContinueStoryPage(String storyName) {
      assertKeyPresent("story.editor.move_or_continue");
      assertTextPresent(storyName);
   }

   public void assertOnMoveContinueTaskPage(String taskName) {
      assertKeyPresent("task.editor.move_or_continue");
      assertTextPresent(taskName);
   }

   public void assertOnPage(String page) {
      if (page.equals(PROJECT_PAGE)) {
         assertOnProjectPage();
      } else if (page.equals(STORY_PAGE)) {
         assertOnStoryPage();
      } else if (page.equals(ITERATION_PAGE)) {
         iterationTester.assertOnIterationPage();
      } else if (page.equals(TASK_PAGE)) {
         assertOnTaskPage();
      } else {
         Logger.getLogger(XPlannerWebTesterImpl.class).warn(
               "Could not locate page: " + page + " in assertOnPage(" + page + ")");
      }
   }

   public void assertOnProjectPage() {
      assertKeyPresent("project.prefix");
      assertKeyPresent("notes.label.notes");
   }

   public void assertOnStoryPage() {
      assertKeyPresent("story.prefix");
      assertLinkPresentWithKey("navigation.top");
      assertLinkPresentWithKey("navigation.project");
      assertLinkPresentWithKey("navigation.iteration");
      assertKeyPresent("notes.label.notes");
   }

   public void assertOnStoryPage(String storyName) {
      assertOnStoryPage();
      assertTextPresent(storyName);
   }

   public void assertOnTaskPage() {
      assertKeyPresent("task.prefix");
      assertKeyPresent("task.label.estimated_hours");
      assertKeyPresent("task.label.actual_hours");
      assertKeyPresent("notes.label.notes");
   }

   public void assertOnTopPage() {
      assertTitleEqualsKey("projects.title");
      if (isTextPresent(getMessage("projects.tableheading.name"))) {
         assertKeyPresent("projects.tableheading.name");
         assertKeyPresent("projects.tableheading.iteration");
         //assertKeyPresent("projects.tableheading.actions");
      }
   }

   public void assertOptionListed(String selectName, String option) {
      assertFormElementPresent(selectName);
      assertSelectOptionPresent(selectName, option);
   }

   public void assertOptionNotListed(String selectName, String option) {
      assertFormElementPresent(selectName);
      assertSelectOptionNotPresent(selectName, option);
   }

   public void assertTextNotPresentWithKey(String key) {
      assertTextNotPresent(getMessage(key));
   }

   public void assertTextPresentWithKey(String key) {
      assertKeyPresent(key);
   }

   public void assertKeyPresent(String key, Object arg) {
      String formatPattern = getMessage(key);
      MessageFormat format = new MessageFormat(formatPattern);
      String textToLookFor = format.format(new Object[]{arg});
      assertTextPresent(textToLookFor);
   }

   public void assignRoleOnProject(String projectName, String roleName)
         throws SAXException {
      int[] rows = this.getRowNumbersWithText(AbstractPageTestScript.ROLES_TABLE, projectName);
      Assert.assertNotNull("No project found", rows);
      int projectRowNbr = rows[0] - 1;
      selectOption("projectRole[" + projectRowNbr + "]", roleName);
   }

   public void clickContinueLinkInRowWithText(String text) {
      String imageName = MOVECONTINUE_IMAGE;
      String tableId = MAIN_TABLE_ID;
      clickImageLinkInTableForRowWithText(imageName, tableId, text);
   }

   public void clickDeleteLinkForRowWithText(String text) {
      String imageName = DELETE_IMAGE;
      String tableId = MAIN_TABLE_ID;
      clickImageLinkInTableForRowWithText(imageName, tableId, text);
   }

   public void clickEditLinkInRowWithText(String text) {
      String imageName = EDIT_IMAGE;
      String tableId = MAIN_TABLE_ID;
      clickImageLinkInTableForRowWithText(imageName, tableId, text);
   }

   public void clickEditTimeImage() {
      assertOnTaskPage();
      ITestingEngine dialog = getDialog();
      Table timeTable;
      try {
         timeTable = getTable("time_entries");
         int lastRowIndex = timeTable.getRowCount() - 1;
         Cell lastRowCell = getCell(lastRowIndex, timeTable, 0);
         //HTMLElementPredicate imagePredicate = new LinkImagePredicate();
//FIXME: should be rewrited         WebLink editLink = lastRowCell.getFirstMatchingLink(imagePredicate, EDIT_IMAGE);
//         editLink.click();
      }
      catch (Exception e) {
//         throw new RuntimeException(ExceptionUtility.stackTraceToString(e));
      }
   }

   public void clickEnterTimeLinkInRowWithText(String text) {
      String imageName = CLOCK_IMAGE;
      String tableId = MAIN_TABLE_ID;
      clickImageLinkInTableForRowWithText(imageName, tableId, text);
   }

   public void clickImageLinkInNoteWithSubject(String imageName, String subject) {
      int linkRowInNoteTable = 2;
//      WebResponse storyPage = getDialog().getResponse();
//      try {
//         WebTable noteTable =  storyPage.getFirstMatchingTable(new TextInTablePredicate(),
//                                                              subject);
//         TableCell linkCell = noteTable.getTableCell(linkRowInNoteTable, 0);
//         WebLink deleteLink = linkCell.getFirstMatchingLink(new LinkImagePredicate(),
//                                                            imageName);
//         clickLink(deleteLink);
//      }
//      catch (Exception e) {
//         throw new RuntimeException(ExceptionUtility.stackTraceToString(e));
//      }
   }

   public void clickImageLinkInTableForRowWithText(String imageName,
                                                   String tableId,
                                                   String text) {
//      boolean foundLink = false;
//      HttpUnitDialog dialog = getDialog();
//      WebResponse editPage = dialog.getResponse();
//      try {
//         WebTable objectTable = editPage.getTableWithID(tableId);
//         int[] rows = getRowNumbersWithText(tableId, text);
//         if (rows == null) {
//            throw new RuntimeException("Acceptance tests require table with id = " + tableId);
//         }
//         int rowToSearchForLink = rows[0];
//         for (int column = 0; column < objectTable.getColumnCount(); column++) {
//            TableCell cell = objectTable.getTableCell(rowToSearchForLink, column);
//            LinkImagePredicate predicate = new LinkImagePredicate();
//            WebLink editLink = cell.getFirstMatchingLink(predicate, imageName);
//            if (editLink != null) {
//               clickLink(editLink);
//               foundLink = true;
//               break;
//            }
//         }
//         Assert.assertTrue("Could not found an edit link on row containing: " + text,
//                           foundLink);
//      }
//      catch (Exception e) {
////         throw new RuntimeException(ExceptionUtility.stackTraceToString(e));
//      }
   }

   public void clickLink(WebLink link) {
//      try {
//         link.click();
//         // JM 06/25/04: Hack since there is no way to extend this sucker.
//         // Filed a jwebunit request http://sourceforge.net/tracker/index.php?func=detail&aid=980105&group_id=61302&atid=497982
//         FieldAccessor.set(getDialog(), "resp", getDialog().getWebClient().getCurrentPage());
//      }
//      catch (Exception e) {
//         throw new RuntimeException(ExceptionUtility.stackTraceToString(e));
//      }
   }

   public void clickLinkWithImage(String imageName, String pathElement) {
      WebLink link = findLinkWithImage(imageName, pathElement);
      if (link == null) {
         throw new RuntimeException(
               "Could not find link with image [" + imageName + "] containing path element [" + pathElement + "]");
      }
      clickLink(link);
   }

   public void clickLinkWithKey(String key) {
      try {
         clickLinkWithText(getMessage(key));
      }
      catch (RuntimeException ex) {
         String message = ex.getMessage();
         if (message.indexOf(HttpNotFoundException.class.getName()) != -1) {
            Assert.fail("broken link: " + message.substring(message.indexOf("Error ")));
         } else {
            throw ex;
         }
      }
   }

   public void clickLinkWithText(String text) {
      WebLink link = findLinkWithText(text);
      if (link == null) {
         throw new RuntimeException("Could not find link with text '" + text + "'");
      }
      clickLink(link);
   }

   public void deleteNoteWithSubject(String subject) {
      String imageName = DELETE_IMAGE;
      clickImageLinkInNoteWithSubject(imageName, subject);
   }

   public void deleteObjects(Class clazz, String attribute, String value) {
      deleteObjects(clazz, attribute, value, Hibernate.STRING);
   }

   public void deleteObjects(Class clazz, String attribute, String value, Type type) {
      Session session;
      try {
         session = getSession();
         session.flush();
         session.connection().commit(); // start new txn for following query
         if (NoteAttachable.class.isAssignableFrom(clazz)) {
            List objectList = session.find("from object in " + clazz + " where object." + attribute + " = ?",
                                           value,
                                           type);
            for (Iterator it = objectList.iterator(); it.hasNext();) {
               NoteAttachable obj = (NoteAttachable) it.next();
               NoteHelper.deleteNotesFor(obj, session);
            }
         }
         session.delete("from object in " + clazz + " where object." + attribute + " = ?",
                        value,
                        type);
         releaseSession();
      }
      catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public void deleteProject(String name) throws Exception {
      deleteObjects(Project.class, "name", name);
   }

   public void editNoteWithSubject(String subject) {
      String imageName = EDIT_IMAGE;
      clickImageLinkInNoteWithSubject(imageName, subject);
   }

   public WebLink findLinkWithImage(String imageName, String pathElement) {
//      try {
//         Object[] criteria = new Object[]{imageName, pathElement};
//         return getDialog().getResponse().getFirstMatchingLink(new ImageInLinkPredicate(), criteria);
//      }
//      catch (Exception e) {
         throw new RuntimeException();
//      }
   }

   public WebLink findLinkWithText(String text) {
//      try {
//         return getDialog().getResponse().getFirstMatchingLink(new TextInLinkPredicate(), text);
//      }
//      catch (Exception e) {
         throw new RuntimeException();
//      }
   }

   public TableCell getCell(String tableId, String columnName, int rowIndex) {
//      WebResponse editPage = getDialog().getResponse();
//      WebTable objectTable = getTable(editPage, tableId);
//      int col = getFirstColNumberWithText(objectTable, 0, getMessage(columnName));
//      return objectTable.getTableCell(rowIndex, col);
	   return null;
   }

   public String getCurrentPageObjectId() {
//      String query = getDialog().getResponse().getURL().getQuery();
//      return retrieveIdFromUrl(query);
	   return null;
   }

   public int getFirstColNumberWithText(Table table, int row, String columnName) {
      int colIdx = -1;
//      for (int column = 0; column < table.getColumnCount(); column++) {
//         if (table.getCellAsText(row, column).indexOf(columnName) != -1) {
//            colIdx = column;
//            return colIdx;
//         }
//      }
//      Assert.fail("Could not locate column: " + columnName);
      return colIdx;
   }

   public String getIdFromLinkWithText(String linkText) {
      WebLink link = findLinkWithText(linkText);
      String query = link.getURLString();
      query = query.substring(query.indexOf('?') + 1);
      return retrieveIdFromUrl(query);
   }

   private String retrieveIdFromUrl(String query) {
      String[] options = query.split("&");
      String oid = null;
      for (int i = 0; i < options.length; i++) {
         if (options[i].indexOf("oid=") == 0) {
            oid = options[i].substring(options[i].indexOf("=") + 1);
            break;
         }
      }
      Assert.assertNotNull("object id not found in query string", oid);
      return oid;
   }

   public int[] getRowNumbersWithText(String tableId, String text) {
      Table objectTable = getTable(tableId);
      return getRowNumbersWithText(objectTable, text);
   }

   public String dateStringForNDaysAway(int daysFromToday) {
      return DateHelper.getDateStringDaysFromToday(daysFromToday);
   }

   public String dateTimeStringForNHoursAway(int hoursFromNow) {
      return DateHelper.getDateTimeStringHoursFromNow(hoursFromNow);
   }

   public Session getSession() throws HibernateException {
      Logger.getLogger("org.hibernate").setLevel(Level.WARN);
      HibernateHelper.initializeHibernate();
      if (hibernateSession == null) {
         hibernateSession = GlobalSessionFactory.get().openSession();
      }
      return hibernateSession;
   }

   public String getXPlannerLoginId() {
      return properties.getProperty("xplanner.test.user");
   }

   public String getXPlannerLoginPassword() {
      return properties.getProperty("xplanner.test.password");
   }

   public void gotoPage(String operation, String objectType, int oid) {
      gotoRelativeUrl("/do/" +
                      operation +
                      "/" +
                      objectType +
                      (oid != 0 ? "?oid=" + oid : ""));
   }

   public void gotoProjectsPage() {
      gotoRelativeUrl("/do/view/projects");
   }

   public void gotoRelativeUrl(String relativeUrl) {
      WebResponse response = requestRelativeUrl(relativeUrl);
      FieldAccessor.set(getDialog(), "resp", response);
   }

   private WebResponse requestRelativeUrl(String relativeUrl) {
//      try {
//         return getDialog().getWebClient().getResponse(getTestContext().getBaseUrl() + relativeUrl);
//      } catch (Exception e) {
         throw new RuntimeException();
//      }
   }

   public boolean isKeyPresent(String key) {
	   return false;
//      return getDialog().isTextInResponse(getMessage(key));
   }

   public boolean isLinkPresentWithKey(String key) {
      return isLinkPresentWithText(getMessage(key));
   }

   public boolean isLinkPresentWithText(String linkText) {
      return findLinkWithText(linkText) != null;
   }

   public boolean isTextPresent(String text) {
	   return false;
//      return getDialog().isTextInResponse(text);
   }

   public void login() {
      login(getXPlannerLoginId(), getXPlannerLoginPassword());
   }

   public void login(String user, String password) {
      beginAt("/do/login");
      setFormElement("userId", user);
      setFormElement("password", password);
      submit();
      assertKeyNotPresent("login.failed");
   }

   public void logout() throws Exception {
      gotoProjectsPage();
      clickLinkWithKey("logout");
   }

   public void moveCurrentDay(int days)
         throws UnsupportedEncodingException {
      StringBuffer link = new StringBuffer();
      link.append("/do/edit/putTheClockForward")
            .append("?" + PutTheClockForwardAction.OFFSET_IN_DAYS_KEY + "=")
            .append(days);
      link.append("&returnto=");
//      link.append(URLEncoder.encode(getDialog().getResponse().getURL().toString(), "ISO-8859-1"));
      gotoRelativeUrl(link.toString());
      currentDayIndex += days;
   }

   public Date getCurrentDate(){
      return TimeGenerator.shiftDate(new Date(), Calendar.DATE, currentDayIndex);
   }

   public int getCurrentDayIndex() {
      return currentDayIndex;
   }

   public void executeTask(String taskExecutorUrl)
         throws UnsupportedEncodingException {
      StringBuffer link = new StringBuffer();
      link.append(taskExecutorUrl).append("?returnto=");
//      link.append(URLEncoder.encode(getDialog().getResponse().getURL().toString(), "ISO-8859-1"));
      gotoRelativeUrl(link.toString());
   }

   public void changeLocale(String language) throws Exception {
      StringBuffer link = new StringBuffer("/do/changeLocale?");
      if (!StringUtils.isEmpty(language)) {
         link.append("language=").append(language).append("&");
         getTestContext().setLocale(new Locale(language));
      } else {
         getTestContext().setLocale(Locale.getDefault());
      }
      link.append(EditObjectAction.RETURNTO_PARAM).append("=");
//      link.append(URLEncoder.encode(getDialog().getResponse().getURL().toString(), "ISO-8859-1"));
      gotoRelativeUrl(link.toString());
   }

   public void releaseSession() throws HibernateException, SQLException {
      if (hibernateSession != null) {
         hibernateSession.flush();
         hibernateSession.connection().commit();
         hibernateSession.close();
         hibernateSession = null;
      }
   }

   public void setFormElementWithLabel(String formElementLabel, String value) {
//      super.setFormElementWithLabel(formElementLabel, value);
   }

   public void uploadFile(String fieldName, String filename, InputStream fileContents) {
      String file = (filename.lastIndexOf("/") < 0) ? filename :
                    filename.substring(filename.lastIndexOf("/"));
      UploadFileSpec[] testUploads = {
            new UploadFileSpec(file, fileContents, "text/plain")
      };
//      HttpUnitDialog dialog = getDialog();
//      dialog.getFormParameterValue(fieldName);
//      WebForm form = dialog.getForm();
//      form.setParameter(fieldName, testUploads);
   }

   public void uploadFile(String fieldName, String fileName, String fileContents) {
      uploadFile(fieldName, fileName, new ByteArrayInputStream(fileContents.getBytes()));
   }

   public void verifyNotesLink() {
      assertLinkPresentWithKey("note.create");
   }

   public void completeCurrentTask() {
      assertOnTaskPage();
      setWorkingForm("completion");
      submit();
   }

   public void assertImageInTable(String imageName) {

   }

   public void editProperty(String propertyName, String propertyValue) throws UnsupportedEncodingException {
      StringBuffer link = new StringBuffer();
      link.append("/do/edit/properties?propertyName=").append(propertyName);
      link.append("&propertyValue=").append(propertyValue);
      link.append("&returnto=");
//      link.append(URLEncoder.encode(getDialog().getResponse().getURL().toString(), "ISO-8859-1"));
      gotoRelativeUrl(link.toString());
   }

   public static class TextInTablePredicate implements HTMLElementPredicate {
      public boolean matchesCriteria(Object object, Object criteria) {
         WebTable table = (WebTable) object;
         String textToMatch = (String) criteria;
         for (int i = 0; i < table.getRowCount(); i++) {
            for (int j = 0; j < table.getColumnCount(); j++) {
               TableCell cell = table.getTableCell(i, j);
               if (cell != null && cell.asText().indexOf(textToMatch) != -1) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public static class TextInLinkPredicate implements HTMLElementPredicate {
      public boolean matchesCriteria(Object htmlElement, Object criteria) {
         WebLink link = (WebLink) htmlElement;
         String textToMatchWithoutAmpersand = ((String) criteria).replaceAll("&", "");
//         Node item = link.getDOMSubtree().getAttributes().getNamedItem("accesskey");
//         if (item != null)
//         {
//            return item.getNodeValue().equals(String.valueOf(AccessKeyTransformer.getAccessKey((String) criteria)));
//         }
//         else
//         {
         return WebLink.MATCH_TEXT.matchesCriteria(htmlElement, textToMatchWithoutAmpersand);
//         }
      }
   }

   public static class ImageInLinkPredicate implements HTMLElementPredicate {
      public boolean matchesCriteria(Object htmlElement, Object criteria) {
         WebLink link = (WebLink) htmlElement;
         String imageNameToMatch = (String) ((Object[]) criteria)[0];
         String pathElement = (String) ((Object[]) criteria)[1];
         Node item = link.getDOMSubtree().getFirstChild();
         if (item != null &&
             "img".equalsIgnoreCase(item.getNodeName()) &&
             item.getAttributes().getNamedItem("src") != null) {
            return StringUtils.contains(item.getAttributes().getNamedItem("src").getNodeValue(), imageNameToMatch) &&
                   StringUtils.contains(link.getAttribute("href"), pathElement);
         } else {
            return false;
         }
      }
   }

@Override
public void assertFormElementNotPresentWithLabel(String formElementLabel) {
	// TODO Auto-generated method stub
	
}

@Override
public void assertFormElementPresentWithLabel(String formElementLabel) {
	// TODO Auto-generated method stub
	
}

@Override
public void assertOptionEquals(String selectName, String option) {
	// TODO Auto-generated method stub
	
}

@Override
public void assertOptionValuesEqual(String selectName, String[] expectedValues) {
	// TODO Auto-generated method stub
	
}

@Override
public void assertOptionValuesNotEqual(String selectName, String[] optionValues) {
	// TODO Auto-generated method stub
	
}

@Override
public void assertOptionsEqual(String selectName, String[] expectedOptions) {
	// TODO Auto-generated method stub
	
}

@Override
public void assertOptionsNotEqual(String selectName, String[] expectedOptions) {
	// TODO Auto-generated method stub
	
}

@Override
public void assertSubmitButtonValue(String buttonName, String expectedValue) {
	// TODO Auto-generated method stub
	
}

@Override
public void clickLinkWithTextAfterText(String linkText, String labelText) {
	// TODO Auto-generated method stub
	
}

@Override
public void dumpCookies(PrintStream stream) {
	// TODO Auto-generated method stub
	
}

@Override
public void dumpResponse() {
	// TODO Auto-generated method stub
	
}

@Override
public void dumpResponse(PrintStream stream) {
	// TODO Auto-generated method stub
	
}

@Override
public void dumpTable(String tableNameOrId, String[][] table) {
	// TODO Auto-generated method stub
	
}

@Override
public void dumpTable(String tableNameOrId, String[][] table, PrintStream stream) {
	// TODO Auto-generated method stub
	
}
}
