package com.technoetic.xplanner.tags;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;

public class FormatDateTag extends TagSupport {
    private static HashMap dateFormatters = new HashMap();
    private final static String DEFAULT_DATE_FORMAT = "EEE MMM dd k:mm:ss z";
    private String property;
    private String scope;
    private String name;
    private String format;
    private String formatKey;
    private String locale;
    private Date value;
    private String bundle;

    public int doStartTag() throws JspException {
        Object object = null;
        if (value == null) {
            object = RequestUtils.lookup(pageContext, name, property, scope);
        } else {
            object = value;
        }

        if (object == null) {
            return (SKIP_BODY);  // Nothing to output
        }

        Date dateValue = null;
        if (object instanceof Date) {
            dateValue = (Date)object;
        } else {
            throw new JspException("date value must be a java.util.Date");
        }

        String dateFormat = null;
        if (format != null) {
            dateFormat = format;
        } else if (formatKey != null) {
            dateFormat = RequestUtils.message(pageContext, bundle, locale, formatKey);
        } else {
            dateFormat = DEFAULT_DATE_FORMAT;
        }

        DateFormat dateFormatter = (DateFormat)dateFormatters.get(dateFormat);
        if (dateFormatter == null) {
            dateFormatter = new SimpleDateFormat(dateFormat, pageContext.getRequest().getLocale());
            // This should really be keyed off of both format and locale
            dateFormatters.put(dateFormat, dateFormatter);
        }

        ResponseUtils.write(pageContext, dateFormatter.format(dateValue));

        return (SKIP_BODY);
    }


    public void setProperty(String property) {
        this.property = property;
    }

    public String getProperty() {
        return property;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getScope() {
        return scope;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    public void setFormatKey(String formatKey) {
        this.formatKey = formatKey;
    }

    public String getFormatKey() {
        return formatKey;
    }

    public void setValue(java.util.Date value) {
        this.value = value;
    }

    public java.util.Date getValue() {
        return value;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public String getBundle() {
        return bundle;
    }
}
