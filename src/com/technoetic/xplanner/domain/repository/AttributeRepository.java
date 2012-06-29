package com.technoetic.xplanner.domain.repository;

import java.util.Map;

public interface AttributeRepository {
    void setAttribute(int targetId, String name, String value) throws RepositoryException;

    void delete(int targetId, String name) throws RepositoryException;

    Map getAttributes(int targetId, String prefix) throws RepositoryException;

    String getAttribute(int targetId, String name) throws RepositoryException;
}
