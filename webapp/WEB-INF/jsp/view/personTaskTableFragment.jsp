<%@ page import="com.technoetic.xplanner.format.DecimalFormat, 
				 java.util.Collection"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>

<bean:define id="person" name="person" scope="request" type="net.sf.xplanner.domain.Person" />
<%--
  The collection containing the relevant tasks to display,
  has been stored in request scope by the Struts action.
--%>
<bean:parameter id="collectionName" name="collectionName" />
<bean:parameter id="label" name="label" />
<bean:parameter id="label_none" name="label.none" />

<%
	Collection tasks = (Collection)request.getAttribute(collectionName);
    if (tasks.size() > 0) {
%>
<span class="title"><h1><bean:message key='<%= label %>'/></h1></span>
 <table class="objecttable" cellpadding="1" id="<%=collectionName%>">
  <thead>
	  <tr>
		<th><bean:message key="person.tableheading.iteration"/></th>
		<th><bean:message key="person.tableheading.story"/></th>
		<th><bean:message key="person.tableheading.task"/></th>
		<th><bean:message key="person.tableheading.acceptor"/></th>
		<th><bean:message key="person.tableheading.remaining"/></th>
		<th><bean:message key="person.tableheading.active"/></th>
		<th><bean:message key="person.tableheading.actions"/></th>
	  </tr>
	 </thead>
	 <tbody>
	  <logic:iterate collection='<%= tasks %>'  id="currentTask"
				    type="net.sf.xplanner.domain.Task">
         <%-- The task is reloaded so lazy collections will work. This is needed for the --%>
         <%-- remaining hours calculation. --%>
         <db:useBean id="task" oid='<%= new Integer(currentTask.getId()) %>'
            type="net.sf.xplanner.domain.Task"/>
	     <db:useBean id="story" oid='<%= new Integer(task.getUserStory().getId()) %>'
			type="net.sf.xplanner.domain.UserStory"/>
		 <db:useBean id="iteration" oid='<%= new Integer(story.getIteration().getId()) %>'
			type="net.sf.xplanner.domain.Iteration"/>
         <bean:define id="isActive" value='<%= new Boolean(task.isCurrentlyActive(person.getId())).toString() %>' />
		  <tr <%= isActive.equals("true") ? "class='active'" : ""%>>
			<td><html:link page="/do/view/iteration" paramId="oid" paramName="iteration" paramProperty="id">
				    ${iteration.name}</html:link></td>
			<td><html:link page="/do/view/userstory" paramId="oid" paramName="story" paramProperty="id">
				    ${story.name}</html:link></td>
			<td><html:link page="/do/view/task" paramId="oid" paramName="task" paramProperty="id">
				    ${task.name}</html:link></td>
			<td align="center"><%= task.getAcceptorId() == person.getId() ? "Yes" : "No" %></td>
            <td align="right"><%= DecimalFormat.format(pageContext, task.getRemainingHours()) %></td>
            <td align="center">
                <logic:equal name="isActive" value="true">
                    <bean:message key="person.task.active"/>
                </logic:equal>
            </td>
                <td  align="center">
                    <xplanner:isUserAuthorized name="task" permission="edit" projectId="${iteration.project.id}" >
                        <xplanner:link styleClass="image" page="/do/edit/time" paramId="oid" paramName="task" paramProperty="id" fkey='<%= task.getId() %>'>
                            <html:img pageKey="edittime.icon" alt="Enter Time" border="0"/></xplanner:link>
                        <xplanner:link styleClass="image" page="/do/edit/task" paramId="oid" paramName="task" paramProperty="id">
                            <html:img page="/images/edit.png" alt="edit" border="0"/></xplanner:link>
                    </xplanner:isUserAuthorized>
                </td>
		  </tr>
	  </logic:iterate>
	  </tbody>
</table>
<% } else { %>
<span class="title"><h1><bean:message key='<%= label_none %>'/></h1></span>
<% } %>
