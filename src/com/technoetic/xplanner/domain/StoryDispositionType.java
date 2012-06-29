package com.technoetic.xplanner.domain;

public class StoryDispositionType extends CharacterEnumType {

   public Class returnedClass() {
      return StoryDisposition.class;
   }

   protected CharacterEnum getType(String code) {
      return StoryDisposition.fromCode(code.charAt(0));
   }

}
