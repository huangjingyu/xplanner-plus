/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: May 15, 2006
 * Time: 4:26:22 PM
 */

import junit.framework.Test;
import junitx.extensions.TestSetup;

import com.technoetic.xplanner.webservers.JettyServer;

public class JettyTestDecorator extends TestSetup {

  public JettyTestDecorator(Test test) {
    super(test);
  }

  protected void setUp() throws Exception {
    super.setUp();
    JettyServer.start();
  }

  protected void tearDown() throws Exception {
    super.tearDown();
//    JettyServer.stop();
  }
}