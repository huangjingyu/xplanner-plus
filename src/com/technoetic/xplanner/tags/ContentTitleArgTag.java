package com.technoetic.xplanner.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class ContentTitleArgTag extends BodyTagSupport {
    private Object value;

    public int doEndTag() throws JspException {
        ContentTitleTag tag = (ContentTitleTag)findAncestorWithClass(this, ContentTitleTag.class);
        if (value != null) {
            tag.addTitleArgument(value);
        } else {
            tag.addTitleArgument(getBodyContent().getString());
        }
        return EVAL_PAGE;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void release() {
        super.release();
        value = null;
    }
}

