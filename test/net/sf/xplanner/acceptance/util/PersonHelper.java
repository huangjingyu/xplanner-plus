package net.sf.xplanner.acceptance.util;

import static net.sf.xplanner.acceptance.web.BaseTest.*;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.xplanner.domain.Person;
import net.sourceforge.jwebunit.html.Cell;
import net.sourceforge.jwebunit.html.Row;
import net.sourceforge.jwebunit.html.Table;
import net.sourceforge.jwebunit.junit.WebTestCase;

import org.apache.commons.lang.RandomStringUtils;
public class PersonHelper {
	public static final String SYSTEM_ADMIN_FIELD = "systemAdmin";
	public static final String EMAIL_FIELD = "email";
	public static final String USER_ID_FIELD = "userIdentifier";
	public static final String NAME_FIELD = "name";
	private WebTestCase tester;

	public PersonHelper(WebTestCase tester) {
		this.tester = tester;
	}

	public void createSysadmin(String userId, String name, String email,
			String password) {
		tester.clickLink(TOP_LINK_ID);
		tester.clickLink(PEOPLE_LINK_ID);
		tester.clickLink(ADD_USER_LINK_ID);
		tester.setTextField(NAME_FIELD, name);
		tester.setTextField(USER_ID_FIELD, userId);
		tester.setTextField("initials", RandomStringUtils.randomAlphabetic(4));
		tester.setTextField(EMAIL_FIELD, email);
		tester.setTextField("newPassword", password);
		tester.setTextField("newPasswordConfirm", password);
		tester.checkCheckbox(SYSTEM_ADMIN_FIELD);
		tester.submit();
		tester.assertLinkPresentWithExactText(userId);
		tester.assertLinkPresentWithExactText(name);
		tester.assertLinkPresentWithExactText(email);
	}

	public Person getUser() {
		tester.clickLink(TOP_LINK_ID);
		tester.clickLink(PEOPLE_LINK_ID);
		Pattern pattern = Pattern.compile(".*oid=(\\d+)\\D.*");
		Table table = tester.getTable("objecttable");
		System.out.println(table.getRowCount());
		System.out.println(table.getRows());
		List<Row> rows = table.getRows();
		for (int i = 1; i < rows.size(); i++) {
			Row row = rows.get(i);
			List<Cell> cells = row.getCells();
			if(!cells.get(1).getValue().equals("sysadmin")) {
//				tester.clickLinkWithText(cells.get(1).getValue());
				String href = tester.getElementByXPath(".//*[@id='objecttable']/tbody/tr[3]/td[2]/a").getAttribute("href");
				Matcher matcher = pattern.matcher(href);
				if(matcher.find()) {
					
				};
				
			}
		}
		return null;
	}

}
