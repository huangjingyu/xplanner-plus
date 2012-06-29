/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: May 11, 2004
 * Time: 12:06:32 AM
 */
package com.technoetic.xplanner.importer;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import junitx.framework.CallLog;

import com.technoetic.xplanner.importer.spreadsheet.Spreadsheet;
import com.technoetic.xplanner.importer.util.IOStreamFactory;

//TODO investigate having a default spring test configuration made of fake objects and have the tests override the beans they need to have control over

public class MockSpreadsheet extends Spreadsheet
{
   public List storiesReturn = Collections.EMPTY_LIST;
   public CallLog callLog = new CallLog();

   public MockSpreadsheet()
   {
      super(new IOStreamFactory(), new SpreadsheetStoryFactory());
   }

   public MockSpreadsheet(IOStreamFactory factory)
   {
      super(factory, new SpreadsheetStoryFactory());
   }

   public void open(String path)
   {
      callLog.addActualCallToCurrentMethod(path);
   }

   public void addStory(SpreadsheetStory spreadsheetStory)
   {
      callLog.addActualCallToCurrentMethod(spreadsheetStory);
   }

   public void setStories(List stories)
   {
      callLog.addActualCallToCurrentMethod(stories);
   }

   public List getStories()
   {
      return storiesReturn;
   }

   public void save() throws IOException
   {
      callLog.addActualCallToCurrentMethod();
   }

   public void verify()
   {
      callLog.verify();
   }

}