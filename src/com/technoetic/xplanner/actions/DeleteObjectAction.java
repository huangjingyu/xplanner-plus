package com.technoetic.xplanner.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.Note;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.domain.Identifiable;
import com.technoetic.xplanner.domain.repository.ObjectRepository;

public class DeleteObjectAction<T extends Identifiable> extends AbstractAction<T> {
    public ActionForward doExecute(ActionMapping actionMapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse reply)
            throws Exception {
        Class<Identifiable> objectClass = getObjectType(actionMapping, request);
        int objectIdentifier = Integer.parseInt(request.getParameter("oid"));
        Identifiable object = getCommonDao().getById(objectClass, objectIdentifier);
        getEventBus().publishDeleteEvent((DomainObject) object, getLoggedInUser(request));
        getCommonDao().delete(object);
        String returnto = request.getParameter("returnto");
        return returnto != null ?
                new ActionForward(returnto, true)
                : actionMapping.findForward("view/projects");
    }
}
