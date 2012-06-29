/*
 * Copyright (c) 2005, Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.db.jdbc;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.collections.EnumerationUtils;

import com.technoetic.xplanner.XPlannerProperties;
import com.thoughtworks.proxy.toys.echo.Echoing;

public class DriverWrapper implements Driver {
   Driver driver;

   static {
      try {
         initWrapperDriver();
      } catch (SQLException e) {
         e.printStackTrace();
      }

   }

   private static void initWrapperDriver() throws SQLException {
      DriverManager.setLogWriter(new PrintWriter(System.err));
//      List registeredDrivers = new ArrayList(EnumerationUtils.toList(DriverManager.getDrivers()));
//      for (Iterator it = registeredDrivers.iterator(); it.hasNext();) {
//         Driver driver = (Driver) it.next();
//         DriverManager.deregisterDriver(driver);
//      }
      DriverManager.registerDriver(new DriverWrapper());
//      for (Iterator it = registeredDrivers.iterator(); it.hasNext();) {
//         Driver driver = (Driver) it.next();
//         DriverManager.registerDriver(driver);
//      }
   }

   private static Driver findDriver(String driverClassName) throws SQLException {
      Class driverClass = null;
      try {
         driverClass = Class.forName(driverClassName);
      } catch (ClassNotFoundException e) {
         throwDriverNotFoundException(driverClassName);
      }
      List registeredDrivers = new ArrayList(EnumerationUtils.toList(DriverManager.getDrivers()));
      for (Iterator it = registeredDrivers.iterator(); it.hasNext();) {
         Driver driver = (Driver) it.next();
         if (driver.getClass().equals(driverClass)) return driver;
      }
      throwDriverNotFoundException(driverClassName);
      return null; //never reached
   }

   private static void throwDriverNotFoundException(String driverClassName) throws SQLException {
      throw new SQLException("Could not find driver '" + driverClassName + "'");
   }

   public DriverWrapper() throws SQLException {
      String driverClassName = new XPlannerProperties().getProperty("xplanner.wrapped.driver");
      driver = findDriver(driverClassName);
      DriverManager.deregisterDriver(driver);
   }

   public int getMajorVersion() {return driver.getMajorVersion();}

   public int getMinorVersion() {return driver.getMinorVersion();}

   public boolean jdbcCompliant() {return driver.jdbcCompliant();}

   public boolean acceptsURL(String url) throws SQLException {return driver.acceptsURL(url);}

   public Connection connect(String url, Properties info) throws SQLException {
      return (Connection) Echoing.object(Connection.class, driver.connect(url, info));
   }

   public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
      return driver.getPropertyInfo(url, info);
   }

	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new SQLFeatureNotSupportedException();
	}
}
