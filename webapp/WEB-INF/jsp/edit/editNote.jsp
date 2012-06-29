<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>

<xplanner:content definition="tiles:edit" >

<bean:define id="note" name="edit/note" type="com.technoetic.xplanner.forms.NoteEditorForm" />
<logic:present parameter="oid">
    <tiles:put name="editMessage">notes.title.edit</tiles:put>
    <tiles:put name="titlePrefix">notes.title.edit</tiles:put>
    <tiles:put name="titleSuffix"><%=note.getSubject()%></tiles:put>
    <tiles:put name="showTitleSuffix">true</tiles:put>
    <tiles:put name="specialNavigation">true</tiles:put>
</logic:present>
<logic:notPresent parameter="oid">
    <tiles:put name="editMessage">notes.title.add</tiles:put>
    <tiles:put name="titlePrefix">notes.title.add</tiles:put>
    <tiles:put name="specialNavigation">true</tiles:put>
</logic:notPresent>


<%-- rewrite is necessary for cookie-less sessions --%>
<form method="post" action="<html:rewrite page="/do/edit/note"/>" enctype="multipart/form-data" name="editNote">
  <div id="editObject">
  <logic:present parameter="oid">
    <bean:parameter id="oid" name="oid"/>
    <input type="hidden" name="oid" value='<%=oid%>'>
  </logic:present>
  <input type="hidden" name="id" value='<%= note.getId() %>'>
  <bean:parameter id="returnto" name="returnto"/>
  <input type="hidden" name="returnto" value='<%=returnto%>'>
  <bean:parameter id="projectId" name="projectId"/>
  <input type="hidden" name="projectId" value='<%=projectId%>'>
  <bean:parameter id="attachedToId" name="attachedToId"/>
  <input type="hidden" name="attachedToId" value='<%= request.getParameter("attachedToId") %>'>
  <table class="editor">
      <tr>
        <th class="firstColumn" nowrap valign="top"><b><bean:message key="note.editor.subject"/></b> </th>
        <td class=""><div class="borderedInput"><div><html:text name="note" property="subject" size="40" /></div></div></td>
      </tr>
      <tr>
        <th  class="firstColumn" nowrap><b><bean:message key="note.editor.author"/></b></th>
        <td><div class="borderedInput"><div>
          <xplanner:authenticatedUser id="author"/>
          <% if (author != null) { %>
              <bean:write name="author" property="name"/> (<bean:write name="author" property="userId"/>)
              <html:hidden name="author" property="authorId"
                value='<%= Integer.toString(author.getId()) %>'/>
          <% } else { %>
              <div class="borderedInput"><div><html:select property="authorId" name="note">
                <html:option value="0"><bean:message key="person.not_assigned"/></html:option>
                <xplanner:personOptions/>
              </html:select></div></div>
          <% } %>
</div></div>
        </td>
      </tr>
      <tr>
        <th  class="firstColumn" nowrap><b><bean:message key="note.editor.body"/></b></th>
        <td align="right">
          <%@ include file="/WEB-INF/jsp/common/formattingHelpLink.jsp" %>
        </td>
      </tr>
      <tr>
        <td colspan="2"><div class="borderedInput"><div><html:textarea name="note" property="body" cols="50" rows="10" /></div></div></td>
      </tr>
      <logic:present parameter="oid">
        <tr>
          <th  class="firstColumn" nowrap><b><bean:message key="note.editor.current_attachment"/></b></th>
          <td ><div class="borderedInput"><div>
             <%= (note.getAttachedFile() != null) ? note.getAttachedFile().getName() : "None" %>
          </div></div>
		</td>
        </tr>
      </logic:present>
      <tr>
        <td  class="firstColumn" nowrap><b><bean:message key="note.editor.attachment"/></b></td>
        <td><div class="borderedInput"><div><html:file name="note" property="formFile" value='<%= request.getParameter("formFileName")%>'/></div></div></td>
      </tr>
      <tr>
        <td  nowrap colspan="2" class="objecttable_buttons">
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
          <input type="reset" name="reset" value="<bean:message key="form.reset"/>">
          </td>
      </tr>
  </table>
  </div>
</form>
<script language="JavaScript">
document.forms["editNote"].subject.focus();
</script>

<p class="links"><html:link page='<%=returnto%>'><bean:message key="form.back"/></html:link></p>

</xplanner:content>
