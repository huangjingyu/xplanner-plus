package com.technoetic.xplanner.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.technoetic.xplanner.XPlannerProperties;

public class PropertyTag extends TagSupport {
    private String key;
    private XPlannerProperties properties = new XPlannerProperties();

   public int doEndTag() throws JspException {
       try {
           pageContext.getOut().print(properties.getProperty(key));
       } catch (IOException ex) {
           throw new JspException("IO error");
       }
       return EVAL_PAGE;
   }

    public void setKey(String key) {
        this.key = key;
    }
}