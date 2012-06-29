/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Jan 15, 2006
 * Time: 10:40:14 PM
 */
package com.technoetic.xplanner.acceptance;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Project;

import com.technoetic.xplanner.acceptance.web.XPlannerWebTester;
import com.technoetic.xplanner.views.IterationPage;

public class IterationTester {
   XPlannerWebTester tester;

   public String addIteration(String iterationName,
                              String startDateString,
                              String endDateString,
                              String description) {
      tester.assertOnProjectPage();
      tester.clickLinkWithKey("project.link.create_iteration");
      tester.assertLinkPresentWithKey("form.description.help");
      tester.assertKeyPresent("iteration.editor.create");
      tester.assertKeyPresent("iteration.editor.name");
      tester.assertKeyPresent("iteration.editor.set_date");
      tester.assertKeyPresent("iteration.editor.start_date");
      tester.assertKeyPresent("iteration.editor.end_date");
      tester.assertKeyPresent("iteration.editor.description");

      tester.setFormElement("name", iterationName);
      tester.setFormElement("startDateString", startDateString);
      tester.setFormElement("endDateString", endDateString);
      tester.setFormElement("description", description);
      tester.submit();
      tester.assertOnProjectPage();
      return tester.getIdFromLinkWithText(iterationName);
   }


   public void start(Iteration iteration) {
      goToDefaultView(iteration);
      startCurrentIteration();
   }

   public void close(Iteration iteration) {
      goToDefaultView(iteration);
      closeCurrentIteration();
   }

   public void startCurrentIteration() {
      assertOnIterationPage();
      tester.clickLinkWithKey(IterationPage.START_ACTION);
      confirmStartCurrentIteration();
      assertCurrentIterationStarted();
   }

   private void confirmStartCurrentIteration() {
//      if (tester.getDialog().getElement("start") != null) {
//         if(tester.getDialog().getElement("closeIterations") != null) {
//            tester.uncheckCheckbox("closeIterations");
//         }
//         tester.submit("start");
//      }
   }

   public void assertOnStartIterationPromptPageAndStart() {
      assertOnStartIterationPromptPage();
      confirmStartCurrentIteration();
   }

   public void assertOnStartIterationPromptPage() {
      tester.assertKeyPresent("iteration.status.editor.message_4");
   }

   public void closeCurrentIteration() {
      assertOnIterationPage();
      tester.clickLinkWithKey(IterationPage.CLOSE_ACTION);
   }

   public void goToDefaultView(Iteration iteration) {goToDefaultView(iteration.getId());}
   public void goToDefaultView(int iterationId) {tester.gotoPage("view", "iteration", iterationId);}
   public void goToView(Project project, Iteration iteration, String view) {
      tester.gotoProjectsPage();
      tester.clickLinkWithText(Integer.toString(project.getId()));
      tester.clickLinkWithText(iteration.getName());
      tester.clickLinkWithKey(view);
   }

   public void assertOnIterationPage() {
      tester.assertKeyPresent("iteration.prefix");
      tester.assertLinkPresentWithKey("navigation.top");
      tester.assertLinkPresentWithKey("navigation.project");
      tester.assertLinkPresentWithKey("action.edit.iteration");
//      assertLinkPresentWithKey("iteration.link.edit");
      tester.assertLinkPresentWithKey("iteration.link.metrics");
      tester.assertLinkPresentWithKey("iteration.link.create_story");
      tester.assertKeyPresent("notes.label.notes");
   }

   public void assertCurrentIterationStarted() {
      tester.assertKeyNotPresent(IterationPage.START_ACTION);
      tester.assertKeyPresent(IterationPage.CLOSE_ACTION);
   }

   public void assertCurrentIterationClosed() {
      tester.assertKeyPresent(IterationPage.START_ACTION);
      tester.assertKeyNotPresent(IterationPage.CLOSE_ACTION);
   }

   public IterationTester(XPlannerWebTester tester) {
      this.tester = tester;
   }


}