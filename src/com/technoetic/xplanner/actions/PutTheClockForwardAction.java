/*
 * Copyright (c) Mateusz Prokopowicz. All Rights Reserved.
 */

package com.technoetic.xplanner.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.util.TimeGenerator;

public class PutTheClockForwardAction extends Action {
   public static final Logger LOG = Logger.getLogger(PutTheClockForwardAction.class);
   public static final String OFFSET_IN_DAYS_KEY = "dayOffset";


   private TimeGenerator clock;

   public void setTimeGenerator(TimeGenerator clock) {
      this.clock = clock;
   }

   public ActionForward execute(ActionMapping mapping,
                                ActionForm actionForm,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {
      
      int offsetInDays = 0;
      try {
         offsetInDays = Integer.parseInt(request.getParameter(OFFSET_IN_DAYS_KEY));

      } catch (NumberFormatException e) {
      }
      if (offsetInDays > 0) {
         clock.moveCurrentDay(offsetInDays);
      } else {
         clock.reset();
      }
      String returnto = request.getParameter(EditObjectAction.RETURNTO_PARAM);
      if (returnto != null) {
         return new ActionForward(returnto, true);
      }
      return mapping.findForward("view/projects");
   }
}