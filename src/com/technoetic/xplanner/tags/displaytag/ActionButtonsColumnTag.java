package com.technoetic.xplanner.tags.displaytag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.displaytag.properties.MediaTypeEnum;
import org.displaytag.util.TagConstants;

import com.technoetic.xplanner.tags.WritableTag;


public class ActionButtonsColumnTag extends org.displaytag.tags.ColumnTag {
   // TODO: why not use our ColumnTag instead for consistency?
//public class ActionButtonsColumnTag extends com.technoetic.xplanner.tags.displaytag.ColumnTag {
   private static Log log = LogFactory.getLog(ActionButtonsColumnTag.class);
   ActionButtonsTag actionButtonsTag;

   public void setActionButtonsTag(ActionButtonsTag actionButtonsTag)
   {
      this.actionButtonsTag = actionButtonsTag;
   }

    public ActionButtonsColumnTag() {
        setMedia(MediaTypeEnum.HTML.getName());
        actionButtonsTag = new ActionButtonsTag();
        actionButtonsTag.showOnlyActionWithIcon();
    }

   public void setPageContext(PageContext context)
   {
      super.setPageContext(context);
      actionButtonsTag.setPageContext(context);
   }

    public void setId(String s)
    {
        this.id = s;
        actionButtonsTag.setId(s);
    }

   public String getName() {
        return actionButtonsTag.getName();
    }

    public void setName(String name) {
       actionButtonsTag.setName(name);
    }

    public String getScope() {
        return actionButtonsTag.getScope();
    }

    public void setScope(String scope) {
       actionButtonsTag.setScope(scope);
    }

    public int doStartTag() throws JspException {
        try {
            WritableTag parentTable = (WritableTag) this.getParent();
            if (!parentTable.isWritable()) {
                return SKIP_BODY;
            }
            if (!getAttributeMap().containsKey(TagConstants.ATTRIBUTE_NOWRAP))
                getAttributeMap().put(TagConstants.ATTRIBUTE_NOWRAP, "true");

            int status = super.doStartTag();
            if (status != SKIP_BODY) {
               return actionButtonsTag.doStartTag();
            }
            return status;
        } catch (Exception e) {
            throw new JspException(e);
        }
    }


   public int doAfterBody() throws JspException {
       return actionButtonsTag.doAfterBody();
    }

    public int doEndTag() throws JspException {
        actionButtonsTag.doEndTag();
        try {
            WritableTag parentTable = (WritableTag) this.getParent();
            if (!parentTable.isWritable()) {
                return SKIP_BODY;
            } else {
                return super.doEndTag();
            }
        } catch (Exception e) {
            throw new JspException(e);
        }
    }

    public void release() {
      actionButtonsTag.release();
    }
}

