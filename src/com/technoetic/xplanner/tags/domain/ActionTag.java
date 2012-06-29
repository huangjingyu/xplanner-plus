/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.technoetic.xplanner.tags.domain;

import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;

import net.sf.xplanner.domain.DomainObject;

import org.apache.struts.Globals;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.ImgTag;
import org.apache.struts.util.RequestUtils;
import org.springframework.context.MessageSource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.technoetic.xplanner.tags.LinkTag;
import com.technoetic.xplanner.views.ActionRenderer;

//DEBT(DATADRIVEN) Move the responsability of configuring the link to the actionrender (i.e. it is a strategy object)
public class ActionTag extends LinkTag {
   String action;
   ActionRenderer actionRenderer;
   DomainObject targetBean;

   public String getAction() {
      return action;
   }

   public void setAction(String action) {
      this.action = action;
   }

   public ActionRenderer getActionRenderer() {
      return actionRenderer;
   }

   public void setActionRenderer(ActionRenderer actionRenderer) {
      this.actionRenderer = actionRenderer;
   }

   public DomainObject getTargetBean() {
      return targetBean;
   }

   public void setTargetBean(DomainObject targetBean) {
      this.targetBean = targetBean;
   }

   public int doStartTag() throws JspException {
      setPage("/do/" + actionRenderer.getTargetPage());
      setUseReturnto(actionRenderer.useReturnTo());
      setOnclick(actionRenderer.getOnclick());
      if (!actionRenderer.shouldPassOidParam())
         setFkey(0);
      int result = super.doStartTag();
      if (actionRenderer.shouldPassOidParam())
         addRequestParameter("oid", String.valueOf(targetBean.getId()));
      if (actionRenderer.isDisplayedAsIcon())
         renderAsIcon();
      else
         renderAsText();
      return result;
   }

   public int doEndTag() throws JspException {
      return super.doEndTag();
   }

   private void renderAsIcon() throws JspException {
      BodyContent body = pageContext.pushBody();
      ImgTag imgTag = new ImgTag();
      imgTag.setPageContext(pageContext);
      imgTag.setPage(actionRenderer.getIconPath());
      imgTag.setBorder("0");
//      imgTag.setAlt(getActionName());
      imgTag.setStyleClass(actionRenderer.getName());
      imgTag.doStartTag();
      imgTag.doEndTag();
      pageContext.popBody();
      text = body.getString();
   }

   private void renderAsText() throws JspException {text = getActionName();}

   private String getActionName() throws JspException {
	   return ((MessageSource)WebApplicationContextUtils.getRequiredWebApplicationContext(pageContext.getServletContext()).getBean("messageSource")).getMessage(actionRenderer.getTitleKey(), null, TagUtils.getInstance().getUserLocale(pageContext, Globals.LOCALE_KEY));
   }

   public void release() {
      super.release();
      actionRenderer = null;
      text = null;
   }
}
