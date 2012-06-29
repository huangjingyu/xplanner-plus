/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Jan 3, 2006
 * Time: 5:32:09 AM
 */
package com.technoetic.xplanner.tx;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.technoetic.xplanner.util.Callable;

public class CheckedExceptionHandlingTransactionTemplate {
   private TransactionTemplate springTransactionTemplate;

   public void setSpringTransactionTemplate(TransactionTemplate springTransactionTemplate) {
      this.springTransactionTemplate = springTransactionTemplate;
   }

   public Object execute(final Callable callable) throws Exception {
      try {
         return springTransactionTemplate.execute(new TransactionCallback() {
            public Object doInTransaction(TransactionStatus status) {
               try {
                  Object res = callable.run();
//                  ThreadSession.get().flush();
                  return res;
               } catch (Throwable e) {
                  throw new RuntimeException(e);
               }
            }
         });
         // DEBT(SPRING) Remove all this exception handling by doing spring declarative transaction demarkation
         // DEBT(SPRING) Convert all xplanner exception to Runtime so we can remove this crap!
      } catch (RuntimeException re) {
         Throwable e = re.getCause();
         if (e instanceof RuntimeException) throw (RuntimeException) e;
         if (e instanceof Error) throw (Error) e;
         if (e instanceof Exception) throw (Exception) e;
         return null;
      }
   }

}