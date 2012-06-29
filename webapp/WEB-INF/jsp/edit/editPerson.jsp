<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ page import="com.technoetic.xplanner.actions.AuthenticationAction,
                 com.technoetic.xplanner.security.Authenticator,
                 com.technoetic.xplanner.security.auth.SystemAuthorizer,
                 java.util.Collection,
                 java.util.Iterator,
                 net.sf.xplanner.domain.Role,
                 com.technoetic.xplanner.security.SecurityHelper"%>
<%@ page import="com.technoetic.xplanner.security.AuthenticatorImpl"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>

<xplanner:content definition="tiles:edit" >

<bean:define id="person" name="edit/person"
	type="com.technoetic.xplanner.forms.PersonEditorForm" />
<logic:present parameter="oid">
    <tiles:put name="editMessage">person.editor.edit_prefix</tiles:put>
    <tiles:put name="titlePrefix">person.editor.edit_prefix</tiles:put>
    <tiles:put name="titleSuffix"><%=person.getName()%></tiles:put>
    <tiles:put name="showTitleSuffix">true</tiles:put>
    <tiles:put name="specialNavigation">true</tiles:put>
</logic:present>
<logic:notPresent parameter="oid">
    <tiles:put name="editMessage">person.editor.create</tiles:put>
    <tiles:put name="titlePrefix">person.editor.create</tiles:put>
    <tiles:put name="specialNavigation">true</tiles:put>
</logic:notPresent>

<%-- rewrite is necessary for cookie-less sessions --%>
<form method="post" action="<html:rewrite page="/do/edit/person"/>" name="personEditor">
  <div id="editObject">
  <logic:present parameter="oid">
    <bean:parameter id="oid" name="oid"/>
    <input type="hidden" name="oid" value='<%=oid%>'>
  </logic:present>
  <bean:parameter id="returnto" name="returnto"/>
  <input type="hidden" name="id" value='<%= person.getId() %>'>
  <input type="hidden" name="returnto" value='<%=returnto%>'>
  <table class="editor">
	  <tr>
		<th nowrap><bean:message key="person.editor.name"/> </th>
		<td class="secondColumn" ><div class="borderedInput"><div><html:text name="person" property="name"/></div></div></td>
	  </tr>
	  <tr>
		<th nowrap><bean:message key="person.editor.userid"/> </th>
		<td><div class="borderedInput"><div><html:text name="person" property="userIdentifier"/></div></div></td>
	  </tr>
	  <tr>
		<th nowrap><bean:message key="person.editor.initials"/> </th>
		<td><div class="borderedInput"><div><html:text name="person" property="initials"/></div></div></td>
	  </tr>
	  <tr>
		<th nowrap><bean:message key="person.editor.email"/> </th>
		<td><div class="borderedInput"><div><html:text name="person" property="email"/></div></div></td>
	  </tr>
	  <tr>
		<th nowrap valign="top"><bean:message key="person.editor.phone"/> </th>
		<td><div class="borderedInput"><div><html:text name="person" property="phone"/></div></div></td>
	  </tr>
      <xplanner:isUserAuthorized name="person" permission="hide">
          <tr>
              <th>
                  <b><bean:message key="person.editor.ishidden"/>:</b>
              </th>
              <td >
                  <div class="borderedInput"><div><html:select name="person" property="hidden">
                      <html:option value="true">Yes</html:option>
                      <html:option value="false">No</html:option>
                  </html:select></div></div>
              </td>
          </tr>
      </xplanner:isUserAuthorized>
      <%if (AuthenticatorImpl.getLoginModule(request) != null &&
            AuthenticatorImpl.getLoginModule(request).isCapableOfChangingPasswords()) {%>
          <tr>
            <th nowrap>
                <logic:present parameter="oid">
                    <bean:message key="person.editor.password.update"/>
                </logic:present>
                <logic:notPresent parameter="oid">
                    <bean:message key="person.editor.password.create"/>
                </logic:notPresent>
            </th>
            <td><div class="borderedInput"><div><html:password name="person" property="newPassword"/></div></div></td>
          </tr>
          <tr>
            <th nowrap>
                <logic:present parameter="oid">
                    <bean:message key="person.editor.password_confirm.update"/>
                </logic:present>
                <logic:notPresent parameter="oid">
                    <bean:message key="person.editor.password_confirm.create"/>
                </logic:notPresent>
            </th>
            <td><div class="borderedInput"><div><html:password name="person" property="newPasswordConfirm"/></div></div></td>
          </tr>
     <%}%>
   <db:useBeans id="projects" type="net.sf.xplanner.domain.Project"
        where="hidden = false" order="name" />
   <xplanner:isUserAuthorizedForAny name="projects" permissions="admin.edit.role">
   <tr>
        <td nowrap valign="top" colspan="2"><b><bean:message key="person.editor.roles"/> </b></td>
   </tr>
   <tr>
        
                <td><strong><bean:message key="person.roles.tableheading.project"/></strong></td>
                <td><strong><bean:message key="person.roles.tableheading.role"/></strong></td>
            </tr>
        <%int index = -1;%>
        <logic:iterate id="project" name="projects" type="net.sf.xplanner.domain.Project">
            <xplanner:isUserAuthorized name="project" permission="admin.edit.role">
            <tr>
                <td><bean:write name="project" property="name"/></td>
                <td>
                    <% index++; %>
                    <input type="hidden" name="projectId[<%=index%>]" value='<%=project.getId()%>'>
                    <div class="borderedInput"><div><select name="projectRole[<%=index%>]">
                        <option value="none"
                            <%= person.isRoleSelected("none", project.getId()) %>><bean:message key="person.editor.role.none"/>
                        </option>
                        <option value="viewer"
                            <%= person.isRoleSelected("viewer", project.getId()) %>><bean:message key="person.editor.role.viewer"/>
                        </option>
                        <option value="editor"
                            <%= person.isRoleSelected("editor", project.getId()) %>><bean:message key="person.editor.role.editor"/>
                        </option>
                        <option value="admin"
                            <%= person.isRoleSelected("admin", project.getId()) %>><bean:message key="person.editor.role.admin"/>
                        </option>
                    </select></div></div>
                </td>
            </tr>
            </xplanner:isUserAuthorized>
        </logic:iterate>
        
     
    </xplanner:isUserAuthorizedForAny>
      <!-- If current user is a system user then show system user checkbox -->
      <xplanner:isUserAuthorized resourceType="system" permission="sysadmin.promote">
           <tr><td>
                   <b><bean:message key="person.editor.is_sysadmin"/></b></td>
               <td><input name="systemAdmin" type="checkbox" value="true"
                   <%= person.isSysAdmin() ? "checked='checked'" : "" %>>

               </td></tr>
      </xplanner:isUserAuthorized>
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
    document.forms["personEditor"].name.focus();
</script>

</xplanner:content>
