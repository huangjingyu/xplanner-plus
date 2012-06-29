package com.technoetic.xplanner.domain.repository;

public class ObjectNotFoundException extends RepositoryException {
    public ObjectNotFoundException(String message) {
        super(message);
    }
}
