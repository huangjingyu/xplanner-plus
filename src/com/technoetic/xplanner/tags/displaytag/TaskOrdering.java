package com.technoetic.xplanner.tags.displaytag;

import net.sf.xplanner.domain.Task;

import com.technoetic.xplanner.domain.TaskStatus;

public class TaskOrdering implements Comparable
{
    public TaskStatus status;
    public int orderNo;
    public String storyName;
    public String taskName;

    public TaskOrdering(Task task)
    {
        status = task.getStatus();
        orderNo = task.getUserStory().getOrderNo();
        storyName = task.getUserStory().getName();
        taskName = task.getName();
    }

    public int compareTo(Object o)
    {
        TaskOrdering order = (TaskOrdering) o;
        if (!status.equals(order.status)) return status.compareTo(order.status);
        if (orderNo != order.orderNo) return orderNo - order.orderNo;
        if (!storyName.equals(order.storyName)) return storyName.compareTo(order.storyName);
        if (!taskName.equals(order.taskName)) return taskName.compareTo(order.taskName);
        return 0;
    }
}
