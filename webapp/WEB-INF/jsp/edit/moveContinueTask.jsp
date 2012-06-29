<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ page import="org.apache.struts.*,
                 org.apache.commons.lang.StringEscapeUtils" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>

<xplanner:content definition="tiles:edit" >
<bean:define id="task" name="move/continue/task"
	type="com.technoetic.xplanner.forms.MoveContinueTaskForm" />

<tiles:put name="editMessage">task.editor.move_or_continue</tiles:put>
<tiles:put name="titlePrefix">task.editor.move_or_continue</tiles:put>
<tiles:put name="titleSuffix"><%=task.getName()%></tiles:put>
<tiles:put name="showTitleSuffix">true</tiles:put>
<tiles:put name="objectType">net.sf.xplanner.domain.Task</tiles:put>

<form name="moveContinueForm" action="<html:rewrite page="/do/move/continue/task"/>" method="post">
  <div id="editObject">
  <input type="hidden" name="oid" value="${task.id}">
  <input type="hidden" name="merge" value="true">
  <input type="hidden" name="fkey" value='<%= request.getParameter("fkey") %>'>
  <input type="hidden" name="action" value='<%=com.technoetic.xplanner.actions.MoveContinueTaskAction.MOVE_ACTION%>'>
  <input type="hidden" name="returnto"
    value="/do/view/userstory?oid=${task.userStory.id}">
  <table class="editor">
   <tr/>
   <tr><td colspan="2">
    <div class="borderedInput"><div><html:select name="task" property="targetStoryId">
        <xplanner:storyOptions actualStoryId="${task.userStory.id}"/>
    </html:select></div></div>
    </td></tr>
    <tr class="objecttable_buttons"><td>
    <input type="submit" name="actionBtn" id='<%= com.technoetic.xplanner.actions.MoveContinueTaskAction.MOVE_ACTION %>'
         value="<bean:message key="form.move"/>"
         onclick="this.form.action.value='<%= com.technoetic.xplanner.actions.MoveContinueTaskAction.MOVE_ACTION %>'"/>
    </td><td>
    <input type="submit" name="actionBtn" id='<%= com.technoetic.xplanner.actions.MoveContinueTaskAction.CONTINUE_ACTION %>'
         value="<bean:message key="form.continue"/>"
         onclick="this.form.action.value='<%= com.technoetic.xplanner.actions.MoveContinueTaskAction.CONTINUE_ACTION %>'"/>
    </td></tr>
  </table>
  </div>
</form>

<script language="JavaScript">
   select = document.moveContinueForm.targetStoryId;
   select.focus();
</script>

</xplanner:content>
