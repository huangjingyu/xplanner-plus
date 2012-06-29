package com.technoetic.xplanner.views;

import java.beans.PropertyDescriptor;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.technoetic.xplanner.domain.ActionMapping;
import com.technoetic.xplanner.domain.Nameable;
import com.technoetic.xplanner.util.LogUtil;
import com.technoetic.xplanner.util.StringUtilities;

public class ActionRenderer {
    private static Logger log = LogUtil.getLogger();
    private boolean useReturnTo;
   private ActionMapping action;
   private Nameable object;
    private boolean displayedAsIcon;

   public ActionRenderer(ActionMapping action, Nameable object, boolean inParentView, boolean displayAsIcon) throws Exception {
      this.action = action;
      this.object = object;
      this.displayedAsIcon = displayAsIcon;
      if (inParentView) {
         useReturnTo = false;
      } else {
         useReturnTo = action.isBackToParent();
      }
   }

   public String getName() {
       return action.getName();
   }

    public String getDomainType() {
        return action.getDomainType();
    }

    public String getTargetPage() {
        return action.getTargetPage();
    }

    public String getIconPath() {
        return action.getIconPath();
    }

   public boolean useReturnTo()
   {
      return useReturnTo;
   }

   public void setUseReturnTo(boolean useReturnTo)
   {
      this.useReturnTo = useReturnTo;
   }

    public String getPermission() {
        return action.getPermission();
    }

    public String getOnclick() {
       if (!shouldUseOnclick()) return "";
       Object[] messageArgs = new Object[2];
//DEBT Spring load
       String format = ResourceBundle.getBundle("ResourceBundle").getString(action.getConfirmationKey());
       messageArgs[0] = getDomainType();
       messageArgs[1] = StringUtilities.replaceQuotationMarks(object.getName());
       return "return confirm('" + MessageFormat.format(format, messageArgs) + "')";
    }

   public boolean shouldUseOnclick() {return StringUtils.isNotEmpty(action.getConfirmationKey());}

   public String getTitleKey()
   {
      return action.getTitleKey();
   }

   public boolean isDisplayedAsIcon() {
      return displayedAsIcon;
   }

   public boolean shouldPassOidParam() {
      return action.shouldPassOidParam();
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof ActionRenderer)) {
         return false;
      }
       PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(ActionRenderer.class);
       for (int i = 0; i< descriptors.length; i++){
           String propertyName = descriptors[i].getName();
           try {
              if (!PropertyUtils.getProperty(this, propertyName).equals(PropertyUtils.getProperty(obj, propertyName))) {
                 return false;
              }
           } catch (Exception e) {
               log.error("exception: ", e);
           }
       }
       return true;
   }

   public String toString() {
       PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(this);
       StringBuffer str = new StringBuffer();
       for (int i = 0; i < descriptors.length; i++) {
           try {
               str.append("[")
                   .append(descriptors[i].getName())
                   .append("=")
                   .append(PropertyUtils.getProperty(this, descriptors[i].getName()))
                   .append("], ");
           } catch (Exception e) {
               log.error("exception: ", e);
           }
       }
       str.delete(str.length() -2, str.length()-1);
       return str.toString();
   }


   public String getOidParam() {
      return shouldPassOidParam()?"oid":"fkey";
   }
}
