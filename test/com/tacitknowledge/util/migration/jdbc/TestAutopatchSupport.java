/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.tacitknowledge.util.migration.jdbc;

import junit.framework.TestCase;

import org.easymock.MockControl;
import org.easymock.classextension.MockClassControl;

public class TestAutopatchSupport extends TestCase {
   private AutopatchSupport autopatchSupport;
   private MockControl mockPatchTableControl;
   private PatchTable mockPatchTable;

   protected void setUp() throws Exception {
      super.setUp();
      mockPatchTableControl = MockClassControl.createControl(PatchTable.class);
      mockPatchTable = (PatchTable) mockPatchTableControl.getMock();
      autopatchSupport = new AutopatchSupport(new JdbcMigrationLauncher()) {
         public PatchTable makePatchTable() {
            return mockPatchTable;
         }
      };
   }

   public void testSetPatchLevel() throws Exception {
      mockPatchTable.lockPatchTable();
      mockPatchTable.updatePatchLevel(2);
      mockPatchTable.unlockPatchTable();
      mockPatchTableControl.replay();
      autopatchSupport.setPatchLevel(2);
      mockPatchTableControl.verify();
   }
}