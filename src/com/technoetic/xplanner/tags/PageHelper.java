package com.technoetic.xplanner.tags;

import javax.servlet.ServletRequest;

import net.sf.xplanner.domain.Project;

import org.apache.commons.lang.StringUtils;

//DEBT Should be a spring loaded service bean 

public class PageHelper {
   public static int getProjectId(Object resource, ServletRequest request) {
      int projectId = 0;
      DomainContext context = DomainContext.get(request);
      if (context != null) {
         projectId = context.getProjectId();
      }
      if (projectId == 0 && resource instanceof Project) {
         projectId = ((Project) resource).getId();
      }
      String id = request.getParameter("projectId");
      if (projectId == 0 && !StringUtils.isEmpty(id)) {
         projectId = Integer.parseInt(id);
      }
      return projectId;
   }
}
