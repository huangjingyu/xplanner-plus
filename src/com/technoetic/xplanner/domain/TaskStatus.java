package com.technoetic.xplanner.domain;

import java.util.Arrays;
import java.util.List;

@Deprecated
public class TaskStatus implements Comparable
{
    public static final TaskStatus STARTED = new TaskStatus("S");
    public static final TaskStatus NON_STARTED = new TaskStatus("");
    public static final TaskStatus COMPLETED = new TaskStatus("C");
    public static final List statusOrdering = Arrays.asList(new TaskStatus[]{
        STARTED,
        NON_STARTED,
        COMPLETED});

    private final String status;

    private TaskStatus(String status)
    {
        this.status = status;
    }

    public String toString()
    {
        return status;
    }

    public int compareTo(Object o)
    {
        return statusOrdering.indexOf(this) - statusOrdering.indexOf(o);
    }
}
