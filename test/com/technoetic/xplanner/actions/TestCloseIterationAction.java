/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Dec 23, 2005
 * Time: 12:02:43 PM
 */
package com.technoetic.xplanner.actions;

import static org.easymock.EasyMock.expect;

import java.util.Date;

import net.sf.xplanner.domain.History;

import com.technoetic.xplanner.domain.IterationStatus;
import com.technoetic.xplanner.util.TimeGenerator;

public class TestCloseIterationAction extends AbstractIterationStatusTestCase {
   private TimeGenerator mockTimeGenerator;

   public void setUp() throws Exception {
      action = new CloseIterationAction();
      super.setUp();
      mockTimeGenerator = createLocalMock(TimeGenerator.class);
      ((CloseIterationAction) action).setTimeGenerator(mockTimeGenerator);
   }

   public void testExecute() throws Exception
   {
      iteration.setIterationStatus(IterationStatus.ACTIVE);
      mockDataSampler.generateClosingDataSamples(iteration);
      Date date = new Date();
      expect(mockTimeGenerator.getCurrentTime()).andReturn(date);
      event.setAction(History.ITERATION_CLOSED);
      event.setWhenHappened(date);
      expect(mockSession.save(event)).andReturn(null);
      support.setForward("onclose", "/continue/unfinished/stories");

      replay();

      support.executeAction(action);
      assertEquals("wrong iteration status", IterationStatus.INACTIVE_KEY, iteration.getStatusKey());
   }


}