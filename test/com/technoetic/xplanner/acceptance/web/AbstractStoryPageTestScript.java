/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: Jun 18, 2005
 * Time: 3:14:57 PM
 */
package com.technoetic.xplanner.acceptance.web;

import com.technoetic.xplanner.domain.CharacterEnum;
import com.technoetic.xplanner.domain.StoryDisposition;
import com.technoetic.xplanner.domain.TaskDisposition;

public abstract class AbstractStoryPageTestScript extends AbstractPageTestScript {
   public AbstractStoryPageTestScript(String test) {
      super(test);
   }

   protected void assertDispositionEquals(String storyName, StoryDisposition disposition) {
      assertEnumFieldEquals(storyName, disposition);
   }

   protected void assertDispositionEquals(String taskName, TaskDisposition disposition) {
      assertEnumFieldEquals(taskName, disposition);
   }

  private void assertEnumFieldEquals(String object, CharacterEnum e) {
     tester.assertTextPresent(object);
     tester.assertTextPresent(tester.getMessage(e.getNameKey()));
  }

}