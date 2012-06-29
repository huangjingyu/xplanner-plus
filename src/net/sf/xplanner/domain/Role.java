package net.sf.xplanner.domain;


import java.security.Principal;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

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
@Table(name = "roles", uniqueConstraints = @UniqueConstraint(columnNames = "role"))
public class Role implements java.io.Serializable, Principal, Identifiable {
	public static final String SYSADMIN = "sysadmin";
	public static final String ADMIN = "admin";
	public static final String EDITOR = "editor";
	public static final String VIEWER = "viewer";
	private static final long serialVersionUID = 4921403899778522202L;
	private Integer left;
	private Integer right;
	private String name;
	private int id;

	public Role() {
	}

	public Role(String name) {
		setName(name);
	}

	@Column(name = "lft")
	public Integer getLeft() {
		return this.left;
	}

	public void setLeft(Integer lft) {
		this.left = lft;
	}

	@Column(name = "rgt")
	public Integer getRight() {
		return this.right;
	}

	public void setRight(Integer rgt) {
		this.right = rgt;
	}
	
	@Override
	@Column(name="role", nullable=false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Id
	@GeneratedValue(generator="commonId")
	@GenericGenerator(name="commonId", strategy="com.technoetic.xplanner.db.hibernate.HibernateIdentityGenerator")
	@Column(name = "id", unique = true, nullable = false)
	@Override
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
