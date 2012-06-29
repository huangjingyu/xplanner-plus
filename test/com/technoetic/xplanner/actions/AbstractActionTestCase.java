/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Dec 21, 2005
 * Time: 11:18:31 PM
 */
package com.technoetic.xplanner.actions;


import com.technoetic.xplanner.tx.CheckedExceptionHandlingTransactionTemplate;
import com.technoetic.xplanner.util.Callable;

public class AbstractActionTestCase extends ActionTestCase {
   private boolean transactionUsed;

   protected void setUp() throws Exception {
      AbstractAction abstractAction = (AbstractAction) super.action;
      super.setUp();
      setUpThreadSession(false);
      abstractAction.setTransactionTemplate(new CheckedExceptionHandlingTransactionTemplate() {
         public Object execute(Callable action) throws Exception {
            transactionUsed = true;
            return action.run();
         }
      });

   }

   public void verify() {
      super.verify();
      assertTrue(transactionUsed);
   }
}