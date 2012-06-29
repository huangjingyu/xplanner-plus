/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Dec 30, 2005
 * Time: 3:57:02 PM
 */
package com.technoetic.xplanner.forms;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import junit.framework.TestCase;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import com.technoetic.xplanner.XPlannerTestSupport;

public class AbstractEditorFormTestCase extends TestCase {
   protected XPlannerTestSupport support;
   protected AbstractEditorForm form;
   public static final Locale LOCALE = new Locale("da", "nl");
   public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
   public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

   protected void setUp() throws Exception {
      super.setUp();
      support = new XPlannerTestSupport();
      support.resources.setMessage("format.datetime", DATE_TIME_FORMAT.toPattern());
      support.resources.setMessage("format.date", DATE_FORMAT.toPattern());
      support.request.setLocale(LOCALE);
      form.setServlet(support.actionServlet);
   }

   public void assertOneError(String expectedErrorKey, ActionErrors errors) {
      assertErrorsEqual(new String[] {expectedErrorKey}, errors);
   }

   public void assertErrorsEqual(String[] expectedErrorKeys, ActionErrors errors) {
      assertCollectionsEqual(Arrays.asList(expectedErrorKeys), toKeysList(errors));
   }

   private void assertCollectionsEqual(List expectedList, List actualList) {
      List surplusItems = new ArrayList();
      List missingItems = new ArrayList();
      List matchingExpectedItems = new ArrayList();
      List matchingActualItems = new ArrayList();

      compareLists(expectedList, actualList, missingItems, matchingExpectedItems);
      compareLists(actualList, expectedList, surplusItems, matchingActualItems);

      List missingMatching = new ArrayList();
      compareLists(matchingExpectedItems, matchingActualItems, missingMatching, new ArrayList());
      assertEquals(0, missingMatching.size());

      if (missingItems.size() > 0 || surplusItems.size() > 0) {
         String errMsg = "Collections not equal\n";
         errMsg += dumpCollection("missing", missingItems);
         errMsg += dumpCollection("surplus", surplusItems);
         errMsg += dumpCollection("matching", matchingExpectedItems);
         fail(errMsg);
      }
   }

   private void compareLists(List expectedList, List actualList, List missingItems, List matchingItems) {
      List actuals = new ArrayList(actualList);
      for (Iterator expectedIterator = expectedList.iterator();expectedIterator.hasNext();) {
         Object key = expectedIterator.next();
         boolean found = false;
         Iterator actualIterator = actuals.iterator();
         while (actualIterator.hasNext() && !found) {
            found = actualIterator.next().equals(key);
            actualIterator.remove();
         }
         if (!found)
            missingItems.add(key);
         else
            matchingItems.add(key);
      }
   }

   private List toKeysList(ActionErrors errors) {
      List actualList = new ArrayList();
      Iterator iterator = errors.get();
      while (iterator.hasNext()) {
         ActionError error = (ActionError) iterator.next();
         actualList.add(error.getKey());
      }
      return actualList;
   }

   private String dumpCollection(String message, Collection items) {
      String msg = items.size() + " " +message;
      Iterator iterator = items.iterator();
      boolean first = true;
      while (iterator.hasNext()) {
         if (first) {
            first = false;
            msg += ":\n";
         } else {
            msg += ",\n";
         }
         msg += "  " + iterator.next();
      }
      return msg+"\n";
   }
}