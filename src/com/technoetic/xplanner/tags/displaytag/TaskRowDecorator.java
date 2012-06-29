package com.technoetic.xplanner.tags.displaytag;

import net.sf.xplanner.domain.Task;


/**
 * Created by IntelliJ IDEA.
 * User: tkmower
 * Date: Mar 1, 2005
 * Time: 8:43:20 PM
 */
public class TaskRowDecorator implements RowDecorator
{
    public String getCssClasses(Row row)
    {
        try
        {
            if (((Task) row.getObject()).isCompleted() == true)
            {
                return "task_completed";
            }
            else if (((Task) row.getObject()).getActualHours() > 0)
            {
                return "task_started";
            }
            else
            {
                return ((Task) row.getObject()).getAcceptorId() == 0 ? "task_unassigned" :
                    (((Task) row.getObject()).getEstimatedHours() == 0 ? "task_unestimated" : "");
            }
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
