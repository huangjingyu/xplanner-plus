package com.technoetic.xplanner.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.domain.History;
import net.sf.xplanner.domain.Iteration;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.domain.IterationStatus;
import com.technoetic.xplanner.domain.repository.ObjectRepository;
import com.technoetic.xplanner.forms.IterationStatusEditorForm;
import com.technoetic.xplanner.history.HistorySupport;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.util.TimeGenerator;

public class CloseIterationAction extends AbstractIterationAction<Iteration> {
	private TimeGenerator timeGenerator;

	public void setTimeGenerator(TimeGenerator timeGenerator) {
		this.timeGenerator = timeGenerator;
	}

	@Override
	public void beforeObjectCommit(Iteration iteration, ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request, HttpServletResponse reply) throws Exception {
		closeIteration(request, iteration);
		String event = History.ITERATION_CLOSED;
		historySupport.saveEvent(iteration, event, null, SecurityHelper.getRemoteUserId(request),
				timeGenerator.getCurrentTime());
	}

	@Override
	protected ActionForward doExecute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		IterationStatusEditorForm form = (IterationStatusEditorForm) actionForm;
		if (StringUtils.isEmpty(form.getOid()))
			return mapping.getInputForward();

		String iterationId = form.getOid();
		Iteration iteration = (Iteration) getCommonDao().getById(Iteration.class, Integer.parseInt(iterationId));
		if (iteration.isActive()) {
			iteration.setIterationStatus(IterationStatus.INACTIVE);
			setTargetObject(request, iteration);
		}
		getCommonDao().save(iteration);
		String returnto = request.getParameter(EditObjectAction.RETURNTO_PARAM);
		ActionForward forward = mapping.findForward("onclose");
		// DEBT refactor the creation of the url get parameter to reuse what the
		// LinkTag is doing.
		// DEBT We need to move to encapsulate how the links are built so we
		// eventually "invoke" new screens like functions and not build link by
		// hand
		return new ActionForward(forward.getPath() + "?iterationId=" + iterationId + "&" + returnto + "?oid="
				+ iterationId + "&fkey=" + iterationId, forward.getRedirect());
	}

	public void closeIteration(HttpServletRequest request, Iteration iteration) throws Exception {
		iteration.setIterationStatus(IterationStatus.INACTIVE);
		dataSampler.generateClosingDataSamples(iteration);
	}
}