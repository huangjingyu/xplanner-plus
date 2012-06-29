package com.technoetic.xplanner;

import static org.easymock.EasyMock.*;
import static org.easymock.EasyMock.expect;

import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import net.sf.xplanner.domain.Project;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.tags.RequestContextAwareTag;
import org.springframework.web.util.WebUtils;

import com.technoetic.xplanner.db.hibernate.HibernateHelper;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.tags.DomainContext;

public abstract class AbstractRequestUnitTestCase extends AbstractUnitTestCase {

	protected PageContext pageContext;
	protected HttpServletRequest request;
	private WebApplicationContext applicationContext;
	protected ServletResponse response;
	private ServletContext servletContext;
	private LocaleResolver localeResolver;
	protected HttpSession httpSession;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		pageContext = easymockHelper.createGlobalMock(PageContext.class);

		setUpServlet();

		applicationContext = easymockHelper.createGlobalMock(WebApplicationContext.class);
		expect(applicationContext.getServletContext()).andReturn(servletContext).anyTimes();
		
		expect(request.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE)).andReturn(applicationContext).anyTimes();
		expect(pageContext.getAttribute(RequestContextAwareTag.REQUEST_CONTEXT_PAGE_ATTRIBUTE)).andReturn(null).anyTimes();
		expect(request.getAttribute(RequestContext.WEB_APPLICATION_CONTEXT_ATTRIBUTE)).andReturn(applicationContext).anyTimes();
		localeResolver = easymockHelper.createGlobalMock(LocaleResolver.class);
		expect(localeResolver.resolveLocale(request)).andReturn(Locale.ENGLISH).anyTimes();
		expect(request.getAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE)).andReturn(localeResolver).anyTimes();
		expect(servletContext.getInitParameter(WebUtils.HTML_ESCAPE_CONTEXT_PARAM)).andReturn(null);
		pageContext.setAttribute((String) anyObject(), anyObject());
		expect(request.getAttribute(HibernateHelper.SESSION_ATTRIBUTE_KEY)).andReturn(mockSession).anyTimes();
	}

	private void setUpServlet() {
		request = easymockHelper.createGlobalMock(HttpServletRequest.class);
		response = easymockHelper.createGlobalMock(HttpServletResponse.class);
		servletContext = easymockHelper.createGlobalMock(ServletContext.class);
		httpSession = easymockHelper.createGlobalMock(HttpSession.class);
		expect(request.getSession()).andReturn(httpSession).anyTimes();
		expect(pageContext.getRequest()).andReturn(request).anyTimes();
		expect(pageContext.getResponse()).andReturn(response);
		expect(pageContext.getServletContext()).andReturn(servletContext).anyTimes();
		
		expect(httpSession.getAttribute(SecurityHelper.SECURITY_SUBJECT_KEY)).andReturn(support.setUpSubject("user", new String[0])).anyTimes();
		expect(pageContext.findAttribute("project")).andReturn(new Project()).anyTimes();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void setUpDomainContext(int id) {
        DomainContext context = new DomainContext();
        context.setProjectId(id);
        expect(request.getAttribute(DomainContext.REQUEST_KEY)).andReturn(context).anyTimes();
        expect(request.getParameter("projectId")).andReturn("" + context.getProjectId()).anyTimes();
        		
    }

}
