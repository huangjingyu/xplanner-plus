/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.UserStory;

import com.technoetic.xplanner.AbstractUnitTestCase;
import com.technoetic.xplanner.forms.ReorderStoriesForm;

/**
 * Created by IntelliJ IDEA.
 * User: SG0897500
 * Date: Mar 7, 2006
 * Time: 3:11:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestIteration extends AbstractUnitTestCase {

   private Collection expectedStoryList = null;
   private Iteration iteration = null;


   @Override
protected void setUp() throws Exception {
      super.setUp();
      iteration = new Iteration();
    }

   @Override
protected void tearDown() throws Exception {
      super.tearDown();
   }

   public void testModifyStoryOrderWithNoDuplicates() {

      assertStoryData(new int[]{1,2,3}, new int[]{1,2,3}, new int[]{3, 1, 2}, new int[]{3, 1, 2});
   }

   public void testModifyStoryOrderWithGapInFromOrder() {

      assertStoryData(new int[]{1,2,3}, new int[]{1,2,3}, new int[]{3, 1, 2}, new int[]{4, 1, 2});
   }

   public void testModifyStoryOrderWithGapInFromOrder2() {

      assertStoryData(new int[]{1,2,3}, new int[]{1,2,3}, new int[]{1, 2, 3}, new int[]{2, 4, 6});
   }

   public void testModifyStoryOrderWithGapInFromOrder3() {

      assertStoryData(new int[]{1,2,3}, new int[]{1,2,3}, new int[]{3, 1, 2}, new int[]{6, 2, 4});
   }

   public void testModifyStoryOrderWithDuplicateFormOrders() {

      assertStoryData(new int[]{1,2,3}, new int[]{1,2,3}, new int[]{3, 2, 1}, new int[]{3, 2, 2});
   }

   public void testModifyStoryOrderWithDuplicateFormOrders2() {

      assertStoryData(new int[]{1,2,3}, new int[]{1,2,3}, new int[]{1, 3, 2}, new int[]{1, 2, 2});
   }

   public void testModifyStoryOrderWithDuplicateFormOrders3() {

      assertStoryData(new int[]{1,2,3,4}, new int[]{1,2,3,4}, new int[]{1, 4, 3, 2}, new int[]{1, 2, 2, 2});
   }

   public void testModifyStoryOrderWithDuplicateFormOrders4() {

      assertStoryData(new int[]{1,2,3,4,5}, new int[]{1,2,3,4,5}, new int[]{1, 4, 3, 2, 5}, new int[]{1, 2, 2, 2, 3});
   }

   public void testModifyStoryOrderWithGapsAndDuplicates() {

      assertStoryData(new int[]{1,2,3,4,5}, new int[]{1,2,3,4,5}, new int[]{2,1,5,4,3}, new int[]{1,1,6,5,3});
   }

   public void testModifyStoryOrderForExistingStoriesContinuedOrMovedStoriesFromPastIteration() {

      assertStoryData(new int[]{1,2,3,4,5}, new int[]{0,1,2,3,4}, new int[]{1,2,3,4,5}, new int[]{0,1,2,3,4});
   }

   public void testModifyStoryOrderForExistingStoriesContinuedOrMovedStoriesFromFutureIteration() {

      assertStoryData(new int[]{1,2,3,4,5}, new int[]{1,2,3,4,Integer.MAX_VALUE}, new int[]{1,2,3,4,5}, new int[]{1,2,3,4,Integer.MAX_VALUE});
   }

   private void assertStoryData(int[] storyIds, int[] storyOrders, int[] storyExpectedOrder, int[] formOrder)
   {
      List<UserStory> storyListToBeOrdered = new ArrayList<UserStory>(storyIds.length);
      expectedStoryList = new ArrayList<String>(storyIds.length);
      ReorderStoriesForm mockForm = new ReorderStoriesForm();
      for (int i = 0; i < storyIds.length; i++) {
         UserStory storyToBeOrdered = new UserStory();
         storyToBeOrdered.setId(storyIds[i]);
         storyToBeOrdered.setOrderNo(storyOrders[i]);
         storyListToBeOrdered.add(storyToBeOrdered);
         UserStory storyExpected = new UserStory();
         storyExpected.setId(storyIds[i]);
         storyExpected.setOrderNo(storyExpectedOrder[i]);
         expectedStoryList.add(storyExpected);
         mockForm.setOrderNo(i, Integer.toString(formOrder[i]));
         mockForm.setStoryId(i, Integer.toString(storyIds[i]));
         iteration.getUserStories().add(storyToBeOrdered);
      }
      iteration.modifyStoryOrder(buildStoryIdNewOrderArray(storyIds, formOrder));
      Collections.sort(storyListToBeOrdered, new StoryOrderNoComparator());
      verifyResultsOrder(storyListToBeOrdered);
      verifyNoDuplicates(storyListToBeOrdered);
   }

   private int[][] buildStoryIdNewOrderArray(int[] storyIds, int[] formOrder) {
      int[][] storyIdAndNewOrder = new int[storyIds.length][2];
      for (int index = 0; index < storyIdAndNewOrder.length; index++) {
         storyIdAndNewOrder[index][0] = storyIds[index];
         storyIdAndNewOrder[index][1] = formOrder[index];
      }
      return storyIdAndNewOrder;
   }


   private void verifyResultsOrder(Collection reorderedStoryResult) {

      for (Iterator expectedIterator = expectedStoryList.iterator(); expectedIterator.hasNext();) {
         UserStory expectedStory = (UserStory) expectedIterator.next();
         for (Iterator resultIterator = reorderedStoryResult.iterator(); resultIterator.hasNext();) {
            UserStory resultStory = (UserStory)resultIterator.next();
            if(expectedStory.getId() == resultStory.getId()){
               assertEquals("The order does not match", expectedStory.getOrderNo(), resultStory.getOrderNo());
            }
         }
      }
   }

   private void verifyNoDuplicates(Collection reorderedStoryResult) {
      Map storiesByOrder = new HashMap();
      for (Iterator iterator = reorderedStoryResult.iterator(); iterator.hasNext();) {
         UserStory userStory = (UserStory) iterator.next();
         UserStory userStoryWithSameOrder = (UserStory) storiesByOrder.get(new Integer(userStory.getOrderNo()));
         if (userStoryWithSameOrder != null) {
            fail("User story ");
         }
         storiesByOrder.put(new Integer(userStory.getOrderNo()), userStory);
      }
   }

   class StoryOrderNoComparator implements Comparator {

      public int compare(Object o1, Object o2) {
         return new Integer(((UserStory)o1).getOrderNo()).compareTo(new Integer(((UserStory)o2).getOrderNo()));
      }
   }
}

