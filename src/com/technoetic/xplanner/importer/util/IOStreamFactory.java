/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: Apr 20, 2004
 * Time: 1:25:57 AM
 */
package com.technoetic.xplanner.importer.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOStreamFactory
{
   public InputStream newInputStream(String path) throws IOException
   {
      return new FileInputStream(path);
   }
}