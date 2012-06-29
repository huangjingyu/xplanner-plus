package net.sf.xplanner.domain;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
@Table(name = "patches")
public class Patches implements java.io.Serializable {
	private static final long serialVersionUID = 3888790427234669567L;
	private String systemName;
	private int patchLevel;
	private Date patchDate;
	private char patchInProgress;

	public Patches() {
	}

	public Patches(String systemName, int patchLevel, Date patchDate,
			char patchInProgress) {
		this.systemName = systemName;
		this.patchLevel = patchLevel;
		this.patchDate = patchDate;
		this.patchInProgress = patchInProgress;
	}

	@Id
	@Column(name = "system_name", unique = true, nullable = false, length = 30)
	public String getSystemName() {
		return this.systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	@Column(name = "patch_level", nullable = false)
	public int getPatchLevel() {
		return this.patchLevel;
	}

	public void setPatchLevel(int patchLevel) {
		this.patchLevel = patchLevel;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "patch_date", nullable = false, length = 19)
	public Date getPatchDate() {
		return this.patchDate;
	}

	public void setPatchDate(Date patchDate) {
		this.patchDate = patchDate;
	}

	@Column(name = "patch_in_progress", nullable = false, length = 1)
	public char getPatchInProgress() {
		return this.patchInProgress;
	}

	public void setPatchInProgress(char patchInProgress) {
		this.patchInProgress = patchInProgress;
	}

}
