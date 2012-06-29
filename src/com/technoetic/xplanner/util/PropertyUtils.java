/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Dec 23, 2005
 * Time: 4:10:03 PM
 */
package com.technoetic.xplanner.util;

public class PropertyUtils extends org.apache.commons.beanutils.PropertyUtils {
   public static void setProperty(Object object, String property, Object value) {
      try {
         org.apache.commons.beanutils.PropertyUtils.setProperty(object, property, value);
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }
   public static Object getProperty(Object object, String property) {
      try {
         return org.apache.commons.beanutils.PropertyUtils.getProperty(object, property);
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

}