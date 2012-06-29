package com.technoetic.xplanner.security.install;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.xplanner.domain.Permission;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.PersonRole;
import net.sf.xplanner.domain.Role;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

import com.tacitknowledge.util.migration.MigrationContext;
import com.tacitknowledge.util.migration.MigrationException;
import com.technoetic.xplanner.db.hibernate.GlobalSessionFactory;
import com.technoetic.xplanner.db.hibernate.HibernateHelper;
import com.technoetic.xplanner.db.hsqldb.HsqlServer;

public class BootstrapSystemUser {
   private final Logger log = Logger.getLogger(getClass());
   protected static final String SYSADMIN_USER_ID = "sysadmin";
   protected static final int PATCH_LEVEL = 2;
   protected static final String PATCH_NAME = "XPlanner bootstrap";

   public BootstrapSystemUser() {
//      setLevel(new Integer(PATCH_LEVEL));
//      setName(PATCH_NAME);
   }

   public void run(String sysadminId) throws Exception {
      try {
         //HibernateHelper.initializeHibernate();
         Session session = GlobalSessionFactory.get().openSession();
         session.connection().setAutoCommit(false);
         try {
            List people = session.find("from person in class " + Person.class.getName() +
                                       " where person.userId = ?",
                                       sysadminId, Hibernate.STRING);
            Iterator personItr = people.iterator();
            Person sysadmin;
            if (personItr.hasNext()) {
               sysadmin = (Person) personItr.next();
               log.info("using " + sysadminId + " user");
            } else {
               sysadmin = createSysAdmin(sysadminId, session);
            }
            Role viewerRole = initializeRole(session, "viewer", 1, 8);
            Role editorRole = initializeRole(session, "editor", 2, 7);
            Role adminRole = initializeRole(session, "admin", 3, 6);
            Role sysadminRole = initializeRole(session, SYSADMIN_USER_ID, 4, 5);
            addRoleAssociation(session, sysadminRole.getId(), sysadmin.getId(), 0);
            createPermission(session, sysadminRole, "%", "%");
            createNegativePermission(session, editorRole, "system.project", "create.project");
            createNegativePermission(session, editorRole, "system.person", "create.person");
            createNegativePermission(session, adminRole, "system.project", "create.project");
            createPermission(session, adminRole, "%", "admin%");
            createPermission(session, editorRole, "%", "create%");
            createPermission(session, editorRole, "%", "edit%");
            createPermission(session, editorRole, "%", "integrate%");
            createPermission(session, editorRole, "%", "delete%");
            createPermission(session, viewerRole, "%", "read%");
            session.flush();
            session.connection().commit();
         } finally {
            session.close();
         }
      } catch (Exception e) {
         e.printStackTrace();
         throw e;
      }
   }

   private Person createSysAdmin(String sysadminId, Session session) throws HibernateException {
      Person sysadmin;
      log.info("creating " + sysadminId + " user");
      sysadmin = new Person();
      sysadmin.setUserId(sysadminId);
      sysadmin.setName(sysadminId);
      sysadmin.setInitials("SYS");
      sysadmin.setEmail("no@reply.com");
      sysadmin.setPassword("1tGWp1Bdm02Sw4bD7/o0N2ao405Tf8kjxGBW/A=="); // password=admin
      sysadmin.setLastUpdateTime(new Date());
      session.save(sysadmin);
      return sysadmin;
   }

   private void addRoleAssociation(Session session, int roleId, int personId, int projectId) throws HibernateException {
      session.save(new PersonRole(projectId, personId, roleId));
   }

   private void createPermission(Session session, Role sysadminRole, String resourceType, String permissionName)
         throws HibernateException {
      Permission permission = new Permission(resourceType, 0, sysadminRole.getId(), permissionName);
      session.save(permission);
   }

   private void createNegativePermission(Session session, Role sysadminRole, String resourceType, String permissionName)
         throws HibernateException {
      Permission permission = new Permission(resourceType, 0, sysadminRole.getId(), permissionName);
      permission.setPositive(false);
      session.save(permission);
   }

   private Role initializeRole(Session session, String roleName, int left, int right) throws HibernateException {
      List roles = session.find("from role in class " + Role.class.getName() + " where role.name = ?",
                                roleName, Hibernate.STRING);
      Role role;
      if (roles.size() == 0) {
         log.info("creating role: " + roleName);
         role = new Role(roleName);
         role.setLeft(left);
         role.setRight(right);
         session.save(role);
      } else {
         role = (Role) roles.get(0);
      }
      return role;
   }

   public void migrate(MigrationContext context) throws MigrationException {
      try {
         run(SYSADMIN_USER_ID);
      } catch (Exception e) {
         throw new MigrationException("error during migration", e);
      }
      finally {
         HsqlServer.shutdown();
      }
   }

   public static void main(String[] args) {
      String sysadminId = SYSADMIN_USER_ID;
      if (args.length == 1) {
         sysadminId = args[0];
      }
      BootstrapSystemUser action = new BootstrapSystemUser();
      try {
         action.run(sysadminId);
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         HsqlServer.shutdown();
      }
   }
}
