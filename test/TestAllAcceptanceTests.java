import java.io.File;

import junit.framework.Test;
import junitx.util.DirectorySuiteBuilder;
import junitx.util.SimpleTestFilter;

import com.technoetic.xplanner.acceptance.web.FeaturePageTestScript;


public class TestAllAcceptanceTests {

   static Class[] excludedTests = {FeaturePageTestScript.class};

   public static Test suite() throws Exception {
      return buildTestSuite("Acceptance Tests", getClassRootForClass(TestAllAcceptanceTests.class.getName()), excludedTests);
   }

   private static String getClassRootForClass(String fqClassName) {
      String packagePath = fqClassName.replaceAll("\\.", "/")+".class";
      String classFilePath = TestAllAcceptanceTests.class.getResource("/"+packagePath).getFile();
      return classFilePath.substring(0, classFilePath.length() - packagePath.length());
   }

   private static Test buildTestSuite(String name, String rootDir, final Class[] excludedTests) throws Exception {
      return new DirectorySuiteBuilder(new SimpleTestFilter() {
         public boolean include(String classpath) {
             return classpath.endsWith("TestScript.class") && !isExcludedTest(classpath, excludedTests);
         }
      }).suite(rootDir);
   }

   private static boolean isExcludedTest(String classpath, Class[] excludedTests) {
      for (int i = 0; i < excludedTests.length; i++) {
         if (classpath.endsWith(getClassPath(excludedTests[i]))) {
            return true;
         }
      }
      return false;
   }

   private static String getClassPath(Class excludedTest) {
      return excludedTest.getName().replace('.', File.separatorChar) + ".class";
   }

    public static void main(String[] args) throws Exception {
        System.out.println(suite().countTestCases());

    }
}

