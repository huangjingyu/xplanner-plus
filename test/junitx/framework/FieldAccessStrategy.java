/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Mar 31, 2006
 * Time: 11:54:33 PM
 */
package junitx.framework;

import java.util.List;

import com.technoetic.xplanner.util.ClassUtil;

public class FieldAccessStrategy extends MemberAccessStrategy {

   protected Object getMemberValue(Object object, String member) throws Exception {
      return ClassUtil.getFieldValue(object, member);
   }

   protected List getMembers(Object object) throws Exception {
     return ClassUtil.getAllFieldNames(object);
   }
}
