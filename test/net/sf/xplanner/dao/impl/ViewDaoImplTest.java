package net.sf.xplanner.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.view.ProjectView;
import net.sf.xplanner.rest.ViewManager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-text.xml"})
public class ViewDaoImplTest {
	@Autowired
	private ViewManager viewManager;
	@Autowired
	private SessionFactory sessionFactory;
	
	@Test
	public void testGetById() {
		Session session = SessionFactoryUtils.getSession(sessionFactory, true);
		session.beginTransaction();
		Project project = new Project();
		project.setName("Test project");
		session.save(project);
		session.flush();
		assertTrue(0!= project.getId());
		ProjectView projectView = viewManager.getProject(project.getId());
		assertEquals(project.getId(), projectView.getId());
		assertEquals(project.getName(), projectView.getName());
		System.out.println(projectView);
	}

}
