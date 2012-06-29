package com.technoetic.xplanner.domain;

import java.util.Iterator;

import junit.framework.TestCase;
import net.sf.xplanner.domain.Project;

public class TestDomainClass extends TestCase {

   public void testAddMapping() throws Exception {
      DomainClass domainClass = new DomainClass("project", Project.class);

      ActionMapping[] mappings = new ActionMapping[6];

      for (int i = 0; i < mappings.length; i++) {
         ActionMapping mapping = new ActionMapping("action", "actionKey", "permission", "iconPath", "targetPage", "domainType", false);
         mappings[i] = mapping;
         domainClass.addMapping(mapping);
      }

      Iterator iter = domainClass.getActions().iterator();
      for (int i = 0; i < mappings.length; i++) {
         ActionMapping expectedMapping = mappings[i];
         assertSame(expectedMapping, iter.next());
      }
   }
}