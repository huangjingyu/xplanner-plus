package com.technoetic.xplanner.domain;

public class TaskDispositionType extends CharacterEnumType {

   public Class returnedClass() {
      return TaskDisposition.class;
   }

   protected CharacterEnum getType(String code) {
      return TaskDisposition.fromCode(code.charAt(0));
   }

}
