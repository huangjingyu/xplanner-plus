import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class TestAllUnitTests extends TestCase {

   private static Class[] excludedTestCandidateClasses = {
   };
   private static String[] excludedDirs = { ".dependency-info", ".svn", "acceptance"};
   private static String[] includedPatterns = { "Test.*" };
   private static String[] excludedPatterns = { "TestAll.*" };

   public TestAllUnitTests(String name) {
      super(name);
   }

   public static Test suite() {
      BasicConfigurator.configure();
      Logger.getRootLogger().setLevel(Level.FATAL);
      return makeTestSuite();
   }

   public static void printTestsInSuite() {
      TestSuite suite = makeTestSuite();
      java.util.Enumeration enumeration = suite.tests();
      while (enumeration.hasMoreElements()) {
         Test test = (Test) enumeration.nextElement();
         System.out.println("test = " + test.toString());
      }
   }			

   private static TestSuite makeTestSuite() {
      TestSuite suite = new TestSuite("All Tests in classpath");
      File file = new File(getTestClassPath().replaceAll("%20", " "));
      List currentPath = new ArrayList();
      addTestsInHierarchyToSuite(suite, file, currentPath);
      return suite;
   }

   private static String getTestPath() {
      String relativeClassFilePath = TestAllUnitTests.class.getName().replaceAll("\\.", "/") + ".class";
      String absoluteClassFilePath = TestAllUnitTests.class.getResource("/" + relativeClassFilePath).getFile();
      return absoluteClassFilePath.substring(0, absoluteClassFilePath.length() - relativeClassFilePath.length());
   }

   private static String getTestClassPath() {
      String classPath = System.getProperty("TestPath");
      if (classPath != null) return classPath;
      return getTestPath();
   }

   private static void addTestsInHierarchyToSuite(TestSuite suite, File classPathRoot, List currentPath) {
      addTestsInDirectoryToSuite(suite, classPathRoot, currentPath);
      File[] tests = classPathRoot.listFiles(directoryFilter);
      if (tests == null) {
         return;
      }
      for (int i = 0; i < tests.length; i++) {
         File directory = tests[i];
         currentPath.add(directory.getName());
         try {
            addTestsInHierarchyToSuite(suite,
                                       new File(classPathRoot.getCanonicalPath() +
                                                File .separator +
                                                directory.getName()),
                                       currentPath);
         } catch (Exception ex) {
            System.out.println("ex = " + ex);
         }
         currentPath.remove(currentPath.size() - 1);
      }
   }

   private static void addTestsInDirectoryToSuite(TestSuite suite, File classPathRoot, List currentPath) {
      File[] tests = classPathRoot.listFiles(testFilter);
      if (tests == null) {
         return;
      }
      for (int i = 0; i < tests.length; i++) {
         addTestToSuite(suite, currentPath, tests[i]);
      }
   }

   private static String makeFullyQualifiedClassName(List currentPath, File testcaseFile) {
      StringBuffer fullyQualified = new StringBuffer();
      for (Iterator iterator = currentPath.iterator(); iterator.hasNext();) {
         fullyQualified.append((String) iterator.next());
         fullyQualified.append(".");
      }
      fullyQualified.append(stripClassSuffix(testcaseFile.getName()));
      return fullyQualified.toString();
   }

   private static void addTestToSuite(TestSuite suite, List currentPath, File testcaseFile) {
      String fullClassName = makeFullyQualifiedClassName(currentPath, testcaseFile);
      try {
    	  Class<?> testClass = Class.forName(fullClassName);
    	  if (testClass.isAssignableFrom(Test.class)) {
    		  suite.addTest(new TestSuite(testClass));
    	  }else{
    		  suite.addTest(new JUnit4TestAdapter(testClass));
    	  }
      } catch (ClassNotFoundException ex) {
         throw new RuntimeException("TestAll: could not load class " + fullClassName);
      }
   }

   private static String stripClassSuffix(String filename) {
      int endingIndex = filename.indexOf(".class");
      return filename.substring(0, endingIndex);
   }

   private static final FileFilter directoryFilter = new FileFilter() {
      public boolean accept(File pathname) {
         String path = pathname.getName();
         if (!pathname.isDirectory()) return false;
         for (int i = 0; i < excludedDirs.length; i++) {
            if (path.endsWith(excludedDirs[i])) return false;
         }
         return true;
      }
   };

   private static final FilenameFilter testFilter = new FilenameFilter() {
      public boolean accept(File dir, String name) {
         return isTestClass(name, dir.getPath() + File.separator + name);
      }
   };

   private static boolean isTestClass(String filename, String path) {
      return (filename.startsWith("Test") &&
             filename.endsWith(".class") &&
             !filename.startsWith("TestAll") &&
             !(filename.indexOf("$") >= 0) &&
             !isExcludedTest(path, excludedTestCandidateClasses));
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

}
