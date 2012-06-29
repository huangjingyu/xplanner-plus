/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Apr 1, 2006
 * Time: 12:17:27 AM
 */
package junitx.framework;

import java.util.ArrayList;
import java.util.Collection;

public class CollectionMemberEqualAssert extends ValueMemberEqualAssert {
   private EqualAssert equalAssert;

   public CollectionMemberEqualAssert(EqualAssert equalAssert) {
      this.equalAssert = equalAssert;
   }

   @Override
   public boolean assertValueEquals(String propertyName, Object expectedValue, Object actualValue) {
       if (expectedValue != null && Collection.class.isAssignableFrom(expectedValue.getClass())) {
          equalAssert.assertEquals(new ArrayList((Collection)expectedValue), new ArrayList((Collection)actualValue));
          return true;
       }
       return false;
   }
}