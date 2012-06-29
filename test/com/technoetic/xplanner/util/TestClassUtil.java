/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Apr 2, 2006
 * Time: 1:34:01 PM
 */
package com.technoetic.xplanner.util;

import junit.framework.TestCase;
import junitx.framework.ArrayAssert;

public class TestClassUtil extends TestCase {

   public static class Base {
      private int i;

      public void setI(int i) { this.i = i; }
   }

   public static class Derived extends Base {
      private String s;

      public void setS(String s) { this.s = s; }
   }

   public static class DerivedOverriding extends Base {
      private int i;
      private String s;

      public void setI(int i) { this.i = i; }
      public void setS(String s) { this.s = s; }
   }

   public void testGetAllFieldNamesWithoutInheritance() throws Exception {
      assertMembersEquals(new String[]{"i"}, new Base());
   }

   public void testGetAllFieldNamesWithInheritedFields() throws Exception {
      assertMembersEquals(new String[]{"s", "i"},new Derived());
   }

   public void testGetAllFieldNamesWithInheritedAndOverriddenFields() throws Exception {
      assertMembersEquals(new String[]{"s", "i"}, new DerivedOverriding());
   }

   private void assertMembersEquals(String[] expectedMembers, Base object) throws Exception {
     ArrayAssert.assertEquivalenceArrays(expectedMembers, ClassUtil.getAllFieldNames(object).toArray());
   }

   public void testGetFieldValueWithoutInheritance() throws Exception {
      Base object = new Base();
      object.setI(1);
     assertEquals(new Integer(1), ClassUtil.getFieldValue(object, "i"));
   }

   public void testGetFieldValueWithInheritance() throws Exception {
      Derived object = new Derived();
      object.setI(1);
      object.setS("test");
      assertEquals(new Integer(1), ClassUtil.getFieldValue(object, "i"));
      assertEquals("test", ClassUtil.getFieldValue(object, "s"));
   }

   public void testGetFieldValueWithInheritanceAndOverriddenFields() throws Exception {
      DerivedOverriding object = new DerivedOverriding();
      object.setI(1);
      object.setS("test");
      assertEquals(new Integer(1), ClassUtil.getFieldValue(object, "i"));
      assertEquals("test", ClassUtil.getFieldValue(object, "s"));
   }

   public void testGetFieldValueThrowExceptionIfMemberDoesNotExist() throws Exception {
      Base object = new Base();
      try {
         ClassUtil.getFieldValue(object, "unkown");
         fail("Did not throw " + NoSuchFieldException.class);
      } catch (NoSuchFieldException e) {
      }
   }



}