package com.technoetic.xplanner.actions;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.domain.Iteration;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.domain.repository.ObjectRepository;
import com.technoetic.xplanner.metrics.IterationMetrics;

public class ViewIterationMetricsAction extends ViewObjectAction<Iteration> {
	private IterationMetrics iterationMetrics;

	protected ActionForward doExecute(ActionMapping actionMapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse reply) throws Exception {
		// DEBT(SPRING) Should have been injected directly from the spring
		// context file

		iterationMetrics.setIterationRepository(getRepository(actionMapping, request));
		iterationMetrics.analyze(Integer.parseInt(request.getParameter("oid")));
		request.setAttribute("metrics", iterationMetrics);
		return super.doExecute(actionMapping, form, request, reply);
	}

	public void setIterationMetrics(IterationMetrics iterationMetrics) {
		this.iterationMetrics = iterationMetrics;
	}

	protected ObjectRepository getRepository(ActionMapping actionMapping, HttpServletRequest request)
			throws ClassNotFoundException, ServletException {
		Class objectClass = getObjectType(actionMapping, request);
		ObjectRepository objectRepository = null;// getRepository(objectClass);
		return objectRepository;
	}

}
