/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.db.hsqldb;

import java.io.File;

import junit.framework.TestCase;

import org.hsqldb.Server;
import org.hsqldb.server.ServerConstants;

import com.technoetic.xplanner.XPlannerProperties;

public class TestHsqlServer extends TestCase {
  HsqlServer hsqlServer;
  XPlannerProperties properties;
  protected String webRoot;
  public String rootPath;
  public static final String SLASH = File.separator;

  public void testStartPublicServerWithWebRootPathDefinedAndExplicitDbPathRelativeToWebAppRoot() throws Exception {
    webRoot = "build\\deploy\\";
    assertEmbeddedServerStartsCorrectly(HsqlServer.WEBAPP_ROOT_TOKEN + "/mylocation", webRoot + "mylocation");
  }

  public void testStartPublicServerWithWebRootPathDefinedAndExplicitDbPathRelativeToUndefinedWebAppRoot() throws Exception {
    assertEmbeddedServerStartsCorrectly(HsqlServer.WEBAPP_ROOT_TOKEN + "/mylocation", rootPath + SLASH + "mylocation");
  }

  public void testStartPublicServerWithWebRootPathDefinedAndExplicitDbPathRelativeToServerStartDir() throws Exception {
    assertEmbeddedServerStartsCorrectly("mylocation", rootPath + SLASH + "mylocation");
  }

  public void testStartPublicServerWithWebRootPathDefinedAndExplicitDbPathAbsoluteUnixDir() throws Exception {
    assertEmbeddedServerStartsCorrectly("/usr/local/xplanner/hsqldb", "/usr/local/xplanner/hsqldb");
  }

  public void testStartPublicServerWithWebRootPathDefinedAndExplicitDbPathAbsoluteWindowsDir() throws Exception {
    assertEmbeddedServerStartsCorrectly("c:/usr/local/xplanner/hsqldb", "c:/usr/local/xplanner/hsqldb");
  }

  private void assertEmbeddedServerStartsCorrectly(String dbPathProperty, String expectedDatabaseFilePath) {
    properties.setProperty(HsqlServer.HSQLDB_DB_PATH, dbPathProperty);
    properties.setProperty(HsqlServer.HSQLDB_URL, "jdbc:hsqldb:hsql://localhost:9001/xplanner");
    hsqlServer.initProperties(webRoot, properties);
    hsqlServer.startPublicServer();

    assertEquals(9001, hsqlServer.getServer().getPort());
    assertEquals("xplanner", hsqlServer.getServer().getDatabaseName(0, true));
    assertEquals(expectedDatabaseFilePath, hsqlServer.getServer().getDatabasePath(0, true));
    assertEquals(ServerConstants.SERVER_STATE_ONLINE, hsqlServer.getServer().getState());
  }

  public void testStartPublicServerWithWebRootPathUndefinedDbPathDefinedAndHsqlProtocolWithNonDefaultParams() throws Exception {
    properties.setProperty(HsqlServer.HSQLDB_URL, "jdbc:hsqldb:hsql://localhost:9002/xplanner2");
    properties.setProperty(HsqlServer.HSQLDB_DB_PATH, "/usr/local/xplanner/hsqldb2");
    hsqlServer.initProperties(null, properties);
    hsqlServer.startPublicServer();

    assertEquals(9002, hsqlServer.getServer().getPort());
    assertEquals("xplanner2", hsqlServer.getServer().getDatabaseName(0, true));
    assertEquals("/usr/local/xplanner/hsqldb2", hsqlServer.getServer().getDatabasePath(0, true));
  }

  public void testStartWithLocalDbButNoDbPath_DoesntStartInProcessDB() throws Exception {
    properties.setProperty(HsqlServer.HSQLDB_URL, "jdbc:hsqldb:hsql://localhost:9001/xplanner");
    properties.removeProperty(HsqlServer.HSQLDB_DB_PATH);
    HsqlServer.start();
    assertFalse(HsqlServer.getInstance().isLocalPublicDatabaseStarted());
  }

  public void testStartWithEmbeddedDb_DoesntStartInProcessDB() throws Exception {
    properties.setProperty(HsqlServer.HSQLDB_URL, "jdbc:hsqldb:file:hsqldb/xplanner");
    properties.removeProperty(HsqlServer.HSQLDB_DB_PATH);
    assertFalse(HsqlServer.isLocalPublicDatabaseStarted());
    HsqlServer.start();
    assertFalse(HsqlServer.isLocalPublicDatabaseStarted());
  }

  public void testStartWithRemoteDb_DoesntStartInProcessDB() throws Exception {
    properties.setProperty(HsqlServer.HSQLDB_URL, "jdbc:hsqldb:hsql://myserver:9002/xplanner2");
    properties.removeProperty(HsqlServer.HSQLDB_DB_PATH);
    HsqlServer.start();
    assertFalse(HsqlServer.isLocalPublicDatabaseStarted());
  }

  public void testStartWithEmbeddedDb_DoesntStartInProcessDBAndReplaceWebRootToken() throws Exception {
    properties.setProperty(HsqlServer.HSQLDB_URL, "jdbc:hsqldb:file:${WEBAPP_ROOT}/xplanner2;shutdown=true");
    properties.removeProperty(HsqlServer.HSQLDB_DB_PATH);
    String webRoot = rootPath + SLASH + "webroot";
    HsqlServer.start(webRoot);
    assertEquals("jdbc:hsqldb:file:"+webRoot+SLASH + "xplanner2;shutdown=true", properties.getProperty(HsqlServer.HSQLDB_URL));
    assertFalse(HsqlServer.isLocalPublicDatabaseStarted());
  }

  protected void setUp() throws Exception {
    super.setUp();
    hsqlServer = new HsqlServer() {
      protected Server createServer() {
        return new Server() {
          int state = ServerConstants.SERVER_STATE_SHUTDOWN;

          public int start() {
            state = ServerConstants.SERVER_STATE_ONLINE;
            return ServerConstants.SERVER_STATE_OPENING;
          }

          public int stop() {
            state = ServerConstants.SERVER_STATE_SHUTDOWN;
            return ServerConstants.SERVER_STATE_CLOSING;
          }

          public synchronized int getState() { return state; }
        };
      }
    };
    properties = new XPlannerProperties();
    rootPath = new File(".").getCanonicalPath();
  }

}