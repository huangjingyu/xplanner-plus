package junitx.framework;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Jacques
 * Date: Oct 2, 2003
 * Time: 12:59:21 AM
 */
public class CallLog
{
   List<String> actualCalls = new ArrayList<String>();
   List<String> expectedCalls = new ArrayList<String>();

   public void addActualCallTo(String method)
   {
      actualCalls.add(method);
   }

   public void addActualCallToCurrentMethod()
   {
      addActualCallTo(getCallerMethod());
   }

   public void addActualCallToCurrentMethod(Object arg)
   {
      addActualCallTo(getCallerMethod() + "(" + arg + ")");
   }

   public void addExpectedCallTo(String methodName)
   {
      expectedCalls.add(methodName);
   }

   public void addExpectedCallTo(String methodName,
                                 Object arg)
   {
      expectedCalls.add(methodName + "(" + arg + ")");
   }

   public static String getCallerMethod()
   {
      String methodName = "";
      StackTraceElement[] stackTrace = new Throwable().getStackTrace();
      if (stackTrace != null)
      {
         methodName = stackTrace[2].getMethodName();
      }
      return methodName;
   }

   public void verify(String[] expectedMethodCalls)
   {
      //TODO change ArrayAssert to print elements on both side if the element counts are different
      ArrayAssert.assertEquals("calls", expectedMethodCalls, actualCalls.toArray(new String[0]));
   }

   public void verifyNoCalls()
   {
      verify(new String[0]);
   }

   public void verify()
   {
      verify((String[]) expectedCalls.toArray(new String[expectedCalls.size()]));
   }

   public void reset()
   {
      expectedCalls.clear();
      actualCalls.clear();
   }
}
