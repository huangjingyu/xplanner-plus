package com.technoetic.xplanner.tags;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import org.apache.struts.util.RequestUtils;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.domain.Feature;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.PersonPrincipal;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.tags.db.DatabaseTagSupport;

@Deprecated
public class NavigationBarTag extends DatabaseTagSupport {
   private Object object;
   private boolean inclusive;
   private boolean back;
   private int oid;
   private String type;
   public static final String PROJECT_NAVIGATION_LINK_KEY = "navigation.project";
   public static final String ITERATION_NAVIGATION_LINK_KEY = "navigation.iteration";
   public static final String STORY_NAVIGATION_LINK_KEY = "navigation.story";
   public static final String TASK_NAVIGATION_LINK_KEY = "navigation.task";
   public static final String FEATURE_NAVIGATION_LINK_KEY = "navigation.feature";

   public void setObject(Object object) {
      this.object = object;
   }

   public void setOid(int oid) {
      this.oid = oid;
   }

   public int getOid() {
      return oid;
   }

   public void setType(String type) {
      this.type = type;
   }

   public String getType() {
      return type;
   }

   public void setBack(boolean back) {
      this.back = back;
   }

   public boolean getBack() {
      return back;
   }

   public void setInclusive(boolean inclusive) {
      this.inclusive = inclusive;
   }

   @Override
public int doEndTag() throws JspException {

      try {
         Object object = this.object;
         int objectId = oid;

         if (objectId == 0 && pageContext.getRequest().getParameter("oid") != null) {
            objectId = Integer.parseInt(pageContext.getRequest().getParameter("oid"));
         }

         if (objectId == 0 && pageContext.getRequest().getParameter("fkey") != null) {
            objectId = Integer.parseInt(pageContext.getRequest().getParameter("fkey"));
         }

         if (object == null && objectId > 0 && type != null) {
            object = getObject(objectId, type);
         }

         DomainContext domainContext = getContext(object);
         render(object, domainContext, inclusive);

      }
      catch (JspException e) {
         throw e;
      }
      catch (Exception e) {
         throw new JspException(e);
      }

      return EVAL_PAGE;
   }

   private DomainContext getContext(Object subject) throws Exception {
      DomainContext domainContext = DomainContext.get(pageContext.getRequest());
      if (domainContext == null) {
         domainContext = new DomainContext();
         domainContext.populate(subject);
         domainContext.save(pageContext.getRequest());
      }
      return domainContext;
   }

   private Object getObject(int id, String type) throws Exception {
      Session session = getSession();
      return session.load(Class.forName(type), new Integer(id));
   }

   private void render(Object object, DomainContext context, boolean inclusive)
         throws IOException, JspException {

      JspWriter out = pageContext.getOut();
      out.print("<table bgcolor='#CCCCCC' width='100%' ");
      out.print("class='navbar' border='0' cellpadding='0'><tr><td>");
      renderLink(out, "navigation.top", "projects", null);

      if (object != null) {
         Class objectClass = object.getClass();
         if (context.getProjectName() != null && (objectClass != Project.class || inclusive)) {
            renderLink(out, PROJECT_NAVIGATION_LINK_KEY, "project", context.getProjectId());
            if (context.getIterationName() != null &&
                (objectClass != Iteration.class || inclusive)) {
               renderLink(out, ITERATION_NAVIGATION_LINK_KEY, "iteration", context.getIterationId());
               if (context.getStoryName() != null &&
                   (objectClass != UserStory.class || inclusive)) {
                  renderLink(out, STORY_NAVIGATION_LINK_KEY, "userstory", context.getStoryId());
                  if (context.getTaskName() != null &&
                      (objectClass != Task.class || inclusive)) {
                     renderLink(out, TASK_NAVIGATION_LINK_KEY, "task", context.getTaskId());
                  }
                  if (context.getFeatureName() != null &&
                      (objectClass != Feature.class || inclusive)) {
                     renderLink(out, FEATURE_NAVIGATION_LINK_KEY, "feature", context.getFeatureId());
                  }
               }
            }
         }
      }

      if (back) {
         out.print(" | ");
         if (pageContext.getRequest().getParameter("returnto") != null) {
            renderLink(out,
                       "navigation.back",
                       null,
                       null,
                       pageContext.getRequest().getParameter("returnto"));
         } else {
            renderLink(out, "navigation.back", "javascript:history.back()");
         }
      }
      out.print("</td>");

      out.print("<td align='right'>");

      generateRightSideContent(out, context);

      out.print("</td></tr></table>");
   }

   private void generateRightSideContent(JspWriter out, DomainContext context) throws IOException,
                                                                                      JspException {
      XPlannerProperties xPlannerProperties = new XPlannerProperties();
      boolean isGlobalContentSearchScope =
            Boolean.valueOf(xPlannerProperties.getProperty("search.content.globalScopeEnable")).booleanValue();
      out.print("<table><tr>");
      if (isGlobalContentSearchScope || context.getProjectId() != 0) {
         out.print("<td>");
         generateContentSearchForm(out, context);
         out.print("</td>");
      }
      out.print("<td>");
      generateIdSearchForm(out);
      out.print("</td><td>");

      if (context.getProjectId() != 0) {
         HashMap params = new HashMap();
         params.put("projectId", new Integer(context.getProjectId()));
         renderLink(out, "navigation.integrations", "integrations", params);
         out.print(" | ");
         out.print("</td><td>");
      }

      int myId = getCurrentUserId();
      if (myId != 0) {
         HashMap params = new HashMap();
         params.put("oid", new Integer(myId));
         renderLink(out, "navigation.me", "person", params);
      } else {
         renderLink(out, "navigation.people", "people", null);
      }
      out.print("</td></tr></table>");
   }

   private void generateIdSearchForm(JspWriter out) throws IOException, JspException {
      out.print("<form id='idSearchForm' class='formnavbar' action='" +
                RequestUtils.computeURL(pageContext, "search/id", null, null,
                                        null, null, null, false) + "'>");
      out.print("<span class='idsearch'>");
      out.print(getMessage("idsearch.label"));
      out.print(" <input type='text' size='5' name='searchedId' /> <input type='submit' name='action' value='" +
                getMessage("idsearch.button.label") + "'/> ");
      out.print("</span> | </form>");
   }

   private void generateContentSearchForm(JspWriter out, DomainContext context) throws IOException, JspException {
      out.print("<form id='search' class='formnavbar' action='" +
                RequestUtils.computeURL(pageContext, "search/content", null, null,
                                        null, null, null, false) + "'>");
      out.print("<span class='idsearch'>");
      out.print(getMessage("contentsearch.label"));
      out.print(" <input type='text' size='5' name='searchedContent' /> <input type='submit' name='action' value='" +
                getMessage("contentsearch.button.label") + "'/> ");
      out.print(" <input type='hidden' name='restrictToProjectId' value='"+ context.getProjectId() +"'/>");
      out.print("</span> | </form>");
   }

   private int getCurrentUserId() throws JspException {
      if (SecurityHelper.isUserAuthenticated((HttpServletRequest) pageContext.getRequest())) {
         try {
            PersonPrincipal me = (PersonPrincipal) (SecurityHelper.getUserPrincipal(
                  (HttpServletRequest) pageContext.getRequest()));
            return me.getPerson().getId();
         }
         catch (AuthenticationException e) {
            throw new JspException(e);
         }
      }
      return 0;
   }

//AccessKeyTransformer.removeMnemonicMarkers(
//AccessKeyTransformer.getHtml(message)

   protected String getMessage(String key) throws JspException {
      return RequestUtils.message(pageContext, null, null, key);
   }

   private void renderLink(JspWriter out, String titleKey, String type, int id)
         throws IOException, JspException {
      HashMap params = new HashMap();
      params.put("oid", new Integer(id));
      out.print(" | ");
      renderLink(out, titleKey, type, params);
   }

   private void renderLink(JspWriter out, String titleKey, String type, Map params)
         throws IOException, JspException {
      renderLink(out, titleKey, type, params, "");
   }

   private void renderLink(JspWriter out,
                           String titleKey,
                           String type,
                           Map params,
                           String urlSuffix) throws IOException, JspException {
      String page = "";
      if (type != null) {
         page = "/do/view/" + type;
      }
      String url = RequestUtils.computeURL(pageContext,
                                           null,
                                           null,
                                           page,
                                           null,
                                           params,
                                           null,
                                           false) +
                                                  urlSuffix;
      renderLink(out, titleKey, url);
   }

   private void renderLink(JspWriter out, String titleKey, String url) throws IOException,
                                                                              JspException {
      String title = getMessage(titleKey);
      out.print("<a href='");
      out.print(url);
      out.print("'" + AccessKeyTransformer.getHtml(title) + ">");
      out.print(AccessKeyTransformer.removeMnemonicMarkers(title));
      out.print("</a>");
   }

   @Override
public void release() {
      object = null;
      inclusive = false;
      back = false;
      oid = 0;
      type = null;
      super.release();
   }

@Override
protected int doStartTagInternal() throws Exception {
	// TODO Auto-generated method stub
	return 0;
}

}

