package com.technoetic.xplanner.tags;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.jsp.PageContext;

import net.sf.xplanner.domain.Iteration;

import org.hibernate.HibernateException;

import com.technoetic.xplanner.domain.repository.IterationRepository;
import com.technoetic.xplanner.security.AuthenticationException;

public class IterationLoader
{
    private PageContext pageContext;

   public List getIterationOptions(int projectId, boolean onlyCurrentProject, Date startDate)
       throws HibernateException, AuthenticationException
   {
       ContextInitiator contextInitiator = new ContextInitiator(pageContext);
      //DEBT: roll the initStaticContext into the ctor. Clean up the exception: either everybody should get a jspException for a failed authentication or should get a AuthenticationException
       contextInitiator.initStaticContext();
//DEBT(Spring) load IterationRepository
       IterationRepository dao = new IterationRepository(contextInitiator.getSession(),
                                                         contextInitiator.getAuthorizer(),
                                                         contextInitiator.getLoggedInUserId());
       List iterations;
       if (onlyCurrentProject)
           iterations = dao.fetchEditableIterations(projectId, startDate);
       else
           iterations = dao.fetchEditableIterations();

       List options = new ArrayList();
       for (int i = 0; i < iterations.size(); i++)
       {
          Iteration it = (Iteration) iterations.get(i);
          options.add(new IterationModel(it));
       }
       return options;
   }

    public void setPageContext(PageContext pageContext)
    {
        this.pageContext = pageContext;
    }
}
