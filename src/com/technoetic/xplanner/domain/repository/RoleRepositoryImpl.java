package com.technoetic.xplanner.domain.repository;

import java.util.Iterator;
import java.util.List;

import net.sf.xplanner.domain.Role;

import org.hibernate.HibernateException;

public class RoleRepositoryImpl extends HibernateObjectRepository implements RoleRepository {
    public RoleRepositoryImpl() throws HibernateException {
        super(Role.class);
    }

    public Role findRoleByName(String rolename) throws RepositoryException {
        List roles = null;
       roles = getHibernateTemplate().find("from role in class " +
                                           Role.class.getName() + " where role.name = ?",
                                           rolename);
       Role role = null;
        Iterator roleIterator = roles.iterator();
        if (roleIterator.hasNext()) {
            role = (Role)roleIterator.next();
        }
        return role;
    }
}
