<%@ page import="java.util.Collection"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<bean:define id="person" name="person" scope="request" type="net.sf.xplanner.domain.Person" />

<%--
  The collection containing the relevant stories to display,
  has been stored in request scope by the Struts action.
--%>
<bean:parameter id="collectionName" name="collectionName" />
<bean:parameter id="label" name="label" />
<bean:parameter id="label_none" name="label.none" />

<%
	Collection stories = (Collection)request.getAttribute(collectionName);
    if (stories.size() > 0) {
%>

<span class="title"><h1><bean:message key='<%= label %>'/></h1></span>
<table class="objecttable" id="<%=collectionName%>">
  <thead>
	  <tr>
		<th><bean:message key="person.tableheading.iteration"/></th>
		<th><bean:message key="person.tableheading.story"/></th>
        <th><bean:message key="person.tableheading.priority"/></th>
        <th><bean:message key="person.tableheading.remaining"/></th>
      </tr>
    </thead>
    <tbody>
      <logic:iterate collection='<%= stories %>' id="story" type="net.sf.xplanner.domain.UserStory">
        <tr <%= story.isStarted() ? "class='active'" : ""%>>
        	<c:set var="iteration" value="${story.iteration}"/>
            <td>
                <html:link page="/do/view/iteration" paramId="oid" paramName="iteration" paramProperty="id">
                    ${iteration.name}</html:link>
            </td>
            <td>
                <html:link page="/do/view/userstory" paramId="oid" paramName="story" paramProperty="id">
                    ${story.name}</html:link>
            </td>
            <td>${story.priority}</td>
            <td><xplanner:progressBar
                  actual='${story.cachedActualHours}'
                  estimate='<%= story.getCachedActualHours() + story.getTaskBasedRemainingHours() %>'
                  complete='<%= story.isCompleted() %>'
                  width="100%" height="8"/>
            </td>
        </tr>
      </logic:iterate>
      </tbody>
    </table>
<% } else { %>
    <span class="title"><h1><bean:message key='<%= label_none %>'/></h1></span>
<% } %>
