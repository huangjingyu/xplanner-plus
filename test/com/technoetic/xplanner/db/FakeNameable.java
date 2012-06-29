package com.technoetic.xplanner.db;

import com.technoetic.xplanner.domain.Nameable;

public class FakeNameable implements Nameable {
    private int id;
    private String title;
    private String description;

    public FakeNameable(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return title;
    }

    public String getDescription() {
        return description;
    }

	@Override
	public String getAttribute(String attributeName) {
		// TODO Auto-generated method stub
		return null;
	}
}
