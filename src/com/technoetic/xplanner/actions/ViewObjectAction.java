package com.technoetic.xplanner.actions;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.domain.Identifiable;
import com.technoetic.xplanner.domain.repository.ObjectRepository;

public class ViewObjectAction<T extends Identifiable> extends AbstractAction<T> {
    private boolean authorizationRequired = true;

    protected ActionForward doExecute(ActionMapping actionMapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse reply)
            throws Exception {
        Class objectClass = getObjectType(actionMapping, request);
        String forwardPath = getForwardPath(actionMapping, request);
        if (isSecure(actionMapping)) {
            Object object = getCommonDao().getById(objectClass, Integer.parseInt(request.getParameter("oid")));
            setDomainContext(request, object, actionMapping);
        }
        return new ActionForward(forwardPath);
    }

    private String getForwardPath(ActionMapping actionMapping, HttpServletRequest request)
        throws UnsupportedEncodingException {
        String forwardPath = actionMapping.findForward("display").getPath();
        String returnto = request.getParameter("returnto");
        if (returnto != null) {
            forwardPath +=
                    (forwardPath.indexOf("?") != -1 ? "&" : "?") +
                    "returnto=" + URLEncoder.encode(returnto, "UTF-8");
        }
        return forwardPath;
    }

    private boolean isSecure(ActionMapping actionMapping) {
        return authorizationRequired;
    }

    public void setAuthorizationRequired(boolean authorizationRequired) {
        this.authorizationRequired = authorizationRequired;
    }
}