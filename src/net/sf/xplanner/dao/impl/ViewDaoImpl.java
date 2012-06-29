package net.sf.xplanner.dao.impl;

import java.io.Serializable;
import java.util.List;

import net.sf.xplanner.dao.ViewDao;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.view.ProjectView;
import net.sf.xplanner.domain.view.UserStoryView;
import net.sf.xplanner.hibernate.AliasToBeanResultTransformer;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.technoetic.xplanner.domain.Identifiable;

public class ViewDaoImpl implements ViewDao {
	private SessionFactory sessionFactory;
	
	public final Session getSession(){
		return SessionFactoryUtils.getSession(sessionFactory, true);
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public Object getById(Class<? extends Identifiable> domainClass, Serializable id) {
		Criteria criteria = getSession().createCriteria(Project.class).add(Restrictions.eq("id", id)).setProjection(Projections.projectionList().add(Projections.property("name").as("name")).add(Projections.property("id").as("id"))).setResultTransformer(new org.hibernate.transform.AliasToBeanResultTransformer(ProjectView.class));
		return criteria.uniqueResult();
	}
	
	public List<UserStoryView> getUserStories(Integer iterationId) {
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("us.priority").as("priority"));
		projectionList.add(Projections.property("us.id").as("id"));
		projectionList.add(Projections.property("us.name").as("name"));
		projectionList.add(Projections.property("us.description").as("description"));
		projectionList.add(Projections.property("us.trackerId").as("trackerId"));
		projectionList.add(Projections.property("us.estimatedHoursField").as("estimatedHours"));
		projectionList.add(Projections.property("us.orderNo").as("orderNo"));
		projectionList.add(Projections.property("us.status").as("status"));
		projectionList.add(Projections.property("tasks.name").as("tasks.name"));
		projectionList.add(Projections.property("tasks.id").as("tasks.id"));
		projectionList.add(Projections.property("tasks.completed").as("tasks.completed"));
		projectionList.add(Projections.property("tasks.description").as("tasks.description"));
		projectionList.add(Projections.property("tasks.createdDate").as("tasks.createdDate"));
		projectionList.add(Projections.property("tasks.originalEstimate").as("tasks.originalEstimate"));
		projectionList.add(Projections.property("tasks.estimatedHours").as("tasks.estimatedHours"));
		projectionList.add(Projections.property("tasks.acceptorId").as("tasks.acceptorId"));
		//projectionList.add(Projections.sum("tasks.actualHours").as("actualHours"));
		Criteria criteria = getSession().createCriteria(Iteration.class).add(Restrictions.eq("this.id", iterationId)).createAlias("userStories", "us").setProjection(projectionList).setResultTransformer(new AliasToBeanResultTransformer<UserStoryView>(UserStoryView.class));
		criteria.createAlias("us.tasks", "tasks", Criteria.LEFT_JOIN);
		return criteria.list();
	}
	
	public Serializable save(Identifiable domainObject) {
		return getSession().save(domainObject);
	}

}
