package net.sf.xplanner.tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;
import net.sf.xplanner.tags.domain.Link;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts.taglib.TagUtils;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.db.IdSearchHelper;
import com.technoetic.xplanner.domain.Feature;
import com.technoetic.xplanner.tags.AccessKeyTransformer;
import com.technoetic.xplanner.tags.DomainContext;
import com.technoetic.xplanner.tags.db.DatabaseTagSupport;

/**
*    XplannerPlus, agile planning software
*    @author Maksym. 
*    Copyright (C) 2009  Maksym Chyrkov
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>
* 	 
*/
public class NavigationBarTag extends DatabaseTagSupport {
	private static final long serialVersionUID = -4509893685722734469L;
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
			int objectId = oid;

			if (objectId == 0
					&& pageContext.getRequest().getParameter("oid") != null) {
				objectId = NumberUtils.toInt(pageContext.getRequest()
						.getParameter("oid"));
			}

			if (objectId == 0
					&& pageContext.getRequest().getParameter("fkey") != null) {
				objectId = NumberUtils.toInt(pageContext.getRequest()
						.getParameter("fkey"));
			}

			if (object == null && objectId > 0 && type != null) {
				setObject(objectId);
			}

			DomainContext domainContext = getContext(object);
			render(domainContext);

		} catch (JspException e) {
			throw e;
		} catch (Exception e) {
			throw new JspException(e);
		}

		return EVAL_PAGE;
	}

	private void setObject(int objectId) throws Exception {
		if(StringUtils.isBlank(type)){
			setObject(((IdSearchHelper) getRequestContext().getWebApplicationContext().getBean("idSearchHelper")).search(objectId));
		}else{
			setObject(getObject(objectId, type));
		}
		DomainContext domainContext = DomainContext.get(pageContext.getRequest());
		if(domainContext==null){
			domainContext = new DomainContext();
			domainContext.populate(object);
			domainContext.save(pageContext.getRequest());
		}
	}

	private DomainContext getContext(Object subject) throws Exception {
		DomainContext domainContext = DomainContext.get(pageContext
				.getRequest());
		if (domainContext == null) {
			domainContext = new DomainContext();
			domainContext.populate(subject);
			domainContext.save(pageContext.getRequest());
		}
		return domainContext;
	}

	private Object getObject(int objectId, String typeName) throws Exception {
		Session session = getSession();
		return session.load(Class.forName(typeName), new Integer(objectId));
	}

	/**
	 * @param context
	 * @throws IOException
	 * @throws JspException
	 */
	private void render(DomainContext context)
			throws IOException, JspException {
		List<Link> links= new ArrayList<Link>();
		links.add(createLink("topLink","navigation.top", "projects", null));

		if (object != null) {
			Class objectClass = object.getClass();
			if (context.getProjectName() != null
					&& (objectClass != Project.class || inclusive)) {
				links.add(createLink(context.getProjectName(), "project", context
						.getProjectId()));
				if (context.getIterationName() != null
						&& (objectClass != Iteration.class || inclusive)) {
					links.add(createLink(context.getIterationName(), "iteration",
							context.getIterationId()));
					if (context.getStoryName() != null
							&& (objectClass != UserStory.class || inclusive)) {
						links.add(createLink(context.getStoryName(), "userstory",
								context.getStoryId()));
						if (context.getTaskName() != null
								&& (objectClass != Task.class || inclusive)) {
							links.add(createLink(context.getTaskName(), "task",
									context.getTaskId()));
						}
						if (context.getFeatureName() != null
								&& (objectClass != Feature.class || inclusive)) {
							links.add(createLink(context.getFeatureName(),
									"feature", context.getFeatureId()));
						}
					}
				}
			}
		}

		if (back) {
			List<Link> backLinks = new ArrayList<Link>();
			if (pageContext.getRequest().getParameter("returnto") != null) {
				backLinks.add(createLink("backLink","navigation.back", null, null, pageContext
						.getRequest().getParameter("returnto")));
			} else {
				backLinks.add(createLink("backLink", "navigation.back", "javascript:history.back()"));
			}
			pageContext.setAttribute("backNavigation", backLinks);
		}
		pageContext.setAttribute("navigation", links);
	}

	protected String getMessage(String key) throws JspException {
		return TagUtils.getInstance().message(pageContext, null, null, key);
	}

	private Link createLink(String titleKey, String objectType, int objectId)
			throws IOException, JspException {
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("oid", new Integer(objectId));
		return createLink(null, titleKey, objectType, params);
	}

	private Link createLink(String linkId, String titleKey, String objectType, Map<String,Object> params) throws IOException, JspException {
		return createLink(linkId, titleKey, objectType, params, "");
	}

	private Link createLink(String linkId, String titleKey, String objectType, Map<String,Object> params, String urlSuffix) throws IOException, JspException {
		String page = "";
		if (objectType != null) {
			page = "/do/view/" + objectType;
		}
		
		String url = TagUtils.getInstance().computeURL(pageContext, null, null, page, null, null, params, null, false)
				+ urlSuffix;
		if("projects".equals(objectType)){
			return createLink(linkId, titleKey, url);
		}else{
			return createLinkToContent(linkId, titleKey, url);
		}
	}

	/**
	 * @param titleKey
	 * @param url
	 * @return
	 */
	private Link createLinkToContent(String linkId, String title, String url) {
	  Link link = new Link();
	  link.setId(linkId);
	  link.setUrl(url);
	  link.setText(title);
	  return link;
	}

	private Link createLink(String linkId, String titleKey, String url) throws JspException {
	  Link link = new Link();
	  link.setUrl(url);
	  link.setId(linkId);
	  String title = getMessage(titleKey);
	  link.setAccessKey(AccessKeyTransformer.getHtml(title));
	  link.setText(AccessKeyTransformer.removeMnemonicMarkers(title));
	  return link;
	}

	@Override
	public void release() {
		object = null;
		inclusive = Boolean.FALSE;
		back = Boolean.FALSE;
		oid = 0;
		type = null;
		super.release();
	}

	@Override
	protected int doStartTagInternal() throws Exception {
		return SKIP_BODY;
	}

}
