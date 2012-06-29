package com.technoetic.xplanner.security;
import java.io.Serializable;

import junitx.extensions.SerializabilityTestCase;

import com.technoetic.xplanner.security.module.LoginModuleLoader;
import com.technoetic.xplanner.security.module.LoginSupportImpl;
import com.technoetic.xplanner.security.module.MockLoginModule;

public class TestLoginContextSerializability extends SerializabilityTestCase {
   public static MockLoginModule loginModule;

   public TestLoginContextSerializability(String name) {
      super(name);
   }

   protected void setUp() throws Exception {
      super.setUp();
      loginModule = new MockLoginModule();
      loginModule.authenticateReturn = new LoginSupportImpl().createSubject();
   }

   protected Serializable createInstance() throws Exception {
      LoginModuleLoader moduleLoader = new DummyLoginModuleLoader();
      LoginContext context = new DummyLoginContext(moduleLoader);
      return context;
   }

   protected void checkThawedObject(Serializable expected, Serializable actual) throws Exception {
      LoginContext expectedContext = (LoginContext) expected;
      LoginContext actualContext = (LoginContext) actual;
      assertEquals(expectedContext.getSubject(), actualContext.getSubject());
      assertNotNull(actualContext.getLoginModules());
   }

   private static class DummyLoginModuleLoader extends LoginModuleLoader {
      public LoginModule[] loadLoginModules() { return new LoginModule[]{loginModule}; }
   }

   private static class DummyLoginContext extends LoginContext {
      public DummyLoginContext(LoginModuleLoader moduleLoader) {super(moduleLoader);}

      protected LoginModuleLoader createModuleLoader() {
         return new DummyLoginModuleLoader();
      }
   }
}