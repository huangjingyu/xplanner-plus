package com.technoetic.xplanner.format;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

public class DateFormat extends AbstractFormat {
    private java.text.DateFormat format = null;

    public DateFormat(HttpServletRequest request) {
        String format = getFormat(request, "format.date");
        if (format != null) {
            this.format = new SimpleDateFormat(format);
        } else {
            this.format = java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT, request.getLocale());
        }
    }

    public String format(Date value) {
        return format.format(value);
    }

    public Date parse(String value) throws ParseException {
        return format.parse(value);
    }

    public static String format(PageContext pageContext, Date value) {
        return new DateFormat((HttpServletRequest)pageContext.getRequest()).format(value);
    }

    public static String format(HttpServletRequest request, Date value) {
        return new DateFormat(request).format(value);
    }

    public static Date parse(PageContext pageContext, String dateString) throws ParseException {
        return new DateFormat((HttpServletRequest)pageContext.getRequest()).parse(dateString);
    }

}