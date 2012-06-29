/**
 * 
 */
package net.sf.xplanner.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;

import com.technoetic.xplanner.domain.Identifiable;


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
public interface Dao<E extends Identifiable> {

	E getById(Serializable id);

	Criteria createCriteria();
	
	int save(E object);

	void delete(Serializable objectId);

	void delete(E object);

	void deleteAll(List<E> object);

	E getUniqueObject(String field, Object value);
	
	boolean isNewObject(E object);

	abstract Class<E> getDomainClass();
	
	void evict(E object);
}
