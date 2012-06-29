package com.technoetic.xplanner.domain;

import java.text.MessageFormat;

public class StoryDisposition extends CharacterEnum{


   public static final String PLANNED_NAME = "planned";
   public static final String CARRIED_OVER_NAME = "carriedOver";
   public static final String DISCOVERED_NAME = "discovered";
   public static final String ADDED_NAME = "added";
   public static final String OVERHEAD_NAME = "overhead";

   private static final String NAME_KEY_TEMPLATE = "story.disposition.{0}.name";
   private static final String ABBREVIATION_KEY_TEMPLATE = "story.disposition.{0}.abbreviation";

   public static final StoryDisposition PLANNED = new StoryDisposition('p', PLANNED_NAME);
   public static final StoryDisposition CARRIED_OVER = new StoryDisposition('c', CARRIED_OVER_NAME);
   public static final StoryDisposition ADDED = new StoryDisposition('a', ADDED_NAME);

   public static final StoryDisposition[] enums = {PLANNED, CARRIED_OVER, ADDED};

   public String getNameKey() {
      return MessageFormat.format(NAME_KEY_TEMPLATE, new String[]{name});
   }

   public String getAbbreviationKey() {
      return MessageFormat.format(ABBREVIATION_KEY_TEMPLATE, new String[]{name});
   }

   protected StoryDisposition(char code, String name) {
      super(code, name);
   }

   public CharacterEnum[] listEnums() {
      return enums;
   }


   public static StoryDisposition valueOf(String name) {
      return (StoryDisposition) valueOf(name, enums);
   }

   public static StoryDisposition fromCode(char code) {
      return (StoryDisposition) fromCode(code, enums);
   }

   public static StoryDisposition fromNameKey(String key) {
      return (StoryDisposition) fromNameKey(key, enums);
   }

   public static StoryDisposition fromName(String name)
   {
      return (StoryDisposition) fromName(name, enums);
   }
}
