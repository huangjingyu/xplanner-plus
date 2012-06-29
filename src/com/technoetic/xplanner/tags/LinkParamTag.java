package com.technoetic.xplanner.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;
import org.apache.struts.util.RequestUtils;

import com.technoetic.xplanner.util.LogUtil;

/**
 * Implements a custom tag to add parameter to a href request.<br/>
 * This tag is intended to be nested in a <code>hm:link</code> tag.
 *
 * Title:        BSquare
 * Description:  Bsquare Projects
 * Copyright:    Copyright (c) 2001
 * Company:      HubMethods
 * @author Eric Fesler
 * @version 1.0
 */

public class LinkParamTag extends BodyTagSupport {
    // ----------------------------------------------------- Logging
    Logger cat = LogUtil.getLogger();

    // ----------------------------------------------------- Instance variables
    /**
     * The name of the request parameter
     */
    private String id = null;

    /**
     * The value of the request parameter
     */
    private String value = null;

    /**
     * The source bean
     */
    private String name = null;

    /**
     * The source bean property
     */
    private String property = null;

    /**
     * The scope of the source bean
     */
    private String scope = null;


    // ----------------------------------------------------- Properties

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the request parameter name
     *
     * @return the request parameter name
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the request parameter value
     *
     * @param value the request parameter value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Returns the request parameter value
     *
     * @return the request parameter value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the source bean name
     * @param sourceBean the source bean name
     */
    public void setName(String sourceBean) {
        this.name = sourceBean;
    }

    /**
     * Returns the source bean name
     * @return the source bean name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the source bean property
     * @param sourceProperty the source property
     */
    public void setProperty(String sourceProperty) {
        this.property = sourceProperty;
    }

    /**
     * Returns the source bean property
     * @return the source property
     */
    public String getProperty() {
        return property;
    }

    /**
     * Set the source bean scope.
     * @param sourceScope the source bean scope
     */
    public void setScope(String sourceScope) {
        this.scope = sourceScope;
    }

    /**
     * Returns the source bean scope
     * @return the source bean scope
     */
    public String getScope() {
        return this.scope;
    }


    // ------------------------------------------------------ Public Methods
    /**
     * Add the parameter and its value to the link tag
     */
    public int doAfterBody() throws JspException {
        // parent tag must be a LinkTag, gives access to methods in parent
        LinkTag myparent = (LinkTag)javax.servlet.jsp.tagext.TagSupport.findAncestorWithClass(this, LinkTag.class);

        if (myparent == null)
            throw new JspException("linkparam tag not nested within link tag");
        else {
            BodyContent bodyContent = getBodyContent();
            if (bodyContent != null && !bodyContent.getString().equals("")) {
                setValue(bodyContent.getString());
            } else if (getValue() == null) setValue("null");
        }
        return SKIP_BODY;
    }

    public int doEndTag() throws JspException {
        // parent tag must be a LinkTag, gives access to methods in parent
        LinkTag myparent = (LinkTag)javax.servlet.jsp.tagext.TagSupport.
                findAncestorWithClass(this, LinkTag.class);

        if (myparent == null) {
            throw new JspException("linkparam tag not nested within link tag");
        }
        myparent.addRequestParameter(getId(), getValue());
        return EVAL_PAGE;
    }

    /**
     * Process the start tag
     */
    public int doStartTag() throws javax.servlet.jsp.JspException {

        // Look up the requested property value
        if (name != null) {
            Object beanValue =
                    RequestUtils.lookup(pageContext, name, property, scope);
            if (cat.isDebugEnabled()) cat.debug("Value is : '" + beanValue + "'");
            if (beanValue == null)
                return (EVAL_BODY_BUFFERED);

            // set the property as value
            setValue(beanValue.toString());
        }

        // Continue processing this page
        return (EVAL_BODY_BUFFERED);

    }
}