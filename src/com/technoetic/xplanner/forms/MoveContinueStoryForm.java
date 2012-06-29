package com.technoetic.xplanner.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.XPlannerProperties;

public class MoveContinueStoryForm extends AbstractEditorForm {
    private String name;
    private int iterationId;
    private int targetIterationId;
    private int customerId;
   private boolean isFutureIteration;

    static final String MISSING_NAME_ERROR_KEY = "story.editor.missing_name";
    static final String SAME_ITERATION_ERROR_KEY = "story.editor.same_iteration";

    public String getContainerId() {
        return Integer.toString(getIterationId());
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (isSubmitted()) {
            if (!isMerge()) {
                require(errors, name, MISSING_NAME_ERROR_KEY);
            } else {
                require(errors, targetIterationId != iterationId, SAME_ITERATION_ERROR_KEY);
            }

        }
        return errors;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
       XPlannerProperties props = new XPlannerProperties();
        reset(mapping, request, props);
    }

    public void reset(ActionMapping mapping, HttpServletRequest request, XPlannerProperties props) {
        super.reset(mapping, request);
        name = null;
        iterationId = 0;
        targetIterationId = 0;
        customerId = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIterationId(int iterationId) {
        if (targetIterationId == 0) {
            targetIterationId = iterationId;
        }
        this.iterationId = iterationId;
    }

    public int getIterationId() {
        return iterationId;
    }

    public int getTargetIterationId() {
        return targetIterationId;
    }

    public void setTargetIterationId(int targetIterationId) {
        this.targetIterationId = targetIterationId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

   public boolean isFutureIteration() {
      return isFutureIteration;
   }

   public void setFutureIteration(boolean futureIteration) {
      isFutureIteration = futureIteration;
   }
}