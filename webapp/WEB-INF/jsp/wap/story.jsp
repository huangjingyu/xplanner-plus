<?xml version="1.0"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%@ page contentType="text/vnd.wap.wml" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<db:useBean id="story" type="net.sf.xplanner.domain.UserStory"
	oid='<%= new Integer(request.getParameter(\"oid\")) %>' />
<db:useBean id="iteration" type="net.sf.xplanner.domain.Iteration" 
	oid='<%= new Integer(story.getIteration().getId()) %>' />
<wml>
<card id="top" title="Story Info">
<do type="accept" label="Iteration">
  <go href="/xplanner/do/mobile/view/iteration?oid=<%=iteration.getId()%>"/>
</do>
<do type="accept" label="Project">
  <go href="/xplanner/do/mobile/view/project?oid=<%=iteration.getProject().getId()%>"/>
</do>
<p>
<i><bean:write name="story" property="name"/></i> <a href="#details">Details</a><br/>
<b>Tasks</b><br/>
<logic:iterate name="story" property="tasks" id="task" type="net.sf.xplanner.domain.Task">
<a href="/xplanner/do/mobile/view/task?oid=<%=task.getId()%>"><bean:write name="task" property="name"/></a><br/>
</logic:iterate>
</p>
</card>
<card id="details" title="Story Details">
<do type="accept" label="Story">
  <go href="#top"/>
</do>
<do type="accept" label="Iteration">
  <go href="/xplanner/do/mobile/view/iteration?oid=<%=iteration.getId()%>"/>
</do>
<do type="accept" label="Project">
  <go href="/xplanner/do/mobile/view/project?oid=<%=iteration.getProject().getId()%>"/>
</do>
<p>
<b>Name:</b> <bean:write name="story" property="name"/><br/>
<b>Description:</b> <bean:write name="story" property="description"/><br/>
</p>
</card>
</wml>