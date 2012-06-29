<%@ page import="com.technoetic.xplanner.actions.UpdateTimeNotificationReceivers,
                 com.technoetic.xplanner.actions.EditObjectAction,
                 org.apache.struts.Globals,
                 com.technoetic.xplanner.forms.ProjectEditorForm"%>
<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>
<%@ taglib uri="http://xplanner.org/displaytag.sf.net" prefix="dt" %>



<xplanner:content definition="tiles:edit" >
<bean:define id="project" name="edit/project" type="com.technoetic.xplanner.forms.ProjectEditorForm" />
<bean:define id="messages" name='<%=Globals.MESSAGES_KEY%>' type="org.apache.struts.util.MessageResources"/>

<script language="javascript">
   function deleteNotifiedPerson(userId){
      document.forms["projectEditorForm"].action.value = "<%=UpdateTimeNotificationReceivers.DELETE%>";
      document.forms["projectEditorForm"].personToDelete.value = userId;
      document.forms["projectEditorForm"].submit();
   }
</script>

<logic:present parameter="oid">
    <tiles:put name="editMessage">project.editor.edit_prefix</tiles:put>
    <tiles:put name="titlePrefix">project.editor.edit_prefix</tiles:put>
    <tiles:put name="titleSuffix"><%=project.getName()%></tiles:put>
    <tiles:put name="showTitleSuffix">true</tiles:put>
    <tiles:put name="objectType">net.sf.xplanner.domain.Project</tiles:put>
</logic:present>
<logic:notPresent parameter="oid">
    <tiles:put name="editMessage">project.editor.create</tiles:put>
    <tiles:put name="titlePrefix">project.editor.create</tiles:put>
    <tiles:put name="specialNavigation">true</tiles:put>
</logic:notPresent>

<%-- html:rewrite is necessary for cookie-less sessions --%>
<form name="projectEditorForm" method="post" action="<html:rewrite page="/do/edit/project"/>">
  <div id="editObject" class="editProject">
    <input type="hidden" name="action" value="" />
    <input type="hidden" name="personToDelete" value="" />
	  <logic:present parameter="oid">
	    <bean:parameter id="oid" name="oid"/>
	    <input type="hidden" name="oid" value='<%=oid%>'>
	  </logic:present>
	  <bean:parameter id="returnto" name="returnto"/>
	  <input type="hidden" name="id" value='<%= project.getId() %>'>
	  <input type="hidden" name="returnto" value='<%=returnto%>'>

  <table class="editor" >
      <tr>
        <th nowrap valign="top">
			<b><bean:message key="project.editor.name"/>:</b>
        </th>
        <td class="secondColumn">
          <div class="borderedInput"><div><html:text name="project" property="name" size="40"/></div></div>
        </td>
      </tr>
      <tr>
        <th nowrap valign="top"><b><bean:message key="project.editor.description"/>: </b></th>
        <td align="right">
          <%@ include file="/WEB-INF/jsp/common/formattingHelpLink.jsp" %>
        </td>
      </tr>
      <tr>
        <td colspan="2">
          <div class="borderedInput"><div><html:textarea name="project" property="description" cols="60" rows="10"/></div></div>
        </td>
      </tr>
      <tr>
            <td colspan="2">
              <xplanner:isUserAuthorized name="project" permission="hide">
                <input name="hidden" type="checkbox" value="true"
                   <%= project.isHidden() ? "checked='checked'" : "" %> >
                <b><bean:message key="project.editor.ishidden"/></b>&nbsp;&nbsp;&nbsp;&nbsp;
              </xplanner:isUserAuthorized>
                <input name="optEscapeBrackets" type="checkbox" value="true"
                   <%= project.isOptEscapeBrackets() ? "checked='checked'" : "" %> >
                <b><bean:message key="project.editor.optEscapeBrackets"/></b>
                <p/>
               <%--TODO jm rename sendemail to isAcceptorMissingTimeEntryReminderEmailOn--%>
                <input name="sendemail" type="checkbox" value="true"
                   <%= project.isSendingMissingTimeEntryReminderToAcceptor() ? "checked='checked'" : "" %> >
                <b><bean:message key="project.editor.sendemail"/></b>
                <p/>
            </td>
      </tr>
      <tr>
            <td colspan="2">
                <b><bean:message key="project.editor.wikiurl"/></b>
                <%-- String wikiURL = new com.technoetic.xplanner.XPlannerProperties().getProperty("twiki.scheme.wiki", project, "http://");
                  request.setAttribute("twiki.scheme.wiki", wikiURL); %>
                <input name="twiki.scheme.wiki" type="text" size="50" value='<%=wikiURL%>'>  --%>
                <div class="borderedInput"><div><html:text name="project" property="wikiUrl" size="50"/></div></div>
            </td>
      </tr>

            <tr>
        <td nowrap colspan="2" class="objecttable_buttons">
          <logic:present parameter="oid">
              <input type="submit" value="<bean:message key="form.update"/>" onclick="this.form.action.value='<%= EditObjectAction.UPDATE_ACTION%>'"/>
          </logic:present>
          <logic:notPresent parameter="oid">
              <input type="submit" value="<bean:message key="form.create"/>" onclick="this.form.action.value='<%= EditObjectAction.CREATE_ACTION%>'">
          </logic:notPresent>
          <input type="reset" value="<bean:message key="form.reset"/>">
        </td>
      </tr>
  </table>

  <logic:present parameter="oid">
  <p><span class="title"><bean:message key="time_entry_notification.editor.editor_prefix"/></span></p>
                    <p><bean:message key="time_entry_notification.editor.explanation"/></p>
  <table id="remarks" class="editor" >
      <tr>
            <th nowrap><bean:message  key="time_entry_notification.editor.userToAdd"/></th>
            <td class="addRecipient">
                <div class="borderedInput"><div><html:select property="personToAddId" name="project">
            			<html:option value="0"><bean:message key="person.not_assigned"/></html:option>
            			<xplanner:personOptions/>
                </html:select></div></div>
                <div class="objecttable_buttons"><input type="submit" name="send" id="add" value="<bean:message key="form.add"/>" onclick="this.form.action.value = '<%=UpdateTimeNotificationReceivers.ADD%>'"></div>
            </td>
      </tr>
      <tr>
            <td colspan="2">
                <logic:notEmpty name="project" property="people">
                    <bean:message key="time_entry_notification.editor.receivers_list"/>
                </logic:notEmpty>
                <table>
                  <logic:notEmpty name="project" property="people">
                  <thead>
                  <tr>
                    <th ><bean:message key="projects.tableheading.actions"/></th>
                    <th nowrap><bean:message key="time_entry_notification.editor.user_name"/></th>
                  </tr>
                  </thead>
                  <tbody>
                  <logic:iterate id="personInfo" name="project" property="people">
                  <tr>
                    <td align="center">
                      <a href="#" onclick="return deleteNotifiedPerson('<%=((ProjectEditorForm.PersonInfo)personInfo).getId()%>')"><html:img page="/images/delete.png" border="0"/></a>
                    </td>
                    <td>
                      <bean:write name="personInfo" property="name"/>
                    </td>
                  </tr>
                  </logic:iterate>
                  </tbody>
                  </logic:notEmpty>
                </table>
            </td>
      </tr>
    </table>
    </logic:present>
  </div>
</form>

  <script language="JavaScript">
    document.forms["projectEditorForm"].name.focus();
  </script>

</xplanner:content>