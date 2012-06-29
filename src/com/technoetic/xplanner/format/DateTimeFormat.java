package com.technoetic.xplanner.format;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

public class DateTimeFormat extends AbstractFormat {
    private SimpleDateFormat formatter = null;

    public DateTimeFormat(HttpServletRequest request) {
        String format = getFormat(request, "format.datetime");
        if (format != null) {
            formatter = new SimpleDateFormat(format);
        } else {
            formatter = new SimpleDateFormat();
        }
    }

    public String format(Date value) {
        return value != null ? formatter.format(value) : "";
    }

    public Date parse(String date) throws ParseException {
        return formatter.parse(date);
    }

    public static String format(PageContext pageContext, Date value) {
        return new DateTimeFormat(((HttpServletRequest)pageContext.getRequest())).format(value);
    }

    public static String format(HttpServletRequest request, Date value) {
        return new DateTimeFormat(request).format(value);
    }
}