<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ page import="org.apache.struts.action.*" %>
<%@ page import="com.technoetic.xplanner.format.*" %>
<%@ page import="com.technoetic.xplanner.actions.UpdateTimeAction" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>

<!-- DEBT Do we need this page? -->
<xplanner:content>
<bean:define id="task" name="edit/task" type="net.sf.xplanner.domain.Task" />
<xplanner:contentTitle>
    XPlanner -
    <logic:present parameter="oid">
        <bean:message key="task.editor.edit_prefix"/>
        ${task.name}
    </logic:present>
    <logic:notPresent parameter="oid"><bean:message key="task.editor.create"/></logic:notPresent>
</xplanner:contentTitle>

<logic:present parameter="oid">
<xplanner:navigation oid='<%= task.getId() %>'
	type="net.sf.xplanner.domain.Task" inclusive="true"/>
</logic:present>
<logic:notPresent parameter="oid">
<xplanner:navigation oid='<%= Integer.parseInt(request.getParameter("fkey")) %>'
	type="net.sf.xplanner.domain.UserStory" inclusive="true"/>
</logic:notPresent>

<html:errors/>

<p>
    <span class="title"><bean:message key="taskestimate.title"/>: </span>
        ${task.name}
</p>

<bean:define id="actualHours" name="actualHours" toScope="page" type="Double"/>
<bean:define id="estimatedHours" name="estimatedHours" toScope="page" type="Double"/>

<form method="post" action="<html:rewrite page="/do/edit/time"/>" name="editTaskEstimate">
    <bean:parameter id="fkey" name="fkey"/>
    <input type="hidden" name="fkey" value='<%=fkey%>'>
    <bean:parameter id="returnto" name="returnto"/>
    <input type="hidden" name="returnto" value='<%=returnto%>'>
    <logic:present parameter="oid">
      <bean:parameter id="oid" name="oid"/>
      <input type="hidden" name="oid" value='<%=oid%>'>
    </logic:present>
    <table width="50%" border="0">
      <tr>
        <td><font color="red"><bean:message key="taskestimate.message"/></font></td>
      </tr>
    </table>
    <p>
    <table border="0">
    <tr>
      <td><bean:message key="taskestimate.label.hours.actual"/>:</td>
      <td align="right"><%= DecimalFormat.format(pageContext, actualHours.doubleValue()) %> <bean:message key="taskestimate.label.hours"/></td>
    </tr>
    <tr>
      <td><bean:message key="taskestimate.label.hours.estimated"/>:</td>
      <td align="right"><%= DecimalFormat.format(pageContext, estimatedHours.doubleValue()) %> <bean:message key="taskestimate.label.hours"/></td>
    </tr>
    </table>
    </p>
    <p><bean:message key="taskestimate.label.hours.new"/>:
      <input type="text" name="estimate" size="6" value='<%= DecimalFormat.format(pageContext, actualHours.doubleValue()) %>'>
      <input type="hidden" name="action" value='<%= UpdateTimeAction.UPDATE_ESTIMATE_ACTION %>' />
      <input type="submit" name="submit" value="<bean:message key="taskestimate.label.change"/>">
    </p>
</form>

  <script language="JavaScript">
    document.forms["editTaskEstimate"].estimate.focus();
  </script>



</xplanner:content>
