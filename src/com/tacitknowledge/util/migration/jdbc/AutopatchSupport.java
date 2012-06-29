/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.tacitknowledge.util.migration.jdbc;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.tacitknowledge.util.migration.MigrationException;

public class AutopatchSupport {
   private static final Logger log = Logger.getLogger(AutopatchSupport.class);
   private JdbcMigrationLauncher launcher;

   public AutopatchSupport(String systemName) throws MigrationException {
      this(MigrationLauncherFactoryLoader.createFactory(), systemName);
   }

   public AutopatchSupport(JdbcMigrationLauncherFactory launcherFactory, String systemName) throws MigrationException {
      this(launcherFactory.createMigrationLauncher(systemName));
   }

   public AutopatchSupport(JdbcMigrationLauncher launcher) {
      this.launcher = launcher;
   }

   public PatchTable makePatchTable() throws SQLException {
      JdbcMigrationContext jdbcMigrationContext = launcher.getJdbcMigrationContext();
      return new PatchTable(jdbcMigrationContext, jdbcMigrationContext.getConnection());
   }

   public void setPatchLevel(int patchLevel) throws SQLException {
      PatchTable patchTable = makePatchTable();
      patchTable.lockPatchTable();
      patchTable.updatePatchLevel(patchLevel);
      log.info("Set the patch level to " + patchLevel);
      patchTable.unlockPatchTable();
   }

   public int getPatchLevel() throws SQLException {
      PatchTable patchTable = makePatchTable();
      return patchTable.getPatchLevel();
   }

   public int getHighestPatchLevel() throws MigrationException {
      return launcher.getNextPatchLevel() - 1;
   }


}
