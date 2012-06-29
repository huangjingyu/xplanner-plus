/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Mar 26, 2005
 * Time: 9:29:01 AM
 */
package com.technoetic.xplanner.db.hibernate;

import java.sql.Connection;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.technoetic.xplanner.util.LogUtil;

public class IdGenerator {
  int nextId;

  protected static final Logger LOG = LogUtil.getLogger();

  static IdGenerator instance;
  public static final int NEXT_ID_COL_INDEX = 1;

  public static String getUniqueId(String prefix) {
    return prefix + getInstance().getNext();
  }

  public static int getNextPersistentId() throws Exception {
    return getInstance().getFromDB(HibernateHelper.getConnection());
  }

  public static void setNextPersistentId(int id) throws Exception {
    getInstance().setInDB(HibernateHelper.getConnection(), id);
  }

  private int getNext() {
    return nextId++;
  }

  private static IdGenerator getInstance() {
    if (instance == null) {
      instance = new IdGenerator();
    }
    return instance;
  }

  private IdGenerator() {
    try {
      nextId = getFromDB(HibernateHelper.getConnection());
    } catch (Exception e) {
      LOG.error(e);
    }
  }

  public static int getFromDB(Connection connection) throws Exception {
    return newTemplate(connection).queryForInt(HibernateIdentityGenerator.GET_NEXT_ID_QUERY);
  }

  public static void setInDB(Connection connection, int nextValue) throws Exception {
    newTemplate(connection).update(HibernateIdentityGenerator.SET_NEXT_ID_QUERY, new Object[]{new Integer(nextValue)});
  }

  private static JdbcTemplate newTemplate(Connection connection) {
    return new JdbcTemplate(new SingleConnectionDataSource(connection, true));
  }

  public static void main(String[] args) {
    System.out.println("id=" + getUniqueId("test"));
    System.out.println("id=" + getUniqueId("test"));
    System.out.println("id=" + getUniqueId("test"));
  }

}