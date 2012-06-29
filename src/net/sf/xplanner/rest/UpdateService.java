package net.sf.xplanner.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Path("/update")
@Component
@Scope("request")
public class UpdateService {
	private UpdateManager updateManager;
	
	@Autowired
	public void setUpdateManager(UpdateManager updateManager) {
		this.updateManager = updateManager;
	}

	@POST
    @Path("/task/{taskId}/status")
    @Consumes("application/x-www-form-urlencoded")
    @Produces({"application/xml", "application/json"})
    public Result post(@PathParam("taskId") int id, @FormParam("status") String status, @FormParam("originalEstimate") double originalEstimate) {
		return updateManager.updateTaskStatus(id, status, originalEstimate);
    }
}
