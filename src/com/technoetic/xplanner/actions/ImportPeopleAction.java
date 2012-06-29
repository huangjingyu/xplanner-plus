package com.technoetic.xplanner.actions;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.dao.PersonDao;
import net.sf.xplanner.domain.Person;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.technoetic.xplanner.domain.repository.DuplicateUserIdException;
import com.technoetic.xplanner.forms.ImportPeopleForm;
import com.technoetic.xplanner.security.auth.AuthorizationException;
import com.technoetic.xplanner.util.Callable;

/**
 * User: Mateusz Prokopowicz
 * Date: Dec 2, 2004
 * Time: 5:59:21 PM
 */
public class ImportPeopleAction extends AbstractAction {
   private static final int NBR_OF_COLUMNS = 5;
   private static final String STATUS_SUCCESS_KEY = "people.import.status.success";
   private static final String STATUS_WRONG_ENTRY_KEY = "people.import.status.wrong_entry_format";
   private static final String STATUS_EMPTY_USERID_KEY = "people.import.status.empty_userId";
   private static final String STATUS_USERID_EXISTS_KEY = "people.import.status.userId_exists";
   static final String EDIT_PERSON_ACTION_NAME = "/edit/person";
   private PersonDao personDao;

   public ActionForward execute(ActionMapping mapping,
                                ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {
      ImportPeopleForm importForm = (ImportPeopleForm) form;
      FormFile formFile = importForm.getFormFile();
      if (formFile != null) {
         String filename = formFile.getFileName();
         if (StringUtils.isNotEmpty(filename)) {
            String contentType = formFile.getContentType();
            InputStream input = formFile.getInputStream();
            int fileSize = formFile.getFileSize();
            BufferedReader importReader = new BufferedReader(new InputStreamReader(input));
            for (String line = importReader.readLine();
                 line != null;
                 line = importReader.readLine()) {
               List entry = new ArrayList();
               String status = null;
               String id = "";
               entry.addAll(Arrays.asList(line.split(",")));
               try {
                  if (entry.size() < NBR_OF_COLUMNS) {
                     throw new PeopleImportException(STATUS_WRONG_ENTRY_KEY);
                  }
                  if (StringUtils.isEmpty((String) entry.get(0))) {
                     throw new PeopleImportException(STATUS_EMPTY_USERID_KEY);
                  }
                  final Person person = new Person();
                  person.setUserId((String) entry.get(0));
                  person.setName((String) entry.get(1));
                  person.setEmail((String) entry.get(2));
                  person.setInitials((String) entry.get(3));
                  person.setPhone((String) entry.get(4));
                  id = (String) transactionTemplate.execute(new Callable() {
                     public Object run() throws Exception {
                    	 personDao.save(person);
                        return "" + person.getId();
                     }
                  });
                  status = STATUS_SUCCESS_KEY;

               }
               catch (DuplicateUserIdException duie){
                  status = STATUS_USERID_EXISTS_KEY;
               }
               catch (PeopleImportException ex) {
                  status = ex.getMessage();
               }
               catch (AuthorizationException e) {
                  request.setAttribute("exception", e);
                  return mapping.findForward("security/notAuthorized");
               }
               finally {
                  importForm.addResult(id,
                                       (String) entry.get(0),
                                       (String) entry.get(1),
                                       status);
               }
            }
            Logger.getLogger(ImportPeopleAction.class).debug("Importing people: filename="
                                                             +
                                                             filename +
                                                             ", fileSize=" +
                                                             fileSize +
                                                             ", contentType=" +
                                                             contentType);
         }
      }
      return new ActionForward(mapping.getInput());
   }

   protected ActionForward doExecute(ActionMapping mapping,
                                     ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {
      return null;
   }

   class PeopleImportException extends Exception {
      public PeopleImportException(String msg) {
         super(msg);
      }
   }
   
   public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}

}
