package com.technoetic.xplanner.tags;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import org.apache.log4j.Logger;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.domain.Feature;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;

public class OutlineTag extends TagSupport {
   private final Logger log = Logger.getLogger(getClass());
   private final String br = "<br>";
   public static final String PREVIOUS = "<<";
   public static final String NEXT = ">>";

   @Override
public int doStartTag() throws JspException {
      DomainContext context = DomainContext.get(pageContext.getRequest());
      if (context != null) {
         doOutline(context);
      }
      return EVAL_BODY_INCLUDE;
   }

   private void doOutline(DomainContext context) throws JspException {
      String spacer = getImageUrl("spacer.gif");
      String elbow = getImageUrl("elbow.gif");
      String[] names =
            {context.getProjectName(),
             context.getIterationName(),
             context.getStoryName(),
             context.getTaskName(),
             context.getFeatureName()};

      String[] objects = {"project", "iteration", "userstory", "task", "feature"};
      int[] ids =
            {context.getProjectId(),
             context.getIterationId(),
             context.getStoryId(),
             context.getTaskId(),
             context.getFeatureId()};
      StringBuffer buffer = new StringBuffer();
      int indent = 0;

      buffer.append("<div class='navigation_outline'>\n");
      for (int i = 0; i < names.length; i++) {
         String name = names[i];
         if (name != null) {
            for (int j = 0; j < (indent * 3); j++) {
               buffer.append(spacer);
            }
            buffer.append(elbow);
            int[] nextPrevIds = getNextPrevId(ids, objects[i]);
            if (ids[i] != nextPrevIds[0]) {
               buffer.append(renderLink(PREVIOUS, nextPrevIds[0], objects[i], context));
               buffer.append("&nbsp;&nbsp;");
            }
            if (i == names.length - 1 || (names[i + 1] == null && (i == names.length - 2 || names[i + 2] == null))) {
               buffer.append(name);
            } else {
               buffer.append(renderLink(name, ids[i], objects[i], context));
            }
            if (ids[i] != nextPrevIds[1]) {
               buffer.append("&nbsp;&nbsp;");
               buffer.append(renderLink(NEXT, nextPrevIds[1], objects[i], context));
            }
            buffer.append(br);
            indent++;
         }
      }
      buffer.append("</div>\n");
      ResponseUtils.write(pageContext, buffer.toString());
   }

   private int[] getNextPrevId(int[] ids, String type) throws JspException {
      Session session = ThreadSession.get();
      try {
         //DEBT remove this magic strings.
         if ("project".equals(type)) {
            Project project = (Project) session.load(Project.class, new Integer(ids[0]));
            List list = session.find("from project in " + Project.class + " order by project.name asc");

            list = verifyProjectAccess(list);

            int[] nextPrevIndexes = calculateIndex(list.indexOf(project), list.size());
            int[] nextPrevIds =
                  {((Project) list.get(nextPrevIndexes[0])).getId(), ((Project) list.get(nextPrevIndexes[1])).getId()};
            return nextPrevIds;
         }
         if ("iteration".equals(type)) {
            Iteration iteration = (Iteration) session.load(Iteration.class, new Integer(ids[1]));
            List list =
                  session.find("from iteration in " +
                               Iteration.class +
                               " where project_id=" +
                               ids[0] +
                               " order by iteration.startDate asc");
            int[] nextPrevIndexes = calculateIndex(list.indexOf(iteration), list.size());
            int[] nextPrevIds =
                  {((Iteration) list.get(nextPrevIndexes[0])).getId(),
                   ((Iteration) list.get(nextPrevIndexes[1])).getId()};
            return nextPrevIds;
         }
         if ("userstory".equals(type)) {
            UserStory userstory = (UserStory) session.load(UserStory.class, new Integer(ids[2]));
            List list =
                  session.find("from story in " +
                               UserStory.class +
                               " where iteration_id=" +
                               ids[1] +
                               " order by story.orderNo asc");
            int[] nextPrevIndexes = calculateIndex(list.indexOf(userstory), list.size());
            int[] nextPrevIds =
                  {((UserStory) list.get(nextPrevIndexes[0])).getId(),
                   ((UserStory) list.get(nextPrevIndexes[1])).getId()};
            return nextPrevIds;
         }
         if ("task".equals(type)) {
            Task task = (Task) session.load(Task.class, new Integer(ids[3]));
            List list =
                  session.find("from task in " + Task.class + " where story_id=" + ids[2] + " order by task.name asc");
            int[] nextPrevIndexes = calculateIndex(list.indexOf(task), list.size());
            int[] nextPrevIds =
                  {((Task) list.get(nextPrevIndexes[0])).getId(), ((Task) list.get(nextPrevIndexes[1])).getId()};
            return nextPrevIds;
         }
         if ("feature".equals(type)) {
            Feature feature = (Feature) session.load(Feature.class, new Integer(ids[4]));
            List list =
                  session.find("from feature in " +
                               Feature.class +
                               " where story_id=" +
                               ids[2] +
                               " order by feature.name asc");
            int[] nextPrevIndexes = calculateIndex(list.indexOf(feature), list.size());
            int[] nextPrevIds =
                  {((Feature) list.get(nextPrevIndexes[0])).getId(), ((Feature) list.get(nextPrevIndexes[1])).getId()};
            return nextPrevIds;
         }
      } catch (HibernateException e) {
         log.error("error", e);
      }

      return null;
   }

   private List verifyProjectAccess(List list) throws JspException {
      Iterator iterator = list.iterator();
      ArrayList newList = new ArrayList();
      while (iterator.hasNext()) {
         Project project = (Project) iterator.next();
         boolean hasPermission = false;
         try {
            hasPermission = SystemAuthorizer.get().hasPermission(project.getId(),
                                                                 SecurityHelper.getRemoteUserId(pageContext),
                                                                 project,
                                                                 "read");
         }
         catch (AuthenticationException e) {
            throw new JspException(e.getMessage());
         }
         if (hasPermission) {
            newList.add(project);
         }
      }
      return newList;
   }

   private int[] calculateIndex(int index, int listSize) {
      int[] indexes = {0, 0};
      if (index > 0) {
         indexes[0] = index - 1;
      } else {
         indexes[0] = index;
      }
      if (index < listSize - 1) {
         indexes[1] = index + 1;
      } else {
         indexes[1] = index;
      }
      return indexes;
   }

   private StringBuffer renderLink(String name, int id, String type, DomainContext context) {
      try {

         StringBuffer buffer = null;
         if (!name.equals(PREVIOUS) && !name.equals(NEXT)) {
            buffer = new StringBuffer("<a href='");
         } else {
            if (name.equals(PREVIOUS)) {
               buffer = new StringBuffer("<a id=" + type.substring(0, 1) + "_desc href='");
            } else {
               buffer = new StringBuffer("<a id=" + type.substring(0, 1) + "_asc href='");
            }
         }
         HashMap params = new HashMap();
         params.put("oid", new Integer(id));

         //params.put("returnto","/do/view/"+type+"?oid="+id);
         //params.put("fkey",""+id);

         //buffer.append(RequestUtils.computeURL(pageContext, null, null, "/do/view/" + type,
         //        null, params, null, false));


         String url = null;

         if (context.getActionMapping() != null) {
            url = getUrl(context.getActionMapping().getPath(), type, params);
         } else {
            url = RequestUtils.computeURL(pageContext, null, null, "/do/view/" + type, null, params, null, false);
         }

         buffer.append(url);
         buffer.append("'>");
         if (PREVIOUS.equals(name)) {
            name = getImageUrl("previous.gif");
         } else if (NEXT.equals(name)) {
            name = getImageUrl("next.gif");
         }
         buffer.append(name);
         buffer.append("</a>");
         return buffer;
      } catch (MalformedURLException e) {
         log.error("error", e);
         return new StringBuffer("");
      }
   }

   private String getUrl(String path, String type, Map params) throws MalformedURLException {
      String url = null;
      url = RequestUtils.computeURL(pageContext, null, null, "/do" + path, null, params, null, false);
      if ("project".equals(type) && path.endsWith("history")) {
         //url = url+"&amp;oid="+id+"&amp;@type="+(new Project()).getClass().getName()+"&amp;container=true";
         url = url + "&amp;@type=" + (new Project()).getClass().getName() + "&amp;container=true";
      } else if ("iteration".equals(type) && (path.endsWith("metrics") ||
                                              path.endsWith("statistics") ||
                                              path.endsWith("accuracy") ||
                                              path.endsWith("history") ||
                                              path.endsWith("tasks"))) {
         //url = url+"&amp;oid="+id+"&amp;@type="+(new Iteration()).getClass().getName()+"&amp;container=true";
         url = url + "&amp;@type=" + (new Iteration()).getClass().getName() + "&amp;container=true";
      } else if ("userstory".equals(type) && path.endsWith("history")) {
         //url = url+"&amp;oid="+id+"&amp;@type="+(new UserStory()).getClass().getName()+"&amp;container=true";
         url = url + "&amp;@type=" + (new UserStory()).getClass().getName() + "&amp;container=true";
      } else if ("task".equals(type) && path.endsWith("history")) {
         //url = url+"&amp;oid="+id+"&amp;@type="+(new Task()).getClass().getName()+"&amp;container=true";
         url = url + "&amp;@type=" + (new Task()).getClass().getName() + "&amp;container=true";
      } else {
         url = RequestUtils.computeURL(pageContext, null, null, "/do/view/" + type, null, params, null, false);
      }
      return url;
   }

   private String getImageUrl(String imageFileName) {
      return "<img src=\"" + getContextPath() + "/images/" + imageFileName + "\">";
   }

   private String getContextPath() {
      return ((HttpServletRequest) pageContext.getRequest()).getContextPath();
   }

   @Override
public void release() {
      super.release();
   }
}
