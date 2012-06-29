/**
 * 
 */
package net.sf.xplanner.dao.impl;

import java.util.Date;
import java.util.List;

import net.sf.xplanner.dao.DataSampleDao;
import net.sf.xplanner.domain.DataSample;
import net.sf.xplanner.domain.Iteration;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

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
public class DataSampleDaoImpl extends BaseDao<DataSample> implements DataSampleDao{
	
	@SuppressWarnings("unchecked")
	public List<DataSample> getDataSamples(Date date, Iteration iteration, String aspect){
		Criteria criteria = createCriteria();
		criteria.add(Restrictions.eq(DataSample.SAMPLE_TIME, date));
		criteria.add(Restrictions.eq(DataSample.REFERENCE_ID, iteration.getId()));
		criteria.add(Restrictions.eq(DataSample.ASPECT, aspect));
		return criteria.list();
	}
}