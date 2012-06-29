package com.technoetic.xplanner.tags;

import net.sf.xplanner.domain.UserStory;


//FIXME Test this

public class StoryModel {
    private IterationModel iterationModel;
    private UserStory story;

    public StoryModel(IterationModel iterationModel, UserStory story) {
        this.iterationModel = iterationModel;
        this.story = story;
    }

    public String getName() {
        return iterationModel.getName() + " :: " + story.getName();
    }

    public int getId() {
        return story.getId();
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StoryModel)) return false;

        final StoryModel storyModel = (StoryModel) o;

        if (!iterationModel.equals(storyModel.iterationModel)) return false;
        if (!story.equals(storyModel.story)) return false;

        return true;
    }

    public int hashCode() {
        return story.hashCode();
    }

    public String toString() {
        return "StoryModel{" +
            "iterationModel=" + iterationModel +
            ", story=" + story +
            "}";
    }

    public UserStory getUserStory() {
        return story;
    }

    public IterationModel getIterationModel() {
        return iterationModel;
    }


}
