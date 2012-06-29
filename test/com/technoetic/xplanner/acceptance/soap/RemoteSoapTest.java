package com.technoetic.xplanner.acceptance.soap;



import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.XPlannerTestSupport;
import com.technoetic.xplanner.soap.XPlanner;

public class RemoteSoapTest extends AbstractSoapTestCase {

   public RemoteSoapTest(String s) {
      super(s);
   }

   public XPlanner createXPlanner() throws Exception {
      XPlannerProperties properties = new XPlannerProperties();
      String serviceUrl = XPlannerTestSupport.getAbsoluteTestURL() + "/soap/XPlanner";
      String userId = getUserId() != null ? getUserId() : properties.getProperty("xplanner.test.user");
      String password = getPassword() != null ? getPassword() : properties.getProperty("xplanner.test.password");
      //XPlannerServiceLocator serviceLocator = new XPlannerServiceLocator();
      //XPlanner service = serviceLocator.getXPlanner(new URL(serviceUrl));
      //((Stub) service).setUsername(userId);
      //((Stub) service).setPassword(password);
      //return service;
      return null;
   }
}
