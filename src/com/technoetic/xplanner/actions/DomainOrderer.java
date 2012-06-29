package com.technoetic.xplanner.actions;

import java.util.Collection;
import java.util.Iterator;

import net.sf.xplanner.domain.UserStory;

public class DomainOrderer {
    public static int[][] buildStoryIdNewOrderArray(Collection stories) {
       int[][] storyIdAndNewOrder = new int[stories.size()][2];
       int index = 0;
       for (Iterator iterator = stories.iterator(); iterator.hasNext(); index++) {
          UserStory userStory = (UserStory) iterator.next();
          storyIdAndNewOrder[index][0] = userStory.getId();
          storyIdAndNewOrder[index][1] = userStory.getOrderNo();
       }
       return storyIdAndNewOrder;
    }
}
