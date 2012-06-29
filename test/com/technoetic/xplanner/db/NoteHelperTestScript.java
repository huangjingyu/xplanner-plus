package com.technoetic.xplanner.db;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Note;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript;
import com.technoetic.xplanner.domain.NoteAttachable;

/**
 * Created by IntelliJ IDEA.
 * User: tkmower
 * Date: Sep 1, 2005
 * Time: 2:26:04 PM
 */
public class NoteHelperTestScript extends AbstractDatabaseTestScript {
   private Note taskNote;
   private Note storyNote;
   private Note iterationNote;
   private Note projectNote;
   private Project project;
   private Iteration iteration;
   private UserStory story;
   private Task task;

   @Override
protected void setUp() throws Exception {
      super.setUp();
      assertNotNull(getSession());

      project = newProject();
      iteration = newIteration(project);
      story = newUserStory(iteration);
      task = newTask(story);
      Person person = newPerson();

      projectNote = newNote(project, person);
      assertTrue(projectNote.getId() != 0);
      projectNote.setSubject("project note");
      iterationNote = newNote(iteration, person);
      iterationNote.setSubject("iteration note");
      storyNote = newNote(story, person);
      storyNote.setSubject("story note");
      taskNote = newNote(task, person);
      taskNote.setSubject("task note");

      commitSession();
      assertSessionContains(new Note[]{projectNote, iterationNote, storyNote, taskNote});
   }

   public void testDeleteNotesForTask() throws Exception
   {
      assertCorrectDeletions(task,
                             new Note[]{taskNote},
                             new Note[]{storyNote, iterationNote, projectNote});
   }

   public void testDeleteNotesForStoryAndTask() throws Exception
   {
      assertCorrectDeletions(story,
                             new Note[]{taskNote, storyNote},
                             new Note[]{iterationNote, projectNote});
   }

   public void testDeleteNotesForIterationStoryAndTask() throws Exception
   {
      assertCorrectDeletions(iteration,
                             new Note[]{taskNote, storyNote, iterationNote},
                             new Note[]{projectNote});
   }

   public void testDeleteNotesForProjectIterationStoryAndTask() throws Exception
   {
      assertCorrectDeletions(project,
                             new Note[]{taskNote, storyNote, iterationNote, projectNote},
                             new Note[]{});
   }

   private void assertCorrectDeletions(NoteAttachable objectDeleted, Note[] expectedDeletions, Note[] expectedRemaining)
         throws Exception {
      NoteHelper.deleteNotesFor(objectDeleted, getSession());
      commitSession();
      assertNotInSession(expectedDeletions);
      assertSessionContains(expectedRemaining);
   }

   private void assertNotInSession(Note[] notesToCheck) throws Exception {
      assertSessionState(notesToCheck, false);
   }

   private void assertSessionContains(Note[] notesToCheck) throws Exception {
      assertSessionState(notesToCheck, true);
   }

   private void assertSessionState(Note[] notesToCheck, boolean expected) throws Exception {
      for (int i = 0; i < notesToCheck.length; i++) {
         Note note = notesToCheck[i];
         assertEquals(note.getSubject() + (expected?" not ":" ") + "in session ", expected, getSession().contains(note));
      }
   }

}
