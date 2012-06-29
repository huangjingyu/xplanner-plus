/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Mar 6, 2006
 * Time: 9:47:29 PM
 */
package com.technoetic.xplanner.util;

import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;

public class LocaleUtil {
   public static Locale getLocale(HttpSession session) {
      Locale locale = null;
      try {
          locale = (Locale)session.getAttribute(Globals.LOCALE_KEY);
      } catch (IllegalStateException e) {	// Invalidated session
          locale = null;
      }
      if (locale == null) {
          locale = Locale.getDefault();
      }
      return locale;
   }
}