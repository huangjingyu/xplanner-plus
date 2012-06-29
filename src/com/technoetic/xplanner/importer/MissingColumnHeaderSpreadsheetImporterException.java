package com.technoetic.xplanner.importer;

public class MissingColumnHeaderSpreadsheetImporterException extends SpreadsheetImporterException
{
   String columnName;

   public String getColumnName()
   {
      return columnName;
   }

   public MissingColumnHeaderSpreadsheetImporterException(String columnName)
   {
      super("Missing column '" + columnName + "'");
      this.columnName = columnName;
   }


}
