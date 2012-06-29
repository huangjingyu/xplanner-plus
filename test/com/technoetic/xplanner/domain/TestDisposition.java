/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.technoetic.xplanner.domain;

import junit.framework.TestCase;

public class TestDisposition extends TestCase {
   private StoryDisposition storyDisposition;
   private TaskDisposition taskDisposition;


   public void testFromInt() throws Exception {
      storyDisposition = StoryDisposition.fromCode('p');
      assertEquals("Disposition name", StoryDisposition.PLANNED_NAME, storyDisposition.getName());
      storyDisposition = StoryDisposition.fromCode('c');
      assertEquals("Disposition name", StoryDisposition.CARRIED_OVER_NAME, storyDisposition.getName());
      storyDisposition = StoryDisposition.fromCode('a');
      assertEquals("Disposition name", StoryDisposition.ADDED_NAME, storyDisposition.getName());
      taskDisposition = TaskDisposition.fromCode('p');
      assertEquals("Disposition name", TaskDisposition.PLANNED_NAME, taskDisposition.getName());
      taskDisposition = TaskDisposition.fromCode('c');
      assertEquals("Disposition name", TaskDisposition.CARRIED_OVER_NAME, taskDisposition.getName());
      taskDisposition = TaskDisposition.fromCode('a');
      assertEquals("Disposition name", TaskDisposition.ADDED_NAME, taskDisposition.getName());
      taskDisposition = TaskDisposition.fromCode('o');
      assertEquals("Disposition name", TaskDisposition.OVERHEAD_NAME, taskDisposition.getName());
   }

   public void testValueOf() throws Exception {
      storyDisposition = StoryDisposition.PLANNED;
      assertEquals("PLANNED",
                   (StoryDisposition.valueOf(StoryDisposition.PLANNED_NAME)).getName(),
                   storyDisposition.getName());
      storyDisposition = StoryDisposition.CARRIED_OVER;
      assertEquals("CARRIED_OVER",
                   (StoryDisposition.valueOf(StoryDisposition.CARRIED_OVER_NAME)).getName(),
                   storyDisposition.getName());
      storyDisposition = StoryDisposition.ADDED;
      assertEquals("ADDED",
                   (StoryDisposition.valueOf(StoryDisposition.ADDED_NAME)).getName(),
                   storyDisposition.getName());
      taskDisposition = TaskDisposition.PLANNED;
      assertEquals("PLANNED",
                   (TaskDisposition.valueOf(TaskDisposition.PLANNED_NAME)).getName(),
                   taskDisposition.getName());
      taskDisposition = TaskDisposition.ADDED;
      assertEquals("ADDED",
                   (TaskDisposition.valueOf(TaskDisposition.ADDED_NAME)).getName(),
                   taskDisposition.getName());
      taskDisposition = TaskDisposition.CARRIED_OVER;
      assertEquals("CARRIED_OVER",
                   (TaskDisposition.valueOf(TaskDisposition.CARRIED_OVER_NAME)).getName(),
                   taskDisposition.getName());
      taskDisposition = TaskDisposition.OVERHEAD;
      assertEquals("OVERHEAD",
                   (TaskDisposition.valueOf(TaskDisposition.OVERHEAD_NAME)).getName(),
                   taskDisposition.getName());
   }

   public void testValueOfThrowsExceptionIfBadName() throws Exception {
      try {
         StoryDisposition.valueOf("Not valid");
         fail("Did not throw exception");
      } catch (Exception e) {
      }
      try {
         TaskDisposition.valueOf("Not valid");
         fail("Did not throw exception");
      } catch (Exception e) {
      }
   }

//   public void testToChar() throws Exception {
//      assertEquals("code of planned", , StoryDisposition.PLANNED.toInt());
//      assertEquals("code of carried over", 1, StoryDisposition.CARRIED_OVER.toInt());
//      assertEquals("code of added", 2, StoryDisposition.ADDED.toInt());
//   }

   public void testToString() throws Exception {
      storyDisposition = StoryDisposition.PLANNED;
      assertEquals("planned", StoryDisposition.PLANNED_NAME, storyDisposition.toString());
      storyDisposition = StoryDisposition.CARRIED_OVER;
      assertEquals("carried over", StoryDisposition.CARRIED_OVER_NAME, storyDisposition.toString());
      storyDisposition = StoryDisposition.ADDED;
      assertEquals("added", StoryDisposition.ADDED_NAME, storyDisposition.toString());
      taskDisposition = TaskDisposition.PLANNED;
      assertEquals("planned", TaskDisposition.PLANNED_NAME, taskDisposition.toString());
      taskDisposition = TaskDisposition.ADDED;
      assertEquals("added", TaskDisposition.ADDED_NAME, taskDisposition.toString());
      taskDisposition = TaskDisposition.CARRIED_OVER;
      assertEquals("cariedOver", TaskDisposition.CARRIED_OVER_NAME, taskDisposition.toString());
      taskDisposition = TaskDisposition.OVERHEAD;
      assertEquals("overhead", TaskDisposition.OVERHEAD_NAME, taskDisposition.toString());
   }

}