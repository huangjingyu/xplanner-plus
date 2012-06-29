package com.technoetic.xplanner.importer;

import java.util.Date;

/*
 * $Header$
 * $Revision: 540 $
 * $Date: 2005-06-07 07:03:50 -0500 (Tue, 07 Jun 2005) $
 *
 * Copyright (c) 1999-2002 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */

public class SpreadsheetStoryFactory
{
   public SpreadsheetStory newInstance(String title,
                                       String status,
                                       double estimate)
   {
      return new SpreadsheetStory(title, status, estimate);
   }

   public SpreadsheetStory newInstance(Date storyEndDate,
                                       String title,
                                       String status,
                                       double estimate,
                                       int priority)
   {
      return new SpreadsheetStory(storyEndDate, title, status, estimate, priority);
   }

   public SpreadsheetStory newInstance()
   {
      return new SpreadsheetStory("", "", 0);
   }

}
