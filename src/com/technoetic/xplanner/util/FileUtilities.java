/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: May 12, 2005
 * Time: 11:39:17 PM
 */
package com.technoetic.xplanner.util;

import java.io.InputStream;

public class FileUtilities {
   public static InputStream getFileFromClassPath(String filePath) {
      return FileUtilities.class.getClassLoader().getResourceAsStream(filePath);
   }
}