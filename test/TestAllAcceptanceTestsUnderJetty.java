/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: May 15, 2006
 * Time: 4:33:02 PM
 */

import junit.framework.Test;
import junit.framework.TestSuite;

public class TestAllAcceptanceTestsUnderJetty extends TestSuite {
  public static Test suite() throws Exception {
    return new JettyTestDecorator(TestAllAcceptanceTests.suite());
  }
}