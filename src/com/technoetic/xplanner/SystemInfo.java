/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Jan 1, 2006
 * Time: 2:44:27 AM
 */
package com.technoetic.xplanner;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.web.context.ServletContextAware;

import com.technoetic.xplanner.db.hsqldb.HsqlServer;
import com.technoetic.xplanner.util.LogUtil;

/** @noinspection StringContatenationInLoop,MagicNumber*/
public class SystemInfo implements ServletContextAware {
   static final long MEGABYTE = 1048576L;
   private Properties properties;
   private ServletContext servletContext;
   private SessionFactory sessionFactory;

   public Properties getProperties() {
      return properties;
   }

   public void setProperties(Properties properties) {
      this.properties = properties;
   }

   public void setSessionFactory(SessionFactory sessionFactory) {
     this.sessionFactory = sessionFactory;
   }

   public static Map<String,String> getSystemProperties() {
      Properties sysProps = System.getProperties();
      Map<String,String> props = new ListOrderedMap();
      props.put("System Date", DateFormat.getDateInstance().format(new Date()));
      props.put("System Time", DateFormat.getTimeInstance().format(new Date()));
      props.put("Current directory", getCurrentDirectory());

      props.put("Java Version", sysProps.getProperty("java.version"));
      props.put("Java Vendor", sysProps.getProperty("java.vendor"));
      props.put("JVM Version", sysProps.getProperty("java.vm.specification.version"));
      props.put("JVM Vendor", sysProps.getProperty("java.vm.specification.vendor"));
      props.put("JVM Implementation Version", sysProps.getProperty("java.vm.version"));
      props.put("Java Runtime", sysProps.getProperty("java.runtime.name"));
      props.put("Java VM", sysProps.getProperty("java.vm.name"));

      props.put("User Name", sysProps.getProperty("user.name"));
      props.put("User Timezone", sysProps.getProperty("user.timezone"));

      props.put("Operating System", sysProps.getProperty("os.name") + " " + sysProps.getProperty("os.version"));
      props.put("OS Architecture", sysProps.getProperty("os.arch"));

      return props;
   }

   private static String getCurrentDirectory() {
      try {
         return new File(".").getCanonicalPath();
      } catch (IOException e) {
         // Should not happen
         return "<undefined>";      }
   }

   public Map<String,String> getJVMStatistics() {
      Map<String,String> stats = new ListOrderedMap();
      stats.put("Total Memory", "" + getTotalMemory() + "MB");
      stats.put("Free Memory", "" + getFreeMemory() + "MB");
      stats.put("Used Memory", "" + getUsedMemory() + "MB");
      return stats;
   }

   public Map<String,String> getBuildInfo() {
      Map<String,String> stats = new ListOrderedMap();
      stats.put("Version", properties.getProperty(XPlannerProperties.XPLANNER_VERSION_KEY));
      stats.put("Build Date", properties.getProperty(XPlannerProperties.XPLANNER_BUILD_DATE_KEY));
      stats.put("Build Revision", properties.getProperty(XPlannerProperties.XPLANNER_BUILD_REVISION_KEY));
      stats.put("Build Package", properties.getProperty(XPlannerProperties.XPLANNER_BUILD_PACKAGE_KEY, "War"));
      return stats;
   }

   public Map<String,String> getDatabaseInfo() {
      Map<String,String> props = new ListOrderedMap();
      props.put("Dialect", properties.getProperty("hibernate.dialect"));
      props.put("Driver", properties.getProperty("hibernate.connection.driver_class"));
      props.put("Driver Version", getDriverVersion());
      props.put("Database Vendor", getDatabaseVendor());
      props.put("Database Version", getDatabaseVersion());
      props.put("Database Name", properties.getProperty("hibernate.connection.dbname"));
      props.put("Database Url", properties.getProperty(XPlannerProperties.CONNECTION_URL_KEY));
      if (HsqlServer.isLocalPublicDatabaseStarted()) {
        props.put("Database File", HsqlServer.getInstance().getDbPath());
      }
      props.put("Database User Name", properties.getProperty("hibernate.connection.username"));
      props.put("Database User Password",
                isEmpty(properties.getProperty("hibernate.connection.password"))?"[not set]":"******");
      props.put("Database Patch Level", getDatabasePatchLevel());
      return props;
   }

   private String getDatabaseVendor() {
     return (String)new HibernateTemplate(sessionFactory).execute(new HibernateCallback() {
       public Object doInHibernate(Session session) throws HibernateException, SQLException {
         return session.connection().getMetaData().getDatabaseProductName();
       }
     });
   }

   private String getDatabaseVersion() {
     return (String)new HibernateTemplate(sessionFactory).execute(new HibernateCallback() {
       public Object doInHibernate(Session session) throws HibernateException, SQLException {
         return session.connection().getMetaData().getDatabaseProductVersion();
       }
     });
   }

	private String getDriverVersion() {
		return (String) new HibernateTemplate(sessionFactory)
				.execute(new HibernateCallback() {
					@Override
					public Object doInHibernate(org.hibernate.Session session)
							throws HibernateException, SQLException {
						return session.connection().getMetaData()
								.getDriverVersion();
					}
				});
	}

   private static String getDatabasePatchLevel() {
     try {
        return "to be fixed to use liquid";
     } catch (Exception e) {
        return "Unknown (Exception during retrieval: " + e.getMessage() + ")";
     }
   }
   public Map<String,String> getAppServerInfo() {
      Map<String,String> props = new ListOrderedMap();
      props.put("Application Server", servletContext.getServerInfo());
      props.put("Servlet Version", servletContext.getMajorVersion()+"."+servletContext.getMinorVersion());
      return props;
   }

   private static boolean isEmpty(String value) {
      return value == null || "".equals(value);
   }

   public long getTotalMemory() {
      return Runtime.getRuntime().totalMemory() / MEGABYTE;
   }

   public long getFreeMemory() {
      return Runtime.getRuntime().freeMemory() / MEGABYTE;
   }

   public long getUsedMemory() {
      return getTotalMemory() - getFreeMemory();
   }

   public void setServletContext(ServletContext servletContext) {
      this.servletContext = servletContext;
      LogUtil.getLogger().info("*********************** XPLANNER INFO ************************\n" + toString());
   }

   @Override
public String toString() {
      StringBuffer buf = new StringBuffer();
      buf.append(propertiesMapToString("Build", getBuildInfo()));
      buf.append(propertiesMapToString("Database", getDatabaseInfo()));
      buf.append(propertiesMapToString("App Server", getAppServerInfo()));
      buf.append(propertiesMapToString("System", getSystemProperties()));
      return buf.toString();
   }

   private static String propertiesMapToString(String mapName, Map<String,String> properties) {
      StringBuffer buf = new StringBuffer();
      buf.append(mapName);
      buf.append(":\n");

      Iterator<String> iterator = properties.keySet().iterator();
      while (iterator.hasNext()) {
         String name = (String) iterator.next();
         String value = (String) properties.get(name);
         buf.append("   ");
         buf.append(StringUtils.rightPad(name+":", 30));
         buf.append(value).append("\n");
      }
      buf.append("\n");
      return buf.toString();
   }

}
