package net.sf.xplanner.acceptance.web;

import static net.sf.xplanner.acceptance.util.PersonHelper.*;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class PeoplePageTest extends BaseTest {

	@BeforeClass
	@Override
	public void setUp() throws Exception {
		super.setUp();
		loginAsAdmin();
	}
	
	@Test
	public void testSysadminPresent() {
		clickLink(PEOPLE_LINK_ID);
		assertTitleEquals("People");
		assertTablePresent("objecttable");
		assertTextInTable("objecttable", ADMIN_LOGIN);
	}
	
	@Test
	@Ignore
	public void testAddAndEditPerson() {
		clickLink(PEOPLE_LINK_ID);
		clickLink(ADD_USER_LINK_ID);
		assertFormPresent("personEditor");
		String userId = "userId_" + RandomStringUtils.random(3, false, true);
		String name = "user_" + RandomStringUtils.random(3, false, true);
		String email = userId + "@test.com";
		String password = "password_" + RandomStringUtils.random(3, false, true);
		createSysadmin(userId, name, email, password);
		assertTitleEquals("People");
		clickLinkWithExactText(userId);
		clickLink(EDIT_LINK_ID);

		assertFormPresent("personEditor");
		assertTextFieldEquals(NAME_FIELD, name);
		assertTextFieldEquals(USER_ID_FIELD, userId);
		assertTextFieldEquals(EMAIL_FIELD, email);
		assertCheckboxSelected(SYSTEM_ADMIN_FIELD);
		
	}


}
