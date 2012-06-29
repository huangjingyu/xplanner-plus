package com.technoetic.xplanner.filters;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.security.auth.Subject;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.technoetic.xplanner.format.PrintfFormat;
import com.technoetic.xplanner.security.PersonPrincipal;
import com.technoetic.xplanner.security.SecurityHelper;

public class ActivityLogFilterHelper {

   public static final String LOG_LINE_PATTERN = "%-20.20s %-10.10s %-14.14s %-25.25s %-6.6s %-12.12s %s";

   public static final String ACTION_START = "START";
   public static final String ACTION_END = "END";
   public static final String ACTION_PREFIX = "/do/";
   public static final String DATE_FORMAT = "MM/dd HH:mm:ss.SSSS";
   public static final String ACTION_PERIOD_PATTERN = "{0,number,#.####}";
   public static final String NO_USER_MSG = "";
   public static final String NO_QUERY_MSG = "";

   private String userId = null;
   private String remoteAddr = null;
   private String actionName = null;
   private String queryString = null;
   private Date startDate = null;
   private Date endDate = null;

   public void doHelperSetUp(ServletRequest request){
      HttpServletRequest httpRequest = (HttpServletRequest)request;
      userId = getUserId(httpRequest);
      remoteAddr = httpRequest.getRemoteAddr();
      actionName = getActionName(httpRequest);
      queryString = getQueryString(httpRequest);
   }

   public String getStartLogRecord() {
      startDate = new Date();
      Object[] elements = new Object[] {
            getFormatedTime(startDate),
            userId,
            remoteAddr,
            actionName,
            ACTION_START,
            "",
            queryString
      };
      return getLogLine(elements);
   }

   public String getEndLogRecord() {
      endDate = new Date();
      Object[] elements = new Object[]{
            getFormatedTime(endDate),
            userId,
            remoteAddr,
            actionName,
            ACTION_END,
            getActionPeriod(),
            queryString
      };
      return getLogLine(elements);
   }

   public String getFormatedStartDate() {
      return getFormatedTime(startDate);
   }

   public String getFormatedEndDate() {
      return getFormatedTime(endDate);
   }

   private String getFormatedTime(Date date) {
      SimpleDateFormat formater = new SimpleDateFormat(DATE_FORMAT);
      return formater.format(date);
   }

   private String getUserId(HttpServletRequest httpRequest) {
      Subject subject = (Subject)httpRequest.getSession().getAttribute(SecurityHelper.SECURITY_SUBJECT_KEY);
      if(subject!=null){
         Set pricipalSet = subject.getPrincipals();
         Iterator iterator = pricipalSet.iterator();
         return ((PersonPrincipal)iterator.next()).getPerson().getUserId();
      } else {
         return NO_USER_MSG;
      }
   }

   private String getQueryString(HttpServletRequest httpRequest) {
      if(httpRequest.getQueryString() != null){
         return httpRequest.getQueryString();
      } else {
         return NO_QUERY_MSG;
      }
   }

   private String getActionName(HttpServletRequest httpRequest) {
      String url = httpRequest.getRequestURI();
      int index = url.indexOf(ACTION_PREFIX);
      return url.substring(index + ACTION_PREFIX.length());
   }


   private String getLogLine(Object[] elements){
      PrintfFormat formater = new PrintfFormat(LOG_LINE_PATTERN);
      return formater.sprintf(elements);
   }

   public String getActionPeriod() {
      double mseconds = endDate.getTime() - startDate.getTime();
      double seconds = mseconds/1000;
      Object[] param = new Object[]{new Double(seconds)};
      return MessageFormat.format(ACTION_PERIOD_PATTERN, param);
   }

}
