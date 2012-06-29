package com.technoetic.xplanner.tags;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;

import com.technoetic.xplanner.security.AuthenticationException;
public class IterationOptionsTag extends OptionsTag
{
    private int projectId;
    private boolean onlyCurrentProject;
    private Date startDate;
    private IterationLoader iterationLoader;

    protected List getOptions() throws HibernateException, AuthenticationException
   {
       iterationLoader = new IterationLoader();
       iterationLoader.setPageContext(pageContext);

       return iterationLoader.getIterationOptions(projectId, onlyCurrentProject, startDate);
   }

    public int getProjectId()
    {
        return projectId;
    }

    public void setProjectId(int projectId)
    {
        this.projectId = projectId;
    }

    public boolean isOnlyCurrentProject()
    {
        return onlyCurrentProject;
    }

    public void setOnlyCurrentProject(boolean onlyCurrentProject)
    {
        this.onlyCurrentProject = onlyCurrentProject;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }
}
