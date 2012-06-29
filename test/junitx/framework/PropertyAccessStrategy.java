/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Mar 31, 2006
 * Time: 11:51:42 PM
 */
package junitx.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

public class PropertyAccessStrategy extends MemberAccessStrategy {
   public Object getMemberValue(Object object, String property) throws Exception {
      return PropertyUtils.getProperty(object, property);
   }

   protected List getMembers(Object object1) throws Exception {
      Map map = PropertyUtils.describe(object1);
      return new ArrayList(map.keySet());
   }

}