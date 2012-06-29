package com.technoetic.xplanner.domain;

import java.io.InvalidObjectException;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: sg897500
 * Date: Aug 9, 2005
 * Time: 2:49:51 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class CharacterEnum implements Serializable {

   protected final char code;
   protected final String name;

   public abstract String getAbbreviationKey();

   public CharacterEnum(char code, String name)
   {
      this.code = code;
      this.name = name;
   }

   public abstract String getNameKey();

   public String getName() {
      return name;
   }

   public char getCode(){
      return code;
   }

   public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null || !(obj instanceof CharacterEnum)) return false;
      return this.getName().equals(((CharacterEnum) obj).getName());
   }

   public int hashCode() {
      return code;
   }

   public String toString() {
      return getName();
   }

   //See http://www.javaworld.com/javaworld/javatips/jw-javatip122.html for the reason of this method
   private Object readResolve () throws InvalidObjectException {
      CharacterEnum[] enums = listEnums();
      for (int i = 0; i < enums.length; i++) {
         if (enums[i].getName().equals(getName())) return enums[i];
      }
      throw new InvalidObjectException("Wrong enum value (" + code + "," + name + ")");
   }

   public abstract CharacterEnum[] listEnums();


   protected static CharacterEnum valueOf(String name, CharacterEnum[] enums) {
      if (name == null) return null;
      for (int i = 0; i < enums.length; i++) {
         CharacterEnum enumeration = enums[i];
         if (enumeration.getName().equals(name)) return enumeration;
      }

      throw new RuntimeException("Unknown enum name " + name);
   }

   protected static CharacterEnum fromName(String name, CharacterEnum[] enums) {
      return valueOf(name, enums);
   }

   protected static CharacterEnum fromNameKey(String key, CharacterEnum[] enums) {
      if (key == null) return null;
      for (int i = 0; i < enums.length; i++) {
         CharacterEnum enumeration = enums[i];
         if (enumeration.getNameKey().equals(key)) return enumeration;
      }
      throw new RuntimeException("Unknown enum key " + key);
   }

   protected static CharacterEnum fromCode(char code, CharacterEnum[] enums) {
      for (int i = 0; i < enums.length; i++) {
         CharacterEnum enumeration = enums[i];
         if (enumeration.code == code) return enumeration;
      }
      throw new RuntimeException("Unknown enum code " + code);
   }
}
