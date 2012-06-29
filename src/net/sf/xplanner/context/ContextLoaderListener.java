package net.sf.xplanner.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.technoetic.xplanner.XPlannerProperties;

import net.sf.xplanner.domain.enums.IterationType;

public class ContextLoaderListener implements ServletContextListener {
	private static final long serialVersionUID = 6909006298505174312L;
	@Deprecated
	private static WebApplicationContext requiredWebApplicationContext;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		sce.getServletContext().setAttribute("iterationTypes", IterationType.getAllValues());
		sce.getServletContext().setAttribute("appUrl", (new XPlannerProperties()).getProperty(XPlannerProperties.APPLICATION_URL_KEY));
		requiredWebApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext());
		
	}

	public static WebApplicationContext getContext() {
		return requiredWebApplicationContext;
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}
}
