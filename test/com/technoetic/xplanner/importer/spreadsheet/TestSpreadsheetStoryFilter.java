package com.technoetic.xplanner.importer.spreadsheet;

/**
 * Created by IntelliJ IDEA.
 * User: tkmower
 * Date: May 19, 2005
 * Time: 10:23:51 PM
 */

import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import com.technoetic.xplanner.importer.SpreadsheetStory;
import com.technoetic.xplanner.importer.SpreadsheetStoryFactory;
import com.technoetic.xplanner.testing.DateHelper;

public class TestSpreadsheetStoryFilter extends TestCase
{
   SpreadsheetStoryFilter filter;
   SpreadsheetStoryFactory storyFactory;
   public static final Date START_DATE = DateHelper.createDate(1942, 6, 31);
   public static final Date END_DATE = DateHelper.createDate(1942, 7, 10);

   protected void setUp() throws Exception
   {
      super.setUp();
      filter = new SpreadsheetStoryFilter(START_DATE, END_DATE);
      storyFactory = new SpreadsheetStoryFactory();
   }

   public void testFilterPass() throws Exception
   {
      Date storyEndDate = DateHelper.createDate(1942, 7, 8);
      validateFilterResult(storyEndDate, true);
   }

   public void testFilterFail() throws Exception
   {
      Date storyEndDate = DateHelper.createDate(1942, 9, 8);
      validateFilterResult(storyEndDate, false);
   }

   public void testFilterPassStartDate() throws Exception
   {
      Date storyEndDate = START_DATE;
      validateFilterResult(storyEndDate, true);
   }

   public void testFilterPassEndDate() throws Exception
   {
      Date storyEndDate = END_DATE;
      validateFilterResult(storyEndDate, true);
   }

   public void testFilterFailNull() throws Exception
   {
      Date storyEndDate = null;
      validateFilterResult(storyEndDate, false);
   }

   public void testFilterFailLastMinuteBefore() throws Exception
   {
      Calendar cal = Calendar.getInstance();
      cal.setTime(START_DATE);
      cal.add(Calendar.MINUTE, -1);
      Date storyEndDate = cal.getTime();
      validateFilterResult(storyEndDate, false);
   }

   public void testFilterFailFirstMinuteAfter() throws Exception
   {
      Calendar cal = Calendar.getInstance();
      cal.setTime(END_DATE);
      cal.add(Calendar.MINUTE, 1);
      Date storyEndDate = cal.getTime();
      validateFilterResult(storyEndDate, false);
   }

   public void testStartDateBeforeEndDate() throws Exception
   {
      Calendar cal = Calendar.getInstance();
      cal.setTime(END_DATE);
      cal.add(Calendar.MINUTE, 1);
      Date badStartDate = cal.getTime();

      try
      {
         filter = new SpreadsheetStoryFilter(badStartDate, END_DATE);
         fail("Should throw IllegalArgumentException");
      }
      catch (IllegalArgumentException e)
      {
      }
   }

   private void validateFilterResult(Date storyEndDate, boolean expectedResult)
   {
      SpreadsheetStory story = storyFactory.newInstance(storyEndDate, "SomeStroy", "", 4, 4);

      assertEquals("Incorrect filter result " + storyEndDate + ", " + filter, expectedResult, filter.matches(story));
   }

}