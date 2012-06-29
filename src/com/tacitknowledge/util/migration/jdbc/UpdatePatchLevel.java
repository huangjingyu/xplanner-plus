/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.tacitknowledge.util.migration.jdbc;

import org.apache.commons.lang.StringUtils;

import com.technoetic.xplanner.db.hsqldb.HsqlServer;


public class UpdatePatchLevel {
   private AutopatchSupport autopatchSupport;

   public UpdatePatchLevel(AutopatchSupport autopatchSupport) {
      this.autopatchSupport = autopatchSupport;
   }

   public static void main(String[] arguments) throws Exception {
     String migrationName = System.getProperty("migration.systemname");
     AutopatchSupport autopatchSupport = new AutopatchSupport(migrationName);
     UpdatePatchLevel dummyMigrationLauncher = new UpdatePatchLevel(autopatchSupport);
     String patchLevel = null;
     if ((arguments != null) && (arguments.length > 0)) {
       patchLevel = arguments[0].trim();
     }
     dummyMigrationLauncher.updatePatchLevel(migrationName, patchLevel);
     HsqlServer.shutdown();
   }

   protected void updatePatchLevel(String migrationName, String patchLevel) throws Exception {
      if (migrationName == null) {
         throw new IllegalArgumentException("The migration.systemname "
                                            + "system property is required");
      }
      int patchLevelVal;

      if (StringUtils.isEmpty(patchLevel)) {
         patchLevelVal = autopatchSupport.getHighestPatchLevel();
      } else {
         patchLevelVal = Integer.parseInt(patchLevel);
      }
      autopatchSupport.setPatchLevel(patchLevelVal);
   }

}
