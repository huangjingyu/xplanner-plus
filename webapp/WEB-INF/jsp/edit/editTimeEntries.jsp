<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ page import="java.text.*,java.util.*" %>
<%@ page import="com.technoetic.xplanner.actions.UpdateTimeAction" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="com.technoetic.xplanner.forms.TimeEditorForm"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>

<xplanner:content definition="tiles:edit" >
<db:useBean id="task" type="net.sf.xplanner.domain.Task"
    oid='<%= new Integer(request.getParameter("oid")) %>'/>
<bean:define id="entries" name="edit/time" type="com.technoetic.xplanner.forms.TimeEditorForm" />

<tiles:put name="editMessage">timelog.editor.edit_prefix</tiles:put>
<tiles:put name="titlePrefix">timelog.editor.edit_prefix</tiles:put>
<tiles:put name="titleSuffix"><%=task.getName()%></tiles:put>
<tiles:put name="showTitleSuffix">true</tiles:put>
<tiles:put name="objectType">net.sf.xplanner.domain.Task</tiles:put>
<script src="<html:rewrite page="/time.js"/>"></script>
<script src="<html:rewrite page="/editTimeEntries.js"/>"></script>
<script language="Javascript">
<!--

function setTime(rowcount) {
  field = getTimeField("startTime", rowcount-1);
  if (field.value == "") {
    field.value = formatDate(new Date(), "<bean:message key="format.datetime"/>");
    entryChanged();
    return;
  }
  field = getTimeField("endTime", rowcount-1);
  if (field.value == "") {
    field.value = formatDate(new Date(), "<bean:message key="format.datetime"/>");
    entryChanged();
    return;
  }
}

function getTimeField(name, offset) {
  name = name+"["+offset+"]";
  for(i=0; i<document.timelog.length; i++) {
     if (document.timelog[i].name == name) {
        return document.timelog[i];
     }
  }
}
// -->
</script>
<body>

<%-- rewrite is necessary for cookie-less sessions --%>
<form method="post" name="timelog" action="<html:rewrite page="/do/edit/time"/>">
<div id="objecttable" class="objecttable time">
  <bean:parameter id="oid" name="oid"/>
  <input type="hidden" name="oid" value='<%=oid%>'>
  <bean:parameter id="returnto" name="returnto"/>
  <input type="hidden" name="returnto" value='<%=returnto%>'>
  <bean:parameter id="fkey" name="fkey"/>
  <input type="hidden" name="fkey" value='<%=fkey%>'>
  <input type="hidden" name="taskId" value='<%=fkey%>'>
  <table>
    <thead>
    <tr>
      <th nowrap><bean:message key="timelog.editor.start_time"/></th>
      <th nowrap><bean:message key="timelog.editor.end_time"/></th>
      <th nowrap><bean:message key="timelog.editor.report_date"/></th>
      <th class="small_th" nowrap><bean:message key="timelog.editor.duration"/></th>
      <th nowrap><span id="remainingHoursLabel" style="color: black">
            <bean:message key="timelog.editor.remainingHours"/></span>
      </th>
      <th><bean:message key="timelog.editor.person1"/></th>
      <th>
      
      


	<div class="plus" style="cursor:pointer; float:left;"><html:img page="/images/plus.png" alt="delete" border="0"/></div>
    <p class="person2" style="cursor:pointer; display:none;"><bean:message key="timelog.editor.person2"/></p>
      
</th>
      <th>
      
      <bean:message key="timelog.editor.description"/></th>
      <th><html:img page="/images/delete1.png" alt="delete" border="0"/></th>
    </tr>
    </thead>
   <%
		int rowcount = entries.getRowcount();
		for (int row = 0; row < rowcount; row++) {
   %>
    <tr>
      <td class="objecttable small_th_1" <%= entries.isIntervalReadOnly(row) ? "bgcolor='#f0f0f0'" : "" %>>
        <% if (entries.isIntervalReadOnly(row)) { %>
            &nbsp;
        <% } else { %>
            <input type="text" size="12" name="startTime[<%=row%>]" value='<%= entries.getStartTime(row) %>'
            onchange='entryChanged()'/>
        <% } %>
      </td>
      <td class="objecttable small_th_2" <%= entries.isIntervalReadOnly(row) ? "bgcolor='#f0f0f0'" : "" %>>
        <% if (entries.isIntervalReadOnly(row)) { %>
            &nbsp;
        <% } else { %>
            <input type="text" size="16" name="endTime[<%=row%>]" value='<%= entries.getEndTime(row) %>'
            onchange='entryChanged()'/>
        <% } %>
      </td>
      <td class="objecttable small_th_3">
        <input type="text" size="16" name="reportDate[<%=row%>]" value='<%= entries.getReportDate(row) %>'>
      </td>
      <td class="objecttable small_th_4"  align="right">
        <% if (entries.isDurationReadOnly(row)) { %>
            <% if (!entries.isEmpty(row)) { %>
                <%= entries.getDuration(row) %>
                <input type="hidden" name="duration[<%= row %>]" value='<%= entries.getDuration(row) %>'
                    onchange='entryChanged()'/>
            <% } else { %>
                &nbsp;
            <% } %>
        <% } else { %>
            <input type="text" size="2" name="duration[<%=row%>]" onchange='entryChanged()'
                value='<%= entries.getDuration(row) == null ? "" : entries.getDuration(row).toString() %>'>
        <% } %>
      </td>
      <td class="objecttable small_th_5" align="right">
        <% if (row == rowcount - 1) { %>
        <input type="text" size="2" name="remainingHours" onChange="changeRemainingHours()"
            value='<%= task.getRemainingHours() %>'/>
        <% } else { %>
            &nbsp;
        <% } %>
      </td>
      <td class="objecttable small_th_6">
		<html:select name="entries" property='<%="person1Id["+row+"]"%>'
					 value='<%=entries.getPerson1Id(row)%>'>
			<html:option value="0">??</html:option>
			<xplanner:personOptions />
		</html:select>
      </td>
      <td class="objecttable small_th_7">
		<div class="hiden" style="display:none;"><html:select name="entries" property='<%="person2Id["+row+"]"%>'
					 value='<%=entries.getPerson2Id(row)%>'>
			<html:option value="0">??</html:option>
			<xplanner:personOptions/>
		</html:select></div>
	<script>
    $("div.plus").click(function () {
    $("p.person2").show("fast", function () {
    	$("div.plus").hide("fast", function () {
    		$("p.person2").click(function () {
    			$("div.hiden").hide("fast");
    			$("p.person2").hide("fast");
    			$("div.plus").show("fast");
			
        		})
        	});    	
        } );
    $("div.hiden").show("fast");
    });
    
    </script>

      </td>
      <td class="objecttable small_th_8">
        <html:text name="entries" property='<%="description["+row+"]"%>' size="50" maxlength="<%=String.valueOf(TimeEditorForm.MAX_DESCRIPTION_LENGTH)%>"/>
		<!--input type="text" size="50" name="description[<%//=row%>]" value='<%//=entries.getDescription(row)%>'-->
      </td>
      <td align="center"  class="objecttable small_th_9">
	    <input type="hidden" name="entryId[<%=row%>]" value='<%=entries.getEntryId(row)%>'>
        <% if (!entries.isEmpty(row)) { %>
            <input type="checkbox" name="deleted[<%=row%>]" value="true" onChange="entryChanged()"/>
        <% } else { %>
            <input type="hidden" name="emptyRow[<%=row%>]" value="true"/>
        <% } %>
    
  </td>
	


    </tr>
	<% } /* for loop */ %>
    <tr>
      <td class="objecttable_buttons" nowrap colspan="9">
        <input type="hidden" name="action" value='<%= UpdateTimeAction.UPDATE_TIME_ACTION %>'/>
        <input type="submit" name="submit" value="<bean:message key="form.update"/>">&nbsp;
        <input type="button" name="reset" value="<bean:message key="form.reset"/>" onclick="javascript: window.location.reload()">&nbsp;
		<input type="button" name="action"
			    value="<bean:message key="timelog.editor.insert_time"/>"
				onClick='setTime(<%=rowcount%>)'>
        &nbsp;<bean:message key="timelog.editor.timeformat"/>
      </td>
    </tr>
  </table>
  <input type="hidden" name="rowcount" value='<%=rowcount%>'>
  </div>
</form>

<script language="JavaScript">
document.getElementsByName('startTime[<%= rowcount-1 %>]')[0].focus();
saveRemainingHours(<%=task.getRemainingHours()%>);
saveActualHours(<%=task.getActualHours()%>);
entryChanged();
</script>

</xplanner:content>
