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

package net.sf.xplanner.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.technoetic.xplanner.domain.Identifiable;

/**
 *
 * @author Maksym
 */
@Entity
@Table(name="object_type")
public class ObjectType extends DomainObject implements Identifiable {
    private static final long serialVersionUID = 1L;

    @Column(unique=true, length=32)
    private String name;

    @Column(length=255)
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "net.sf.xplanner.domain.ObjectType[name=" + name + "]";
    }

}
