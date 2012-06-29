package com.technoetic.xplanner.domain;

public class StoryStatusType extends CharacterEnumType {

   public Class returnedClass() {
      return StoryStatus.class;
   }

   protected CharacterEnum getType(String code) {
      return StoryStatus.fromCode(code.charAt(0));
   }

}
