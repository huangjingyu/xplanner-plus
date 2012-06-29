package com.technoetic.xplanner.acceptance;

import java.sql.SQLException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

import com.technoetic.mocks.hibernate.MockSessionFactory;
import com.technoetic.xplanner.db.hibernate.GlobalSessionFactory;
import com.technoetic.xplanner.db.hibernate.HibernateHelper;
import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.domain.PersistentObjectMother;

public class DatabaseSupport {
   protected Session session;
   protected Level previousLoggingLevel;
   PersistentObjectMother mom;

   public DatabaseSupport() { }

   protected void setUp() throws Exception {
      previousLoggingLevel = Logger.getRootLogger().getLevel();
      Logger.getRootLogger().setLevel(Level.WARN);
      initializeHibernate();
      mom = new PersistentObjectMother();
      openSession();
   }

   private void initializeHibernate() throws HibernateException {
      if (isMockFactoryInstalled()) GlobalSessionFactory.set(null);
      HibernateHelper.initializeHibernate();
   }

   private boolean isMockFactoryInstalled() {
      return GlobalSessionFactory.get() != null && GlobalSessionFactory.get() instanceof MockSessionFactory;
   }

   protected void tearDown() throws Exception {
      try {
         openSession();
         mom.deleteTestObjects();
         Logger.getRootLogger().setLevel(previousLoggingLevel);
         commitSession();
      } catch (Exception e) {
         e.printStackTrace();
         rollbackSession();
         throw e;
      } finally {
         closeSession();
      }
   }


   public Session commitCloseAndOpenSession() throws HibernateException, SQLException {
      commitSession();
      closeSession();
      return openSession();
   }

   public Session openSession() throws HibernateException {
      if (isSessionOpened()) return session;
      session = GlobalSessionFactory.get().openSession();
      ThreadSession.set(session);
      mom.setSession(session);
      return session;
   }

   public void rollbackSession() {
      if (!isSessionOpened()) return;
      try {
         session.connection().rollback();
      } catch (Exception e) {
         e.printStackTrace();
      }

   }

   public void commitSession() throws HibernateException, SQLException {
      if (!isSessionOpened()) return;
      session.flush();
      session.connection().commit();
   }

   private boolean isSessionOpened() {
      return session != null && session.isOpen();
   }

   public void closeSession() {
      try {
         if (!isSessionOpened()) return;
         session.close();
         session = null;
         ThreadSession.set(null);
      } catch (HibernateException e) {
         e.printStackTrace();
      }
   }


   public PersistentObjectMother getMom() {
      return mom;
   }

   public Session getSession() throws HibernateException {
      openSession();
      return session;
   }
}