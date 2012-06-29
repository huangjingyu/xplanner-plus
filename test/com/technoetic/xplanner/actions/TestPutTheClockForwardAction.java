/*
 * Copyright (c) Mateusz Prokopowicz. All Rights Reserved.
 */

package com.technoetic.xplanner.actions;
/**
 * User: mprokopowicz
 * Date: Feb 9, 2006
 * Time: 1:09:22 PM
 */
import static org.easymock.EasyMock.expect;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts.action.ActionForward;

import com.technoetic.xplanner.util.TimeGenerator;

public class TestPutTheClockForwardAction extends ActionTestCase {

   private PutTheClockForwardAction putTheClockForwardAction;
   private TimeGenerator mockTimeGenerator;


   protected void setUp() throws Exception {
      action = new PutTheClockForwardAction();
      super.setUp();
      putTheClockForwardAction = (PutTheClockForwardAction) action;
      mockTimeGenerator = createLocalMock(TimeGenerator.class);
      putTheClockForwardAction.setTimeGenerator(mockTimeGenerator);
      Map propertyDefinitionMap = new HashMap();
      propertyDefinitionMap.put(PutTheClockForwardAction.OFFSET_IN_DAYS_KEY, String.class);
      support.request.setParameterValue(EditObjectAction.RETURNTO_PARAM, new String[]{""});
   }

   public void testMoveTime() throws Exception {
      String expectedReturnTo = "/do/view/iteration";
      support.request.setParameterValue(PutTheClockForwardAction.OFFSET_IN_DAYS_KEY, new String[]{"2"});
      expect(mockTimeGenerator.moveCurrentDay(2)).andReturn(2);
      support.request.setParameterValue(EditObjectAction.RETURNTO_PARAM, new String[]{expectedReturnTo});
      replay();
      ActionForward actionForward = support.executeAction(action);
      assertEquals(expectedReturnTo, actionForward.getPath());
      verify();
   }
}