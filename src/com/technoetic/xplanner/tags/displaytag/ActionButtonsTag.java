package com.technoetic.xplanner.tags.displaytag;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.struts.util.RequestUtils;

import com.technoetic.xplanner.domain.ActionMapping;
import com.technoetic.xplanner.domain.DomainMetaDataRepository;
import com.technoetic.xplanner.domain.Nameable;
import com.technoetic.xplanner.views.ActionRenderer;

public class ActionButtonsTag extends BodyTagSupport 
{
   private Nameable object;
   Iterator iterator;
   private String name;
   private String scope;
   private ActionMapping actionMapping;
   private boolean showOnlyActionWithIcon;
   private DomainMetaDataRepository domainMetaDataRepository;

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public String getScope()
   {
      return scope;
   }

   public void setScope(String scope)
   {
      this.scope = scope;
   }

   public Nameable getObject() throws JspException {
	   Nameable object = this.object;
       if (object == null) {
           object = (Nameable) RequestUtils.lookup(pageContext, name, scope);
       }

       if (object == null) {
           JspException e = new JspException("no object");
           RequestUtils.saveException(pageContext, e);
           throw e;
       }
       return object;
   }

   public void setObject(Nameable object) {
       this.object = object;
   }

   public DomainMetaDataRepository getDomainMetaDataRepository() {
      if (domainMetaDataRepository == null)
         domainMetaDataRepository = DomainMetaDataRepository.getInstance();
      return domainMetaDataRepository;
   }

   public int doStartTag() throws JspException
   {
      iterator = getDomainMetaDataRepository().getMetaData(getObject().getClass()).getActions().iterator();
      return doAfterBody();
   }

   public int doAfterBody() throws JspException {
      try
      {
         if (iterator.hasNext())
         {
            actionMapping = (ActionMapping) iterator.next();
            if ((!showOnlyActionWithIcon || doesActionHaveIcon()) && isActionVisible()) {
               pageContext.setAttribute(id, new ActionRenderer(actionMapping, getObject(), showOnlyActionWithIcon, showOnlyActionWithIcon));
               return EVAL_BODY_BUFFERED;
            } else {
               return doAfterBody();
            }
         }
         else {
            return SKIP_BODY;
         }
      }
      catch (Exception e) {
         throw new JspException(e);
      }
   }

   private boolean isActionVisible() throws JspException {return actionMapping.isVisible(getObject());}

   private boolean doesActionHaveIcon() {return actionMapping.getIconPath() != null;}


   public void release() {
      super.release();
      iterator = null;
      name = null;
      scope = null;
      object = null;
      id = null;
   }

   public int doEndTag() throws JspException {
      iterator = null;
      try
      {
         if (bodyContent != null) {
            pageContext.getOut().print(bodyContent.getString());
         }

      }
      catch (IOException e)
      {
        throw new JspException(e);
      }
      return super.doEndTag();
   }

      public void showOnlyActionWithIcon() {
         showOnlyActionWithIcon = true;
      }

   public void setDomainMetaDataRepository(DomainMetaDataRepository domainMetaDataRepository) {

      this.domainMetaDataRepository = domainMetaDataRepository;
   }
}
