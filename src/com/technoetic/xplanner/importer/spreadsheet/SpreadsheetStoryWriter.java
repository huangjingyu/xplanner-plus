package com.technoetic.xplanner.importer.spreadsheet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.technoetic.xplanner.importer.SpreadsheetStory;

/*
 * $Header$
 * $Revision: 540 $
 * $Date: 2005-06-07 07:03:50 -0500 (Tue, 07 Jun 2005) $
 *
 * Copyright (c) 1999-2002 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */

public class SpreadsheetStoryWriter implements CookbookFields
{
   OutputStream output;
   public static final String END_DATE_HEADER = "Iteration End Date";
   public static final String TITLE_HEADER = "Feature/Story Title";
   public static final String STATUS_HEADER = "Status";
   public static final String PRIORITY_HEADER = "Priority  (1 thru n)";
   public static final String ESTIMATE_HEADER = "Work Unit Estimate";

   public SpreadsheetStoryWriter(OutputStream stream)
   {
      output = stream;
   }

   public void writeStories(List stories) throws IOException
   {
      //assert stories != null;

      HSSFWorkbook wb = new HSSFWorkbook();
      HSSFSheet sheet = wb.createSheet("Features");
      writeHeader(sheet);
      for (int i = 0; i < stories.size(); i++)
      {
         SpreadsheetStory spreadsheetStory = (SpreadsheetStory) stories.get(i);
         writeStory(sheet, spreadsheetStory, i + 1);
      }
      wb.write(output);
      output.close();
   }

   private void writeStory(HSSFSheet sheet, SpreadsheetStory spreadsheetStory, int i)
   {
      HSSFRow row = sheet.createRow(i);
      setCellValue(row, STORY_END_DATE_COL, spreadsheetStory.getEndDate());
      setCellValue(row, TITLE_COL, spreadsheetStory.getTitle());
      setCellValue(row, STATUS_COL, spreadsheetStory.getStatus());
      setCellValue(row, STORY_PRIORITY_COL, spreadsheetStory.getPriority());
      setCellValue(row, ESTIMATE_NUMBER_COL, spreadsheetStory.getEstimate());
   }

   private void writeHeader(HSSFSheet sheet)
   {
      HSSFRow row = sheet.createRow(0);
      setCellValue(row, STORY_END_DATE_COL, END_DATE_HEADER);
      setCellValue(row, TITLE_COL, TITLE_HEADER);
      setCellValue(row, STATUS_COL, STATUS_HEADER);
      setCellValue(row, STORY_PRIORITY_COL, PRIORITY_HEADER);
      setCellValue(row, ESTIMATE_NUMBER_COL, ESTIMATE_HEADER);
   }

   private void setCellValue(HSSFRow row, int col, Date date)
   {
      HSSFCell cell = row.createCell((short) col);
      cell.setCellValue(date);
   }

   private void setCellValue(HSSFRow row, int col, int value)
   {
      HSSFCell cell = row.createCell((short) col);
      cell.setCellValue(value);
   }

   private void setCellValue(HSSFRow row, int col, double value)
   {
      HSSFCell cell = row.createCell((short) col);
      cell.setCellValue(value);
   }

   private void setCellValue(HSSFRow row, int col, String value)
   {
      HSSFCell cell = row.createCell((short) col);
      cell.setCellValue(value);
   }
}
