package com.technoetic.xplanner.util;

public class ToStringUtils {
   public static String arrayToString(Object[] array) {
      StringBuffer str = new StringBuffer();
      if (array.length > 1) str.append("{");
      for (int i = 0; i < array.length; i++) {
         if (i != 0) str.append(",");
         Object o = array[i];
         str.append(safeToString(o));
      }
      if (array.length > 1) str.append("}");
      return str.toString();
   }

   public static String safeToString(Object o) {
      return o == null ? "null" : o.toString();
   }
}
