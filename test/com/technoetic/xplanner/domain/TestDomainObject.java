package com.technoetic.xplanner.domain;

import junit.framework.TestCase;
import net.sf.xplanner.domain.DomainObject;

public class TestDomainObject extends TestCase{
   DomainObject domainObject;


   public void testEquals() throws Exception
   {
      DomainObject object1 = new DummyDomainObject();
      DomainObject object2 = new DummyDomainObject();
      assertEquals(object1,object2);
   }

}