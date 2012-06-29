/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.forms.ReorderStoriesForm;

/**
 * Created by IntelliJ IDEA.
 * User: SG0897500
 * Date: Mar 6, 2006
 * Time: 11:55:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReorderStoriesAction extends AbstractAction {

   @Override
protected ActionForward doExecute(ActionMapping actionMapping,
                                     ActionForm actionForm,
                                     HttpServletRequest request,
                                     HttpServletResponse reply) throws Exception {

      ReorderStoriesForm form = (ReorderStoriesForm) actionForm;

      getIteration(Integer.parseInt(form.getIterationId())).modifyStoryOrder(buildStoryIdNewOrderArray(form));

      ActionForward actionForward = actionMapping.getInputForward();
      actionForward.setPath(actionForward.getPath() + "?oid=" + form.getIterationId());
      actionForward.setRedirect(true);
      return actionForward;

   }

   private int[][] buildStoryIdNewOrderArray(ReorderStoriesForm form) {
      List<String> storyIds = form.getStoryIds();
      List<String> orderNos = form.getOrderNos();
      int[][] storyIdAndNewOrder = new int[storyIds.size()][2];
      for (int index = 0; index < storyIdAndNewOrder.length; index++) {
         storyIdAndNewOrder[index][0] = Integer.parseInt(storyIds.get(index).toString());
         storyIdAndNewOrder[index][1] = (int) Double.parseDouble(orderNos.get(index).toString());
      }
      return storyIdAndNewOrder;
   }

}
