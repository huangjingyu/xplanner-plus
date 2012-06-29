package com.technoetic.xplanner.importer.spreadsheet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.technoetic.xplanner.importer.SpreadsheetStory;
import com.technoetic.xplanner.importer.SpreadsheetStoryFactory;
import com.technoetic.xplanner.importer.WrongImportFileSpreadsheetImporterException;

/**
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Dec 28, 2003
 * Time: 5:23:42 PM
 * To change this template use Options | File Templates.
 */
public class SpreadsheetStoryReader implements CookbookFields
{
   private ArrayList stories = new ArrayList();
   private Logger log = Logger.getLogger(SpreadsheetStoryReader.class);
   private SpreadsheetStoryFactory spreadsheetStoryFactory;

   public SpreadsheetStoryReader(SpreadsheetStoryFactory spreadsheetStoryFactory)
   {
      this.spreadsheetStoryFactory = spreadsheetStoryFactory;
   }

   public List readStories(SpreadsheetHeaderConfiguration headerConfiguration, InputStream input) throws IOException
   {
      HSSFSheet sheet = getWorksheet(input, headerConfiguration.getWorksheetName());
      headerConfiguration.setWorksheet(sheet);
      return readStories(headerConfiguration, sheet);
   }

   public HSSFSheet getWorksheet(InputStream input, String worksheetName)
      throws IOException
   {
      POIFSFileSystem fs;
      try
      {
         fs = new POIFSFileSystem(input);
      }
      catch (IOException e)
      {
         throw new WrongImportFileSpreadsheetImporterException("Bad spreadsheet file", e);
      }
      HSSFWorkbook wb = new HSSFWorkbook(fs);
      HSSFSheet sheet = wb.getSheet(worksheetName);
      if (sheet == null) {
         throw new MissingWorksheetException(worksheetName);
      }

      return sheet;
   }

   private List readStories(SpreadsheetHeaderConfiguration headerConfiguration, HSSFSheet sheet)
   {
      Iterator it = new StoryRowIterator(headerConfiguration, sheet);
      while (it.hasNext())
      {
         SpreadsheetStory spreadsheetStory = (SpreadsheetStory) it.next();
         stories.add(spreadsheetStory);
      }
      return stories;
   }

   private class StoryRowIterator implements Iterator
   {
      Iterator it;
      SpreadsheetStory nextSpreadsheetStory;
      SpreadsheetHeaderConfiguration headerConfiguration;

      public StoryRowIterator(SpreadsheetHeaderConfiguration headerConfiguration, HSSFSheet sheet)
      {
         this.headerConfiguration = headerConfiguration;
         it = sheet.rowIterator();
         it.next();
      }

      /**
       * TODO: *WARNING* Do not call multiple time. It implements a look ahead that does not take this case into account
       */
      public boolean hasNext()
      {
         boolean hasNext = it.hasNext();
         if (!hasNext) return false;
         nextSpreadsheetStory = readRow((HSSFRow) it.next());
         return nextSpreadsheetStory != null;
      }

      private SpreadsheetStory readRow(HSSFRow row)
      {
         try
         {
            String title = getCellStringValue(row, headerConfiguration.getStoryTitleColumnIndex());
            int priority = getCellIntValue(row, headerConfiguration.getStoryPriorityColumnIndex());
            if (StringUtils.isEmpty(title) && priority == 0)
               return null;
            String status = getCellStringValue(row, headerConfiguration.getStoryStatusColumnIndex());
            double estimate = getCellDoubleValue(row, headerConfiguration.getStoryEstimateColumnIndex());
            Date storyEndDate = getCellDateValue(row, headerConfiguration.getStoryEndDateColumnIndex());
            return spreadsheetStoryFactory.newInstance(storyEndDate,
                                                       title,
                                                       status,
                                                       estimate,
                                                       priority);
         }
         catch (RuntimeException e)
         {
            log.error("Error while reading row "+ row.getRowNum(), e);
            throw e;
         }
      }

      private String getCellStringValue(HSSFRow row, int column)
      {
         HSSFCell cell = row.getCell((short) column);
         if (cell == null) return "";
         return cell.getStringCellValue();
      }

      private int getCellIntValue(HSSFRow row, int column)
      {
         HSSFCell cell = row.getCell((short) column);
         if (cell == null) return 0;
         return (int) cell.getNumericCellValue();
      }

      private double getCellDoubleValue(HSSFRow row, int column)
      {
         HSSFCell cell = row.getCell((short) column);
         if (cell == null) return 0;
         return cell.getNumericCellValue();
      }

      private Date getCellDateValue(HSSFRow row, int column)
      {
         HSSFCell cell = row.getCell((short) column);
         if (cell == null) return null;
         return cell.getDateCellValue();
      }


      public Object next()
      {
         return nextSpreadsheetStory;
      }

      public void remove()
      {
      }
   }
}
