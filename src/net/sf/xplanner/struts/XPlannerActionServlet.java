package net.sf.xplanner.struts;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.MessageResources;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class XPlannerActionServlet extends ActionServlet {
	private static final long serialVersionUID = 7941783949341881332L;
	
	@Override
	protected void initModuleMessageResources(ModuleConfig config)	throws ServletException {
		MessageResources messageResource = (MessageResources) WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext()).getBean("strutsMessageSource"); 
        getServletContext().setAttribute(Globals.MESSAGES_KEY,  messageResource);
	}
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setAttribute(Globals.MESSAGES_KEY, (MessageResources) WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext()).getBean("strutsMessageSource"));
		super.service(req, resp);
	}

}
