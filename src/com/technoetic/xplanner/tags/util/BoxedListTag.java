/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Jan 1, 2006
 * Time: 10:59:22 PM
 */
package com.technoetic.xplanner.tags.util;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletRequest;

import com.technoetic.xplanner.util.StringUtilities;

public class BoxedListTag extends BoxTag {
   private Map keyValues;
   public static final String NAME_STYLE_CLASS = "propertyName";
   public static final String VALUE_STYLE_CLASS = "propertyValue";

   protected void renderBody(StringBuffer results) {
      results.append("<ul>");
      if (bodyContent == null) {
         for (Iterator i = keyValues.keySet().iterator(); i.hasNext();) {
            renderRow(results, (String) i.next());
         }
      } else {
         results.append(bodyContent.getString());
      }
      results.append("</ul>");
   }

   private void renderRow(StringBuffer results, String name) {
      String key = StringUtilities.htmlEncode(name);
      Object rawValue = keyValues.get(key);
      results.append("<li>" + renderProperty(key, rawValue) + "</li>");
   }

   public static String renderProperty(String key, Object rawValue) {
      return span(key, NAME_STYLE_CLASS) + ": " + span(getStringValue(rawValue), VALUE_STYLE_CLASS);
   }

   private static String getStringValue(Object rawValue) {
      String value = "";
      if (rawValue instanceof String) {
         value = StringUtilities.htmlEncode((String) rawValue);
      } else if (rawValue instanceof String[]) {
         String[] values = (String[]) rawValue;
         if (values.length > 1) value = "{";
         for (int i = 0; i < values.length; i++) {
            value += StringUtilities.htmlEncode(values[i]);
            if (i < values.length - 1) {
               value += ", ";
            }
         }
         if (values.length > 1) value += "}";
      }
      return value;
   }

   private static String span(String key, String styleClass) {return "<span class=\"" + styleClass + "\">"+key + "</span>";}

   public static Map getRequestAttributeMap(ServletRequest request) {
      Map map = new TreeMap();
      Enumeration attributeNames = request.getAttributeNames();
      while (attributeNames.hasMoreElements()) {
         String name = (String) attributeNames.nextElement();
         map.put(name, StringUtilities.htmlEncode(request.getAttribute(name).toString()));
      }
      return map;
   }

   public void setKeyValues(Map keyValues) {
      this.keyValues = keyValues;
   }
}