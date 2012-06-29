package com.technoetic.xplanner.domain.repository;

import com.technoetic.xplanner.domain.Nameable;

@Deprecated
public interface ObjectRepository {
    /**
     * Delete an object using it's object ID (OID).
     * @param objectIdentifier
     */
    void delete(int objectIdentifier) throws RepositoryException;

    /**
     * Load an instance of an object
     * @param objectIdentifier
     */
    Object load(int objectIdentifier) throws RepositoryException;

    /**
     * Create a new instance in the repository
     * @param object the object to insert
     * @return the object identifier
     */
    int insert(Nameable object) throws RepositoryException;

    /**
     * Updates an object in the repository. Note: This is a no-op for Hibernate.
     * @param object - the object to update
     */
    void update(Nameable object) throws RepositoryException;
}
