package com.technoetic.xplanner.actions.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.SessionFactory;

import com.technoetic.xplanner.util.LogUtil;

public class InvalidateHibernateCacheAction extends Action {
    private static final Logger log = LogUtil.getLogger();
    SessionFactory sessionFactory;

   public ActionForward execute(ActionMapping mapping,
                                ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {
      sessionFactory.evictQueries();
      log.info("hibernate cache cleared");
      return null;
   }


   public void setSessionFactory(SessionFactory sessionFactory) {
      this.sessionFactory = sessionFactory;
   }
}
