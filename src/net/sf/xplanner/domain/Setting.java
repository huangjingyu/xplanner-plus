/**
 *    XplannerPlus, agile planning software
 *    @author Maksym_Chyrkov. 
 *    Copyright (C) 2010  Maksym Chyrkov
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

import javax.persistence.*;

import net.sf.xplanner.domain.enums.SettingScope;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="object_type", discriminatorType = DiscriminatorType.STRING)
@Table(name="setting")
public class Setting extends NamedObject{
	private String category;
	private String defaultValue;
	private SettingScope scope;
	private ObjectType objectType;

	@ManyToOne(optional=false)
	public ObjectType getObjectType() {
		return objectType;
	}

	@Column(name = "category", length = 255)
	public String getCategory() {
		return category;
	}

	@Column(name = "defaultValue", length = 255)
	public String getDefaultValue() {
		return defaultValue;
	}

	@Column(name = "setting_scope")
	@Enumerated(EnumType.ORDINAL)
	public SettingScope getScope() {
		return scope;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	public void setScope(SettingScope scope) {
		this.scope = scope;
	}
	
	public void setObjectType(ObjectType objectType) {
		this.objectType = objectType;
	}
	
}
