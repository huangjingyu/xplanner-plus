package com.technoetic.xplanner.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.struts.upload.FormFile;

public class InputStreamFormFile implements FormFile {
    private byte[] fileData;
    private String fileName;
    private int fileSize;
    private String contentType;

    public InputStreamFormFile(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte[] buffer = new byte[8192];
        int bytesRead = 0;
        while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
            baos.write(buffer, 0, bytesRead);
        }

        fileData = baos.toByteArray();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getFileData() throws FileNotFoundException, IOException {
        return fileData;
    }

    public InputStream getInputStream() throws FileNotFoundException, IOException {
        return new ByteArrayInputStream(fileData);
    }

    public void destroy() {
        fileName = null;
        fileSize = -1;
        contentType = null;
        fileData = null;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }

        InputStreamFormFile that = (InputStreamFormFile)obj;
        return ((fileData == null ? that.fileData == null : Arrays.equals(fileData, that.fileData))
                && (fileName == null ? that.fileName == null : fileName.equals(that.fileName))
                && (fileSize == that.fileSize)
                && (contentType == null ? that.contentType == null : contentType.equals(that.contentType))
                && true);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("InputStreamFormFile - ");
        sb.append("fileName: " + fileName + "\n");
        sb.append("fileSize: " + fileSize + "\n");
        sb.append("contentType: " + contentType + "\n");
        sb.append("fileData: " + new String(fileData) + "\n");
        return sb.toString();
    }

}
