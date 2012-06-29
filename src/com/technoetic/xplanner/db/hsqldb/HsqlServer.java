package com.technoetic.xplanner.db.hsqldb;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.hsqldb.DatabaseURL;
import org.hsqldb.Server;
import org.hsqldb.server.ServerConstants;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.db.hibernate.HibernateHelper;
import com.technoetic.xplanner.util.LogUtil;

/** @noinspection UseOfSystemOutOrSystemErr,EmptyCatchBlock*/

public class HsqlServer {
   public static final Logger LOG = LogUtil.getLogger();
   public static final String DATABASE_NAME = "xplanner";

   public static HsqlServer theServer;

   public static final String HSQLDB_DB_PATH = "xplanner.hsqldb.server.database";
   public static final String HSQLDB_URL = XPlannerProperties.CONNECTION_URL_KEY;
   public static final String WEBAPP_ROOT_TOKEN = "${WEBAPP_ROOT}";
   public static final String WEBAPP_ROOT_TOKEN_PATTERN_STRING = "\\$\\{WEBAPP_ROOT\\}";

   public static final String FILE_URL_PREFIX = DatabaseURL.S_URL_PREFIX+DatabaseURL.S_FILE;
   public static final String HSQLDB_FILE_URL_PATTERN_STRING = FILE_URL_PREFIX + ":([a-zA-Z0-9/\\\\]+)";
   public static final Pattern HSQLDB_FILE_URL_PATTERN = Pattern.compile(HSQLDB_FILE_URL_PATTERN_STRING);
   public static final String HSQL_URL_PREFIX = DatabaseURL.S_URL_PREFIX+DatabaseURL.S_HSQL;
   public static final String HSQLDB_HSQL_URL_PATTERN_STRING = HSQL_URL_PREFIX + "([a-zA-Z0-9.]*)(:([0-9]+))?/([a-zA-Z0-9]+)";
   public static final Pattern HSQLDB_HSQL_URL_PATTERN = Pattern.compile(HSQLDB_HSQL_URL_PATTERN_STRING);
   public static final String ABSOLUTE_PATH_PATTERN_STRING = "^([a-zA-Z]:|/|\\\\).*";
   public static final Pattern ABSOLUTE_PATH_PATTERN = Pattern.compile(ABSOLUTE_PATH_PATTERN_STRING);

   private String webRootPath = "";
   private Server server;
   private String databaseName;
   private String dbPath;
   private int port = 9001;

  public static void start() { start("");   }

   public static void start(String webRoot) {
     XPlannerProperties properties = new XPlannerProperties();
     if (!isHsqldb(properties)) return;

     theServer = new HsqlServer();
     theServer.initProperties(webRoot, properties);
     registerShutdownHook();

      if (isEmbeddedPrivateDatabase(properties)) {
         LOG.info("HSQL: detected an embedded database, in-process public server not started");
         return;
      }
      if (isRemoteDatabase(properties))  {
         LOG.info("HSQL: detected a remote database, in-process server not started");
         return;
      }
      if (isLocalOutOfProcessDatabase(properties))  {
         LOG.info("HSQL: detected a local out-of-process database, in-process server not started");
         return;
      }
      theServer.startPublicServer();
   }

  private static void registerShutdownHook() {
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        shutdown();
      }
    });
  }

  public void startPublicServer() {
    if (dbPath == null) {
      throw new RuntimeException(HSQLDB_DB_PATH +
                                 " property is required in order to start an in-process HSQLDB server");
    }

    LOG.info("Starting HSQLDB server for db " + databaseName + " stored at " + dbPath);

    server = createServer();
    server.setDatabasePath(0,dbPath);
    server.setDatabaseName(0,databaseName);
    server.setNoSystemExit(true);
    server.setPort(port);
    server.setTrace(isTraceOn());
    server.setSilent(!isTraceOn());
    server.start();

    waitForServerToTransitionOutOf(ServerConstants.SERVER_STATE_OPENING);
    LOG.info("HSQLDB server started");
  }

  public void initProperties(String webRootPath, XPlannerProperties properties) {
    this.webRootPath = webRootPath;

    if (isHsqlProtocol(properties)) {
      dbPath = getDbPath(properties);
      if (dbPath != null) {
        Matcher matcher = HSQLDB_HSQL_URL_PATTERN.matcher(getUrl(properties));
        if (matcher.matches()) {
          databaseName = getDatabaseName(matcher);
          port = getPort(matcher);
        }
      }
    }

    setUrl(properties, replaceWebRootToken(getUrl(properties)));


    LOG.debug("    " + dumpPropertyValue(HSQLDB_URL, properties));
    LOG.debug("    " + dumpPropertyValue(HSQLDB_DB_PATH, properties));
    LOG.debug("    " + dumpNameValuePair("webroot", webRootPath));
  }

  private String dumpPropertyValue(String property, XPlannerProperties properties) {
    return dumpNameValuePair(property, properties.getProperty(property));
  }

  private String dumpNameValuePair(String name, String value) {return name + "='" + value + "'";}

  private static String getUrl(XPlannerProperties properties) {return properties.getProperty(HSQLDB_URL);}
  private static void setUrl(XPlannerProperties properties, String value) { properties.setProperty(HSQLDB_URL, value);}

   private String getDatabaseName(Matcher matcher) {
      String name = matcher.group(4);
      if (name == null) return DATABASE_NAME;
      return name;
   }

   private int getPort(Matcher matcher) {
      String port = matcher.group(3);
      if (port == null) return 9001;
      return Integer.parseInt(port);
   }

  public static boolean isHsqldb(XPlannerProperties properties) {
    return properties.getProperty(HSQLDB_URL,"").startsWith(DatabaseURL.S_URL_PREFIX);
  }

  public static boolean isLocalPublicDatabaseStarted() {
    return theServer != null &&
           theServer.server != null &&
           theServer.server.getState()==ServerConstants.SERVER_STATE_ONLINE;
  }

  private static boolean isLocalOutOfProcessDatabase(XPlannerProperties properties) {
    return isLocalHsqlProtocolDatabase(properties) && !isEmbeddedDatabaseSpecified(properties);
  }

  private static boolean isRemoteDatabase(XPlannerProperties properties) {
    return !isLocalHsqlProtocolDatabase(properties) && isHsqlProtocol(properties); }

  private static boolean isHsqlProtocol(XPlannerProperties properties) {
    return getUrl(properties)!=null && getUrl(properties).startsWith(HSQL_URL_PREFIX);
  }

  private static boolean isEmbeddedDatabaseSpecified(XPlannerProperties properties) {
    return properties.getProperty(HSQLDB_DB_PATH)!=null;
  }
  // Could use HSQL DatabaseUrl
   private static boolean isLocalHsqlProtocolDatabase(XPlannerProperties properties) {
      if (getUrl(properties) == null) return false;
      Matcher matcher = HSQLDB_HSQL_URL_PATTERN.matcher(getUrl(properties));
      if (!matcher.matches()) return false;
      String host = matcher.group(1);
      return "localhost".equals(host);
   }

   private static boolean isEmbeddedPrivateDatabase(XPlannerProperties properties) {
     return !isLocalHsqlProtocolDatabase(properties) || !isEmbeddedDatabaseSpecified(properties);
   }

   private String getDbPath(XPlannerProperties properties) {
      String dbPath = properties.getProperty(HSQLDB_DB_PATH);
      if (dbPath == null) return null;
      if (dbPath.startsWith(WEBAPP_ROOT_TOKEN)) {
        return replaceWebRootToken(dbPath);
      }
     if (ABSOLUTE_PATH_PATTERN.matcher(dbPath).matches()) {
       return dbPath;
     }
     return new File(getStartDirectory() + File.separator + dbPath).getAbsolutePath();
   }

  private String replaceWebRootToken(String oldPath) {
    //         String prefix = oldPath.replaceFirst(WEBAPP_ROOT_TOKEN_PATTERN_STRING, webRootPath);  // TODO: Once in JDK 1.5 try this one again, in 1.4 it get a StringIndexOutOfBoundsEx if the webroot is "C:\\Projects\\xplanner-trunk\\build\\deploy\\"
    int tokenStartPos = oldPath.indexOf(WEBAPP_ROOT_TOKEN);
    int semiCommaPos = oldPath.indexOf(';');
    if (tokenStartPos == -1) return oldPath;
    int tokenEndPos = tokenStartPos + WEBAPP_ROOT_TOKEN.length() + 1; // THe slash in ${WEBAPP_ROOT}/
    String prefix = oldPath.substring(0, tokenStartPos);
    String suffix = "";
    String oldFilePath = "";
    if (semiCommaPos != -1) {
      oldFilePath = oldPath.substring(tokenEndPos, semiCommaPos);
      suffix = oldPath.substring(semiCommaPos);
    } else {
      oldFilePath = oldPath.substring(tokenEndPos);
    }
    String filePath = "";
    if (webRootPath != null) {
      filePath += webRootPath;
      if (!webRootPath.endsWith("\\") && !webRootPath.endsWith("/")) {
        filePath += File.separator;
      }
    }
    filePath += oldFilePath;
    return prefix + new File(filePath).getAbsolutePath() + suffix;
  }

  private String getStartDirectory() {
     try {
        return new File(".").getCanonicalPath();
     } catch (IOException e) {
        throw new RuntimeException(e);
     }
  }

   protected Server createServer() {return new Server();}

   private static boolean isTraceOn() {
      return Boolean.valueOf(new XPlannerProperties().getProperty("hibernate.show_sql")).booleanValue();
   }

   public static void shutdown() {
     if (theServer != null) {
       theServer.stop();
     }
   }

   public void stop() {
     LOG.info("Stopping HSQLDB server");
     sendShutdownCommand();

     if (server != null) {
      server.stop();
      waitForServerToTransitionOutOf(ServerConstants.SERVER_STATE_CLOSING);
      server = null;
     }
     theServer = null;
     LOG.info("HSQLDB server stopped");
   }

  private void sendShutdownCommand() {
    JdbcTemplate template = new JdbcTemplate(new SingleConnectionDataSource(HibernateHelper.getConnection(), false));
    template.execute("SHUTDOWN");
  }

  private void waitForServerToTransitionOutOf(int serverState) {
     while (server.getState() == serverState) {
        try {
           Thread.sleep(100);
        } catch (InterruptedException e) {}
     }
  }

   public static void main(String[] args) throws SQLException {
      start();
   }

   public Server getServer() {
      return server;
   }

  public String getDbPath() {
    return dbPath;
  }

  public static HsqlServer getInstance() {
      return theServer;
   }

}
