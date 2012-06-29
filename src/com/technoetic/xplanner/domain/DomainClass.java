package com.technoetic.xplanner.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DomainClass {
   private Class javaClass;
   private String typeName;
   private String parentProperty;
   private Class parentClass;
   private String childrenProperty;
   private Map actionByName = new HashMap();
   private List actions = new ArrayList();

   public DomainClass(String typeName, Class myClass) {
      this.javaClass = myClass;
      this.typeName = typeName;
   }

   public DomainClass(String typeName,
                      Class myClass,
                      String parentProperty,
                      Class parentClass,
                      String childrenProperty) {
      this(typeName, myClass);
      this.parentProperty = parentProperty;
      this.parentClass = parentClass;
      this.childrenProperty = childrenProperty;
   }

   public String getParentProperty() {
      return parentProperty;
   }

   public String getChildrenProperty() {
      return childrenProperty;
   }

   public String getTypeName() {
      return typeName;
   }

   public Class getParentClass() {
      return parentClass;
   }

   public Class getJavaClass() {
      return javaClass;
   }

   public List getActions() {
      return actions;
   }

   public Map getActionByName() {
      return actionByName;
   }

   public void setActionByName(Map actionByName) {
      this.actionByName = actionByName;
   }

   public void addMapping(ActionMapping action) {
      this.actions.add(action);
      this.actionByName.put(action.getName(), action);
   }

}
