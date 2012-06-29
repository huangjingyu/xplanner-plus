package com.technoetic.xplanner.domain;

import java.util.Date;
import java.util.List;

import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.File;

import org.hibernate.HibernateException;

import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.tags.DomainContext;

public class Note2 extends DomainObject implements Nameable
{
   private int attachedToId;
   private int authorId;
   private String subject;
   private String body;
   private Date submissionTime = new Date();
   private File file;
   public static final String ATTACHED_NOTES_QUERY = "AttachedNotesQuery";

   public void setAttachedToId(int attachedToId)
   {
      this.attachedToId = attachedToId;
   }

   public int getAttachedToId()
   {
      return attachedToId;
   }

   public void setAuthorId(int authorId)
   {
      this.authorId = authorId;
   }

   public int getAuthorId()
   {
      return authorId;
   }

   public void setSubject(String subject)
   {
      this.subject = subject;
   }

   public String getSubject()
   {
      return subject;
   }

   public void setBody(String body)
   {
      this.body = body;
   }

   public String getBody()
   {
      return body;
   }

   public void setFile(File file)
   {
      this.file = file;
   }

   public File getFile()
   {
      return file;
   }

   public void setSubmissionTime(Date submissionTime)
   {
      this.submissionTime = submissionTime;
   }

   public Date getSubmissionTime()
   {
      return submissionTime;
   }

   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (!(o instanceof Note2)) return false;
      //todo Get rid of this hack checking id == 0. Jacques will add an abstract fieldsEquals member to DomainObject that all inheriting classes will implement.
      if (getId() != 0 && !super.equals(o)) return false;

      final Note2 note = (Note2) o;

      if (attachedToId != note.attachedToId) return false;
      if (authorId != note.authorId) return false;
      if (body != null ? !body.equals(note.body) : note.body != null) return false;
      if (file != null ? isFilenameEqual(note.file) : note.file != null) return false;
      if (subject != null ? !subject.equals(note.subject) : note.subject != null) return false;
      return !(submissionTime != null ?
               !submissionTime.equals(note.submissionTime) :
               note.submissionTime != null);

   }

   private boolean isFilenameEqual(File otherFile)
   {
      return otherFile != null && file.getName() != null ?
         !file.getName().equals(otherFile.getName()) :
         false;
   }

   public String toString()
   {
      StringBuffer sb = new StringBuffer();
      sb.append("Note - ");
      sb.append("filename: " + (file != null ? file.getName() : "none") + "\n");
      sb.append("file: " + file + "\n");
      sb.append("attachedToId: " + attachedToId + "\n");
      sb.append("authorId: " + authorId + "\n");
      sb.append("body: " + body + "\n");
      sb.append("subject: " + subject + "\n");
      sb.append("submissionTime: " + submissionTime + "\n");

      return sb.toString();
   }

   public String getName()
   {
      return getSubject();
   }

   public String getDescription()
   {
      return getBody();
   }

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

   public DomainObject getParent()
   {
      //DEBT: Remove the cycle. Note should not depends on a web tier operation
      return DomainContext.getNoteTarget(getAttachedToId());
   }
}
