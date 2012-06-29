package com.technoetic.xplanner.file;

import java.sql.Blob;

import com.technoetic.xplanner.domain.Identifiable;

public class File2 implements Identifiable {
    private int id;
    private String name;
    private String contentType;
    private Blob data;
    private long fileSize;
    private Directory2 directory;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Blob getData() {
        return data;
    }

    public void setData(Blob data) {
        this.data = data;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Directory2 getDirectory() {
        return directory;
    }

    protected void setDirectory(Directory2 directory) {
        this.directory = directory;
    }


  public String toString() {
    return "File{" +
           "id=" + id +
           ", name='" + name + '\'' +
           ", directory=" + directory +
           ", contentType='" + contentType + '\'' +
           ", fileSize=" + fileSize +
           '}';
  }
}
