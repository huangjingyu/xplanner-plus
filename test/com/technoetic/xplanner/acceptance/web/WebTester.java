/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Aug 29, 2004
 * Time: 12:00:26 AM
 */
package com.technoetic.xplanner.acceptance.web;

import java.io.PrintStream;
import java.util.Date;

import net.sourceforge.jwebunit.api.ITestingEngine;
import net.sourceforge.jwebunit.util.TestContext;

import com.meterware.httpunit.TableCell;
import com.meterware.httpunit.WebLink;
public interface WebTester {
   void assertButtonNotPresent(String buttonId);
   void assertButtonPresent(String buttonId);
   void assertCheckboxNotSelected(String checkBoxName);
   void assertCheckboxSelected(String checkBoxName);
   void assertCookiePresent(String cookieName);
   void assertCookieValueEquals(String cookieName, String expectedValue);
   void assertElementNotPresent(String anID);
   void assertElementPresent(String anID);
   void assertFormElementEmpty(String formElementName);
   void assertFormElementEquals(String formElementName, String expectedValue);
   void assertFormElementNotPresent(String formElementName);
   void assertFormElementNotPresentWithLabel(String formElementLabel);
   void assertFormElementPresent(String formElementName);
   void assertFormElementPresentWithLabel(String formElementLabel);
   void assertFormNotPresent();
   void assertFormNotPresent(String nameOrID);
   void assertFormPresent();
   void assertFormPresent(String nameOrID);
   void assertFramePresent(String frameName);
   void assertKeyInTable(String tableSummaryOrId, String key);
   void assertKeyNotInTable(String tableSummaryOrId, String key);
   void assertKeyNotPresent(String key);
   void assertKeyPresent(String key);
   void assertKeyPresent(String key, Object arg);
   void assertKeysInTable(String tableSummaryOrId, String[] keys);
   void assertLinkNotPresent(String linkId);
   void assertLinkNotPresentWithImage(String imageFileName);
   void assertLinkNotPresentWithText(String linkText);
   void assertLinkNotPresentWithText(String linkText, int index);
   void assertLinkPresent(String linkId);
   void assertLinkPresentWithImage(String imageFileName);
   void assertLinkPresentWithText(String linkText);
   void assertLinkPresentWithText(String linkText, int index);
   void assertOptionEquals(String selectName, String option);
   void assertOptionValuesEqual(String selectName, String[] expectedValues);
   void assertOptionValuesNotEqual(String selectName, String[] optionValues);
   void assertOptionsEqual(String selectName, String[] expectedOptions);
   void assertOptionsNotEqual(String selectName, String[] expectedOptions);
   void assertRadioOptionNotPresent(String name, String radioOption);
   void assertRadioOptionNotSelected(String name, String radioOption);
   void assertRadioOptionPresent(String name, String radioOption);
   void assertRadioOptionSelected(String name, String radioOption);
   void assertSubmitButtonNotPresent(String buttonName);
   void assertSubmitButtonPresent(String buttonName);
   void assertSubmitButtonValue(String buttonName, String expectedValue);
//   void assertTableEquals(String tableSummaryOrId, ExpectedTable expectedTable);
   void assertTableEquals(String tableSummaryOrId, String[][] expectedCellValues);
   void assertTableNotPresent(String tableSummaryOrId);
   void assertTablePresent(String tableSummaryOrId);
//   void assertTableRowsEqual(String tableSummaryOrId, int startRow, ExpectedTable expectedTable);
   void assertTableRowsEqual(String tableSummaryOrId, int startRow, String[][] expectedCellValues);
   void assertTextInElement(String elementID, String text);
   void assertTextInTable(String tableSummaryOrId, String text);
   void assertTextInTable(String tableSummaryOrId, String[] text);
   void assertTextNotInElement(String elementID, String text);
   void assertTextNotInTable(String tableSummaryOrId, String text);
   void assertTextNotInTable(String tableSummaryOrId, String[] text);
   void assertTextNotPresent(String text);
   void assertTextPresent(String text);
   /**
    * @deprecated use {@link #assertKeyPresent(String)} instead
    * @param key
    */
   void assertTextPresentWithKey(String key);
   void assertTextNotPresentWithKey(String key);
   void assertTitleEquals(String title);
   void assertTitleEqualsKey(String titleKey);
   void assertWindowPresent(String windowName);
   void beginAt(String relativeURL);
   void checkCheckbox(String checkBoxName);
   void checkCheckbox(String checkBoxName, String value);
   void clickButton(String buttonId);
   void clickLink(String linkId);
   void clickLink(WebLink link);
   void clickLinkWithImage(String imageFileName);
   void clickLinkWithText(String linkText);
   void clickLinkWithText(String linkText, int index);
   void clickLinkWithTextAfterText(String linkText, String labelText);
   void dumpCookies();
   void dumpCookies(PrintStream stream);
   void dumpResponse();
   void dumpResponse(PrintStream stream);
   void dumpTable(String tableNameOrId, PrintStream stream);
   void dumpTable(String tableNameOrId, String[][] table);
   void dumpTable(String tableNameOrId, String[][] table, PrintStream stream);
   WebLink findLinkWithText(String text);
   ITestingEngine getDialog();
   String getMessage(String key);
   TestContext getTestContext();
   void gotoFrame(String frameName);
   void gotoPage(String url);
   void gotoRootWindow();
   void gotoWindow(String windowName);
   void reset();
   void selectOption(String selectName, String option);
   void setFormElement(String formElementName, String value);
   void setWorkingForm(String nameOrId);
   void submit();
   void submit(String buttonName);
   void uncheckCheckbox(String checkBoxName);
   void uncheckCheckbox(String checkBoxName, String value);

   TableCell getCell(String tableId, String columnName, int rowIndex);

   void assertLinkPresentWithImage(String imageName, String pathElement);

   void clickLinkWithImage(String imageName, String pathElement);

   void assertCellTextForRowWithTextAndColumnKeyOccurs(String tableId,
                                                       String rowName,
                                                       String columnName,
                                                       String text, int nbr);

   void assertCellNumberForRowWithKey(String tableId,
                                             String rowName,
                                             String key,
                                             Number val, int nbr);

   void assertCellNumberForRowWithTextAndColumnKeyOccurs(String tableId,
                                                         String rowName,
                                                         String columnName,
                                                         Number val, int nbr);

   boolean isKeyPresent(String key);
   boolean isTextPresent(String text);
   boolean isLinkPresentWithText(String linkText);
   boolean isLinkPresentWithKey(String key);

   Date getCurrentDate();

   int getCurrentDayIndex();
}