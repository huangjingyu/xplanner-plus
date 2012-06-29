package com.technoetic.xplanner.forms;

import javax.servlet.http.HttpServletRequest;

import net.sf.xplanner.domain.Person;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.XPlannerProperties;

public class UserStoryEditorForm extends AbstractEditorForm {
    private String name;
    private String description;
    private int trackerId;
    private int iterationId;
    private int targetIterationId;
    private double estimatedHoursField;
    private double taskBasedEstimatedHours;
    private int priority;
    private int orderNo;
    private String dispositionName;
    private String statusName;
    private int customerId;
    /*package*/
    static final int DEFAULT_PRIORITY = 4;
    static final String DEFAULT_PRIORITY_KEY = "xplanner.story.defaultpriority";
    static final String INVALID_PRIORITY_ERROR_KEY = "story.editor.invalid_priority";
    static final String NEGATIVE_ESTIMATED_HOURS_ERROR_KEY = "story.editor.negative_estimated_hours";
    static final String MISSING_NAME_ERROR_KEY = "story.editor.missing_name";
    static final String SAME_ITERATION_ERROR_KEY = "story.editor.same_iteration";
    static final String PRIORITY_PARAM = "priority";

    public String getContainerId() {
        return Integer.toString(getIterationId());
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (isSubmitted()) {
            if (!isMerge()) {
                require(errors, name, MISSING_NAME_ERROR_KEY);
                require(errors, estimatedHoursField >= 0, NEGATIVE_ESTIMATED_HOURS_ERROR_KEY);
                validateIsNumber(errors, request, PRIORITY_PARAM, INVALID_PRIORITY_ERROR_KEY);
            } else {
                require(errors, targetIterationId != iterationId, SAME_ITERATION_ERROR_KEY);
            }

        }
        return errors;
    }

    private void validateIsNumber(ActionErrors errors, HttpServletRequest request, String param, String errorKey) {
        String priority = request.getParameter(param);
        if (priority == null) return;
        try {
            Integer.parseInt(priority);
        } catch (NumberFormatException e) {
            error(errors, errorKey);
        }
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
       XPlannerProperties props = new XPlannerProperties();
        reset(mapping, request, props);
    }

    public void reset(ActionMapping mapping, HttpServletRequest request, XPlannerProperties props) {
        super.reset(mapping, request);
        name = null;
        description = null;
        trackerId = 0;
        iterationId = 0;
        estimatedHoursField = 0;
        taskBasedEstimatedHours = 0;
        targetIterationId = 0;
        customerId = 0;
        orderNo = 0;
        resetPriority(props);
    }

    public void resetPriority(XPlannerProperties props) {
       if (props.getProperty(DEFAULT_PRIORITY_KEY) != null) {
          priority = Integer.parseInt(props.getProperty(DEFAULT_PRIORITY_KEY));
       } else {
          priority = DEFAULT_PRIORITY;
       }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
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

    public void setTrackerId(int trackerId) {
        this.trackerId = trackerId;
    }

    public int getTrackerId() {
        return trackerId;
    }

    public void setEstimatedHoursField(double estimatedHours) {
        this.estimatedHoursField = estimatedHours;
    }

    public double getEstimatedHoursField() {
        return estimatedHoursField;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public String getDispositionName() {
        return dispositionName;
    }

    public void setDispositionName(String dispositionName) {
        this.dispositionName = dispositionName;
    }

    public String getStatusName() {
       return statusName;
    }

    public void setStatusName(String statusName) {
       this.statusName = statusName;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public double getTaskBasedEstimatedHours() {
        return taskBasedEstimatedHours;
    }

    public void setTaskBasedEstimatedHours(double hours) {
        taskBasedEstimatedHours = hours;
    }

    public int getTargetIterationId() {
        return targetIterationId;
    }

    public void setTargetIterationId(int targetIterationId) {
        this.targetIterationId = targetIterationId;
    }

    public int getOrderNo() {
       return orderNo;
    }

    public void setOrderNo(int orderNo) {
       this.orderNo = orderNo;
    }
    public void setCustomer(Person customer){
    	if(customer!=null){
    		setCustomerId(customer.getId());
    	}
    }
}