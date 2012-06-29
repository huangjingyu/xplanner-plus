<%@ page import="org.apache.struts.util.RequestUtils,
                 org.apache.struts.Globals"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>

<xplanner:content definition="tiles:edit" >

<bean:define id="importForm" name="import" type="com.technoetic.xplanner.forms.ImportForm" />

<tiles:put name="editMessage">stories.import.editor.title</tiles:put>
<tiles:put name="titlePrefix">stories.import.editor.title</tiles:put>
<tiles:put name="objectType">net.sf.xplanner.domain.Iteration</tiles:put>

<tiles:put name="specialNavigation">false</tiles:put>

  <bean:define id="messages" name='<%=Globals.MESSAGES_KEY%>' type="org.apache.struts.util.MessageResources"/>

  <span class="help_message">
    <bean:message key="import.editor.helpMessage"/><br/><br/>
     <bean:message key="import.editor.download.template"/> <html:link page='<%= "/" + messages.getMessage("import.editor.download.template.file.name")%>'>
      <bean:message key="import.editor.download.template.location"/></html:link>
    <br/>
  </span>

  <form method="post" action="<html:rewrite page="/do/import/stories"/>" enctype="multipart/form-data" name="importForm">
    <div id="editObject">
    <bean:parameter id="returnto" name="returnto"/>
    <input type="hidden" name="returnto" value='<%=returnto%>'>
    <br/>
    <logic:present parameter="oid">
      <bean:parameter id="oid" name="oid"/>
      <input type="hidden" name="oid" value='<%=oid%>'>
    </logic:present>
    <table class="editor" >
       <tr>
         <th class="required"><bean:message key="form.required.field.indicator"/></th>
         <th nowrap ><bean:message key="import.editor.worksheetName"/></th>
         <td><html:text name="importForm" property="worksheetName"/></td>
        </tr>
       <tr>
         <th class="required"><bean:message key="form.required.field.indicator"/></th>
         <th nowrap ><b><bean:message key="import.editor.titleColumn"/></th>
         <td><html:text name="importForm" property="titleColumn"/></td>
        </tr>
       <tr>
         <th class="required"><bean:message key="form.required.field.indicator"/></th>
         <th nowrap><bean:message key="import.editor.endDateColumn"/></th>
         <td><html:text name="importForm" property="endDateColumn"/></td>
       </tr>
       <tr>
         <th class="required"><bean:message key="form.required.field.indicator"/></th>
         <th nowrap><bean:message key="import.editor.priorityColumn"/></th>
         <td><html:text name="importForm" property="priorityColumn"/></td>
       </tr>
        <tr>
          <th class="required">&nbsp;</th>
          <th nowrap><bean:message key="import.editor.estimateColumn"/></th>
          <td><html:text name="importForm" property="estimateColumn"/></td>
        </tr>
        <tr>
          <th class="required">&nbsp;</th>
          <th nowrap><bean:message key="import.editor.statusColumn"/></th>
          <td><html:text name="importForm" property="statusColumn"/></td>
        </tr>
        <tr>
          <th class="required">&nbsp;</th>
          <th nowrap><bean:message key="import.editor.onlyIncomplete"/></th>
          <td><html:checkbox name="importForm" property="onlyIncomplete"/></td>
        </tr>
        <tr>
          <th class="required">&nbsp;</th>
          <th nowrap><bean:message key="import.editor.completedStatus"/></th>
          <td><html:text name="importForm" property="completedStatus"/></td>
        </tr>
        <tr>
        <td colspan="3">&nbsp;</td>
        </tr>
         <tr>
           <th class="required"><bean:message key="form.required.field.indicator"/></th>
           <th nowrap><bean:message key="import.editor.importFile"/></th>
           <td><html:file name="importForm" property="formFile"/></td>
         </tr>
        <tr>
          <th class="required"><bean:message key="form.required.field.indicator"/></th>
          <th colspan="2"><span class="required"><bean:message key="form.required.field"/></span></th>
        </tr>
        <tr>
           <td nowrap colspan="3" class="objecttable_buttons">
             <input type="hidden" name="action"
                    value='<%= com.technoetic.xplanner.actions.EditObjectAction.UPDATE_ACTION %>'>
             <input type="submit" value="<bean:message key="import.button.label"/>">
             <input type="reset" name="reset" value="<bean:message key="form.reset"/>">
           </td>
        </tr>
    </table>
    </div>
  </form>

<% if (importForm.isSubmitted() && RequestUtils.getActionErrors(pageContext, Globals.ERROR_KEY).isEmpty()) { %>
  <p class="highlighted_message"><bean:message key="stories.import.imported_stories" arg0='<%=""+importForm.getResults().size()%>' /></p>
<% } %>
</xplanner:content>