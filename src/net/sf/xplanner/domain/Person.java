package net.sf.xplanner.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.technoetic.xplanner.domain.Nameable;

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
@Table(name = "person", uniqueConstraints = @UniqueConstraint(columnNames = "userId"))
public class Person extends DomainObject implements java.io.Serializable, Nameable {
	private static final long serialVersionUID = -8209945280001173802L;
	public static final String USER_ID = "userId";
	private String email;
	private String phone;
	private String initials;
	private String userId;
	private String password;
	private Boolean isHidden;
	private String name;

	public Person() {
	}
	
    /**
     * Public constructor
     * @param userId user identity
     */
    public Person(String userId) {
        this.userId = userId;
    }

	@Column(name = "email")
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "phone")
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "initials")
	public String getInitials() {
		return this.initials;
	}

	public void setInitials(String initials) {
		this.initials = initials;
	}

	@Column(name = "userId", unique = true)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "password")
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "is_hidden")
	public Boolean getHidden() {
		return this.isHidden;
	}

	public void setHidden(Boolean isHidden) {
		this.isHidden = isHidden;
	}
	
	@Column(name = "name")
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Transient
	public String getDescription() {
		return null;
	}

}
