package net.sf.xplanner.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.sf.xplanner.dao.ProjectDao;
import net.sf.xplanner.domain.Project;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-text.xml"})
public class ProjectDaoImplTest {
	
	@Autowired
	private ProjectDao projectDao;
	
	@Test
	public void testSaveAndGet(){
		Project project = new Project();
		project.setDescription("Description of project");
		project.setName("Name of the project");
		Integer id = projectDao.save(project);
		assertTrue(0 != id);
		
		Project project5 = new Project();
		project5.setName("second proj");
		project5.setDescription("sfsdf sdfgsfd ");
		
		Integer id2 = projectDao.save(project5);
		assertTrue(0 != id2);
		assertTrue(id != id2);
		
		Project project2 = projectDao.getById(id);
		System.out.println(projectDao.getAllProjects(null));
		assertEquals(project, project2);
	}
}