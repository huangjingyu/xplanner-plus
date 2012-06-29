package net.sf.xplanner.acceptance.web;

import java.text.MessageFormat;

import org.displaytag.util.HtmlTagUtil;
import org.junit.Test;

import com.technoetic.xplanner.security.LoginModule;

public class LoginTest extends BaseTest {
	private String developerUserId = "sysadmin";

	@Test
	public void testLogin_WrongPassword() {
		doLogin(developerUserId, "password_xyz");
		assertTextPresent(getErrorMessage(LoginModule.MESSAGE_AUTHENTICATION_FAILED_KEY));
	}

	@Test
	public void testLogin_NoUser() {
		doLogin("userWhoDoesNotExist", "password");
		assertTextPresent(getErrorMessage(LoginModule.MESSAGE_USER_NOT_FOUND_KEY));
	}
	
	@Test
	public void testLoginAdmin() {
		loginAsAdmin();
	}

	private String getErrorMessage(String key) {
		MessageFormat messageFormat = new MessageFormat(tester.getMessage(key));
		return HtmlTagUtil.stripHTMLTags(messageFormat.format(new Object[] { "XPlanner" }));
	}
}
