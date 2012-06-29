package com.technoetic.xplanner.actions;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * User: Mateusz Prokopowicz
 * Date: Aug 2, 2005
 * Time: 12:29:23 PM
 */
public class ChangeLocaleAction extends Action {
   public ActionForward execute(ActionMapping mapping,
                                ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {
      String language = request.getParameter("language");
      String returnto = request.getParameter(EditObjectAction.RETURNTO_PARAM);
      Locale locale;
      if (!StringUtils.isEmpty(language)) {
         locale = new Locale(language);
      } else {
         locale = Locale.getDefault();
      }
      HttpSession session = request.getSession();
      session.setAttribute(Globals.LOCALE_KEY, locale);
      if (StringUtils.isEmpty(returnto)) {
         return mapping.findForward("view/projects");
      } else {
         return new ActionForward(returnto, true);
      }
   }
}
