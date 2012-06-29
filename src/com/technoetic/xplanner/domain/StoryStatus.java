package com.technoetic.xplanner.domain;

import java.text.MessageFormat;

public class StoryStatus extends CharacterEnum {

   public static final String DRAFT_NAME = "draft";
   public static final String DEFINED_NAME = "defined";
   public static final String ESTIMATED_NAME = "estimated";
   public static final String PLANNED_NAME = "planned";
   public static final String IMPLEMENTED_NAME = "implemented";
   public static final String VERIFIED_NAME = "verified";
   public static final String ACCEPTED_NAME = "accepted";

   private static final String NAME_KEY_TEMPLATE = "story.status.{0}.name";
   private static final String ABBREVIATION_KEY_TEMPLATE = "story.status.{0}.abbreviation";

   public static final StoryStatus DRAFT =       new StoryStatus('d', DRAFT_NAME);
   public static final StoryStatus DEFINED =     new StoryStatus('D', DEFINED_NAME);
   public static final StoryStatus ESTIMATED =   new StoryStatus('e', ESTIMATED_NAME);
   public static final StoryStatus PLANNED =     new StoryStatus('p', PLANNED_NAME);
   public static final StoryStatus IMPLEMENTED = new StoryStatus('i', IMPLEMENTED_NAME);
   public static final StoryStatus VERIFIED =    new StoryStatus('v', VERIFIED_NAME);
   public static final StoryStatus ACCEPTED =    new StoryStatus('a', ACCEPTED_NAME);

   private static transient final StoryStatus[] enums = {DRAFT, DEFINED, ESTIMATED, PLANNED, IMPLEMENTED, VERIFIED, ACCEPTED};

   StoryStatus(char code, String name) {
      super(code, name);
   }

   public String getAbbreviationKey() {
      return MessageFormat.format(ABBREVIATION_KEY_TEMPLATE, new String[]{name});
   }

//   protected StoryStatus(char code, String name) {
//      this(code, name, name, "" + name.charAt(0));
//   }

   public String getNameKey() {
      return MessageFormat.format(NAME_KEY_TEMPLATE, new String[]{name});
   }

   public CharacterEnum[] listEnums() {
      return enums;
   }

   public static StoryStatus valueOf(String key) {
      return (StoryStatus) valueOf(key, enums);
   }

   public static StoryStatus fromNameKey(String key) {
      return (StoryStatus) fromNameKey(key, enums);
   }

   public static StoryStatus fromCode(char code) {
      return (StoryStatus) fromCode(code, enums);
   }

   public static StoryStatus fromName(String name) {
      return (StoryStatus) fromName(name, enums);
   }

}
