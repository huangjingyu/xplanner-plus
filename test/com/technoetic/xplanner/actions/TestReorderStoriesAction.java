/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.actions;

import static org.easymock.EasyMock.*;

import java.util.ArrayList;
import java.util.List;

import net.sf.xplanner.domain.Iteration;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.easymock.Capture;

import com.technoetic.xplanner.AbstractUnitTestCase;
import com.technoetic.xplanner.forms.ReorderStoriesForm;

/**
 * Created by IntelliJ IDEA.
 * User: SG0897500
 * Date: Mar 7, 2006
 * Time: 1:54:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestReorderStoriesAction extends AbstractUnitTestCase {

   private ActionMapping mockActionMapping = null;
   private ReorderStoriesForm mockReorderStoriesForm = null;
   private ReorderStoriesAction action = null;
   private Iteration mockIteration = null;

   protected void setUp() throws Exception {
      super.setUp();
      setUpRepositories();
      mockIteration = createLocalMock(Iteration.class);
      List userStories = new ArrayList();
      mockActionMapping = createLocalMock(ActionMapping.class);
      mockReorderStoriesForm = new ReorderStoriesForm();
      mockReorderStoriesForm.setIterationId("1");
      mockReorderStoriesForm.setOrderNo(0, "1.1");
      mockReorderStoriesForm.setStoryId(0, "1");

      action = new ReorderStoriesAction() {
         public Iteration getIteration(int id) {
            assertEquals(1, id);
            return mockIteration;
         }
      };
   }

   protected void tearDown() throws Exception {

   }

   public void testDoExecute() throws Exception {
	  Capture<int[][]> captureInt = new Capture<int[][]>();
	  mockIteration.modifyStoryOrder(capture(captureInt));
      expect(mockActionMapping.getInputForward()).andReturn(new ActionForward());
      replay();
      action.doExecute(mockActionMapping, mockReorderStoriesForm, null, null);
      verify();
      assertEquals(1, (captureInt.getValue()[0][0]));
      assertEquals(1, (captureInt.getValue()[0][1]));
   }
}
