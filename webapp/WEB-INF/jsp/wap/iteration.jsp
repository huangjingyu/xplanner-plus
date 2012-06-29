
<?xml version="1.0"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%@ page contentType="text/vnd.wap.wml" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<db:useBean id="iteration" type="net.sf.xplanner.domain.Iteration"
	oid='<%= new Integer(request.getParameter("oid")) %>' />
<wml>
<card id="top" title="Iteration Info">
<do type="accept" label="Project">
  <go href="/xplanner/do/mobile/view/project?oid=<%=iteration.getProject().getId()%>"/>
</do>
<p>
<i><bean:write name="iteration" property="name"/></i> <a href="#details">Details</a><br/>
<b>Stories</b><br/>
<logic:iterate name="iteration" property="userStories" id="story" type="net.sf.xplanner.domain.UserStory">
<a href="/xplanner/do/mobile/view/story?oid=<%=story.getId()%>"><bean:write name="story" property="name"/></a><br/>
</logic:iterate>
</p>
</card>
<card id="details" title="Iteration Details">
<do type="accept" label="Iteration">
  <go href="#top"/>
</do>
<do type="accept" label="Project">
  <go href="/xplanner/do/mobile/view/project?oid=<%=iteration.getProject().getId()%>"/>
</do>
<p>
<b>Name:</b> <bean:write name="iteration" property="name"/><br/>
<b>Start:</b> <bean:write name="iteration" property="startDate"/><br/>
<b>End:</b> <bean:write name="iteration" property="endDate"/><br/>
<b>Description:</b> <bean:write name="iteration" property="description"/><br/>
</p>
</card>
</wml>