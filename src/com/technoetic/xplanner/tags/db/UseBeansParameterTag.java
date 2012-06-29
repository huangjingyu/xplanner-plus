package com.technoetic.xplanner.tags.db;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.hibernate.Hibernate;
import org.hibernate.type.BasicTypeRegistry;
import org.hibernate.type.Type;
import org.hibernate.type.TypeFactory;
import org.hibernate.type.TypeHelper;
import org.hibernate.type.TypeResolver;

public class UseBeansParameterTag extends TagSupport {
    private static final TypeResolver TYPE_RESOLVER = new TypeResolver();
	private String name;
    private Object value;
    private Type type;

    public void setValue(Object value) {
        this.value = value;
    }

    public void setType(String type) {
       this.type = TYPE_RESOLVER.basic(type);
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
	public int doEndTag() throws JspException {
        Tag parent = getParent();
        if (parent instanceof UseBeansTag) {
            if (name == null) {
                ((UseBeansTag)parent).addParameter(value, type);
            } else {
                ((UseBeansTag)parent).addParameter(name, value, type);
            }
        }
        return EVAL_PAGE;
    }

    @Override
	public void release() {
        name = null;
        value = null;
        type = null;
        super.release();
    }
}
