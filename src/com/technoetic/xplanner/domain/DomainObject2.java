package com.technoetic.xplanner.domain;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import net.sf.xplanner.domain.DomainObject;

public abstract class DomainObject2 implements Identifiable, Nameable
{
   private Date lastUpdateTime;
   private int id;
   private Map attributes;

   public Date getLastUpdateTime()
   {
      return lastUpdateTime;
   }

   public void setLastUpdateTime(Date lastUpdateTime)
   {
      this.lastUpdateTime = lastUpdateTime;
   }

   public int getId()
   {
      return id;
   }

   public void setId(int id)
   {
      this.id = id;
   }

   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (!(o instanceof DomainObject)) return false;

      final DomainObject object = (DomainObject) o;

      if (id == 0) return this == o;
      if (id != object.getId()) return false;

      return true;
   }

   public int hashCode()
   {
      return id;
   }

   public String getAttribute(String name){
     attributes = getAttributes();
     if (attributes != null) {
        return (String) attributes.get(name);
     }
     else {
        return null;
     }
   }

   public Map getAttributes() {
      return attributes != null ? Collections.unmodifiableMap(attributes) : null;
   }

   protected void setAttributes(Map attributes)
   {
      this.attributes = attributes;
   }

   protected static String getValidProperty(Class beanClass, String property) {
      BeanInfo beanInfo;
      try {
         beanInfo = Introspector.getBeanInfo(beanClass);
      } catch (IntrospectionException e) {
         throw new RuntimeException("could not introspect " + beanClass, e);
      }
      PropertyDescriptor[] properties = beanInfo.getPropertyDescriptors();
      boolean found = false;
      for (int i = 0; i < properties.length; i++) {
         if (properties[i].getName().equals(property)) {
            found = true;
            break;
         }
      }
      if (!found) {
         throw new RuntimeException("Could not find property " + property + " in " + beanClass);
      }

      return property;

   }
}