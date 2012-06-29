/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.forms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

/**
 * Created by IntelliJ IDEA.
 * User: SG0897500
 * Date: Mar 6, 2006
 * Time: 11:58:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReorderStoriesForm extends AbstractEditorForm {

   public static final String INVALID_ORDER_NUMBER = "story.editor.invalid.order.number";
   private List<String> storyIds = new ArrayList<String>();
   private List<String> orderNos = new ArrayList<String>();
   private String iterationId;

   @Override
public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
      ActionErrors errors = new ActionErrors();
      if (orderNos != null) {
         for (Iterator<String> iterator = orderNos.iterator(); iterator.hasNext();) {
            String orderValue = iterator.next();
            try {
               Double.parseDouble(orderValue);
            } catch (NumberFormatException e) {
               error(errors, INVALID_ORDER_NUMBER, new Object[] {orderValue});
            }
         }
      }
      return errors;
   }

   public List<String> getStoryIds() {
      return storyIds;
   }

   public void setStoryIds(List<String> storyIds) {
      this.storyIds = storyIds;
   }

   public void setStoryId(int index, String storyId) {
       ensureSize(storyIds, index + 1);
       storyIds.set(index, storyId);
   }

   public String getStoryId(int index) {
       return storyIds.get(index);
   }

   public int getStoryIdAsInt(int index) {
       return Integer.parseInt(getStoryId(index));
   }

   public int getStoryCount() {
       return storyIds.size();
   }

   public List<String> getOrderNos() {
      return orderNos;
   }

   public void setOrderNos(List<String> orderNos) {
      this.orderNos = orderNos;
   }

   public void setOrderNo(int index, String orderNo) {
       ensureSize(orderNos, index + 1);
       orderNos.set(index, orderNo);
   }

   public String getOrderNo(int index) {
       return orderNos.get(index);
   }

   public int getOrderNoAsInt(int index) {
       return (int) Double.parseDouble(getOrderNo(index));
   }

   public String getIterationId() {
      return iterationId;
   }

   public void setIterationId(String iterationId) {
      this.iterationId = iterationId;
   }

}
