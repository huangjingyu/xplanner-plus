package com.technoetic.xplanner.forms;

import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.MessageResources;

import com.technoetic.xplanner.format.DecimalFormat;

public abstract class AbstractEditorForm extends ActionForm {
   private String action;
   private String oid;
   private boolean merge;
   private int id;

   protected static SimpleDateFormat dateTimeConverter;
   protected static SimpleDateFormat dateConverter;
   protected static DecimalFormat decimalConverter;
   protected static NumberFormat numberConverter;


   @Override
public void reset(ActionMapping mapping, HttpServletRequest request) {
      super.reset(mapping, request);
      action = null;
      oid = null;
      merge = false;
      id = 0;
   }

   public String getAction() {
      return action;
   }

   public void setAction(String action) {
      this.action = action;
   }

   public boolean isSubmitted() {
      return isPresent(action);
   }

   public String getMode() {
      return oid == null ? "create" : "modify";
   }

   public void setOid(String oid) {
      this.oid = oid;
   }

   public String getOid() {
      return oid;
   }

   public static void error(ActionErrors errors, String key) {
      errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(key));
   }

   public static void error(ActionErrors errors, String key, Object[] values) {
      errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(key, values));
   }

   public static boolean isPresent(String value) {
      return value != null && !value.equals("") && !value.equals("null");
   }

   public static void require(ActionErrors errors, String value, String msgkey) {
      if (!isPresent(value)) {
         error(errors, msgkey);
      }
   }

   public static void require(ActionErrors errors, Date value, String msgkey) {
      if (value.getTime() == 0) {
         error(errors, msgkey);
      }
   }

   public static void require(ActionErrors errors, int value, String msgkey) {
      if (value == 0) {
         error(errors, msgkey);
      }
   }

   public static void require(ActionErrors errors, boolean valid, String msgkey) {
      if (!valid) {
         error(errors, msgkey);
      }
   }

   public static void require(ActionErrors errors, FormFile value, String msgkey) {
      if (value == null || value.getFileSize() == 0) {
         error(errors, msgkey);
      }
   }

   public static void require(ActionErrors errors, byte[] value, String msgkey) {
      if (value == null || value.length == 0) {
         error(errors, msgkey);
      }
   }

   public void setMerge(boolean merge) {
      this.merge = merge;
   }

   public boolean isMerge() {
      return merge;
   }

   public void setId(int id) {
      this.id = id;
   }

   public int getId() {
      return id;
   }

   public static MessageResources getResources(HttpServletRequest request) {
	   MessageResources resources = (MessageResources) request.getAttribute(Globals.MESSAGES_KEY);
	   if(resources==null){
		   resources = (MessageResources) request.getSession().getAttribute(Globals.MESSAGES_KEY);
	   }
	   if(resources == null){
		   resources = (MessageResources) request.getSession().getServletContext().getAttribute(Globals.MESSAGES_KEY);
	   }
	   
	return resources;
   }

   public static void ensureSize(List<String> list, int size) {
      if (size > list.size()) {
         for (int i = list.size(); i < size; i++) {
            list.add(null);
         }
      }
   }

   public static void initConverters(HttpServletRequest request) {
      if (dateTimeConverter == null) {
         String format = getResources(request).getMessage("format.datetime");
         dateTimeConverter = new SimpleDateFormat(format);
      }

      if (dateConverter == null) {
         String format = getResources(request).getMessage("format.date");
         dateConverter = new SimpleDateFormat(format);
      }

      if (decimalConverter == null) {
         decimalConverter = new DecimalFormat(request);
      }

      if (numberConverter == null) {
         numberConverter = NumberFormat.getIntegerInstance();
      }
   }

   public static Date convertToDate(String date, String errorMessageKey, ActionErrors errors) {
      return (Date) convert(date, errorMessageKey, errors, dateConverter);
   }

   public static Date convertToDateTime(String date, String errorMessageKey, ActionErrors errors) {
      return (Date) convert(date, errorMessageKey, errors, dateTimeConverter);
   }

   public static Number convertToInt(String integer, String errorMessageKey, ActionErrors errors) {
      return (Number) convert(integer, errorMessageKey, errors, numberConverter);
   }

   public static Number convertToInt(String integer) {
      return (Number) convert(integer, numberConverter);
   }

   public static Object convert(String stringValue, Format format) {
      if (isPresent(stringValue))
         try {
            return format.parseObject(stringValue);
         } catch (ParseException ex) {
            //don't do anything
         }
      return null;
   }

   public static Object convert(String stringValue, String errorMessageKey, ActionErrors errors, Format format) {
      Object o = convert(stringValue, format);
      if (o == null && isPresent(stringValue))
         errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(errorMessageKey));
      return o;
   }
}
