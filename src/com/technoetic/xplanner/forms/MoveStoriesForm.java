package com.technoetic.xplanner.forms;

import java.util.ArrayList;
import java.util.List;

public class MoveStoriesForm extends AbstractEditorForm {
	private List<String> storyIds = new ArrayList<String>();
	private int targetIterationId;
	private String iterationId;

	public List<String> getStoryIds() {
		return storyIds;
	}

	public void setSelected(int index, String storyId) {
		storyIds.add(storyId);
	}

	public int getTargetIterationId() {
		return targetIterationId;
	}

	public void setTargetIterationId(int targetIterationId) {
		this.targetIterationId = targetIterationId;
	}

	public String getIterationId() {
		return iterationId;
	}

	public void setIterationId(String iterationId) {
		this.iterationId = iterationId;
	}
}
