/*
 * Copyright (c) Mateusz Prokopowicz. All Rights Reserved.
 */

package com.technoetic.xplanner.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.Command;

/**
 * User: mprokopowicz
 * Date: Feb 3, 2006
 * Time: 11:32:41 AM
 */
public class CommandExecutorAction extends Action {
   private Command command;


   public void setTask(Command command) {
      this.command = command;
   }

   public ActionForward execute(ActionMapping mapping,
                                ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {
      command.execute();
      String returnto = request.getParameter(EditObjectAction.RETURNTO_PARAM);
      if (returnto != null) {
         return new ActionForward(returnto, true);
      }
      return mapping.findForward("view/projects");
   }
}
