package com.technoetic.xplanner.db;

/**
 * Created by IntelliJ IDEA.
 * User: tkmower
 * Date: Nov 24, 2004
 * Time: 12:19:47 PM
 */

import static org.easymock.EasyMock.expect;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.AbstractUnitTestCase;
import com.technoetic.xplanner.db.hibernate.ThreadSession;

public class TestSearchContentQuery extends AbstractUnitTestCase {
   SearchContentQuery searchContentQuery;
   private Session mockSession;
   protected static final int RESTRICT_TO_PROJECT_ID = 1;

   protected void setUp() throws Exception {
      super.setUp();
      mockSession = createLocalMock(Session.class);
      ThreadSession.set(mockSession);
      searchContentQuery = new SearchContentQuery();
   }

   public void testFind_WhereNameOrDescriptionContains() throws Exception {
      String query = Object.class.getName() + "SearchQuery";
      Query mockQuery = createLocalMock(Query.class);
      expect(mockSession.getNamedQuery(query)).andReturn(mockQuery);
      List value = Arrays.asList(new String[]{"Value 1", "Value 2"});
      expect(mockQuery.setString("contents", "%whatever%")).andReturn(null);
      expect(mockQuery.list()).andReturn(value);
      replay();

      Object loadedObject = searchContentQuery.findWhereNameOrDescriptionContains("whatever", Object.class);

      verify();
      assertEquals(value, loadedObject);
   }

   public void testFind_withRestrictedScopeToProjectId() throws Exception {
      searchContentQuery.setRestrictedProjectId(RESTRICT_TO_PROJECT_ID);
      String query = Object.class.getName() + "RestrictedSearchQuery";
      Query mockQuery = createLocalMock(Query.class);
      expect(mockSession.getNamedQuery(query)).andReturn(mockQuery);
      List value = Arrays.asList(new String[]{"Value 1", "Value 2"});
      expect(mockQuery.setString("contents", "%whatever%")).andReturn(null);
      expect(mockQuery.setInteger("projectId", RESTRICT_TO_PROJECT_ID)).andReturn(null);
      expect(mockQuery.list()).andReturn(value);
      replay();

      Object loadedObject = searchContentQuery.findWhereNameOrDescriptionContains("whatever", Object.class);

      verify();
      assertEquals(value, loadedObject);
   }
}