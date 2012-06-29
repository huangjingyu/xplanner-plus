package com.technoetic.xplanner.forms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.sf.xplanner.domain.UserStory;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class TaskEditorForm extends AbstractEditorForm {
    private static SimpleDateFormat dateConverter;
    private String name;
    private String description;
    private Date createdDate;
    private String createdDateString;
    private int userStoryId;
    private int targetStoryId;
    private int acceptorId;
    private double estimatedHours;
    private double actualHours;
    private String type;
    private String dispositionName;
    private boolean completed;

    public String getContainerId() {
        return Integer.toString(getUserStoryId());
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if (dateConverter == null) {
            String format = getResources(request).getMessage("format.date");
            dateConverter = new SimpleDateFormat(format);
        }

        // Set created date to be now
        //        if (createdDate == null) {
        //            setCreatedDate(new Date());
        //        }

        if (isSubmitted()) {
            if (createdDateString != null) {
                createdDate = null;
                try {
                    createdDate = dateConverter.parse(createdDateString);
                } catch (ParseException ex) {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("task.editor.bad_created_date"));
                }
            }

            if (!isMerge()) {
                require(errors, name, "task.editor.missing_name");
                require(errors, estimatedHours >= 0.0, "task.editor.negative_estimated_hours");
            }
        }
        return errors;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        name = null;
        description = null;
        userStoryId = 0;
        targetStoryId = 0;
        completed = false;
        acceptorId = 0;
        estimatedHours = 0;
        actualHours = 0;
        type = null;
        dispositionName = null;
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

    public void setUserStoryId(int storyId) {
        if (targetStoryId == 0) {
            targetStoryId = storyId;
        }
        this.userStoryId = storyId;
    }

    public int getUserStoryId() {
        return userStoryId;
    }

    public void setUserStory(UserStory story) {
        this.userStoryId = story==null?0:story.getId();
    }
    public void setEstimatedHours(double estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public double getEstimatedHours() {
        return estimatedHours;
    }

    public void setActualHours(double actualHours) {
        this.actualHours = actualHours;
    }

    public double getActualHours() {
        return actualHours;
    }

    public void setAcceptorId(int acceptorId) {
        this.acceptorId = acceptorId;
    }

    public int getAcceptorId() {
        return acceptorId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setDispositionName(String dispositionName) {
        this.dispositionName = dispositionName;
    }

    public String getDispositionName() {
        return dispositionName;
    }

    public void setCompleted(boolean flag) {
        this.completed = flag;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCreatedDateString(String createdDateString) {
        this.createdDateString = createdDateString;
    }

    public String getCreatedDateString() {
        return createdDateString;
    }

    public Date getCreatedDate() {
        if (createdDate == null) {
            setCreatedDate(new Date());
        }

        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        if (createdDate == null) {
            createdDate = new Date();
        }

        this.createdDate = createdDate;
        createdDateString = dateConverter.format(createdDate);
    }

    public int getTargetStoryId() {
        return targetStoryId;
    }

    public void setTargetStoryId(int targetStoryId) {
        this.targetStoryId = targetStoryId;
    }

}
