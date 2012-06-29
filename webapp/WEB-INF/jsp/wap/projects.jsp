<?xml version="1.0"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%@ page contentType="text/vnd.wap.wml" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<db:useBeans id="projects" type="net.sf.xplanner.domain.Project" where="hidden = false" />
<wml>
<card id="top" title="XPlanner">
<p>
<b>Projects</b><br/>
<logic:iterate name="projects" id="project" type="net.sf.xplanner.domain.Project">
<a href="/xplanner/do/mobile/view/project?oid=<%=project.getId()%>"><bean:write name="project" property="name"/></a><br/>
</logic:iterate>
</p>
</card>
</wml>