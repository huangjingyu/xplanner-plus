package com.technoetic.xplanner.format;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.util.MessageResources;

import com.technoetic.xplanner.util.LocaleUtil;

public class AbstractFormat {

    public static String getFormat(HttpServletRequest request, String key) {
        HttpSession session = request.getSession();
       Locale locale = LocaleUtil.getLocale(session);
       MessageResources resources = (MessageResources)request.getAttribute(Globals.MESSAGES_KEY);
       if(resources==null) {
    	   resources = (MessageResources) session.getServletContext().getAttribute(Globals.MESSAGES_KEY);
       }
        return resources.getMessage(locale, key);
    }

}