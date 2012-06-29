package com.technoetic.xplanner.tags;

import java.text.MessageFormat;
import java.util.ArrayList;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.struts.Globals;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.util.MessageResources;

public class ContentTitleTag extends BodyTagSupport {
    private ArrayList titleArguments;
    private String title;
    private String titleKey;

    public int doStartTag() throws JspException {
        titleArguments = new ArrayList();
        return super.doStartTag();
    }

    public int doEndTag() throws JspException {
        String formattedTitle = null;
        if (title == null) {
            if (titleKey != null) {
                MessageResources resources = (MessageResources)pageContext.findAttribute(Globals.MESSAGES_KEY);
                if (resources == null) {
                    throw new JspException("no resource bundle in request");
                }
                formattedTitle = resources.getMessage(TagUtils.getInstance().getUserLocale(pageContext, ""), titleKey, titleArguments.toArray());
            } else {
                formattedTitle = getBodyContent().getString().trim();
            }
        } else {
            formattedTitle = MessageFormat.format(title, titleArguments.toArray());
        }
        ContentTag tag = (ContentTag)findAncestorWithClass(this, ContentTag.class);
        tag.putAttribute("title", formattedTitle);
        return super.doEndTag();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTitleKey(String titleKey) {
        this.titleKey = titleKey;
    }

    public void addTitleArgument(Object value) {
        titleArguments.add(value);
    }

    public void release() {
        title = null;
        titleKey = null;
        titleArguments = null;
        super.release();
    }
}
