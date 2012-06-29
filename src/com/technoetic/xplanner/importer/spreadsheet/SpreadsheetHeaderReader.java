package com.technoetic.xplanner.importer.spreadsheet;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * Created by IntelliJ IDEA.
 * User: tkmower
 * Date: Mar 31, 2005
 * Time: 11:54:54 PM
 */
public class SpreadsheetHeaderReader
{
   private HSSFSheet sheet;

   public void setWorksheet(HSSFSheet worksheet)
   {
      sheet = worksheet;
   }

   public int getColumnIndex(String headerText)
   {
      HSSFRow row = sheet.getRow(0);

      for (short i = row.getFirstCellNum(); i < row.getLastCellNum(); ++i)
      {
         String stringCellValue = getTextForCell(row, i);
         if ((stringCellValue != null) && !StringUtils.isEmpty(headerText) && (headerText.equalsIgnoreCase(stringCellValue.trim())))
         {
            return i;
         }
      }
      return -1;
   }

   private String getTextForCell(HSSFRow row, short i)
   {
      HSSFCell cell = row.getCell(i);
      if (cell == null)
      {
         return null;
      }
      return cell.getStringCellValue();
   }
}
