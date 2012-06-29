package net.sf.xplanner.acceptance.web;

import net.sf.xplanner.acceptance.util.PersonHelper;
import net.sf.xplanner.domain.Person;
import net.sourceforge.jwebunit.junit.WebTestCase;
import net.sourceforge.jwebunit.util.TestingEngineRegistry;

import org.junit.Before;

public class BaseTest extends WebTestCase {
	protected static final String ADMIN_LOGIN = "sysadmin";
	public static final String TOP_LINK_ID = "topLink";
	public static final String PEOPLE_LINK_ID = "aKO";
	public static final String EDIT_LINK_ID = "aKE";
	public static final String ADD_USER_LINK_ID = "aKA";
	protected PersonHelper personHelper;
	
	
	@Override
	@Before
	public void setUp() throws Exception {
		setTestingEngineKey(TestingEngineRegistry.TESTING_ENGINE_HTMLUNIT);
		getTestContext().setResourceBundleName("ResourceBundle");
		super.setUp();
		setBaseUrl("http://localhost:8080/xplanner");
		personHelper = new PersonHelper(this);
	}
	
	public void doLogin(String userId, String password) {
		beginAt("/do/login");
		assertFormPresent("loginForm");
		setTextField("userId", userId);
		setTextField("password", password);
		submit();
	}
	
	protected void loginAsAdmin() {
		doLogin(ADMIN_LOGIN, "admin");
		assertLinkPresent(PEOPLE_LINK_ID);
	}

	protected void createSysadmin(String userId, String name, String email, String password) {
		personHelper.createSysadmin(userId, name, email, password);
	}

	protected Person getAndLoginAsUser() {
		loginAsAdmin();
		return personHelper.getUser();
	}

}
