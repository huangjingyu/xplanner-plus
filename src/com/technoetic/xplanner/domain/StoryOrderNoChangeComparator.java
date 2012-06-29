/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */
package com.technoetic.xplanner.domain;

import java.util.Comparator;

import net.sf.xplanner.domain.UserStory;

/**
 * Created by IntelliJ IDEA.
 * User: sg310782
 * Date: Mar 13, 2006
 * Time: 2:51:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class StoryOrderNoChangeComparator implements Comparator {

   public int compare(Object o1, Object o2) {
      UserStory userStory1 = (UserStory) o1;
      UserStory userStory2 = (UserStory) o2;
      int compare = new Integer(userStory1.getOrderNo()).compareTo(new Integer(userStory2.getOrderNo()));
      if (compare == 0) {
         compare = -(new Integer(userStory1.getPreviousOrderNo()).compareTo(
               new Integer(userStory2.getPreviousOrderNo())));
      }
      return compare;
   }
}
