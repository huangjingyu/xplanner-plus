package com.technoetic.xplanner.forms;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.actions.ContinueUnfinishedStoriesAction;

public class ContinueUnfinishedStoriesForm extends AbstractEditorForm{
    private int iterationId;
    private int targetIterationId;
    private int projectId;
    private Date startDate;
    private static final String SAME_ITERATION_ERROR_KEY = "iteration.status.editor.continue_in_same_iteration";

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
    {
        ActionErrors errors = new ActionErrors();
        if (isSubmitted() && getAction().equals(ContinueUnfinishedStoriesAction.OK_ACTION))
        {
            require(errors, targetIterationId != iterationId, SAME_ITERATION_ERROR_KEY);
        }
        return errors;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
       XPlannerProperties props = new XPlannerProperties();
        reset(mapping, request, props);
    }

    public void reset(ActionMapping mapping, HttpServletRequest request, XPlannerProperties props) {
        super.reset(mapping, request);
        iterationId = 0;
        targetIterationId = 0;
        projectId = 0;
    }

    public int getIterationId() {
        return iterationId;
    }

    public void setIterationId(int iterationId) {
        this.iterationId = iterationId;
    }

    public int getTargetIterationId() {
        return targetIterationId;
    }

    public void setTargetIterationId(int targetIterationId) {
        this.targetIterationId = targetIterationId;
    }

    public void setProjectId(int projectId)
    {
        this.projectId = projectId;
    }

    public int getProjectId()
    {
        return projectId;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }
}
