package com.technoetic.xplanner.forms;

import java.util.Iterator;

import junit.framework.TestCase;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.XPlannerTestSupport;

public class TestUserStoryEditorForm extends TestCase {
   public XPlannerProperties xplannerProperties;
   private XPlannerTestSupport support;
   private UserStoryEditorForm form;

   protected void setUp() throws Exception {
      super.setUp();
      support = new XPlannerTestSupport();
      form = new UserStoryEditorForm();
      form.setName("name");
      xplannerProperties = new XPlannerProperties();
   }

   public void testReset() {
      form.reset(support.mapping, support.request, xplannerProperties);
      assertNull("action not reset", form.getAction());
      assertNull("name not reset", form.getName());
      assertNull("description not reset", form.getDescription());
      assertEquals("trackerId not reset", 0, form.getTrackerId());
      assertEquals("iterationId not reset", 0, form.getIterationId());
      int defaultPriority = Integer.parseInt(xplannerProperties.getProperty(UserStoryEditorForm.DEFAULT_PRIORITY_KEY));
      assertEquals("priority not reset", defaultPriority, form.getPriority());
      assertEquals("estimateHours not reset", 0.0, form.getEstimatedHoursField(), 0);
   }

   public void testPriorityProperty() {
      xplannerProperties.setProperty(UserStoryEditorForm.DEFAULT_PRIORITY_KEY, "20976");
      form.resetPriority(xplannerProperties);
      assertEquals("property not be used", 20976, form.getPriority());

   }

   public void testValidateFormOk() {
      assertValidateReturnsNoError();

   }

   public void testValidateMissingNameNotSubmitted() {
      form.setName(null);

      assertValidateReturnsNoError();
   }

   public void testValidateMissingName() {
      form.setName(null);
      form.setAction("Update");

      assertValidateReturnsError(UserStoryEditorForm.MISSING_NAME_ERROR_KEY);
   }


   public void testValidateNegativeEstimate() {
      form.setEstimatedHoursField(-10.0);
      form.setAction("Update");

      assertValidateReturnsError(UserStoryEditorForm.NEGATIVE_ESTIMATED_HOURS_ERROR_KEY);
   }

   public void testValidateSameIteration() {
      form.setIterationId(10);
      form.setTargetIterationId(10);
      form.setAction("Update");
      form.setMerge(true);

      assertValidateReturnsError(UserStoryEditorForm.SAME_ITERATION_ERROR_KEY);
   }

   public void testValidatePriorityNotANumber() throws Exception {
      support.request.addParameter(UserStoryEditorForm.PRIORITY_PARAM, "blabla");
      form.setAction("Update");

      assertValidateReturnsError(UserStoryEditorForm.INVALID_PRIORITY_ERROR_KEY);
   }

   public void testValidatePriorityANumber() throws Exception {
      support.request.addParameter(UserStoryEditorForm.PRIORITY_PARAM, "10");
      form.setAction("Update");

      assertValidateReturnsNoError();
   }

   private void assertValidateReturnsNoError() {
      ActionErrors errors = form.validate(support.mapping, support.request);

      assertEquals("unexpected errors", 0, errors.size());
   }

   private void assertValidateReturnsError(String errorKey) {
      ActionErrors errors = form.validate(support.mapping, support.request);
      assertEquals("wrong # of errors", 1, errors.size());
      Iterator errorItr = errors.get();
      ActionError error = (ActionError) errorItr.next();
      assertEquals("wrong key", errorKey, error.getKey());
   }


}