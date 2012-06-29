package com.technoetic.xplanner.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.export.ExportException;
import com.technoetic.xplanner.export.Exporter;

public class ExportAction extends AbstractAction {
    private Exporter exporter;

    @Override
	protected ActionForward doExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Session session = getSession(request);
        try {
            Class objectClass = getObjectType(mapping, request);
            Object object = getCommonDao().getById(objectClass, Integer.parseInt(request.getParameter("oid")));
            byte[] data = exporter.export(session, object);
            exporter.initializeHeaders(response);
            response.getOutputStream().write(data);
        } catch (Exception ex) {
            throw new ExportException(ex);
        } finally {
            session.connection().rollback();
        }

        // Optional forward
        return mapping.findForward("display");
    }

    public void setExporter(Exporter exporter) {
        this.exporter = exporter;
    }
}
