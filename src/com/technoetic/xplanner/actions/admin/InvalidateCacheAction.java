/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.technoetic.xplanner.actions.admin;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.security.auth.Authorizer;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;
import com.technoetic.xplanner.util.LogUtil;

public class InvalidateCacheAction extends Action {
    private static final Logger log = LogUtil.getLogger();
    private Authorizer authorizer;
    private Map cacheMap;

    public void setCacheMap(Map cacheMap) {
      this.cacheMap = cacheMap;
    }

   public ActionForward execute(ActionMapping mapping,
                                ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {
      cacheMap.clear();
      if (SystemAuthorizer.get() != authorizer) {
         log.error("Configuration problem: there are 2 Authorizers in the system!");
      }
      log.info("cache cleared");
      return null;
   }

   public void setAuthorizer(Authorizer authorizer) {
      this.authorizer = authorizer;
   }
}
