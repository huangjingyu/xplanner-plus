<%@ page import="org.apache.struts.Globals" contentType = "text/html;charset=UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<xplanner:content definition="tiles:edit" >

<bean:define id="iteration" name="edit/iteration" type="com.technoetic.xplanner.forms.IterationEditorForm" />

<logic:present parameter="oid">
    <tiles:put name="editMessage">iteration.editor.edit_prefix</tiles:put>
    <tiles:put name="titlePrefix">iteration.editor.edit_prefix</tiles:put>
    <tiles:put name="titleSuffix"><%=iteration.getName()%></tiles:put>
    <tiles:put name="showTitleSuffix">true</tiles:put>
    <tiles:put name="objectType">net.sf.xplanner.domain.Iteration</tiles:put>
</logic:present>
<logic:notPresent parameter="oid">
    <tiles:put name="editMessage">iteration.editor.create</tiles:put>
    <tiles:put name="titlePrefix">iteration.editor.create</tiles:put>
    <tiles:put name="objectType">net.sf.xplanner.domain.Project</tiles:put>
</logic:notPresent>

<%-- rewrite is necessary for cookie-less sessions --%>
<form method="post" action="<html:rewrite page="/do/edit/iteration"/>" name="iterationEditor">
  <div id="editObject">
  <logic:present parameter="oid">
    <bean:parameter id="oid" name="oid"/>
    <input type="hidden" name="oid" value='<%=oid%>'>
  </logic:present>
  <bean:parameter id="returnto" name="returnto"/>
  <input type="hidden" name="returnto" value='<%=returnto%>'>
  <bean:parameter id="fkey" name="fkey"/>
  <input type="hidden" name="fkey" value='<%=fkey%>'>
  <input type="hidden" name="projectId" value='<%=fkey%>'>
  <input type="hidden" name="id" value='<%= iteration.getId() %>'>
  <bean:define id="messages" name='<%=Globals.MESSAGES_KEY%>' type="org.apache.struts.util.MessageResources"/>
  <table class="editor">
	  <tr>
		<th nowrap valign="top"><bean:message key="iteration.editor.name"/></th>
		<td class="secondColumn"><div class="borderedInput"><div><html:text name="iteration" property="name" size="40"/></div></div></td>
	  </tr>
      <logic:present parameter="oid">
          <tr>
            <th nowrap valign="top"><b><bean:message key="iteration.editor.status"/></b></th>
            <td><%=messages.getMessage("iteration.status."+ iteration.getStatusKey())%></td>
          </tr>
      </logic:present>    
	  <tr>
		<th nowrap valign="top"><b><bean:message key="iteration.editor.start_date"/></b></th>
		<td><div class="borderedInput dateInput"><div><input type="text" id="startDate" name="startDateString" size="12"
                value="<bean:write name="iteration" property="startDateString"/>" class="dateField"/>&nbsp;
			<input type="image" src="<html:rewrite page="/images/calendar.gif" />" align="top"
                 name="setStartDate" value="<bean:message key="iteration.editor.set_date"/>"
				 onclick="return showCalendar('startDate', '<bean:message key="format.date"/>');"/></div></div></td>
	  </tr>
	  <tr>
		<th nowrap valign="top"><b><bean:message key="iteration.editor.end_date"/></b></th>
		<td><div class="borderedInput dateInput"><div><input type="text" id="endDate" name="endDateString" size="12"
                value="<bean:write name="iteration" property="endDateString" />" class="dateField"/>&nbsp;
			<input type="image" src="<html:rewrite page="/images/calendar.gif" />" align="top"
                name="setEndDate" value="<bean:message key="iteration.editor.set_date"/>"
				onclick="return showCalendar('endDate', '<bean:message key="format.date"/>');"/>
				</div></div>
		</td>
	  </tr>
    <logic:present parameter="oid">
    <tr>
      <th nowrap valign="top"><b><bean:message key="iteration.editor.days_worked"/></b></th>
      <td><div class="borderedInput"><div><input type="text" id="daysWorked" name="daysWorked" size="5"
      value="<bean:write name="iteration" property="daysWorked"/>"/></div></div>&nbsp;</td>
    </tr>
    </logic:present>
     <tr>
		<th nowrap valign="top"><b><bean:message key="iteration.editor.description"/></b></th>
        <td align="right">
          <%@ include file="/WEB-INF/jsp/common/formattingHelpLink.jsp" %>
        </td>
	  </tr>
	  <tr>
		<td colspan="2">
		  <div class="borderedInput"><div><html:textarea name="iteration" property="description" cols="60" rows="10"/></div></div>
		</td>
	  </tr>
	  <tr>
		<td colspan="2" valign="top" class="objecttable_buttons">
		  <logic:present parameter="oid">
              <input type="hidden" name="action"
                value='<%= com.technoetic.xplanner.actions.EditObjectAction.UPDATE_ACTION %>'>
			  <input type="submit" value="<bean:message key="form.update"/>">
		  </logic:present>
		  <logic:notPresent parameter="oid">
              <input type="hidden" name="action"
                value='<%= com.technoetic.xplanner.actions.EditObjectAction.CREATE_ACTION %>'>
			  <input type="submit" value="<bean:message key="form.create"/>">
		  </logic:notPresent>
		  <input type="reset" value="<bean:message key="form.reset"/>">
		</td>
	  </tr>
  </table>
  </div>
  </form>
  <script language="JavaScript">
    document.forms["iterationEditor"].name.focus();
  </script>
</xplanner:content>
