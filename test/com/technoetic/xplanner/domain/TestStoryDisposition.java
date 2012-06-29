package com.technoetic.xplanner.domain;

import junit.framework.TestCase;

public class TestStoryDisposition extends TestCase {
   private StoryDisposition storyDisposition;

   protected void setUp() throws Exception {
      super.setUp();
      storyDisposition = StoryDisposition.ADDED;
   }

   public void testGetKeys() throws Exception {
      assertEquals("story.disposition.added.name", storyDisposition.getNameKey());
      assertEquals("story.disposition.added.abbreviation", storyDisposition.getAbbreviationKey());
   }

   public void testValueOf() throws Exception {
      assertEquals(StoryDisposition.ADDED_NAME, storyDisposition.getName());
   }

   public void testFromCode() throws Exception {
      assertEquals(storyDisposition, StoryDisposition.fromCode('a'));
   }

   public void testFromName() throws Exception {
      assertEquals(storyDisposition, StoryDisposition.fromName(StoryDisposition.ADDED_NAME));
   }
}