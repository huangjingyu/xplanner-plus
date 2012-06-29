/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: May 12, 2005
 * Time: 11:08:51 PM
 */
package com.technoetic.xplanner.actions;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.dao.impl.CommonDao;
import net.sf.xplanner.domain.Iteration;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.forms.AbstractEditorForm;
import com.technoetic.xplanner.forms.ImportStoriesForm;
import com.technoetic.xplanner.importer.MissingColumnHeaderSpreadsheetImporterException;
import com.technoetic.xplanner.importer.MissingFieldSpreadsheetImporterException;
import com.technoetic.xplanner.importer.SpreadsheetStoryImporter;
import com.technoetic.xplanner.importer.WrongImportFileSpreadsheetImporterException;
import com.technoetic.xplanner.importer.spreadsheet.MissingWorksheetException;
import com.technoetic.xplanner.importer.spreadsheet.SpreadsheetHeaderConfiguration;
import com.technoetic.xplanner.util.CookieSupport;

public class ImportStoriesAction extends EditObjectAction
{
   SpreadsheetStoryImporter importer;
public static final String WORKSHEET_NAME_PROPERTY_KEY = "import.spreadsheet.worksheet.name";
   public static final String STORY_TITLE_PROPERTY_KEY = "import.spreadsheet.story.title.column.header";
   public static final String STORY_ESTIMATE_PROPERTY_KEY = "import.spreadsheet.story.estimate.column.header";
   public static final String ITERATION_END_DATE_PROPERTY_KEY = "import.spreadsheet.iteration.end.date.column.header";
   public static final String STORY_PRIORITY_PROPERTY_KEY = "import.spreadsheet.story.priority.column.header";
   public static final String STORY_STATUS_PROPERTY_KEY = "import.spreadsheet.story.status.column.header";
   public final static String ONLY_INCOMPLETE_COOKIE_NAME = "import.spreadsheet.onlyIncomplete";
   public final static String COMPLETED_STORY_STATUS_KEY = "import.spreadsheet.completedStoryStatus";

   @Override
public ActionForward execute(ActionMapping mapping,
                                ActionForm actionForm,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception
   {
      ActionErrors errors = new ActionErrors();
      try
      {
         return super.execute(mapping, actionForm, request, response);
      }
      catch (WrongImportFileSpreadsheetImporterException e)
      {
         errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("import.status.corrupted_file"));
      }
      catch (MissingFieldSpreadsheetImporterException e)
      {
         errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("import.status.missing_required_field", e.getField()));
      }
      catch (MissingColumnHeaderSpreadsheetImporterException e)
      {
         errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("import.status.wrong_header", e.getColumnName()));
      }
      catch (MissingWorksheetException e)
      {
         errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("import.status.worksheet_not_found", e.getWorksheetName()));
      }
      saveErrors(request, errors);
      return mapping.getInputForward();
   }

   @Override
protected void populateObject(HttpServletRequest request, Object object, ActionForm form) throws IOException
   {
      ImportStoriesForm importStoriesForm = (ImportStoriesForm) form;
      SpreadsheetHeaderConfiguration headerConfiguration = new SpreadsheetHeaderConfiguration();
      populateHeaderConfiguration(headerConfiguration, importStoriesForm);
      importStoriesForm.setResults(
         importer.importStories((Iteration) object,
                                headerConfiguration,
                                importStoriesForm.getFormFile().getInputStream(),
                                importStoriesForm.isOnlyIncomplete()));
   }

   private void populateHeaderConfiguration(SpreadsheetHeaderConfiguration headerConfiguration,
                                            ImportStoriesForm form)
   {
      headerConfiguration.setWorksheetName(form.getWorksheetName());
      headerConfiguration.setTitleHeader(form.getTitleColumn());
      headerConfiguration.setEndDateHeader(form.getEndDateColumn());
      headerConfiguration.setEstimateHeader(form.getEstimateColumn());
      headerConfiguration.setPriorityHeader(form.getPriorityColumn());
      headerConfiguration.setStatusHeader(form.getStatusColumn());
   }

   @Override
protected void setCookies(AbstractEditorForm form,
                             ActionMapping mapping,
                             HttpServletRequest request,
                             HttpServletResponse response)
   {
      ImportStoriesForm importForm = (ImportStoriesForm) form;
      addCookie(WORKSHEET_NAME_PROPERTY_KEY, importForm.getWorksheetName(), response);
      addCookie(STORY_TITLE_PROPERTY_KEY, importForm.getTitleColumn(), response);
      addCookie(ITERATION_END_DATE_PROPERTY_KEY, importForm.getEndDateColumn(), response);
      addCookie(STORY_ESTIMATE_PROPERTY_KEY, importForm.getEstimateColumn(), response);
      addCookie(STORY_PRIORITY_PROPERTY_KEY, importForm.getPriorityColumn(), response);
      addCookie(STORY_STATUS_PROPERTY_KEY, importForm.getStatusColumn(), response);
      addCookie(ONLY_INCOMPLETE_COOKIE_NAME, "" + importForm.isOnlyIncomplete(), response);
      addCookie(COMPLETED_STORY_STATUS_KEY, importForm.getCompletedStatus(), response);
   }

   private void addCookie(String propertyKey, String value, HttpServletResponse response) {
      CookieSupport.createCookie(propertyKey, value, response);
   }

   @Override
protected void populateForm(AbstractEditorForm actionForm, ActionMapping actionMapping, HttpServletRequest request)
   {
      ImportStoriesForm form = (ImportStoriesForm) actionForm;
      form.setWorksheetName(getValueFromCookieOrProperties(WORKSHEET_NAME_PROPERTY_KEY, request));
      form.setTitleColumn(getValueFromCookieOrProperties(STORY_TITLE_PROPERTY_KEY, request));
      form.setEndDateColumn(getValueFromCookieOrProperties(ITERATION_END_DATE_PROPERTY_KEY, request));
      form.setPriorityColumn(getValueFromCookieOrProperties(STORY_PRIORITY_PROPERTY_KEY, request));
      form.setEstimateColumn(getValueFromCookieOrProperties(STORY_ESTIMATE_PROPERTY_KEY, request));
      form.setStatusColumn(getValueFromCookieOrProperties(STORY_STATUS_PROPERTY_KEY, request));
      form.setOnlyIncomplete(
            Boolean.valueOf(getValueFromCookieOrProperties(ONLY_INCOMPLETE_COOKIE_NAME, request)).booleanValue());
      form.setCompletedStatus(getValueFromCookieOrProperties(COMPLETED_STORY_STATUS_KEY, request));
   }

   public SpreadsheetStoryImporter getImporter()
   {
      return importer;
   }

   public void setImporter(SpreadsheetStoryImporter importer)
   {
      this.importer = importer;
   }

   public String getValueFromCookieOrProperties(String key, HttpServletRequest request)
   {
      Cookie cookie = CookieSupport.getCookie(key, request);
      if (cookie != null)
      {
         return cookie.getValue();
      }
      XPlannerProperties xplannerProperties = new XPlannerProperties();
      return xplannerProperties.getProperty(key);
   }
}