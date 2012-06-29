package com.technoetic.xplanner.domain.repository;

import java.util.List;

import net.sf.xplanner.domain.PersonRole;
import net.sf.xplanner.domain.Role;

import org.hibernate.HibernateException;

public class RoleAssociationRepositoryImpl extends HibernateObjectRepository
        implements RoleAssociationRepository {
    private RoleRepository roleRepository;

    public RoleAssociationRepositoryImpl() throws HibernateException {
        super(PersonRole.class);
    }

    public void deleteAllForPersonOnProject(int personId, int projectId) throws RepositoryException {
    	List list = getHibernateTemplate().find("select assoc from assoc in " + PersonRole.class +
                                     " where assoc.id.personId = ? and assoc.id.projectId = ?", new Object[]{new Integer(personId), new Integer(projectId)});
    	getHibernateTemplate().deleteAll(list);
   }

    public void deleteForPersonOnProject(String roleName, int personId, int projectId) throws RepositoryException {
    	Role role = roleRepository.findRoleByName(roleName);
    	List list = getHibernateTemplate().find("select assoc from assoc in " + PersonRole.class +
                " where assoc.id.personId = ? and assoc.id.projectId = ? and assoc.id.roleId = ?", new Object[]{new Integer(personId), new Integer(projectId), new Integer(role.getId())});
    	getHibernateTemplate().deleteAll(list);

    }

    public void insertForPersonOnProject(String roleName, int personId, int projectId)
            throws RepositoryException {
        Role role = roleRepository.findRoleByName(roleName);
        if (role != null) {
           getHibernateTemplate().save(new PersonRole(projectId, personId, role.getId()));
        }
    }

    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
}
