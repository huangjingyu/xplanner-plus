package com.technoetic.xplanner.domain;

//DEBT Should include setters
//DEBT Should det
public interface Nameable extends Identifiable {
    String getName();
    String getDescription();
    String getAttribute(String attributeName);
}
