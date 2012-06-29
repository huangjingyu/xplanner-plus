package com.technoetic.xplanner.importer.spreadsheet;

/**
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Dec 28, 2003
 * Time: 5:23:51 PM
 * To change this template use Options | File Templates.
 */

import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.technoetic.xplanner.importer.BaseTestCase;

public class TestSpreadsheetHeaderReader extends BaseTestCase
{
   public void testSpreadsheetHeader() throws Exception
   {
      InputStream stream = TestSpreadsheetHeaderReader.class.getResourceAsStream("/data/Cookbook.xls");
      assertNotNull(stream);

      SpreadsheetHeaderReader headerReader = new SpreadsheetHeaderReader();
      POIFSFileSystem fs = new POIFSFileSystem(stream);
      HSSFWorkbook wb = new HSSFWorkbook(fs);
      headerReader.setWorksheet(wb.getSheet("Features"));

      assertEquals(9, headerReader.getColumnIndex("Status"));
      assertEquals(4, headerReader.getColumnIndex("Feature/Story Title"));
   }

}