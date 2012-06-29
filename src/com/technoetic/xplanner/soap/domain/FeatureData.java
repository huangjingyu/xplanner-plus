package com.technoetic.xplanner.soap.domain;

import com.technoetic.xplanner.domain.Feature;

public class FeatureData extends DomainData {
    private int storyId;
    private String name;
    private String description;

    public void setStoryId(int storyId) {
        this.storyId = storyId;
    }

    public int getStoryId() {
        return storyId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


    public static Class getInternalClass() {
        return Feature.class;
    }
}