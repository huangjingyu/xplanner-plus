package com.technoetic.xplanner.export;

import java.io.ByteArrayOutputStream;

import javax.servlet.http.HttpServletResponse;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.mspdi.MSPDIWriter;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;

import org.hibernate.classic.Session;

public class MspdiExporter extends MpxExporter {
    @Override
	public void initializeHeaders(HttpServletResponse response) {
        response.setHeader("Content-type", "application/mspdi");
        response.setHeader("Content-disposition", "inline; filename=export.xml");
    }

    @Override
	public byte[] export(Session session, Object object) throws ExportException {
        try {
        	ProjectFile file = new ProjectFile();
            file.setAutoTaskID(true);
            file.setAutoTaskUniqueID(true);
            file.setAutoResourceID(true);
            file.setAutoResourceUniqueID(true);
            file.setAutoOutlineLevel(true);
            file.setAutoOutlineNumber(true);
            file.setAutoWBS(true);
            // Add a default calendar called "Standard"
//            file.addDefaultBaseCalendar();

            ResourceRegistry resourceRegistry = new ResourceRegistry(session.find("from person in " + Person.class), file);

            if (object instanceof Project) {
                exportProject(file, (Project) object, resourceRegistry);
            } else if (object instanceof Iteration) {
                exportIteration(file, null, (Iteration) object, resourceRegistry);
            }

            ByteArrayOutputStream data = new ByteArrayOutputStream();
            MSPDIWriter writer = new MSPDIWriter();
            writer.write(file,data);
            return data.toByteArray();
        } catch (Exception e) {
            throw new ExportException("exception during export", e);
        }
    }
}
