package com.technoetic.xplanner.soap.domain;

import net.sf.xplanner.domain.Project;


public class ProjectData extends DomainData {
    private String name;
    private String description;

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

    public static Class getInternalClass() {
        return Project.class;
    }
}