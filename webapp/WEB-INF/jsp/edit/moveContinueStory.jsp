<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ page import="com.technoetic.xplanner.format.DecimalFormat"%>
<%@ page import="com.technoetic.xplanner.domain.repository.ObjectRepository"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>

<xplanner:content definition="tiles:edit" >
<bean:define id="story" name="move/continue/userstory" type="com.technoetic.xplanner.forms.MoveContinueStoryForm" />

<tiles:put name="editMessage">story.editor.move_or_continue</tiles:put>
<tiles:put name="titlePrefix">story.editor.move_or_continue</tiles:put>
<tiles:put name="titleSuffix"><%=story.getName()%></tiles:put>
<tiles:put name="showTitleSuffix">true</tiles:put>
<tiles:put name="objectType">net.sf.xplanner.domain.UserStory</tiles:put>

<form name="moveContinueForm" method="post" action="<html:rewrite page="/do/move/continue/userstory"/>">
  <div id="editObject">
  <input type="hidden" name="oid" value="${story.id}">
  <input type="hidden" name="iterationId" value="${story.iterationId}">
  <input type="hidden" name="merge" value="true">
  <input type="hidden" name="fkey" value='<%= request.getParameter("fkey") %>'>
  <input type="hidden" name="action" value='<%=com.technoetic.xplanner.actions.MoveContinueStoryAction.MOVE_ACTION%>'/>
  <input type="hidden" name="returnto"
    value="/do/view/iteration?oid=${story.iterationId}">
  <table class="editor">
   <tr/>
   <tr><td colspan="2">
    <div class="borderedInput"><div><html:select name="story" property="targetIterationId">
        <xplanner:iterationOptions/>
    </html:select></div></div>
    </td></tr>
    <tr class="objecttable_buttons"><td>
    <input type="submit" name="actionBtn" id='<%= com.technoetic.xplanner.actions.MoveContinueStoryAction.MOVE_ACTION %>'
         onclick="this.form.action.value='<%= com.technoetic.xplanner.actions.MoveContinueStoryAction.MOVE_ACTION %>'"
         value="<bean:message key="form.move"/>">
    </td><td>
       <%if (!story.isFutureIteration()){%>
    <input type="submit" name="actionBtn" id='<%= com.technoetic.xplanner.actions.MoveContinueStoryAction.CONTINUE_ACTION %>'
         onclick="this.form.action.value='<%= com.technoetic.xplanner.actions.MoveContinueStoryAction.CONTINUE_ACTION %>'"
         value="<bean:message key="form.continue"/>">
       <%}
       else {%>
       &nbsp;
       <%}%>
    </td></tr>
  </table>
  </div>
</form>

<script language="JavaScript">
   select = document.moveContinueForm.targetIterationId;
   select.focus();
   if (select.selectedIndex + 1 < select.length)
      select.selectedIndex = select.selectedIndex + 1;
</script>

</xplanner:content>