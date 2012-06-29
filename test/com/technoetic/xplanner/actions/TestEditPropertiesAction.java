package com.technoetic.xplanner.actions;
/**
 * Created by IntelliJ IDEA.
 * User: sg620641
 * Date: Oct 24, 2005
 * Time: 4:27:58 PM
 * To change this template use File | Settings | File Templates.
 */

import java.util.Properties;

import junit.framework.TestCase;

import org.apache.struts.action.ActionForward;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.XPlannerTestSupport;

public class TestEditPropertiesAction extends TestCase {
   private EditPropertiesAction editPropertiesAction;
   private XPlannerTestSupport support;
   private Properties properties;
   protected static final String TEST_PROPERTY_NAME = "test.property.name";

   protected void setUp() throws Exception {
      properties = new Properties();
      properties.setProperty(TEST_PROPERTY_NAME, "value");
      support = new XPlannerTestSupport();
      editPropertiesAction = new EditPropertiesAction() {
         protected Properties getPropertiesToUpdate(XPlannerProperties xPlannerProperties) {
            return properties;
         }
      };
   }

   public void testEditProperties() throws Exception {
      assertEquals("value", properties.getProperty(TEST_PROPERTY_NAME));
      support.request.setParameterValue(EditPropertiesAction.PROPERTY_NAME_PARAM, new String[]{TEST_PROPERTY_NAME});
      support.request.setParameterValue(EditPropertiesAction.PROPERTY_VALUE_PARAM, new String[]{"new value"});
      support.request.setParameterValue(EditPropertiesAction.RETURN_TO_PARAM, new String[]{"/view/projects"});
      ActionForward actionForward = support.executeAction(editPropertiesAction);
      assertEquals("/view/projects", actionForward.getPath());
      assertEquals("new value", properties.getProperty(TEST_PROPERTY_NAME));
   }
}