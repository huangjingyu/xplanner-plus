package com.technoetic.xplanner.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.dao.impl.CommonDao;
import net.sf.xplanner.domain.History;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Project;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.charts.DataSampler;
import com.technoetic.xplanner.domain.repository.ObjectRepository;
import com.technoetic.xplanner.domain.repository.RepositoryException;
import com.technoetic.xplanner.forms.IterationStatusEditorForm;
import com.technoetic.xplanner.forms.TimeEditorForm;
import com.technoetic.xplanner.history.HistorySupport;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.util.RequestUtils;
import com.technoetic.xplanner.util.TimeGenerator;

public class StartIterationAction extends AbstractIterationAction<Iteration> {
	private TimeGenerator timeGenerator;

	public void setTimeGenerator(TimeGenerator timeGenerator) {
		this.timeGenerator = timeGenerator;
	}

	@Override
	public void beforeObjectCommit(Iteration object, ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse reply) throws Exception {
		Iteration iteration = (Iteration) object;
		iteration.start();
		dataSampler.generateOpeningDataSamples(iteration);
		historySupport.saveEvent(iteration, History.ITERATION_STARTED, null,
				SecurityHelper.getRemoteUserId(request), timeGenerator.getCurrentTime());
	}

	@Override
	protected ActionForward doExecute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		IterationStatusEditorForm form = (IterationStatusEditorForm) actionForm;
		Iteration iteration;

		ActionForward nextPage = mapping.getInputForward();
		if (!form.isIterationStartConfirmed()) {
			if (UpdateTimeAction.isFromUpdateTime(request)) {
				// DEBT Extract constants for all these strings. from_edit/time
				// -> UpdateTimeAction, edit/iteration -> this class
				iteration = getCommonDao().getById(Iteration.class,((Iteration) request.getAttribute("edit/iteration")).getId());
			} else {
				String iterationId = form.getOid();
				iteration = (Iteration) getCommonDao().getById(Iteration.class, Integer.parseInt(iterationId));
			}
			if (iteration != null) {
				request.setAttribute("edit/iteration", iteration);
				Project project = iteration.getProject();
				Collection<Iteration> startedIterationsList = getStartedIterations(project);
				int startedIterationsCount = startedIterationsList.size();
				request.setAttribute("startedIterationsNr", new Integer(startedIterationsCount));
				if (startedIterationsCount > 0) {
					// DEBT These should not be strings but constant used both
					// in java code and jsp.
					nextPage = mapping.findForward("start/iteration");
				} else if (!UpdateTimeAction.isFromUpdateTime(request)) {
					iteration.close();
					getCommonDao().save(iteration);
					setTargetObject(request, iteration);
					String returnto = request.getParameter(EditObjectAction.RETURNTO_PARAM);
					nextPage = returnto != null ? new ActionForward(returnto, true) : mapping
							.findForward("view/projects");
				}
			}
		} else {
			String iterationId = form.getOid();
			iteration = (Iteration) getCommonDao().getById(Iteration.class, Integer.parseInt(iterationId));
			if (form.isCloseIterations()) {
				Project project = iteration.getProject();
				closeStartedIterations(project);
			}
			iteration.close();
			getCommonDao().save(iteration);

			setTargetObject(request, iteration);

			// DEBT: Use the normal returnto mechanism to send the control back
			// to edit/time w/o embedding knowledge of it inside this action
			if (RequestUtils.isParameterTrue(request, IterationStatusEditorForm.SAVE_TIME_ATTR)) {
				request.setAttribute(TimeEditorForm.WIZARD_MODE_ATTR, Boolean.TRUE);
				nextPage = mapping.findForward("edit/time");
			} else {
				String returnto = request.getParameter(EditObjectAction.RETURNTO_PARAM);
				nextPage = returnto != null ? new ActionForward(returnto, true) : mapping.findForward("view/projects");
			}
		}
		return nextPage;
	}

	private void closeStartedIterations(Project project)
			throws RepositoryException {
		Collection<Iteration> startedIterationsList = getStartedIterations(project);
		Iterator<Iteration> iterator = startedIterationsList.iterator();
		while (iterator.hasNext()) {
			Iteration iteration = (Iteration) iterator.next();
			iteration.close();
			getCommonDao().save(iteration);
		}
	}

	private Collection<Iteration> getStartedIterations(Project project) {
		Collection<Iteration> startedIterationList = new ArrayList<Iteration>();
		Collection<Iteration> iterationList = project.getIterations();
		Iterator<Iteration> iterator = iterationList.iterator();
		while (iterator.hasNext()) {
			Iteration iteration = iterator.next();
			if (iteration.isActive()) {
				startedIterationList.add(iteration);
			}
		}
		return startedIterationList;
	}

	@Override
	public void setDataSampler(DataSampler dataSampler) {
		this.dataSampler = dataSampler;
	}

	@Override
	public DataSampler getDataSampler() {
		return dataSampler;
	}
}