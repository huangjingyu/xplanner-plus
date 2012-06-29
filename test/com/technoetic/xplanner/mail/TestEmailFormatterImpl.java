package com.technoetic.xplanner.mail;

/**
 * User: Mateusz Prokopowicz
 * Date: May 19, 2005
 * Time: 4:23:04 PM
 */

import static org.easymock.EasyMock.expect;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;

import com.technoetic.xplanner.AbstractUnitTestCase;
import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.util.HttpClient;

public class TestEmailFormatterImpl extends AbstractUnitTestCase
{
   EmailFormatterImpl emailFormatterImpl;
   HttpClient mockHttpClient;
   XPlannerProperties properties = new XPlannerProperties();

   protected void setUp() throws Exception {
      super.setUp();
      mockHttpClient = createLocalMock(HttpClient.class);
      emailFormatterImpl = new EmailFormatterImpl();
   }

   public void testFormatEmailEntry() throws Exception
   {
      List entryList = new ArrayList();
      final MockTemplate mockTemplate = new MockTemplate();
      VelocityEngine mockVelocityEngine = new VelocityEngine()
      {
         public Template getTemplate(String name)
         {
            return mockTemplate;
         }
      };

      emailFormatterImpl.setVelocityEngine(mockVelocityEngine);
      emailFormatterImpl.setHttpClient(mockHttpClient);
      expect(
         mockHttpClient.getPage(properties.getProperty("xplanner.application.url") + "/css/email.css")).andReturn("");
      replay();
      emailFormatterImpl.formatEmailEntry("header",
                                          "footer",
                                          "story",
                                          "task",
                                          entryList);
      verify();
      assertTrue("Velocity has not been called", mockTemplate.mergeCalled);
   }

   private static class MockTemplate extends Template
   {
      boolean mergeCalled = false;

      public void merge(Context context, Writer writer)
      {
         mergeCalled = true;
      }
   }
}