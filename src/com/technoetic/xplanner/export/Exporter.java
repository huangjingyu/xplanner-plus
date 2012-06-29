package com.technoetic.xplanner.export;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.classic.Session;

public interface Exporter {
    byte[] export(Session session, Object object) throws ExportException;

    void initializeHeaders(HttpServletResponse response);
}
