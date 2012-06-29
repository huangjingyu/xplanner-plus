package com.technoetic.xplanner.importer.spreadsheet;

import com.technoetic.xplanner.importer.SpreadsheetImporterException;

/**
 * Created by IntelliJ IDEA.
 * User: tkmower
 * Date: Nov 15, 2005
 * Time: 11:34:44 AM
 */
public class MissingWorksheetException extends SpreadsheetImporterException {
   private String worksheetName;

   public MissingWorksheetException(String worksheetName) {
      super("Could not find worksheet named " + worksheetName);
      this.worksheetName = worksheetName;
   }

   public String getWorksheetName() {
      return worksheetName;
   }
}
