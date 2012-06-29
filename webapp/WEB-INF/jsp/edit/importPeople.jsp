<%@ page import="org.apache.struts.Globals" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>

<xplanner:content definition="tiles:edit">

  <bean:define id="importPeople" name="import/people" type="com.technoetic.xplanner.forms.ImportPeopleForm"/>

  <tiles:put name="editMessage">people.import.editor.title</tiles:put>
  <tiles:put name="titlePrefix">people.import.editor.title</tiles:put>
  <tiles:put name="specialNavigation">true</tiles:put>

  <bean:define id="messages" name='<%=Globals.MESSAGES_KEY%>' type="org.apache.struts.util.MessageResources"/>
  <span class="help_message">
  <bean:message key="people.import.editor.importFileFormat"/>
  <br/>
  <bean:message key="import.editor.download.template"/> <html:link page='<%= "/" + messages.getMessage("people.import.download.template.file.name")%>'>
  <bean:message key="import.editor.download.template.location"/></html:link>
  <br/>
  </span>

  <%-- rewrite is necessary for cookie-less sessions --%>
  <form method="post"
        action="<html:rewrite page="/do/import/people"/>"
        enctype="multipart/form-data"
        name="importPeople">
    <div id="editObject">
    <bean:parameter id="returnto" name="returnto"/>
    <input type="hidden" name="returnto" value='<%=returnto%>'>
    <br/>
    <table class="editor">
      <tr>
        <th nowrap><bean:message key="import.editor.importFile"/></th>
        <td><html:file name="importPeople" property="formFile" value='<%= request.getParameter("formFileName")%>'/></td>
      </tr>
      <tr>
        <td nowrap colspan="2" class="objecttable_buttons">
          <input type="hidden" name="action"
                 value='<%= com.technoetic.xplanner.actions.EditObjectAction.UPDATE_ACTION %>'>
          <input type="submit" value="<bean:message key="import.button.label"/>">
          <input type="reset" name="reset" value="<bean:message key="form.reset"/>">
        </td>
      </tr>
    </table>
    <br/>
    </div>
  </form>
  <logic:notEmpty name="importPeople" property="results">
    <p><b><bean:message key="people.import.editor.results"/></b></p>
    <table class="objecttable" id="objecttable">
      <th><bean:message key="people.import.tableheading.lineNbr"/></th>
      <th><bean:message key="people.import.tableheading.id"/></th>
      <th><bean:message key="people.import.tableheading.loginId"/></th>
      <th><bean:message key="people.import.tableheading.name"/></th>
      <th><bean:message key="people.import.tableheading.status"/></th>
      <tbody>
        <logic:iterate id="result" property="results" name="importPeople">
          <tr>
            <td><bean:write name="result" property="lineNbr"/></td>
            <td><bean:write name="result" property="id"/></td>
            <td><bean:write name="result" property="loginId"/></td>
            <td><bean:write name="result" property="name"/></td>
            <td><bean:message name="result" property="status"/></td>
          </tr>
        </logic:iterate>
      </tbody>
    </table>
  </logic:notEmpty>
  <script language="JavaScript">
    document.forms["importPeople"].formFile.focus();
  </script>

  <p class="links"><html:link page='<%=returnto%>'><bean:message key="form.back"/></html:link></p>

</xplanner:content>
