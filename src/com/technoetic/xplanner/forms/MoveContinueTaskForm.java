package com.technoetic.xplanner.forms;

import javax.servlet.http.HttpServletRequest;

import net.sf.xplanner.domain.UserStory;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class MoveContinueTaskForm extends AbstractEditorForm {
    private String name;
    private int storyId;
    private int targetStoryId;
    private int taskId;
	private UserStory userStory = new UserStory();
    static final String SAME_STORY_ERROR_KEY = "task.editor.same_story";

    public String getContainerId() {
        return Integer.toString(getStoryId());
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if (isSubmitted()) {
            if (!isMerge()) {
                require(errors, name, "task.editor.missing_name");
            } else {
                require(errors, targetStoryId != storyId, SAME_STORY_ERROR_KEY);
            }
        }
        return errors;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        storyId = 0;
        targetStoryId = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStoryId(int storyId) {
        if (targetStoryId == 0) {
            targetStoryId = storyId;
        }
        this.storyId = storyId;
    }

    public int getStoryId() {
        return userStory.getId();
    }

    public void setStory(UserStory story) {
        this.userStory = story;
    }

    public int getTargetStoryId() {
        return targetStoryId;
    }

    public void setTargetStoryId(int targetStoryId) {
        this.targetStoryId = targetStoryId;
    }

    public int getTaskId() {
        return taskId;
    }
    
	public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

	public void setUserStory(UserStory userStory){
		this.userStory = userStory;
	}
	
	public UserStory getUserStory() {
		return userStory;
	}
}
