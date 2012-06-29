package net.sf.xplanner.domain;


import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

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

@Entity
@Table(name = "attribute")
public class Attribute implements java.io.Serializable, Identifiable {
	private static final long serialVersionUID = -7560899355285382322L;
	private AttributeId id;
	private String value;

	public Attribute() {
	}

	public Attribute(AttributeId id) {
		this.id = id;
	}

	public Attribute(AttributeId id, String value) {
		this.id = id;
		this.value = value;
	}

	/**
	 * @param id2
	 * @param attributeName
	 * @param attributeValue
	 */
	public Attribute(int targetId, String attributeName, String attributeValue) {
		this(new AttributeId(targetId, attributeName), attributeValue);
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "targetId", column = @Column(name = "targetId", nullable = false)),
			@AttributeOverride(name = "name", column = @Column(name = "name", nullable = false)) })
	public AttributeId getAttributeId() {
		return this.id;
	}

	public void setAttributeId(AttributeId id) {
		this.id = id;
	}

	@Column(name = "\"value\"")
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Transient
	public String getName() {
		return id.getName();
	}
	
	@Transient
	public int getId() {
		return 0;
	}
}
