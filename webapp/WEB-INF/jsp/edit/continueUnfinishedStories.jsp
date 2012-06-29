<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ page import="com.technoetic.xplanner.format.DecimalFormat"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>

<xplanner:content definition="tiles:edit" >

<bean:define id="iteration" name="continue/unfinished/stories"
	type="com.technoetic.xplanner.forms.ContinueUnfinishedStoriesForm" />

<jsp:useBean id="iterationLoader" class="com.technoetic.xplanner.tags.IterationLoader" scope="request" >
   <jsp:setProperty name="iterationLoader" property="pageContext" value='<%=pageContext%>'/>
</jsp:useBean>

<tiles:put name="editMessage">iteration.continue.unfinished.stories</tiles:put>
<tiles:put name="titlePrefix">iteration.continue.unfinished.stories</tiles:put>
<tiles:put name="objectType">net.sf.xplanner.domain.Iteration</tiles:put>

<% if (iterationLoader.getIterationOptions(iteration.getProjectId(), true, iteration.getStartDate()).size() == 0) { %>
    <p class="highlighted_message"><bean:message key="iteration.status.editor.no_future_iteration"/></p>
<% } else { %>

<form id="continue" method="post" action="<html:rewrite page="/do/continue/unfinished/stories"/>">
  <div id="editObject">
  <input type="hidden" name="oid" value="${iteration.iterationId}">
  <input type="hidden" name="iterationId" value="${iteration.iterationId}">
  <input type="hidden" name="merge" value="true">
  <input type="hidden" name="fkey" value='<%= request.getParameter("fkey") %>'>
  <input type="hidden" name="returnto"
    value="/do/view/iteration?oid=${iteration.iterationId}">
  <table cellpadding="4" bgcolor="" class="editor">
   <tr/>
   <tr class=""><td colspan="2">
   <div class="borderedInput"><div><html:select name="iteration" property="targetIterationId">
        <xplanner:iterationOptions projectId='<%= iteration.getProjectId() %>' onlyCurrentProject="true" startDate='<%= iteration.getStartDate()%>' />
    </html:select></div></div>
    </td></tr>
    <tr class="objecttable_buttons"><td>
    <input type="submit" name="action" id='<%= com.technoetic.xplanner.actions.ContinueUnfinishedStoriesAction.OK_ACTION %>' value="<bean:message key="ok"/>">
    </td><td>
    <input type="submit" name="action" id='<%= com.technoetic.xplanner.actions.ContinueUnfinishedStoriesAction.CANCEL_ACTION %>' value="<bean:message key="cancel"/>">
    </td></tr>
  </table>
</div>
</form>
<% } %>
</xplanner:content>