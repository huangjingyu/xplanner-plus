<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ page import="com.technoetic.xplanner.security.auth.SystemAuthorizer,
                 com.technoetic.xplanner.security.SecurityHelper,
                 com.technoetic.xplanner.format.DecimalFormat,
                 org.apache.struts.Globals,
                 com.technoetic.xplanner.views.*,
                 net.sf.xplanner.domain.UserStory,
                 com.technoetic.xplanner.XPlannerProperties,
                 org.apache.struts.action.ActionMessages,
                 org.apache.struts.action.ActionErrors"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>
<%@ taglib uri="http://xplanner.org/displaytag.sf.net" prefix="dt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<db:useBean id="iteration" type="net.sf.xplanner.domain.Iteration" scope="request" />
<xplanner:content definition="tiles:view" >

	<script src="<html:rewrite page="/js/tooltip.js"/>" type="text/javascript"></script>

<%@include file="../layout/iterationTileParams.jsp" %>
<bean:define id="messages" name='<%=Globals.MESSAGES_KEY%>' type="org.apache.struts.util.MessageResources"/>

<logic:greaterThan name="storyCount" value="0" >
<script src="<html:rewrite page="/js/iteration.js"/>"></script>

<html:messages id="message" message="true" property='<%=ActionErrors.GLOBAL_ERROR%>' />

<form action="<html:rewrite page="/do/edit/reorderstories"/>" method="POST" name="reordering" >

<xplanner:isUserAuthorized name="iteration" permission="edit" >
	<a class="saveOrder" href="javascript:document.forms['reordering'].submit()"><bean:message key="iteration.link.save_order"/></a>

	<a class="moveMultiple" 
	href="javascript:submitMoveMultiple('<html:rewrite page="/do/move/userstories"/>')">
	<bean:message key="iteration.link.move_multiple"/></a>
</xplanner:isUserAuthorized>

<input type="hidden" name="iterationId" value="<%=iteration.getId()%>"/>
<input type="hidden" name="oid" value="<%=iteration.getId()%>"/>
<%int index = -1;
boolean hasIterationEditPermission = (SystemAuthorizer.get().hasPermission(
			iteration.getProject().getId(), SecurityHelper.getRemoteUserId(request), iteration, "edit"));
int defaultSortColumnIndex = (hasIterationEditPermission ? 4 : 2); %>

<xplanner:writableTable defaultsort="<%=defaultSortColumnIndex %>"  defaultorder="ascending" name="requestScope.iteration" id="objecttable"
        wholeCollection='<%=iteration.getUserStories() %>' permissions="edit,delete" property="userStories"
        styleClass="objecttable" requestURI="" decorator="com.technoetic.xplanner.tags.displaytag.UserStoryDecorator">
    <bean:define id="story" name="objecttable" type="net.sf.xplanner.domain.UserStory"/>
    <%
    double visualEstimation = (story.getCachedActualHours() == 0.0 && story.isCompleted()) ?
        0.0 : story.getTaskBasedRemainingHours() + story.getCachedActualHours();
    index++;
    %>

	<xplanner:isUserAuthorized name="iteration" permission="edit" >
	    <dt:column sortable="false" title='<%= messages.getMessage(IterationStoriesPage.STORIES_SELECTED_COLUMN)%>'>
	      <input type="checkbox" name="selected[<%=index%>]" value='<%=story.getId()%>'>
	    </dt:column>
    </xplanner:isUserAuthorized>

    <xplanner:actionButtonsColumn  name="story" id="action" title='<%= messages.getMessage("stories.tableheading.actions") %>' >
<%--DEBT: Remove duplication. Everything in the actionButtons should be extracted --%>
    <xplanner:isUserAuthorized name="story" permission='<%=action.getPermission()%>'>
      <xplanner:link styleClass="image" page='<%=action.getTargetPage()%>' paramId="oid" onclick='<%=action.getOnclick()%>'
                  paramName='<%=action.getDomainType()%>' paramProperty="id">
      <html:img styleClass="${action.name}" page='<%=action.getIconPath()%>' alt='<%=action.getName()%>' border="0"/></xplanner:link>
  </xplanner:isUserAuthorized>
  </xplanner:actionButtonsColumn>
    <dt:column title='<%= messages.getMessage("objects.tableheading.id") %>'>
        <xplanner:link page="/do/view/userstory" paramId="oid" paramName="story" paramProperty="id">
        ${story.id}</xplanner:link>
    </dt:column>

    <dt:column sortable="true" sortProperty="orderNo" title='<%= messages.getMessage(IterationStoriesPage.STORIES_ORDER_COLUMN)%>'>
      <c:choose>
      	<c:when test="<%=hasIterationEditPermission %>">
			<input type="text" name="orderNo[<%=index%>]" value='<%=story.getOrderNo()%>' size="2" onkeypress="return submitOnEnter(this, event)">
      	</c:when>
      	<c:otherwise>
			${story.orderNo}
      	</c:otherwise>
      </c:choose>
      <input type="hidden" name="storyId[<%=index%>]" value='<%=story.getId()%>'>
    </dt:column>

    <dt:column sortable="true" sortProperty="name" title='<%= messages.getMessage("stories.tableheading.name")%>'>
        <xplanner:link page="/do/view/userstory" styleClass="name"  paramId="oid" paramName="story" paramProperty="id">
          ${story.name}</xplanner:link>
    </dt:column>
    <dt:column sortable="true" sortProperty="priority" title='<%= messages.getMessage(IterationStoriesPage.STORIES_PRIORITY_COLUMN)%>'>
        ${story.priority}</dt:column>
    <dt:column sortable="true" sortProperty="customer.name" title='<%= messages.getMessage("stories.tableheading.customer")%>'>
        <logic:notEmpty name="story" property="customer">
                <bean:define id="customer" name="story" property="customer" type="net.sf.xplanner.domain.Person" />
				<xplanner:link page="/do/view/person" paramId="oid" paramName="customer" paramProperty="id">
					${customer.initials}
                </xplanner:link>
            </logic:notEmpty>
            <logic:empty name="story" property="customer">
                  &nbsp;
        </logic:empty>
    </dt:column>
    <dt:column sortable="true" sortProperty="percentCompleted" title='<%= messages.getMessage(IterationStoriesPage.STORIES_PROGRESS_COLUMN)%>'>
        <xplanner:progressBar
                  actual='<%= story.getCachedActualHours() %>'
                  estimate='<%= visualEstimation %>'
                  complete='<%= story.isCompleted() %>'
                  width="100%" />
    </dt:column>
    <%
      if (!"image".equals(new XPlannerProperties().getProperty("xplanner.progressbar.impl"))) { %>
      <dt:column sortable="true" sortProperty='<%=UserStory.CACHED_TASK_BASED_ACTUAL_HOURS%>' align="right"
                 title='<%= messages.getMessage(IterationStoriesPage.STORIES_ACTUAL_HOURS_COLUMN)%>'>
          <%= DecimalFormat.format(pageContext, story.getCachedActualHours()) %>
      </dt:column>
    <%}%>
    <dt:column sortable="true" sortProperty='<%=UserStory.TASK_BASED_REMAINING_HOURS%>' align="right"
               title='<%= messages.getMessage(IterationStoriesPage.STORIES_REMAINING_HOURS_COLUMN)%>'>
        <%= story.isCompleted() ? "" : DecimalFormat.format(pageContext, story.getTaskBasedRemainingHours()) %>
    </dt:column>
    <dt:column sortable="true" sortProperty='<%=UserStory.ADJUSTED_ESTIMATED_HOURS%>' align="right"
               title='<%= messages.getMessage(IterationStoriesPage.STORIES_ESTIMATED_HOURS_COLUMN)%>'>
        <span class='<%= story.getEstimatedHours() == 0 ? "highlighted_message" : "" %>'><%=
            DecimalFormat.format(pageContext, story.getAdjustedEstimatedHours()) %></span>
    </dt:column>
    <dt:column sortable="true" sortProperty='<%=UserStory.TASK_BASED_ESTIMATED_ORIGINAL_HOURS%>' align="right"
               title='<%= messages.getMessage(IterationStoriesPage.STORIES_ESTIMATED_ORIGINAL_HOURS_COLUMN)%>'>
        <span class='<%= story.getTaskBasedEstimatedOriginalHours() == 0 ? "highlighted_message" : "" %>'><%=
            DecimalFormat.format(pageContext, story.getTaskBasedEstimatedOriginalHours()) %></span>
    </dt:column>
<%--    <dt:column sortable="true" sortProperty="tasksEstimated" align="right" title='<%= messages.getMessage(IterationStoriesPage.STORIES_ITERATION_START_ESTIMATE_HOURS_COLUMN)%>'>--%>
<%--        <%= !Disposition.CARRIED_OVER.equals(story.getType()) ? "" : DecimalFormat.format(pageContext, story.getTasksEstimateAtIterationStart()) %>--%>
<%--    </dt:column>--%>
    <dt:column sortable="true" sortProperty="taskCount" align="right" title='<%= messages.getMessage("stories.tableheading.tasks")%>'>
        <span class='<%= story.getTasks().size() == 0 ? "highlighted_message" : "" %>'><%= story.getTasks().size() %></span>
    </dt:column>
    <dt:column sortable="true" sortProperty="trackerName" align="center" title='<%= messages.getMessage("stories.tableheading.tracker")%>'>
		    <logic:notEqual name="story" property="trackerId" value="0">
				<db:useBean id="tracker" type="net.sf.xplanner.domain.Person"
					oid='<%= new Integer(story.getTrackerId()) %>'/>
				<xplanner:link page="/do/view/person" paramId="oid" paramName="tracker" paramProperty="id">
					${tracker.initials}</xplanner:link>
		    </logic:notEqual>
			<logic:equal name="story" property="trackerId" value="0">
                  &nbsp;
			</logic:equal>
    </dt:column>
    <dt:column align="center" sortable="true" title='<%= messages.getMessage("stories.tableheading.disposition")%>'>
        <%=messages.getMessage(story.getDisposition().getNameKey())%>
    </dt:column>
    <dt:column align="center" sortable="true" title='<%= messages.getMessage("stories.tableheading.status")%>'>
        <%=messages.getMessage(story.getStatusEnum().getNameKey())%>
    </dt:column>
</xplanner:writableTable>
<xplanner:isUserAuthorized name="iteration" permission="edit" >
	<a class="saveOrderBottom" href="javascript:document.forms['reordering'].submit()"><bean:message key="iteration.link.save_order"/></a>
</xplanner:isUserAuthorized>
</logic:greaterThan>
</form>

<logic:equal name="storyCount" value="0" >
  <p class="highlighted_message"><bean:message key="stories.none"/></p>
</logic:equal>
	<script>
		tooltip("<%= messages.getMessage("action.edit.story")%>", "edit", $('img'));
		tooltip("<%= messages.getMessage("action.delete.story")%>", "delete", $('img'));
		tooltip("<%= messages.getMessage("action.movecontinue.story")%>", "movecontinue", $('img'));
		
		</script>

</xplanner:content>
