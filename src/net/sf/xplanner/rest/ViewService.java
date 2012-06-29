package net.sf.xplanner.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.sf.xplanner.domain.view.ProjectView;
import net.sf.xplanner.domain.view.UserStoryView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Path("/view")
@Component()
@Scope("request")
public class ViewService {
	private ViewManager viewManager;
	
	@Autowired
    public void setViewManager(ViewManager viewManager) {
		this.viewManager = viewManager;
	}
	// The Java method will process HTTP GET requests
    @GET
    // The Java method will produce content identified by the MIME Media
    // type "text/plain"
    @Produces("text/plain")
    public String getIt() {
        return "Hi there!";
    }
    
    @GET
    @Produces({"application/xml", "application/json"})
    @Path("/project/{projectId}")
    public ProjectView getProject(@PathParam("projectId") Integer id) {
        return viewManager.getProject(id);
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/iteration/{iterationId}/userstories")
    public UserStoryView[] getUserStories(@PathParam("iterationId") Integer id) {
        List<UserStoryView> userStories = viewManager.getUserStories(id);
		return userStories.toArray(new UserStoryView[userStories.size()]);
    }
    
}
