package com.technoetic.xplanner.importer;

/*
 * $Header$
 * $Revision: 540 $
 * $Date: 2005-06-07 15:03:50 +0300 (Вт, 07 июн 2005) $
 *
 * Copyright (c) 1999-2002 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */


public class TestSpreadsheetStoryFactory extends BaseTestCase
{
   SpreadsheetStoryFactory factorySpreadsheet;

   public void testNewInstance_SimpleCreation() throws Exception
   {
      factorySpreadsheet = new SpreadsheetStoryFactory();
      SpreadsheetStory spreadsheetStory = factorySpreadsheet.newInstance("t", "C", 0);
      assertEquals("t", spreadsheetStory.getTitle());
      assertEquals("C", spreadsheetStory.getStatus());
   }

//   public void testNewInstance_NoCookbookId() throws Exception {
//      factory = new StoryFactory();
//      Story story = factory.newInstance(1, 0, 3, "t", "C");
//      assertEquals(1, story.getCookbookId());
//      story = factory.newInstance(1, 0, 3, "t", "C");
//      assertEquals(1, story.getCookbookId());
//      story = factory.newInstance(1, 10, 3, "t", "C");
//      assertEquals(10, story.getCookbookId());
//      story = factory.newInstance(1, 11, 3, "t", "C");
//      assertEquals(11, story.getCookbookId());
//   }

}