package net.sf.xplanner.dao;

import java.util.List;

import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;


public interface ProjectDao extends Dao<Project> {
	List<Project> getAllProjects(Person user);
}
