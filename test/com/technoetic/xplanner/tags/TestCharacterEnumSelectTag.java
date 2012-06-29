package com.technoetic.xplanner.tags;

import javax.servlet.jsp.tagext.BodyTag;

import junit.framework.TestCase;
import net.sf.xplanner.domain.UserStory;

import com.technoetic.xplanner.XPlannerTestSupport;
import com.technoetic.xplanner.domain.StoryDisposition;
import com.technoetic.xplanner.forms.UserStoryEditorForm;

public class TestCharacterEnumSelectTag extends TestCase {
   private CharacterEnumSelectTag characterEnumSelectTag;
   private XPlannerTestSupport support;
   private UserStory story;
   private UserStoryEditorForm userStoryEditorForm;
   private static final String LF = System.getProperty("line.separator", "\n");

   protected void setUp() throws Exception {
      super.setUp();
      support = new XPlannerTestSupport();
      story = new UserStory();
      story.setDisposition(StoryDisposition.ADDED);
      characterEnumSelectTag = new CharacterEnumSelectTag();
      characterEnumSelectTag.setPageContext(support.pageContext);
      characterEnumSelectTag.setObject(story);
      characterEnumSelectTag.setEnumProperty("disposition");
      characterEnumSelectTag.setName("story");
      characterEnumSelectTag.setProperty("dispositionName");
      support.mapping.setPath("/page.jsp");
      support.request.setContextPath("/xplanner");
      userStoryEditorForm = new UserStoryEditorForm();
   }

   public void testRender_InViewMode() throws Exception
   {
      characterEnumSelectTag.setMode(CharacterEnumSelectTag.VIEW_MODE);
      int startResult = characterEnumSelectTag.doStartTag();
      assertEquals(BodyTag.EVAL_BODY_BUFFERED, startResult);
      int endResult = characterEnumSelectTag.doEndTag();
      assertEquals(BodyTag.EVAL_PAGE, endResult);
      String out = support.jspWriter.output.toString().trim();
      assertEquals("Enum text value not found", "Added", out);
   }

   public void testRender_InEditMode() throws Exception
   {
      String expected = "<select name=\"dispositionName\"><option value='p'>Planned</option>" + LF +
                        "<option value='c'>Carried Over</option>" + LF +
                        "<option value='a'>Added</option>" + LF +
                        "</select>";
      characterEnumSelectTag.setMode(CharacterEnumSelectTag.EDIT_MODE);
      support.pageContext.setAttribute( "story", userStoryEditorForm );
      int startResult = characterEnumSelectTag.doStartTag();
      assertEquals(BodyTag.EVAL_BODY_BUFFERED, startResult);
      int endResult = characterEnumSelectTag.doEndTag();
      assertEquals(BodyTag.EVAL_PAGE, endResult);
      String out = support.jspWriter.output.toString().trim();
      assertEquals("Enum select not found", expected, out);
   }

   public void testRender_InEditModeWithSelectedOption() throws Exception
   {
      String expected = "<select name=\"dispositionName\"><option value='p'>Planned</option>" + LF +
                        "<option value='c' selected='selected'>Carried Over</option>" + LF +
                        "<option value='a'>Added</option>" + LF +
                        "</select>";
      userStoryEditorForm.setDispositionName("c");
      characterEnumSelectTag.setMode(CharacterEnumSelectTag.EDIT_MODE);
      support.pageContext.setAttribute( "story", userStoryEditorForm );
      int startResult = characterEnumSelectTag.doStartTag();
      assertEquals(BodyTag.EVAL_BODY_BUFFERED, startResult);
      int endResult = characterEnumSelectTag.doEndTag();
      assertEquals(BodyTag.EVAL_PAGE, endResult);
      String out = support.jspWriter.output.toString().trim();
      assertEquals("Enum select not found", expected, out);
   }
}