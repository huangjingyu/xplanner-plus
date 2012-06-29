package com.technoetic.xplanner.actions;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.domain.Directory;
import net.sf.xplanner.domain.File;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.file.FileSystem;
import com.technoetic.xplanner.forms.FileManagerForm;

/**
 * EXPERIMENTAL - File Management Action
 */
public class FileManagerAction extends AbstractAction {
    private final int BUFFER_SIZE = 4000;
    private FileSystem fileSystem;

    @Override
	protected ActionForward doExecute(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Session hibernateSession = getSession(request);
        FileManagerForm fform = (FileManagerForm)form;
        try {
            hibernateSession.connection().setAutoCommit(false);
            if (fform.getAction() == null) {
                fform.setAction("list");
                fform.setDirectoryId(Integer.toString(
                        fileSystem.getRootDirectory().getId()));
            }
            if (fform.getAction().equals("upload")) {
                FormFile formFile = fform.getFormFile();
                fileSystem.createFile(hibernateSession, Integer.parseInt(fform.getDirectoryId()), formFile.getFileName(),
                        formFile.getContentType(), formFile.getFileSize(), formFile.getInputStream());
            } else if (fform.getAction().equals("download")) {
                File file = fileSystem.getFile(hibernateSession, Integer.parseInt(fform.getFileId()));
                writeFileToResponse(response, file);
            } else if (fform.getAction().equals("delete")) {
                fileSystem.deleteFile(hibernateSession, Integer.parseInt(fform.getFileId()));
            } else if (fform.getAction().equals("mkdir")) {
                int parentDirectoryId = Integer.parseInt(fform.getDirectoryId());
                fileSystem.createDirectory(hibernateSession, parentDirectoryId, fform.getName());
            } else if (fform.getAction().equals("rmdir")) {
                Directory directory = fileSystem.getDirectory(hibernateSession, Integer.parseInt(fform.getDirectoryId()));
                Directory parent = directory.getParent();
                if (parent == null) {
                    parent = fileSystem.getRootDirectory();
                }
                fform.setDirectoryId(Integer.toString(parent.getId()));
                fileSystem.deleteDirectory(hibernateSession, directory.getId());
            }
            hibernateSession.flush();
            hibernateSession.connection().commit();
            if (fform.getDirectoryId() != null) {
                Directory directory = fileSystem.getDirectory(hibernateSession, Integer.parseInt(fform.getDirectoryId()));
                request.setAttribute("directory", directory);
            }
            request.setAttribute("root", fileSystem.getRootDirectory());
            return mapping.findForward("display");
        } catch (ObjectNotFoundException ex) {
            request.setAttribute("exception", ex);
            return mapping.findForward("error/objectNotFound");
        } catch (Exception e) {
            hibernateSession.connection().rollback();
            throw e;
        }
    }

    private void writeFileToResponse(HttpServletResponse response, File file) throws IOException, SQLException {
        response.setContentType(file.getContentType());
        response.setHeader("Content-disposition", "note;filename=\"" +
                file.getName() + "\"");
        response.addHeader("Content-description", file.getName());
        ServletOutputStream stream = response.getOutputStream();
        InputStream attachmentStream = file.getData().getBinaryStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        int n = attachmentStream.read(buffer);
        while (n > 0) {
            stream.write(buffer, 0, n);
            n = attachmentStream.read(buffer);
        }
        stream.flush();
        stream.close();
    }

    public void setFileSystem(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }
}
