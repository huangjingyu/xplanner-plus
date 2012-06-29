package com.technoetic.xplanner.util;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.beanutils.PropertyUtils;

public class CollectionUtils {
   public static double sum(Collection collection, DoubleFilter filter) {
      if (collection == null || collection.isEmpty()) return 0.0;
      double value = 0.0;
      Iterator it = collection.iterator();
      while(it.hasNext()){
         value += filter.filter(it.next());
      }
      return value;
   }

   public interface DoubleFilter {
      double filter(Object o);
   }

   public static class DoublePropertyFilter implements DoubleFilter {
      private String name;

      public DoublePropertyFilter(String name) {this.name = name;}

      public double filter(Object o) {
         try {
            return ((Double) PropertyUtils.getProperty(o, name)).doubleValue();
         } catch (Exception e) { throw new RuntimeException(e); }
      }
   }
}
