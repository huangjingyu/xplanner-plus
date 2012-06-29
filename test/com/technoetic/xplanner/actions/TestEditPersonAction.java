/*
 * Copyright (c) Mateusz Prokopowicz. All Rights Reserved.
 */

package com.technoetic.xplanner.actions;

import java.util.HashMap;

import net.sf.xplanner.domain.Person;

import org.apache.struts.action.ActionMapping;
import org.easymock.MockControl;
import org.easymock.classextension.MockClassControl;

import com.technoetic.xplanner.AbstractUnitTestCase;
import com.technoetic.xplanner.XPlannerTestSupport;
import com.technoetic.xplanner.forms.PersonEditorForm;

public class TestEditPersonAction extends AbstractUnitTestCase {
   private PersonEditorForm mockEditorForm;
   private EditPersonAction action;
   private XPlannerTestSupport support;
   private Person mockPerson;
   private int PERSON_ID = XPlannerTestSupport.DEFAULT_PERSON_ID;
   private MockControl mockEditPersonHelperControl;
   private EditPersonHelper mockEditPersonHelper;
   static final String PERSON_USER_ID = "mock";

   protected void setUp() throws Exception {
      super.setUp();
      support = new XPlannerTestSupport();
      support.setUpSubjectInRole("xyz");
      mockEditorForm = new PersonEditorForm();
      mockEditorForm.reset(support.mapping, support.request);
      mockPerson = new Person(PERSON_USER_ID);
      mockPerson.setId(PERSON_ID);
      action = new EditPersonAction();
      mockEditPersonHelperControl = MockClassControl.createControl(EditPersonHelper.class);
      mockEditPersonHelper = (EditPersonHelper) mockEditPersonHelperControl.getMock();
      action.setEditPersonHelper(mockEditPersonHelper);
   }

   public void testBeforeObjectCommit() throws Exception {
      mockEditPersonHelper.modifyRoles(new HashMap(), mockPerson, false, PERSON_ID);
      replay();
      action.beforeObjectCommit(mockPerson,
                                new ActionMapping(),
                                mockEditorForm,
                                support.request,
                                support.response);
      verify();
   }

   public void testAfterObjectCommit() throws Exception {
      String newPassword = "xyz";
      mockEditorForm.setNewPassword(newPassword);
      mockEditPersonHelper.changeUserPassword(newPassword, null, null);
      replay();
      action.afterObjectCommit(new ActionMapping(),
                               mockEditorForm,
                               support.request,
                               support.response);
      verify();
   }

}

