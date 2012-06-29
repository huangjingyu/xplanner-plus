package com.technoetic.xplanner.domain;

import java.text.MessageFormat;


/**
 * Created by IntelliJ IDEA.
 * User: sg897500
 * Date: Aug 9, 2005
 * Time: 11:16:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class TaskDisposition extends CharacterEnum {

   boolean original;

   public static final String PLANNED_NAME = "planned";
   public static final String CARRIED_OVER_NAME = "carriedOver";
   public static final String DISCOVERED_NAME = "discovered";
   public static final String ADDED_NAME = "added";
   public static final String OVERHEAD_NAME = "overhead";

   private static final String NAME_KEY_TEMPLATE = "task.disposition.{0}.name";
   private static final String ABBREVIATION_KEY_TEMPLATE = "task.disposition.{0}.abbreviation";

   public static final TaskDisposition PLANNED      = new TaskDisposition('p',
                                                                          PLANNED_NAME,
                                                                          true);
   public static final TaskDisposition CARRIED_OVER = new TaskDisposition('c',
                                                                          CARRIED_OVER_NAME,
                                                                          true);
   public static final TaskDisposition ADDED        = new TaskDisposition('a',
                                                                          ADDED_NAME,
                                                                          false);
   public static final TaskDisposition DISCOVERED   = new TaskDisposition('d',
                                                                          DISCOVERED_NAME,
                                                                          false);
   public static final TaskDisposition OVERHEAD     = new TaskDisposition('o',
                                                                          OVERHEAD_NAME,
                                                                          false);

   public static final TaskDisposition[] enums = {PLANNED, CARRIED_OVER, ADDED, DISCOVERED, OVERHEAD};

   TaskDisposition(char code, String name, boolean original) {
      super(code, name);
      this.original = original;
   }

   public String getAbbreviationKey() {
      return MessageFormat.format(ABBREVIATION_KEY_TEMPLATE, new String[]{name});
   }

   public String getNameKey() {
      return MessageFormat.format(NAME_KEY_TEMPLATE, new String[]{name});
   }

   public CharacterEnum[] listEnums() {
      return enums;
   }

   public boolean isOriginal() {
      return original;
   }

   public static TaskDisposition valueOf(String key) {
      return (TaskDisposition) valueOf(key, enums);
   }

   public static TaskDisposition fromNameKey(String key) {
      return (TaskDisposition) fromNameKey(key, enums);
   }

   public static TaskDisposition fromCode(char code) {
      return (TaskDisposition) fromCode(code, enums);
   }

   public static TaskDisposition fromName(String name) {
      return (TaskDisposition) fromName(name, enums);
   }

}
