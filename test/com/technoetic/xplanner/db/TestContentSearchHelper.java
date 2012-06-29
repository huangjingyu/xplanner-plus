package com.technoetic.xplanner.db;

/**
 * Created by IntelliJ IDEA.
 * User: tkmower
 * Date: Nov 23, 2004
 * Time: 10:43:24 AM
 */

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Note;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import org.apache.commons.collections.Predicate;

import com.technoetic.xplanner.domain.SearchResult;


public class TestContentSearchHelper extends TestCase {
   ContentSearchHelper helper;
   private static final int PROJECT_ID = 1;
   private static final int ITERATION_ID = 2;
   private static final int STORY_ID = 3;
   private static final int TASK_ID = 4;
   private static final int NOTE_ID = 5;
   public static final String EXPECTED_SEARCH_CRITERIA = "Some";
   private SearchResultFactory searchResultFactory;
   private Map availableObjects;
   private List expectedObjects;
   private SearchContentQuery searchContentQuery;
   public static final int UNAUTHORIZED_USER_ID = 1;
   public static final int AUTHORIZED_USER_ID = 0;
   private boolean authorized = true;

   @Override
protected void setUp() throws Exception {
      super.setUp();
      searchResultFactory = new SearchResultFactory(new HashMap());

      FakeNameable project = new FakeNameable(PROJECT_ID, "some project", "");
      FakeNameable iteration = new FakeNameable(ITERATION_ID, "some iteration", "");
      FakeNameable story = new FakeNameable(STORY_ID, "some story", "");
      FakeNameable task = new FakeNameable(TASK_ID, "some task", "");
      FakeNameable note = new FakeNameable(NOTE_ID, "some note", "");

      expectedObjects = new LinkedList();
      expectedObjects.addAll(Arrays.asList(new Object[]{
            new SearchResult(project, "project.type"),
            new SearchResult(iteration, "iteration.type"),
            new SearchResult(story, "story.type"),
            new SearchResult(task, "task.type"),
            new SearchResult(note, "note.type")}));

        availableObjects = new HashMap();
        availableObjects.put(Project.class, Arrays.asList(new Object[]{project}));
        availableObjects.put(Iteration.class, Arrays.asList(new Object[]{iteration}));
        availableObjects.put(UserStory.class, Arrays.asList(new Object[]{story}));
        availableObjects.put(Task.class, Arrays.asList(new Object[]{task}));
        availableObjects.put(Note.class, Arrays.asList(new Object[]{note}));

      searchContentQuery = new SearchContentQuery() {
         @Override
		public List findWhereNameOrDescriptionContains(String searchCriteria,
                                                        Class objectClass) {
            assertEquals(EXPECTED_SEARCH_CRITERIA, searchCriteria);
            return (List) availableObjects.get(objectClass);
         }
      };
      helper = new ContentSearchHelper(searchResultFactory, searchContentQuery) {
         @Override
		protected Predicate getAuthorizationPredicate(int userId) {
            return new Predicate() {
               public boolean evaluate(Object o) {
                  return authorized;
               }
            };
         }
      };
   }

   public void testSearch() throws Exception {
      helper.search(EXPECTED_SEARCH_CRITERIA, AUTHORIZED_USER_ID, 0);
      List list = helper.getSearchResults();
      assertEquals(expectedObjects.size(), list.size());
      for (Iterator iterator = expectedObjects.iterator(); iterator.hasNext();) {
         SearchResult object = (SearchResult) iterator.next();
         assertTrue(list.contains(object));
      }
   }

   public void testSearch_UnauthorizedUser() throws Exception {
      authorized = false;
      helper.search(EXPECTED_SEARCH_CRITERIA, UNAUTHORIZED_USER_ID, 0);
      List list = helper.getSearchResults();
      assertEquals(0, list.size());
   }
}