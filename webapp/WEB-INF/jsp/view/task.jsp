<%@page import="org.hibernate.Hibernate"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.technoetic.xplanner.actions.AbstractAction,
                 com.technoetic.xplanner.format.DecimalFormat" %>
<%--<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>--%>
<%--<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>--%>
<%--<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>--%>
<%--<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>--%>
<%--<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %> --%>
<%--<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %> --%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>

<db:useBean id="task" type="net.sf.xplanner.domain.Task" scope="request"/>
<xplanner:content definition="tiles:view">

<tiles:put name="beanName">task</tiles:put>
<tiles:put name="titlePrefix">task.prefix</tiles:put>
<tiles:put name="showTitle">false</tiles:put>

<tiles:put name="progress" direct="true">
<div class="iterationCard">
  <div class="iterationCardHeader">
    <strong><bean:message key="task.prefix"/> 
         ${task.name} [id=${task.id}]</strong>
    <xplanner:progressBar
              actual='<%= task.getActualHours() %>'
              estimate='<%= task.getActualHours() + task.getRemainingHours() %>'
              complete='<%= task.isCompleted() %>'
              width="150px" height="16"/>
  </div>
  <logic:notEmpty name="task" property="description">
	  <div class="description">
	    <xplanner:twiki name="task" property="description"/>&nbsp;
	  </div>
	  <hr class="delimeter"/>
	</logic:notEmpty>
<table class="info">
	<tr>
		<logic:notEqual name="task" property="acceptorId" value="0">
	    <th nowrap><bean:message key="task.label.acceptor"/></th>
	    <db:useBean
	        id="acceptor" type="net.sf.xplanner.domain.Person"
	        oid='<%= new Integer(task.getAcceptorId()) %>'/>
	    <td><xplanner:link page="/do/view/person" paramId="oid" paramName="acceptor" paramProperty="id">
	        ${acceptor.name}</xplanner:link></td>
		</logic:notEqual>
		<logic:equal name="task" property="acceptorId" value="0">
      <td colspan="2">&nbsp;</td>
		</logic:equal>
    <th nowrap><bean:message key="task.label.estimated_hours"/> </th>
    <td>
      <%= DecimalFormat.format(pageContext, task.getEstimatedHours()) %>
      <logic:greaterThan name="task" property="estimatedOriginalHours" value="0">
          (<%= DecimalFormat.format(pageContext, task.getEstimatedOriginalHours()) %>)
      </logic:greaterThan>
     </td>
	</tr>
	<tr>
	  <th nowrap><bean:message key="task.label.created_date"/> </th>
	  <td nowrap><xplanner:formatDate formatKey="format.date" name="task" property="createdDate"/></td>
	  <th nowrap><bean:message key="task.label.actual_hours"/> </th>
    <td><%= DecimalFormat.format(pageContext, task.getActualHours()) %></td>
	</tr>
</table>
</div>
</tiles:put>

<xplanner:isUserAuthorized name="task" permission="edit">
   
    <logic:equal name="task" property="completed" value="true">
        <form class="completeBtn" method="post" name="completion" action="<html:rewrite page="/do/edit/task"/>">
          <input type="hidden" name="oid" value='<%=task.getId()%>'/>
          <input type="hidden" name="action" value="Update"/>
          <input type="hidden" name="merge" value="true"/>
          <input type="hidden" name="returnto" value="/do/view/task?oid=<%=task.getId()%>"/>
          <input type="hidden" name="completed" value="false"/>
          <input type="submit" name="reopen" value="<bean:message key="task.label.reopen"/>"/>
        </form>
    </logic:equal>

    <logic:equal name="task" property="completed" value="false">
        <form class="completeBtn" method="post" name="completion" action="<html:rewrite page="/do/edit/task"/>">
          <input type="hidden" name="oid" value='<%=task.getId()%>'/>
          <input type="hidden" name="action" value="Update"/>
          <input type="hidden" name="merge" value="true"/>
          <input type="hidden" name="returnto" value="/do/view/task?oid=${task.id}"/>
          <input type="hidden" name="completed" value="true"/>
          <input type="submit" name="complete" value="<bean:message key="task.label.complete"/>"/>
        </form>
    </logic:equal>
    
</xplanner:isUserAuthorized>



<db:useBeans id="entries" type="net.sf.xplanner.domain.TimeEntry"
             where='<%= "task.id = " + request.getParameter("oid") %>'
             order="startTime"/>
<bean:size id="entryCount" name="entries"/>


<!-- added for RFE [ 853202 ] Simplified task time entry -->
<xplanner:isUserAuthorized name="task" permission="edit">
<xplanner:authenticatedUser id="user"/>
<logic:present name="user">
<% if (user.getId() == task.getAcceptorId()) { %>
<% int newRowCount = entries.size() + 1; %>

<script src="<html:rewrite page="/time.js"/>"></script>
<script language="Javascript">
<!--
function setTime(rowcount) {
  field = getTimeField("startTime", rowcount - 1);
  if (field.value == "") {
    field.value = formatDate(new Date(), "<bean:message key="format.datetime"/>");
    return;
  }
  field = getTimeField("endTime", rowcount - 1);
  if (field.value == "") {
    field.value = formatDate(new Date(), "<bean:message key="format.datetime"/>");
    return;
  }
}

function getTimeField(name, offset) {
  name = name + "[" + offset + "]";
  for (i = 0; i < document.timelog.length; i++) {
    if (document.timelog[i].name == name) {
      return document.timelog[i];
    }
  }
}
// -->
</script>

<!-- setting name/type directly although deprecated because otherwise "edit/time" would be set as name
and calendar-function cannot process the "/"-character -->
<%--<html:form name="timelog" type="com.technoetic.xplanner.forms.TimeEditorForm" method="post" action="edit/time">--%>
<%--  <input type="hidden" name="oid" value="${task.id}">--%>
<%--  <input type="hidden" name="returnto" value="/do/view/task?oid=${task.id}">--%>
<%--  <input type="hidden" name="fkey" value="${task.id}">--%>
<%--  <table class="objecttable" cellpadding="1">--%>
<%--    <tr>--%>
<%--      <th class="objecttable" nowrap><bean:message key="timelog.editor.start_time"/></th>--%>
<%--      <th class="objecttable" nowrap><bean:message key="timelog.editor.end_time"/></th>--%>
<%--      <th class="objecttable" nowrap><bean:message key="timelog.editor.report_date"/></th>--%>
<%--      <th class="objecttable" nowrap><bean:message key="timelog.editor.duration"/></th>--%>
<%--      <th class="objecttable"><bean:message key="timelog.editor.person1"/></th>--%>
<%--      <th class="objecttable"><bean:message key="timelog.editor.person2"/></th>--%>
<%--      <th class="objecttable"><html:img page="/images/delete.gif" alt="delete" border="0"/></th>--%>
<%--    </tr>--%>
<%--    <tr>--%>
<%--      <td class="objecttable">--%>
<%--            <input type="text" size="16" name="startTime[<bean:write name="entryCount"/>]" value="" >--%>
<%--      </td>--%>
<%--      <td class="objecttable">--%>
<%--            <input type="text" size="16" name="endTime[<bean:write name="entryCount"/>]" value="" >--%>
<%--      </td>--%>
<%--      <td class="objecttable">--%>
<%--        <input type="text" size="16" name="reportDate[<bean:write name="entryCount"/>]" value='<%= DateFormat.format(pageContext, new Date(System.currentTimeMillis())) %>' >--%>
<%--      </td>--%>
<%--      <td class="objecttable" align="right">--%>
<%--            <input type="text" size="2" name="duration[<bean:write name="entryCount"/>]" value="">--%>
<%--      </td>--%>
<%--	  <td class="objecttable">--%>
<%--		<html:select property='<%=\"person1Id[\"+entryCount.intValue()+\"]\"%>' value='<%= new Integer(user.getId()).toString() %>'>--%>
<%--			<html:option value="0">??</html:option>--%>
<%--			<xplanner:personOptions property="id" labelProperty="name" />--%>
<%--		</html:select>--%>
<%--      </td>--%>
<%--      <td class="objecttable">--%>
<%--		<html:select property='<%=\"person2Id[\"+entryCount.intValue()+\"]\"%>' value="-1">--%>
<%--			<html:option value="0">??</html:option>--%>
<%--			<xplanner:personOptions property="id" labelProperty="name" />--%>
<%--		</html:select>--%>
<%--	  </td>--%>
<%--	  <td>--%>
<%--		<input type="hidden" name="entryId[<bean:write name="entryCount"/>]" value="0">--%>
<%--		<input type="hidden" name="emptyRow[<bean:write name="entryCount"/>]" value="true"/>--%>
<%--	  </td>--%>
<%--    </tr>--%>
<%--    <tr>--%>
<%--      <td class="objecttable_buttons" nowrap colspan="7">--%>
<%--        <input type="hidden" name="action" value='<%= UpdateTimeAction.UPDATE_TIME_ACTION %>'/>--%>
<%--        <input type="submit" name="submit" value="<bean:message key="form.update"/>">&nbsp;--%>
<%--        <input type="button" name="reset" value="<bean:message key="form.reset"/>" onclick="javascript: window.location.reload()">&nbsp;--%>
<%--		<input type="button" name="action" value="<bean:message key="timelog.editor.insert_time"/>" onClick='setTime(<%=newRowCount%>)'>--%>
<%--        &nbsp;<bean:message key="timelog.editor.timeformat"/>--%>
<%--      </td>--%>
<%--    </tr>--%>
<%--  </table>--%>
<%--	<logic:greaterThan name="entryCount" value="0" >--%>
<%--	<table>--%>
<%--	<logic:iterate name="entries" scope="page" id="entry" indexId="index" type="com.technoetic.xplanner.domain.TimeEntry">--%>
<%--      <tr>--%>
<%--        <td><input type="hidden" name="startTime[<bean:write name="index"/>]" value="<xplanner:formatDate formatKey="format.datetime" name="entry" property="startTime"/>"</td>--%>
<%--        <td><input type="hidden" name="endTime[<bean:write name="index"/>]" value="<xplanner:formatDate formatKey="format.datetime" name="entry" property="endTime"/>"</td>--%>
<%--        <td><input type="hidden" name="reportDate[<bean:write name="index"/>]" value="<xplanner:formatDate formatKey="format.date" name="entry" property="reportDate"/>"</td>--%>
<%--        <td><input type="hidden" name="duration[<bean:write name="index"/>]" --%>
<%--			value="<logic:greaterThan name="entry" property="duration" value="0">  <%= DecimalFormat.format(pageContext, entry.getDuration()) %></logic:greaterThan>" </td>--%>
<%--		<td><input type="hidden" name="person1Id[<bean:write name="index"/>]" value='<%= new Integer(entry.getPerson1Id()) %>'</td>--%>
<%--		<td><input type="hidden" name="person2Id[<bean:write name="index"/>]" value='<%= new Integer(entry.getPerson2Id()) %>'</td>--%>
<%--		<input type="hidden" name="entryId[<bean:write name="index"/>]" value='<%=entry.getId()%>'>--%>
<%--      </tr>--%>
<%--  	</logic:iterate>--%>
<%--	</table>--%>
<%--	</logic:greaterThan>--%>
<%--  <input type="hidden" name="rowcount" value='<%=newRowCount%>' >--%>
<%--</html:form>--%>
<% } %>
</logic:present>
</xplanner:isUserAuthorized>
<!-- end of RFE[ 853202 ] -->


<logic:greaterThan name="entryCount" value="0">
<p class="table_label"><bean:message key="task.label.timelog"/></p>
<table id="time_entries" class="objecttable" cellpadding="1">
  <thead>
  <tr>
    <th><bean:message key="timelog.tableheading.start"/></th>
    <th><bean:message key="timelog.tableheading.end"/></th>
    <th><bean:message key="timelog.tableheading.report_date"/></th>
    <th><bean:message key="timelog.tableheading.duration"/></th>
    <th><bean:message key="timelog.tableheading.pair"/></th>
    <th><bean:message key="timelog.tableheading.description"/></th>
  </tr>
  </thead>
  <tbody>

   <logic:iterate name="entries" scope="page" id="entry" indexId="n"
                  type="net.sf.xplanner.domain.TimeEntry">
      <tr>
        <td nowrap align="center"  <%= entry.getStartTime() == null ? "bgcolor='#f0f0f0'" : "" %>>
            <xplanner:formatDate formatKey="format.datetime" name="entry" property="startTime"/></td>
        <td nowrap  <%= entry.getEndTime() == null ? "bgcolor='#f0f0f0'" : "" %>>
            <xplanner:formatDate formatKey="format.datetime" name="entry" property="endTime"/></td>
        <td nowrap align="center"  <%= entry.getReportDate() == null ? "bgcolor='#f0f0f0'" : "" %>>
            <xplanner:formatDate formatKey="format.date" name="entry" property="reportDate"/></td>
        <td nowrap  <%= entry.getDuration() == 0 ? "bgcolor='#f0f0f0'" : "" %>>
            <logic:greaterThan name="entry" property="duration" value="0">
                <%= DecimalFormat.format(pageContext, entry.getDuration()) %>
            </logic:greaterThan>
        </td>
        <td nowrap>
            <logic:notEqual name="entry" property="person1Id" value="0">
                <db:useBean
                    id="person1" type="net.sf.xplanner.domain.Person"
                    oid='<%= new Integer(entry.getPerson1Id()) %>'/>
                <xplanner:link page="/do/view/person" paramId="oid" paramName="person1" paramProperty="id">
                    ${person1.name}</xplanner:link>
            </logic:notEqual>
            <logic:notEqual name="entry" property="person2Id" value="0">
                <db:useBean
                    id="person2" type="net.sf.xplanner.domain.Person"
                    oid='<%= new Integer(entry.getPerson2Id()) %>'/>
                <xplanner:link page="/do/view/person" paramId="oid" paramName="person2" paramProperty="id">
                    ${person2.name}</xplanner:link>
            </logic:notEqual>
        </td>
        <td align="left" <logic:empty name="entry" property="description">"bgcolor='#f0f0f0'"</logic:empty>>
            <xplanner:twiki name="entry" property="description"/>
        </td>
      </tr>
  </logic:iterate>
  </tbody>
</table>
</logic:greaterThan>
<br>

<tiles:put name="actions" direct="true">
<xplanner:isUserAuthorized name="task" permission="edit">
<xplanner:actionButtons
    name="task" id="action">
    <xplanner:isUserAuthorized name="task" permission='<%=action.getPermission()%>'>
      <xplanner:link page='<%=action.getTargetPage()%>'
                     paramId="oid"
                     onclick='<%=action.getOnclick()%>'
                     fkey='<%=task.getUserStory().getId()%>'
                     paramName='<%=action.getDomainType()%>'
                     paramProperty="id"
                     useReturnto='<%=action.useReturnTo()%>'>
          <bean:message key='<%=action.getTitleKey()%>'/>
      </xplanner:link>
  </xplanner:isUserAuthorized>
</xplanner:actionButtons>
</xplanner:isUserAuthorized> 
</tiles:put>
<tiles:put name="globalActions" direct="true">
<jsp:include page="exportLinks.jsp"/>
 <xplanner:link page="/do/view/history">
    <bean:message key="history.link"/>
    <xplanner:linkParam id="oid" name="task" property="id"/>
    <xplanner:linkParam id='<%= AbstractAction.TYPE_KEY %>' value='<%= Hibernate.getClass(task).getName() %>'/>
 </xplanner:link>
</tiles:put>

</xplanner:content>
