package com.technoetic.xplanner.acceptance;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.PersonRole;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Role;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.db.hibernate.GlobalSessionFactory;
import com.technoetic.xplanner.db.hibernate.HibernateHelper;
import com.technoetic.xplanner.domain.Feature;
import com.technoetic.xplanner.domain.TaskDisposition;
import com.technoetic.xplanner.security.module.LoginSupportImpl;
import com.technoetic.xplanner.security.module.XPlannerLoginModule;

public class LargeDatabasePopulator {
    private static Logger log = Logger.getLogger(LargeDatabasePopulator.class);

    public static void main(String[] args) {
        try {
            HibernateHelper.initializeHibernate();
            for (int i = 0; i < 1; i++) {
                //new Thread(new PopulationTask(30,30,10,5, 100)).start();
                new Thread(new PopulationTask(0, 0, 0, 0, 100)).start();
            }
        } catch (Exception e) {
            log.error("error", e);
        }
    }

    private static class PopulationTask implements Runnable {
        private final Logger log = Logger.getLogger(getClass());
        private int projectCount = 100;
        private int iterationCount = 100;
        private int storyCount = 30;
        private int taskCount = 5;
        private int personCount = 100;
        private Session session;
        public static final long HOUR = 60 * 60 * 1000L;
        public static final long DAY = 24 * HOUR;
        private Iteration iteration;

        public PopulationTask(int projectCount, int iterationCount, int storyCount,
                int taskCount, int personCount) {
            this.projectCount = projectCount;
            this.iterationCount = iterationCount;
            this.storyCount = storyCount;
            this.taskCount = taskCount;
            this.personCount = personCount;
        }

        public void run() {
            try {
                session = GlobalSessionFactory.get().openSession();
                for (int i = 0; i < projectCount; i++) {
                    Project project = newProject();
                    session.flush();
                    session.connection().commit();
                    log.info("project " + i);
                    for (int j = 0; j < iterationCount; j++) {
                        iteration = newIteration(project, iteration);
                        session.flush();
                        session.connection().commit();
                        log.info("iteration " + i + " " + j);
                        for (int k = 0; k < storyCount; k++) {
                            UserStory story = newUserStory(iteration);
                            session.flush();
                            session.connection().commit();
                            log.info("story " + i + " " + j + " " + k);
                            for (int l = 0; l < taskCount; l++) {
                                /*Task task = */newTask(story);
                                log.info("task " + i + " " + j + " " + k + " " + l);
                                session.flush();
                                session.connection().commit();
                            }
                        }
                        iteration = null;
                    }
                }
                List projects = session.find("from project in " + Project.class);
                XPlannerLoginModule encryptor = new XPlannerLoginModule(new LoginSupportImpl());
                for (int i = 0; i < personCount; i++) {
                    Person person = new Person();
                    session.save(person);
                    person.setName("Person " + person.getId());
                    String initials = "P" + person.getId();
                    person.setInitials(initials);
                    person.setPassword(encryptor.encodePassword(initials, null));
                    Role role = getRole(session, "sysadmin");
                    for (int j = 0; j < projects.size(); j++) {
                        Project project = (Project)projects.get(j);
                        session.save(new PersonRole(project.getId(), person.getId(), role.getId()));
                    }
                    System.out.println("person "+i);
                }
                session.flush();
                session.connection().commit();
            } catch (Exception e) {
                log.error("error", e);
            }
        }

        public static Role getRole(Session session, String rolename) throws HibernateException {
            List roles = session.find("from role in class " +
                    Role.class.getName() + " where role.name = ?",
                    rolename, Hibernate.STRING);
            Role role = null;
            Iterator roleIterator = roles.iterator();
            if (roleIterator.hasNext()) {
                role = (Role)roleIterator.next();
            }
            return role;
        }

        public Project newProject() throws HibernateException {
            Project project = new Project();
            project.setName("Test project");
            session.save(project);
            project.setName("Test project " + project.getId());
            return project;
        }

        public Iteration newIteration(Project project, Iteration previousIteration) throws HibernateException {
            Iteration iteration = new Iteration();
            iteration.setProject(project);
            if (previousIteration == null) {
                iteration.setStartDate(new Date(System.currentTimeMillis() - DAY));
                iteration.setEndDate(new Date(System.currentTimeMillis() + DAY));
            } else {
                iteration.setStartDate(previousIteration.getEndDate());
                iteration.setEndDate(new Date(previousIteration.getEndDate().getTime() + DAY));

            }
            session.save(iteration);
            project.getIterations().add(iteration);
            iteration.setName("Test iteration");
            iteration.setName("Test iteration " + iteration.getId());
            return iteration;
        }

        public UserStory newUserStory(Iteration iteration) throws HibernateException {
            UserStory story = new UserStory();
            story.setName("Test userstory");
            story.setIteration(iteration);
            iteration.getUserStories().add(story);
            session.save(story);
            story.setName("Test userstory" + story.getId());
            return story;
        }

        public Task newTask(UserStory story) throws HibernateException {
            Task task = new Task();
            task.setUserStory(story);
            story.getTasks().add(task);
            task.setType("");
            task.setDisposition(TaskDisposition.PLANNED);
            task.setName("Test task");
            session.save(task);
            task.setName("Test task " + task.getId());
            return task;
        }

        public Feature newFeature(UserStory story) throws HibernateException {
        Feature feature = new Feature();
        feature.setStory(story);
        story.getFeatures().add(feature);
        feature.setDescription("Description of test feature");
        feature.setName("Test feature");
        session.save(feature);
        feature.setName("Test feature " + feature.getId());
        return feature;
    }
    }
}