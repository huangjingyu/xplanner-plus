package com.technoetic.xplanner.security;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Role;

import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.db.hibernate.GlobalSessionFactory;
import com.technoetic.xplanner.db.hibernate.HibernateHelper;
import com.technoetic.xplanner.security.module.LoginSupportImpl;
import com.technoetic.xplanner.security.module.XPlannerLoginModule;

public class TomcatUserImporter {
    public static void main(String[] args) {
        Logger log = Logger.getLogger(TomcatUserImporter.class);

        String filename = null;
        if (args.length > 0) {
            filename = args[0];
        } else {
            log.error("usage: TomcatUserImporter filename");
            return;
        }

        Digester digester = new Digester();
        digester.setValidating(false);

        digester.addObjectCreate("tomcat-users", ArrayList.class);

        digester.addObjectCreate("tomcat-users/user", User.class);
        digester.addSetProperties("tomcat-users/user");
        digester.addSetNext("tomcat-users/user", "add");

        try {
            FileInputStream in = new FileInputStream(filename);
            List users = (List)digester.parse(in);

            HibernateHelper.initializeHibernate();
            Session session = GlobalSessionFactory.get().openSession();
            session.connection().setAutoCommit(false);
            XPlannerLoginModule encryptor = new XPlannerLoginModule(new LoginSupportImpl());
            Iterator userItr = users.iterator();
            while (userItr.hasNext()) {
                User user = (User)userItr.next();
                try {
                    List projects = session.find("from project in class " + Project.class.getName());
                    List people = session.find("from person in class " +
                            Person.class.getName() + " where userid = ?",
                            user.getUsername(), Hibernate.STRING);
                    Person person = null;
                    Iterator peopleIterator = people.iterator();
                    if (peopleIterator.hasNext()) {
                        person = (Person)peopleIterator.next();
                        if (person.getPassword() == null) {
                            log.info("setting password: user=" + user.getUsername());
                            person.setPassword(encryptor.encodePassword(user.getPassword(), null));
                        }
                        String[] roles = user.getRoles().split(",");
                        for (int i = 0; i < roles.length; i++) {
                            Iterator projectItr = projects.iterator();
                            while (projectItr.hasNext()) {
                                Project project = (Project)projectItr.next();
                                if (!isUserInRole(person, project.getId(), roles[i])) {
                                    Role role = new Role(roles[i]);
                                    log.info("adding role: user=" + user.getUsername() + ", role=" + roles[i]);
                                    session.save(role);
                                }
                            }
                        }
                    } else {
                        log.warn("no xplanner user: " + user.getUsername());
                    }
                    session.flush();
                    session.connection().commit();
                } catch (Throwable e) {
                    session.connection().rollback();
                }
            }
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isUserInRole(Person person, int projectId, String roleName) {
//        Iterator roleItr = person.getRoles().iterator();
//        while (roleItr.hasNext()) {
//            Role role = (Role)roleItr.next();
//            // do-before-release Check if Importer is still correct
//            if (role.fromNameKey().equals(roleName)) {
//                return true;
//            }
//        }
        return false;
    }

    public static class User {
        private String username;
        private String password;
        private String roles;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getRoles() {
            return roles;
        }

        public void setRoles(String roles) {
            this.roles = roles;
        }
    }
}
