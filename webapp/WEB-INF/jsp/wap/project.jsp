<%@ page contentType="text/vnd.wap.wml" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<db:useBean id="project" type="net.sf.xplanner.domain.Project"
	oid='<%= new Integer(request.getParameter("oid")) %>'/>
<wml>
<card id="top" title="Project Info">
<p>
<i><bean:write name="project" property="name"/></i> <a href="#details">Details</a><br/>
<b>Iterations</b><br/>
<logic:notEmpty name="project" property="currentIteration">
<a href="/xplanner/do/mobile/view/iteration?oid=<%=project.getCurrentIteration().getId()%>">Current Iteration</a><br/>
</logic:notEmpty>
<logic:iterate name="project" property="iterations" id="iteration" type="net.sf.xplanner.domain.Iteration">
<a href="/xplanner/do/mobile/view/iteration?oid=<%=iteration.getId()%>"><bean:write name="iteration" property="name"/></a><br/>
</logic:iterate>
</p>
<do type="accept" label="Projects">
    <go href="/xplanner/do/mobile/view/projects"/>
</do>
</card>
<card id="details" title="Project Details">
<do type="accept" label="Project">
  <go href="#top"/>
</do>
<p>
<b>Name:</b> <bean:write name="project" property="name"/><br/>
<b>Description:</b> <bean:write name="project" property="description"/><br/>
</p>
</card>
</wml>