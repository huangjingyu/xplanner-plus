package com.technoetic.xplanner.domain.repository;

import net.sf.xplanner.domain.Role;

public interface RoleRepository extends ObjectRepository {
    Role findRoleByName(String name) throws RepositoryException;
}
