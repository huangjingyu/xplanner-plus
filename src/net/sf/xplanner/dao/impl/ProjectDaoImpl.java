package net.sf.xplanner.dao.impl;

import java.util.List;

import net.sf.xplanner.dao.ProjectDao;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

public class ProjectDaoImpl extends BaseDao<Project> implements ProjectDao {

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly=true)
	public List<Project> getAllProjects(Person user) {
		Criteria criteria = createCriteria();
		criteria.add(Restrictions.eq(Project.HIDDEN, Boolean.FALSE));
		return criteria.list();
	}

}
