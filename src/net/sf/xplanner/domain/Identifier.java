package net.sf.xplanner.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Table(name = "identifier")
public class Identifier implements java.io.Serializable {
	private static final long serialVersionUID = -8577533926128326398L;
	private int nextId;

	public Identifier() {
	}

	public Identifier(int nextId) {
		this.nextId = nextId;
	}

	@Id
	@Column(name = "nextId", nullable = false)
	public int getNextId() {
		return this.nextId;
	}

	public void setNextId(int nextId) {
		this.nextId = nextId;
	}

}
