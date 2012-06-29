/*
 * Created by IntelliJ IDEA.
 * User: Jacq
* Date: Aug 28, 2004
 * Time: 11:47:00 PM
 */
package com.technoetic.xplanner.acceptance.web;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.classic.Session;
import org.xml.sax.SAXException;


public interface XPlannerWebTester extends WebTester {
    String DEFAULT_PASSWORD = "password";
    String PROJECT_PAGE = "project.jsp";
    String ITERATION_PAGE = "iteration.jsp";
    String STORY_PAGE = "userstory.jsp";
    String TASK_PAGE = "task.jsp";
    String FEATURE_PAGE = "feature.jsp";
    String MAIN_TABLE_ID = "objecttable";


    String addFeature(String name, String description);

    void addNote(String subject, String body, String authorName);

    void addNote(String subject, String body, String authorName, String filename);

   String addProject(String projectName, String description);

    String addTask(String name, String acceptorName, String description, String estimatedHours);
    String addTask(String name, String acceptorName, String description, String estimatedHours, String disposition);

    void setTimeEntry(int index, int durationInHours, String firstPersonInitials);
    void setTimeEntry(int index, int startHourOffset, int endHourOffset, String firstPersonInitials);
    void setTimeEntry(int index,
                      int startTime, int endTime, String firstPersonInitials, String secondPersonInitials);

    String addUserStory(String storyName, String storyDescription, String estimatedHours, String orderNo);

    void assertLinkPresentWithKey(String key);

    void assertLinkNotPresentWithKey(String key);

    void assertOnFeaturePage();

    void assertOnMoveContinueStoryPage(String name);

    void assertOnMoveContinueTaskPage(String name);

    void assertOnPage(String page);

   void assertOnProjectPage();

    void assertOnStoryPage();

    void assertOnStoryPage(String storyName);

    void assertOnTaskPage();

    void assertOnTopPage();

    void assertOptionListed(String selectName, String option);

    void assertOptionNotListed(String selectName, String option);

    void clickContinueLinkInRowWithText(String name);

    void clickEnterTimeLinkInRowWithText(String name);

    void clickDeleteLinkForRowWithText(String text);

    void clickEditLinkInRowWithText(String text);

    void clickEditTimeImage();

    void clickImageLinkInNoteWithSubject(String imageName, String subject);

    void clickImageLinkInTableForRowWithText(String imageName, String tableId, String text);

    void clickLinkWithKey(String key);

    String dateStringForNDaysAway(int daysFromToday);

    String dateTimeStringForNHoursAway(int hoursFromNow);

    void deleteNoteWithSubject(String subject);

    void deleteObjects(Class clazz, String attribute, String value);

    void deleteProject(String name) throws Exception;

    void editNoteWithSubject(String subject);

    String getCurrentPageObjectId();

    String getIdFromLinkWithText(String linkText);

    int[] getRowNumbersWithText(String tableId, String text) throws SAXException;

    Session getSession() throws HibernateException;

    String getXPlannerLoginId();

    String getXPlannerLoginPassword();

    void gotoPage(String operation, String objectType, int oid) ;

   void gotoProjectsPage();

    void gotoRelativeUrl(String relativeUrl);

    void login();

    void login(String user, String password);

    void logout() throws Exception;

   void changeLocale(String language) throws Exception;

    void releaseSession() throws HibernateException, SQLException;

    void setFormElementWithLabel(String formElementLabel, String value);

    void verifyNotesLink();

    void assertCellTextForRowWithTextAndColumnKeyEquals(String tableId, String rowName, String key, String text);

    void assertCellTextForRowWithTextAndColumnNameEquals(String tableId,
                                                         String rowName,
                                                         String columnName,
                                                         String text);


    void assertCellNumberForRowWithTextAndColumnNameEquals(String tableId,
                                                           String rowName,
                                                           String columnName,
                                                           Number num);
    void assertCellNumberForRowWithTextAndColumnKeyEquals(String tableId, String rowName, String key, Number number);

    void assertCellTextForRowIndexAndColumnKeyContains(String tableId, int rowIndex, String columnName,
                                                       String expectedCellText);

    void assertCellTextForRowIndexAndColumnKeyEquals(String tableId, int rowIndex, String columnKey,
                                                     String expectedCellText);

    void assertActualHoursColumnPresent();

   //DEBT: Should be extracted in its own helper class
    void setUpSmtpServer() throws Exception;
    void tearDownSmtpServer();

    void tearDown() throws Exception;

    void assertEmailNotificationMessage(String subject, String[] recipients, String from, List bodyElements)
        throws InterruptedException;

    void assertNoEmailMessage(String subject,
                              String[] recipients,
                              String from,
                              List bodyElements)
        throws InterruptedException;

    void uploadFile(String fieldName, String fileName, InputStream fileContents);

    void assignRoleOnProject(String projectName, String roleName) throws SAXException;

   void completeCurrentTask();

   void editProperty(String propertyName, String propertyValue) throws UnsupportedEncodingException;

   void moveCurrentDay(int days)
         throws UnsupportedEncodingException;

   void executeTask(String taskExecutorUrl)
         throws UnsupportedEncodingException;

   void resetTime() throws UnsupportedEncodingException;
}