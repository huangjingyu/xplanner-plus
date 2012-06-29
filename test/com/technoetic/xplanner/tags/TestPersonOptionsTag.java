package com.technoetic.xplanner.tags;

import java.util.ArrayList;
import java.util.List;

import junitx.framework.ArrayAssert;
import net.sf.xplanner.domain.Person;

import com.technoetic.mocks.hibernate.MockQuery;

public class TestPersonOptionsTag extends AbstractOptionsTagTestCase {
    private int PROJECT_ID = 11;
    private Person person2;
    private Person person1;

    public void testProjectSpecificListWithExplicitProjectId() throws Throwable {
        getTag().setProjectId(PROJECT_ID);
        setUpMockQuery();
        setUpAuthorizedPerson(person1);

        List options = tag.getOptions();

       assertProjectPeopleCount(1);
    }

   private void assertProjectPeopleCount(int expectedCount) {
      assertTrue("database not accessed", support.hibernateSession.createQueryCalled);
      assertEquals(PersonOptionsTag.ALL_ACTIVE_PEOPLE_QUERY, support.hibernateSession.createQueryQueryString);
      assertEquals("authorizer not used", expectedCount, authorizer.getPeopleForPrincipalOnProjectCount);
   }

   public void testProjectSpecificListWithImplicitProjectId() throws Throwable {
       setUpDomainContext();
       setUpMockQuery();
       setUpAuthorizedPerson(person1);

       List options = tag.getOptions();

       Person[] expectedPersons = new Person[]{person1};
       ArrayAssert.assertEquals(expectedPersons, options.toArray());

      assertProjectPeopleCount(1);
   }

   private void assertB() {
      assertTrue("database not accessed", support.hibernateSession.findCalled);
      assertEquals("authorizer not used", 1, authorizer.getPeopleForPrincipalOnProjectCount);
   }

   public void testProjectSpecificListWithProjectIdInRequest() throws Throwable {
       support.request.setParameterValue("projectId", new String[]{"11"});
       setUpMockQuery();
       setUpAuthorizedPerson(person1);

      List options = tag.getOptions();

       Person[] expectedPersons = new Person[]{person1};
       ArrayAssert.assertEquals(expectedPersons, options.toArray());

      assertProjectPeopleCount(1);
   }

   private void setUpAuthorizedPerson(Person person) {
      if (authorizer.getPeopleForPrincipalOnProjectReturn == null)
         authorizer.getPeopleForPrincipalOnProjectReturn = new ArrayList();
      authorizer.getPeopleForPrincipalOnProjectReturn.add(person);
   }

   public void testSystemPersonOptionsWithDomainContext() throws Throwable {
       setUpDomainContext();
       getTag().setFiltered("false");
       setUpMockQuery();

       List options = tag.getOptions();

       Person[] expectedPersons = new Person[]{person1, person2};
       ArrayAssert.assertEquals(expectedPersons, options.toArray());

      assertProjectPeopleCount(0);
   }

    protected void setUp() throws Exception {
        tag = new PersonOptionsTag();
        super.setUp();
        authorizer.hasPermissionReturns = new Boolean[]{Boolean.TRUE, Boolean.FALSE};
    }

    public PersonOptionsTag getTag() {
        return (PersonOptionsTag) tag;
    }

    private void setUpDomainContext() {
        DomainContext context = new DomainContext();
        context.save(support.pageContext.getRequest());
        context.setProjectId(PROJECT_ID);
    }

    private void setUpMockQuery() {
        ArrayList results = new ArrayList();
        person1 = new Person();
        person1.setId(22);
        person1.setName("XYZ");
        person1.setUserId("userId1");
        results.add(person1);
        person2 = new Person();
        person2.setId(33);
        person2.setName("ABC");
        person2.setUserId("userId2");
        results.add(person2);
        MockQuery mockQuery = new MockQuery();
        mockQuery.listReturn = results;
        support.hibernateSession.createQueryReturn = mockQuery;
    }
}
