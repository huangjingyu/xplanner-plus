package com.technoetic.xplanner.forms;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.xplanner.domain.File;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

/**
 * User: Mateusz Prokopowicz
 * Date: Jun 7, 2005
 * Time: 12:02:31 PM
 */
public class ImportForm extends AbstractEditorForm
{
   protected FormFile formFile;
   protected String action = null;
   protected List results = null;
   private File file;
   static final String NO_IMPORT_FILE_KEY = "import.status.no_import_file";

   public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
   {
      ActionErrors errors = new ActionErrors();
      if (isSubmitted())
      {
         if (formFile == null || StringUtils.isEmpty(formFile.getFileName()))
         {
            errors.add(ActionErrors.GLOBAL_ERROR,
                       new ActionError(NO_IMPORT_FILE_KEY));
         }

      }
      return errors;
   }

   public void reset(ActionMapping mapping, HttpServletRequest request)
   {
      super.reset(mapping, request);
      results = new ArrayList();
      formFile = null;
      action = null;
   }

   public void setFormFile(FormFile formFile)
   {
      this.formFile = formFile;
   }

   public FormFile getFormFile()
   {
      return formFile;
   }

   public File getAttachedFile()
   {
      return file;
   }

   public void setFile(File file)
   {
      this.file = file;
   }

   public boolean isSubmitted()
   {
      return action != null && !action.equals("") && !action.equals("null");
   }

   public String getAction()
   {
      return action;
   }

   public void setAction(String action)
   {
      this.action = action;
   }

   public List getResults()
   {
      return results;
   }

   public void setResults(List results)
   {
      this.results = results;
   }
}
