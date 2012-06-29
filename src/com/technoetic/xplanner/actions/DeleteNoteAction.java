package com.technoetic.xplanner.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.domain.Note;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.db.NoteHelper;
import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.domain.repository.ObjectRepository;

/**
 * Created by IntelliJ IDEA.
 * User: sg897500
 * Date: Nov 26, 2004
 * Time: 12:27:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class DeleteNoteAction extends DeleteObjectAction<Note> {

    public ActionForward doExecute(ActionMapping actionMapping, ActionForm form,
                                   HttpServletRequest request, HttpServletResponse reply)
        throws Exception {
        Note note = getCommonDao().getById(Note.class, new Integer(request.getParameter("oid")).intValue());
        getEventBus().publishDeleteEvent(note, getLoggedInUser(request));
        NoteHelper.deleteNote(note, ThreadSession.get());

        String returnto = request.getParameter("returnto");
        return returnto != null ?
               new ActionForward(returnto, true)
               : actionMapping.findForward("view/projects");
    }


}
