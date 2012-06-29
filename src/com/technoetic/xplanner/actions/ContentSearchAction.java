package com.technoetic.xplanner.actions;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.domain.DomainObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.db.ContentSearchHelper;
import com.technoetic.xplanner.db.IdSearchHelper;
import com.technoetic.xplanner.domain.Nameable;
import com.technoetic.xplanner.domain.repository.RepositoryException;
import com.technoetic.xplanner.security.SecurityHelper;

/**
 * @noinspection UnusedAssignment
 */
public class ContentSearchAction extends AbstractAction {
   public static final String SEARCH_CRITERIA_KEY = "searchedContent";
   private ContentSearchHelper searchHelper;
   private IdSearchHelper idSearchHelper;
   
   protected static final String RESTRICTED_PROJECT_ID_KEY = "restrictToProjectId";

   
   protected ActionForward doExecute(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response)
         throws Exception {
      int remoteUserId = SecurityHelper.getRemoteUserId(request);
      String searchCriteria = request.getParameter(SEARCH_CRITERIA_KEY);
      if (StringUtils.isEmpty(searchCriteria)) {
         request.setAttribute("exception", produceException(request));
         return mapping.findForward("error");
      }
      XPlannerProperties xPlannerProperties = new XPlannerProperties();
      Boolean isGlobalSearchScope = Boolean.valueOf(xPlannerProperties.getProperty("search.content.globalScopeEnable"));
      int projectId = 0;
      if (!isGlobalSearchScope.booleanValue())
         projectId = Integer.parseInt(request.getParameter(RESTRICTED_PROJECT_ID_KEY));
      List results = search(searchCriteria, remoteUserId, projectId);
      if(NumberUtils.toInt(searchCriteria)!=0 && projectId==0){
    	  DomainObject domainObject = idSearchHelper.search(NumberUtils.toInt(searchCriteria));
    	  if(domainObject instanceof Nameable){
    		  results.add(0, searchHelper.convertToSearchResult((Nameable)domainObject, searchCriteria));
    	  }
      }
      request.setAttribute("searchResults", results);
      request.setAttribute(SEARCH_CRITERIA_KEY, searchCriteria);

      return mapping.findForward("success");
   }

   protected List search(String searchCriteria, int userId, int restrictedProjectId) throws RepositoryException {
      searchHelper.search(searchCriteria, userId, restrictedProjectId);
      return searchHelper.getSearchResults();
   }

   private Exception produceException(HttpServletRequest request) {
      return produceException(((MessageResources) request.getAttribute(Globals.MESSAGES_KEY)),
                              request.getLocale(),
                              "missing content");
   }

   private Exception produceException(MessageResources messageResources,
                                      Locale locale,
                                      String message) {
      String invalidMessage = messageResources.getMessage(locale, "contentsearch.invalid_id");
      String exceptionMessage = invalidMessage + (message != null ? ": " + message : message);
      return new Exception(exceptionMessage);
   }

	public void setContentSearchHelper(ContentSearchHelper searchHelper) {
		this.searchHelper = searchHelper;
	}

	public void setIdSearchHelper(IdSearchHelper idSearchHelper) {
		this.idSearchHelper = idSearchHelper;
	}
}
