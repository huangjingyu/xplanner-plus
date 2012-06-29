/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.technoetic.xplanner.tags.domain;

import net.sf.xplanner.domain.DomainObject;

public class UnknownActionException extends RuntimeException {
   public UnknownActionException(String action, DomainObject targetBean) {
      super("Unknown action '" + action + "' on type " + targetBean.getClass().getName());
   }
}
