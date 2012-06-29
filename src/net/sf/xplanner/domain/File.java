package net.sf.xplanner.domain;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
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
@Table(name = "xfile")
public class File extends DomainObject implements java.io.Serializable {
	private static final long serialVersionUID = -7604190182077162703L;
	private String name;
	private String contentType;
	private Blob data;
	private long fileSize;
	private Directory directory;

	public File() {
	}

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "content_type")
	public String getContentType() {
		return this.contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Lob
	@Column(name = "data", length=1000000000)
	public Blob getData() {
		return this.data;
	}

	public void setData(Blob data) {
		this.data = data;
	}

	@Column(name = "file_size")
	public long getFileSize() {
		return this.fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	@ManyToOne
	@JoinColumn(name="dir_id")
	public Directory getDirectory(){
		return this.directory;
	}
	public void setDirectory(Directory directory) {
		this.directory = directory;
	}


}
