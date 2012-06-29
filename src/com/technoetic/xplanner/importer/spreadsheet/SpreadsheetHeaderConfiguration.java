package com.technoetic.xplanner.importer.spreadsheet;

import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.technoetic.xplanner.importer.MissingColumnHeaderSpreadsheetImporterException;

/**
 * Created by IntelliJ IDEA.
 * User: tkmower
 * Date: Apr 1, 2005
 * Time: 12:17:53 AM
 */
public class SpreadsheetHeaderConfiguration
{
   private SpreadsheetHeaderReader headerReader;

   public static final String DEFAULT_WORKSHEET_NAME = "Features";

   private String titleHeader;
   private String endDateHeader;
   private String priorityHeader;
   private String statusHeader;
   private String estimateHeader;
   private String worksheetName = DEFAULT_WORKSHEET_NAME;

   public SpreadsheetHeaderConfiguration()
   {
      super();
   }

   public SpreadsheetHeaderConfiguration(String titleHeader,
                                         String endDateHeader,
                                         String priorityHeader,
                                         String statusHeader,
                                         String estimateHeader)
   {
      this.titleHeader = titleHeader;
      this.endDateHeader = endDateHeader;
      this.priorityHeader = priorityHeader;
      this.statusHeader = statusHeader;
      this.estimateHeader = estimateHeader;
   }

   public void setTitleHeader(String titleHeader)
   {
      this.titleHeader = titleHeader;
   }

   public void setEndDateHeader(String endDateHeader)
   {
      this.endDateHeader = endDateHeader;
   }

   public void setPriorityHeader(String priorityHeader)
   {
      this.priorityHeader = priorityHeader;
   }

   public void setStatusHeader(String statusHeader)
   {
      this.statusHeader = statusHeader;
   }

   public void setEstimateHeader(String estimateHeader)
   {
      this.estimateHeader = estimateHeader;
   }

   public int getStoryTitleColumnIndex()
   {
      return getRequiredColumnIndex(titleHeader);
   }

   public int getStoryEstimateColumnIndex()
   {
      return headerReader.getColumnIndex(estimateHeader);
   }

   public int getStoryEndDateColumnIndex()
   {
      return getRequiredColumnIndex(endDateHeader);
   }

   public int getStoryPriorityColumnIndex()
   {
      return getRequiredColumnIndex(priorityHeader);
   }

   public int getStoryStatusColumnIndex()
   {
      return headerReader.getColumnIndex(statusHeader);
   }

   public void setWorksheet(HSSFSheet worksheet)
   {
      headerReader = new SpreadsheetHeaderReader();
      headerReader.setWorksheet(worksheet);
   }

   private int getRequiredColumnIndex(String header)
   {
      int columnIndex = headerReader.getColumnIndex(header);
      if (columnIndex == -1)
         throw new MissingColumnHeaderSpreadsheetImporterException(header);
      return columnIndex;
   }

   public void setWorksheetName(String worksheetName) {
      this.worksheetName = worksheetName;
   }

   public String getWorksheetName() {
      return worksheetName;
   }
}
