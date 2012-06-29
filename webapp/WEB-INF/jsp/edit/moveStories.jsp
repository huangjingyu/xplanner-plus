<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<xplanner:content definition="tiles:edit" >
<bean:define id="moveStories" name="move/userstories" type="com.technoetic.xplanner.forms.MoveStoriesForm" />

<tiles:put name="editMessage">stories.move.editor.title</tiles:put>
<tiles:put name="titlePrefix">stories.move.editor.title</tiles:put>
<tiles:put name="showTitleSuffix">false</tiles:put>

<form name="moveStoriesForm" method="post" action="<html:rewrite page="/do/move/userstories"/>">
	<c:forEach var="storyId" items="<%= moveStories.getStoryIds() %>" varStatus="status">
		<input type="hidden" name="selected[${status.index}]" value="${storyId}" />
	</c:forEach>
	
	<input type="hidden" name="iterationId" value="<%= moveStories.getIterationId() %>" />
	
	<div id="editObject">
		<table class="editor">
			<tr/>
			<tr><td>
				<div class="borderedInput"><div>
				<html:select name="moveStories" property="targetIterationId">
				<xplanner:iterationOptions/>
				</html:select></div></div>
			</td></tr>
			<tr class="objecttable_buttons"><td>
				<input type="submit" name="actionBtn"
				id='<%= com.technoetic.xplanner.actions.MoveStoriesAction.MOVE_ACTION %>'
				onclick="this.form.action.value='<%= com.technoetic.xplanner.actions.MoveStoriesAction.MOVE_ACTION %>'"
				value="<bean:message key="form.move"/>">
			</td></tr>
		</table>
	</div>
</form>

</xplanner:content>