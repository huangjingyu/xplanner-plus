package com.technoetic.xplanner.soap;

public class ObjectNotFoundException extends Exception {
    public ObjectNotFoundException() { }
    public ObjectNotFoundException(String message) { super(message); }
    public ObjectNotFoundException(Throwable cause) { super(cause); }
    public ObjectNotFoundException(String message, Throwable cause) { super(message, cause); }
    public ObjectNotFoundException(Class aClass, int id, ObjectNotFoundException ex) {
        this(aClass.getName() + " with id=" + id + " not found", ex);
    }
}
