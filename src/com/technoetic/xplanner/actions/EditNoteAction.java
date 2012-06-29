package com.technoetic.xplanner.actions;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import net.sf.xplanner.domain.Directory;
import net.sf.xplanner.domain.File;
import net.sf.xplanner.domain.Note;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

import com.technoetic.xplanner.file.FileSystem;
import com.technoetic.xplanner.forms.NoteEditorForm;
public class EditNoteAction extends EditObjectAction<Note> {
    private FileSystem fileSystem;
	protected void populateObject(HttpServletRequest request, Object object, ActionForm form) throws Exception {
        Logger.getLogger(EditNoteAction.class).debug("Populating...");

        super.populateObject(request, object, form);

        NoteEditorForm noteForm = (NoteEditorForm)form;

        FormFile formFile = noteForm.getFormFile();
        if (formFile != null) {
            String filename = formFile.getFileName();
            if (StringUtils.isNotEmpty(filename)) {
                String contentType = formFile.getContentType();
                InputStream input = formFile.getInputStream();
                int fileSize = formFile.getFileSize();
                int projectId = request.getParameter("projectId") != null ?
                        Integer.parseInt(request.getParameter("projectId")) : 0;
                Directory directory = fileSystem.getDirectory("/attachments/project/"+projectId);
                File file = fileSystem.createFile(getSession(request), directory, filename, contentType, fileSize, input);
                Note note = (Note)object;
                note.setFile(file);

                Logger.getLogger(EditNoteAction.class).debug("Saving note: filename="
                        + filename + ", fileSize=" + fileSize + ", contentType=" + contentType);

            }
        }
    }

    public void setFileSystem(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }
}

