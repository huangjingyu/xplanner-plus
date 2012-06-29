/**
 * 
 */
package net.sf.xplanner.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import net.sf.xplanner.dao.TaskDao;
import net.sf.xplanner.domain.Task;

/**
 *    XplannerPlus, agile planning software
 *    @author Maksym_Chyrkov. 
 *    Copyright (C) 2009  Maksym Chyrkov
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>
 * 	 
 */
public class TaskDaoImpl extends BaseDao<Task> implements TaskDao{

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly=true)
	public List<Task> getCurrentTasksForPerson(int personId) {
		Criteria criteria = createCriteria();
		criteria.createAlias("userStory", "userStory");
		criteria.createAlias("userStory.iteration", "iteration");
		Date now = new Date();
		criteria.add(Restrictions.le("iteration.startDate", now));
		criteria.add(Restrictions.ge("iteration.endDate", now));
		criteria.add(Restrictions.eq("acceptorId", personId));
		criteria.setFetchMode("timeEntries", FetchMode.SELECT);
		return criteria.list();
	}
}