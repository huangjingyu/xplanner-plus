package com.technoetic.xplanner.file;

import java.util.Set;

import com.technoetic.xplanner.domain.Identifiable;

public class Directory2 implements Identifiable {
    private int id;
    private String name;
    private Set subdirectories;
    private Set files;
    private Directory2 parent;

    public int getId() {
        return id;
    }

    protected void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Directory2 getParent() {
        return parent;
    }

    public void setParent(Directory2 parent) {
        this.parent = parent;
    }

    public Set getSubdirectories() {
        return subdirectories;
    }

    public void setSubdirectories(Set subdirectories) {
        this.subdirectories = subdirectories;
    }

    public Set getFiles() {
        return files;
    }

    public void setFiles(Set files) {
        this.files = files;
    }
}
