package com.technoetic.xplanner.actions;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.Authenticator;
import com.technoetic.xplanner.security.CredentialCookie;
import com.technoetic.xplanner.security.SecurityHelper;

public class AuthenticationAction extends Action {
    private Logger log = Logger.getLogger(getClass());
    private Authenticator authenticator;
    public static final String AUTHENTICATION_MODULE_NAME_KEY = "authentication.module.name";
   public static final String MODULE_MESSAGES_KEY = "moduleMessages";

   public void setAuthenticator(Authenticator authenticator) {
       this.authenticator = authenticator;
   }

    public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm,
                                 HttpServletRequest httpServletRequest,
                                 HttpServletResponse httpServletResponse)
            throws Exception {
        ActionForward forward = actionMapping.findForward("notAuthenticated");
        DynaActionForm form = (DynaActionForm)actionForm;
        if (StringUtils.isEmpty((String)form.get("action"))) {
            return forward;
        }
        try {
            String userId = (String)form.get("userId");
            String password = (String)form.get("password");
            authenticator.authenticate(httpServletRequest, userId, password);
            if (StringUtils.equals(httpServletRequest.getParameter("remember"), "Y")) {
                CredentialCookie credentials = new CredentialCookie(httpServletRequest, httpServletResponse);
                credentials.set(userId, password);
            }
            String savedUrl = SecurityHelper.getSavedUrl(httpServletRequest);
            if (savedUrl != null) {
                return new ActionForward(savedUrl, true);
            } else {
                forward = actionMapping.findForward("authenticated");
            }
        } catch (AuthenticationException e) {
            // Using message since text will be formatted slightly differently than the normal "error".
            log.warn(e.getMessage()+": " + e.getCause());
            ActionMessages errors = new ActionMessages();
            Map errorMap = e.getErrorsByModule();
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("login.failed"));
            for (Iterator iterator = errorMap.keySet().iterator(); iterator.hasNext();)
            {
               String moduleName =  (String) iterator.next();
               String message = (String) errorMap.get(moduleName);
               errors.add(MODULE_MESSAGES_KEY, new ActionMessage(message, moduleName));

            }
            httpServletRequest.setAttribute(Globals.MESSAGE_KEY, errors);
        }
        return forward;
    }
}
