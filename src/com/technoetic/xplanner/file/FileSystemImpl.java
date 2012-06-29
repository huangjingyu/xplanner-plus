package com.technoetic.xplanner.file;

import java.io.InputStream;
import java.util.List;

import net.sf.xplanner.domain.Directory;
import net.sf.xplanner.domain.File;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.db.hibernate.ThreadSession;

// Repository for files and directories
public class FileSystemImpl implements FileSystem {
    public Directory getRootDirectory() throws HibernateException {
        Session session = ThreadSession.get();
        List dirs = session.createQuery(
                "from dir in " + Directory.class + " where dir.parent is null").
                setMaxResults(1).list();
        if (dirs.iterator().hasNext()) {
            return (Directory)dirs.iterator().next();
        } else {
            Directory root = new Directory();
            root.setName("");
            session.save(root);
            session.flush();
            session.refresh(root);
            return root;
        }
    }

    public Directory getDirectory(Session session, int directoryId) throws HibernateException {
        return (Directory)session.load(Directory.class, new Integer(directoryId));
    }

    public Directory getDirectory(String path) throws HibernateException {
        String[] pathElements = path.split("/");
        Directory dir = getRootDirectory();
        Session session = ThreadSession.get();
        for (int i = 1; i < pathElements.length; i++) {
            List subdirectory = session.createQuery(
                    "from dir in "+Directory.class+" where dir.parent = :parent and dir.name = :name").
                    setParameter("parent", dir, Hibernate.entity(Directory.class)).
                    setParameter("name", pathElements[i]).
                    setMaxResults(1).list();
            if (subdirectory.size() > 0) {
                dir = (Directory)subdirectory.get(0);
            } else {
                dir = createDirectory(session, dir, pathElements[i]);
            }
        }
        return dir;
    }

    public File createFile(Session session, Directory directory, String name, String contentType,
            long size, InputStream data) throws HibernateException {
        File file = new File();
        file.setName(name);
        file.setContentType(contentType);
        file.setFileSize(size);
        if (data != null) {

            file.setData(Hibernate.createBlob(data, (int)size, session));
        }
        ThreadSession.get().save(file);
        directory.getFiles().add(file);
        return file;
    }

    public File createFile(Session session, int directoryId, String name, String contentType,
            long size, InputStream data) throws HibernateException {
        Directory directory = (Directory)session.load(Directory.class, new Integer(directoryId));
        return createFile(session, directory, name, contentType, size, data);
    }

    public File getFile(Session session, int fileId) throws HibernateException {
        return (File)session.load(File.class, new Integer(fileId));
    }

    public void deleteFile(Session session, int fileId) throws HibernateException {
        session.delete("from file in "+File.class+" where id = ?", new Integer(fileId), Hibernate.INTEGER);
    }

    public Directory createDirectory(Session session, int parentDirectoryId, String name) throws HibernateException {
        Directory parent = getDirectory(session, parentDirectoryId);
         return createDirectory(session, parent, name);
    }

    public Directory createDirectory(Session session, Directory parent, String name) throws HibernateException {
        Directory subdirectory = new Directory();
        subdirectory.setName(name);
        session.save(subdirectory);
        session.flush();
        session.refresh(subdirectory);
        parent.getSubdirectories().add(subdirectory);
        return subdirectory;
    }

    public void deleteDirectory(Session session, int directoryId) throws HibernateException {
        session.delete("from dir in "+Directory.class+" where id = ?",
                new Integer(directoryId), Hibernate.INTEGER);
    }
}
