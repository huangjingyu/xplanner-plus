package com.technoetic.xplanner.tags;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.taglib.html.SelectTag;
import org.apache.struts.util.RequestUtils;

import com.technoetic.xplanner.domain.CharacterEnum;

public class CharacterEnumSelectTag extends SelectTag {
   public static final String EDIT_MODE = "edit";
   public static final String VIEW_MODE = "view";
   private String mode;
   private String enumProperty;
   private Object object;
   private String objectName;
   private ResourceBundle resourceBundle;
   private String resourceType;

   public int doStartTag() throws JspException {
      resourceBundle = ResourceBundle.getBundle("ResourceBundle");
      if (EDIT_MODE.equals(mode)) {
         return super.doStartTag();
      } else {
         renderHtmlText();
         return EVAL_BODY_BUFFERED;
      }
   }

   public int doEndTag() throws JspException {
      try {
         if (EDIT_MODE.equals(mode)) {
            JspWriter out = pageContext.getOut();
            CharacterEnum[] enums = getEnumObject().listEnums();
            for (int i = 0; i < enums.length; i++) {
               CharacterEnum enumm = enums[i];
               out.println(renderSelectOptionElement(enumm));
            }
            out.println("</select>");
         }
      } catch (IOException e) {
         throw new JspException(e);
      }
      return EVAL_PAGE;
   }

   private void renderHtmlText() throws JspException {
      try {
         JspWriter out = pageContext.getOut();
         CharacterEnum e = getEnumObject();
         out.write(resourceBundle.getString(e.getNameKey()));
      } catch (IOException e) {
         throw new JspException(e);
      }
   }

   private String renderSelectOptionElement(CharacterEnum anEnum) {
      StringBuffer sb = new StringBuffer();
      sb.append("<option ");
      sb.append("value='").append(anEnum.getCode()).append("'");
      if (this.isMatched(new String(String.valueOf(anEnum.getCode())))) {
         sb.append(" selected='selected'");
      }
      sb.append(">");
      sb.append(resourceBundle.getString(anEnum.getNameKey()));
      sb.append("</option>");
      return sb.toString();
   }

   private CharacterEnum getEnumObject() throws JspException {
      Object resource = getResource();
      try {
         return (CharacterEnum) PropertyUtils.getProperty(resource, enumProperty);
      } catch (Exception e) {
         throw new JspException(e);
      }
   }

   private Object getResource() throws JspException {
      Object resource = object;
      if (object instanceof String) {
         resource = pageContext.findAttribute((String) object);
      }
      if (resource == null && name != null) {
         resource = RequestUtils.lookup(pageContext, objectName, enumProperty, null);
      }
      if (resource == null) {
         resource = pageContext.findAttribute("project");
      }
      if (resource == null && resourceType == null) {
         throw new JspException("object or resource type/id must be specified");
      }
      return resource;
   }


   public void setMode(String mode) {
      this.mode = mode;
   }

   public void setEnumProperty(String enumProperty) {
      this.enumProperty = enumProperty;
   }

   public void setObjectName(String objectName) {
      this.objectName = objectName;
   }

   public void setObject(Object object) {
      this.object = object;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setResourceType(String resourceType) {
      this.resourceType = resourceType;
   }
}
