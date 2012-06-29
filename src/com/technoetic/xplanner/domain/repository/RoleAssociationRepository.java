package com.technoetic.xplanner.domain.repository;

public interface RoleAssociationRepository extends ObjectRepository {
    void deleteAllForPersonOnProject(int personId, int projectId) throws RepositoryException;

    void deleteForPersonOnProject(String roleName, int personId, int projectId) throws RepositoryException;

    void insertForPersonOnProject(String roleName, int personId, int projectId) throws RepositoryException;
}
