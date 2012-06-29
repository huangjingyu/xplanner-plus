package net.sf.xplanner.rest;

import java.util.List;

import net.sf.xplanner.dao.ViewDao;
import net.sf.xplanner.domain.view.ProjectView;
import net.sf.xplanner.domain.view.UserStoryView;

public class ViewManager {
	private ViewDao viewDao;
	
	public ProjectView getProject(Integer id) {
		return (ProjectView) viewDao.getById(ProjectView.class, id);
	}
	

	public void setViewDao(ViewDao viewDao) {
		this.viewDao = viewDao;
	}


	public List<UserStoryView> getUserStories(Integer id) {
		return viewDao.getUserStories(id);
	}

}
