package com.technoetic.xplanner.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

import com.technoetic.xplanner.XPlannerProperties;

public class PropertyEqualTag extends TagSupport {
    private XPlannerProperties properties = new XPlannerProperties();

   private String key;
    private String value;

    public int doStartTag() throws JspException {
        return StringUtils.equals(properties.getProperty(key), value)
                ? EVAL_BODY_INCLUDE : SKIP_BODY;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
