package com.technoetic.xplanner.format;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

public class DecimalFormat extends AbstractFormat {
    private java.text.DecimalFormat formatter = null;
    private NumberFormat parser = null;

    public DecimalFormat(HttpServletRequest request) {
        this(request.getLocale(), getFormat(request, "format.decimal"));
    }

    public DecimalFormat(Locale locale, String format) {
        formatter = (java.text.DecimalFormat)NumberFormat.getNumberInstance(locale);
        if (format != null) {
            formatter.applyPattern(format);
        }
        parser = getParser(locale);
    }

    private NumberFormat getParser(Locale locale) {
        return java.text.DecimalFormat.getInstance(locale);
    }

    public String format(double value) {
        return formatter.format(value);
    }

    public double parse(String value) throws ParseException {
        value = value.trim();
        return parser.parse(value).doubleValue();
    }

    public static String format(PageContext pageContext, double value) {
        return format((HttpServletRequest)pageContext.getRequest(), value);
    }

    public static String format(HttpServletRequest request, double value) {
        return format(request, value, null);
    }

    public static String format(PageContext pageContext, double value, String zeroString) {
        return (value == 0.0 && zeroString != null) ? zeroString :
            new DecimalFormat((HttpServletRequest) pageContext.getRequest()).format(value);
    }

    public static String format(HttpServletRequest request, double value, String zeroString) {
        return (value == 0.0 && zeroString != null) ? zeroString :
                new DecimalFormat(request).format(value);
    }
}