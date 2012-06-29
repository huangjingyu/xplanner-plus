package com.technoetic.xplanner.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;

import org.apache.struts.taglib.tiles.InsertTag;
import org.apache.struts.tiles.DirectStringAttribute;

public class ContentTag extends InsertTag implements BodyTag {
    private BodyContent bodyContent;

    @Override
	public int doStartTag() throws JspException {
        if (PrintLinkTag.isInPrintMode(pageContext)) {
            definitionName = "tiles:print";
        } else if (definitionName == null || definitionName=="tiles:print") {
            definitionName = "tiles:default";
        }
        super.doStartTag();
        return EVAL_BODY_BUFFERED;
    }

    public void doInitBody() throws JspException {
        // empty
    }

    public void setBodyContent(BodyContent bodyContent) {
        this.bodyContent = bodyContent;
    }

    @Override
	public int doEndTag() throws JspException {
        putAttribute("body", new DirectStringAttribute(bodyContent.getString()));
        return super.doEndTag();
    }

    @Override
	public void release() {
        bodyContent = null;
        super.release();
    }
}
