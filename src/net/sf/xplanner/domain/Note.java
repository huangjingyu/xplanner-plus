package net.sf.xplanner.domain;


import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.HibernateException;

import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.domain.Nameable;
import com.technoetic.xplanner.tags.DomainContext;

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
@Table(name = "note")
public class Note extends DomainObject implements java.io.Serializable, Nameable {
	private static final long serialVersionUID = 4379309425634770729L;
	private int attachedToId;
	private Integer authorId;
	private String subject;
	private String body;
	private Date submissionTime;
	private File file;
	public static final String ATTACHED_NOTES_QUERY = "AttachedNotesQuery";

	public Note() {
	}

	@Column(name = "attachedTo_id")
	public int getAttachedToId() {
		return this.attachedToId;
	}

	public void setAttachedToId(int attachedToId) {
		this.attachedToId = attachedToId;
	}

	@Column(name = "author_id")
	public Integer getAuthorId() {
		return this.authorId;
	}

	public void setAuthorId(Integer authorId) {
		this.authorId = authorId;
	}

	@Column(name = "subject")
	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Column(name = "body", length = 65535)
	public String getBody() {
		return this.body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "submission_time", length = 19)
	public Date getSubmissionTime() {
		return this.submissionTime;
	}

	public void setSubmissionTime(Date submissionTime) {
		this.submissionTime = submissionTime;
	}
	
	@OneToOne
	@JoinColumn(name="attachment_id")
	public File getFile(){
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	@Transient @Deprecated
	 public int getAttachmentCount() throws HibernateException
	   {
	      List noteList = null;
	      noteList =
	         ThreadSession.get().find("select note from Note note where note.file.id=" + this.getFile().getId());
	      if (noteList != null)
	      {
	         return noteList.size();
	      }
	      else
	      {
	         return 0;
	      }
	   }

	
	@Transient @Deprecated
	   public DomainObject getParent()
	   {
	      //DEBT: Remove the cycle. Note should not depends on a web tier operation
	      return DomainContext.getNoteTarget(getAttachedToId());
	   }

	@Transient
	@Override
	public String getName() {
		return getSubject();
	}

	@Transient
	@Override
	public String getDescription() {
		return getBody();
	}



}
