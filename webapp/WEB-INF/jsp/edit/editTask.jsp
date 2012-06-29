<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ page import="org.apache.struts.*" %><%@ page import="com.technoetic.xplanner.domain.TaskDisposition"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>

<xplanner:content definition="tiles:edit" >
<bean:define id="task" name="edit/task" type="com.technoetic.xplanner.forms.TaskEditorForm" />

<logic:present parameter="oid">
    <tiles:put name="editMessage">task.editor.edit_prefix</tiles:put>
    <tiles:put name="titlePrefix">task.editor.edit_prefix</tiles:put>
    <tiles:put name="titleSuffix"><%=task.getName()%></tiles:put>
    <tiles:put name="showTitleSuffix">true</tiles:put>
    <tiles:put name="objectType">net.sf.xplanner.domain.Task</tiles:put>
</logic:present>
<logic:notPresent parameter="oid">
    <tiles:put name="editMessage">task.editor.create</tiles:put>
    <tiles:put name="titlePrefix">task.editor.create</tiles:put>
    <tiles:put name="objectType">net.sf.xplanner.domain.UserStory</tiles:put>
</logic:notPresent>

<%-- rewrite is necessary for cookie-less sessions --%>
<form action="<html:rewrite page="/do/edit/task"/>" method="POST" name="taskEditor">
  <div id="editObject">
  <logic:present parameter="oid">
    <bean:parameter id="oid" name="oid"/>
    <input type="hidden" name="oid" value='<%=oid%>'>
  </logic:present>
  <bean:parameter id="returnto" name="returnto"/>
  <input type="hidden" name="returnto" value='<%=returnto%>'>
  <bean:parameter id="fkey" name="fkey"/>
  <input type="hidden" name="fkey" value='<%=fkey%>'>
  <input type="hidden" name="userStoryId" value='<%=fkey%>'>
  <input type="hidden" name="id" value='<%= task.getId() %>'>
  <input type="hidden" name="completed" value='<%= task.isCompleted() ? "true" : "false" %>'>
  <table class="editor">
	  <tr>
		<th nowrap valign="top"><b><bean:message key="task.editor.name"/> </b></th>
		<td class="secondColumn"><div class="borderedInput"><div><html:text name="task" property="name" size="40"/></div></div></td>
	  </tr>
	  <tr>
		<th nowrap valign="top"><b><bean:message key="task.editor.type"/> </b></th>
		<td>
			<bean:define id="messages" name='<%= Globals.MESSAGES_KEY %>' scope="application"
				type="org.apache.struts.util.MessageResources" />
			<!-- DEBT(DYNAMICFIELDS) Make task type dynamic and create a html:option derived tag -->
      <div class="borderedInput"><div><html:select name="task" property="type">
				<html:option value='<%= messages.getMessage("task.type.feature") %>'>
                    <bean:message key="task.type.feature"/>
                </html:option>
				<html:option value='<%= messages.getMessage("task.type.debt") %>'>
                    <bean:message key="task.type.debt"/>
                </html:option>
				<html:option value='<%= messages.getMessage("task.type.defect") %>'>
                    <bean:message key="task.type.defect"/>
                </html:option>
				<html:option value='<%= messages.getMessage("task.type.ftest") %>'>
                    <bean:message key="task.type.ftest"/>
                </html:option>
				<html:option value='<%= messages.getMessage("task.type.atest") %>'>
                    <bean:message key="task.type.atest"/>
                </html:option>
				<html:option value='<%= messages.getMessage("task.type.overhead") %>'>
                    <bean:message key="task.type.overhead"/>
                </html:option>
			</html:select></div></div>
		</td>
	  </tr>
	  <tr>
		<th nowrap valign="top"><b><bean:message key="task.editor.disposition"/> </b></th>
		<td>
		  <div class="borderedInput"><div>
			<html:select name="task" property="dispositionName">
				<html:option value='<%= TaskDisposition.PLANNED.getName() %>'>
                    <bean:message key='<%= TaskDisposition.PLANNED.getNameKey() %>'/>
                </html:option>
				<html:option value='<%= TaskDisposition.DISCOVERED.getName() %>'>
                    <bean:message key='<%= TaskDisposition.DISCOVERED.getNameKey() %>'/>
                </html:option>
				<html:option value='<%= TaskDisposition.ADDED.getName() %>'>
                    <bean:message key='<%= TaskDisposition.ADDED.getNameKey() %>'/>
                </html:option>
				<html:option value='<%= TaskDisposition.CARRIED_OVER.getName() %>'>
                    <bean:message key='<%= TaskDisposition.CARRIED_OVER.getNameKey() %>'/>
                </html:option>
			</html:select>
			</div></div>
		</td>
	  </tr>
	  <tr>
		<th nowrap valign="top"><b><bean:message key="task.editor.acceptor"/> </b></th>
		<td>
		  <div class="borderedInput"><div>
			  <html:select property="acceptorId" name="task">
					<html:option value="0"><bean:message key="person.not_assigned"/></html:option>
					<xplanner:personOptions/>
			  </html:select>
		  </div></div>
		</td>
	  </tr>
	  <tr>
		<th nowrap valign="top"><b><bean:message key="task.editor.estimated_hours"/></b></th>
		<td><div class="borderedInput"><div><html:text name="task" property="estimatedHours" size="6"/></div></div></td>
	  </tr>
	  <tr>
		<td colspan="2" valign="top">
		<table width="100%" border="0"><tr>
			<td><b><bean:message key="task.editor.description"/></b> (<bean:message key="form.maxtext"/>)</td>
                        <td align="right">
                          <%@ include file="/WEB-INF/jsp/common/formattingHelpLink.jsp" %>
                        </td>
		</tr></table>
		</td>
	  </tr>
	  <tr>
		<td colspan="2"><div class="borderedInput"><div><html:textarea name="task" property="description" cols="60" rows="10" /></div></div></td>
	  </tr>
	  <tr>
		<td nowrap colspan="2" valign="top" class="objecttable_buttons">
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
    document.forms["taskEditor"].name.focus();
</script>

</xplanner:content>
