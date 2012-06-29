/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: May 20, 2006
 * Time: 6:05:05 AM
 */
package com.technoetic.xplanner.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassUtil {
  public static Object getFieldValue(Object object, String fieldName) throws Exception {
    Field field = getField(object, fieldName);
    field.setAccessible(true);
    return field.get(object);
  }

  public static void setFieldValue(Object object, String fieldName, Object value) throws Exception {
    Field field = getField(object, fieldName);
    field.setAccessible(true);
    field.set(object, value);
  }

  private static Field getField(Object object, String fieldName) throws NoSuchFieldException {
    Map allFields = getAllFieldByNames(object.getClass());
    Field field = (Field) allFields.get(fieldName);
    if (field == null) throw new NoSuchFieldException(fieldName + " is not a field of " + object.getClass());
    return field;
  }

  public static List getAllFieldNames(Object object) throws Exception {
     Map allFields = getAllFieldByNames(object.getClass());
     return new ArrayList(allFields.keySet());
  }

  public static List getAllFields(Object object) throws Exception {
     Map allFields = getAllFieldByNames(object.getClass());
     return new ArrayList(allFields.values());
  }

  public static Map getAllFieldByNames(Class theClass) {
     Map fields;
     Class superclass = theClass.getSuperclass();
     if (superclass != Object.class) {
       fields = getAllFieldByNames(superclass);
     }
     else
       fields = new HashMap();
    fields.putAll(getClassFieldByNames(theClass));
    return fields;
  }

  private static Map getClassFieldByNames(Class theClass) {
     Field[] fields = theClass.getDeclaredFields();
     Map fieldNames = new HashMap(fields.length);
     for (int i = 0; i < fields.length; i++) {
        Field field = fields[i];
        field.setAccessible(true);
        fieldNames.put(field.getName(), field);
     }
     return fieldNames;
  }

}