package com.technoetic.xplanner.importer.spreadsheet;

/*
 * $Header$
 * $Revision: 540 $
 * $Date: 2005-06-07 15:03:50 +0300 (Вт, 07 июн 2005) $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */

import java.io.IOException;
import java.io.InputStream;

import com.technoetic.xplanner.importer.BaseTestCase;
import com.technoetic.xplanner.importer.SpreadsheetStoryFactory;
import com.technoetic.xplanner.importer.util.IOStreamFactory;

public class TestSpreadsheet extends BaseTestCase
{
   Spreadsheet spreadsheet;

   public void testOpen() throws Exception
   {
      String path = "jkhjhk:\\No-such path";
      spreadsheet = new Spreadsheet(new IOStreamFactory()
      {
         public InputStream newInputStream(String path) throws IOException
         {
            throw new IOException();
         }
      }, new SpreadsheetStoryFactory());

      spreadsheet.open(path);
   }
}